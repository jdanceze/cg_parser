package soot.grimp.internal;

import soot.grimp.Grimp;
import soot.jimple.internal.JTableSwitchStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GTableSwitchStmt.class */
public class GTableSwitchStmt extends JTableSwitchStmt {
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public GTableSwitchStmt(soot.Value r9, int r10, int r11, java.util.List<? extends soot.Unit> r12, soot.Unit r13) {
        /*
            r8 = this;
            r0 = r8
            soot.grimp.Grimp r1 = soot.grimp.Grimp.v()
            r2 = r9
            soot.ValueBox r1 = r1.newExprBox(r2)
            r2 = r10
            r3 = r11
            r4 = r12
            soot.grimp.Grimp r5 = soot.grimp.Grimp.v()
            r6 = r5
            java.lang.Class r6 = r6.getClass()
            void r5 = this::newStmtBox
            soot.UnitBox[] r4 = getTargetBoxesArray(r4, r5)
            soot.grimp.Grimp r5 = soot.grimp.Grimp.v()
            r6 = r13
            soot.UnitBox r5 = r5.newStmtBox(r6)
            r0.<init>(r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.grimp.internal.GTableSwitchStmt.<init>(soot.Value, int, int, java.util.List, soot.Unit):void");
    }

    @Override // soot.jimple.internal.JTableSwitchStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GTableSwitchStmt(Grimp.cloneIfNecessary(getKey()), getLowIndex(), getHighIndex(), getTargets(), getDefaultTarget());
    }
}
