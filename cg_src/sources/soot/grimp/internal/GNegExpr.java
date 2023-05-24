package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNegExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GNegExpr.class */
public class GNegExpr extends AbstractNegExpr {
    public GNegExpr(Value op) {
        super(Grimp.v().newExprBox(op));
    }

    @Override // soot.jimple.internal.AbstractUnopExpr, soot.Value
    public Object clone() {
        return new GNegExpr(Grimp.cloneIfNecessary(getOp()));
    }
}
