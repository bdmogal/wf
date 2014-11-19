package workflow;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class WorkflowGenerator
 * Takes a DependencyGraph and generates a workflow composed of a list of stages.
 * Each stage contains a list of tasks whose dependencies have been satisfied and can be executed in parallel.
 */
public class WorkflowGenerator {

    private DependencyGraph dependencyGraph = null;
    private List<Stage> workflow = null;

    protected WorkflowGenerator(DependencyGraph graph) {
        dependencyGraph = graph;
        workflow = new ArrayList<Stage>();
    }

    /**
     * Generates a workflow for a given DependencyGraph
     */
    protected void generate() {
        while (moreTasksToSchedule()) {
            Stage nextStage = new Stage(workflow.size() + 1);
            for (DependencyGraph.TaskNode node : getUnscheduledTasks()) {
                if (dependenciesSatisfied(node)) {
                    schedule(node, nextStage);
                }
            }
            workflow.add(nextStage);
        }
    }

    /**
     * Check if all dependencies for a given task have been scheduled already in prior stages in this workflow
     * @param node the task whose dependencies are to be checked
     * @return true if all dependencies have been scheduled already in prior stages, false otherwise
     */
    private boolean dependenciesSatisfied(DependencyGraph.TaskNode node) {
        List<String> dependencies = dependencyGraph.dependenciesOf(node);
        if (dependencies != null) {
            for (String dependency : dependencies) {
                DependencyGraph.TaskNode dependencyNode = dependencyGraph.getTaskByName(dependency);
                if (!isScheduled(dependencyNode)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if a task has been scheduled in a prior stage in this workflow
     * @param node the task to check
     * @return true if the task has been scheduled, false otherwise
     */
    private boolean isScheduled(DependencyGraph.TaskNode node) {
        for (Stage stage : workflow) {
            if (stage.containsJob(node)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Schedule a task by adding it to the current (next) stage of the workflow
     * @param task the task to schedule
     * @param stage the stage to schedule the task in
     */
    private void schedule(DependencyGraph.TaskNode task, Stage stage) {
        stage.addJob(task);
    }

    /**
     * Get all the tasks scheduled in stages prior to the current stage
     * @return list of tasks scheduled in prior stages
     */
    private List<DependencyGraph.TaskNode> getScheduledTasks() {
        List<DependencyGraph.TaskNode> scheduled = new ArrayList<DependencyGraph.TaskNode>();
        for (Stage stage : workflow) {
            scheduled.addAll(stage.getAllJobs());
        }
        return scheduled;
    }

    /**
     * Return tasks that have not been scheduled so far
     * @return list of tasks that have not been scheduled so far
     */
    private List<DependencyGraph.TaskNode> getUnscheduledTasks() {
        List<DependencyGraph.TaskNode> allTasks = dependencyGraph.getAllTasks();
        List<DependencyGraph.TaskNode> scheduled = getScheduledTasks();
        List<DependencyGraph.TaskNode> unscheduled = new ArrayList<DependencyGraph.TaskNode>();
        for (DependencyGraph.TaskNode task : allTasks) {
            if (!scheduled.contains(task)) {
                unscheduled.add(task);
            }
        }
        return unscheduled;
    }

    /**
     * Check if there are pending/unscheduled tasks
     * @return true if there are more unscheduled tasks, false otherwise
     */
    private boolean moreTasksToSchedule() {
        return getUnscheduledTasks().size() > 0;
    }

    /**
     * Print a workflow to a PrintStream
     * @param out the PrintStream to print to
     */
    protected void print(PrintStream out) {
        for (Stage stage : workflow) {
            stage.print(out);
            out.println();
        }
    }

    /**
     * Getter intended to be visible only for testing
     * @return generated workflow
     */
    protected List<Stage> getWorkflow() {
        return workflow;
    }
}