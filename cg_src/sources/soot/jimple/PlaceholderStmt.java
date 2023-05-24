package soot.jimple;

import soot.Unit;
import soot.UnitPrinter;
import soot.jimple.internal.AbstractStmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/PlaceholderStmt.class */
public class PlaceholderStmt extends AbstractStmt {
    private Unit source;

    public String toString() {
        return "<placeholder: " + this.source.toString() + ">";
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("<placeholder: ");
        this.source.toString(up);
        up.literal(">");
    }

    PlaceholderStmt(Unit source) {
        this.source = source;
    }

    public Unit getSource() {
        return this.source;
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
