package soot.jimple.spark.solver;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/PropWorklist.class */
public class PropWorklist extends Propagator {
    private static final Logger logger = LoggerFactory.getLogger(PropWorklist.class);
    protected final Set<VarNode> varNodeWorkList = new TreeSet();
    protected PAG pag;
    protected OnFlyCallGraph ofcg;

    public PropWorklist(PAG pag) {
        this.pag = pag;
    }

    @Override // soot.jimple.spark.solver.Propagator
    public void propagate() {
        this.ofcg = this.pag.getOnFlyCallGraph();
        new TopoSorter(this.pag, false).sort();
        for (AllocNode object : this.pag.allocSources()) {
            handleAllocNode(object);
        }
        boolean verbose = this.pag.getOpts().verbose();
        do {
            if (verbose) {
                logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
            }
            while (!this.varNodeWorkList.isEmpty()) {
                VarNode src = this.varNodeWorkList.iterator().next();
                this.varNodeWorkList.remove(src);
                handleVarNode(src);
            }
            if (verbose) {
                logger.debug("Now handling field references");
            }
            for (final VarNode src2 : this.pag.storeSources()) {
                Node[] targets = this.pag.storeLookup(src2);
                for (Node element0 : targets) {
                    final FieldRefNode target = (FieldRefNode) element0;
                    target.getBase().makeP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropWorklist.1
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public final void visit(Node n) {
                            AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode) n, target.getField());
                            if (PropWorklist.this.ofcg != null) {
                                PropWorklist.this.ofcg.updatedFieldRef(nDotF, src2.getP2Set());
                            }
                            nDotF.makeP2Set().addAll(src2.getP2Set(), null);
                        }
                    });
                }
            }
            HashSet<Object[]> edgesToPropagate = new HashSet<>();
            for (FieldRefNode object2 : this.pag.loadSources()) {
                handleFieldRefNode(object2, edgesToPropagate);
            }
            Set<PointsToSetInternal> nodesToFlush = Collections.newSetFromMap(new IdentityHashMap());
            Iterator<Object[]> it = edgesToPropagate.iterator();
            while (it.hasNext()) {
                Object[] pair = it.next();
                PointsToSetInternal nDotF = (PointsToSetInternal) pair[0];
                PointsToSetInternal newP2Set = nDotF.getNewSet();
                VarNode loadTarget = (VarNode) pair[1];
                if (loadTarget.makeP2Set().addAll(newP2Set, null)) {
                    this.varNodeWorkList.add(loadTarget);
                }
                nodesToFlush.add(nDotF);
            }
            for (PointsToSetInternal nDotF2 : nodesToFlush) {
                nDotF2.flushNew();
            }
        } while (!this.varNodeWorkList.isEmpty());
    }

    protected boolean handleAllocNode(AllocNode src) {
        boolean ret = false;
        Node[] targets = this.pag.allocLookup(src);
        for (Node element : targets) {
            if (element.makeP2Set().add(src)) {
                this.varNodeWorkList.add((VarNode) element);
                ret = true;
            }
        }
        return ret;
    }

    protected boolean handleVarNode(VarNode src) {
        Node[] assignInstanceLookup;
        boolean ret = false;
        boolean flush = true;
        if (src.getReplacement() != src) {
            throw new RuntimeException("Got bad node " + src + " with rep " + src.getReplacement());
        }
        final PointsToSetInternal newP2Set = src.getP2Set().getNewSet();
        if (newP2Set.isEmpty()) {
            return false;
        }
        if (this.ofcg != null) {
            QueueReader<Node> addedEdges = this.pag.edgeReader();
            this.ofcg.updatedNode(src);
            this.ofcg.build();
            while (addedEdges.hasNext()) {
                Node addedSrc = addedEdges.next();
                Node addedTgt = addedEdges.next();
                ret = true;
                if (addedSrc instanceof VarNode) {
                    VarNode edgeSrc = (VarNode) addedSrc.getReplacement();
                    if (addedTgt instanceof VarNode) {
                        VarNode edgeTgt = (VarNode) addedTgt.getReplacement();
                        if (edgeTgt.makeP2Set().addAll(edgeSrc.getP2Set(), null)) {
                            this.varNodeWorkList.add(edgeTgt);
                            if (edgeTgt == src) {
                                flush = false;
                            }
                        }
                    } else if (addedTgt instanceof NewInstanceNode) {
                        NewInstanceNode edgeTgt2 = (NewInstanceNode) addedTgt.getReplacement();
                        if (edgeTgt2.makeP2Set().addAll(edgeSrc.getP2Set(), null)) {
                            for (Node element : this.pag.assignInstanceLookup(edgeTgt2)) {
                                this.varNodeWorkList.add((VarNode) element);
                                if (element == src) {
                                    flush = false;
                                }
                            }
                        }
                    }
                } else if (addedSrc instanceof AllocNode) {
                    VarNode edgeTgt3 = (VarNode) addedTgt.getReplacement();
                    if (edgeTgt3.makeP2Set().add(addedSrc)) {
                        this.varNodeWorkList.add(edgeTgt3);
                        if (edgeTgt3 == src) {
                            flush = false;
                        }
                    }
                } else if ((addedSrc instanceof NewInstanceNode) && (addedTgt instanceof VarNode)) {
                    final NewInstanceNode edgeSrc2 = (NewInstanceNode) addedSrc.getReplacement();
                    final VarNode edgeTgt4 = (VarNode) addedTgt.getReplacement();
                    addedSrc.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropWorklist.2
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public void visit(Node n) {
                            if (n instanceof ClassConstantNode) {
                                ClassConstantNode ccn = (ClassConstantNode) n;
                                Type ccnType = ccn.getClassConstant().toSootType();
                                SootClass targetClass = ((RefType) ccnType).getSootClass();
                                if (targetClass.resolvingLevel() == 0) {
                                    Scene.v().forceResolve(targetClass.getName(), 2);
                                }
                                edgeTgt4.makeP2Set().add(PropWorklist.this.pag.makeAllocNode(edgeSrc2.getValue(), ccnType, ccn.getMethod()));
                                PropWorklist.this.varNodeWorkList.add(edgeTgt4);
                            }
                        }
                    });
                    if (edgeTgt4.makeP2Set().add(addedSrc) && edgeTgt4 == src) {
                        flush = false;
                    }
                }
            }
        }
        Node[] simpleTargets = this.pag.simpleLookup(src);
        for (Node element2 : simpleTargets) {
            if (element2.makeP2Set().addAll(newP2Set, null)) {
                this.varNodeWorkList.add((VarNode) element2);
                if (element2 == src) {
                    flush = false;
                }
                ret = true;
            }
        }
        Node[] storeTargets = this.pag.storeLookup(src);
        for (Node element3 : storeTargets) {
            FieldRefNode fr = (FieldRefNode) element3;
            final SparkField f = fr.getField();
            ret = fr.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropWorklist.3
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode) n, f);
                    if (nDotF.makeP2Set().addAll(newP2Set, null)) {
                        this.returnValue = true;
                    }
                }
            }) | ret;
        }
        final HashSet<Node[]> storesToPropagate = new HashSet<>();
        final HashSet<Node[]> loadsToPropagate = new HashSet<>();
        for (FieldRefNode fr2 : src.getAllFieldRefs()) {
            final SparkField field = fr2.getField();
            final Node[] storeSources = this.pag.storeInvLookup(fr2);
            if (storeSources.length > 0) {
                newP2Set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropWorklist.4
                    @Override // soot.jimple.spark.sets.P2SetVisitor
                    public final void visit(Node n) {
                        Node[] nodeArr;
                        AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode) n, field);
                        for (Node element4 : storeSources) {
                            Node[] pair = {element4, nDotF.getReplacement()};
                            storesToPropagate.add(pair);
                        }
                    }
                });
            }
            final Node[] loadTargets = this.pag.loadLookup(fr2);
            if (loadTargets.length > 0) {
                newP2Set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropWorklist.5
                    @Override // soot.jimple.spark.sets.P2SetVisitor
                    public final void visit(Node n) {
                        Node[] nodeArr;
                        AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode) n, field);
                        if (nDotF != null) {
                            for (Node element4 : loadTargets) {
                                Node[] pair = {nDotF.getReplacement(), element4};
                                loadsToPropagate.add(pair);
                            }
                        }
                    }
                });
            }
        }
        if (flush) {
            src.getP2Set().flushNew();
        }
        Iterator<Node[]> it = storesToPropagate.iterator();
        while (it.hasNext()) {
            Node[] p = it.next();
            VarNode storeSource = (VarNode) p[0];
            AllocDotField nDotF = (AllocDotField) p[1];
            if (nDotF.makeP2Set().addAll(storeSource.getP2Set(), null)) {
                ret = true;
            }
        }
        Iterator<Node[]> it2 = loadsToPropagate.iterator();
        while (it2.hasNext()) {
            Node[] p2 = it2.next();
            AllocDotField nDotF2 = (AllocDotField) p2[0];
            VarNode loadTarget = (VarNode) p2[1];
            if (loadTarget.makeP2Set().addAll(nDotF2.getP2Set(), null)) {
                this.varNodeWorkList.add(loadTarget);
                ret = true;
            }
        }
        return ret;
    }

    protected final void handleFieldRefNode(FieldRefNode src, final HashSet<Object[]> edgesToPropagate) {
        final Node[] loadTargets = this.pag.loadLookup(src);
        if (loadTargets.length == 0) {
            return;
        }
        final SparkField field = src.getField();
        src.getBase().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropWorklist.6
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                Node[] nodeArr;
                AllocDotField nDotF = PropWorklist.this.pag.makeAllocDotField((AllocNode) n, field);
                if (nDotF != null) {
                    PointsToSetInternal p2Set = nDotF.getP2Set();
                    if (!p2Set.getNewSet().isEmpty()) {
                        for (Node element : loadTargets) {
                            Object[] pair = {p2Set, element};
                            edgesToPropagate.add(pair);
                        }
                    }
                }
            }
        });
    }
}
