package soot.jimple;

import soot.Unit;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/Stmt.class */
public interface Stmt extends Unit {
    boolean containsInvokeExpr();

    InvokeExpr getInvokeExpr();

    ValueBox getInvokeExprBox();

    boolean containsArrayRef();

    ArrayRef getArrayRef();

    ValueBox getArrayRefBox();

    boolean containsFieldRef();

    FieldRef getFieldRef();

    ValueBox getFieldRefBox();
}
