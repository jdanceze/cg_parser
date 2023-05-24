package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.grimp.Grimp;
import soot.grimp.internal.GStaticInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DStaticInvokeExpr.class */
public class DStaticInvokeExpr extends GStaticInvokeExpr {
    public DStaticInvokeExpr(SootMethodRef methodRef, List args) {
        super(methodRef, args);
    }

    @Override // soot.jimple.internal.AbstractStaticInvokeExpr, soot.Value
    public void toString(UnitPrinter up) {
        up.type(this.methodRef.declaringClass().getType());
        up.literal(".");
        super.toString(up);
    }

    @Override // soot.grimp.internal.GStaticInvokeExpr, soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        ArrayList clonedArgs = new ArrayList(getArgCount());
        for (int i = 0; i < getArgCount(); i++) {
            clonedArgs.add(i, Grimp.cloneIfNecessary(getArg(i)));
        }
        return new DStaticInvokeExpr(this.methodRef, clonedArgs);
    }
}
