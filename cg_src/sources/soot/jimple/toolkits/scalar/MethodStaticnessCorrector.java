package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/MethodStaticnessCorrector.class */
public class MethodStaticnessCorrector extends AbstractStaticnessCorrector {
    private static final Logger logger = LoggerFactory.getLogger(MethodStaticnessCorrector.class);

    public MethodStaticnessCorrector(Singletons.Global g) {
    }

    public static MethodStaticnessCorrector v() {
        return G.v().soot_jimple_toolkits_scalar_MethodStaticnessCorrector();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        SootMethod target;
        Iterator<Unit> unitIt = b.getUnits().snapshotIterator();
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (u instanceof Stmt) {
                Stmt s = (Stmt) u;
                if (s.containsInvokeExpr()) {
                    InvokeExpr iexpr = s.getInvokeExpr();
                    if (iexpr instanceof StaticInvokeExpr) {
                        SootMethodRef methodRef = iexpr.getMethodRef();
                        if (isClassLoaded(methodRef.declaringClass()) && (target = Scene.v().grabMethod(methodRef.getSignature())) != null && !target.isStatic() && canBeMadeStatic(target)) {
                            Body targetBody = target.getActiveBody();
                            targetBody.getUnits().remove(targetBody.getThisUnit());
                            target.setModifiers(target.getModifiers() | 8);
                            logger.warn(String.valueOf(target.getName()) + " changed into a static method");
                        }
                    }
                }
            }
        }
    }

    private boolean canBeMadeStatic(SootMethod target) {
        if (!target.hasActiveBody()) {
            return false;
        }
        Body body = target.getActiveBody();
        Value thisLocal = body.getThisLocal();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            for (ValueBox vb : u.getUseBoxes()) {
                if (vb.getValue() == thisLocal) {
                    return false;
                }
            }
        }
        return true;
    }
}
