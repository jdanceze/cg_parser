package soot.jimple;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/StmtSwitch.class */
public interface StmtSwitch extends Switch {
    void caseBreakpointStmt(BreakpointStmt breakpointStmt);

    void caseInvokeStmt(InvokeStmt invokeStmt);

    void caseAssignStmt(AssignStmt assignStmt);

    void caseIdentityStmt(IdentityStmt identityStmt);

    void caseEnterMonitorStmt(EnterMonitorStmt enterMonitorStmt);

    void caseExitMonitorStmt(ExitMonitorStmt exitMonitorStmt);

    void caseGotoStmt(GotoStmt gotoStmt);

    void caseIfStmt(IfStmt ifStmt);

    void caseLookupSwitchStmt(LookupSwitchStmt lookupSwitchStmt);

    void caseNopStmt(NopStmt nopStmt);

    void caseRetStmt(RetStmt retStmt);

    void caseReturnStmt(ReturnStmt returnStmt);

    void caseReturnVoidStmt(ReturnVoidStmt returnVoidStmt);

    void caseTableSwitchStmt(TableSwitchStmt tableSwitchStmt);

    void caseThrowStmt(ThrowStmt throwStmt);

    void defaultCase(Object obj);
}
