package soot.jimple.infoflow.solver.cfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.IDESolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import soot.JavaMethods;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/cfg/InfoflowCFG.class */
public class InfoflowCFG implements IInfoflowCFG {
    private static final int MAX_SIDE_EFFECT_ANALYSIS_DEPTH = 25;
    private static final int MAX_STATIC_USE_ANALYSIS_DEPTH = 50;
    protected final Map<SootMethod, Map<SootField, StaticFieldUse>> staticFieldUses;
    protected final Map<SootMethod, Boolean> methodSideEffects;
    protected final BiDiInterproceduralCFG<Unit, SootMethod> delegate;
    protected final LoadingCache<Unit, IInfoflowCFG.UnitContainer> unitsToDominator;
    protected final LoadingCache<Unit, IInfoflowCFG.UnitContainer> unitToPostdominator;
    protected final LoadingCache<SootMethod, Local[]> methodToUsedLocals;
    protected final LoadingCache<SootMethod, Local[]> methodToWrittenLocals;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$solver$cfg$InfoflowCFG$StaticFieldUse;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/cfg/InfoflowCFG$StaticFieldUse.class */
    public enum StaticFieldUse {
        Unknown,
        Unused,
        Read,
        Write,
        ReadWrite;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static StaticFieldUse[] valuesCustom() {
            StaticFieldUse[] valuesCustom = values();
            int length = valuesCustom.length;
            StaticFieldUse[] staticFieldUseArr = new StaticFieldUse[length];
            System.arraycopy(valuesCustom, 0, staticFieldUseArr, 0, length);
            return staticFieldUseArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$solver$cfg$InfoflowCFG$StaticFieldUse() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$solver$cfg$InfoflowCFG$StaticFieldUse;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[StaticFieldUse.valuesCustom().length];
        try {
            iArr2[StaticFieldUse.Read.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[StaticFieldUse.ReadWrite.ordinal()] = 5;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[StaticFieldUse.Unknown.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[StaticFieldUse.Unused.ordinal()] = 2;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[StaticFieldUse.Write.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$solver$cfg$InfoflowCFG$StaticFieldUse = iArr2;
        return iArr2;
    }

    public InfoflowCFG() {
        this(new JimpleBasedInterproceduralCFG(true, true));
    }

    public InfoflowCFG(BiDiInterproceduralCFG<Unit, SootMethod> delegate) {
        this.staticFieldUses = new ConcurrentHashMap();
        this.methodSideEffects = new ConcurrentHashMap();
        this.unitsToDominator = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Unit, IInfoflowCFG.UnitContainer>() { // from class: soot.jimple.infoflow.solver.cfg.InfoflowCFG.1
            @Override // com.google.common.cache.CacheLoader
            public IInfoflowCFG.UnitContainer load(Unit unit) throws Exception {
                SootMethod method = InfoflowCFG.this.getMethodOf(unit);
                DirectedGraph<Unit> graph = InfoflowCFG.this.delegate.getOrCreateUnitGraph(method);
                MHGDominatorsFinder<Unit> dominatorFinder = new MHGDominatorsFinder<>(graph);
                Unit dom = dominatorFinder.getImmediateDominator(unit);
                if (dom == null) {
                    return new IInfoflowCFG.UnitContainer(method);
                }
                return new IInfoflowCFG.UnitContainer(dom);
            }
        });
        this.unitToPostdominator = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Unit, IInfoflowCFG.UnitContainer>() { // from class: soot.jimple.infoflow.solver.cfg.InfoflowCFG.2
            @Override // com.google.common.cache.CacheLoader
            public IInfoflowCFG.UnitContainer load(Unit unit) throws Exception {
                SootMethod method = InfoflowCFG.this.getMethodOf(unit);
                DirectedGraph<Unit> graph = InfoflowCFG.this.delegate.getOrCreateUnitGraph(method);
                MHGPostDominatorsFinder<Unit> postdominatorFinder = new MHGPostDominatorsFinder<>(graph);
                Unit postdom = postdominatorFinder.getImmediateDominator(unit);
                if (postdom == null) {
                    return new IInfoflowCFG.UnitContainer(method);
                }
                return new IInfoflowCFG.UnitContainer(postdom);
            }
        });
        this.methodToUsedLocals = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, Local[]>() { // from class: soot.jimple.infoflow.solver.cfg.InfoflowCFG.3
            @Override // com.google.common.cache.CacheLoader
            public Local[] load(SootMethod method) throws Exception {
                if (!method.isConcrete() || !method.hasActiveBody()) {
                    return new Local[0];
                }
                List<Local> lcs = new ArrayList<>(method.getParameterCount() + (method.isStatic() ? 0 : 1));
                Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    for (ValueBox vb : u.getUseBoxes()) {
                        int i = 0;
                        while (true) {
                            if (i < method.getParameterCount()) {
                                if (method.getActiveBody().getParameterLocal(i) != vb.getValue()) {
                                    i++;
                                } else {
                                    lcs.add((Local) vb.getValue());
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!method.isStatic()) {
                    lcs.add(method.getActiveBody().getThisLocal());
                }
                return (Local[]) lcs.toArray(new Local[lcs.size()]);
            }
        });
        this.methodToWrittenLocals = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, Local[]>() { // from class: soot.jimple.infoflow.solver.cfg.InfoflowCFG.4
            @Override // com.google.common.cache.CacheLoader
            public Local[] load(SootMethod method) throws Exception {
                if (!method.isConcrete() || !method.hasActiveBody()) {
                    return new Local[0];
                }
                List<Local> lcs = new ArrayList<>(method.getActiveBody().getLocalCount());
                Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    if (u instanceof AssignStmt) {
                        AssignStmt assignStmt = (AssignStmt) u;
                        if (assignStmt.getLeftOp() instanceof Local) {
                            lcs.add((Local) assignStmt.getLeftOp());
                        }
                    }
                }
                return (Local[]) lcs.toArray(new Local[lcs.size()]);
            }
        });
        this.delegate = delegate;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public IInfoflowCFG.UnitContainer getPostdominatorOf(Unit u) {
        return this.unitToPostdominator.getUnchecked(u);
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public IInfoflowCFG.UnitContainer getDominatorOf(Unit u) {
        return this.unitsToDominator.getUnchecked(u);
    }

    @Override // heros.InterproceduralCFG
    public SootMethod getMethodOf(Unit u) {
        if (u == null) {
            throw new RuntimeException("Cannot get the method that contains a null unit");
        }
        return this.delegate.getMethodOf(u);
    }

    @Override // heros.InterproceduralCFG
    public List<Unit> getSuccsOf(Unit u) {
        return this.delegate.getSuccsOf(u);
    }

    @Override // heros.InterproceduralCFG
    public boolean isExitStmt(Unit u) {
        return this.delegate.isExitStmt(u);
    }

    @Override // heros.InterproceduralCFG
    public boolean isStartPoint(Unit u) {
        return this.delegate.isStartPoint(u);
    }

    @Override // heros.InterproceduralCFG
    public boolean isFallThroughSuccessor(Unit u, Unit succ) {
        return this.delegate.isFallThroughSuccessor(u, succ);
    }

    @Override // heros.InterproceduralCFG
    public boolean isBranchTarget(Unit u, Unit succ) {
        return this.delegate.isBranchTarget(u, succ);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getStartPointsOf(SootMethod m) {
        return this.delegate.getStartPointsOf(m);
    }

    @Override // heros.InterproceduralCFG
    public boolean isCallStmt(Unit u) {
        return this.delegate.isCallStmt(u);
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> allNonCallStartNodes() {
        return this.delegate.allNonCallStartNodes();
    }

    @Override // heros.InterproceduralCFG
    public Collection<SootMethod> getCalleesOfCallAt(Unit u) {
        return this.delegate.getCalleesOfCallAt(u);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getCallersOf(SootMethod m) {
        return this.delegate.getCallersOf(m);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getReturnSitesOfCallAt(Unit u) {
        return this.delegate.getReturnSitesOfCallAt(u);
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> getCallsFromWithin(SootMethod m) {
        return this.delegate.getCallsFromWithin(m);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG, heros.InterproceduralCFG
    public List<Unit> getPredsOf(Unit u) {
        return this.delegate.getPredsOf(u);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public Collection<Unit> getEndPointsOf(SootMethod m) {
        return this.delegate.getEndPointsOf(m);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public List<Unit> getPredsOfCallAt(Unit u) {
        return this.delegate.getPredsOf(u);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public Set<Unit> allNonCallEndNodes() {
        return this.delegate.allNonCallEndNodes();
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public DirectedGraph<Unit> getOrCreateUnitGraph(SootMethod m) {
        return this.delegate.getOrCreateUnitGraph(m);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public List<Value> getParameterRefs(SootMethod m) {
        return this.delegate.getParameterRefs(m);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public boolean isReturnSite(Unit n) {
        return this.delegate.isReturnSite(n);
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isStaticFieldRead(SootMethod method, SootField variable) {
        StaticFieldUse use = checkStaticFieldUsed(method, variable);
        return use == StaticFieldUse.Read || use == StaticFieldUse.ReadWrite || use == StaticFieldUse.Unknown;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isStaticFieldUsed(SootMethod method, SootField variable) {
        StaticFieldUse use = checkStaticFieldUsed(method, variable);
        return use == StaticFieldUse.Write || use == StaticFieldUse.ReadWrite || use == StaticFieldUse.Unknown;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.util.Map, java.util.HashMap] */
    protected StaticFieldUse checkStaticFieldUsed(SootMethod smethod, SootField variable) {
        StaticFieldUse b;
        if (!smethod.isConcrete() || !smethod.hasActiveBody()) {
            return StaticFieldUse.Unused;
        }
        List<SootMethod> workList = new ArrayList<>();
        workList.add(smethod);
        ?? hashMap = new HashMap();
        int processedMethods = 0;
        while (!workList.isEmpty()) {
            SootMethod method = workList.remove(workList.size() - 1);
            processedMethods++;
            if (method.hasActiveBody()) {
                if (processedMethods > 50) {
                    return StaticFieldUse.Unknown;
                }
                boolean hasInvocation = false;
                boolean reads = false;
                boolean writes = false;
                Map<SootField, StaticFieldUse> entry = this.staticFieldUses.get(method);
                if (entry != null && (b = entry.get(variable)) != null && b != StaticFieldUse.Unknown) {
                    hashMap.put(method, b);
                } else {
                    StaticFieldUse oldUse = (StaticFieldUse) hashMap.get(method);
                    Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        if (u instanceof AssignStmt) {
                            AssignStmt assign = (AssignStmt) u;
                            if (assign.getLeftOp() instanceof StaticFieldRef) {
                                SootField sf = ((StaticFieldRef) assign.getLeftOp()).getField();
                                registerStaticVariableUse(method, sf, StaticFieldUse.Write);
                                if (variable.equals(sf)) {
                                    writes = true;
                                }
                            }
                            if (assign.getRightOp() instanceof StaticFieldRef) {
                                SootField sf2 = ((StaticFieldRef) assign.getRightOp()).getField();
                                registerStaticVariableUse(method, sf2, StaticFieldUse.Read);
                                if (variable.equals(sf2)) {
                                    reads = true;
                                }
                            }
                        }
                        if (((Stmt) u).containsInvokeExpr()) {
                            Iterator<Edge> edgeIt = Scene.v().getCallGraph().edgesOutOf(u);
                            while (edgeIt.hasNext()) {
                                Edge e = edgeIt.next();
                                SootMethod callee = e.getTgt().method();
                                if (callee.isConcrete()) {
                                    StaticFieldUse calleeUse = (StaticFieldUse) hashMap.get(callee);
                                    if (calleeUse == null) {
                                        if (!hasInvocation) {
                                            workList.add(method);
                                        }
                                        workList.add(callee);
                                        hasInvocation = true;
                                    } else {
                                        reads |= calleeUse == StaticFieldUse.Read || calleeUse == StaticFieldUse.ReadWrite;
                                        writes |= calleeUse == StaticFieldUse.Write || calleeUse == StaticFieldUse.ReadWrite;
                                    }
                                }
                            }
                        }
                    }
                    StaticFieldUse fieldUse = StaticFieldUse.Unused;
                    if (reads && writes) {
                        fieldUse = StaticFieldUse.ReadWrite;
                    } else if (reads) {
                        fieldUse = StaticFieldUse.Read;
                    } else if (writes) {
                        fieldUse = StaticFieldUse.Write;
                    }
                    if (fieldUse != oldUse) {
                        hashMap.put(method, fieldUse);
                    }
                }
            }
        }
        Throwable th = hashMap;
        synchronized (th) {
            for (Map.Entry<SootMethod, StaticFieldUse> tempEntry : hashMap.entrySet()) {
                registerStaticVariableUse(tempEntry.getKey(), variable, tempEntry.getValue());
            }
            th = th;
            StaticFieldUse outerUse = (StaticFieldUse) hashMap.get(smethod);
            return outerUse == null ? StaticFieldUse.Unknown : outerUse;
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, java.util.Map] */
    protected void registerStaticVariableUse(SootMethod method, SootField variable, StaticFieldUse fieldUse) {
        StaticFieldUse newUse;
        Map<SootField, StaticFieldUse> map = this.staticFieldUses.get(method);
        synchronized (this.staticFieldUses) {
            if (map == 0) {
                Map<SootField, StaticFieldUse> entry = new ConcurrentHashMap<>();
                this.staticFieldUses.put(method, entry);
                entry.put(variable, fieldUse);
                return;
            }
            StaticFieldUse oldUse = (StaticFieldUse) map.get(variable);
            if (oldUse == null) {
                map.put(variable, fieldUse);
                return;
            }
            switch ($SWITCH_TABLE$soot$jimple$infoflow$solver$cfg$InfoflowCFG$StaticFieldUse()[oldUse.ordinal()]) {
                case 1:
                case 2:
                case 5:
                    newUse = fieldUse;
                    break;
                case 3:
                    newUse = fieldUse == StaticFieldUse.Read ? oldUse : StaticFieldUse.ReadWrite;
                    break;
                case 4:
                    newUse = fieldUse == StaticFieldUse.Write ? oldUse : StaticFieldUse.ReadWrite;
                    break;
                default:
                    throw new RuntimeException("Invalid field use");
            }
            map.put(variable, newUse);
        }
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean hasSideEffects(SootMethod method) {
        return hasSideEffects(method, new HashSet(), 0);
    }

    protected boolean hasSideEffects(SootMethod method, Set<SootMethod> runList, int depth) {
        if (!method.hasActiveBody() || !runList.add(method)) {
            return false;
        }
        Boolean hasSideEffects = this.methodSideEffects.get(method);
        if (hasSideEffects != null) {
            return hasSideEffects.booleanValue();
        }
        if (depth > 25) {
            return true;
        }
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                if (assign.getLeftOp() instanceof FieldRef) {
                    this.methodSideEffects.put(method, true);
                    return true;
                }
            }
            if (((Stmt) u).containsInvokeExpr()) {
                Iterator<Edge> edgeIt = Scene.v().getCallGraph().edgesOutOf(u);
                while (edgeIt.hasNext()) {
                    Edge e = edgeIt.next();
                    int i = depth;
                    depth++;
                    if (hasSideEffects(e.getTgt().method(), runList, i)) {
                        return true;
                    }
                }
                continue;
            }
        }
        this.methodSideEffects.put(method, false);
        return false;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public void notifyMethodChanged(SootMethod m) {
        if (this.delegate instanceof JimpleBasedInterproceduralCFG) {
            ((JimpleBasedInterproceduralCFG) this.delegate).initializeUnitToOwner(m);
        }
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean methodReadsValue(SootMethod m, Value v) {
        Local[] reads = this.methodToUsedLocals.getUnchecked(m);
        if (reads != null) {
            for (Local l : reads) {
                if (l == v) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean methodWritesValue(SootMethod m, Value v) {
        Local[] writes = this.methodToWrittenLocals.getUnchecked(m);
        if (writes != null) {
            for (Local l : writes) {
                if (l == v) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isExceptionalEdgeBetween(Unit u1, Unit u2) {
        ThrowableSet ts;
        SootMethod m1 = getMethodOf(u1);
        SootMethod m2 = getMethodOf(u2);
        if (m1 != m2) {
            throw new RuntimeException("Exceptional edges are only supported inside the same method");
        }
        DirectedGraph<Unit> ug1 = getOrCreateUnitGraph(m1);
        if (!(ug1 instanceof ExceptionalUnitGraph)) {
            return false;
        }
        ExceptionalUnitGraph eug = (ExceptionalUnitGraph) ug1;
        if (!eug.getExceptionalSuccsOf(u1).contains(u2)) {
            return false;
        }
        Collection<ExceptionalUnitGraph.ExceptionDest> dests = eug.getExceptionDests(u1);
        if (dests != null && !dests.isEmpty() && (ts = Scene.v().getDefaultThrowAnalysis().mightThrow(u1)) != null) {
            boolean hasTraps = false;
            for (ExceptionalUnitGraph.ExceptionDest dest : dests) {
                Trap trap = dest.getTrap();
                if (trap != null) {
                    hasTraps = true;
                    if (!ts.catchableAs(trap.getException().getType())) {
                        return false;
                    }
                }
            }
            if (!hasTraps) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public List<Unit> getConditionalBranchIntraprocedural(Unit callSite) {
        return null;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public List<Unit> getConditionalBranchesInterprocedural(Unit unit) {
        return null;
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public boolean isReachable(Unit u) {
        return this.delegate.isReachable(u);
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isExecutorExecute(InvokeExpr ie, SootMethod dest) {
        if (ie == null || dest == null) {
            return false;
        }
        SootMethod ieMethod = ie.getMethod();
        if (!ieMethod.getName().equals("execute") && !ieMethod.getName().equals("doPrivileged")) {
            return false;
        }
        String ieSubSig = ieMethod.getSubSignature();
        String calleeSubSig = dest.getSubSignature();
        if (ieSubSig.equals("void execute(java.lang.Runnable)") && calleeSubSig.equals(JavaMethods.SIG_RUN)) {
            return true;
        }
        if (dest.getName().equals("run") && dest.getParameterCount() == 0 && (dest.getReturnType() instanceof RefType)) {
            if (ieSubSig.equals("java.lang.Object doPrivileged(java.security.PrivilegedAction)") || ieSubSig.equals("java.lang.Object doPrivileged(java.security.PrivilegedAction,java.security.AccessControlContext)") || ieSubSig.equals("java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction)") || ieSubSig.equals("java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction,java.security.AccessControlContext)")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public Collection<SootMethod> getOrdinaryCalleesOfCallAt(Unit u) {
        InvokeExpr iexpr = ((Stmt) u).getInvokeExpr();
        Collection<SootMethod> originalCallees = getCalleesOfCallAt(u);
        List<SootMethod> callees = new ArrayList<>(originalCallees.size());
        for (SootMethod sm : originalCallees) {
            if (!sm.isStaticInitializer() && !isExecutorExecute(iexpr, sm)) {
                callees.add(sm);
            }
        }
        return callees;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isReflectiveCallSite(Unit u) {
        if (isCallStmt(u)) {
            InvokeExpr iexpr = ((Stmt) u).getInvokeExpr();
            return isReflectiveCallSite(iexpr);
        }
        return false;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isReflectiveCallSite(InvokeExpr iexpr) {
        if (iexpr instanceof VirtualInvokeExpr) {
            VirtualInvokeExpr viexpr = (VirtualInvokeExpr) iexpr;
            if ((viexpr.getBase().getType() instanceof RefType) && ((RefType) viexpr.getBase().getType()).getSootClass().getName().equals("java.lang.reflect.Method") && viexpr.getMethod().getName().equals("invoke")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public void purge() {
        this.methodSideEffects.clear();
        this.staticFieldUses.clear();
        this.methodToUsedLocals.invalidateAll();
        this.methodToUsedLocals.cleanUp();
        this.methodToWrittenLocals.invalidateAll();
        this.methodToWrittenLocals.cleanUp();
        this.unitToPostdominator.invalidateAll();
        this.unitToPostdominator.cleanUp();
        this.unitsToDominator.invalidateAll();
        this.unitsToDominator.cleanUp();
    }
}
