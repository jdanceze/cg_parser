package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
import soot.jimple.RetStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JRetStmt.class */
public class JRetStmt extends AbstractStmt implements RetStmt {
    protected final ValueBox stmtAddressBox;

    public JRetStmt(Value stmtAddress) {
        this(Jimple.v().newLocalBox(stmtAddress));
    }

    protected JRetStmt(ValueBox stmtAddressBox) {
        this.stmtAddressBox = stmtAddressBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JRetStmt(Jimple.cloneIfNecessary(getStmtAddress()));
    }

    public String toString() {
        return "ret " + this.stmtAddressBox.getValue().toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("ret ");
        this.stmtAddressBox.toString(up);
    }

    @Override // soot.jimple.RetStmt
    public Value getStmtAddress() {
        return this.stmtAddressBox.getValue();
    }

    @Override // soot.jimple.RetStmt
    public ValueBox getStmtAddressBox() {
        return this.stmtAddressBox;
    }

    @Override // soot.jimple.RetStmt
    public void setStmtAddress(Value stmtAddress) {
        this.stmtAddressBox.setValue(stmtAddress);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        List<ValueBox> useBoxes = new ArrayList<>(this.stmtAddressBox.getValue().getUseBoxes());
        useBoxes.add(this.stmtAddressBox);
        return useBoxes;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseRetStmt(this);
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
