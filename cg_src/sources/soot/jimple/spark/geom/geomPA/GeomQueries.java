package soot.jimple.spark.geom.geomPA;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import soot.Local;
import soot.PointsToSet;
import soot.SootMethod;
import soot.jimple.spark.geom.dataMgr.ContextsCollector;
import soot.jimple.spark.geom.dataMgr.Obj_full_extractor;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.SimpleInterval;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/GeomQueries.class */
public class GeomQueries {
    protected GeomPointsTo geomPTA;
    protected int n_func;
    protected CgEdge[] call_graph;
    protected int[] vis_cg;
    protected int[] rep_cg;
    protected int[] scc_size;
    protected int[] block_num;
    protected long[] max_context_size_block;
    protected int[] top_rank;
    private boolean prop_initialized = false;
    private Queue<Integer> topQ;
    private int[] in_degree;
    private ContextsCollector[] contextsForMethods;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GeomQueries.class.desiredAssertionStatus();
    }

    public GeomQueries(GeomPointsTo geom_pta) {
        this.geomPTA = null;
        this.geomPTA = geom_pta;
        this.n_func = this.geomPTA.n_func;
        this.vis_cg = this.geomPTA.vis_cg;
        this.rep_cg = this.geomPTA.rep_cg;
        this.scc_size = this.geomPTA.scc_size;
        this.block_num = this.geomPTA.block_num;
        this.max_context_size_block = this.geomPTA.max_context_size_block;
        this.call_graph = new CgEdge[this.n_func];
        Arrays.fill(this.call_graph, (Object) null);
        this.in_degree = new int[this.n_func];
        Arrays.fill(this.in_degree, 0);
        CgEdge[] raw_call_graph = this.geomPTA.call_graph;
        for (int i = 0; i < this.n_func; i++) {
            if (this.vis_cg[i] != 0) {
                int rep = this.rep_cg[i];
                for (CgEdge p = raw_call_graph[i]; p != null; p = p.next) {
                    if (!p.scc_edge) {
                        CgEdge q = p.duplicate();
                        q.next = this.call_graph[rep];
                        this.call_graph[rep] = q;
                        int[] iArr = this.in_degree;
                        int i2 = this.rep_cg[q.t];
                        iArr[i2] = iArr[i2] + 1;
                    }
                }
            }
        }
    }

    private void prepareIntervalPropagations() {
        if (this.prop_initialized) {
            return;
        }
        this.top_rank = new int[this.n_func];
        Arrays.fill(this.top_rank, 0);
        this.topQ = new LinkedList();
        this.topQ.add(0);
        while (!this.topQ.isEmpty()) {
            int s = this.topQ.poll().intValue();
            CgEdge cgEdge = this.call_graph[s];
            while (true) {
                CgEdge p = cgEdge;
                if (p == null) {
                    break;
                }
                int t = p.t;
                int rep_t = this.rep_cg[t];
                int w = this.top_rank[s] + 1;
                if (this.top_rank[rep_t] < w) {
                    this.top_rank[rep_t] = w;
                }
                int[] iArr = this.in_degree;
                int i = iArr[rep_t] - 1;
                iArr[rep_t] = i;
                if (i == 0) {
                    this.topQ.add(Integer.valueOf(rep_t));
                }
                cgEdge = p.next;
            }
        }
        this.contextsForMethods = new ContextsCollector[this.n_func];
        for (int i2 = 0; i2 < this.n_func; i2++) {
            ContextsCollector cc = new ContextsCollector();
            cc.setBudget(Parameters.qryBudgetSize);
            this.contextsForMethods[i2] = cc;
        }
        this.prop_initialized = true;
    }

    protected boolean dfsScanSubgraph(int s, int target) {
        int rep_s = this.rep_cg[s];
        int rep_target = this.rep_cg[target];
        if (rep_s == rep_target) {
            return true;
        }
        boolean reachable = false;
        CgEdge cgEdge = this.call_graph[rep_s];
        while (true) {
            CgEdge p = cgEdge;
            if (p != null) {
                int t = p.t;
                int rep_t = this.rep_cg[t];
                if (this.in_degree[rep_t] != 0 || (this.top_rank[rep_t] <= this.top_rank[rep_target] && dfsScanSubgraph(t, target))) {
                    int[] iArr = this.in_degree;
                    iArr[rep_t] = iArr[rep_t] + 1;
                    reachable = true;
                }
                cgEdge = p.next;
            } else {
                return reachable;
            }
        }
    }

    protected void transferInSCC(int s, int t, long L, long R, ContextsCollector tContexts) {
        if (s == t && this.scc_size[s] == 1) {
            tContexts.insert(L, R);
            return;
        }
        int n_blocks = this.block_num[t];
        long block_size = this.max_context_size_block[this.rep_cg[s]];
        long offset = (L - 1) % block_size;
        long ctxtLength = R - L;
        long block_offset = 0;
        for (int i = 0; i < n_blocks; i++) {
            long lEnd = 1 + offset + block_offset;
            long rEnd = lEnd + ctxtLength;
            tContexts.insert(lEnd, rEnd);
            block_offset += block_size;
        }
    }

    protected boolean propagateIntervals(int start, long L, long R, int target) {
        if (!dfsScanSubgraph(start, target)) {
            return false;
        }
        int rep_start = this.rep_cg[start];
        int rep_target = this.rep_cg[target];
        ContextsCollector targetContexts = this.contextsForMethods[target];
        if (rep_start == rep_target) {
            transferInSCC(start, target, L, R, targetContexts);
            return true;
        }
        transferInSCC(start, rep_start, L, R, this.contextsForMethods[rep_start]);
        this.topQ.clear();
        this.topQ.add(Integer.valueOf(rep_start));
        while (!this.topQ.isEmpty()) {
            int s = this.topQ.poll().intValue();
            ContextsCollector sContexts = this.contextsForMethods[s];
            CgEdge cgEdge = this.call_graph[s];
            while (true) {
                CgEdge p = cgEdge;
                if (p == null) {
                    break;
                }
                int t = p.t;
                int rep_t = this.rep_cg[t];
                if (this.in_degree[rep_t] != 0) {
                    ContextsCollector reptContexts = this.contextsForMethods[rep_t];
                    long block_size = this.max_context_size_block[s];
                    for (SimpleInterval si : sContexts.bars) {
                        long in_block_offset = (si.L - 1) % block_size;
                        long newL = p.map_offset + in_block_offset;
                        long newR = (si.R - si.L) + newL;
                        if (rep_t == rep_target) {
                            transferInSCC(t, target, newL, newR, targetContexts);
                        } else {
                            transferInSCC(t, rep_t, newL, newR, reptContexts);
                        }
                    }
                    int[] iArr = this.in_degree;
                    int i = iArr[rep_t] - 1;
                    iArr[rep_t] = i;
                    if (i == 0 && rep_t != rep_target) {
                        this.topQ.add(Integer.valueOf(rep_t));
                    }
                }
                cgEdge = p.next;
            }
            sContexts.clear();
        }
        return true;
    }

    public boolean contextsGoBy(Edge sootEdge, Local l, PtSensVisitor visitor) {
        LocalVarNode vn;
        IVarAbstraction pn;
        CgEdge ctxt = this.geomPTA.getInternalEdgeFromSootEdge(sootEdge);
        if (ctxt == null || ctxt.is_obsoleted || (vn = this.geomPTA.findLocalVarNode(l)) == null || (pn = this.geomPTA.findInternalNode(vn)) == null) {
            return false;
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        if (!pn2.hasPTResult()) {
            return false;
        }
        SootMethod sm = vn.getMethod();
        int target = this.geomPTA.getIDFromSootMethod(sm);
        if (target == -1) {
            return false;
        }
        long L = ctxt.map_offset;
        long R = L + this.max_context_size_block[this.rep_cg[ctxt.s]];
        if ($assertionsDisabled || L < R) {
            visitor.prepare();
            prepareIntervalPropagations();
            if (propagateIntervals(ctxt.t, L, R, target)) {
                ContextsCollector targetContexts = this.contextsForMethods[target];
                for (SimpleInterval si : targetContexts.bars) {
                    if (!$assertionsDisabled && si.L >= si.R) {
                        throw new AssertionError();
                    }
                    pn2.get_all_context_sensitive_objects(si.L, si.R, visitor);
                }
                targetContexts.clear();
            }
            visitor.finish();
            return visitor.numOfDiffObjects() != 0;
        }
        throw new AssertionError();
    }

    @Deprecated
    public boolean contexsByAnyCallEdge(Edge sootEdge, Local l, PtSensVisitor visitor) {
        return contextsGoBy(sootEdge, l, visitor);
    }

    public boolean contextsGoBy(Edge sootEdge, Local l, SparkField field, PtSensVisitor visitor) {
        IVarAbstraction objField;
        Obj_full_extractor pts_l = new Obj_full_extractor();
        if (!contextsGoBy(sootEdge, l, pts_l)) {
            return false;
        }
        visitor.prepare();
        for (VarType icv : pts_l.outList) {
            AllocNode obj = (AllocNode) icv.var;
            AllocDotField obj_f = this.geomPTA.findAllocDotField(obj, field);
            if (obj_f != null && (objField = this.geomPTA.findInternalNode(obj_f)) != null) {
                long L = icv.L;
                long R = icv.R;
                if (!$assertionsDisabled && L >= R) {
                    throw new AssertionError();
                }
                objField.get_all_context_sensitive_objects(L, R, visitor);
            }
        }
        visitor.finish();
        return visitor.numOfDiffObjects() != 0;
    }

    @Deprecated
    public boolean contextsByAnyCallEdge(Edge sootEdge, Local l, SparkField field, PtSensVisitor visitor) {
        return contextsGoBy(sootEdge, l, visitor);
    }

    public boolean kCFA(Edge[] callEdgeChain, Local l, PtSensVisitor visitor) {
        LocalVarNode vn;
        IVarAbstraction pn;
        SootMethod firstMethod = callEdgeChain[0].src();
        int firstMethodID = this.geomPTA.getIDFromSootMethod(firstMethod);
        if (firstMethodID == -1 || (vn = this.geomPTA.findLocalVarNode(l)) == null || (pn = this.geomPTA.findInternalNode(vn)) == null) {
            return false;
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        if (!pn2.hasPTResult()) {
            return false;
        }
        SootMethod sm = vn.getMethod();
        if (this.geomPTA.getIDFromSootMethod(sm) == -1) {
            return false;
        }
        visitor.prepare();
        long L = 1;
        for (Edge sootEdge : callEdgeChain) {
            CgEdge ctxt = this.geomPTA.getInternalEdgeFromSootEdge(sootEdge);
            if (ctxt == null || ctxt.is_obsoleted) {
                return false;
            }
            int caller = this.geomPTA.getIDFromSootMethod(sootEdge.src());
            long block_size = this.max_context_size_block[this.rep_cg[caller]];
            long in_block_offset = (L - 1) % block_size;
            L = ctxt.map_offset + in_block_offset;
        }
        long ctxtLength = this.max_context_size_block[this.rep_cg[firstMethodID]];
        long R = L + ctxtLength;
        pn2.get_all_context_sensitive_objects(L, R, visitor);
        visitor.finish();
        return visitor.numOfDiffObjects() != 0;
    }

    @Deprecated
    public boolean contextsByCallChain(Edge[] callEdgeChain, Local l, PtSensVisitor visitor) {
        return kCFA(callEdgeChain, l, visitor);
    }

    public boolean kCFA(Edge[] callEdgeChain, Local l, SparkField field, PtSensVisitor visitor) {
        IVarAbstraction objField;
        Obj_full_extractor pts_l = new Obj_full_extractor();
        if (!kCFA(callEdgeChain, l, pts_l)) {
            return false;
        }
        visitor.prepare();
        for (VarType icv : pts_l.outList) {
            AllocNode obj = (AllocNode) icv.var;
            AllocDotField obj_f = this.geomPTA.findAllocDotField(obj, field);
            if (obj_f != null && (objField = this.geomPTA.findInternalNode(obj_f)) != null) {
                long L = icv.L;
                long R = icv.R;
                if (!$assertionsDisabled && L >= R) {
                    throw new AssertionError();
                }
                objField.get_all_context_sensitive_objects(L, R, visitor);
            }
        }
        visitor.finish();
        return visitor.numOfDiffObjects() != 0;
    }

    @Deprecated
    public boolean contextsByCallChain(Edge[] callEdgeChain, Local l, SparkField field, PtSensVisitor visitor) {
        return kCFA(callEdgeChain, l, field, visitor);
    }

    public boolean isAliasCI(Local l1, Local l2) {
        PointsToSet pts1 = this.geomPTA.reachingObjects(l1);
        PointsToSet pts2 = this.geomPTA.reachingObjects(l2);
        return pts1.hasNonEmptyIntersection(pts2);
    }

    public boolean isAlias(IVarAbstraction pn1, IVarAbstraction pn2) {
        IVarAbstraction pn12 = pn1.getRepresentative();
        IVarAbstraction pn22 = pn2.getRepresentative();
        if (!pn12.hasPTResult() || !pn22.hasPTResult()) {
            VarNode vn1 = (VarNode) pn12.getWrappedNode();
            VarNode vn2 = (VarNode) pn22.getWrappedNode();
            return isAliasCI((Local) vn1.getVariable(), (Local) vn2.getVariable());
        }
        return pn12.heap_sensitive_intersection(pn22);
    }

    public boolean isAlias(Local l1, Local l2) {
        LocalVarNode vn1 = this.geomPTA.findLocalVarNode(l1);
        LocalVarNode vn2 = this.geomPTA.findLocalVarNode(l2);
        if (vn1 == null || vn2 == null) {
            return false;
        }
        IVarAbstraction pn1 = this.geomPTA.findInternalNode(vn1);
        IVarAbstraction pn2 = this.geomPTA.findInternalNode(vn2);
        if (pn1 == null || pn2 == null) {
            return isAliasCI(l1, l2);
        }
        IVarAbstraction pn12 = pn1.getRepresentative();
        IVarAbstraction pn22 = pn2.getRepresentative();
        if (!pn12.hasPTResult() || !pn22.hasPTResult()) {
            return isAliasCI(l1, l2);
        }
        return pn12.heap_sensitive_intersection(pn22);
    }
}
