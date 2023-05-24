package soot.coffi;

import soot.UnitPrinter;
import soot.jimple.StmtSwitch;
import soot.jimple.internal.AbstractStmt;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/coffi/FutureStmt.class */
class FutureStmt extends AbstractStmt {
    public Object object;

    public FutureStmt(Object object) {
        this.object = object;
    }

    public FutureStmt() {
    }

    public String toString() {
        return "<futurestmt>";
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("<futurestmt>");
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).defaultCase(this);
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        throw new RuntimeException();
    }

    @Override // soot.Unit
    public boolean branches() {
        throw new RuntimeException();
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        throw new RuntimeException();
    }
}
