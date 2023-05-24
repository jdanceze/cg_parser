package soot.jimple.internal;

import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.MulExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JMulExpr.class */
public class JMulExpr extends AbstractJimpleFloatBinopExpr implements MulExpr {
    public JMulExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " * ";
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseMulExpr(this);
    }

    @Override // soot.jimple.internal.AbstractJimpleFloatBinopExpr
    protected Unit makeBafInst(Type opType) {
        return Baf.v().newMulInst(getOp1().getType());
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new JMulExpr(Jimple.cloneIfNecessary(getOp1()), Jimple.cloneIfNecessary(getOp2()));
    }
}
