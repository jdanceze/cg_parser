package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.jimple.Jimple;
import soot.jimple.internal.AbstractStmt;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DAbruptStmt.class */
public class DAbruptStmt extends AbstractStmt {
    private String command;
    private SETNodeLabel label;
    public boolean surpressDestinationLabel;

    public DAbruptStmt(String command, SETNodeLabel label) {
        this.command = command;
        this.label = label;
        label.set_Name();
        this.surpressDestinationLabel = false;
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return false;
    }

    @Override // soot.Unit
    public boolean branches() {
        return false;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new DAbruptStmt(this.command, this.label);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(this.command);
        if (!this.surpressDestinationLabel && this.label.toString() != null) {
            b.append(Instruction.argsep);
            b.append(this.label.toString());
        }
        return b.toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(this.command);
        if (!this.surpressDestinationLabel && this.label.toString() != null) {
            up.literal(Instruction.argsep);
            up.literal(this.label.toString());
        }
    }

    public boolean is_Continue() {
        return this.command.equals("continue");
    }

    public boolean is_Break() {
        return this.command.equals(Jimple.BREAK);
    }

    public void setLabel(SETNodeLabel label) {
        this.label = label;
    }

    public SETNodeLabel getLabel() {
        return this.label;
    }
}
