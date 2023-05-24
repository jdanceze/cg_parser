package soot.jimple.infoflow.solver.cfg;

import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.SwitchStmt;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.toolkits.ide.icfg.BackwardsInterproceduralCFG;
import soot.toolkits.graph.DirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/cfg/BackwardsInfoflowCFG.class */
public class BackwardsInfoflowCFG extends InfoflowCFG {
    private final IInfoflowCFG baseCFG;

    public BackwardsInfoflowCFG(IInfoflowCFG baseCFG) {
        super(new BackwardsInterproceduralCFG(baseCFG));
        this.baseCFG = baseCFG;
    }

    public IInfoflowCFG getBaseCFG() {
        return this.baseCFG;
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isStaticFieldRead(SootMethod method, SootField variable) {
        return this.baseCFG.isStaticFieldRead(method, variable);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isStaticFieldUsed(SootMethod method, SootField variable) {
        return this.baseCFG.isStaticFieldUsed(method, variable);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean hasSideEffects(SootMethod method) {
        return this.baseCFG.hasSideEffects(method);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean methodReadsValue(SootMethod m, Value v) {
        return this.baseCFG.methodReadsValue(m, v);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean methodWritesValue(SootMethod m, Value v) {
        return this.baseCFG.methodWritesValue(m, v);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public IInfoflowCFG.UnitContainer getPostdominatorOf(Unit u) {
        return this.baseCFG.getPostdominatorOf(u);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public IInfoflowCFG.UnitContainer getDominatorOf(Unit u) {
        return this.baseCFG.getDominatorOf(u);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public boolean isExceptionalEdgeBetween(Unit u1, Unit u2) {
        return super.isExceptionalEdgeBetween(u2, u1);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public List<Unit> getConditionalBranchIntraprocedural(Unit unit) {
        SootMethod sm = getMethodOf(unit);
        if (sm.getDeclaringClass().getName().equals("dummyMainClass") && sm.getName().equals("dummy")) {
            return null;
        }
        DirectedGraph<Unit> graph = getOrCreateUnitGraph(sm);
        List<Unit> worklist = new ArrayList<>(sameLevelPredecessors(graph, unit));
        Set<Unit> doneSet = new HashSet<>();
        List<Unit> conditionals = new ArrayList<>();
        while (worklist.size() > 0) {
            Unit item = worklist.remove(0);
            doneSet.add(item);
            if ((item instanceof IfStmt) || (item instanceof SwitchStmt)) {
                conditionals.add(item);
            }
            List<Unit> preds = new ArrayList<>(sameLevelPredecessors(graph, item));
            doneSet.getClass();
            preds.removeIf((v1) -> {
                return r1.contains(v1);
            });
            worklist.addAll(preds);
        }
        return conditionals;
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public List<Unit> getConditionalBranchesInterprocedural(Unit unit) {
        List<Unit> conditionals = new ArrayList<>();
        Set<Unit> doneSet = new HashSet<>();
        getConditionalsRecursive(unit, conditionals, doneSet);
        return conditionals;
    }

    private void getConditionalsRecursive(@NotNull Unit unit, @NotNull List<Unit> conditionals, @NotNull Set<Unit> doneSet) {
        SootMethod sm = getMethodOf(unit);
        if (sm.getDeclaringClass().getName().equals("dummyMainClass") && sm.getName().equals("dummy")) {
            return;
        }
        DirectedGraph<Unit> graph = getOrCreateUnitGraph(sm);
        List<Unit> worklist = new ArrayList<>(sameLevelPredecessors(graph, unit));
        doneSet.getClass();
        worklist.removeIf((v1) -> {
            return r1.contains(v1);
        });
        doneSet.addAll(worklist);
        while (worklist.size() > 0) {
            Unit item = worklist.remove(0);
            if ((item instanceof IfStmt) || (item instanceof SwitchStmt)) {
                conditionals.add(item);
            }
            if ((item instanceof Stmt) && ((Stmt) item).containsInvokeExpr()) {
                List<Unit> entryPoints = (List) getPredsOf(item).stream().filter(pred -> {
                    return getMethodOf(sm) != r4;
                }).collect(Collectors.toList());
                doneSet.getClass();
                entryPoints.removeIf((v1) -> {
                    return r1.contains(v1);
                });
                for (Unit entryPoint : entryPoints) {
                    getConditionalsRecursive(entryPoint, conditionals, doneSet);
                }
            }
            if (isExitStmt(item)) {
                List<Unit> entryPoints2 = new ArrayList<>(getCallersOf(sm));
                doneSet.getClass();
                entryPoints2.removeIf((v1) -> {
                    return r1.contains(v1);
                });
                for (Unit entryPoint2 : entryPoints2) {
                    getConditionalsRecursive(entryPoint2, conditionals, doneSet);
                }
            }
            List<Unit> preds = new ArrayList<>(sameLevelPredecessors(graph, item));
            doneSet.getClass();
            preds.removeIf((v1) -> {
                return r1.contains(v1);
            });
            worklist.addAll(preds);
            doneSet.addAll(preds);
        }
    }

    private List<Unit> sameLevelPredecessors(DirectedGraph<Unit> graph, Unit u) {
        List<Unit> preds = graph.getPredsOf(u);
        if (preds.size() <= 1) {
            if (preds.size() == 0) {
                return preds;
            }
            Unit pred = preds.get(0);
            if (!pred.branches()) {
                return preds;
            }
            IInfoflowCFG.UnitContainer postdom = getPostdominatorOf(pred);
            if (postdom.getUnit() != u) {
                return preds;
            }
        }
        IInfoflowCFG.UnitContainer dom = getDominatorOf(u);
        if (dom.getUnit() != null) {
            return graph.getPredsOf(dom.getUnit());
        }
        return Collections.emptyList();
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public void notifyMethodChanged(SootMethod m) {
        this.baseCFG.notifyMethodChanged(m);
    }

    @Override // soot.jimple.infoflow.solver.cfg.InfoflowCFG, soot.jimple.infoflow.solver.cfg.IInfoflowCFG
    public void purge() {
        this.baseCFG.purge();
        super.purge();
    }
}
