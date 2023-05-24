package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Kind;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/TargetMethodsFinder.class */
public class TargetMethodsFinder {
    public List<SootMethod> find(Unit unit, CallGraph cg, boolean canBeNullList, boolean canBeNative) {
        List<SootMethod> target = new ArrayList<>();
        Iterator<Edge> it = cg.edgesOutOf(unit);
        while (it.hasNext()) {
            Edge edge = it.next();
            SootMethod targetMethod = edge.tgt();
            if (!targetMethod.isNative() || canBeNative) {
                if (edge.kind() != Kind.CLINIT) {
                    target.add(targetMethod);
                }
            }
        }
        if (target.size() < 1 && !canBeNullList) {
            throw new RuntimeException("No target method for: " + unit);
        }
        return target;
    }
}
