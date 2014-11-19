package workflow;

import java.io.PrintStream;

/**
 * Utilities for generating a workflow
 */
public class WorkflowUtil {

    protected static enum JSON_KEYS {
        tasks,
        job_type,
        depends_on;
    }

    protected static final String JSON_FILE_INPUT_TYPE = "jsonfile";

    protected static final String INTERACTIVE_INPUT_TYPE = "interactive";

    protected static final String PROPERTIES_FILE_INPUT_TYPE = "properties";

    protected static final void usage(PrintStream stream) {
        stream.println("Usage:");
        stream.println("java -cp <classpath> WorkflowRunner [jsonfile|interactive|properties] [Optional file]");
        stream.println("[Optional file] is the absolute path to the file containing json data for input type 'jsonfile'" +
                " or to the file containing properties for input type 'properties'");
    }
}
