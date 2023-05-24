package soot.grimp.internal;

import javassist.compiler.TokenId;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.OrExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GOrExpr.class */
public class GOrExpr extends AbstractGrimpIntLongBinopExpr implements OrExpr {
    public GOrExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public String getSymbol() {
        return " | ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntLongBinopExpr, soot.grimp.Precedence
    public int getPrecedence() {
        return TokenId.NEQ;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseOrExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GOrExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
