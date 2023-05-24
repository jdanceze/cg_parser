package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.Context;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ContextSensitiveCallGraph.class */
public interface ContextSensitiveCallGraph {
    Iterator<MethodOrMethodContext> edgeSources();

    Iterator<ContextSensitiveEdge> allEdges();

    Iterator<ContextSensitiveEdge> edgesOutOf(Context context, SootMethod sootMethod, Unit unit);

    Iterator<ContextSensitiveEdge> edgesOutOf(Context context, SootMethod sootMethod);

    Iterator<ContextSensitiveEdge> edgesInto(Context context, SootMethod sootMethod);
}
