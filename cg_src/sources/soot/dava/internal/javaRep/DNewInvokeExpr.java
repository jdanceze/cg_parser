package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.RefType;
import soot.SootMethodRef;
import soot.grimp.Grimp;
import soot.grimp.internal.GNewInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DNewInvokeExpr.class */
public class DNewInvokeExpr extends GNewInvokeExpr {
    public DNewInvokeExpr(RefType type, SootMethodRef methodRef, List args) {
        super(type, methodRef, args);
    }

    @Override // soot.grimp.internal.GNewInvokeExpr, soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        ArrayList clonedArgs = new ArrayList(getArgCount());
        for (int i = 0; i < getArgCount(); i++) {
            clonedArgs.add(i, Grimp.cloneIfNecessary(getArg(i)));
        }
        return new DNewInvokeExpr((RefType) getType(), this.methodRef, clonedArgs);
    }
}
