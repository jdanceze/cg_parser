package soot.grimp.internal;

import soot.SootFieldRef;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractInstanceFieldRef;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GInstanceFieldRef.class */
public class GInstanceFieldRef extends AbstractInstanceFieldRef implements Precedence {
    public GInstanceFieldRef(Value base, SootFieldRef fieldRef) {
        super(Grimp.v().newObjExprBox(base), fieldRef);
    }

    private String toString(Value op, String opString, String rightString) {
        String leftOp = opString;
        if ((op instanceof Precedence) && ((Precedence) op).getPrecedence() < getPrecedence()) {
            leftOp = "(" + leftOp + ")";
        }
        return String.valueOf(leftOp) + rightString;
    }

    @Override // soot.jimple.internal.AbstractInstanceFieldRef
    public String toString() {
        return toString(getBase(), getBase().toString(), "." + this.fieldRef.getSignature());
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 950;
    }

    @Override // soot.jimple.internal.AbstractInstanceFieldRef, soot.Value
    public Object clone() {
        return new GInstanceFieldRef(Grimp.cloneIfNecessary(getBase()), this.fieldRef);
    }
}
