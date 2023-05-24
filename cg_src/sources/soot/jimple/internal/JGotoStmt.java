package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JGotoStmt.class */
public class JGotoStmt extends AbstractStmt implements GotoStmt {
    protected final UnitBox targetBox;
    protected final List<UnitBox> targetBoxes;

    public JGotoStmt(Unit target) {
        this(Jimple.v().newStmtBox(target));
    }

    public JGotoStmt(UnitBox box) {
        this.targetBox = box;
        this.targetBoxes = Collections.singletonList(box);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JGotoStmt(getTarget());
    }

    public String toString() {
        Unit t = getTarget();
        String target = t.branches() ? "(branch)" : t.toString();
        return "goto [?= " + target + "]";
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("goto ");
        this.targetBox.toString(up);
    }

    @Override // soot.jimple.GotoStmt
    public Unit getTarget() {
        return this.targetBox.getUnit();
    }

    @Override // soot.jimple.GotoStmt
    public void setTarget(Unit target) {
        this.targetBox.setUnit(target);
    }

    @Override // soot.jimple.GotoStmt
    public UnitBox getTargetBox() {
        return this.targetBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<UnitBox> getUnitBoxes() {
        return this.targetBoxes;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseGotoStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Baf vaf = Baf.v();
        Unit u = vaf.newGotoInst(vaf.newPlaceholderInst(getTarget()));
        u.addAllTagsOf(this);
        out.add(u);
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return false;
    }

    @Override // soot.Unit
    public boolean branches() {
        return true;
    }
}
