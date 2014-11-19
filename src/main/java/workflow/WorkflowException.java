package workflow;

import java.io.IOException;

/**
 * Thrown to signal error conditions while generating a workflow
 */
public class WorkflowException extends IOException {
    public WorkflowException(String msg) {
        super(msg);
    }
}
