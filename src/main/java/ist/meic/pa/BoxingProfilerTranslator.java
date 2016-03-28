package ist.meic.pa;

import javassist.*;
import javassist.expr.*;

import java.util.ArrayList;

class BoxingProfilerTranslator implements Translator {
    private ArrayList<String> targetMethods = new ArrayList<String>();
    
    public BoxingProfilerTranslator() {
        targetMethods.add("java.lang.Integer.valueOf");
        targetMethods.add("java.lang.Double.valueOf");
        targetMethods.add("java.lang.Long.valueOf");
        targetMethods.add("java.lang.Float.valueOf");
        targetMethods.add("java.lang.Short.valueOf");
        targetMethods.add("java.lang.Byte.valueOf");
        targetMethods.add("java.lang.Character.valueOf");
        targetMethods.add("java.lang.Boolean.valueOf");

        targetMethods.add("java.lang.Integer.intValue");
        targetMethods.add("java.lang.Double.doubleValue");
        targetMethods.add("java.lang.Long.longValue");
        targetMethods.add("java.lang.Float.floatValue");
        targetMethods.add("java.lang.Short.shortValue");
        targetMethods.add("java.lang.Byte.byteValue");
        targetMethods.add("java.lang.Character.charValue");
        targetMethods.add("java.lang.Boolean.booleanValue");
    }

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException { }
    
    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);
        profileBoxing(ctClass);
    }
    void profileBoxing(CtClass ctClass) throws NotFoundException, CannotCompileException {

            final String template = "{" +
                                        "ist.meic.pa.DataManager.getInstance().processEntry(\"%s\", \"%s\", \"%s\");" +
                                        "$_ = $proceed($$);" +
                                    "}";
                                    
            final String finalize = "{ System.err.print(ist.meic.pa.DataManager.getInstance()); }";

            for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
                if (ctBehavior.getName().equals("main")) {
                    ctBehavior.insertAfter(finalize, true);
                }
                ctBehavior.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall methodCall) throws CannotCompileException {
                        String fullMethodName = methodCall.getClassName() + "." + methodCall.getMethodName();

                        if (targetMethods.contains(fullMethodName)) {
                            methodCall.replace(String.format(template, ctBehavior.getLongName(), methodCall.getMethodName(), methodCall.getClassName()));
                        }
                    }
                });

            }
    }
}