package soot.dava.internal.javaRep;

import soot.IntType;
import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.internal.AbstractGrimpIntBinopExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DCmplExpr.class */
public class DCmplExpr extends AbstractGrimpIntBinopExpr implements CmplExpr {
    public DCmplExpr(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.jimple.BinopExpr
    public final String getSymbol() {
        return " - ";
    }

    @Override // soot.grimp.internal.AbstractGrimpIntBinopExpr, soot.grimp.Precedence
    public final int getPrecedence() {
        return 700;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseCmplExpr(this);
    }

    @Override // soot.jimple.internal.AbstractBinopExpr, soot.Value
    public Object clone() {
        return new DCmplExpr(Grimp.cloneIfNecessary(getOp1()), Grimp.cloneIfNecessary(getOp2()));
    }

    @Override // soot.jimple.internal.AbstractIntBinopExpr, soot.Value
    public Type getType() {
        if (getOp1().getType().equals(getOp2().getType())) {
            return getOp1().getType();
        }
        return IntType.v();
    }
}
