package sk.mkrajcovic.springai.agents.workflows;

import static java.util.concurrent.CompletableFuture.runAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The orchestrator pattern is designed to implement more complex agent-like
 * behaviors while maintaining control over the task decomposition and execution
 * process.
 * <p>
 * This pattern involves a central orchestrator that divides a task into
 * specialized subtasks, which are then handled by different workers. The
 * workers, each specialized in a specific aspect of the task, execute their
 * respective tasks concurrently, and their outputs are combined by the
 * orchestrator to produce the final result.
 * <p>
 * This pattern is ideal for scenarios where tasks can't be easily predicted up
 * front or when different perspectives or approaches are needed to address
 * various aspects of a complex problem. The orchestrator ensures that the right
 * worker handles the appropriate part of the task, and it can also manage
 * situations where multiple workers need to collaborate to achieve the final
 * outcome.
 * <p>
 * <b>When to use</b>
 * <ul>
 * 	<li>Complex tasks with unpredictable subtasks</li>
 * 	<li>Tasks requiring different approaches or perspectives</li>
 * 	<li>Adaptive problem-solving</li>
 * </ul>
 */
@RequiredArgsConstructor
public class OrchestratorWorkersWorkflow {

	private static final Logger LOG = LoggerFactory.getLogger(OrchestratorWorkersWorkflow.class);

	private static final String DOCUMENTATION_ROUTING_PROMPT = """
		There are two types of documentation for a REST API endpoint:
		- Technical documentation is intended for developers and includes details about the API's
		  functionality, parameters, and response formats.
		- User-friendly documentation is intended for end-users and includes information about how to use the API,
		  examples, and best practices.

		There are two types of workers: technical worker and user-friendly worker.
		Either one or Both the workers can be used simultaneously to generate documentation.
		- Technical worker generates technical documentation.
		- User-friendly worker generates user-friendly documentation.
		The user will provide a prompt that specifies the type of documentation they want.

		Generate only a JSON string that contains the following fields without any formatting or extra text:
		- "technical": a boolean value that indicates whether the technical documentation should be generated (never null)
		- "userFriendly": a boolean value that indicates whether the user-friendly documentation shold be generated (never null)
		The user will provide a prompt that specifies the type of documentation they want:
		""";

	private final ChatClient chatClient;

	/*
	 * The orchestrator divides the task of generating both technical
	 * and user-friendly documentation for a REST API endpoint into
	 * two separate tasks, handled by specialized workers.
	 */
	public void run() {
		var workerResponse = process("Generate both technical and user-friendly documentation for a REST API endpoint");
		LOG.debug("Worker responses: {}", workerResponse.getResponses());
		LOG.debug("Worker responses analysis: {}", workerResponse.getAnalysis());
	}

	private WorkerResponse process(String userPrompt) {
		var documentationSelection = selectDocumentation(userPrompt);
		var workerResponse = orchestrate(documentationSelection);
		return updateResponseWithAnalysis(workerResponse);
	}

	private DocumentationSelection selectDocumentation(String userPrompt) {
		return chatClient.prompt()
			.system(DOCUMENTATION_ROUTING_PROMPT)
			.user(userPrompt)
			.call()
			.entity(DocumentationSelection.class);
	}

	private WorkerResponse orchestrate(DocumentationSelection selection) {
		var workerResponse = new WorkerResponse();
		var workerTasks = new ArrayList<CompletableFuture<Void>>(2);

		if (selection.isTechnical()) {
			workerTasks.add(runAsync(() -> workerResponse.addResponse(runTechnicalWorker())));
		}
		if (selection.isUserFriendly()) {
			workerTasks.add(runAsync(() -> workerResponse.addResponse(runUserFriendlyWorker())));
		}

		// wait for all tasks to finish
		CompletableFuture.allOf(workerTasks.toArray(new CompletableFuture[0])).join();

		return workerResponse;
	}

	private String runTechnicalWorker() {
		return chatClient.prompt("Generate technical documentation for a REST API endpoint")
			.call()
			.content();
	}

	private String runUserFriendlyWorker() {
		return chatClient.prompt("Generate user-friendly documentation for a REST API endpoint")
			.call()
			.content();
	}

	private WorkerResponse updateResponseWithAnalysis(WorkerResponse workerResponse) {
		String analysis = chatClient.prompt("Analyze the response and provide a summary of the documentation generated")
			.user(String.join("\n\n", workerResponse.getResponses()))
			.call()
			.content();

		workerResponse.setAnalysis(analysis);
		return workerResponse;
	}

	@Getter
	static class DocumentationSelection {
		private boolean technical;
		private boolean userFriendly;
	}

	@Getter
	static class WorkerResponse {
		@Setter(AccessLevel.PACKAGE)
		private String analysis;
		private List<String> responses = new ArrayList<>(2);

		void addResponse(String response) {
			responses.add(response);
		}
	}
}
