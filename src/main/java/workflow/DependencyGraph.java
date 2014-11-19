package workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class DependencyGraph
 * Generates a Directed Graph for tasks in the workflow
 * The graph contains an edge from Task T2 to Task T1 if T2 depends on T1
 */
public class DependencyGraph {

    /**
     * Class TaskNode
     * Represents a task in the Directed Graph
     */
    protected static class TaskNode {
        private String name;
        private List<String> dependencies;
        private String jobType;

        public TaskNode(String taskName, List<String> taskDependencies, String taskType) {
            name = taskName;
            dependencies = taskDependencies;
            jobType = taskType;
        }

        public String getName() {
            return name;
        }

        public List<String> getDependencies() {
            return dependencies;
        }

        public String getJobType() {
            return jobType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TaskNode taskNode = (TaskNode) o;

            if (dependencies != null ? !dependencies.equals(taskNode.dependencies) : taskNode.dependencies != null)
                return false;
            if (jobType != null ? !jobType.equals(taskNode.jobType) : taskNode.jobType != null) return false;
            if (name != null ? !name.equals(taskNode.name) : taskNode.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (dependencies != null ? dependencies.hashCode() : 0);
            result = 31 * result + (jobType != null ? jobType.hashCode() : 0);
            return result;
        }
    }

    private LinkedHashMap<TaskNode, List<TaskNode>> edgeMap = null;

    private List<TaskNode> vertices = null;

    public DependencyGraph() {
        edgeMap = new LinkedHashMap<TaskNode, List<TaskNode>>();
        vertices = new ArrayList<TaskNode>();
    }

    /**
     * Adds a task to the DependencyGraph
     * Adds the task to the list of vertices in the DependencyGraph
     * Also adds an edge from the task to each of its dependencies
     * @param task the task to add to the graph
     * @throws java.io.IOException if there is a problem while adding the task
     */
    protected void addTask(TaskNode task) throws IOException {
        if (exists(task)) {
            throw new WorkflowException("Error: Task '" + task.getName() + "' already added. Please use a distinct name for tasks.");
        }
        vertices.add(task);
        if (null != task.dependencies) {
            for (String dependency : task.dependencies) {
                TaskNode dependencyTask = getTaskByName(dependency);
                if (null == dependencyTask) {
                    throw new WorkflowException("Error: Task '" + task.name + "' attempting to add a dependency on unknown task '" + dependency + "'.");
                }
                else if (task.equals(dependencyTask)) {
                    throw new WorkflowException("Error: Task '" + task.name + "' attempting to add a dependency on itself. This will create a dependency loop.");
                }
                addEdge(task, dependencyTask);
            }
        }
    }

    /**
     * Check if a task has already been added to the DependencyGraph
     * @param task the TaskNode to check
     * @return true if task already exists in the DependencyGraph, false otherwise
     */
    private boolean exists(TaskNode task) {
        for (TaskNode vertex : vertices) {
            if (vertex.getName().equals(task.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an edge to the graph
     * @param from the source task in the edge
     * @param to the destination task in the edge
     */
    protected void addEdge(TaskNode from, TaskNode to) {
        if (edgeMap.containsKey(from)) {
            edgeMap.get(from).add(to);
        }
        else {
            List<TaskNode> nodes = new ArrayList<TaskNode>();
            nodes.add(to);
            edgeMap.put(from, nodes);
        }
    }

    protected List<TaskNode> getAllTasks() {
        return vertices;
    }

    protected int inDegree(TaskNode node) {
        return node.dependencies.size();
    }

    protected List<String> dependenciesOf(TaskNode node) {
        return node.dependencies;
    }

    protected TaskNode getTaskByName(String taskName) {
        for (TaskNode vertex : vertices) {
            if (vertex.name.equals(taskName)) {
                return vertex;
            }
        }
        return null;
    }
}