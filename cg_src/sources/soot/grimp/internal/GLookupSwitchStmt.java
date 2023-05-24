package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.grimp.Grimp;
import soot.jimple.IntConstant;
import soot.jimple.internal.JLookupSwitchStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GLookupSwitchStmt.class */
public class GLookupSwitchStmt extends JLookupSwitchStmt {
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public GLookupSwitchStmt(soot.Value r8, java.util.List<soot.jimple.IntConstant> r9, java.util.List<? extends soot.Unit> r10, soot.Unit r11) {
        /*
            r7 = this;
            r0 = r7
            soot.grimp.Grimp r1 = soot.grimp.Grimp.v()
            r2 = r8
            soot.ValueBox r1 = r1.newExprBox(r2)
            r2 = r9
            r3 = r10
            soot.grimp.Grimp r4 = soot.grimp.Grimp.v()
            r5 = r4
            java.lang.Class r5 = r5.getClass()
            void r4 = this::newStmtBox
            soot.UnitBox[] r3 = getTargetBoxesArray(r3, r4)
            soot.grimp.Grimp r4 = soot.grimp.Grimp.v()
            r5 = r11
            soot.UnitBox r4 = r4.newStmtBox(r5)
            r0.<init>(r1, r2, r3, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.grimp.internal.GLookupSwitchStmt.<init>(soot.Value, java.util.List, java.util.List, soot.Unit):void");
    }

    @Override // soot.jimple.internal.JLookupSwitchStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        List<IntConstant> clonedLookupValues = new ArrayList<>(this.lookupValues.size());
        for (IntConstant c : this.lookupValues) {
            clonedLookupValues.add(IntConstant.v(c.value));
        }
        return new GLookupSwitchStmt(Grimp.cloneIfNecessary(getKey()), clonedLookupValues, getTargets(), getDefaultTarget());
    }
}
