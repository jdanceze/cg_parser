package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/MonitorStmt.class */
public interface MonitorStmt extends Stmt {
    Value getOp();

    void setOp(Value value);

    ValueBox getOpBox();
}
