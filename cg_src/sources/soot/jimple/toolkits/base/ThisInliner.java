package soot.jimple.toolkits.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.shimple.ShimpleBody;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/ThisInliner.class */
public class ThisInliner extends BodyTransformer {
    private static final boolean DEBUG = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ThisInliner.class.desiredAssertionStatus();
    }

    @Override // soot.BodyTransformer
    public void internalTransform(Body b, String phaseName, Map<String, String> options) {
        InvokeStmt invokeStmt;
        if (!$assertionsDisabled && !(b instanceof JimpleBody) && !(b instanceof ShimpleBody)) {
            throw new AssertionError();
        }
        if (!"<init>".equals(b.getMethod().getName()) || (invokeStmt = getFirstSpecialInvoke(b)) == null) {
            return;
        }
        SpecialInvokeExpr specInvokeExpr = (SpecialInvokeExpr) invokeStmt.getInvokeExpr();
        SootMethod specInvokeMethod = specInvokeExpr.getMethod();
        if (specInvokeMethod.getDeclaringClass().equals(b.getMethod().getDeclaringClass())) {
            Body specInvokeBody = specInvokeMethod.retrieveActiveBody();
            if (!$assertionsDisabled && b.getClass() != specInvokeBody.getClass()) {
                throw new AssertionError();
            }
            HashMap<Local, Local> oldLocalsToNew = new HashMap<>();
            for (Local l : specInvokeBody.getLocals()) {
                Local newLocal = (Local) l.clone();
                b.getLocals().add(newLocal);
                oldLocalsToNew.put(l, newLocal);
            }
            Value origIdStmtLHS = findIdentityStmt(b).getLeftOp();
            HashMap<Unit, Unit> oldStmtsToNew = new HashMap<>();
            Chain<Unit> containerUnits = b.getUnits();
            Iterator<Unit> it = specInvokeBody.getUnits().iterator();
            while (it.hasNext()) {
                Stmt inlineeStmt = (Stmt) it.next();
                if (inlineeStmt instanceof IdentityStmt) {
                    IdentityStmt idStmt = (IdentityStmt) inlineeStmt;
                    Value rightOp = idStmt.getRightOp();
                    if (rightOp instanceof ThisRef) {
                        Stmt newThis = Jimple.v().newAssignStmt(oldLocalsToNew.get((Local) idStmt.getLeftOp()), origIdStmtLHS);
                        containerUnits.insertBefore(newThis, invokeStmt);
                        oldStmtsToNew.put(inlineeStmt, newThis);
                    } else if (rightOp instanceof CaughtExceptionRef) {
                        Stmt newInlinee = (Stmt) inlineeStmt.clone();
                        for (ValueBox vb : newInlinee.getUseAndDefBoxes()) {
                            Value val = vb.getValue();
                            if (val instanceof Local) {
                                vb.setValue(oldLocalsToNew.get((Local) val));
                            }
                        }
                        containerUnits.insertBefore(newInlinee, invokeStmt);
                        oldStmtsToNew.put(inlineeStmt, newInlinee);
                    } else if (rightOp instanceof ParameterRef) {
                        Stmt newParam = Jimple.v().newAssignStmt(oldLocalsToNew.get((Local) idStmt.getLeftOp()), specInvokeExpr.getArg(((ParameterRef) rightOp).getIndex()));
                        containerUnits.insertBefore(newParam, invokeStmt);
                        oldStmtsToNew.put(inlineeStmt, newParam);
                    }
                } else if (inlineeStmt instanceof ReturnVoidStmt) {
                    Stmt newRet = Jimple.v().newGotoStmt(containerUnits.getSuccOf(invokeStmt));
                    containerUnits.insertBefore(newRet, invokeStmt);
                    oldStmtsToNew.put(inlineeStmt, newRet);
                } else {
                    Stmt newInlinee2 = (Stmt) inlineeStmt.clone();
                    for (ValueBox vb2 : newInlinee2.getUseAndDefBoxes()) {
                        Value val2 = vb2.getValue();
                        if (val2 instanceof Local) {
                            vb2.setValue(oldLocalsToNew.get((Local) val2));
                        }
                    }
                    containerUnits.insertBefore(newInlinee2, invokeStmt);
                    oldStmtsToNew.put(inlineeStmt, newInlinee2);
                }
            }
            for (Trap t : specInvokeBody.getTraps()) {
                Unit newBegin = oldStmtsToNew.get(t.getBeginUnit());
                Unit newEnd = oldStmtsToNew.get(t.getEndUnit());
                Unit newHandler = oldStmtsToNew.get(t.getHandlerUnit());
                if (newBegin == null || newEnd == null || newHandler == null) {
                    throw new RuntimeException("couldn't map trap!");
                }
                b.getTraps().add(Jimple.v().newTrap(t.getException(), newBegin, newEnd, newHandler));
            }
            Iterator<Unit> it2 = specInvokeBody.getUnits().iterator();
            while (it2.hasNext()) {
                Unit u = it2.next();
                if (u instanceof GotoStmt) {
                    GotoStmt inlineeStmt2 = (GotoStmt) u;
                    ((GotoStmt) oldStmtsToNew.get(inlineeStmt2)).setTarget(oldStmtsToNew.get(inlineeStmt2.getTarget()));
                }
            }
            containerUnits.remove(invokeStmt);
            LocalNameStandardizer.v().transform(b, "ji.lns");
        }
    }

    private InvokeStmt getFirstSpecialInvoke(Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof InvokeStmt) {
                InvokeStmt s = (InvokeStmt) u;
                if (s.getInvokeExpr() instanceof SpecialInvokeExpr) {
                    return s;
                }
            }
        }
        return null;
    }

    private IdentityStmt findIdentityStmt(Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityStmt) {
                IdentityStmt s = (IdentityStmt) u;
                if (s.getRightOp() instanceof ThisRef) {
                    return s;
                }
            }
        }
        return null;
    }
}
