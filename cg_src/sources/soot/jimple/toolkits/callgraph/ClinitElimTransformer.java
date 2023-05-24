package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ClinitElimTransformer.class */
public class ClinitElimTransformer extends BodyTransformer {
    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Stmt srcStmt;
        ClinitElimAnalysis a = new ClinitElimAnalysis(new BriefUnitGraph(b));
        CallGraph cg = Scene.v().getCallGraph();
        Iterator<Edge> edgeIt = cg.edgesOutOf(b.getMethod());
        while (edgeIt.hasNext()) {
            Edge e = edgeIt.next();
            if (e.isClinit() && (srcStmt = e.srcStmt()) != null && a.getFlowBefore(srcStmt).contains(e.tgt())) {
                cg.removeEdge(e);
            }
        }
    }
}
