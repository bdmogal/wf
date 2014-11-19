package workflow;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Class InputParserFactory
 * Factory to pick a way of reading input based on an input type and parsing it into a DependencyGraph
 */
public class InputParserFactory {

    private static HashMap<String, InputParser> inputParsersCache = new HashMap<String, InputParser>();
    private static HashMap<String, Class> inputParsersClassCache = new HashMap<String, Class>();

    /**
     * Return an instance of type InteractiveInputParser or JsonFileInputParser based on the input type
     * @param type the type of input to return an InputParser for
     * @return instance of InteractiveInputParser if type is 'interactive' or JsonFileInputParser if type is 'jsonfile'
     * @throws IOException if passed an invalid type
     */
    protected static InputParser getInstance(String type) throws IOException {
        if (null == type) {
            throw new IllegalArgumentException("Input type cannot be null");
        }

        InputParser parser = inputParsersCache.get(type);
        if (null == parser) {
            Class<? extends InputParser> inputParserClass = inputParsersClassCache.get(type);
            if (inputParserClass != null) {
                Constructor constructor = null;
                try {
                    constructor = inputParserClass.getDeclaredConstructor(new Class[]{});
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException(e);
                }
                constructor.setAccessible(true);
                try {
                    parser = (InputParser) constructor.newInstance();
                } catch (InstantiationException e) {
                    throw new IllegalStateException(e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
                inputParsersCache.put(type, parser);
            }
            else {
                throw new IOException("Error: Invalid input type - '" + type + "'.");
            }
        }
        return parser;
    }

    /**
     * Register an InputParser class
     * @param type the type of this InputParser class
     * @param classz the InputParser class for this input type
     */
    protected static void registerInputParser(String type, Class classz) {
        inputParsersClassCache.put(type, classz);
    }
}
