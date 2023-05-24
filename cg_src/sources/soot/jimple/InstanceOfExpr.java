package soot.jimple;

import soot.Type;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/InstanceOfExpr.class */
public interface InstanceOfExpr extends Expr {
    Value getOp();

    void setOp(Value value);

    ValueBox getOpBox();

    Type getCheckType();

    void setCheckType(Type type);
}
