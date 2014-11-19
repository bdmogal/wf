package workflow;

import java.io.IOException;

/**
 * Interface to parse user input into a DependencyGraph
 */
public interface InputParser {
    public DependencyGraph parse(String inputSource) throws IOException;
}
