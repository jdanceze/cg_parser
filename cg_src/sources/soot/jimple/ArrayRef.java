package soot.jimple;

import soot.Local;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/ArrayRef.class */
public interface ArrayRef extends ConcreteRef {
    Value getBase();

    void setBase(Local local);

    ValueBox getBaseBox();

    Value getIndex();

    void setIndex(Value value);

    ValueBox getIndexBox();

    @Override // soot.Value
    Type getType();

    @Override // soot.util.Switchable
    void apply(Switch r1);
}
