package soot.jimple.spark.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.util.LargeNumberedMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/PropCycle.class */
public class PropCycle extends Propagator {
    private static final Logger logger = LoggerFactory.getLogger(PropCycle.class);
    private PAG pag;
    private OnFlyCallGraph ofcg;
    private Integer currentIteration;
    private final LargeNumberedMap<VarNode, Integer> varNodeToIteration;

    public PropCycle(PAG pag) {
        this.pag = pag;
        this.varNodeToIteration = new LargeNumberedMap<>(pag.getVarNodeNumberer());
    }

    @Override // soot.jimple.spark.solver.Propagator
    public void propagate() {
        boolean changed;
        this.ofcg = this.pag.getOnFlyCallGraph();
        boolean verbose = this.pag.getOpts().verbose();
        Collection<VarNode> bases = new HashSet<>();
        Iterator<FieldRefNode> it = this.pag.getFieldRefNodeNumberer().iterator();
        while (it.hasNext()) {
            FieldRefNode frn = it.next();
            bases.add(frn.getBase());
        }
        Collection<VarNode> bases2 = new ArrayList<>(bases);
        int iteration = 0;
        boolean finalIter = false;
        do {
            changed = false;
            iteration++;
            this.currentIteration = new Integer(iteration);
            if (verbose) {
                logger.debug("Iteration: " + iteration);
            }
            for (VarNode v : bases2) {
                changed = computeP2Set((VarNode) v.getReplacement(), new ArrayList<>()) | changed;
            }
            if (this.ofcg != null) {
                throw new RuntimeException("NYI");
            }
            if (verbose) {
                logger.debug("Processing stores");
            }
            for (Object object : this.pag.storeSources()) {
                final VarNode src = (VarNode) object;
                Node[] targets = this.pag.storeLookup(src);
                for (Node element0 : targets) {
                    final FieldRefNode target = (FieldRefNode) element0;
                    changed = target.getBase().makeP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropCycle.1
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public final void visit(Node n) {
                            AllocDotField nDotF = PropCycle.this.pag.makeAllocDotField((AllocNode) n, target.getField());
                            nDotF.makeP2Set().addAll(src.getP2Set(), null);
                        }
                    }) | changed;
                }
            }
            if (!changed && !finalIter) {
                finalIter = true;
                if (verbose) {
                    logger.debug("Doing full graph");
                }
                bases2 = new ArrayList<>(this.pag.getVarNodeNumberer().size());
                Iterator<VarNode> it2 = this.pag.getVarNodeNumberer().iterator();
                while (it2.hasNext()) {
                    VarNode v2 = it2.next();
                    bases2.add(v2);
                }
                changed = true;
            }
        } while (changed);
    }

    private boolean computeP2Set(final VarNode v, ArrayList<VarNode> path) {
        boolean ret = false;
        if (path.contains(v)) {
            return false;
        }
        Integer vnIteration = this.varNodeToIteration.get(v);
        if (this.currentIteration != null && vnIteration != null && this.currentIteration.intValue() == vnIteration.intValue()) {
            return false;
        }
        this.varNodeToIteration.put(v, this.currentIteration);
        path.add(v);
        if (v.getP2Set().isEmpty()) {
            Node[] srcs = this.pag.allocInvLookup(v);
            for (Node element : srcs) {
                ret = v.makeP2Set().add(element) | ret;
            }
        }
        Node[] srcs2 = this.pag.simpleInvLookup(v);
        for (Node element2 : srcs2) {
            VarNode src = (VarNode) element2;
            ret = v.makeP2Set().addAll(src.getP2Set(), null) | computeP2Set(src, path) | ret;
        }
        Node[] srcs3 = this.pag.loadInvLookup(v);
        for (Node element3 : srcs3) {
            final FieldRefNode src2 = (FieldRefNode) element3;
            ret = src2.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropCycle.2
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    AllocNode an = (AllocNode) n;
                    AllocDotField adf = PropCycle.this.pag.makeAllocDotField(an, src2.getField());
                    this.returnValue = v.makeP2Set().addAll(adf.getP2Set(), null) | this.returnValue;
                }
            }) | ret;
        }
        path.remove(path.size() - 1);
        return ret;
    }
}
