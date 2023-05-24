package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/RetStmt.class */
public interface RetStmt extends Stmt {
    Value getStmtAddress();

    ValueBox getStmtAddressBox();

    void setStmtAddress(Value value);
}
