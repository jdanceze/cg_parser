package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.NopStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JNopStmt.class */
public class JNopStmt extends AbstractStmt implements NopStmt {
    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JNopStmt();
    }

    public String toString() {
        return Jimple.NOP;
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(Jimple.NOP);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseNopStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newNopInst();
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
