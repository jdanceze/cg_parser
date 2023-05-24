package soot.dava.internal.javaRep;

import soot.Type;
import soot.UnitPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DShortcutAssignStmt.class */
public class DShortcutAssignStmt extends DAssignStmt {
    Type type;

    public DShortcutAssignStmt(DAssignStmt assignStmt, Type type) {
        super(assignStmt.getLeftOpBox(), assignStmt.getRightOpBox());
        this.type = type;
    }

    @Override // soot.dava.internal.javaRep.DAssignStmt, soot.Unit
    public void toString(UnitPrinter up) {
        up.type(this.type);
        up.literal(Instruction.argsep);
        super.toString(up);
    }

    @Override // soot.dava.internal.javaRep.DAssignStmt
    public String toString() {
        return String.valueOf(this.type.toString()) + Instruction.argsep + this.leftBox.getValue().toString() + " = " + this.rightBox.getValue().toString();
    }
}
