package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/ExitMonitorStmt.class */
public interface ExitMonitorStmt extends MonitorStmt {
    @Override // soot.jimple.MonitorStmt
    Value getOp();

    @Override // soot.jimple.MonitorStmt
    void setOp(Value value);

    @Override // soot.jimple.MonitorStmt
    ValueBox getOpBox();
}
