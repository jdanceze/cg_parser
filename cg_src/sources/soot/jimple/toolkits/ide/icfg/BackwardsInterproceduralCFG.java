package soot.jimple.toolkits.ide.icfg;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/BackwardsInterproceduralCFG.class */
public class BackwardsInterproceduralCFG implements BiDiInterproceduralCFG<Unit, SootMethod> {
    protected final BiDiInterproceduralCFG<Unit, SootMethod> delegate;

    public BackwardsInterproceduralCFG(BiDiInterproceduralCFG<Unit, SootMethod> fwICFG) {
        this.delegate = fwICFG;
    }

    @Override // heros.InterproceduralCFG
    public List<Unit> getSuccsOf(Unit n) {
        return this.delegate.getPredsOf(n);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getStartPointsOf(SootMethod m) {
        return this.delegate.getEndPointsOf(m);
    }

    @Override // heros.InterproceduralCFG
    public List<Unit> getReturnSitesOfCallAt(Unit n) {
        return this.delegate.getPredsOfCallAt(n);
    }

    @Override // heros.InterproceduralCFG
    public boolean isExitStmt(Unit stmt) {
        return this.delegate.isStartPoint(stmt);
    }

    @Override // heros.InterproceduralCFG
    public boolean isStartPoint(Unit stmt) {
        return this.delegate.isExitStmt(stmt);
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> allNonCallStartNodes() {
        return this.delegate.allNonCallEndNodes();
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG, heros.InterproceduralCFG
    public List<Unit> getPredsOf(Unit u) {
        return this.delegate.getSuccsOf(u);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public Collection<Unit> getEndPointsOf(SootMethod m) {
        return this.delegate.getStartPointsOf(m);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public List<Unit> getPredsOfCallAt(Unit u) {
        return this.delegate.getSuccsOf(u);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public Set<Unit> allNonCallEndNodes() {
        return this.delegate.allNonCallStartNodes();
    }

    @Override // heros.InterproceduralCFG
    public SootMethod getMethodOf(Unit n) {
        return this.delegate.getMethodOf(n);
    }

    @Override // heros.InterproceduralCFG
    public Collection<SootMethod> getCalleesOfCallAt(Unit n) {
        return this.delegate.getCalleesOfCallAt(n);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getCallersOf(SootMethod m) {
        return this.delegate.getCallersOf(m);
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> getCallsFromWithin(SootMethod m) {
        return this.delegate.getCallsFromWithin(m);
    }

    @Override // heros.InterproceduralCFG
    public boolean isCallStmt(Unit stmt) {
        return this.delegate.isCallStmt(stmt);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public DirectedGraph<Unit> getOrCreateUnitGraph(SootMethod m) {
        return this.delegate.getOrCreateUnitGraph(m);
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public List<Value> getParameterRefs(SootMethod m) {
        return this.delegate.getParameterRefs(m);
    }

    @Override // heros.InterproceduralCFG
    public boolean isFallThroughSuccessor(Unit stmt, Unit succ) {
        throw new UnsupportedOperationException("not implemented because semantics unclear");
    }

    @Override // heros.InterproceduralCFG
    public boolean isBranchTarget(Unit stmt, Unit succ) {
        throw new UnsupportedOperationException("not implemented because semantics unclear");
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public boolean isReturnSite(Unit n) {
        for (Unit pred : getSuccsOf(n)) {
            if (isCallStmt(pred)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG
    public boolean isReachable(Unit u) {
        return this.delegate.isReachable(u);
    }
}
