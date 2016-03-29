package ist.meic.pa;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * This singleton class is responsible for storing and managing the data of the profiler.
 */
final public class DataManager {
    private static DataManager instance = null;
    private TreeMap<String, Integer> data = new TreeMap<String, Integer>();

    private DataManager() {

    }

    /** 
     * Get the singleton instance
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


    /** 
     * Adds a new entry if it is the first box/unbox of its type in the speciefied behaviour, 
     * else it increments the counter of occurrences.
     */
    public void processEntry(String behaviourName, String methodName, String className) {
        String entryKey = makeKey(behaviourName, methodName, className);

        if ( ! data.containsKey(entryKey)) {
            data.put(entryKey, 1);
        }
        else {
            data.put(entryKey, data.get(entryKey) + 1);
        }
    }


    /**
     * Makes the key for a given entry, enforcing desired lexical order.
     */
    private String makeKey(String behaviourName, String methodName, String className) {

        String fullMethodName = className + "." + methodName;

        // Boxing methods are called valueOf; all others that get here are, by design, unboxing ones.
        String boxType = methodName.equals("valueOf") ? "boxed" : "unboxed";

        return behaviourName + " " + className + " " + boxType;
    }

    /** 
     * Returns a string that is the external representation of this singleton.
     */
    public String toString() {
        String output = "";
                
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            output += makePrintableEntry(entry.getKey(), entry.getValue()) + System.lineSeparator();
        }

        return output;
    }

    /** 
     * Given a key and a value produces the external representation of an entry.
     */
    private String makePrintableEntry(String key, Integer value) {
        String[] elements = key.split(" ");
        String behaviourName = elements[0];
        String className = elements[1];
        String boxType = elements[2];

        return behaviourName + " " + boxType + " " + value + " " + className;
    }
}
