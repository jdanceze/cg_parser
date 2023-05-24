package soot.jimple.toolkits.reflection;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/reflection/ConstantInvokeMethodBaseTransformer.class */
public class ConstantInvokeMethodBaseTransformer extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ConstantInvokeMethodBaseTransformer.class);
    private static final String INVOKE_SIG = "<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>";

    public ConstantInvokeMethodBaseTransformer(Singletons.Global g) {
    }

    public static ConstantInvokeMethodBaseTransformer v() {
        return G.v().soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        boolean verbose = options.containsKey("verbose");
        Jimple jimp = Jimple.v();
        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            if (sootClass.resolvingLevel() >= 3) {
                for (SootMethod sootMethod : sootClass.getMethods()) {
                    Body body = sootMethod.retrieveActiveBody();
                    Chain<Local> locals = body.getLocals();
                    Chain<Unit> units = body.getUnits();
                    Iterator<Unit> iterator = units.snapshotIterator();
                    while (iterator.hasNext()) {
                        Stmt s = (Stmt) iterator.next();
                        if (s.containsInvokeExpr()) {
                            InvokeExpr invokeExpr = s.getInvokeExpr();
                            if (INVOKE_SIG.equals(invokeExpr.getMethod().getSignature())) {
                                Value arg0 = invokeExpr.getArg(0);
                                if (arg0 instanceof StringConstant) {
                                    Local newLocal = jimp.newLocal("sc" + locals.size(), arg0.getType());
                                    locals.add(newLocal);
                                    units.insertBefore(jimp.newAssignStmt(newLocal, (StringConstant) arg0), s);
                                    invokeExpr.setArg(0, newLocal);
                                    if (verbose) {
                                        logger.debug("Replaced constant base object of Method.invoke() by local in: " + sootMethod.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
