package soot.jimple.toolkits.ide.icfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.DontSynchronize;
import heros.SynchronizedBy;
import heros.solver.IDESolver;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/AbstractJimpleBasedICFG.class */
public abstract class AbstractJimpleBasedICFG implements BiDiInterproceduralCFG<Unit, SootMethod> {
    protected final boolean enableExceptions;
    @DontSynchronize("written by single thread; read afterwards")
    private final Map<Unit, Body> unitToOwner;
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected LoadingCache<Body, DirectedGraph<Unit>> bodyToUnitGraph;
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected LoadingCache<SootMethod, List<Value>> methodToParameterRefs;
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected LoadingCache<SootMethod, Set<Unit>> methodToCallsFromWithin;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractJimpleBasedICFG.class.desiredAssertionStatus();
    }

    public AbstractJimpleBasedICFG() {
        this(true);
    }

    protected Map<Unit, Body> createUnitToOwnerMap() {
        return new LinkedHashMap();
    }

    public AbstractJimpleBasedICFG(boolean enableExceptions) {
        this.unitToOwner = createUnitToOwnerMap();
        this.bodyToUnitGraph = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Body, DirectedGraph<Unit>>() { // from class: soot.jimple.toolkits.ide.icfg.AbstractJimpleBasedICFG.1
            @Override // com.google.common.cache.CacheLoader
            public DirectedGraph<Unit> load(Body body) throws Exception {
                return AbstractJimpleBasedICFG.this.makeGraph(body);
            }
        });
        this.methodToParameterRefs = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, List<Value>>() { // from class: soot.jimple.toolkits.ide.icfg.AbstractJimpleBasedICFG.2
            @Override // com.google.common.cache.CacheLoader
            public List<Value> load(SootMethod m) throws Exception {
                return m.getActiveBody().getParameterRefs();
            }
        });
        this.methodToCallsFromWithin = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, Set<Unit>>() { // from class: soot.jimple.toolkits.ide.icfg.AbstractJimpleBasedICFG.3
            @Override // com.google.common.cache.CacheLoader
            public Set<Unit> load(SootMethod m) throws Exception {
                return AbstractJimpleBasedICFG.this.getCallsFromWithinMethod(m);
            }
        });
        this.enableExceptions = enableExceptions;
    }

    public Body getBodyOf(Unit u) {
        if ($assertionsDisabled || this.unitToOwner.containsKey(u)) {
            Body b = this.unitToOwner.get(u);
            return b;
        }
        throw new AssertionError("Statement " + u + " not in unit-to-owner mapping");
    }

    @Override // heros.InterproceduralCFG
    public SootMethod getMethodOf(Unit u) {
        Body b = getBodyOf(u);
        if (b == null) {
            return null;
        }
        return b.getMethod();
    }

    @Override // heros.InterproceduralCFG
    public List<Unit> getSuccsOf(Unit u) {
        Body body = getBodyOf(u);
        if (body == null) {
            return Collections.emptyList();
        }
        DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
        return unitGraph.getSuccsOf(u);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public DirectedGraph<Unit> getOrCreateUnitGraph(SootMethod m) {
        return getOrCreateUnitGraph(m.getActiveBody());
    }

    public DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
        return this.bodyToUnitGraph.getUnchecked(body);
    }

    protected DirectedGraph<Unit> makeGraph(Body body) {
        return this.enableExceptions ? ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body) : new BriefUnitGraph(body);
    }

    protected Set<Unit> getCallsFromWithinMethod(SootMethod m) {
        Set<Unit> res = null;
        Iterator<Unit> it = m.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (isCallStmt(u)) {
                if (res == null) {
                    res = new LinkedHashSet<>();
                }
                res.add(u);
            }
        }
        return res == null ? Collections.emptySet() : res;
    }

    @Override // heros.InterproceduralCFG
    public boolean isExitStmt(Unit u) {
        Body body = getBodyOf(u);
        DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
        return unitGraph.getTails().contains(u);
    }

    @Override // heros.InterproceduralCFG
    public boolean isStartPoint(Unit u) {
        Body body = getBodyOf(u);
        DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
        return unitGraph.getHeads().contains(u);
    }

    @Override // heros.InterproceduralCFG
    public boolean isFallThroughSuccessor(Unit u, Unit succ) {
        if ($assertionsDisabled || getSuccsOf(u).contains(succ)) {
            if (!u.fallsThrough()) {
                return false;
            }
            Body body = getBodyOf(u);
            return body.getUnits().getSuccOf((UnitPatchingChain) u) == succ;
        }
        throw new AssertionError();
    }

    @Override // heros.InterproceduralCFG
    public boolean isBranchTarget(Unit u, Unit succ) {
        if ($assertionsDisabled || getSuccsOf(u).contains(succ)) {
            if (!u.branches()) {
                return false;
            }
            for (UnitBox ub : u.getUnitBoxes()) {
                if (ub.getUnit() == succ) {
                    return true;
                }
            }
            return false;
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public List<Value> getParameterRefs(SootMethod m) {
        return this.methodToParameterRefs.getUnchecked(m);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getStartPointsOf(SootMethod m) {
        if (m.hasActiveBody()) {
            Body body = m.getActiveBody();
            DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
            return unitGraph.getHeads();
        }
        return Collections.emptySet();
    }

    public boolean setOwnerStatement(Unit u, Body b) {
        return this.unitToOwner.put(u, b) == null;
    }

    @Override // heros.InterproceduralCFG
    public boolean isCallStmt(Unit u) {
        return ((Stmt) u).containsInvokeExpr();
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> allNonCallStartNodes() {
        Set<Unit> res = new LinkedHashSet<>(this.unitToOwner.keySet());
        Iterator<Unit> iter = res.iterator();
        while (iter.hasNext()) {
            Unit u = iter.next();
            if (isStartPoint(u) || isCallStmt(u)) {
                iter.remove();
            }
        }
        return res;
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public Set<Unit> allNonCallEndNodes() {
        Set<Unit> res = new LinkedHashSet<>(this.unitToOwner.keySet());
        Iterator<Unit> iter = res.iterator();
        while (iter.hasNext()) {
            Unit u = iter.next();
            if (isExitStmt(u) || isCallStmt(u)) {
                iter.remove();
            }
        }
        return res;
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getReturnSitesOfCallAt(Unit u) {
        return getSuccsOf(u);
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> getCallsFromWithin(SootMethod m) {
        return this.methodToCallsFromWithin.getUnchecked(m);
    }

    public void initializeUnitToOwner(SootMethod m) {
        if (m.hasActiveBody()) {
            Body b = m.getActiveBody();
            PatchingChain<Unit> units = b.getUnits();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                this.unitToOwner.put(unit, b);
            }
        }
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG, heros.InterproceduralCFG
    public List<Unit> getPredsOf(Unit u) {
        if ($assertionsDisabled || u != null) {
            Body body = getBodyOf(u);
            if (body == null) {
                return Collections.emptyList();
            }
            DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
            return unitGraph.getPredsOf(u);
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public Collection<Unit> getEndPointsOf(SootMethod m) {
        if (m.hasActiveBody()) {
            Body body = m.getActiveBody();
            DirectedGraph<Unit> unitGraph = getOrCreateUnitGraph(body);
            return unitGraph.getTails();
        }
        return Collections.emptySet();
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public List<Unit> getPredsOfCallAt(Unit u) {
        return getPredsOf(u);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public boolean isReturnSite(Unit n) {
        for (Unit pred : getPredsOf(n)) {
            if (isCallStmt(pred)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public boolean isReachable(Unit u) {
        return this.unitToOwner.containsKey(u);
    }
}
