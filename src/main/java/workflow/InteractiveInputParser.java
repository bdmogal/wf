package workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Class InteractiveInputParser
 * Implements InputParser to read user input interactively and parse it into a DependencyGraph
 */
public class InteractiveInputParser implements InputParser {

    /**
     * Read input interactively from a user and parse it into a DependencyGraph
     * @param inputSource the source for input data. This is not used in InteractiveInputParser.
     * @return a DependencyGraph from interactive user input
     * @throws IOException if there is a problem while reading input interactively from the console
     */
    @Override
    public DependencyGraph parse(String inputSource) throws IOException {
        System.out.print("Enter number of tasks: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DependencyGraph graph = new DependencyGraph();
        int numTasks = 0;
        try {
            numTasks = Integer.parseInt(br.readLine());
            for (int i = 0; i < numTasks; i++) {
                readTaskToGraph(graph, br, i + 1);
            }
        } catch (WorkflowException e) {
            throw e;
        } catch (IOException e) {
            throw new IllegalStateException("An unknown error occurred", e);
        } catch (NumberFormatException e) {
            throw new WorkflowException("Error: Number of tasks must be a number.");
        } finally {
            br.close();
        }

        System.out.println("Workflow:");
        System.out.println();

        return graph;
    }

    /**
     * Read information for a task interactively and add it to the DependencyGraph
     * @param graph the DependencyGraph to add the task to
     * @param reader the BufferedReader to use to read input interactively
     * @param taskNum the current task number
     * @throws IOException if there is a problem while reading input interactively from the console
     */
    private void readTaskToGraph(DependencyGraph graph, BufferedReader reader, int taskNum) throws IOException {
        System.out.print("Enter name of task " + taskNum + ": ");
        String name = reader.readLine();
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("Task name must be non-empty.");
        }
        System.out.print("Enter task dependencies (Comma-separated list of tasks on which task '" + name +
                "' depends. Leave empty if '" + name + "' has no dependencies.): ");
        String dependenciesString = reader.readLine();
        List<String> dependencies = null;
        if (dependenciesString.equals("")) {
            dependenciesString = null;
        }
        if (null != dependenciesString) {
            dependencies = Arrays.asList(dependenciesString.split(",[ ]*"));
        }
        System.out.print("Enter task type: ");
        String type = reader.readLine();
        if (null == type || type.isEmpty()) {
            throw new IllegalArgumentException("Task type must be non-empty.");
        }
        DependencyGraph.TaskNode node = new DependencyGraph.TaskNode(name, dependencies, type);
        graph.addTask(node);
    }
}