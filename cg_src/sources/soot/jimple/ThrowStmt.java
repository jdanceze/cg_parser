package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/ThrowStmt.class */
public interface ThrowStmt extends Stmt {
    ValueBox getOpBox();

    Value getOp();

    void setOp(Value value);
}
