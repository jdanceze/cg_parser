package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/AbstractStmtSwitch.class */
public abstract class AbstractStmtSwitch<T> implements StmtSwitch {
    T result;

    @Override // soot.jimple.StmtSwitch
    public void caseBreakpointStmt(BreakpointStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseInvokeStmt(InvokeStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseAssignStmt(AssignStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseRetStmt(RetStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
        defaultCase(stmt);
    }

    @Override // soot.jimple.StmtSwitch
    public void defaultCase(Object obj) {
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}
