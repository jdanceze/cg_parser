package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/ReturnStmt.class */
public interface ReturnStmt extends Stmt {
    ValueBox getOpBox();

    void setOp(Value value);

    Value getOp();
}
