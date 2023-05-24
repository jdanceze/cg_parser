package soot.jimple.internal;

import soot.IntType;
import soot.Type;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractIntBinopExpr.class */
public abstract class AbstractIntBinopExpr extends AbstractBinopExpr {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractIntBinopExpr(ValueBox op1Box, ValueBox op2Box) {
        super(op1Box, op2Box);
    }

    @Override // soot.Value
    public Type getType() {
        return IntType.v();
    }
}
