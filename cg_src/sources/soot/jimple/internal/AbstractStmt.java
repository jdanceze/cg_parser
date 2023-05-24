package soot.jimple.internal;

import java.util.List;
import soot.AbstractUnit;
import soot.Unit;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ArrayRef;
import soot.jimple.ConvertToBaf;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleToBafContext;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractStmt.class */
public abstract class AbstractStmt extends AbstractUnit implements Stmt, ConvertToBaf {
    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newNopInst();
        out.add(u);
        u.addAllTagsOf(this);
    }

    @Override // soot.jimple.Stmt
    public boolean containsInvokeExpr() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public InvokeExpr getInvokeExpr() {
        throw new RuntimeException("getInvokeExpr() called with no invokeExpr present!");
    }

    @Override // soot.jimple.Stmt
    public ValueBox getInvokeExprBox() {
        throw new RuntimeException("getInvokeExprBox() called with no invokeExpr present!");
    }

    @Override // soot.jimple.Stmt
    public boolean containsArrayRef() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public ArrayRef getArrayRef() {
        throw new RuntimeException("getArrayRef() called with no ArrayRef present!");
    }

    @Override // soot.jimple.Stmt
    public ValueBox getArrayRefBox() {
        throw new RuntimeException("getArrayRefBox() called with no ArrayRef present!");
    }

    @Override // soot.jimple.Stmt
    public boolean containsFieldRef() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public FieldRef getFieldRef() {
        throw new RuntimeException("getFieldRef() called with no FieldRef present!");
    }

    @Override // soot.jimple.Stmt
    public ValueBox getFieldRefBox() {
        throw new RuntimeException("getFieldRefBox() called with no FieldRef present!");
    }
}
