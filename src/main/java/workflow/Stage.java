package workflow;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static workflow.WorkflowUtil.*;

/**
 * Class Stage
 * Represents a stage in a workflow
 * A stage consists of a list of tasks each of whose dependencies have been satisfied and who can be executed in parallel.
 */
public class Stage {
    private static final String STAGE = "Stage";
    private String name;
    private List<DependencyGraph.TaskNode> jobsInStage;

    protected Stage(int stageNum) {
        name = STAGE + stageNum;
        jobsInStage = new ArrayList<DependencyGraph.TaskNode>();
    }

    /**
     * Add a job to the stage
     * @param job the job to add to the stage
     */
    protected void addJob(DependencyGraph.TaskNode job) {
        jobsInStage.add(job);
    }

    /**
     * Check if the stage contains a job
     * @param job the job to check
     * @return true if the job exists in the stage, false otherwise
     */
    protected boolean containsJob(DependencyGraph.TaskNode job) {
        return jobsInStage.contains(job);
    }

    /**
     * Get a list of all jobs in this stage
     * @return list of jobs in this stage
     */
    protected List<DependencyGraph.TaskNode> getAllJobs() {
        return jobsInStage;
    }

    /**
     * Print this stage to a PrintStream
     * @param out the PrintStream to print to
     */
    protected void print(PrintStream out) {
        out.println(name);
        for (int i = 0; i < jobsInStage.size(); i++) {
            DependencyGraph.TaskNode job = jobsInStage.get(i);
            out.print(job.getName() + ": " + job.getJobType());
            if (null != job.getDependencies()) {
                List<String> dependencies = job.getDependencies();
                out.print(" (" + JSON_KEYS.depends_on.toString() + ": ");
                for (int j = 0; j < dependencies.size(); j++) {
                    out.print(dependencies.get(j));
                    if (j < dependencies.size() - 1) {
                        out.print(", ");
                    }
                }
                out.print(")");
            }
            out.println();
        }
    }
}
