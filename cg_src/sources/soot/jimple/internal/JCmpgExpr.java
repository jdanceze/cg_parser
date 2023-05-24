package soot.jimple.internal;

import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.CmpgExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JCmpgExpr.class */
public class JCmpgExpr extends AbstractJimpleIntBinopExpr implements CmpgExpr {
    public JCmpgExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " cmpg ";
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseCmpgExpr(this);
    }

    @Override // soot.jimple.internal.AbstractJimpleIntBinopExpr
    protected Unit makeBafInst(Type opType) {
        return Baf.v().newCmpgInst(getOp1().getType());
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new JCmpgExpr(Jimple.cloneIfNecessary(getOp1()), Jimple.cloneIfNecessary(getOp2()));
    }
}
