package ist.meic.pa;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//This singleton class is responsible of storing and managing the data of the profiler
final public class DataManager {
    private static DataManager instance = null;
    private TreeMap<String, Integer> data = new TreeMap<String, Integer>();

    private DataManager() {

    }

    /* Singleton instance getter
    */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


    /* Adds a new entry if it's the first box/unbox of the class in the speciefied behaviour, 
    /* else it increments the counter
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


    /* Makes the key for a given entry, with the desired lexical order
    */
    private String makeKey(String behaviourName, String methodName, String className) {

        String fullMethodName = className + "." + methodName;

        // Boxing methods are called valueOf; all others that get here are, by design, unboxing ones.
        String boxType = methodName.equals("valueOf") ? "boxed" : "unboxed";

        return behaviourName + " " + className + " " + boxType;
    }

    /* Prints the data
    */
    public String toString() {
        String output = "";
                
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            output += makePrintableEntry(entry.getKey(), entry.getValue()) + System.lineSeparator();
        }

        return output;
    }

    /* Given a key and a value produces the output fitting the requirements
    */
    private String makePrintableEntry(String key, Integer value) {
        String[] elements = key.split(" ");
        String behaviourName = elements[0];
        String className = elements[1];
        String boxType = elements[2];

        return behaviourName + " " + boxType + " " + value + " " + className;
    }
}