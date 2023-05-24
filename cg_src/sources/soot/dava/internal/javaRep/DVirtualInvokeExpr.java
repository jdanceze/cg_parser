package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import soot.NullType;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.grimp.internal.GVirtualInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DVirtualInvokeExpr.class */
public class DVirtualInvokeExpr extends GVirtualInvokeExpr {
    private HashSet<Object> thisLocals;

    public DVirtualInvokeExpr(Value base, SootMethodRef methodRef, List args, HashSet<Object> thisLocals) {
        super(base, methodRef, args);
        this.thisLocals = thisLocals;
    }

    @Override // soot.grimp.internal.GVirtualInvokeExpr, soot.jimple.internal.AbstractVirtualInvokeExpr, soot.Value
    public void toString(UnitPrinter up) {
        if (getBase().getType() instanceof NullType) {
            up.literal("((");
            up.type(this.methodRef.declaringClass().getType());
            up.literal(") ");
            if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
                up.literal("(");
            }
            this.baseBox.toString(up);
            if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
                up.literal(")");
            }
            up.literal(")");
            up.literal(".");
            up.methodRef(this.methodRef);
            up.literal("(");
            if (this.argBoxes != null) {
                for (int i = 0; i < this.argBoxes.length; i++) {
                    if (i != 0) {
                        up.literal(", ");
                    }
                    this.argBoxes[i].toString(up);
                }
            }
            up.literal(")");
            return;
        }
        super.toString(up);
    }

    @Override // soot.grimp.internal.GVirtualInvokeExpr, soot.jimple.internal.AbstractVirtualInvokeExpr
    public String toString() {
        if (getBase().getType() instanceof NullType) {
            StringBuffer b = new StringBuffer();
            b.append("((");
            b.append(this.methodRef.declaringClass().getJavaStyleName());
            b.append(") ");
            String baseStr = getBase().toString();
            if ((getBase() instanceof Precedence) && ((Precedence) getBase()).getPrecedence() < getPrecedence()) {
                baseStr = "(" + baseStr + ")";
            }
            b.append(baseStr);
            b.append(").");
            b.append(this.methodRef.name());
            b.append("(");
            if (this.argBoxes != null) {
                for (int i = 0; i < this.argBoxes.length; i++) {
                    if (i != 0) {
                        b.append(", ");
                    }
                    b.append(this.argBoxes[i].getValue().toString());
                }
            }
            b.append(")");
            return b.toString();
        }
        return super.toString();
    }

    @Override // soot.grimp.internal.GVirtualInvokeExpr, soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        ArrayList clonedArgs = new ArrayList(getArgCount());
        for (int i = 0; i < getArgCount(); i++) {
            clonedArgs.add(i, Grimp.cloneIfNecessary(getArg(i)));
        }
        return new DVirtualInvokeExpr(Grimp.cloneIfNecessary(getBase()), this.methodRef, clonedArgs, this.thisLocals);
    }
}
