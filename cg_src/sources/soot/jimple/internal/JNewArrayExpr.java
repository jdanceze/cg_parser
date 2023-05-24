package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JNewArrayExpr.class */
public class JNewArrayExpr extends AbstractNewArrayExpr {
    public JNewArrayExpr(Type type, Value size) {
        super(type, Jimple.v().newImmediateBox(size));
    }

    public JNewArrayExpr(Type type, ValueBox box) {
        super(type, box);
    }

    @Override // soot.jimple.internal.AbstractNewArrayExpr, soot.Value
    public Object clone() {
        return new JNewArrayExpr(getBaseType(), Jimple.cloneIfNecessary(getSize()));
    }
}
