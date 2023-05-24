package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.AndExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GAndExpr.class */
public class GAndExpr extends AbstractGrimpIntLongBinopExpr implements AndExpr {
    public GAndExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " & ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntLongBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return 500;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseAndExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GAndExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
