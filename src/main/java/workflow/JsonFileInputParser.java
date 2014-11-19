package workflow;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static workflow.WorkflowUtil.*;

/**
 * Class JsonFileInputParser
 * Implements InputParser to read input as a JSON File and parse it into a DependencyGraph
 */
public class JsonFileInputParser implements InputParser {

    /**
     * Read a workflow from a file containing its JSON representation
     * and parse it into a DependencyGraph
     * @param inputSource path to the file containing JSON representation of input data
     * @return a DependencyGraph for the JSON workflow
     * @throws IOException if the JSON file is not provided
     */
    @Override
    public DependencyGraph parse(String inputSource) throws IOException {
        validate(inputSource);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = null;
        try {
            data = mapper.readValue(new File(inputSource), Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return buildDependencyGraph(data);
    }

    /**
     * Build a DependencyGraph from the deserialized JSON
     * @param data the java object deserialized from JSON data in the provided file
     * @return a DependencyGraph
     */
    private DependencyGraph buildDependencyGraph(Map<String, Object> data) throws IOException {
        DependencyGraph graph = new DependencyGraph();
        List<LinkedHashMap<String, Object>> tasks = getTasks(data);
        for (LinkedHashMap<String, Object> task : tasks) {
            DependencyGraph.TaskNode taskNode = getTaskNode(task);
            graph.addTask(taskNode);
        }
        return graph;
    }

    private List<LinkedHashMap<String, Object>> getTasks(Map<String, Object> data) {
        if (data.containsKey(JSON_KEYS.tasks.toString())) {
            return (List<LinkedHashMap<String, Object>>) data.get(JSON_KEYS.tasks.toString());
        }
        return null;
    }

    /**
     * Parse a TaskNode to add to the DependencyGraph
     * @param task a map of data for the task to add to the DependencyGraph
     * @return a TaskNode to add to the DependencyGraph
     */
    private DependencyGraph.TaskNode getTaskNode(LinkedHashMap<String, Object> task) {
        Set<Map.Entry<String, Object>> entries = task.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String taskName = entry.getKey();
            if (null == taskName) {
                throw new IllegalStateException("Job Name cannot be null");
            }
            LinkedHashMap<String, Object> taskMetadata = (LinkedHashMap<String, Object>) entry.getValue();
            String taskType = (String) taskMetadata.get(JSON_KEYS.job_type.toString());
            if (null == taskType) {
                throw new IllegalStateException("Job Type cannot be null");
            }
            List<String> dependencies = null;
            if (taskMetadata.containsKey(JSON_KEYS.depends_on.toString())) {
                dependencies = (List<String>) taskMetadata.get(JSON_KEYS.depends_on.toString());
            }
            DependencyGraph.TaskNode node = new DependencyGraph.TaskNode(taskName, dependencies, taskType);
            return node;
        }
        return null;
    }

    private void validate(String src) throws IOException {
        if (null == src) {
            throw new IOException("Input type 'jsonfile' needs the path to " +
                    "the json file as the second argument");
        }
    }
}