package soot.jimple.spark.sets;

import java.util.HashMap;
import java.util.Map;
import soot.G;
import soot.Singletons;
import soot.jimple.spark.sets.SharedListSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/AllSharedListNodes.class */
public class AllSharedListNodes {
    public Map<SharedListSet.Pair, SharedListSet.ListNode> allNodes = new HashMap();

    public AllSharedListNodes(Singletons.Global g) {
    }

    public static AllSharedListNodes v() {
        return G.v().soot_jimple_spark_sets_AllSharedListNodes();
    }
}
