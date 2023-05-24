package soot.jimple.spark.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/EBBCollapser.class */
public class EBBCollapser {
    private static final Logger logger = LoggerFactory.getLogger(EBBCollapser.class);
    protected int numCollapsed = 0;
    protected PAG pag;

    public void collapse() {
        boolean verbose = this.pag.getOpts().verbose();
        if (verbose) {
            logger.debug("Total VarNodes: " + this.pag.getVarNodeNumberer().size() + ". Collapsing EBBs...");
        }
        collapseAlloc();
        collapseLoad();
        collapseSimple();
        if (verbose) {
            logger.debug(this.numCollapsed + " nodes were collapsed.");
        }
    }

    public EBBCollapser(PAG pag) {
        this.pag = pag;
    }

    protected void collapseAlloc() {
        boolean ofcg = this.pag.getOnFlyCallGraph() != null;
        for (Object object : this.pag.allocSources()) {
            AllocNode n = (AllocNode) object;
            Node[] succs = this.pag.allocLookup(n);
            VarNode firstSucc = null;
            for (Node element0 : succs) {
                VarNode succ = (VarNode) element0;
                if (this.pag.allocInvLookup(succ).length <= 1 && this.pag.loadInvLookup(succ).length <= 0 && this.pag.simpleInvLookup(succ).length <= 0 && (!ofcg || !succ.isInterProcTarget())) {
                    if (firstSucc == null) {
                        firstSucc = succ;
                    } else if (firstSucc.getType().equals(succ.getType())) {
                        firstSucc.mergeWith(succ);
                        this.numCollapsed++;
                    }
                }
            }
        }
    }

    protected void collapseSimple() {
        boolean change;
        boolean ofcg = this.pag.getOnFlyCallGraph() != null;
        TypeManager typeManager = this.pag.getTypeManager();
        do {
            change = false;
            Iterator<Object> nIt = new ArrayList(this.pag.simpleSources()).iterator();
            while (nIt.hasNext()) {
                VarNode n = (VarNode) nIt.next();
                Type nType = n.getType();
                Node[] succs = this.pag.simpleLookup(n);
                for (Node element : succs) {
                    VarNode succ = (VarNode) element;
                    Type sType = succ.getType();
                    if (typeManager.castNeverFails(nType, sType) && this.pag.allocInvLookup(succ).length <= 0 && this.pag.loadInvLookup(succ).length <= 0 && this.pag.simpleInvLookup(succ).length <= 1 && (!ofcg || (!succ.isInterProcTarget() && !n.isInterProcSource()))) {
                        n.mergeWith(succ);
                        change = true;
                        this.numCollapsed++;
                    }
                }
            }
        } while (change);
    }

    protected void collapseLoad() {
        boolean ofcg = this.pag.getOnFlyCallGraph() != null;
        TypeManager typeManager = this.pag.getTypeManager();
        Iterator<Object> nIt = new ArrayList(this.pag.loadSources()).iterator();
        while (nIt.hasNext()) {
            FieldRefNode n = (FieldRefNode) nIt.next();
            Type nType = n.getType();
            Node[] succs = this.pag.loadLookup(n);
            Node firstSucc = null;
            HashMap<Type, VarNode> typeToSucc = new HashMap<>();
            for (Node element : succs) {
                VarNode succ = (VarNode) element;
                Type sType = succ.getType();
                if (this.pag.allocInvLookup(succ).length <= 0 && this.pag.loadInvLookup(succ).length <= 1 && this.pag.simpleInvLookup(succ).length <= 0 && (!ofcg || !succ.isInterProcTarget())) {
                    if (typeManager.castNeverFails(nType, sType)) {
                        if (firstSucc == null) {
                            firstSucc = succ;
                        } else {
                            firstSucc.mergeWith(succ);
                            this.numCollapsed++;
                        }
                    } else {
                        VarNode rep = typeToSucc.get(succ.getType());
                        if (rep == null) {
                            typeToSucc.put(succ.getType(), succ);
                        } else {
                            rep.mergeWith(succ);
                            this.numCollapsed++;
                        }
                    }
                }
            }
        }
    }
}
