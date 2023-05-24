package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.jimple.internal.AbstractInterfaceInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GInterfaceInvokeExpr.class */
public class GInterfaceInvokeExpr extends AbstractInterfaceInvokeExpr implements Precedence {
    public GInterfaceInvokeExpr(Value base, SootMethodRef methodRef, List<? extends Value> args) {
        super(Grimp.v().newObjExprBox(base), methodRef, new ValueBox[args.size()]);
        Grimp grmp = Grimp.v();
        ListIterator<? extends Value> it = args.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.argBoxes[it.previousIndex()] = grmp.newExprBox(v);
        }
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 950;
    }

    @Override // soot.jimple.internal.AbstractInterfaceInvokeExpr
    public String toString() {
        Value base = getBase();
        String baseString = base.toString();
        if ((base instanceof Precedence) && ((Precedence) base).getPrecedence() < getPrecedence()) {
            baseString = "(" + baseString + ")";
        }
        StringBuilder buf = new StringBuilder(baseString);
        buf.append('.').append(this.methodRef.getSignature()).append('(');
        if (this.argBoxes != null) {
            int e = this.argBoxes.length;
            for (int i = 0; i < e; i++) {
                if (i != 0) {
                    buf.append(", ");
                }
                buf.append(this.argBoxes[i].getValue().toString());
            }
        }
        buf.append(')');
        return buf.toString();
    }

    @Override // soot.jimple.internal.AbstractInterfaceInvokeExpr, soot.Value
    public void toString(UnitPrinter up) {
        boolean needsBrackets = PrecedenceTest.needsBrackets(this.baseBox, this);
        if (needsBrackets) {
            up.literal("(");
        }
        this.baseBox.toString(up);
        if (needsBrackets) {
            up.literal(")");
        }
        up.literal(".");
        up.methodRef(this.methodRef);
        up.literal("(");
        if (this.argBoxes != null) {
            int e = this.argBoxes.length;
            for (int i = 0; i < e; i++) {
                if (i != 0) {
                    up.literal(", ");
                }
                this.argBoxes[i].toString(up);
            }
        }
        up.literal(")");
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        int count = getArgCount();
        List<Value> clonedArgs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            clonedArgs.add(Grimp.cloneIfNecessary(getArg(i)));
        }
        return new GInterfaceInvokeExpr(Grimp.cloneIfNecessary(getBase()), this.methodRef, clonedArgs);
    }
}
