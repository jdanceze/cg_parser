package soot.jimple.spark.solver;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ClassConstantNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.NewInstanceNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.util.HashMultiMap;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/PropAlias.class */
public class PropAlias extends Propagator {
    private static final Logger logger = LoggerFactory.getLogger(PropAlias.class);
    protected Set<VarNode> aliasWorkList;
    protected PAG pag;
    protected LargeNumberedMap<FieldRefNode, PointsToSetInternal> loadSets;
    protected OnFlyCallGraph ofcg;
    protected final Set<VarNode> varNodeWorkList = new TreeSet();
    protected Set<FieldRefNode> fieldRefWorkList = new HashSet();
    protected Set<FieldRefNode> outFieldRefWorkList = new HashSet();
    protected MultiMap<SparkField, VarNode> fieldToBase = new HashMultiMap();
    protected MultiMap<FieldRefNode, FieldRefNode> aliasEdges = new HashMultiMap();

    public PropAlias(PAG pag) {
        this.pag = pag;
        this.loadSets = new LargeNumberedMap<>(pag.getFieldRefNodeNumberer());
    }

    @Override // soot.jimple.spark.solver.Propagator
    public void propagate() {
        this.ofcg = this.pag.getOnFlyCallGraph();
        new TopoSorter(this.pag, false).sort();
        for (Object object : this.pag.loadSources()) {
            FieldRefNode fr = (FieldRefNode) object;
            this.fieldToBase.put(fr.getField(), fr.getBase());
        }
        for (Object object2 : this.pag.storeInvSources()) {
            FieldRefNode fr2 = (FieldRefNode) object2;
            this.fieldToBase.put(fr2.getField(), fr2.getBase());
        }
        for (Object object3 : this.pag.allocSources()) {
            handleAllocNode((AllocNode) object3);
        }
        boolean verbose = this.pag.getOpts().verbose();
        do {
            if (verbose) {
                logger.debug("Worklist has " + this.varNodeWorkList.size() + " nodes.");
            }
            this.aliasWorkList = new HashSet();
            while (!this.varNodeWorkList.isEmpty()) {
                VarNode src = this.varNodeWorkList.iterator().next();
                this.varNodeWorkList.remove(src);
                this.aliasWorkList.add(src);
                handleVarNode(src);
            }
            if (verbose) {
                logger.debug("Now handling field references");
            }
            for (VarNode src2 : this.aliasWorkList) {
                for (FieldRefNode srcFr : src2.getAllFieldRefs()) {
                    SparkField field = srcFr.getField();
                    for (VarNode dst : this.fieldToBase.get(field)) {
                        if (src2.getP2Set().hasNonEmptyIntersection(dst.getP2Set())) {
                            FieldRefNode dstFr = dst.dot(field);
                            this.aliasEdges.put(srcFr, dstFr);
                            this.aliasEdges.put(dstFr, srcFr);
                            this.fieldRefWorkList.add(srcFr);
                            this.fieldRefWorkList.add(dstFr);
                            if (makeP2Set(dstFr).addAll(srcFr.getP2Set().getOldSet(), null)) {
                                this.outFieldRefWorkList.add(dstFr);
                            }
                            if (makeP2Set(srcFr).addAll(dstFr.getP2Set().getOldSet(), null)) {
                                this.outFieldRefWorkList.add(srcFr);
                            }
                        }
                    }
                }
            }
            for (FieldRefNode src3 : this.fieldRefWorkList) {
                for (FieldRefNode dst2 : this.aliasEdges.get(src3)) {
                    if (makeP2Set(dst2).addAll(src3.getP2Set().getNewSet(), null)) {
                        this.outFieldRefWorkList.add(dst2);
                    }
                }
                src3.getP2Set().flushNew();
            }
            this.fieldRefWorkList = new HashSet();
            for (FieldRefNode src4 : this.outFieldRefWorkList) {
                PointsToSetInternal set = getP2Set(src4).getNewSet();
                if (!set.isEmpty()) {
                    Node[] targets = this.pag.loadLookup(src4);
                    for (Node element0 : targets) {
                        VarNode target = (VarNode) element0;
                        if (target.makeP2Set().addAll(set, null)) {
                            addToWorklist(target);
                        }
                    }
                    getP2Set(src4).flushNew();
                }
            }
            this.outFieldRefWorkList = new HashSet();
        } while (!this.varNodeWorkList.isEmpty());
    }

    protected boolean handleAllocNode(AllocNode src) {
        boolean ret = false;
        Node[] targets = this.pag.allocLookup(src);
        for (Node element : targets) {
            if (element.makeP2Set().add(src)) {
                addToWorklist((VarNode) element);
                ret = true;
            }
        }
        return ret;
    }

    protected boolean handleVarNode(VarNode src) {
        boolean ret = false;
        if (src.getReplacement() != src) {
            throw new RuntimeException("Got bad node " + src + " with rep " + src.getReplacement());
        }
        PointsToSetInternal newP2Set = src.getP2Set().getNewSet();
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
                    VarNode edgeSrc = (VarNode) addedSrc;
                    if (addedTgt instanceof VarNode) {
                        VarNode edgeTgt = (VarNode) addedTgt;
                        if (edgeTgt.makeP2Set().addAll(edgeSrc.getP2Set(), null)) {
                            addToWorklist(edgeTgt);
                        }
                    } else if (addedTgt instanceof NewInstanceNode) {
                        NewInstanceNode edgeTgt2 = (NewInstanceNode) addedTgt.getReplacement();
                        if (edgeTgt2.makeP2Set().addAll(edgeSrc.getP2Set(), null)) {
                            for (Node element : this.pag.assignInstanceLookup(edgeTgt2)) {
                                addToWorklist((VarNode) element);
                            }
                        }
                    }
                } else if (addedSrc instanceof AllocNode) {
                    AllocNode edgeSrc2 = (AllocNode) addedSrc;
                    VarNode edgeTgt3 = (VarNode) addedTgt;
                    if (edgeTgt3.makeP2Set().add(edgeSrc2)) {
                        addToWorklist(edgeTgt3);
                    }
                } else if ((addedSrc instanceof NewInstanceNode) && (addedTgt instanceof VarNode)) {
                    final NewInstanceNode edgeSrc3 = (NewInstanceNode) addedSrc.getReplacement();
                    final VarNode edgeTgt4 = (VarNode) addedTgt.getReplacement();
                    addedSrc.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.PropAlias.1
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public void visit(Node n) {
                            if (n instanceof ClassConstantNode) {
                                ClassConstantNode ccn = (ClassConstantNode) n;
                                Type ccnType = ccn.getClassConstant().toSootType();
                                SootClass targetClass = ((RefType) ccnType).getSootClass();
                                if (targetClass.resolvingLevel() == 0) {
                                    Scene.v().forceResolve(targetClass.getName(), 2);
                                }
                                edgeTgt4.makeP2Set().add(PropAlias.this.pag.makeAllocNode(edgeSrc3.getValue(), ccnType, ccn.getMethod()));
                                PropAlias.this.addToWorklist(edgeTgt4);
                            }
                        }
                    });
                }
                FieldRefNode frn = null;
                if (addedSrc instanceof FieldRefNode) {
                    frn = (FieldRefNode) addedSrc;
                }
                if (addedTgt instanceof FieldRefNode) {
                    frn = (FieldRefNode) addedTgt;
                }
                if (frn != null) {
                    VarNode base = frn.getBase();
                    if (this.fieldToBase.put(frn.getField(), base)) {
                        this.aliasWorkList.add(base);
                    }
                }
            }
        }
        Node[] simpleTargets = this.pag.simpleLookup(src);
        for (Node element2 : simpleTargets) {
            if (element2.makeP2Set().addAll(newP2Set, null)) {
                addToWorklist((VarNode) element2);
                ret = true;
            }
        }
        Node[] storeTargets = this.pag.storeLookup(src);
        for (Node element3 : storeTargets) {
            FieldRefNode fr = (FieldRefNode) element3;
            if (fr.makeP2Set().addAll(newP2Set, null)) {
                this.fieldRefWorkList.add(fr);
                ret = true;
            }
        }
        src.getP2Set().flushNew();
        return ret;
    }

    protected final PointsToSetInternal makeP2Set(FieldRefNode n) {
        PointsToSetInternal ret = this.loadSets.get(n);
        if (ret == null) {
            ret = this.pag.getSetFactory().newSet(null, this.pag);
            this.loadSets.put(n, ret);
        }
        return ret;
    }

    protected final PointsToSetInternal getP2Set(FieldRefNode n) {
        PointsToSetInternal ret = this.loadSets.get(n);
        if (ret == null) {
            return EmptyPointsToSet.v();
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean addToWorklist(VarNode n) {
        if (n.getReplacement() != n) {
            throw new RuntimeException("Adding bad node " + n + " with rep " + n.getReplacement());
        }
        return this.varNodeWorkList.add(n);
    }
}
