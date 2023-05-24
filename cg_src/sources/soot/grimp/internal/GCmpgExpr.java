package soot.grimp.internal;

import android.provider.CalendarContract;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.CmpgExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GCmpgExpr.class */
public class GCmpgExpr extends AbstractGrimpIntBinopExpr implements CmpgExpr {
    public GCmpgExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " cmpg ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return CalendarContract.CalendarColumns.CAL_ACCESS_EDITOR;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseCmpgExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GCmpgExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
