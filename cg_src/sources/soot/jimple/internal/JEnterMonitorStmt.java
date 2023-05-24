package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JEnterMonitorStmt.class */
public class JEnterMonitorStmt extends AbstractOpStmt implements EnterMonitorStmt {
    public JEnterMonitorStmt(Value op) {
        this(Jimple.v().newImmediateBox(op));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JEnterMonitorStmt(ValueBox opBox) {
        super(opBox);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JEnterMonitorStmt(Jimple.cloneIfNecessary(getOp()));
    }

    public String toString() {
        return "entermonitor " + this.opBox.getValue().toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("entermonitor ");
        this.opBox.toString(up);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseEnterMonitorStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getOp()).convertToBaf(context, out);
        Unit u = Baf.v().newEnterMonitorInst();
        u.addAllTagsOf(this);
        out.add(u);
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
