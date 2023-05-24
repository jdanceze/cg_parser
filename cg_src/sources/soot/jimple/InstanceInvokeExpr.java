package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/InstanceInvokeExpr.class */
public interface InstanceInvokeExpr extends InvokeExpr {
    Value getBase();

    ValueBox getBaseBox();

    void setBase(Value value);
}
