package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.DivExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GDivExpr.class */
public class GDivExpr extends AbstractGrimpFloatBinopExpr implements DivExpr {
    public GDivExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " / ";
    }

    @Override // soot.grimp.internal.AbstractGrimpFloatBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return 800;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseDivExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GDivExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
