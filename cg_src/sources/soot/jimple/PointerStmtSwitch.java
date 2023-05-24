package soot.jimple;

import soot.ArrayType;
import soot.Local;
import soot.RefType;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/PointerStmtSwitch.class */
public abstract class PointerStmtSwitch extends AbstractStmtSwitch {
    Stmt statement;

    protected abstract void caseAssignConstStmt(Value value, Constant constant);

    protected abstract void caseCopyStmt(Local local, Local local2);

    protected abstract void caseIdentityStmt(Local local, IdentityRef identityRef);

    protected abstract void caseLoadStmt(Local local, InstanceFieldRef instanceFieldRef);

    protected abstract void caseStoreStmt(InstanceFieldRef instanceFieldRef, Local local);

    protected abstract void caseArrayLoadStmt(Local local, ArrayRef arrayRef);

    protected abstract void caseArrayStoreStmt(ArrayRef arrayRef, Local local);

    protected abstract void caseGlobalLoadStmt(Local local, StaticFieldRef staticFieldRef);

    protected abstract void caseGlobalStoreStmt(StaticFieldRef staticFieldRef, Local local);

    protected abstract void caseReturnStmt(Local local);

    protected abstract void caseAnyNewStmt(Local local, Expr expr);

    protected abstract void caseInvokeStmt(Local local, InvokeExpr invokeExpr);

    protected void caseCastStmt(Local dest, Local src, CastExpr c) {
        caseCopyStmt(dest, src);
    }

    protected void caseReturnConstStmt(Constant val) {
        caseUninterestingStmt(this.statement);
    }

    protected void caseNewStmt(Local dest, NewExpr e) {
        caseAnyNewStmt(dest, e);
    }

    protected void caseNewArrayStmt(Local dest, NewArrayExpr e) {
        caseAnyNewStmt(dest, e);
    }

    protected void caseNewMultiArrayStmt(Local dest, NewMultiArrayExpr e) {
        caseAnyNewStmt(dest, e);
    }

    protected void caseThrowStmt(Local thrownException) {
        caseUninterestingStmt(this.statement);
    }

    protected void caseCatchStmt(Local dest, CaughtExceptionRef cer) {
        caseUninterestingStmt(this.statement);
    }

    protected void caseUninterestingStmt(Stmt s) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public final void caseAssignStmt(AssignStmt s) {
        this.statement = s;
        Value lhs = s.getLeftOp();
        Value rhs = s.getRightOp();
        if (!(lhs.getType() instanceof RefType) && !(lhs.getType() instanceof ArrayType)) {
            if (rhs instanceof InvokeExpr) {
                caseInvokeStmt(null, (InvokeExpr) rhs);
            } else {
                caseUninterestingStmt(s);
            }
        } else if (rhs instanceof InvokeExpr) {
            caseInvokeStmt((Local) lhs, (InvokeExpr) rhs);
        } else if (lhs instanceof Local) {
            if (rhs instanceof Local) {
                caseCopyStmt((Local) lhs, (Local) rhs);
            } else if (rhs instanceof InstanceFieldRef) {
                caseLoadStmt((Local) lhs, (InstanceFieldRef) rhs);
            } else if (rhs instanceof ArrayRef) {
                caseArrayLoadStmt((Local) lhs, (ArrayRef) rhs);
            } else if (rhs instanceof StaticFieldRef) {
                caseGlobalLoadStmt((Local) lhs, (StaticFieldRef) rhs);
            } else if (rhs instanceof NewExpr) {
                caseNewStmt((Local) lhs, (NewExpr) rhs);
            } else if (rhs instanceof NewArrayExpr) {
                caseNewArrayStmt((Local) lhs, (NewArrayExpr) rhs);
            } else if (rhs instanceof NewMultiArrayExpr) {
                caseNewMultiArrayStmt((Local) lhs, (NewMultiArrayExpr) rhs);
            } else if (!(rhs instanceof CastExpr)) {
                if (rhs instanceof Constant) {
                    caseAssignConstStmt(lhs, (Constant) rhs);
                    return;
                }
                throw new RuntimeException("unhandled stmt " + s);
            } else {
                CastExpr r = (CastExpr) rhs;
                Value rv = r.getOp();
                if (rv instanceof Constant) {
                    caseAssignConstStmt(lhs, (Constant) rv);
                } else {
                    caseCastStmt((Local) lhs, (Local) rv, r);
                }
            }
        } else if (lhs instanceof InstanceFieldRef) {
            if (rhs instanceof Local) {
                caseStoreStmt((InstanceFieldRef) lhs, (Local) rhs);
            } else if (rhs instanceof Constant) {
                caseAssignConstStmt(lhs, (Constant) rhs);
            } else {
                throw new RuntimeException("unhandled stmt " + s);
            }
        } else if (lhs instanceof ArrayRef) {
            if (rhs instanceof Local) {
                caseArrayStoreStmt((ArrayRef) lhs, (Local) rhs);
            } else if (rhs instanceof Constant) {
                caseAssignConstStmt(lhs, (Constant) rhs);
            } else {
                throw new RuntimeException("unhandled stmt " + s);
            }
        } else if (lhs instanceof StaticFieldRef) {
            if (rhs instanceof Local) {
                caseGlobalStoreStmt((StaticFieldRef) lhs, (Local) rhs);
            } else if (rhs instanceof Constant) {
                caseAssignConstStmt(lhs, (Constant) rhs);
            } else {
                throw new RuntimeException("unhandled stmt " + s);
            }
        } else if (rhs instanceof Constant) {
            caseAssignConstStmt(lhs, (Constant) rhs);
        } else {
            throw new RuntimeException("unhandled stmt " + s);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public final void caseReturnStmt(ReturnStmt s) {
        this.statement = s;
        Value op = s.getOp();
        if ((op.getType() instanceof RefType) || (op.getType() instanceof ArrayType)) {
            if (op instanceof Constant) {
                caseReturnConstStmt((Constant) op);
                return;
            } else {
                caseReturnStmt((Local) op);
                return;
            }
        }
        caseReturnStmt((Local) null);
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public final void caseReturnVoidStmt(ReturnVoidStmt s) {
        this.statement = s;
        caseReturnStmt((Local) null);
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public final void caseInvokeStmt(InvokeStmt s) {
        this.statement = s;
        caseInvokeStmt(null, s.getInvokeExpr());
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public final void caseIdentityStmt(IdentityStmt s) {
        this.statement = s;
        Value lhs = s.getLeftOp();
        Value rhs = s.getRightOp();
        if (!(lhs.getType() instanceof RefType) && !(lhs.getType() instanceof ArrayType)) {
            caseUninterestingStmt(s);
            return;
        }
        Local llhs = (Local) lhs;
        if (rhs instanceof CaughtExceptionRef) {
            caseCatchStmt(llhs, (CaughtExceptionRef) rhs);
            return;
        }
        IdentityRef rrhs = (IdentityRef) rhs;
        caseIdentityStmt(llhs, rrhs);
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public final void caseThrowStmt(ThrowStmt s) {
        this.statement = s;
        caseThrowStmt((Local) s.getOp());
    }
}
