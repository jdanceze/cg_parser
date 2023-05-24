package soot.jimple.spark.sets;

import java.util.LinkedList;
import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/AllSharedHybridNodes.class */
public class AllSharedHybridNodes {
    public BitVectorLookupMap lookupMap = new BitVectorLookupMap();

    public AllSharedHybridNodes(Singletons.Global g) {
    }

    public static AllSharedHybridNodes v() {
        return G.v().soot_jimple_spark_sets_AllSharedHybridNodes();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/AllSharedHybridNodes$BitVectorLookupMap.class */
    public class BitVectorLookupMap {
        public LinkedList[] map = new LinkedList[1];
        private static final int INCREASE_FACTOR = 2;

        public BitVectorLookupMap() {
        }

        public void add(int size, PointsToBitVector toAdd) {
            if (this.map.length < size + 1) {
                LinkedList[] newMap = new LinkedList[size * 2];
                System.arraycopy(this.map, 0, newMap, 0, this.map.length);
                this.map = newMap;
            }
            if (this.map[size] == null) {
                this.map[size] = new LinkedList();
            }
            this.map[size].add(toAdd);
        }

        public void remove(int size, PointsToBitVector toRemove) {
            this.map[size].remove(toRemove);
        }
    }
}
