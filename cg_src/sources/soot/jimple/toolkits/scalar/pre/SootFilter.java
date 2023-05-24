package soot.jimple.toolkits.scalar.pre;

import soot.EquivalentValue;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConcreteRef;
import soot.jimple.DivExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.RemExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/SootFilter.class */
public class SootFilter {
    public static EquivalentValue equiVal(Value val) {
        if (val != null) {
            return new EquivalentValue(val);
        }
        return null;
    }

    public static Value rhs(Unit unit) {
        if (unit instanceof AssignStmt) {
            return ((AssignStmt) unit).getRightOp();
        }
        return null;
    }

    public static Value binop(Value val) {
        if (val instanceof BinopExpr) {
            return val;
        }
        return null;
    }

    public static Value binopRhs(Unit unit) {
        return binop(rhs(unit));
    }

    public static Value concreteRef(Value val) {
        if (val instanceof ConcreteRef) {
            return val;
        }
        return null;
    }

    public static Value noExceptionThrowing(Value val) {
        if (val == null || throwsException(val)) {
            return null;
        }
        return val;
    }

    public static Value noExceptionThrowingRhs(Unit unit) {
        return noExceptionThrowing(rhs(unit));
    }

    public static Value noInvokeRhs(Unit unit) {
        return noInvoke(rhs(unit));
    }

    public static boolean isInvoke(Value val) {
        return getEquivalentValueRoot(val) instanceof InvokeExpr;
    }

    public static Value noInvoke(Value val) {
        if (val == null || isInvoke(val)) {
            return null;
        }
        return val;
    }

    public static Value local(Value val) {
        if (val == null || !isLocal(val)) {
            return null;
        }
        return val;
    }

    public static Value noLocal(Value val) {
        if (val == null || isLocal(val)) {
            return null;
        }
        return val;
    }

    public static boolean isLocal(Value val) {
        return getEquivalentValueRoot(val) instanceof Local;
    }

    public static Value getEquivalentValueRoot(Value val) {
        while (val instanceof EquivalentValue) {
            val = ((EquivalentValue) val).getValue();
        }
        return val;
    }

    public static boolean throwsException(Value val) {
        Value val2 = getEquivalentValueRoot(val);
        return (val2 instanceof DivExpr) || (val2 instanceof RemExpr) || (val2 instanceof LengthExpr);
    }
}
