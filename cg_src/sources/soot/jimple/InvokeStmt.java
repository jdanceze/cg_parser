package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/InvokeStmt.class */
public interface InvokeStmt extends Stmt {
    void setInvokeExpr(Value value);

    @Override // soot.jimple.Stmt
    InvokeExpr getInvokeExpr();

    @Override // soot.jimple.Stmt
    ValueBox getInvokeExprBox();
}
