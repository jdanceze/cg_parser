package soot.jimple.internal;

import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.AddExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JAddExpr.class */
public class JAddExpr extends AbstractJimpleFloatBinopExpr implements AddExpr {
    public JAddExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " + ";
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseAddExpr(this);
    }

    @Override // soot.jimple.internal.AbstractJimpleFloatBinopExpr
    protected Unit makeBafInst(Type opType) {
        return Baf.v().newAddInst(getOp1().getType());
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new JAddExpr(Jimple.cloneIfNecessary(getOp1()), Jimple.cloneIfNecessary(getOp2()));
    }
}
