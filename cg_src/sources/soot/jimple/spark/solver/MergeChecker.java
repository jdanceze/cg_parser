package soot.jimple.spark.solver;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/MergeChecker.class */
public class MergeChecker {
    private static final Logger logger = LoggerFactory.getLogger(MergeChecker.class);
    protected PAG pag;
    protected MultiMap<SparkField, VarNode> fieldToBase = new HashMultiMap();

    public MergeChecker(PAG pag) {
        this.pag = pag;
    }

    public void check() {
        for (Object object : this.pag.allocSources()) {
            handleAllocNode((AllocNode) object);
        }
        for (Object object2 : this.pag.simpleSources()) {
            handleSimples((VarNode) object2);
        }
        for (Object object3 : this.pag.loadSources()) {
            handleLoads((FieldRefNode) object3);
        }
        for (Object object4 : this.pag.storeSources()) {
            handleStores((VarNode) object4);
        }
        for (Object object5 : this.pag.loadSources()) {
            FieldRefNode fr = (FieldRefNode) object5;
            this.fieldToBase.put(fr.getField(), fr.getBase());
        }
        for (Object object6 : this.pag.storeInvSources()) {
            FieldRefNode fr2 = (FieldRefNode) object6;
            this.fieldToBase.put(fr2.getField(), fr2.getBase());
        }
        Iterator<VarNode> it = this.pag.getVarNodeNumberer().iterator();
        while (it.hasNext()) {
            VarNode src = it.next();
            for (FieldRefNode fr3 : src.getAllFieldRefs()) {
                for (VarNode dst : this.fieldToBase.get(fr3.getField())) {
                    if (src.getP2Set().hasNonEmptyIntersection(dst.getP2Set())) {
                        FieldRefNode fr22 = dst.dot(fr3.getField());
                        if (fr22.getReplacement() != fr3.getReplacement()) {
                            logger.debug("Check failure: " + fr3 + " should be merged with " + fr22);
                        }
                    }
                }
            }
        }
    }

    protected void checkAll(final Node container, PointsToSetInternal nodes, final Node upstream) {
        nodes.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.MergeChecker.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                MergeChecker.this.checkNode(container, n, upstream);
            }
        });
    }

    protected void checkNode(Node container, Node n, Node upstream) {
        if (container.getReplacement() != container) {
            throw new RuntimeException("container " + container + " is illegal");
        }
        if (upstream.getReplacement() != upstream) {
            throw new RuntimeException("upstream " + upstream + " is illegal");
        }
        PointsToSetInternal p2set = container.getP2Set();
        FastHierarchy fh = this.pag.getTypeManager().getFastHierarchy();
        if (!p2set.contains(n)) {
            if (fh == null || container.getType() == null || fh.canStoreType(n.getType(), container.getType())) {
                logger.debug("Check failure: " + container + " does not have " + n + "; upstream is " + upstream);
            }
        }
    }

    protected void handleAllocNode(AllocNode src) {
        Node[] targets = this.pag.allocLookup(src);
        for (Node element : targets) {
            checkNode(element, src, src);
        }
    }

    protected void handleSimples(VarNode src) {
        PointsToSetInternal srcSet = src.getP2Set();
        if (srcSet.isEmpty()) {
            return;
        }
        Node[] simpleTargets = this.pag.simpleLookup(src);
        for (Node element : simpleTargets) {
            checkAll(element, srcSet, src);
        }
    }

    protected void handleStores(VarNode src) {
        PointsToSetInternal srcSet = src.getP2Set();
        if (srcSet.isEmpty()) {
            return;
        }
        Node[] storeTargets = this.pag.storeLookup(src);
        for (Node element : storeTargets) {
            FieldRefNode fr = (FieldRefNode) element;
            checkAll(fr, srcSet, src);
        }
    }

    protected void handleLoads(FieldRefNode src) {
        Node[] loadTargets = this.pag.loadLookup(src);
        PointsToSetInternal set = src.getP2Set();
        if (set.isEmpty()) {
            return;
        }
        for (Node element : loadTargets) {
            VarNode target = (VarNode) element;
            checkAll(target, set, src);
        }
    }
}
