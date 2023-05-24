package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.util.NumberedSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/TopologicalOrderer.class */
public class TopologicalOrderer {
    private final CallGraph cg;
    private final List<SootMethod> order = new ArrayList();
    private final NumberedSet<SootMethod> visited = new NumberedSet<>(Scene.v().getMethodNumberer());

    public TopologicalOrderer(CallGraph cg) {
        this.cg = cg;
    }

    public void go() {
        Iterator<MethodOrMethodContext> methods = this.cg.sourceMethods();
        while (methods.hasNext()) {
            SootMethod m = (SootMethod) methods.next();
            dfsVisit(m);
        }
    }

    private void dfsVisit(SootMethod m) {
        if (this.visited.contains(m)) {
            return;
        }
        this.visited.add(m);
        Iterator<MethodOrMethodContext> targets = new Targets(this.cg.edgesOutOf(m));
        while (targets.hasNext()) {
            SootMethod target = (SootMethod) targets.next();
            dfsVisit(target);
        }
        this.order.add(m);
    }

    public List<SootMethod> order() {
        return this.order;
    }
}
