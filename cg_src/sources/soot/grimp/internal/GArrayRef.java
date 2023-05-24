package soot.grimp.internal;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.jimple.internal.JArrayRef;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GArrayRef.class */
public class GArrayRef extends JArrayRef implements Precedence {
    public GArrayRef(Value base, Value index) {
        super(Grimp.v().newObjExprBox(base), Grimp.v().newExprBox(index));
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 950;
    }

    @Override // soot.jimple.internal.JArrayRef, soot.Value
    public void toString(UnitPrinter up) {
        boolean needsBrackets = PrecedenceTest.needsBrackets(this.baseBox, this);
        if (needsBrackets) {
            up.literal("(");
        }
        this.baseBox.toString(up);
        if (needsBrackets) {
            up.literal(")");
        }
        up.literal("[");
        this.indexBox.toString(up);
        up.literal("]");
    }

    @Override // soot.jimple.internal.JArrayRef
    public String toString() {
        Value op1 = getBase();
        String leftOp = op1.toString();
        if ((op1 instanceof Precedence) && ((Precedence) op1).getPrecedence() < getPrecedence()) {
            leftOp = "(" + leftOp + ")";
        }
        return String.valueOf(leftOp) + "[" + getIndex().toString() + "]";
    }

    @Override // soot.jimple.internal.JArrayRef, soot.Value
    public Object clone() {
        return new GArrayRef(Grimp.cloneIfNecessary(getBase()), Grimp.cloneIfNecessary(getIndex()));
    }
}
