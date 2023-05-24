package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.MethodContext;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ObjSensContextManager.class */
public class ObjSensContextManager implements ContextManager {
    private final CallGraph cg;

    public ObjSensContextManager(CallGraph cg) {
        this.cg = cg;
    }

    @Override // soot.jimple.toolkits.callgraph.ContextManager
    public void addStaticEdge(MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind) {
        this.cg.addEdge(new Edge(src, srcUnit, target, kind));
    }

    @Override // soot.jimple.toolkits.callgraph.ContextManager
    public void addVirtualEdge(MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind, Context typeContext) {
        this.cg.addEdge(new Edge(src, srcUnit, MethodContext.v(target, typeContext), kind));
    }

    @Override // soot.jimple.toolkits.callgraph.ContextManager
    public CallGraph callGraph() {
        return this.cg;
    }
}
