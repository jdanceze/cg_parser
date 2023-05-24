package soot.grimp.internal;

import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractNewArrayExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GNewArrayExpr.class */
public class GNewArrayExpr extends AbstractNewArrayExpr implements Precedence {
    public GNewArrayExpr(Type type, Value size) {
        super(type, Grimp.v().newExprBox(size));
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 850;
    }

    @Override // soot.jimple.internal.AbstractNewArrayExpr, soot.Value
    public Object clone() {
        return new GNewArrayExpr(getBaseType(), Grimp.cloneIfNecessary(getSize()));
    }
}
