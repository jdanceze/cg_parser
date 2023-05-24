package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/UnopExpr.class */
public interface UnopExpr extends Expr {
    Value getOp();

    void setOp(Value value);

    ValueBox getOpBox();
}
