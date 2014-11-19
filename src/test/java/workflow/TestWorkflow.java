package workflow;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import workflow.InputParserFactory;
import workflow.WorkflowUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * Contains some preliminary unit tests for the WorkflowManager API
 */
public class TestWorkflow {
    private DependencyGraph graph = null;
    private final String toTaskName = "to";
    private final String toTaskType = "toType";
    private final String fromTaskName = "from";
    private final String fromTaskType = "fromType";
    private final DependencyGraph.TaskNode to = new DependencyGraph.TaskNode(toTaskName, null, toTaskType);
    private final DependencyGraph.TaskNode from = new DependencyGraph.TaskNode(fromTaskName, Arrays.asList(new String[]{toTaskName}), fromTaskType);

    @BeforeClass
    public void setup() throws IOException {
        graph = new DependencyGraph();
        graph.addTask(to);
        graph.addTask(from);
    }

    @Test
    public void testFactory() throws IOException {
        Assert.assertTrue(InputParserFactory.getInstance(WorkflowUtil.JSON_FILE_INPUT_TYPE)
                instanceof JsonFileInputParser);
        Assert.assertTrue(InputParserFactory.getInstance(WorkflowUtil.PROPERTIES_FILE_INPUT_TYPE)
                instanceof PropertiesFileInputParser);
        Assert.assertTrue(InputParserFactory.getInstance(WorkflowUtil.INTERACTIVE_INPUT_TYPE)
                instanceof InteractiveInputParser);
    }

    @Test
    public void testDependencyGraph() {
        Assert.assertEquals(graph.inDegree(to), 1);
        Assert.assertEquals(graph.inDegree(from), 0);
        Assert.assertEquals(graph.getTaskByName(toTaskName).getJobType(), toTaskType);
        Assert.assertEquals(graph.dependenciesOf(from).get(0), toTaskName);
    }

    @Test
    public void testWorkflowGenerator() {
        WorkflowGenerator generator = new WorkflowGenerator(graph);
        generator.generate();
        Assert.assertEquals(generator.getWorkflow().size(), 2);
    }

    @Test(expectedExceptions = WorkflowException.class)
    public void testSelfDependencyError() throws IOException {
        String newTaskName = "newTask";
        DependencyGraph.TaskNode newtask = new DependencyGraph.TaskNode(newTaskName, Arrays.asList(new String[]{newTaskName}), toTaskType);
        graph.addTask(newtask);
    }

    @Test(expectedExceptions = WorkflowException.class)
    public void testUnknownDependencyError() throws IOException {
        String unknown = "unknown";
        String unknownTask = "unknownTask";
        DependencyGraph.TaskNode newtask = new DependencyGraph.TaskNode(unknownTask, Arrays.asList(new String[]{unknown}), unknown);
        graph.addTask(newtask);
    }

    @Test(expectedExceptions = WorkflowException.class)
    public void testDuplicateError() throws IOException {
        graph.addTask(to);
    }
}
