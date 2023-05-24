package soot.grimp.internal;

import soot.IntType;
import soot.LongType;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.UshrExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GUshrExpr.class */
public class GUshrExpr extends AbstractGrimpIntLongBinopExpr implements UshrExpr {
    public GUshrExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public String getSymbol() {
        return " >>> ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntLongBinopExpr, soot.grimp.Precedence
    public int getPrecedence() {
        return 650;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseUshrExpr(this);
    }

    @Override // soot.jimple.internal.AbstractIntLongBinopExpr, soot.Value
    public Type getType() {
        if (isIntLikeType(this.op2Box.getValue().getType())) {
            Type t1 = this.op1Box.getValue().getType();
            if (isIntLikeType(t1)) {
                return IntType.v();
            }
            LongType tyLong = LongType.v();
            if (tyLong.equals(t1)) {
                return LongType.v();
            }
        }
        return UnknownType.v();
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new GUshrExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }
}
