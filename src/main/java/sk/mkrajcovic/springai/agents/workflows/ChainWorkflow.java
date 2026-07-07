package sk.mkrajcovic.springai.agents.workflows;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import lombok.RequiredArgsConstructor;

/**
 * The Chain Workflow pattern is a powerful method for breaking down complex
 * tasks into simpler, more manageable steps.<br>
 * In this patter, each step in the workflow relies on the output of the
 * previous step, creating a sequence of actions that build upon each other.
 * <p>
 * <i>This pattern is primary and always present in coding agents like Roo code
 * and Kilo code</i>
 * <p>
 * <b>When to use chain workflow</b>
 * <ul>
 * 	<li>Tasks with clear sequential steps</li>
 * 	<li>Trading latency for accuracy - breaking down a task into smaller steps
 *      improves the overall accuracy of the result, even if it introduces some
 *      latency</li>
 * 	<li>Stepwise dependency - each step's output is used as input for the
 *      subsequent step, ensuring that the process evolves logically</li>
 * </ul>
 */
@RequiredArgsConstructor
public class ChainWorkflow {

	private static final Logger LOG = LoggerFactory.getLogger(ChainWorkflow.class);

	private final ChatClient chatClient;

	/**
	 * The code demonstrates the implementation of a Chain Workflow pattern, where
	 * a sequence of steps is executed in a predefined order, with each step
	 * building upon the output of the previous one.
	 * <p>
	 * The chain is also highly extensible as additional steps can be easily added
	 * to the workflow as needed.
	 */
	public void run() {
		String userInput = "What is 2 + 2?";

		// predefined system prompts to guide the workflow
		List<String> systemPrompts = List.of(
			"Think through it.",
			"Validate if the result is scientifically presented.",
			"Share only the output."
		);

		String chainFinalResult = chain(userInput, systemPrompts);
		LOG.debug("Chain response: {}", chainFinalResult);
	}

	private String chain(String userInput, List<String> systemPrompts) {
		String response = userInput;
		for (String systemPrompt : systemPrompts) {
			response = chatClient
				.prompt("%s\n %s".formatted(systemPrompt, response))
				.call()
				.content();
		}
		return response;
	}
}
