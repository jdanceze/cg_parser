package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JInterfaceInvokeExpr.class */
public class JInterfaceInvokeExpr extends AbstractInterfaceInvokeExpr {
    public JInterfaceInvokeExpr(Value base, SootMethodRef methodRef, List<? extends Value> args) {
        super(Jimple.v().newLocalBox(base), methodRef, new ValueBox[args.size()]);
        SootClass declaringClass = methodRef.declaringClass();
        declaringClass.checkLevelIgnoreResolving(1);
        if (!declaringClass.isInterface() && !declaringClass.isPhantom()) {
            throw new RuntimeException("Trying to create interface invoke expression for non-interface type: " + declaringClass + " Use JVirtualInvokeExpr or JSpecialInvokeExpr instead!");
        }
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
        return new JInterfaceInvokeExpr(Jimple.cloneIfNecessary(getBase()), this.methodRef, clonedArgs);
    }
}
