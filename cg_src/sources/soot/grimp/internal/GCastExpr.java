package soot.grimp.internal;

import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractCastExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GCastExpr.class */
public class GCastExpr extends AbstractCastExpr implements Precedence {
    public GCastExpr(Value op, Type type) {
        super(Grimp.v().newExprBox(op), type);
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 850;
    }

    @Override // soot.jimple.internal.AbstractCastExpr
    public String toString() {
        Value op = getOp();
        String opString = op.toString();
        if ((op instanceof Precedence) && ((Precedence) op).getPrecedence() < getPrecedence()) {
            opString = "(" + opString + ")";
        }
        return "(" + getCastType().toString() + ") " + opString;
    }

    @Override // soot.jimple.internal.AbstractCastExpr, soot.Value
    public Object clone() {
        return new GCastExpr(Grimp.cloneIfNecessary(getOp()), getCastType());
    }
}
