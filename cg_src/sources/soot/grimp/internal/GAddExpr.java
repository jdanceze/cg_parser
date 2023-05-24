package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.AddExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GAddExpr.class */
public class GAddExpr extends AbstractGrimpFloatBinopExpr implements AddExpr {
    public GAddExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " + ";
    }

    @Override // soot.grimp.internal.AbstractGrimpFloatBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return 700;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseAddExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GAddExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
