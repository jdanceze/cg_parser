package soot.jimple;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/CastExpr.class */
public interface CastExpr extends Expr {
    Value getOp();

    void setOp(Value value);

    ValueBox getOpBox();

    Type getCastType();

    void setCastType(Type type);

    @Override // soot.Value
    Type getType();

    @Override // soot.util.Switchable
    void apply(Switch r1);
}
