package soot.jimple;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/NewArrayExpr.class */
public interface NewArrayExpr extends Expr, AnyNewExpr {
    Type getBaseType();

    void setBaseType(Type type);

    ValueBox getSizeBox();

    Value getSize();

    void setSize(Value value);

    @Override // soot.Value
    Type getType();

    @Override // soot.util.Switchable
    void apply(Switch r1);
}
