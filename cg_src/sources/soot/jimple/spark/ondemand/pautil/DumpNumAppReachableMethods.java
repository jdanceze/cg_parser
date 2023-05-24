package soot.jimple.spark.ondemand.pautil;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Main;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/DumpNumAppReachableMethods.class */
public class DumpNumAppReachableMethods extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(DumpNumAppReachableMethods.class);

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        int numAppMethods = 0;
        Iterator mIt = Scene.v().getReachableMethods().listener();
        while (mIt.hasNext()) {
            SootMethod m = (SootMethod) mIt.next();
            if (isAppMethod(m)) {
                numAppMethods++;
            }
        }
        logger.debug("Number of reachable methods in application: " + numAppMethods);
    }

    private boolean isAppMethod(SootMethod m) {
        return !SootUtil.inLibrary(m.getDeclaringClass().getName());
    }

    public static void main(String[] args) {
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.narm", new DumpNumAppReachableMethods()));
        Main.main(args);
    }
}
