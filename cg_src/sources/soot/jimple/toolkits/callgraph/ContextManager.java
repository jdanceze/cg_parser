package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ContextManager.class */
public interface ContextManager {
    void addStaticEdge(MethodOrMethodContext methodOrMethodContext, Unit unit, SootMethod sootMethod, Kind kind);

    void addVirtualEdge(MethodOrMethodContext methodOrMethodContext, Unit unit, SootMethod sootMethod, Kind kind, Context context);

    CallGraph callGraph();
}
