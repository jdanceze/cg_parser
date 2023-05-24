package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.internal.AbstractDefinitionStmt;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DAssignStmt.class */
public class DAssignStmt extends AbstractDefinitionStmt implements AssignStmt {
    public DAssignStmt(ValueBox left, ValueBox right) {
        super(left, right);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new DAssignStmt(this.leftBox, this.rightBox);
    }

    @Override // soot.jimple.AssignStmt
    public void setLeftOp(Value variable) {
        this.leftBox.setValue(variable);
    }

    @Override // soot.jimple.AssignStmt
    public void setRightOp(Value rvalue) {
        this.rightBox.setValue(rvalue);
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        this.leftBox.toString(up);
        up.literal(" = ");
        this.rightBox.toString(up);
    }

    public String toString() {
        return String.valueOf(this.leftBox.getValue().toString()) + " = " + this.rightBox.getValue().toString();
    }
}
