package sk.mkrajcovic.springai.agents.workflows;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import lombok.RequiredArgsConstructor;

/**
 * The Routing Workflow pattern is designed to implement intelligent task
 * distribution, enabling specialized handling for different types of input.
 * <p>
 * This pattern is particularly useful for complex tasks where various inputs
 * need to be processed by specialized systems or processes. The core idea is to
 * analyze the input content using an LLM and route it to the appropriate
 * specialized prompt or handler for processing.
 * <p>
 * <b>When to use</b>
 * <ul>
 * 	<li>Complex tasks with distinct categories of input</li>
 * 	<li>Specialized processing for different inputs</li>
 * 	<li>Accurate classification1</li>
 * </ul>
 * In the routing workflow pattern, an agent first analyzes the input and then
 * determines the most appropriate route by matching it to a predefined set of
 * categories or routes. This decision is made dynamically based on the content
 * of the input, and the agent then proceeds with the task using the most
 * relevant process.
 * <p>
 * This pattern is also used in Roo Code when using the Orchestrator mode.
 */
@RequiredArgsConstructor
public class RoutingWorkflow {

	private static final Logger LOG = LoggerFactory.getLogger(RoutingWorkflow.class);

	private final ChatClient chatClient;

	/**
	 * Key insights:
	 * <ul>
	 * 	<li>Task routing</li>
	 * 	<li>Dynamic classification</li>
	 * 	<li>Flexible and scalable</li>
	 * </ul>
	 */
	public void run() {
		Map<String, String> routes = Map.of(
			"billing", "You are a billing specialist. Help resolve billing issues...",
			"technical", "You are a technical support engineer. Help solve technical problems.",
			"general", "You are a customer service representative. Help with general inquiries..."
		);
		String input = "My account was charged twice last week.";
		String result = route(input, routes);

		LOG.debug("Result from chosen route: {}", result);
	}

	private String route(String input, Map<String, String> routes) {
		String route = identifyRoute(input, routes.keySet());
		String routeMessage = routes.get(route);

		return chatClient.prompt(routeMessage)
			.call()
			.content();
	}

	private String identifyRoute(String input, Set<String> availableRoutes) {
		String route = chatClient.prompt(
				"Determine the route for the input: \"" + input
				+ "\" - Available routes are: " + availableRoutes
				+ ". Just respond with the route name.")
			.call()
			.content();

		return route;
	}
}
