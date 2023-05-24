package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.PlaceholderInst;
import soot.jimple.ConvertToBaf;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JLookupSwitchStmt.class */
public class JLookupSwitchStmt extends AbstractSwitchStmt implements LookupSwitchStmt {
    protected List<IntConstant> lookupValues;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public JLookupSwitchStmt(soot.Value r8, java.util.List<soot.jimple.IntConstant> r9, java.util.List<? extends soot.Unit> r10, soot.Unit r11) {
        /*
            r7 = this;
            r0 = r7
            soot.jimple.Jimple r1 = soot.jimple.Jimple.v()
            r2 = r8
            soot.ValueBox r1 = r1.newImmediateBox(r2)
            r2 = r9
            r3 = r10
            soot.jimple.Jimple r4 = soot.jimple.Jimple.v()
            r5 = r4
            java.lang.Class r5 = r5.getClass()
            void r4 = this::newStmtBox
            soot.UnitBox[] r3 = getTargetBoxesArray(r3, r4)
            soot.jimple.Jimple r4 = soot.jimple.Jimple.v()
            r5 = r11
            soot.UnitBox r4 = r4.newStmtBox(r5)
            r0.<init>(r1, r2, r3, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.internal.JLookupSwitchStmt.<init>(soot.Value, java.util.List, java.util.List, soot.Unit):void");
    }

    public JLookupSwitchStmt(Value key, List<IntConstant> lookupValues, List<? extends UnitBox> targets, UnitBox defaultTarget) {
        this(Jimple.v().newImmediateBox(key), lookupValues, (UnitBox[]) targets.toArray(new UnitBox[targets.size()]), defaultTarget);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JLookupSwitchStmt(ValueBox keyBox, List<IntConstant> lookupValues, UnitBox[] targetBoxes, UnitBox defaultTargetBox) {
        super(keyBox, defaultTargetBox, targetBoxes);
        setLookupValues(lookupValues);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        List<IntConstant> clonedLookupValues = new ArrayList<>(this.lookupValues.size());
        for (IntConstant c : this.lookupValues) {
            clonedLookupValues.add(IntConstant.v(c.value));
        }
        return new JLookupSwitchStmt(getKey(), clonedLookupValues, getTargets(), getDefaultTarget());
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("lookupswitch(");
        buf.append(this.keyBox.getValue().toString()).append(')').append(' ');
        buf.append('{').append(' ');
        ListIterator<IntConstant> it = this.lookupValues.listIterator();
        while (it.hasNext()) {
            IntConstant c = it.next();
            buf.append("    case ").append(c).append(": goto ");
            Unit target = getTarget(it.previousIndex());
            buf.append(target == this ? "self" : target).append(';').append(' ');
        }
        buf.append("    default: goto ");
        Unit target2 = getDefaultTarget();
        buf.append(target2 == this ? "self" : target2).append(';').append(' ');
        buf.append('}');
        return buf.toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("lookupswitch(");
        this.keyBox.toString(up);
        up.literal(")");
        up.newline();
        up.literal("{");
        up.newline();
        ListIterator<IntConstant> it = this.lookupValues.listIterator();
        while (it.hasNext()) {
            IntConstant c = it.next();
            up.literal("    case ");
            up.constant(c);
            up.literal(": goto ");
            this.targetBoxes[it.previousIndex()].toString(up);
            up.literal(";");
            up.newline();
        }
        up.literal("    default: goto ");
        this.defaultTargetBox.toString(up);
        up.literal(";");
        up.newline();
        up.literal("}");
    }

    @Override // soot.jimple.LookupSwitchStmt
    public void setLookupValues(List<IntConstant> lookupValues) {
        this.lookupValues = new ArrayList(lookupValues);
    }

    @Override // soot.jimple.LookupSwitchStmt
    public void setLookupValue(int index, int value) {
        this.lookupValues.set(index, IntConstant.v(value));
    }

    @Override // soot.jimple.LookupSwitchStmt
    public int getLookupValue(int index) {
        return this.lookupValues.get(index).value;
    }

    @Override // soot.jimple.LookupSwitchStmt
    public List<IntConstant> getLookupValues() {
        return Collections.unmodifiableList(this.lookupValues);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseLookupSwitchStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getKey()).convertToBaf(context, out);
        Baf baf = Baf.v();
        List<Unit> targets = getTargets();
        List<PlaceholderInst> targetPlaceholders = new ArrayList<>(targets.size());
        for (Unit target : targets) {
            targetPlaceholders.add(baf.newPlaceholderInst(target));
        }
        Unit u = baf.newLookupSwitchInst(baf.newPlaceholderInst(getDefaultTarget()), getLookupValues(), targetPlaceholders);
        u.addAllTagsOf(this);
        out.add(u);
    }
}
