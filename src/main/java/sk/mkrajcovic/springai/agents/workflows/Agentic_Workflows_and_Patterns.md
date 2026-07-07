# Agent Architectural Patterns

Frameworks like LangChain4j are commonly used for building these systems, but they do not currently support
high-level abstractions like "agents" found in AutoGen or CrewAI, which are specifically designed for
building multi-agent systems.

In the context of building agent-based AI systems, there are three primary architectural patterns that can be utilized:
Workflows, Agents, and a combination of both, known as Hybrid Systems.

---

## 1. Workflows

Ideal for tasks that require predictability, consistency, and strict control over the execution process.
By using workflows, developers ensure that the agent follows a set sequence of operations, making it suitable for
well-understood tasks like data retrieval, file searching, and system operations that don't require
decision-making flexibility.

---

## 2. Agents

Unlike workflows, agents are dynamic systems where LLMs autonomously direct their own processes and choose
the tools they need based on the context or input. The agent system excels in situations where tasks are not
strictly defined in advance and require ongoing reasoning and decision-making.

> The `springai-tool-calls` project is an example of this pattern.

---

## 3. Hybrid Systems

Hybrid systems are useful in situations where certain parts of a task can be well-defined and controlled through
workflows, but other parts may require the agent to autonomously decide on the best course of action based on
the available information.

> The Roo and Kilo Code agents are such hybrids.
