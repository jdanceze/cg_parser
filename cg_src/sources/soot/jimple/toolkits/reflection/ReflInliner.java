package soot.jimple.toolkits.reflection;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.CompilationDeathException;
import soot.Main;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;
import soot.rtlib.tamiflex.DefaultHandler;
import soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler;
import soot.rtlib.tamiflex.OpaquePredicate;
import soot.rtlib.tamiflex.ReflectiveCalls;
import soot.rtlib.tamiflex.SootSig;
import soot.rtlib.tamiflex.UnexpectedReflectiveCall;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/reflection/ReflInliner.class */
public class ReflInliner {
    private static final Logger logger = LoggerFactory.getLogger(ReflInliner.class);

    public static void main(String[] args) {
        PackManager.v().getPack("wjpp").add(new Transform("wjpp.inlineReflCalls", new ReflectiveCallsInliner()));
        Scene scene = Scene.v();
        scene.addBasicClass(Object.class.getName());
        scene.addBasicClass(SootSig.class.getName(), 3);
        scene.addBasicClass(UnexpectedReflectiveCall.class.getName(), 3);
        scene.addBasicClass(IUnexpectedReflectiveCallHandler.class.getName(), 3);
        scene.addBasicClass(DefaultHandler.class.getName(), 3);
        scene.addBasicClass(OpaquePredicate.class.getName(), 3);
        scene.addBasicClass(ReflectiveCalls.class.getName(), 3);
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        argList.add("-w");
        argList.add("-p");
        argList.add("cg");
        argList.add("enabled:false");
        argList.add("-app");
        Options.v().set_keep_line_number(true);
        logger.debug("TamiFlex Booster Version " + ReflInliner.class.getPackage().getImplementationVersion());
        try {
            Main.main((String[]) argList.toArray(new String[0]));
        } catch (CompilationDeathException e) {
            logger.debug("\nERROR: " + e.getMessage() + "\n");
            logger.debug("The command-line options are described at:\nhttp://www.sable.mcgill.ca/soot/tutorial/usage/index.html");
            if (Options.v().verbose()) {
                throw e;
            }
            logger.debug("Use -verbose to see stack trace.");
            usage();
        }
    }

    private static void usage() {
        logger.debug(Options.v().getUsage());
    }
}
