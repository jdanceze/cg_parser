package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/EnterMonitorStmt.class */
public interface EnterMonitorStmt extends MonitorStmt {
    @Override // soot.jimple.MonitorStmt
    Value getOp();

    @Override // soot.jimple.MonitorStmt
    void setOp(Value value);

    @Override // soot.jimple.MonitorStmt
    ValueBox getOpBox();
}
