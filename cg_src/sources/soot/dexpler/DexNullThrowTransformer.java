package soot.dexpler;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.scalar.LocalCreation;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexNullThrowTransformer.class */
public class DexNullThrowTransformer extends BodyTransformer {
    public static DexNullThrowTransformer v() {
        return new DexNullThrowTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        LocalCreation lc = Scene.v().createLocalCreation(b.getLocals(), "ex");
        Iterator<Unit> unitIt = b.getUnits().snapshotIterator();
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (u instanceof ThrowStmt) {
                ThrowStmt throwStmt = (ThrowStmt) u;
                if (throwStmt.getOp() == NullConstant.v() || throwStmt.getOp().equals(IntConstant.v(0)) || throwStmt.getOp().equals(LongConstant.v(0L))) {
                    createThrowStmt(b, throwStmt, lc);
                }
            }
        }
    }

    private void createThrowStmt(Body body, Unit oldStmt, LocalCreation lc) {
        RefType tp = RefType.v("java.lang.NullPointerException");
        Local lcEx = lc.newLocal(tp);
        SootMethodRef constructorRef = Scene.v().makeConstructorRef(tp.getSootClass(), Collections.singletonList(RefType.v("java.lang.String")));
        body.getUnits().insertBefore(Jimple.v().newAssignStmt(lcEx, Jimple.v().newNewExpr(tp)), (AssignStmt) oldStmt);
        body.getUnits().insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lcEx, constructorRef, Collections.singletonList(StringConstant.v("Null throw statement replaced by Soot")))), (InvokeStmt) oldStmt);
        body.getUnits().swapWith(oldStmt, (Unit) Jimple.v().newThrowStmt(lcEx));
    }
}
