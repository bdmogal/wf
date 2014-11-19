package workflow;

import java.io.IOException;

/**
 * Driver class for Workflow Manager tool.
 * Demonstrates a sample usage of the WorkflowManager API
 */
public class WorkflowRunner {

    public static void main(String [] args) {
        if (args.length < 1) {
            WorkflowUtil.usage(System.err);
            System.exit(-1);
        }
        String inputType = args[0];
        String inputSource = args.length > 1 ? args[1] : null;
        try {
            WorkflowManager.execute(inputType, inputSource);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
    }
}