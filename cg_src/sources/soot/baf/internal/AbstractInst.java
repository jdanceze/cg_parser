package soot.baf.internal;

import soot.AbstractUnit;
import soot.UnitPrinter;
import soot.baf.Inst;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractInst.class */
public abstract class AbstractInst extends AbstractUnit implements Inst {
    public abstract String getName();

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        throw new RuntimeException("undefined clone for: " + toString());
    }

    public String toString() {
        return String.valueOf(getName()) + getParameters();
    }

    public void toString(UnitPrinter up) {
        up.literal(getName());
        getParameters(up);
    }

    @Override // soot.baf.Inst
    public int getInCount() {
        throw new RuntimeException("undefined " + toString() + "!");
    }

    @Override // soot.baf.Inst
    public int getOutCount() {
        throw new RuntimeException("undefined " + toString() + "!");
    }

    @Override // soot.baf.Inst
    public int getNetCount() {
        return getOutCount() - getInCount();
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return true;
    }

    public boolean branches() {
        return false;
    }

    @Override // soot.baf.Inst
    public int getInMachineCount() {
        throw new RuntimeException("undefined" + toString() + "!");
    }

    @Override // soot.baf.Inst
    public int getOutMachineCount() {
        throw new RuntimeException("undefined" + toString() + "!");
    }

    @Override // soot.baf.Inst
    public int getNetMachineCount() {
        return getOutMachineCount() - getInMachineCount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getParameters() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void getParameters(UnitPrinter up) {
    }

    @Override // soot.baf.Inst
    public boolean containsInvokeExpr() {
        return false;
    }

    @Override // soot.baf.Inst
    public boolean containsArrayRef() {
        return false;
    }

    @Override // soot.baf.Inst
    public boolean containsFieldRef() {
        return false;
    }

    @Override // soot.baf.Inst
    public boolean containsNewExpr() {
        return false;
    }
}
