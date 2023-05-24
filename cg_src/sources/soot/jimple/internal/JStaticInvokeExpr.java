package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JStaticInvokeExpr.class */
public class JStaticInvokeExpr extends AbstractStaticInvokeExpr {
    public JStaticInvokeExpr(SootMethodRef methodRef, List<? extends Value> args) {
        super(methodRef, new ValueBox[args.size()]);
        Jimple jimp = Jimple.v();
        ListIterator<? extends Value> it = args.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.argBoxes[it.previousIndex()] = jimp.newImmediateBox(v);
        }
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        int count = getArgCount();
        List<Value> clonedArgs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            clonedArgs.add(Jimple.cloneIfNecessary(getArg(i)));
        }
        return new JStaticInvokeExpr(this.methodRef, clonedArgs);
    }
}
