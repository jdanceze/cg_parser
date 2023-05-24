package soot.jimple.spark.solver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/SCCCollapser.class */
public class SCCCollapser {
    private static final Logger logger = LoggerFactory.getLogger(SCCCollapser.class);
    protected PAG pag;
    protected boolean ignoreTypes;
    protected TypeManager typeManager;
    protected int numCollapsed = 0;
    protected HashSet<VarNode> visited = new HashSet<>();

    public void collapse() {
        boolean verbose = this.pag.getOpts().verbose();
        if (verbose) {
            logger.debug("Total VarNodes: " + this.pag.getVarNodeNumberer().size() + ". Collapsing SCCs...");
        }
        new TopoSorter(this.pag, this.ignoreTypes).sort();
        TreeSet<VarNode> s = new TreeSet<>();
        Iterator<VarNode> it = this.pag.getVarNodeNumberer().iterator();
        while (it.hasNext()) {
            s.add(it.next());
        }
        Iterator<VarNode> it2 = s.iterator();
        while (it2.hasNext()) {
            VarNode v = it2.next();
            dfsVisit(v, v);
        }
        if (verbose) {
            logger.debug(this.numCollapsed + " nodes were collapsed.");
        }
        this.visited = null;
    }

    public SCCCollapser(PAG pag, boolean ignoreTypes) {
        this.pag = pag;
        this.ignoreTypes = ignoreTypes;
        this.typeManager = pag.getTypeManager();
    }

    protected final void dfsVisit(VarNode v, VarNode rootOfSCC) {
        if (this.visited.contains(v)) {
            return;
        }
        this.visited.add(v);
        Node[] succs = this.pag.simpleInvLookup(v);
        for (Node element : succs) {
            if (this.ignoreTypes || this.typeManager.castNeverFails(element.getType(), v.getType())) {
                dfsVisit((VarNode) element, rootOfSCC);
            }
        }
        if (v != rootOfSCC) {
            if (!this.ignoreTypes) {
                if (this.typeManager.castNeverFails(v.getType(), rootOfSCC.getType()) && this.typeManager.castNeverFails(rootOfSCC.getType(), v.getType())) {
                    rootOfSCC.mergeWith(v);
                    this.numCollapsed++;
                    return;
                }
                return;
            }
            if (this.typeManager.castNeverFails(v.getType(), rootOfSCC.getType())) {
                rootOfSCC.mergeWith(v);
            } else if (this.typeManager.castNeverFails(rootOfSCC.getType(), v.getType())) {
                v.mergeWith(rootOfSCC);
            } else {
                rootOfSCC.getReplacement().setType(null);
                PointsToSetInternal set = rootOfSCC.getP2Set();
                if (set != null) {
                    set.setType(null);
                }
                rootOfSCC.mergeWith(v);
            }
            this.numCollapsed++;
        }
    }
}
