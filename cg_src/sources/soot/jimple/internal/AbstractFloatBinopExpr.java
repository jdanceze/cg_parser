package soot.jimple.internal;

import soot.Type;
import soot.ValueBox;
import soot.jimple.internal.AbstractBinopExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractFloatBinopExpr.class */
public abstract class AbstractFloatBinopExpr extends AbstractBinopExpr {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractFloatBinopExpr(ValueBox op1Box, ValueBox op2Box) {
        super(op1Box, op2Box);
    }

    @Override // soot.Value
    public Type getType() {
        return getType(AbstractBinopExpr.BinopExprEnum.ABSTRACT_FLOAT_BINOP_EXPR);
    }
}
