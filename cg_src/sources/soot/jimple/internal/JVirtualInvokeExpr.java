package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.tagkit.SourceFileTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JVirtualInvokeExpr.class */
public class JVirtualInvokeExpr extends AbstractVirtualInvokeExpr {
    public JVirtualInvokeExpr(Value base, SootMethodRef methodRef, List<? extends Value> args) {
        super(Jimple.v().newLocalBox(base), methodRef, new ValueBox[args.size()]);
        if (!Options.v().ignore_resolution_errors()) {
            SootClass sc = methodRef.declaringClass();
            sc.checkLevelIgnoreResolving(1);
            if (sc.isInterface()) {
                SourceFileTag tag = (SourceFileTag) sc.getTag(SourceFileTag.NAME);
                throw new RuntimeException("Trying to create virtual invoke expression for interface type (" + sc.getName() + " in file " + (tag != null ? tag.getAbsolutePath() : "unknown") + "). Use JInterfaceInvokeExpr instead!");
            }
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
        return new JVirtualInvokeExpr(getBase(), this.methodRef, clonedArgs);
    }
}
