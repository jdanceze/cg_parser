package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JInvokeStmt.class */
public class JInvokeStmt extends AbstractStmt implements InvokeStmt {
    protected final ValueBox invokeExprBox;

    public JInvokeStmt(Value c) {
        this(Jimple.v().newInvokeExprBox(c));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JInvokeStmt(ValueBox invokeExprBox) {
        this.invokeExprBox = invokeExprBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JInvokeStmt(Jimple.cloneIfNecessary(getInvokeExpr()));
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public boolean containsInvokeExpr() {
        return true;
    }

    public String toString() {
        return this.invokeExprBox.getValue().toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        this.invokeExprBox.toString(up);
    }

    @Override // soot.jimple.InvokeStmt
    public void setInvokeExpr(Value invokeExpr) {
        this.invokeExprBox.setValue(invokeExpr);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public InvokeExpr getInvokeExpr() {
        return (InvokeExpr) this.invokeExprBox.getValue();
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public ValueBox getInvokeExprBox() {
        return this.invokeExprBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>(this.invokeExprBox.getValue().getUseBoxes());
        list.add(this.invokeExprBox);
        return list;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseInvokeStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        InvokeExpr ie = getInvokeExpr();
        context.setCurrentUnit(this);
        ((ConvertToBaf) ie).convertToBaf(context, out);
        Type returnType = ie.getMethodRef().returnType();
        if (!VoidType.v().equals(returnType)) {
            Unit u = Baf.v().newPopInst(returnType);
            u.addAllTagsOf(this);
            out.add(u);
        }
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
