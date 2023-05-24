package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractStaticInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GStaticInvokeExpr.class */
public class GStaticInvokeExpr extends AbstractStaticInvokeExpr {
    public GStaticInvokeExpr(SootMethodRef methodRef, List<? extends Value> args) {
        super(methodRef, new ValueBox[args.size()]);
        Grimp grmp = Grimp.v();
        ListIterator<? extends Value> it = args.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.argBoxes[it.previousIndex()] = grmp.newExprBox(v);
        }
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        int count = getArgCount();
        List<Value> clonedArgs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            clonedArgs.add(Grimp.cloneIfNecessary(getArg(i)));
        }
        return new GStaticInvokeExpr(this.methodRef, clonedArgs);
    }
}
