package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/BinopExpr.class */
public interface BinopExpr extends Expr {
    Value getOp1();

    Value getOp2();

    ValueBox getOp1Box();

    ValueBox getOp2Box();

    void setOp1(Value value);

    void setOp2(Value value);

    String getSymbol();

    String toString();
}
