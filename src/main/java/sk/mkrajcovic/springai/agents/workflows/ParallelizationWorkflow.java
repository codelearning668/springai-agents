package sk.mkrajcovic.springai.agents.workflows;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import lombok.RequiredArgsConstructor;

/**
 * The Parallelization Workflow pattern leverages concurrent processing to
 * handle multiple tasks simultaneously, improving efficiency and reducing
 * processing time.
 * <p>
 * This pattern is particularly useful in scenarios when large volumes of
 * similar but independent tasks need to be processed or when tasks require
 * multiple perspectives.
 * <p>
 * There are two main variations of parallelization:
 * <ul>
 * 	<li><b>Sectioning:</b> A task is broken down into smaller, independent
 *     subtasks that can be processed in parallel.</li>
 * 	<li><b>Voting:</b> Multiple instances of the same task are run
 *     simultaneously, and the outputs are aggregated to form a final
 *     result based on consensus.</li>
 * </ul>
 */
@RequiredArgsConstructor
public class ParallelizationWorkflow {

	private static final Logger LOG = LoggerFactory.getLogger(ParallelizationWorkflow.class);

	private final ChatClient chatClient;

	public void run() {
		String prompt = "Analyze how market changes will impact %s stakeholder group.";
		List<String> stakeholders = List.of(
			"Customers",
			"Employees",
			"Investors",
			"Suppliers"
		);
		List<String> marketChangeAnalysis = parallel(prompt, stakeholders);
		LOG.debug("Complete maket change analysis from parallel processing of all stakeholders: {}", marketChangeAnalysis);
	}

	private List<String> parallel(String prompt, List<String> stakeholders) {
		return stakeholders.parallelStream()
			.map(stakeholder -> chatClient
				.prompt(prompt.formatted(stakeholder))
				.call()
				.content())
			.toList();
	}

}
