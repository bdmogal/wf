package workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class PropertiesFileInputParser
 * Implements InputParser to read a properties file with the following structure and parse it into a DependencyGraph
 * {code}
 * tasks = task1, task2, task3
 * task1.job_type = Hive
 * task2.job_type = Pig
 * task3.job_type = Map Reduce
 * task3.depends_on = task1, task2
 * {code}
 */
public class PropertiesFileInputParser implements InputParser {

    /**
     * Keys/Key suffixes in the properties file
     */
    private static class PropertiesKeys {
        private static final String TASKS_KEY = "tasks";
        private static final String DEPENDENCIES_KEY_SUFFIX = ".depends_on";
        private static final String TASK_TYPE_KEY_SUFFIX = ".job_type";
    }

    /**
     * Read a properties file as an argument and return a DependencyGraph for it
     * @param inputSource path to the properties file containing input data
     * @return the DependencyGraph representing data in the properties file
     * @throws IOException if an invalid number of arguments is passed
     */
    @Override
    public DependencyGraph parse(String inputSource) throws IOException {
        validate(inputSource);
        InputStream in = new FileInputStream(new File(inputSource));
        Properties properties = new Properties();
        properties.load(in);
        in.close();
        return buildDependencyGraph(properties);
    }

    /**
     * Builds a DependencyGraph from a Properties object
     * @param properties the Properties object to build the DependencyGraph from
     * @return a DependencyGraph object from the properties
     */
    private DependencyGraph buildDependencyGraph(Properties properties) throws IOException {
        String [] tasks = null;
        if (properties.containsKey(PropertiesKeys.TASKS_KEY)) {
            tasks = properties.getProperty(PropertiesKeys.TASKS_KEY).split(",[ ]*");
        }
        else {
            throw new IllegalArgumentException("Property '" + PropertiesKeys.TASKS_KEY + "' not defined in properties file.");
        }

        DependencyGraph graph = new DependencyGraph();
        for (String task : tasks) {
            graph.addTask(readTaskFromProperties(task, properties));
        }
        return graph;
    }

    /**
     * Translate a task's properties from the properties file into a DependencyGraph.TaskNode object
     * @param task the task name
     * @param properties the task's properties
     * @return a DependencyGraph.TaskNode object representing the task
     */
    private DependencyGraph.TaskNode readTaskFromProperties(String task, Properties properties) {
        String jobType = null;
        List<String> dependencies = null;
        String jobTypeKey = task + PropertiesKeys.TASK_TYPE_KEY_SUFFIX;
        String dependenciesKey = task + PropertiesKeys.DEPENDENCIES_KEY_SUFFIX;

        if (properties.containsKey(jobTypeKey)) {
            jobType = properties.getProperty(jobTypeKey);
        }
        else {
            throw new IllegalArgumentException("Job type for task '" + task + "' cannot be null");
        }
        if (properties.containsKey(dependenciesKey)) {
            String dependenciesString = properties.getProperty(dependenciesKey);
            if (!dependenciesString.equals("")) {
                dependencies = Arrays.asList(dependenciesString.split(",[ ]*"));
            }
        }
        return new DependencyGraph.TaskNode(task, dependencies, jobType);
    }

    /**
     * Validate command line arguments
     * @param src input source
     * @throws IOException if validation fails
     */
    private void validate(String src) throws IOException {
        if (null == src) {
            throw new IOException("Input type 'properties' needs the path to " +
                    "the properties file as the second argument");
        }
    }
}
