package soot.jimple.spark.geom.utils;

import java.util.Iterator;
import soot.Scene;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/utils/SootInfo.class */
public class SootInfo {
    public static int countCallEdgesForCallsite(Stmt callsite, boolean stopForMutiple) {
        CallGraph cg = Scene.v().getCallGraph();
        int count = 0;
        Iterator<Edge> it = cg.edgesOutOf(callsite);
        while (it.hasNext()) {
            it.next();
            count++;
            if (stopForMutiple && count > 1) {
                break;
            }
        }
        return count;
    }
}
