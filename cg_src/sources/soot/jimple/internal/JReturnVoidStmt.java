package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.JimpleToBafContext;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JReturnVoidStmt.class */
public class JReturnVoidStmt extends AbstractStmt implements ReturnVoidStmt {
    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JReturnVoidStmt();
    }

    public String toString() {
        return "return";
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("return");
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseReturnVoidStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newReturnVoidInst();
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
