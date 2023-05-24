package soot.jimple.toolkits.callgraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ExplicitEdgesPred.class */
public class ExplicitEdgesPred implements EdgePredicate {
    @Override // soot.jimple.toolkits.callgraph.EdgePredicate
    public boolean want(Edge e) {
        return e.isExplicit();
    }
}
