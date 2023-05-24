package soot.jimple.spark.solver;

import java.util.Iterator;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.NewInstanceNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/PropIter.class */
public class PropIter extends Propagator {
    private static final Logger logger = LoggerFactory.getLogger(PropIter.class);
    protected PAG pag;

    public PropIter(PAG pag) {
        this.pag = pag;
    }

    @Override // soot.jimple.spark.solver.Propagator
    public void propagate() {
        boolean change;
        OnFlyCallGraph ofcg = this.pag.getOnFlyCallGraph();
        new TopoSorter(this.pag, false).sort();
        for (Object object : this.pag.allocSources()) {
            handleAllocNode((AllocNode) object);
        }
        int iteration = 1;
        do {
            change = false;
            TreeSet<VarNode> simpleSources = new TreeSet<>(this.pag.simpleSources());
            if (this.pag.getOpts().verbose()) {
                int i = iteration;
                iteration++;
                logger.debug("Iteration " + i);
            }
            Iterator<VarNode> it = simpleSources.iterator();
            while (it.hasNext()) {
                VarNode object2 = it.next();
                change = handleSimples(object2) | change;
            }
            if (ofcg != null) {
                QueueReader<Node> addedEdges = this.pag.edgeReader();
                Iterator<VarNode> it2 = this.pag.getVarNodeNumberer().iterator();
                while (it2.hasNext()) {
                    VarNode src = it2.next();
                    ofcg.updatedNode(src);
                }
                ofcg.build();
                while (addedEdges.hasNext()) {
                    Node addedSrc = addedEdges.next();
                    Node addedTgt = addedEdges.next();
                    change = true;
                    if (addedSrc instanceof VarNode) {
                        PointsToSetInternal p2set = ((VarNode) addedSrc).getP2Set();
                        if (p2set != null) {
                            p2set.unFlushNew();
                        }
                    } else if (addedSrc instanceof AllocNode) {
                        ((VarNode) addedTgt).makeP2Set().add(addedSrc);
                    }
                }
                if (change) {
                    new TopoSorter(this.pag, false).sort();
                }
            }
            for (FieldRefNode object3 : this.pag.loadSources()) {
                change = handleLoads(object3) | change;
            }
            for (VarNode object4 : this.pag.storeSources()) {
                change = handleStores(object4) | change;
            }
            for (NewInstanceNode object5 : this.pag.assignInstanceSources()) {
                change = handleNewInstances(object5) | change;
            }
        } while (change);
    }

    protected boolean handleAllocNode(AllocNode src) {
        boolean ret = false;
        Node[] targets = this.pag.allocLookup(src);
        for (Node element : targets) {
            ret = element.makeP2Set().add(src) | ret;
        }
        return ret;
    }

    protected boolean handleSimples(VarNode src) {
        boolean ret = false;
        PointsToSetInternal srcSet = src.getP2Set();
        if (srcSet.isEmpty()) {
            return false;
        }
        Node[] simpleTargets = this.pag.simpleLookup(src);
        for (Node element : simpleTargets) {
            ret = element.makeP2Set().addAll(srcSet, null) | ret;
        }
        Node[] newInstances = this.pag.newInstanceLookup(src);
        for (Node element2 : newInstances) {
            ret = element2.makeP2Set().addAll(srcSet, null) | ret;
        }
        return ret;
    }

    protected boolean handleStores(VarNode src) {
        boolean ret = false;
        final PointsToSetInternal srcSet = src.getP2Set();
        if (srcSet.isEmpty()) {
            return false;
        }
        Node[] storeTargets = this.pag.storeLookup(src);
        for (Node element : storeTargets) {
            FieldRefNode fr = (FieldRefNode) element;
            final SparkField f = fr.getField();
            ret = fr.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropIter.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    AllocDotField nDotF = PropIter.this.pag.makeAllocDotField((AllocNode) n, f);
                    if (nDotF.makeP2Set().addAll(srcSet, null)) {
                        this.returnValue = true;
                    }
                }
            }) | ret;
        }
        return ret;
    }

    protected boolean handleLoads(FieldRefNode src) {
        final Node[] loadTargets = this.pag.loadLookup(src);
        final SparkField f = src.getField();
        boolean ret = src.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropIter.2
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
                    if (target.makeP2Set().addAll(set, null)) {
                        this.returnValue = true;
                    }
                }
            }
        }) | false;
        return ret;
    }

    protected boolean handleNewInstances(final NewInstanceNode src) {
        boolean ret = false;
        Node[] newInstances = this.pag.assignInstanceLookup(src);
        for (final Node instance : newInstances) {
            ret = src.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropIter.3
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    if (n instanceof ClassConstantNode) {
                        ClassConstantNode ccn = (ClassConstantNode) n;
                        Type ccnType = ccn.getClassConstant().toSootType();
                        SootClass targetClass = ((RefType) ccnType).getSootClass();
                        if (targetClass.resolvingLevel() == 0) {
                            Scene.v().forceResolve(targetClass.getName(), 2);
                        }
                        instance.makeP2Set().add(PropIter.this.pag.makeAllocNode(src.getValue(), ccnType, ccn.getMethod()));
                    }
                }
            });
        }
        return ret;
    }
}
