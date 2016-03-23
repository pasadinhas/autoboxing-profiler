package ist.meic.pa;

import javassist.*;
import javassist.expr.*;
import java.io.*;
import java.lang.reflect.*;

public class BoxingProfiler {

    public static void main(String[] args) throws Throwable {
        if (args.length < 1) {
            System.err.println("One must specify one class to profile :)");
        } else {
            Translator translator = new BoxingProfilerTranslator ();
            ClassPool pool = ClassPool.getDefault();
            Loader classLoader = new Loader();
            classLoader.addTranslator(pool, translator);
            String[] restArgs = new String[args.length - 1];
            System.arraycopy(args, 1, restArgs, 0, restArgs.length);
            classLoader.run(args[0],restArgs);
            System.out.printf("Works %s\n", "very well!");
        }
    }
}
