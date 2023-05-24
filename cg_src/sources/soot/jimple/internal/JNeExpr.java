package soot.jimple.internal;

import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.NeExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JNeExpr.class */
public class JNeExpr extends AbstractJimpleIntBinopExpr implements NeExpr {
    public JNeExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " != ";
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseNeExpr(this);
    }

    @Override // soot.jimple.internal.AbstractJimpleIntBinopExpr
    protected Unit makeBafInst(Type opType) {
        throw new RuntimeException("unsupported conversion: " + this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new JNeExpr(Jimple.cloneIfNecessary(getOp1()), Jimple.cloneIfNecessary(getOp2()));
    }
}
