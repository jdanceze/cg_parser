package soot.grimp.internal;

import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractInstanceOfExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GInstanceOfExpr.class */
public class GInstanceOfExpr extends AbstractInstanceOfExpr {
    public GInstanceOfExpr(Value op, Type checkType) {
        super(Grimp.v().newObjExprBox(op), checkType);
    }

    @Override // soot.jimple.internal.AbstractInstanceOfExpr, soot.Value
    public Object clone() {
        return new GInstanceOfExpr(Grimp.cloneIfNecessary(getOp()), getCheckType());
    }
}
