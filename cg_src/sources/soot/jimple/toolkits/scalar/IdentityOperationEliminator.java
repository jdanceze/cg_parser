package soot.jimple.toolkits.scalar;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.LongType;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MulExpr;
import soot.jimple.OrExpr;
import soot.jimple.SubExpr;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/IdentityOperationEliminator.class */
public class IdentityOperationEliminator extends BodyTransformer {
    public IdentityOperationEliminator(Singletons.Global g) {
    }

    public static IdentityOperationEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_IdentityOperationEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Chain<Unit> units = b.getUnits();
        for (Unit u : units) {
            if (u instanceof AssignStmt) {
                AssignStmt assignStmt = (AssignStmt) u;
                Value rightOp = assignStmt.getRightOp();
                if (rightOp instanceof AddExpr) {
                    BinopExpr aer = (BinopExpr) rightOp;
                    if (isConstZero(aer.getOp1())) {
                        assignStmt.setRightOp(aer.getOp2());
                    } else if (isConstZero(aer.getOp2())) {
                        assignStmt.setRightOp(aer.getOp1());
                    }
                } else if (rightOp instanceof SubExpr) {
                    BinopExpr aer2 = (BinopExpr) rightOp;
                    if (isConstZero(aer2.getOp2())) {
                        assignStmt.setRightOp(aer2.getOp1());
                    }
                } else if (rightOp instanceof MulExpr) {
                    BinopExpr aer3 = (BinopExpr) rightOp;
                    if (isConstZero(aer3.getOp1())) {
                        assignStmt.setRightOp(getZeroConst(assignStmt.getLeftOp().getType()));
                    } else if (isConstZero(aer3.getOp2())) {
                        assignStmt.setRightOp(getZeroConst(assignStmt.getLeftOp().getType()));
                    }
                } else if (rightOp instanceof OrExpr) {
                    OrExpr orExpr = (OrExpr) rightOp;
                    if (isConstZero(orExpr.getOp1())) {
                        assignStmt.setRightOp(orExpr.getOp2());
                    } else if (isConstZero(orExpr.getOp2())) {
                        assignStmt.setRightOp(orExpr.getOp1());
                    }
                }
            }
        }
        Iterator<Unit> unitIt = units.iterator();
        while (unitIt.hasNext()) {
            Unit u2 = unitIt.next();
            if (u2 instanceof AssignStmt) {
                AssignStmt assignStmt2 = (AssignStmt) u2;
                if (assignStmt2.getLeftOp() == assignStmt2.getRightOp()) {
                    unitIt.remove();
                }
            }
        }
    }

    private static Value getZeroConst(Type type) {
        if (type instanceof IntType) {
            return IntConstant.v(0);
        }
        if (type instanceof LongType) {
            return LongConstant.v(0L);
        }
        if (type instanceof FloatType) {
            return FloatConstant.v(0.0f);
        }
        if (type instanceof DoubleType) {
            return DoubleConstant.v(Const.default_value_double);
        }
        throw new RuntimeException("Unsupported numeric type");
    }

    private static boolean isConstZero(Value op) {
        return (op instanceof IntConstant) && ((IntConstant) op).value == 0;
    }
}
