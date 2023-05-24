package soot.jimple.internal;

import soot.IntType;
import soot.LongType;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.ShrExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JShrExpr.class */
public class JShrExpr extends AbstractJimpleIntLongBinopExpr implements ShrExpr {
    public JShrExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public String getSymbol() {
        return " >> ";
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseShrExpr(this);
    }

    @Override // soot.jimple.internal.AbstractJimpleIntLongBinopExpr
    protected Unit makeBafInst(Type opType) {
        return Baf.v().newShrInst(getOp1().getType());
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
                return tyLong;
            }
        }
        return UnknownType.v();
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new JShrExpr(Jimple.cloneIfNecessary(getOp1()), Jimple.cloneIfNecessary(getOp2()));
    }
}
