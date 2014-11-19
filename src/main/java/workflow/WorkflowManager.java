package workflow;

import java.io.IOException;

import static workflow.WorkflowUtil.*;

/**
 * Class WorkflowManager.
 * This is the API that is exposed to users.
 * Users can execute the 'execute' method from this class to generate a workflow.
 */
public class WorkflowManager {

    static {
        InputParserFactory.registerInputParser(JSON_FILE_INPUT_TYPE, JsonFileInputParser.class);
        InputParserFactory.registerInputParser(INTERACTIVE_INPUT_TYPE, InteractiveInputParser.class);
        InputParserFactory.registerInputParser(PROPERTIES_FILE_INPUT_TYPE, PropertiesFileInputParser.class);
    }

    /**
     * This is the only API exposed to users of this tool.
     * Sample usage can be found in WorkflowRunner#main()
     * @param inputType the type of input to parse (mandatory)
     * @param inputSource source for input data (optional)
     * @throws IOException if there are problems while generating the workflow
     */
    public static void execute(String inputType, String inputSource) throws IOException {
        InputParser parser = InputParserFactory.getInstance(inputType);
        DependencyGraph graph = parser.parse(inputSource);
        generateWorkflow(graph);
    }

    /**
     * Given a DependencyGraph, generates a workflow for it
     * @param graph the DependencyGraph to generate a workflow for
     */
    private static void generateWorkflow(DependencyGraph graph) {
        WorkflowGenerator generator = new WorkflowGenerator(graph);
        generator.generate();
        generator.print(System.out);
    }
}