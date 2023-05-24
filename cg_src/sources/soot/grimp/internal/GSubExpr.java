package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.SubExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GSubExpr.class */
public class GSubExpr extends AbstractGrimpFloatBinopExpr implements SubExpr {
    public GSubExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " - ";
    }

    @Override // soot.grimp.internal.AbstractGrimpFloatBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return 700;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseSubExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GSubExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
