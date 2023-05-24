package soot.jimple.internal;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.ShortType;
import soot.Type;
import soot.ValueBox;
import soot.jimple.internal.AbstractBinopExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractIntLongBinopExpr.class */
public abstract class AbstractIntLongBinopExpr extends AbstractBinopExpr {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractIntLongBinopExpr(ValueBox op1Box, ValueBox op2Box) {
        super(op1Box, op2Box);
    }

    public static boolean isIntLikeType(Type t) {
        return IntType.v().equals(t) || ByteType.v().equals(t) || ShortType.v().equals(t) || CharType.v().equals(t) || BooleanType.v().equals(t);
    }

    @Override // soot.Value
    public Type getType() {
        return getType(AbstractBinopExpr.BinopExprEnum.ABASTRACT_INT_LONG_BINOP_EXPR);
    }
}
