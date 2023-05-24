package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/InstanceFieldRef.class */
public interface InstanceFieldRef extends FieldRef {
    Value getBase();

    ValueBox getBaseBox();

    void setBase(Value value);
}
