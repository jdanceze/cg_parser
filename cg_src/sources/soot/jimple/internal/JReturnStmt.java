package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.ReturnStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JReturnStmt.class */
public class JReturnStmt extends AbstractOpStmt implements ReturnStmt {
    public JReturnStmt(Value returnValue) {
        this(Jimple.v().newImmediateBox(returnValue));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JReturnStmt(ValueBox returnValueBox) {
        super(returnValueBox);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JReturnStmt(Jimple.cloneIfNecessary(getOp()));
    }

    public String toString() {
        return "return " + this.opBox.getValue().toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("return ");
        this.opBox.toString(up);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseReturnStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getOp()).convertToBaf(context, out);
        Unit u = Baf.v().newReturnInst(getOp().getType());
        u.addAllTagsOf(this);
        out.add(u);
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return false;
    }

    @Override // soot.Unit
    public boolean branches() {
        return false;
    }
}
