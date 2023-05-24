package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.CmpExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GCmpExpr.class */
public class GCmpExpr extends AbstractGrimpIntBinopExpr implements CmpExpr {
    public GCmpExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " cmp ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return 550;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseCmpExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GCmpExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
