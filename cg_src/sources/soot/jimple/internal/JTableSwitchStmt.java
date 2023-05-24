package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.PlaceholderInst;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JTableSwitchStmt.class */
public class JTableSwitchStmt extends AbstractSwitchStmt implements TableSwitchStmt {
    protected int lowIndex;
    protected int highIndex;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public JTableSwitchStmt(soot.Value r9, int r10, int r11, java.util.List<? extends soot.Unit> r12, soot.Unit r13) {
        /*
            r8 = this;
            r0 = r8
            soot.jimple.Jimple r1 = soot.jimple.Jimple.v()
            r2 = r9
            soot.ValueBox r1 = r1.newImmediateBox(r2)
            r2 = r10
            r3 = r11
            r4 = r12
            soot.jimple.Jimple r5 = soot.jimple.Jimple.v()
            r6 = r5
            java.lang.Class r6 = r6.getClass()
            void r5 = this::newStmtBox
            soot.UnitBox[] r4 = getTargetBoxesArray(r4, r5)
            soot.jimple.Jimple r5 = soot.jimple.Jimple.v()
            r6 = r13
            soot.UnitBox r5 = r5.newStmtBox(r6)
            r0.<init>(r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.internal.JTableSwitchStmt.<init>(soot.Value, int, int, java.util.List, soot.Unit):void");
    }

    public JTableSwitchStmt(Value key, int lowIndex, int highIndex, List<? extends UnitBox> targets, UnitBox defaultTarget) {
        this(Jimple.v().newImmediateBox(key), lowIndex, highIndex, (UnitBox[]) targets.toArray(new UnitBox[targets.size()]), defaultTarget);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JTableSwitchStmt(ValueBox keyBox, int lowIndex, int highIndex, UnitBox[] targetBoxes, UnitBox defaultTargetBox) {
        super(keyBox, defaultTargetBox, targetBoxes);
        if (lowIndex > highIndex) {
            throw new RuntimeException("Error creating tableswitch: lowIndex(" + lowIndex + ") can't be greater than highIndex(" + highIndex + ").");
        }
        this.lowIndex = lowIndex;
        this.highIndex = highIndex;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JTableSwitchStmt(Jimple.cloneIfNecessary(getKey()), this.lowIndex, this.highIndex, getTargets(), getDefaultTarget());
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("tableswitch(");
        buf.append(this.keyBox.getValue().toString()).append(')').append(' ');
        buf.append('{').append(' ');
        int low = this.lowIndex;
        int high = this.highIndex;
        for (int i = low; i < high; i++) {
            buf.append("    case ").append(i).append(": goto ");
            Unit target = getTarget(i - low);
            buf.append(target == this ? "self" : target).append(';').append(' ');
        }
        buf.append("    case ").append(high).append(": goto ");
        Unit target2 = getTarget(high - low);
        buf.append(target2 == this ? "self" : target2).append(';').append(' ');
        Unit target3 = getDefaultTarget();
        buf.append("    default: goto ");
        buf.append(target3 == this ? "self" : target3).append(';').append(' ');
        buf.append('}');
        return buf.toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(Jimple.TABLESWITCH);
        up.literal("(");
        this.keyBox.toString(up);
        up.literal(")");
        up.newline();
        up.literal("{");
        up.newline();
        int high = this.highIndex;
        for (int i = this.lowIndex; i < high; i++) {
            printCaseTarget(up, i);
        }
        printCaseTarget(up, high);
        up.literal("    default: goto ");
        this.defaultTargetBox.toString(up);
        up.literal(";");
        up.newline();
        up.literal("}");
    }

    private void printCaseTarget(UnitPrinter up, int targetIndex) {
        up.literal("    case ");
        up.literal(Integer.toString(targetIndex));
        up.literal(": goto ");
        this.targetBoxes[targetIndex - this.lowIndex].toString(up);
        up.literal(";");
        up.newline();
    }

    @Override // soot.jimple.TableSwitchStmt
    public void setLowIndex(int lowIndex) {
        this.lowIndex = lowIndex;
    }

    @Override // soot.jimple.TableSwitchStmt
    public void setHighIndex(int highIndex) {
        this.highIndex = highIndex;
    }

    @Override // soot.jimple.TableSwitchStmt
    public int getLowIndex() {
        return this.lowIndex;
    }

    @Override // soot.jimple.TableSwitchStmt
    public int getHighIndex() {
        return this.highIndex;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseTableSwitchStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getKey()).convertToBaf(context, out);
        Baf vaf = Baf.v();
        List<Unit> targets = getTargets();
        List<PlaceholderInst> targetPlaceholders = new ArrayList<>(targets.size());
        for (Unit target : targets) {
            targetPlaceholders.add(vaf.newPlaceholderInst(target));
        }
        Unit u = vaf.newTableSwitchInst(vaf.newPlaceholderInst(getDefaultTarget()), this.lowIndex, this.highIndex, targetPlaceholders);
        u.addAllTagsOf(this);
        out.add(u);
    }
}
