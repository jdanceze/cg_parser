package soot.jimple.toolkits.callgraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/InstanceInvokeEdgesPred.class */
public class InstanceInvokeEdgesPred implements EdgePredicate {
    @Override // soot.jimple.toolkits.callgraph.EdgePredicate
    public boolean want(Edge e) {
        return e.isInstance();
    }
}
