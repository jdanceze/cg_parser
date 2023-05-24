package soot.jimple.spark.solver;

import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/PropMerge.class */
public class PropMerge extends Propagator {
    private static final Logger logger = LoggerFactory.getLogger(PropMerge.class);
    protected final Set<Node> varNodeWorkList = new TreeSet();
    protected PAG pag;

    public PropMerge(PAG pag) {
        this.pag = pag;
    }

    @Override // soot.jimple.spark.solver.Propagator
    public void propagate() {
        new TopoSorter(this.pag, false).sort();
        for (Object object : this.pag.allocSources()) {
            handleAllocNode((AllocNode) object);
        }
        boolean verbose = this.pag.getOpts().verbose();
        do {
            if (verbose) {
                logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
            }
            int iter = 0;
            while (!this.varNodeWorkList.isEmpty()) {
                VarNode src = (VarNode) this.varNodeWorkList.iterator().next();
                this.varNodeWorkList.remove(src);
                handleVarNode(src);
                if (verbose) {
                    iter++;
                    if (iter >= 1000) {
                        iter = 0;
                        logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
                    }
                }
            }
            if (verbose) {
                logger.debug("Now handling field references");
            }
            for (Object object2 : this.pag.storeSources()) {
                VarNode src2 = (VarNode) object2;
                Node[] storeTargets = this.pag.storeLookup(src2);
                for (Node element0 : storeTargets) {
                    FieldRefNode fr = (FieldRefNode) element0;
                    fr.makeP2Set().addAll(src2.getP2Set(), null);
                }
            }
            for (Object object3 : this.pag.loadSources()) {
                FieldRefNode src3 = (FieldRefNode) object3;
                if (src3 != src3.getReplacement()) {
                    throw new RuntimeException("shouldn't happen");
                }
                Node[] targets = this.pag.loadLookup(src3);
                for (Node element02 : targets) {
                    VarNode target = (VarNode) element02;
                    if (target.makeP2Set().addAll(src3.getP2Set(), null)) {
                        this.varNodeWorkList.add(target);
                    }
                }
            }
        } while (!this.varNodeWorkList.isEmpty());
    }

    protected boolean handleAllocNode(AllocNode src) {
        boolean ret = false;
        Node[] targets = this.pag.allocLookup(src);
        for (Node element : targets) {
            if (element.makeP2Set().add(src)) {
                this.varNodeWorkList.add(element);
                ret = true;
            }
        }
        return ret;
    }

    protected boolean handleVarNode(VarNode src) {
        boolean ret = false;
        if (src.getReplacement() != src) {
            return false;
        }
        PointsToSetInternal newP2Set = src.getP2Set();
        if (newP2Set.isEmpty()) {
            return false;
        }
        Node[] simpleTargets = this.pag.simpleLookup(src);
        for (Node element : simpleTargets) {
            if (element.makeP2Set().addAll(newP2Set, null)) {
                this.varNodeWorkList.add(element);
                ret = true;
            }
        }
        Node[] storeTargets = this.pag.storeLookup(src);
        for (Node element2 : storeTargets) {
            if (((FieldRefNode) element2).makeP2Set().addAll(newP2Set, null)) {
                ret = true;
            }
        }
        for (final FieldRefNode fr : src.getAllFieldRefs()) {
            final SparkField field = fr.getField();
            ret = newP2Set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropMerge.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    AllocDotField nDotF = PropMerge.this.pag.makeAllocDotField((AllocNode) n, field);
                    Node nDotFNode = nDotF.getReplacement();
                    if (nDotFNode != fr) {
                        fr.mergeWith(nDotFNode);
                        this.returnValue = true;
                    }
                }
            }) | ret;
        }
        return ret;
    }
}
