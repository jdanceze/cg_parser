package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JCastExpr.class */
public class JCastExpr extends AbstractCastExpr {
    public JCastExpr(Value op, Type type) {
        super(Jimple.v().newImmediateBox(op), type);
    }

    public JCastExpr(ValueBox op, Type type) {
        super(op, type);
    }

    @Override // soot.jimple.internal.AbstractCastExpr, soot.Value
    public Object clone() {
        return new JCastExpr(Jimple.cloneIfNecessary(getOp()), this.type);
    }
}
