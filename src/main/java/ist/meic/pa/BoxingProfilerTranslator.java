package ist.meic.pa;

import javassist.*;
import javassist.expr.*;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;

class BoxingProfilerTranslator implements Translator {
    private ArrayList<String> boxingMethods = new ArrayList<String>();
    private ArrayList<String> unboxingMethods = new ArrayList<String>();

    public BoxingProfilerTranslator() {
        boxingMethods.add("java.lang.Integer.valueOf");
        boxingMethods.add("java.lang.Double.valueOf");
        boxingMethods.add("java.lang.Long.valueOf");
        boxingMethods.add("java.lang.Float.valueOf");
        boxingMethods.add("java.lang.Short.valueOf");
        boxingMethods.add("java.lang.Byte.valueOf");
        boxingMethods.add("java.lang.Character.valueOf");
        boxingMethods.add("java.lang.Boolean.valueOf");

        unboxingMethods.add("java.lang.Integer.intValue");
        unboxingMethods.add("java.lang.Double.doubleValue");
        unboxingMethods.add("java.lang.Long.longValue");
        unboxingMethods.add("java.lang.Float.floatValue");
        unboxingMethods.add("java.lang.Short.shortValue");
        unboxingMethods.add("java.lang.Byte.byteValue");
        unboxingMethods.add("java.lang.Character.charValue");
        unboxingMethods.add("java.lang.Boolean.booleanValue");
    }

    public void start(ClassPool pool)
            throws NotFoundException, CannotCompileException {
    }
    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);
        profileBoxing(ctClass);
    }
    void profileBoxing(CtClass ctClass) throws NotFoundException, CannotCompileException {
            final String unBoxTemplate = "{" +
                                            "System.err.println(\"%s\" + \" unboxed <> \" + \"%s\");" +
                                            "$_ = $proceed($$);" +
                                         "}";
            final String boxTemplate = "{" +
                                            "System.err.println(\"%s\" + \" boxed <> \" + \"%s\");" +
                                            "$_ = $proceed($$);" +
                                         "}";
            for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
                ctBehavior.instrument(new ExprEditor() {
                    public void edit(MethodCall methodCall) throws CannotCompileException {
                        String fullMethodName = methodCall.getClassName() + "." + methodCall.getMethodName();

                        if (unboxingMethods.contains(fullMethodName)) {
                            String className =  methodCall.getClassName();
                            String behaviorName = ctBehavior.getLongName();
                            methodCall.replace(String.format(unBoxTemplate, behaviorName, className));
                        }
                        else if (boxingMethods.contains(fullMethodName)) {
                            String className =  methodCall.getClassName();
                            String behaviorName = ctBehavior.getLongName();
                            methodCall.replace(String.format(boxTemplate, behaviorName, className));
                        }
                    }
                });
            }
    }
}