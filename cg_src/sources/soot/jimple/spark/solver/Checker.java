package soot.jimple.spark.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/Checker.class */
public class Checker {
    private static final Logger logger = LoggerFactory.getLogger(Checker.class);
    protected PAG pag;

    public Checker(PAG pag) {
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
    }

    protected void checkAll(final Node container, PointsToSetInternal nodes, final Node upstream) {
        nodes.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.Checker.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                Checker.this.checkNode(container, n, upstream);
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

    protected void handleStores(final VarNode src) {
        final PointsToSetInternal srcSet = src.getP2Set();
        if (srcSet.isEmpty()) {
            return;
        }
        Node[] storeTargets = this.pag.storeLookup(src);
        for (Node element : storeTargets) {
            FieldRefNode fr = (FieldRefNode) element;
            final SparkField f = fr.getField();
            fr.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.Checker.2
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    AllocDotField nDotF = Checker.this.pag.makeAllocDotField((AllocNode) n, f);
                    Checker.this.checkAll(nDotF, srcSet, src);
                }
            });
        }
    }

    protected void handleLoads(final FieldRefNode src) {
        final Node[] loadTargets = this.pag.loadLookup(src);
        final SparkField f = src.getField();
        src.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.Checker.3
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                Node[] nodeArr;
                AllocDotField nDotF = ((AllocNode) n).dot(f);
                if (nDotF == null) {
                    return;
                }
                PointsToSetInternal set = nDotF.getP2Set();
                if (set.isEmpty()) {
                    return;
                }
                for (Node element : loadTargets) {
                    VarNode target = (VarNode) element;
                    Checker.this.checkAll(target, set, src);
                }
            }
        });
    }
}
