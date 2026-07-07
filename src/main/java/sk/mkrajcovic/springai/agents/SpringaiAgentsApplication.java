package sk.mkrajcovic.springai.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sk.mkrajcovic.springai.agents.workflows.EvaluatorOptimizerWorkflow;

/**
 * <h1>Agentic AI</h1><br>
 * An agent in the context of artificial intelligence is a software component or
 * system that acts autonomously to perform tasks, make decisions, or solve
 * problems based on the environment or situation it encounters. Agents can
 * perceive their environment, reason about it, and take actions to achieve a
 * specific goal. They do not require constant human intervention to carry out
 * their tasks.
 * <p>
 * <h1>Types of Agents</h1><br>
 * <ul>
 * <li><b>Simple agents:</b> 
 *     Basic agents that perform tasks by following a set of predefined rules.
 *     They often work in environments where conditions are predictable and
 *     controlled. (e.g. Roo code, Kilo code, etc.)</li>
 * <li><b>Autonomous agents:</b>
 *     These agents have the ability to make decisions on their own and adapt to
 *     dynamic, uncertain, or complex environments.</li>
 * <li><b>Intelligent agents:</b>
 *     Are capable of more advanced behaviors, such as learning, reasoning, and
 *     problem-solving. They can improve their performance over time based on 
 *     experience or external feedback (e.g. machine learning)</li>
 * </ul>
 * <p>
 * <h1>Defining Concepts</h1><br>
 * <b>Agent:</b><br> An agent serves as the primary decision-maker, responsible for
 *     determining the next steps. It is driven by a language model and a
 *     prompt. The inputs include: Tools (descriptions, available actions), 
 *     User Input (The overall objective or task), Intermediate Steps (action,
 *     tool output) pairs executed in sequence to achieve the user's goal.<br>
 *     The output produced by the agent specifies the next action(s) to be
 *     taken or provide the final response for the user.<br>
 * <b>Tools:</b><br> Tools are the methods that an agent can invoke. There are two
 *     key factors when utilizing tools:
 *     <ul>
 *     	<li>Providing the agent with access tot the appropriate tools</li>
 *    	<li>Describing these tools in a way that maximizes their usefulness</li>
 *     </ul>
 * <p>
 * <u>Note:</u> When building an agent, it is important to set the model
 * temperature to 0, which ensures that the agent consistently chooses the most
 * probable action.
 * <p>
 * <h1>Components of Agentic Systems</h1><br>
 * <ul>
 * 	<li><b>Models:</b> At the heart of any agent is its model, which serves as the
 *     core intelligence, enabling the agent to reason, make decisions, and process
 *     various types of information.</li>
 * 	<li><b>Tools:</b> They provide the interface through which agents access external
 *     systems or carry out operations beyond their internal model.</li>
 * 	<li><b>Knowledge and Memory:</b> Knowledge and memory components are essential for
 *     augmenting the agent's capabilities beyond its initial training data.
 *     (coding agents like Roo/Kilo code does not use any such store)</li>
 * 	<li><b>Guardrails:</b> Guardrails are critical for ensuring that agents behave
 *     safely, ethically and consistently, particularly in real-world applications.
 *     These mechanisms are designed to prevent agents from engaging in unsafe,
 *     irrelevant, or harmful actions. (Roo/Kilo code allows File restrictions per mode
 *     and auto-approve gates; there is no additional LLM-level content filtering)</li>
 * 	<li><b>Orchestration:</b> Orchestration tools are essential for monitoring and
 *     refining the performance of agents over time.</li>
 * </ul>
 */
@SpringBootApplication
public class SpringaiAgentsApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SpringaiAgentsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringaiAgentsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ChatClient chatClient) {
		return (args -> {
//			new ChainWorkflow(chatClient).run();
//			new ParallelizationWorkflow(chatClient).run();
//			new RoutingWorkflow(chatClient).run();
//			new OrchestratorWorkersWorkflow(chatClient).run();
			new EvaluatorOptimizerWorkflow(chatClient).run();
		});
	}

	@Bean
	ChatClient chatClient(ChatModel chatModel) {
		LOG.info("auto-configured model: " + chatModel.getOptions().getModel());
		return ChatClient.builder(chatModel)
			.defaultAdvisors(SimpleLoggerAdvisor.builder().build())
			.build();
	}
}
