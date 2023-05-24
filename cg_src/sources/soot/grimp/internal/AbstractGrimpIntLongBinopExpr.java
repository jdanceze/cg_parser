package soot.grimp.internal;

import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.DivExpr;
import soot.jimple.SubExpr;
import soot.jimple.internal.AbstractIntLongBinopExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/AbstractGrimpIntLongBinopExpr.class */
public abstract class AbstractGrimpIntLongBinopExpr extends AbstractIntLongBinopExpr implements Precedence {
    @Override // soot.grimp.Precedence
    public abstract int getPrecedence();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractGrimpIntLongBinopExpr(Value op1, Value op2) {
        this(Grimp.v().newArgBox(op1), Grimp.v().newArgBox(op2));
    }

    protected AbstractGrimpIntLongBinopExpr(ValueBox op1Box, ValueBox op2Box) {
        super(op1Box, op2Box);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr
    public String toString() {
        int opPrec;
        int myPrec;
        Value op1 = this.op1Box.getValue();
        String leftOp = op1.toString();
        if ((op1 instanceof Precedence) && ((Precedence) op1).getPrecedence() < getPrecedence()) {
            leftOp = "(" + leftOp + ")";
        }
        Value op2 = this.op2Box.getValue();
        String rightOp = op2.toString();
        if ((op2 instanceof Precedence) && ((opPrec = ((Precedence) op2).getPrecedence()) < (myPrec = getPrecedence()) || (opPrec == myPrec && ((this instanceof SubExpr) || (this instanceof DivExpr))))) {
            rightOp = "(" + rightOp + ")";
        }
        return String.valueOf(leftOp) + getSymbol() + rightOp;
    }
}
