package soot.jimple;

import soot.SootField;
import soot.SootFieldRef;
/* loaded from: gencallgraphv3.jar:soot/jimple/FieldRef.class */
public interface FieldRef extends ConcreteRef {
    SootFieldRef getFieldRef();

    void setFieldRef(SootFieldRef sootFieldRef);

    SootField getField();
}
