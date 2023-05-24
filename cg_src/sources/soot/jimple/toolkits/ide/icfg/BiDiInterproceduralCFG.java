package soot.jimple.toolkits.ide.icfg;

import heros.InterproceduralCFG;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/BiDiInterproceduralCFG.class */
public interface BiDiInterproceduralCFG<N, M> extends InterproceduralCFG<N, M> {
    @Override // heros.InterproceduralCFG
    List<N> getPredsOf(N n);

    Collection<N> getEndPointsOf(M m);

    List<N> getPredsOfCallAt(N n);

    Set<N> allNonCallEndNodes();

    DirectedGraph<N> getOrCreateUnitGraph(M m);

    List<Value> getParameterRefs(M m);

    boolean isReturnSite(N n);

    boolean isReachable(N n);
}
