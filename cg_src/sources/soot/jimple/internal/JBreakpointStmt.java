package soot.jimple.internal;

import soot.UnitPrinter;
import soot.jimple.BreakpointStmt;
import soot.jimple.Jimple;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JBreakpointStmt.class */
public class JBreakpointStmt extends AbstractStmt implements BreakpointStmt {
    public String toString() {
        return Jimple.BREAKPOINT;
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(Jimple.BREAKPOINT);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseBreakpointStmt(this);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JBreakpointStmt();
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return true;
    }

    @Override // soot.Unit
    public boolean branches() {
        return false;
    }
}
