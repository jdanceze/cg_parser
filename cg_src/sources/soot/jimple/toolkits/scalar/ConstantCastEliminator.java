package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/ConstantCastEliminator.class */
public class ConstantCastEliminator extends BodyTransformer {
    public ConstantCastEliminator(Singletons.Global g) {
    }

    public static ConstantCastEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_ConstantCastEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                Value rightOp = assign.getRightOp();
                if (rightOp instanceof CastExpr) {
                    CastExpr ce = (CastExpr) rightOp;
                    Value castOp = ce.getOp();
                    if (castOp instanceof IntConstant) {
                        Type castType = ce.getType();
                        if (castType instanceof FloatType) {
                            assign.setRightOp(FloatConstant.v(((IntConstant) castOp).value));
                        } else if (castType instanceof DoubleType) {
                            assign.setRightOp(DoubleConstant.v(((IntConstant) castOp).value));
                        }
                    }
                }
            }
        }
    }
}
