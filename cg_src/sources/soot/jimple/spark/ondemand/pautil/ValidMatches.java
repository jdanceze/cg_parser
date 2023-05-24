package soot.jimple.spark.ondemand.pautil;

import java.util.Iterator;
import java.util.Set;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.HashSetMultiMap;
import soot.jimple.spark.ondemand.genericutil.MultiMap;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/ValidMatches.class */
public class ValidMatches {
    private final MultiMap<VarNode, VarNode> vMatchEdges = new HashSetMultiMap();
    private final MultiMap<VarNode, VarNode> vMatchBarEdges = new HashSetMultiMap();

    public ValidMatches(PAG pag, SootUtil.FieldToEdgesMap fieldToStores) {
        for (FieldRefNode loadSource : pag.loadSources()) {
            SparkField field = loadSource.getField();
            VarNode loadBase = loadSource.getBase();
            ArraySet<Pair<VarNode, VarNode>> storesOnField = fieldToStores.get((SootUtil.FieldToEdgesMap) field);
            Iterator<Pair<VarNode, VarNode>> it = storesOnField.iterator();
            while (it.hasNext()) {
                Pair<VarNode, VarNode> store = it.next();
                VarNode storeBase = store.getO2();
                if (loadBase.getP2Set().hasNonEmptyIntersection(storeBase.getP2Set())) {
                    VarNode matchSrc = store.getO1();
                    Node[] loadTargets = pag.loadLookup(loadSource);
                    for (Node node : loadTargets) {
                        VarNode matchTgt = (VarNode) node;
                        this.vMatchEdges.put(matchSrc, matchTgt);
                        this.vMatchBarEdges.put(matchTgt, matchSrc);
                    }
                }
            }
        }
    }

    public Set<VarNode> vMatchLookup(VarNode src) {
        return this.vMatchEdges.get(src);
    }

    public Set<VarNode> vMatchInvLookup(VarNode src) {
        return this.vMatchBarEdges.get(src);
    }
}
