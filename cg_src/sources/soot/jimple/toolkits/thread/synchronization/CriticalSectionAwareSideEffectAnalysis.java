package soot.jimple.toolkits.thread.synchronization;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.jimple.toolkits.pointer.StmtRWSet;
import soot.jimple.toolkits.thread.EncapsulatedObjectAnalysis;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/CriticalSectionAwareSideEffectAnalysis.class */
public class CriticalSectionAwareSideEffectAnalysis {
    PointsToAnalysis pa;
    CallGraph cg;
    CriticalSectionVisibleEdgesPred tve;
    TransitiveTargets tt;
    TransitiveTargets normaltt;
    SideEffectAnalysis normalsea;
    Collection<CriticalSection> criticalSections;
    ThreadLocalObjectsAnalysis tlo;
    Map<SootMethod, CodeBlockRWSet> methodToNTReadSet = new HashMap();
    Map<SootMethod, CodeBlockRWSet> methodToNTWriteSet = new HashMap();
    int rwsetcount = 0;
    private HashMap<Stmt, RWSet> RCache = new HashMap<>();
    private HashMap<Stmt, RWSet> WCache = new HashMap<>();
    EncapsulatedObjectAnalysis eoa = new EncapsulatedObjectAnalysis();
    public Vector sigBlacklist = new Vector();
    public Vector sigReadGraylist = new Vector();
    public Vector sigWriteGraylist = new Vector();
    public Vector subSigBlacklist = new Vector();

    /* JADX WARN: Removed duplicated region for block: B:11:0x0052  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void findNTRWSets(soot.SootMethod r7) {
        /*
            Method dump skipped, instructions count: 422
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.thread.synchronization.CriticalSectionAwareSideEffectAnalysis.findNTRWSets(soot.SootMethod):void");
    }

    public void setExemptTransaction(CriticalSection tn) {
        this.tve.setExemptTransaction(tn);
    }

    public RWSet nonTransitiveReadSet(SootMethod method) {
        findNTRWSets(method);
        return this.methodToNTReadSet.get(method);
    }

    public RWSet nonTransitiveWriteSet(SootMethod method) {
        findNTRWSets(method);
        return this.methodToNTWriteSet.get(method);
    }

    public CriticalSectionAwareSideEffectAnalysis(PointsToAnalysis pa, CallGraph cg, Collection<CriticalSection> criticalSections, ThreadLocalObjectsAnalysis tlo) {
        this.pa = pa;
        this.cg = cg;
        this.tve = new CriticalSectionVisibleEdgesPred(criticalSections);
        this.tt = new TransitiveTargets(cg, new Filter(this.tve));
        this.normaltt = new TransitiveTargets(cg, null);
        this.normalsea = new SideEffectAnalysis(pa, cg);
        this.criticalSections = criticalSections;
        this.tlo = tlo;
    }

    private RWSet ntReadSet(SootMethod method, Stmt stmt) {
        if (stmt instanceof AssignStmt) {
            AssignStmt a = (AssignStmt) stmt;
            Value r = a.getRightOp();
            if (r instanceof NewExpr) {
                return null;
            }
            return addValue(r, method, stmt);
        }
        return null;
    }

    public RWSet approximatedReadSet(SootMethod method, Stmt stmt, Value specialRead, boolean allFields) {
        RWSet normalRW;
        CodeBlockRWSet ret = new CodeBlockRWSet();
        if (specialRead != null) {
            if (specialRead instanceof Local) {
                Local vLocal = (Local) specialRead;
                PointsToSet base = this.pa.reachingObjects(vLocal);
                Type pType = vLocal.getType();
                if (pType instanceof RefType) {
                    SootClass baseTypeClass = ((RefType) pType).getSootClass();
                    if (!baseTypeClass.isInterface()) {
                        List<SootClass> baseClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(baseTypeClass);
                        if (!baseClasses.contains(RefType.v("java.lang.Exception").getSootClass())) {
                            for (SootClass baseClass : baseClasses) {
                                for (SootField baseField : baseClass.getFields()) {
                                    if (!baseField.isStatic()) {
                                        ret.addFieldRef(base, baseField);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!allFields) {
                    ret = new CodeBlockRWSet();
                    if (this.RCache.containsKey(stmt)) {
                        normalRW = this.RCache.get(stmt);
                    } else {
                        normalRW = this.normalsea.readSet(method, stmt);
                        this.RCache.put(stmt, normalRW);
                    }
                    if (normalRW != null) {
                        for (Object field : normalRW.getFields()) {
                            if (ret.containsField(field)) {
                                PointsToSet otherBase = normalRW.getBaseForField(field);
                                if (otherBase instanceof FullObjectSet) {
                                    ret.addFieldRef(otherBase, field);
                                } else if (base.hasNonEmptyIntersection(otherBase)) {
                                    ret.addFieldRef(base, field);
                                }
                            }
                        }
                    }
                }
            } else if (specialRead instanceof FieldRef) {
                ret.union(addValue(specialRead, method, stmt));
            }
        }
        if (stmt.containsInvokeExpr()) {
            int argCount = stmt.getInvokeExpr().getArgCount();
            for (int i = 0; i < argCount; i++) {
                ret.union(addValue(stmt.getInvokeExpr().getArg(i), method, stmt));
            }
        }
        if (stmt instanceof AssignStmt) {
            AssignStmt a = (AssignStmt) stmt;
            Value r = a.getRightOp();
            ret.union(addValue(r, method, stmt));
        }
        return ret;
    }

    public RWSet readSet(SootMethod method, Stmt stmt, CriticalSection tn, Set uses) {
        RWSet ntr;
        boolean ignore = false;
        if (stmt.containsInvokeExpr()) {
            InvokeExpr ie = stmt.getInvokeExpr();
            SootMethod calledMethod = ie.getMethod();
            if (!(ie instanceof StaticInvokeExpr) && (ie instanceof InstanceInvokeExpr)) {
                if (calledMethod.getSubSignature().startsWith("void <init>") && this.eoa.isInitMethodPureOnObject(calledMethod)) {
                    ignore = true;
                } else if (this.tlo != null && !this.tlo.hasNonThreadLocalEffects(method, ie)) {
                    ignore = true;
                }
            }
        }
        RWSet ret = new CodeBlockRWSet();
        this.tve.setExemptTransaction(tn);
        Iterator<MethodOrMethodContext> targets = this.tt.iterator(stmt);
        while (!ignore && targets.hasNext()) {
            SootMethod target = (SootMethod) targets.next();
            if (target.isConcrete() && !target.getDeclaringClass().toString().startsWith("java.util") && !target.getDeclaringClass().toString().startsWith("java.lang") && (ntr = nonTransitiveReadSet(target)) != null) {
                ret.union(ntr);
            }
        }
        RWSet ntr2 = ntReadSet(method, stmt);
        if (0 == 0 && ntr2 != null && (stmt instanceof AssignStmt)) {
            AssignStmt a = (AssignStmt) stmt;
            Value r = a.getRightOp();
            if (r instanceof InstanceFieldRef) {
                uses.add(((InstanceFieldRef) r).getBase());
            } else if (r instanceof StaticFieldRef) {
                uses.add(r);
            } else if (r instanceof ArrayRef) {
                uses.add(((ArrayRef) r).getBase());
            }
        }
        ret.union(ntr2);
        if (stmt.containsInvokeExpr()) {
            InvokeExpr ie2 = stmt.getInvokeExpr();
            SootMethod calledMethod2 = ie2.getMethod();
            if (calledMethod2.getDeclaringClass().toString().startsWith("java.util") || calledMethod2.getDeclaringClass().toString().startsWith("java.lang")) {
                Local base = null;
                if (ie2 instanceof InstanceInvokeExpr) {
                    base = (Local) ((InstanceInvokeExpr) ie2).getBase();
                }
                if (this.tlo == null || base == null || !this.tlo.isObjectThreadLocal(base, method)) {
                    RWSet r2 = approximatedReadSet(method, stmt, base, true);
                    if (r2 != null) {
                        ret.union(r2);
                    }
                    int argCount = stmt.getInvokeExpr().getArgCount();
                    for (int i = 0; i < argCount; i++) {
                        uses.add(ie2.getArg(i));
                    }
                    if (base != null) {
                        uses.add(base);
                    }
                }
            }
        }
        return ret;
    }

    private RWSet ntWriteSet(SootMethod method, Stmt stmt) {
        if (stmt instanceof AssignStmt) {
            AssignStmt a = (AssignStmt) stmt;
            Value l = a.getLeftOp();
            return addValue(l, method, stmt);
        }
        return null;
    }

    public RWSet approximatedWriteSet(SootMethod method, Stmt stmt, Value v, boolean allFields) {
        RWSet normalRW;
        CodeBlockRWSet ret = new CodeBlockRWSet();
        if (v != null) {
            if (v instanceof Local) {
                Local vLocal = (Local) v;
                PointsToSet base = this.pa.reachingObjects(vLocal);
                Type pType = vLocal.getType();
                if (pType instanceof RefType) {
                    SootClass baseTypeClass = ((RefType) pType).getSootClass();
                    if (!baseTypeClass.isInterface()) {
                        List<SootClass> baseClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(baseTypeClass);
                        if (!baseClasses.contains(RefType.v("java.lang.Exception").getSootClass())) {
                            for (SootClass baseClass : baseClasses) {
                                for (SootField baseField : baseClass.getFields()) {
                                    if (!baseField.isStatic()) {
                                        ret.addFieldRef(base, baseField);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!allFields) {
                    ret = new CodeBlockRWSet();
                    if (this.WCache.containsKey(stmt)) {
                        normalRW = this.WCache.get(stmt);
                    } else {
                        normalRW = this.normalsea.writeSet(method, stmt);
                        this.WCache.put(stmt, normalRW);
                    }
                    if (normalRW != null) {
                        for (Object field : normalRW.getFields()) {
                            if (ret.containsField(field)) {
                                PointsToSet otherBase = normalRW.getBaseForField(field);
                                if (otherBase instanceof FullObjectSet) {
                                    ret.addFieldRef(otherBase, field);
                                } else if (base.hasNonEmptyIntersection(otherBase)) {
                                    ret.addFieldRef(base, field);
                                }
                            }
                        }
                    }
                }
            } else if (v instanceof FieldRef) {
                ret.union(addValue(v, method, stmt));
            }
        }
        if (stmt instanceof AssignStmt) {
            AssignStmt a = (AssignStmt) stmt;
            Value l = a.getLeftOp();
            ret.union(addValue(l, method, stmt));
        }
        return ret;
    }

    public RWSet writeSet(SootMethod method, Stmt stmt, CriticalSection tn, Set uses) {
        RWSet ntw;
        boolean ignore = false;
        if (stmt.containsInvokeExpr()) {
            InvokeExpr ie = stmt.getInvokeExpr();
            SootMethod calledMethod = ie.getMethod();
            if (!(ie instanceof StaticInvokeExpr) && (ie instanceof InstanceInvokeExpr)) {
                if (calledMethod.getSubSignature().startsWith("void <init>") && this.eoa.isInitMethodPureOnObject(calledMethod)) {
                    ignore = true;
                } else if (this.tlo != null && !this.tlo.hasNonThreadLocalEffects(method, ie)) {
                    ignore = true;
                }
            }
        }
        RWSet ret = new CodeBlockRWSet();
        this.tve.setExemptTransaction(tn);
        Iterator<MethodOrMethodContext> targets = this.tt.iterator(stmt);
        while (!ignore && targets.hasNext()) {
            SootMethod target = (SootMethod) targets.next();
            if (target.isConcrete() && !target.getDeclaringClass().toString().startsWith("java.util") && !target.getDeclaringClass().toString().startsWith("java.lang") && (ntw = nonTransitiveWriteSet(target)) != null) {
                ret.union(ntw);
            }
        }
        RWSet ntw2 = ntWriteSet(method, stmt);
        if (0 == 0 && ntw2 != null && (stmt instanceof AssignStmt)) {
            AssignStmt a = (AssignStmt) stmt;
            Value l = a.getLeftOp();
            if (l instanceof InstanceFieldRef) {
                uses.add(((InstanceFieldRef) l).getBase());
            } else if (l instanceof StaticFieldRef) {
                uses.add(l);
            } else if (l instanceof ArrayRef) {
                uses.add(((ArrayRef) l).getBase());
            }
        }
        ret.union(ntw2);
        if (stmt.containsInvokeExpr()) {
            InvokeExpr ie2 = stmt.getInvokeExpr();
            SootMethod calledMethod2 = ie2.getMethod();
            if (calledMethod2.getDeclaringClass().toString().startsWith("java.util") || calledMethod2.getDeclaringClass().toString().startsWith("java.lang")) {
                Local base = null;
                if (ie2 instanceof InstanceInvokeExpr) {
                    base = (Local) ((InstanceInvokeExpr) ie2).getBase();
                }
                if (this.tlo == null || base == null || !this.tlo.isObjectThreadLocal(base, method)) {
                    RWSet w = approximatedWriteSet(method, stmt, base, true);
                    if (w != null) {
                        ret.union(w);
                    }
                    if (base != null) {
                        uses.add(base);
                    }
                }
            }
        }
        return ret;
    }

    public RWSet valueRWSet(Value v, SootMethod m, Stmt s, CriticalSection tn) {
        RWSet ret;
        if (this.tlo != null) {
            if (v instanceof InstanceFieldRef) {
                InstanceFieldRef ifr = (InstanceFieldRef) v;
                if ((m.isConcrete() && !m.isStatic() && m.retrieveActiveBody().getThisLocal().equivTo(ifr.getBase()) && this.tlo.isObjectThreadLocal(ifr, m)) || this.tlo.isObjectThreadLocal(ifr.getBase(), m)) {
                    return null;
                }
            } else if ((v instanceof ArrayRef) && this.tlo.isObjectThreadLocal(((ArrayRef) v).getBase(), m)) {
                return null;
            }
        }
        if (v instanceof InstanceFieldRef) {
            InstanceFieldRef ifr2 = (InstanceFieldRef) v;
            PointsToSet base = this.pa.reachingObjects((Local) ifr2.getBase());
            ret = new StmtRWSet();
            ret.addFieldRef(base, ifr2.getField());
        } else if (v instanceof StaticFieldRef) {
            StaticFieldRef sfr = (StaticFieldRef) v;
            ret = new StmtRWSet();
            ret.addGlobal(sfr.getField());
        } else if (v instanceof ArrayRef) {
            ArrayRef ar = (ArrayRef) v;
            PointsToSet base2 = this.pa.reachingObjects((Local) ar.getBase());
            ret = new StmtRWSet();
            ret.addFieldRef(base2, PointsToAnalysis.ARRAY_ELEMENTS_NODE);
        } else if (v instanceof Local) {
            Local vLocal = (Local) v;
            PointsToSet base3 = this.pa.reachingObjects(vLocal);
            ret = new CodeBlockRWSet();
            CodeBlockRWSet stmtRW = new CodeBlockRWSet();
            RWSet rSet = readSet(m, s, tn, new HashSet());
            if (rSet != null) {
                stmtRW.union(rSet);
            }
            RWSet wSet = writeSet(m, s, tn, new HashSet());
            if (wSet != null) {
                stmtRW.union(wSet);
            }
            for (Object field : stmtRW.getFields()) {
                PointsToSet fieldBase = stmtRW.getBaseForField(field);
                if (base3.hasNonEmptyIntersection(fieldBase)) {
                    ret.addFieldRef(base3, field);
                }
            }
        } else {
            return null;
        }
        return ret;
    }

    protected RWSet addValue(Value v, SootMethod m, Stmt s) {
        RWSet ret = null;
        if (this.tlo != null) {
            if (v instanceof InstanceFieldRef) {
                InstanceFieldRef ifr = (InstanceFieldRef) v;
                if ((m.isConcrete() && !m.isStatic() && m.retrieveActiveBody().getThisLocal().equivTo(ifr.getBase()) && this.tlo.isObjectThreadLocal(ifr, m)) || this.tlo.isObjectThreadLocal(ifr.getBase(), m)) {
                    return null;
                }
            } else if ((v instanceof ArrayRef) && this.tlo.isObjectThreadLocal(((ArrayRef) v).getBase(), m)) {
                return null;
            }
        }
        if (v instanceof InstanceFieldRef) {
            InstanceFieldRef ifr2 = (InstanceFieldRef) v;
            Local baseLocal = (Local) ifr2.getBase();
            PointsToSet base = this.pa.reachingObjects(baseLocal);
            if (baseLocal.getType() instanceof RefType) {
                SootClass baseClass = ((RefType) baseLocal.getType()).getSootClass();
                if (Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(baseClass, RefType.v("java.lang.Exception").getSootClass())) {
                    return null;
                }
            }
            ret = new StmtRWSet();
            ret.addFieldRef(base, ifr2.getField());
        } else if (v instanceof StaticFieldRef) {
            StaticFieldRef sfr = (StaticFieldRef) v;
            ret = new StmtRWSet();
            ret.addGlobal(sfr.getField());
        } else if (v instanceof ArrayRef) {
            ArrayRef ar = (ArrayRef) v;
            PointsToSet base2 = this.pa.reachingObjects((Local) ar.getBase());
            ret = new StmtRWSet();
            ret.addFieldRef(base2, PointsToAnalysis.ARRAY_ELEMENTS_NODE);
        }
        return ret;
    }

    public String toString() {
        return "TransactionAwareSideEffectAnalysis: PA=" + this.pa + " CG=" + this.cg;
    }
}
