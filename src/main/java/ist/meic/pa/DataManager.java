package ist.meic.pa;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

final public class DataManager {
    private static DataManager instance = null;
    private TreeMap<String, Integer> data = new TreeMap<String, Integer>();

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void processEntry(String behaviourName, String methodName, String className) {
        String entryKey = makeKey(behaviourName, methodName, className);

        if ( ! data.containsKey(entryKey)) {
            data.put(entryKey, 1);
        }
        else {
            data.put(entryKey, data.get(entryKey) + 1);
        }
    }

    private String makeKey(String behaviourName, String methodName, String className) {

        String fullMethodName = className + "." + methodName;

        // Boxing methods are called valueOf.
        String boxType = methodName.equals("valueOf") ? "boxed" : "unboxed";

        return behaviourName + " " + className + " " + boxType;
    }

    public String toString() {
        String output = "";
                
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            output += makePrintableEntry(entry.getKey(), entry.getValue()) + System.lineSeparator();
        }

        return output;
    }

    private String makePrintableEntry(String key, Integer value) {
        String[] elements = key.split(" ");
        String behaviourName = elements[0];
        String className = elements[1];
        String boxType = elements[2];

        return behaviourName + " " + boxType + " " + value + " " + className;
    }
}