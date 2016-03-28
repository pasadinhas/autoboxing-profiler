package ist.meic.pa;

import java.util.ArrayList;
import java.util.TreeMap;

final public class DataManager {
    private static DataManager instance = null;
    private TreeMap<String, Integer> data = new TreeMap<String, Integer>();
    private ArrayList<String> boxingMethods = new ArrayList<String>() {{
        add("java.lang.Integer.valueOf");
        add("java.lang.Double.valueOf");
        add("java.lang.Long.valueOf");
        add("java.lang.Float.valueOf");
        add("java.lang.Short.valueOf");
        add("java.lang.Byte.valueOf");
        add("java.lang.Character.valueOf");
        add("java.lang.Boolean.valueOf");
    }};
    private ArrayList<String> unboxingMethods = new ArrayList<String>() {{
        add("java.lang.Integer.intValue");
        add("java.lang.Double.doubleValue");
        add("java.lang.Long.longValue");
        add("java.lang.Float.floatValue");
        add("java.lang.Short.shortValue");
        add("java.lang.Byte.byteValue");
        add("java.lang.Character.charValue");
        add("java.lang.Boolean.booleanValue");
    }};

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

        if (fullMethodName.endsWith("Of")) {
            return behaviourName + " " + className + " " + "boxed";
        }
        else if (fullMethodName.endsWith("Value")) {
            return behaviourName + " " + className + " " + "unboxed";
        }
        else {
            return null;
        }
    }

    public String asString() {
        String output = "";
        boolean first = true;
        
        for (String key : data.keySet()) {
            if (first) {
               output += makePrintableEntry(key);
               first = false;
            }
            else {
                output +=  System.lineSeparator() + makePrintableEntry(key);
            }
        }

        return output;
    }

    private String makePrintableEntry(String key) {
        String[] elements = key.split(" ");
        Integer value = data.get(key);
        String behaviourName = elements[0];
        String className = elements[1];
        String boxType = elements[2];

        return behaviourName + " " + boxType + " " + value + " " + className;
    }
}
