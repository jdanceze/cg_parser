package soot.jimple.infoflow.aliasing;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.IDESolver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.solver.IInfoflowSolver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/ImplicitFlowAliasStrategy.class */
public class ImplicitFlowAliasStrategy extends AbstractBulkAliasStrategy {
    protected final LoadingCache<SootMethod, Map<AccessPath, Set<AccessPath>>> methodToAliases;

    public ImplicitFlowAliasStrategy(InfoflowManager manager) {
        super(manager);
        this.methodToAliases = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, Map<AccessPath, Set<AccessPath>>>() { // from class: soot.jimple.infoflow.aliasing.ImplicitFlowAliasStrategy.1
            @Override // com.google.common.cache.CacheLoader
            public Map<AccessPath, Set<AccessPath>> load(SootMethod method) throws Exception {
                return ImplicitFlowAliasStrategy.this.computeGlobalAliases(method);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<AccessPath, Set<AccessPath>> computeGlobalAliases(SootMethod method) {
        Map<AccessPath, Set<AccessPath>> res = new HashMap<>();
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                if (((assign.getLeftOp() instanceof FieldRef) && ((assign.getRightOp() instanceof FieldRef) || (assign.getRightOp() instanceof Local))) || ((assign.getRightOp() instanceof FieldRef) && ((assign.getLeftOp() instanceof FieldRef) || (assign.getLeftOp() instanceof Local)))) {
                    AccessPath apLeft = this.manager.getAccessPathFactory().createAccessPath(assign.getLeftOp(), true);
                    AccessPath apRight = this.manager.getAccessPathFactory().createAccessPath(assign.getRightOp(), true);
                    Set<AccessPath> mapLeft = res.get(apLeft);
                    if (mapLeft == null) {
                        mapLeft = new HashSet<>();
                        res.put(apLeft, mapLeft);
                    }
                    mapLeft.add(apRight);
                    Set<AccessPath> mapRight = res.get(apRight);
                    if (mapRight == null) {
                        mapRight = new HashSet<>();
                        res.put(apRight, mapRight);
                    }
                    mapRight.add(apLeft);
                }
            }
        }
        return res;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void computeAliasTaints(Abstraction d1, Stmt src, Value targetValue, Set<Abstraction> taintSet, SootMethod method, Abstraction newAbs) {
        Value baseValue = ((InstanceFieldRef) targetValue).getBase();
        Set<AccessPath> aliases = this.methodToAliases.getUnchecked(method).get(this.manager.getAccessPathFactory().createAccessPath(baseValue, true));
        if (aliases != null) {
            for (AccessPath ap : aliases) {
                AccessPath newAP = this.manager.getAccessPathFactory().merge(ap, newAbs.getAccessPath());
                Abstraction aliasAbs = newAbs.deriveNewAbstraction(newAP, null);
                if (taintSet.add(aliasAbs) && ap.isInstanceFieldRef()) {
                    InstanceFieldRef aliasBaseVal = Jimple.v().newInstanceFieldRef(ap.getPlainValue(), ap.getFirstField().makeRef());
                    computeAliasTaints(d1, src, aliasBaseVal, taintSet, method, aliasAbs);
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void injectCallingContext(Abstraction abs, IInfoflowSolver fSolver, SootMethod callee, Unit callSite, Abstraction source, Abstraction d1) {
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isFlowSensitive() {
        return false;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean requiresAnalysisOnReturn() {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.AbstractAliasStrategy, soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean hasProcessedMethod(SootMethod method) {
        return this.methodToAliases.getIfPresent(method) != null;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public IInfoflowSolver getSolver() {
        return null;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void cleanup() {
        this.methodToAliases.invalidateAll();
        this.methodToAliases.cleanUp();
    }
}
