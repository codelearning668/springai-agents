package sk.mkrajcovic.springai.agents.workflows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The Evaluator-Optimizer pattern introduces a dual-LLM process where two
 * separate models work in tandem to refine a solution iteratively.
 * <p>
 * One model, the generator LLM, produces initial responses, while the second
 * model, the evaluator LLM, assesses the responses and provides feedback for
 * improvement.
 * <p>
 * The iterative approach of this process makes it highly suitable for tasks
 * that require multiple rounds of critique and enhancement.
 * <p>
 * <b>When to use</b>
 * <ul>
 * 	<li>Clear evaluation criteria</li>
 * 	<li>When a task benefits from improving accuracy or clarity over multiple iterations</li>
 * 	<li>Tasks benefit from multiple rounds of critique</li>
 * </ul>
 */
@RequiredArgsConstructor
public class EvaluatorOptimizerWorkflow {

	private static final Logger LOG = LoggerFactory.getLogger(EvaluatorOptimizerWorkflow.class);

	private final ChatClient chatClient;

	/*
	 * Simulates an iterative feedback loop where a solution is generated and then
	 * evaluated to determine whether it meets the required standards. If the
	 * solution is not satisfactory, the process is repeated until the solution
	 * passes the evaluation.
	 */
	public void run() {
		var refinedResponse = loop("Create a Java class implementing a thread-safe counter");
		LOG.debug("Final solution: {}", refinedResponse.getSolution());
	}

	private RefinedResponse loop(String task) {
		var refinedResponse = new RefinedResponse();

		EvaluationResult chainOfThought = EvaluationResult.NEED_IMPROVEMENT;
		String solution = "";

		// repeat until the solution is considered acceptable
		while (chainOfThought != EvaluationResult.PASS) {
			solution = chatClient.prompt(task)
				.call()
				.content();

			// evaluate the solution to check if it needs improvement
			chainOfThought = evaluate(solution, task).getResult();
		}

		refinedResponse.setSolution(solution);

		return refinedResponse;
	}

	private EvaluationResponse evaluate(String solution, String task) {
		return chatClient.prompt("""
				Think step by step. Evaluate the following solution: '%s'
				for task: '%s'.
				Reply with PASS if solution is correct, otherwise reply with NEEDS_IMPROVEMENT.
				""".formatted(solution, task))
			.call()
			.entity(EvaluationResponse.class);
	}

	static enum EvaluationResult {
		PASS,
		NEED_IMPROVEMENT
	}

	@Setter @Getter
	static class EvaluationResponse {
		EvaluationResult result;
	}
	
	@Getter @Setter
	static class RefinedResponse {
//		private String chainOfThought;
		private String solution;
	}
}
