package soot.grimp.internal;

import android.provider.CalendarContract;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.LeExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GLeExpr.class */
public class GLeExpr extends AbstractGrimpIntBinopExpr implements LeExpr {
    public GLeExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " <= ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return CalendarContract.CalendarColumns.CAL_ACCESS_EDITOR;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseLeExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GLeExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
