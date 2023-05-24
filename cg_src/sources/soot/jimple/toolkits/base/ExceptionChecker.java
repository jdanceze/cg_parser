package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.FastHierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.ThrowCreatedByCompilerTag;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/ExceptionChecker.class */
public class ExceptionChecker extends BodyTransformer {
    protected final ExceptionCheckerErrorReporter reporter;
    protected FastHierarchy hierarchy;

    public ExceptionChecker(ExceptionCheckerErrorReporter r) {
        this.reporter = r;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt s = (Stmt) u;
            if (s instanceof ThrowStmt) {
                checkThrow(b, (ThrowStmt) s);
            } else if (s instanceof InvokeStmt) {
                checkInvoke(b, (InvokeStmt) s);
            } else if (s instanceof AssignStmt) {
                Value rightOp = ((AssignStmt) s).getRightOp();
                if (rightOp instanceof InvokeExpr) {
                    checkInvokeExpr(b, (InvokeExpr) rightOp, s);
                }
            }
        }
    }

    protected void checkThrow(Body b, ThrowStmt ts) {
        RefType opType = (RefType) ts.getOp().getType();
        if (!isThrowDeclared(b, opType.getSootClass()) && !isThrowFromCompiler(ts) && !isExceptionCaught(b, ts, opType) && this.reporter != null) {
            this.reporter.reportError(new ExceptionCheckerError(b.getMethod(), opType.getSootClass(), ts, (SourceLnPosTag) ts.getOpBox().getTag(SourceLnPosTag.NAME)));
        }
    }

    protected boolean isThrowDeclared(Body b, SootClass throwClass) {
        if (this.hierarchy == null) {
            this.hierarchy = new FastHierarchy();
        }
        SootClass sootClassRuntimeException = Scene.v().getSootClass("java.lang.RuntimeException");
        SootClass sootClassError = Scene.v().getSootClass("java.lang.Error");
        if (throwClass.equals(sootClassRuntimeException) || throwClass.equals(sootClassError) || this.hierarchy.isSubclass(throwClass, sootClassRuntimeException) || this.hierarchy.isSubclass(throwClass, sootClassError) || b.getMethod().throwsException(throwClass)) {
            return true;
        }
        List<SootClass> exceptions = b.getMethod().getExceptionsUnsafe();
        if (exceptions != null) {
            for (SootClass nextEx : exceptions) {
                if (this.hierarchy.isSubclass(throwClass, nextEx)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    protected boolean isThrowFromCompiler(ThrowStmt ts) {
        return ts.hasTag(ThrowCreatedByCompilerTag.NAME);
    }

    protected boolean isExceptionCaught(Body b, Stmt s, RefType throwType) {
        if (this.hierarchy == null) {
            this.hierarchy = new FastHierarchy();
        }
        for (Trap trap : b.getTraps()) {
            RefType type = trap.getException().getType();
            if (type.equals(throwType) || this.hierarchy.isSubclass(throwType.getSootClass(), type.getSootClass())) {
                if (isThrowInStmtRange(b, (Stmt) trap.getBeginUnit(), (Stmt) trap.getEndUnit(), s)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isThrowInStmtRange(Body b, Stmt begin, Stmt end, Stmt s) {
        Iterator<Unit> it = b.getUnits().iterator(begin, end);
        while (it.hasNext()) {
            Unit u = it.next();
            if (u.equals(s)) {
                return true;
            }
        }
        return false;
    }

    protected void checkInvoke(Body b, InvokeStmt is) {
        checkInvokeExpr(b, is.getInvokeExpr(), is);
    }

    private List<SootClass> getExceptionSpec(SootClass intrface, NumberedString sig) {
        SootMethod sm = intrface.getMethodUnsafe(sig);
        if (sm != null) {
            return sm.getExceptions();
        }
        SootMethod sm2 = Scene.v().getSootClass(Scene.v().getObjectType().toString()).getMethodUnsafe(sig);
        if (sm2 != null && sm2.getExceptionsUnsafe() == null) {
            return Collections.emptyList();
        }
        List<SootClass> result = sm2 == null ? null : new ArrayList<>(sm2.getExceptions());
        for (SootClass suprintr : intrface.getInterfaces()) {
            List<SootClass> other = getExceptionSpec(suprintr, sig);
            if (other != null) {
                if (result == null) {
                    result = other;
                } else {
                    result.retainAll(other);
                }
            }
        }
        return result;
    }

    protected void checkInvokeExpr(Body b, InvokeExpr ie, Stmt s) {
        SootMethodRef methodRef = ie.getMethodRef();
        if ("clone".equals(methodRef.name()) && methodRef.parameterTypes().isEmpty() && (ie instanceof InstanceInvokeExpr) && (((InstanceInvokeExpr) ie).getBase().getType() instanceof ArrayType)) {
            return;
        }
        List<SootClass> exceptions = ie instanceof InterfaceInvokeExpr ? getExceptionSpec(methodRef.declaringClass(), methodRef.getSubSignature()) : ie.getMethod().getExceptionsUnsafe();
        if (exceptions != null) {
            for (SootClass sc : exceptions) {
                if (!isThrowDeclared(b, sc) && !isExceptionCaught(b, s, sc.getType()) && this.reporter != null) {
                    if (s instanceof InvokeStmt) {
                        this.reporter.reportError(new ExceptionCheckerError(b.getMethod(), sc, s, (SourceLnPosTag) s.getTag(SourceLnPosTag.NAME)));
                    } else if (s instanceof AssignStmt) {
                        this.reporter.reportError(new ExceptionCheckerError(b.getMethod(), sc, s, (SourceLnPosTag) ((AssignStmt) s).getRightOpBox().getTag(SourceLnPosTag.NAME)));
                    }
                }
            }
        }
    }
}
