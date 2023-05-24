package soot.jimple.toolkits.annotation.callgraph;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.MethodOrMethodContext;
import soot.MethodToContexts;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.Host;
import soot.tagkit.LinkTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/callgraph/CallGraphTagger.class */
public class CallGraphTagger extends BodyTransformer {
    private MethodToContexts methodToContexts;

    public CallGraphTagger(Singletons.Global g) {
    }

    public static CallGraphTagger v() {
        return G.v().soot_jimple_toolkits_annotation_callgraph_CallGraphTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map options) {
        CallGraph cg = Scene.v().getCallGraph();
        if (this.methodToContexts == null) {
            this.methodToContexts = new MethodToContexts(Scene.v().getReachableMethods().listener());
        }
        Iterator stmtIt = b.getUnits().iterator();
        while (stmtIt.hasNext()) {
            Stmt s = (Stmt) stmtIt.next();
            Iterator edges = cg.edgesOutOf(s);
            while (edges.hasNext()) {
                Edge e = edges.next();
                SootMethod m = e.tgt();
                s.addTag(new LinkTag("CallGraph: Type: " + e.kind() + " Target Method/Context: " + e.getTgt().toString(), m, m.getDeclaringClass().getName(), "Call Graph"));
            }
        }
        SootMethod m2 = b.getMethod();
        for (MethodOrMethodContext momc : this.methodToContexts.get(m2)) {
            Iterator callerEdges = cg.edgesInto(momc);
            while (callerEdges.hasNext()) {
                Edge callEdge = callerEdges.next();
                SootMethod methodCaller = callEdge.src();
                Host src = methodCaller;
                if (callEdge.srcUnit() != null) {
                    src = callEdge.srcUnit();
                }
                m2.addTag(new LinkTag("CallGraph: Source Type: " + callEdge.kind() + " Source Method/Context: " + callEdge.getSrc().toString(), src, methodCaller.getDeclaringClass().getName(), "Call Graph"));
            }
        }
    }
}
