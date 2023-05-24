package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.EquivalentValue;
import soot.JavaBasicTypes;
import soot.JavaMethods;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/LockAllocationBodyTransformer.class */
public class LockAllocationBodyTransformer extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LockAllocationBodyTransformer.class);
    private static final LockAllocationBodyTransformer instance = new LockAllocationBodyTransformer();
    private static boolean addedGlobalLockDefs = false;
    private static int throwableNum = 0;
    static int baseLocalNum = 0;
    static int lockNumber = 0;
    static Map<EquivalentValue, StaticFieldRef> lockEqValToLock = new HashMap();

    private LockAllocationBodyTransformer() {
    }

    public static LockAllocationBodyTransformer v() {
        return instance;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phase, Map opts) {
        throw new RuntimeException("Not Supported");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void internalTransform(Body b, FlowSet fs, List<CriticalSectionGroup> groups, boolean[] insertedGlobalLock) {
        JimpleBody clinitBody;
        JimpleBody j = (JimpleBody) b;
        SootMethod thisMethod = b.getMethod();
        PatchingChain<Unit> units = b.getUnits();
        units.iterator();
        Unit firstUnit = j.getFirstNonIdentityStmt();
        units.getLast();
        Local[] lockObj = new Local[groups.size()];
        boolean[] addedLocalLockObj = new boolean[groups.size()];
        SootField[] globalLockObj = new SootField[groups.size()];
        for (int i = 1; i < groups.size(); i++) {
            lockObj[i] = Jimple.v().newLocal("lockObj" + i, RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
            addedLocalLockObj[i] = false;
            globalLockObj[i] = null;
        }
        for (int i2 = 1; i2 < groups.size(); i2++) {
            CriticalSectionGroup tnGroup = groups.get(i2);
            if (!tnGroup.useDynamicLock && !tnGroup.useLocksets) {
                if (!insertedGlobalLock[i2]) {
                    try {
                        globalLockObj[i2] = Scene.v().getMainClass().getFieldByName("globalLockObj" + i2);
                    } catch (RuntimeException e) {
                        globalLockObj[i2] = Scene.v().makeSootField("globalLockObj" + i2, RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT), 9);
                        Scene.v().getMainClass().addField(globalLockObj[i2]);
                    }
                    insertedGlobalLock[i2] = true;
                } else {
                    globalLockObj[i2] = Scene.v().getMainClass().getFieldByName("globalLockObj" + i2);
                }
            }
        }
        if (!addedGlobalLockDefs) {
            SootClass mainClass = Scene.v().getMainClass();
            Unit firstStmt = null;
            boolean addingNewClinit = !mainClass.declaresMethod(JavaMethods.SIG_CLINIT);
            if (addingNewClinit) {
                SootMethod clinitMethod = Scene.v().makeSootMethod("<clinit>", new ArrayList(), VoidType.v(), 9);
                clinitBody = Jimple.v().newBody(clinitMethod);
                clinitMethod.setActiveBody(clinitBody);
                mainClass.addMethod(clinitMethod);
            } else {
                clinitBody = (JimpleBody) mainClass.getMethod(JavaMethods.SIG_CLINIT).getActiveBody();
                firstStmt = clinitBody.getFirstNonIdentityStmt();
            }
            PatchingChain<Unit> clinitUnits = clinitBody.getUnits();
            for (int i3 = 1; i3 < groups.size(); i3++) {
                CriticalSectionGroup tnGroup2 = groups.get(i3);
                if (!tnGroup2.useDynamicLock && !tnGroup2.useLocksets) {
                    clinitBody.getLocals().add(lockObj[i3]);
                    Stmt newStmt = Jimple.v().newAssignStmt(lockObj[i3], Jimple.v().newNewExpr(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
                    if (addingNewClinit) {
                        clinitUnits.add((PatchingChain<Unit>) newStmt);
                    } else {
                        clinitUnits.insertBeforeNoRedirect(newStmt, firstStmt);
                    }
                    SootClass objectClass = Scene.v().loadClassAndSupport(JavaBasicTypes.JAVA_LANG_OBJECT);
                    RefType.v(objectClass);
                    SootMethod initMethod = objectClass.getMethod(JavaMethods.SIG_INIT);
                    Stmt initStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lockObj[i3], initMethod.makeRef(), Collections.EMPTY_LIST));
                    if (addingNewClinit) {
                        clinitUnits.add((PatchingChain<Unit>) initStmt);
                    } else {
                        clinitUnits.insertBeforeNoRedirect(initStmt, firstStmt);
                    }
                    Stmt assignStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(globalLockObj[i3].makeRef()), lockObj[i3]);
                    if (addingNewClinit) {
                        clinitUnits.add((PatchingChain<Unit>) assignStmt);
                    } else {
                        clinitUnits.insertBeforeNoRedirect(assignStmt, firstStmt);
                    }
                }
            }
            if (addingNewClinit) {
                clinitUnits.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
            }
            addedGlobalLockDefs = true;
        }
        int tempNum = 1;
        Iterator fsIt = fs.iterator();
        Stmt newPrep = null;
        while (fsIt.hasNext()) {
            CriticalSection tn = ((SynchronizedRegionFlowPair) fsIt.next()).tn;
            if (tn.setNumber != -1) {
                if (tn.wholeMethod) {
                    thisMethod.setModifiers(thisMethod.getModifiers() & (-33));
                }
                Local clo = null;
                SynchronizedRegion csr = null;
                int lockNum = 0;
                boolean moreLocks = true;
                while (moreLocks) {
                    if (tn.group.useDynamicLock) {
                        Value lock = getLockFor((EquivalentValue) tn.lockObject);
                        if (lock instanceof Ref) {
                            if (lock instanceof InstanceFieldRef) {
                                InstanceFieldRef ifr = (InstanceFieldRef) lock;
                                if (ifr.getBase() instanceof FakeJimpleLocal) {
                                    lock = reconstruct(b, units, ifr, tn.entermonitor != null ? tn.entermonitor : tn.beginning, tn.entermonitor != null);
                                }
                            }
                            if (!b.getLocals().contains(lockObj[tn.setNumber])) {
                                b.getLocals().add(lockObj[tn.setNumber]);
                            }
                            newPrep = Jimple.v().newAssignStmt(lockObj[tn.setNumber], lock);
                            if (tn.wholeMethod) {
                                units.insertBeforeNoRedirect(newPrep, (Stmt) firstUnit);
                            } else {
                                units.insertBefore(newPrep, (Stmt) tn.entermonitor);
                            }
                            clo = lockObj[tn.setNumber];
                        } else if (lock instanceof Local) {
                            clo = (Local) lock;
                        } else {
                            throw new RuntimeException("Unknown type of lock (" + lock + "): expected Ref or Local");
                        }
                        csr = tn;
                        moreLocks = false;
                    } else if (tn.group.useLocksets) {
                        Value lock2 = getLockFor(tn.lockset.get(lockNum));
                        if (lock2 instanceof FieldRef) {
                            if (lock2 instanceof InstanceFieldRef) {
                                InstanceFieldRef ifr2 = (InstanceFieldRef) lock2;
                                if (ifr2.getBase() instanceof FakeJimpleLocal) {
                                    lock2 = reconstruct(b, units, ifr2, tn.entermonitor != null ? tn.entermonitor : tn.beginning, tn.entermonitor != null);
                                }
                            }
                            Local lockLocal = Jimple.v().newLocal("locksetObj" + tempNum, RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
                            tempNum++;
                            b.getLocals().add(lockLocal);
                            newPrep = Jimple.v().newAssignStmt(lockLocal, lock2);
                            if (tn.entermonitor != null) {
                                units.insertBefore(newPrep, (Stmt) tn.entermonitor);
                            } else {
                                units.insertBeforeNoRedirect(newPrep, (Stmt) tn.beginning);
                            }
                            clo = lockLocal;
                        } else if (lock2 instanceof Local) {
                            clo = (Local) lock2;
                        } else {
                            throw new RuntimeException("Unknown type of lock (" + lock2 + "): expected FieldRef or Local");
                        }
                        if (lockNum + 1 >= tn.lockset.size()) {
                            moreLocks = false;
                        } else {
                            moreLocks = true;
                        }
                        if (lockNum > 0) {
                            SynchronizedRegion nsr = new SynchronizedRegion();
                            nsr.beginning = csr.beginning;
                            for (Pair earlyEnd : csr.earlyEnds) {
                                Stmt earlyExitmonitor = earlyEnd.getO2();
                                nsr.earlyEnds.add(new Pair<>(earlyExitmonitor, null));
                            }
                            nsr.last = csr.last;
                            if (csr.end != null) {
                                Stmt endExitmonitor = csr.end.getO2();
                                nsr.after = endExitmonitor;
                            }
                            csr = nsr;
                        } else {
                            csr = tn;
                        }
                    } else {
                        if (!addedLocalLockObj[tn.setNumber]) {
                            b.getLocals().add(lockObj[tn.setNumber]);
                        }
                        addedLocalLockObj[tn.setNumber] = true;
                        newPrep = Jimple.v().newAssignStmt(lockObj[tn.setNumber], Jimple.v().newStaticFieldRef(globalLockObj[tn.setNumber].makeRef()));
                        if (tn.wholeMethod) {
                            units.insertBeforeNoRedirect(newPrep, (Stmt) firstUnit);
                        } else {
                            units.insertBefore(newPrep, (Stmt) tn.entermonitor);
                        }
                        clo = lockObj[tn.setNumber];
                        csr = tn;
                        moreLocks = false;
                    }
                    Stmt stmt = csr.prepStmt;
                    Stmt newEntermonitor = Jimple.v().newEnterMonitorStmt(clo);
                    if (csr.entermonitor != null) {
                        units.insertBefore(newEntermonitor, (Stmt) csr.entermonitor);
                        units.remove(csr.entermonitor);
                        csr.entermonitor = newEntermonitor;
                    } else {
                        units.insertBeforeNoRedirect(newEntermonitor, (Stmt) csr.beginning);
                        csr.entermonitor = newEntermonitor;
                    }
                    List<Pair<Stmt, Stmt>> newEarlyEnds = new ArrayList<>();
                    for (Pair<Stmt, Stmt> end : csr.earlyEnds) {
                        Stmt earlyEnd2 = end.getO1();
                        Stmt exitmonitor = end.getO2();
                        Stmt newExitmonitor = Jimple.v().newExitMonitorStmt(clo);
                        if (exitmonitor != null) {
                            if (newPrep != null) {
                                units.insertBefore((Stmt) newPrep.clone(), exitmonitor);
                            }
                            units.insertBefore(newExitmonitor, exitmonitor);
                            units.remove(exitmonitor);
                            newEarlyEnds.add(new Pair<>(earlyEnd2, newExitmonitor));
                        } else {
                            if (newPrep != null) {
                                units.insertBefore((Stmt) newPrep.clone(), earlyEnd2);
                            }
                            units.insertBefore(newExitmonitor, earlyEnd2);
                            newEarlyEnds.add(new Pair<>(earlyEnd2, newExitmonitor));
                        }
                    }
                    csr.earlyEnds = newEarlyEnds;
                    if (csr.after != null) {
                        Stmt newExitmonitor2 = Jimple.v().newExitMonitorStmt(clo);
                        if (csr.end != null) {
                            Stmt exitmonitor2 = csr.end.getO2();
                            if (newPrep != null) {
                                units.insertBefore((Stmt) newPrep.clone(), exitmonitor2);
                            }
                            units.insertBefore(newExitmonitor2, exitmonitor2);
                            units.remove(exitmonitor2);
                            csr.end = new Pair<>(csr.end.getO1(), newExitmonitor2);
                        } else {
                            if (newPrep != null) {
                                units.insertBefore((Stmt) newPrep.clone(), (Stmt) csr.after);
                            }
                            units.insertBefore(newExitmonitor2, (Stmt) csr.after);
                            Stmt newGotoStmt = Jimple.v().newGotoStmt(csr.after);
                            units.insertBeforeNoRedirect(newGotoStmt, (Stmt) csr.after);
                            csr.end = new Pair<>(newGotoStmt, newExitmonitor2);
                            csr.last = newGotoStmt;
                        }
                    }
                    Stmt newExitmonitor3 = Jimple.v().newExitMonitorStmt(clo);
                    if (csr.exceptionalEnd != null) {
                        Stmt exitmonitor3 = csr.exceptionalEnd.getO2();
                        if (newPrep != null) {
                            units.insertBefore((Stmt) newPrep.clone(), exitmonitor3);
                        }
                        units.insertBefore(newExitmonitor3, exitmonitor3);
                        units.remove(exitmonitor3);
                        csr.exceptionalEnd = new Pair<>(csr.exceptionalEnd.getO1(), newExitmonitor3);
                    } else {
                        Stmt lastEnd = null;
                        if (csr.end != null) {
                            lastEnd = csr.end.getO1();
                        } else {
                            for (Pair earlyEnd3 : csr.earlyEnds) {
                                Stmt end2 = earlyEnd3.getO1();
                                if (lastEnd == null || (units.contains(lastEnd) && units.contains(end2) && units.follows(end2, lastEnd))) {
                                    lastEnd = end2;
                                }
                            }
                        }
                        if (csr.last == null) {
                            csr.last = lastEnd;
                        }
                        if (lastEnd == null) {
                            throw new RuntimeException("Lock Region has no ends!  Where should we put the exception handling???");
                        }
                        Jimple v = Jimple.v();
                        StringBuilder sb = new StringBuilder("throwableLocal");
                        int i4 = throwableNum;
                        throwableNum = i4 + 1;
                        Local throwableLocal = v.newLocal(sb.append(i4).toString(), RefType.v("java.lang.Throwable"));
                        b.getLocals().add(throwableLocal);
                        Stmt newCatch = Jimple.v().newIdentityStmt(throwableLocal, Jimple.v().newCaughtExceptionRef());
                        if (csr.last == null) {
                            throw new RuntimeException("WHY IS clr.last NULL???");
                        }
                        if (newCatch == null) {
                            throw new RuntimeException("WHY IS newCatch NULL???");
                        }
                        units.insertAfter(newCatch, (Stmt) csr.last);
                        units.insertAfter(newExitmonitor3, newCatch);
                        Stmt newThrow = Jimple.v().newThrowStmt(throwableLocal);
                        units.insertAfter(newThrow, newExitmonitor3);
                        SootClass throwableClass = Scene.v().loadClassAndSupport("java.lang.Throwable");
                        b.getTraps().addFirst(Jimple.v().newTrap(throwableClass, newExitmonitor3, newThrow, newCatch));
                        b.getTraps().addFirst(Jimple.v().newTrap(throwableClass, csr.beginning, lastEnd, newCatch));
                        csr.exceptionalEnd = new Pair<>(newThrow, newExitmonitor3);
                    }
                    lockNum++;
                }
                Iterator<Unit> it = tn.notifys.iterator();
                while (it.hasNext()) {
                    Unit uNotify = it.next();
                    Stmt sNotify = (Stmt) uNotify;
                    Stmt newNotify = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(clo, sNotify.getInvokeExpr().getMethodRef().declaringClass().getMethod("void notifyAll()").makeRef(), Collections.EMPTY_LIST));
                    if (newPrep != null) {
                        Stmt tmp = (Stmt) newPrep.clone();
                        units.insertBefore(tmp, sNotify);
                        units.insertBefore(newNotify, tmp);
                    } else {
                        units.insertBefore(newNotify, sNotify);
                    }
                    redirectTraps(b, sNotify, newNotify);
                    units.remove(sNotify);
                }
                Iterator<Unit> it2 = tn.waits.iterator();
                while (it2.hasNext()) {
                    Unit uWait = it2.next();
                    Stmt sWait = (Stmt) uWait;
                    ((InstanceInvokeExpr) sWait.getInvokeExpr()).setBase(clo);
                    if (newPrep != null) {
                        units.insertBefore((Stmt) newPrep.clone(), (Unit) sWait);
                    }
                }
            }
        }
    }

    public InstanceFieldRef reconstruct(Body b, PatchingChain<Unit> units, InstanceFieldRef lock, Stmt insertBefore, boolean redirect) {
        Local baseLocal;
        logger.debug("Reconstructing " + lock);
        if (!(lock.getBase() instanceof FakeJimpleLocal)) {
            logger.debug("  base is not a FakeJimpleLocal");
            return lock;
        }
        FakeJimpleLocal fakeBase = (FakeJimpleLocal) lock.getBase();
        if (!(fakeBase.getInfo() instanceof LockableReferenceAnalysis)) {
            throw new RuntimeException("InstanceFieldRef cannot be reconstructed due to missing LocksetAnalysis info: " + lock);
        }
        LockableReferenceAnalysis la = (LockableReferenceAnalysis) fakeBase.getInfo();
        EquivalentValue baseEqVal = la.baseFor(lock);
        if (baseEqVal == null) {
            throw new RuntimeException("InstanceFieldRef cannot be reconstructed due to lost base from Lockset");
        }
        Value base = baseEqVal.getValue();
        if (base instanceof InstanceFieldRef) {
            Value newBase = reconstruct(b, units, (InstanceFieldRef) base, insertBefore, redirect);
            Jimple v = Jimple.v();
            StringBuilder sb = new StringBuilder("baseLocal");
            int i = baseLocalNum;
            baseLocalNum = i + 1;
            baseLocal = v.newLocal(sb.append(i).toString(), newBase.getType());
            b.getLocals().add(baseLocal);
            Stmt baseAssign = Jimple.v().newAssignStmt(baseLocal, newBase);
            if (redirect) {
                units.insertBefore(baseAssign, insertBefore);
            } else {
                units.insertBeforeNoRedirect(baseAssign, insertBefore);
            }
        } else if (base instanceof Local) {
            baseLocal = (Local) base;
        } else {
            throw new RuntimeException("InstanceFieldRef cannot be reconstructed because it's base is of an unsupported type" + base.getType() + ": " + base);
        }
        InstanceFieldRef newLock = Jimple.v().newInstanceFieldRef(baseLocal, lock.getField().makeRef());
        logger.debug("  as " + newLock);
        return newLock;
    }

    public static Value getLockFor(EquivalentValue lockEqVal) {
        JimpleBody clinitBody;
        Value lock = lockEqVal.getValue();
        if (lock instanceof InstanceFieldRef) {
            return lock;
        }
        if (lock instanceof ArrayRef) {
            return ((ArrayRef) lock).getBase();
        }
        if (lock instanceof Local) {
            return lock;
        }
        if ((lock instanceof StaticFieldRef) || (lock instanceof NewStaticLock)) {
            if (lockEqValToLock.containsKey(lockEqVal)) {
                return lockEqValToLock.get(lockEqVal);
            }
            SootClass lockClass = null;
            if (lock instanceof StaticFieldRef) {
                StaticFieldRef sfrLock = (StaticFieldRef) lock;
                lockClass = sfrLock.getField().getDeclaringClass();
            } else if (lock instanceof NewStaticLock) {
                DeadlockAvoidanceEdge dae = (DeadlockAvoidanceEdge) lock;
                lockClass = dae.getLockClass();
            }
            Unit firstStmt = null;
            boolean addingNewClinit = !lockClass.declaresMethod(JavaMethods.SIG_CLINIT);
            if (addingNewClinit) {
                SootMethod clinitMethod = Scene.v().makeSootMethod("<clinit>", new ArrayList(), VoidType.v(), 9);
                clinitBody = Jimple.v().newBody(clinitMethod);
                clinitMethod.setActiveBody(clinitBody);
                lockClass.addMethod(clinitMethod);
            } else {
                clinitBody = (JimpleBody) lockClass.getMethod(JavaMethods.SIG_CLINIT).getActiveBody();
                firstStmt = clinitBody.getFirstNonIdentityStmt();
            }
            PatchingChain<Unit> clinitUnits = clinitBody.getUnits();
            Local lockLocal = Jimple.v().newLocal("objectLockLocal" + lockNumber, RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT));
            clinitBody.getLocals().add(lockLocal);
            Stmt newStmt = Jimple.v().newAssignStmt(lockLocal, Jimple.v().newNewExpr(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
            if (addingNewClinit) {
                clinitUnits.add((PatchingChain<Unit>) newStmt);
            } else {
                clinitUnits.insertBeforeNoRedirect(newStmt, firstStmt);
            }
            SootClass objectClass = Scene.v().loadClassAndSupport(JavaBasicTypes.JAVA_LANG_OBJECT);
            RefType.v(objectClass);
            SootMethod initMethod = objectClass.getMethod(JavaMethods.SIG_INIT);
            Stmt initStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lockLocal, initMethod.makeRef(), Collections.EMPTY_LIST));
            if (addingNewClinit) {
                clinitUnits.add((PatchingChain<Unit>) initStmt);
            } else {
                clinitUnits.insertBeforeNoRedirect(initStmt, firstStmt);
            }
            SootField actualLockObject = Scene.v().makeSootField("objectLockGlobal" + lockNumber, RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT), 9);
            lockNumber++;
            lockClass.addField(actualLockObject);
            StaticFieldRef actualLockSfr = Jimple.v().newStaticFieldRef(actualLockObject.makeRef());
            Stmt assignStmt = Jimple.v().newAssignStmt(actualLockSfr, lockLocal);
            if (addingNewClinit) {
                clinitUnits.add((PatchingChain<Unit>) assignStmt);
            } else {
                clinitUnits.insertBeforeNoRedirect(assignStmt, firstStmt);
            }
            if (addingNewClinit) {
                clinitUnits.add((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
            }
            lockEqValToLock.put(lockEqVal, actualLockSfr);
            return actualLockSfr;
        }
        throw new RuntimeException("Unknown type of lock (" + lock + "): expected FieldRef, ArrayRef, or Local");
    }

    public void redirectTraps(Body b, Unit oldUnit, Unit newUnit) {
        Chain<Trap> traps = b.getTraps();
        for (Trap trap : traps) {
            if (trap.getHandlerUnit() == oldUnit) {
                trap.setHandlerUnit(newUnit);
            }
            if (trap.getBeginUnit() == oldUnit) {
                trap.setBeginUnit(newUnit);
            }
            if (trap.getEndUnit() == oldUnit) {
                trap.setEndUnit(newUnit);
            }
        }
    }
}
