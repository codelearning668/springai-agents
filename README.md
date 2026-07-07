# Agentic AI - Learning Material

Learning material on Agentic AI patterns, extracted from the springai-agents project.
It serves as educational content for understanding agent-based architectures and workflow patterns.

---

## Table of Contents

1. [What is an Agent?](#what-is-an-agent)
2. [Types of Agents](#types-of-agents)
3. [Defining Concepts](#defining-concepts)
4. [Components of Agentic Systems](#components-of-agentic-systems)
5. [Architectural Patterns](#architectural-patterns)
6. [Workflow Patterns](#workflow-patterns)
   - [Chain Workflow](#chain-workflow)
   - [Parallelization Workflow](#parallelization-workflow)
   - [Routing Workflow](#routing-workflow)
   - [Orchestrator-Workers Workflow](#orchestrator-workers-workflow)
   - [Evaluator-Optimizer Workflow](#evaluator-optimizer-workflow)

---

## What is an Agent?

An agent in the context of artificial intelligence is a software component or system that acts autonomously to perform tasks, make decisions, or solve problems based on the environment or situation it encounters. Agents can perceive their environment, reason about it, and take actions to achieve a specific goal. They do not require constant human intervention to carry out their tasks.

---

## Types of Agents

| Type | Description | Examples |
|------|-------------|----------|
| **Simple Agents** | Basic agents that perform tasks by following a set of predefined rules. They work in environments where conditions are predictable and controlled. | Roo code, Kilo code |
| **Autonomous Agents** | These agents have the ability to make decisions on their own and adapt to dynamic, uncertain, or complex environments. | - |
| **Intelligent Agents** | Capable of more advanced behaviors, such as learning, reasoning, and problem-solving. They can improve their performance over time based on experience or external feedback (e.g., machine learning). | - |

---

## Defining Concepts

### Agent

An agent serves as the primary decision-maker, responsible for determining the next steps. It is driven by a language model and a prompt. The inputs include:

- **Tools**: Descriptions of available actions
- **User Input**: The overall objective or task
- **Intermediate Steps**: Action/tool output pairs executed in sequence to achieve the user's goal

The output produced by the agent specifies the next action(s) to be taken or provides the final response for the user.

### Tools

Tools are the methods that an agent can invoke. There are two key factors when utilizing tools:

1. Providing the agent with access to the appropriate tools
2. Describing these tools in a way that maximizes their usefulness

> **Note**: When building an agent, it is important to set the model temperature to 0, which ensures that the agent consistently chooses the most probable action.

---

## Components of Agentic Systems

| Component | Description |
|-----------|-------------|
| **Models** | At the heart of any agent is its model, which serves as the core intelligence, enabling the agent to reason, make decisions, and process various types of information. |
| **Tools** | They provide the interface through which agents access external systems or carry out operations beyond their internal model. |
| **Knowledge and Memory** | Knowledge and memory components are essential for augmenting the agent's capabilities beyond its initial training data. *(Note: Coding agents like Roo/Kilo code do not use any such store)* |
| **Guardrails** | Guardrails are critical for ensuring that agents behave safely, ethically and consistently, particularly in real-world applications. These mechanisms are designed to prevent agents from engaging in unsafe, irrelevant, or harmful actions. *(Roo/Kilo code allows File restrictions per mode and auto-approve gates; there is no additional LLM-level content filtering)* |
| **Orchestration** | Orchestration tools are essential for monitoring and refining the performance of agents over time. |

---

## Architectural Patterns

Frameworks like LangChain4j are commonly used for building these systems, but they do not currently support high-level abstractions like "agents" found in AutoGen or CrewAI, which are specifically designed for building multi-agent systems.

In the context of building agent-based AI systems, there are three primary architectural patterns that can be utilized:

### 1. Workflows

Ideal for tasks that require predictability, consistency, and strict control over the execution process. By using workflows, developers ensure that the agent follows a set sequence of operations, making it suitable for well-understood tasks like data retrieval, file searching, and system operations that don't require decision-making flexibility.

### 2. Agents

Unlike workflows, agents are dynamic systems where LLMs autonomously direct their own processes and choose the tools they need based on the context or input. The agent system excels in situations where tasks are not strictly defined in advance and require ongoing reasoning and decision-making.

> The `springai-tool-calls` project is an example of this pattern.

### 3. Hybrid Systems

Hybrid systems are useful in situations where certain parts of a task can be well-defined and controlled through workflows, but other parts may require the agent to autonomously decide on the best course of action based on the available information.

> The Roo and Kilo Code agents are such hybrids.

---

## Workflow Patterns

Workflow patterns are structured approaches to organizing and executing AI-driven tasks. In the Spring AI Agents project, workflows implement agent behaviors while maintaining explicit control over execution flow—unlike pure agents where the LLM autonomously decides the next step.

### Chain Workflow

The Chain Workflow pattern breaks down complex tasks into simpler, sequential steps where each step's output becomes the next step's input.

#### Pattern Characteristics
- **Control Flow**: Explicitly defined sequence of operations
- **State Management**: Each step's output flows directly to the next step
- **Predictability**: Same input always follows the same execution path

#### When to Use
- Tasks with clear sequential dependencies
- When accuracy improvement through step-by-step reasoning is valued over speed
- Coding agents like Roo code and Kilo code use this pattern as their primary execution model

#### Example
A simple arithmetic task broken down into:
1. Think through it
2. Validate if the result is scientifically presented
3. Share only the output

---

### Parallelization Workflow

The Parallelization Workflow pattern leverages concurrent processing to handle multiple independent tasks simultaneously, improving efficiency and reducing processing time.

#### Pattern Characteristics
- **Concurrency**: Multiple tasks execute simultaneously using parallel streams
- **Independence**: Each task operates without dependencies on others
- **Aggregation**: Results are combined to form the final output

#### When to Use
- Large volumes of similar but independent tasks
- Tasks requiring multiple perspectives or analyses
- When throughput is more important than individual response time

#### Example Use Case
Analyzing market changes impact across multiple stakeholder groups (Customers, Employees, Investors, Suppliers) simultaneously.

---

### Routing Workflow

The Routing Workflow pattern implements intelligent task distribution by analyzing input content and routing it to specialized handlers based on the detected category.

#### Pattern Characteristics
- **Dynamic Classification**: LLM analyzes input and determines the appropriate route
- **Specialized Handlers**: Different routes use different prompts or processing logic
- **Extensibility**: New routes can be added without modifying existing logic

#### When to Use
- Complex tasks with distinct categories of input
- When different input types require different processing approaches
- When accurate classification is critical for correct output

#### Example Use Case
Customer service inquiry routing:
- Billing issues → Billing specialist
- Technical problems → Technical support engineer
- General inquiries → Customer service representative

> This pattern is also used in Roo Code when using the Orchestrator mode.

---

### Orchestrator-Workers Workflow

The Orchestrator-Workers pattern implements complex agent-like behaviors by having a central orchestrator decompose tasks into specialized subtasks handled by concurrent workers.

#### Pattern Characteristics
- **Decomposition**: Orchestrator divides complex tasks into subtasks
- **Specialization**: Each worker handles a specific aspect of the task
- **Concurrency**: Workers execute in parallel
- **Aggregation**: Orchestrator combines results and may perform final analysis

#### When to Use
- Complex tasks with unpredictable subtasks that emerge during execution
- Tasks requiring different approaches or perspectives
- When adaptive problem-solving is needed

#### Example Use Case
Generating both technical and user-friendly documentation for a REST API endpoint using specialized workers for each type, with an orchestrator deciding which documentation types are needed based on user input.

---

### Evaluator-Optimizer Workflow

The Evaluator-Optimizer pattern introduces a dual-LLM process where one model generates solutions and another evaluates them, creating an iterative refinement loop.

#### Pattern Characteristics
- **Dual-LLM Architecture**: Separate models for generation and evaluation
- **Feedback Loop**: Evaluation results drive subsequent generations
- **Termination Condition**: Process continues until solution passes evaluation

#### When to Use
- Tasks with clear evaluation criteria
- When accuracy or quality must be improved through iteration
- Tasks that benefit from multiple rounds of critique and refinement

#### Example Use Case
Creating a Java class implementing a thread-safe counter, with iterative refinement based on evaluation feedback until the solution passes all quality checks.

---

## Summary

| Pattern | Best For | Key Mechanism |
|---------|----------|---------------|
| **Chain** | Sequential tasks | Each step builds on previous output |
| **Parallelization** | Independent tasks | Concurrent processing of subtasks |
| **Routing** | Categorized inputs | Dynamic classification and routing |
| **Orchestrator** | Complex multi-faceted tasks | Central coordination of specialized workers |
| **Evaluator-Optimizer** | Iterative refinement | Dual-LLM critique and improvement loop |

---

*This learning material is derived from the Spring AI Agents project and serves as educational content for understanding agentic AI patterns.*
