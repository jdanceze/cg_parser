package soot.jimple;

import java.util.List;
import soot.ArrayType;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/NewMultiArrayExpr.class */
public interface NewMultiArrayExpr extends Expr, AnyNewExpr {
    ArrayType getBaseType();

    void setBaseType(ArrayType arrayType);

    ValueBox getSizeBox(int i);

    int getSizeCount();

    Value getSize(int i);

    List<Value> getSizes();

    void setSize(int i, Value value);

    @Override // soot.Value
    Type getType();

    @Override // soot.util.Switchable
    void apply(Switch r1);
}
