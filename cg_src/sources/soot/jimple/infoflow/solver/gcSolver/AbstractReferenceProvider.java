package soot.jimple.infoflow.solver.gcSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.SootMethod;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/AbstractReferenceProvider.class */
public abstract class AbstractReferenceProvider<D, N> implements IGCReferenceProvider<D, N> {
    protected final BiDiInterproceduralCFG<N, SootMethod> icfg;

    public AbstractReferenceProvider(BiDiInterproceduralCFG<N, SootMethod> icfg) {
        this.icfg = icfg;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<SootMethod> getTransitiveCallees(SootMethod method) {
        Set<SootMethod> callees = new HashSet<>();
        List<SootMethod> workList = new ArrayList<>();
        workList.add(method);
        while (!workList.isEmpty()) {
            SootMethod sm = workList.remove(0);
            if (sm.isConcrete() && sm.hasActiveBody()) {
                for (N callSite : this.icfg.getCallsFromWithin(sm)) {
                    for (SootMethod callee : this.icfg.getCalleesOfCallAt(callSite)) {
                        if (callees.add(callee)) {
                            workList.add(callee);
                        }
                    }
                }
            }
        }
        return callees;
    }
}
