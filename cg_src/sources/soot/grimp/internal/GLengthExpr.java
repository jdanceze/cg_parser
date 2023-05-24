package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractLengthExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GLengthExpr.class */
public class GLengthExpr extends AbstractLengthExpr {
    public GLengthExpr(Value op) {
        super(Grimp.v().newObjExprBox(op));
    }

    @Override // soot.jimple.internal.AbstractUnopExpr, soot.Value
    public Object clone() {
        return new GLengthExpr(Grimp.cloneIfNecessary(getOp()));
    }
}
