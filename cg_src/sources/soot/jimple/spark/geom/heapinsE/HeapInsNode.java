package soot.jimple.spark.geom.heapinsE;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import soot.Hierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;
import soot.jimple.spark.geom.geomPA.Constants;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.geom.geomPA.IWorklist;
import soot.jimple.spark.geom.geomPA.Parameters;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.sets.P2SetVisitor;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/heapinsE/HeapInsNode.class */
public class HeapInsNode extends IVarAbstraction {
    public HashMap<HeapInsNode, HeapInsIntervalManager> flowto;
    public HashMap<AllocNode, HeapInsIntervalManager> pt_objs;
    public Map<AllocNode, HeapInsIntervalManager> new_pts;
    public Vector<PlainConstraint> complex_cons = null;

    static {
        stubManager = new HeapInsIntervalManager();
        pres = new RectangleNode(0L, 0L, Constants.MAX_CONTEXTS, Constants.MAX_CONTEXTS);
        stubManager.addNewFigure(-1, pres);
        deadManager = new HeapInsIntervalManager();
    }

    public HeapInsNode(Node thisVar) {
        this.me = thisVar;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void deleteAll() {
        this.flowto = null;
        this.pt_objs = null;
        this.new_pts = null;
        this.complex_cons = null;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void reconstruct() {
        this.flowto = new HashMap<>();
        this.pt_objs = new HashMap<>();
        this.new_pts = new HashMap();
        this.complex_cons = null;
        this.lrf_value = 0;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void do_before_propagation() {
        SootClass sc;
        do_pts_interval_merge();
        do_flow_edge_interval_merge();
        Node wrappedNode = getWrappedNode();
        if ((wrappedNode instanceof LocalVarNode) && ((LocalVarNode) wrappedNode).isThisPtr()) {
            SootMethod func = ((LocalVarNode) wrappedNode).getMethod();
            if (!func.isConstructor()) {
                SootClass defClass = func.getDeclaringClass();
                Hierarchy typeHierarchy = Scene.v().getActiveHierarchy();
                Iterator<AllocNode> it = this.new_pts.keySet().iterator();
                while (it.hasNext()) {
                    AllocNode obj = it.next();
                    if ((obj.getType() instanceof RefType) && defClass != (sc = ((RefType) obj.getType()).getSootClass())) {
                        try {
                            SootMethod rt_func = typeHierarchy.resolveConcreteDispatch(sc, func);
                            if (rt_func != func) {
                                it.remove();
                                this.pt_objs.put(obj, (HeapInsIntervalManager) deadManager);
                            }
                        } catch (RuntimeException e) {
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void do_after_propagation() {
        for (HeapInsIntervalManager im : this.new_pts.values()) {
            im.flush();
        }
        this.new_pts = new HashMap();
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int num_of_diff_objs() {
        if (this.parent != this) {
            return getRepresentative().num_of_diff_objs();
        }
        if (this.pt_objs == null) {
            return -1;
        }
        return this.pt_objs.size();
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int num_of_diff_edges() {
        if (this.parent != this) {
            return getRepresentative().num_of_diff_objs();
        }
        if (this.flowto == null) {
            return -1;
        }
        return this.flowto.size();
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_points_to_3(AllocNode obj, long I1, long I2, long L) {
        int code;
        pres.I1 = I1;
        pres.I2 = I2;
        pres.L = L;
        if (I1 == 0) {
            code = I2 == 0 ? -1 : 0;
        } else {
            code = I2 == 0 ? 1 : 2;
        }
        return addPointsTo(code, obj);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_points_to_4(AllocNode obj, long I1, long I2, long L1, long L2) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_simple_constraint_3(IVarAbstraction qv, long I1, long I2, long L) {
        int code;
        pres.I1 = I1;
        pres.I2 = I2;
        pres.L = L;
        if (I1 == 0) {
            code = I2 == 0 ? -1 : 0;
        } else {
            code = I2 == 0 ? 1 : 2;
        }
        return addFlowsTo(code, (HeapInsNode) qv);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_simple_constraint_4(IVarAbstraction qv, long I1, long I2, long L1, long L2) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void put_complex_constraint(PlainConstraint cons) {
        if (this.complex_cons == null) {
            this.complex_cons = new Vector<>();
        }
        this.complex_cons.add(cons);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void drop_duplicates() {
        for (HeapInsIntervalManager im : this.pt_objs.values()) {
            im.removeUselessSegments();
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void propagate(GeomPointsTo ptAnalyzer, IWorklist worklist) {
        if (this.complex_cons != null) {
            for (Map.Entry<AllocNode, HeapInsIntervalManager> entry : this.new_pts.entrySet()) {
                AllocNode obj = entry.getKey();
                SegmentNode[] int_entry1 = entry.getValue().getFigures();
                Iterator<PlainConstraint> it = this.complex_cons.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    PlainConstraint pcons = it.next();
                    HeapInsNode objn = (HeapInsNode) ptAnalyzer.findAndInsertInstanceField(obj, pcons.f);
                    if (objn == null) {
                        this.pt_objs.put(obj, (HeapInsIntervalManager) deadManager);
                        entry.setValue((HeapInsIntervalManager) deadManager);
                    } else if (objn.willUpdate) {
                        HeapInsNode qn = (HeapInsNode) pcons.otherSide;
                        for (int i = 0; i < HeapInsIntervalManager.Divisions; i++) {
                            SegmentNode segmentNode = int_entry1[i];
                            while (true) {
                                SegmentNode pts = segmentNode;
                                if (pts != null && pts.is_new) {
                                    switch (pcons.type) {
                                        case 2:
                                            if (objn.add_simple_constraint_3(qn, pts.I2, pcons.code == 0 ? pts.I1 : 0L, pts.L < 0 ? -pts.L : pts.L)) {
                                                worklist.push(objn);
                                                break;
                                            } else {
                                                break;
                                            }
                                        case 3:
                                            if (qn.add_simple_constraint_3(objn, pcons.code == 0 ? pts.I1 : 0L, pts.I2, pts.L < 0 ? -pts.L : pts.L)) {
                                                worklist.push(qn);
                                                break;
                                            } else {
                                                break;
                                            }
                                    }
                                    segmentNode = pts.next;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Map.Entry<HeapInsNode, HeapInsIntervalManager> entry1 : this.flowto.entrySet()) {
            boolean added = false;
            HeapInsNode qn2 = entry1.getKey();
            HeapInsIntervalManager him1 = entry1.getValue();
            SegmentNode[] int_entry12 = him1.getFigures();
            boolean has_new_edges = him1.isThereUnprocessedFigures();
            Map<AllocNode, HeapInsIntervalManager> objs = has_new_edges ? this.pt_objs : this.new_pts;
            for (Map.Entry<AllocNode, HeapInsIntervalManager> entry2 : objs.entrySet()) {
                AllocNode obj2 = entry2.getKey();
                HeapInsIntervalManager him2 = entry2.getValue();
                if (him2 != deadManager && ptAnalyzer.castNeverFails(obj2.getType(), qn2.getWrappedNode().getType())) {
                    SegmentNode[] int_entry2 = him2.getFigures();
                    for (int i2 = 0; i2 < HeapInsIntervalManager.Divisions; i2++) {
                        SegmentNode segmentNode2 = int_entry2[i2];
                        while (true) {
                            SegmentNode pts2 = segmentNode2;
                            if (pts2 != null && (has_new_edges || pts2.is_new)) {
                                for (int j = 0; j < HeapInsIntervalManager.Divisions; j++) {
                                    SegmentNode segmentNode3 = int_entry12[j];
                                    while (true) {
                                        SegmentNode pe = segmentNode3;
                                        if (pe != null && (pts2.is_new || pe.is_new)) {
                                            if (add_new_points_to_tuple(pts2, pe, obj2, qn2)) {
                                                added = true;
                                            }
                                            segmentNode3 = pe.next;
                                        }
                                    }
                                }
                                segmentNode2 = pts2.next;
                            }
                        }
                    }
                }
            }
            if (added) {
                worklist.push(qn2);
            }
            if (has_new_edges) {
                him1.flush();
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_pts_intervals(AllocNode obj) {
        int ret = 0;
        SegmentNode[] int_entry = find_points_to(obj);
        for (int j = 0; j < HeapInsIntervalManager.Divisions; j++) {
            SegmentNode segmentNode = int_entry[j];
            while (true) {
                SegmentNode p = segmentNode;
                if (p == null) {
                    break;
                }
                ret++;
                segmentNode = p.next;
            }
        }
        return ret;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_flow_intervals(IVarAbstraction qv) {
        int ret = 0;
        SegmentNode[] int_entry = find_flowto((HeapInsNode) qv);
        for (int j = 0; j < HeapInsIntervalManager.Divisions; j++) {
            SegmentNode segmentNode = int_entry[j];
            while (true) {
                SegmentNode p = segmentNode;
                if (p == null) {
                    break;
                }
                ret++;
                segmentNode = p.next;
            }
        }
        return ret;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0083, code lost:
        r6 = r6 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0099, code lost:
        r5 = r5 + 1;
     */
    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean heap_sensitive_intersection(soot.jimple.spark.geom.geomPA.IVarAbstraction r4) {
        /*
            r3 = this;
            r0 = r4
            soot.jimple.spark.geom.heapinsE.HeapInsNode r0 = (soot.jimple.spark.geom.heapinsE.HeapInsNode) r0
            r7 = r0
            r0 = r3
            java.util.HashMap<soot.jimple.spark.pag.AllocNode, soot.jimple.spark.geom.heapinsE.HeapInsIntervalManager> r0 = r0.pt_objs
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
            r12 = r0
            goto La3
        L17:
            r0 = r12
            java.lang.Object r0 = r0.next()
            soot.jimple.spark.pag.AllocNode r0 = (soot.jimple.spark.pag.AllocNode) r0
            r13 = r0
            r0 = r13
            boolean r0 = r0 instanceof soot.jimple.spark.pag.ClassConstantNode
            if (r0 != 0) goto La3
            r0 = r13
            boolean r0 = r0 instanceof soot.jimple.spark.pag.StringConstantNode
            if (r0 == 0) goto L36
            goto La3
        L36:
            r0 = r7
            r1 = r13
            soot.jimple.spark.geom.dataRep.SegmentNode[] r0 = r0.find_points_to(r1)
            r11 = r0
            r0 = r11
            if (r0 != 0) goto L47
            goto La3
        L47:
            r0 = r3
            r1 = r13
            soot.jimple.spark.geom.dataRep.SegmentNode[] r0 = r0.find_points_to(r1)
            r10 = r0
            r0 = 0
            r5 = r0
            goto L9c
        L54:
            r0 = r10
            r1 = r5
            r0 = r0[r1]
            r8 = r0
            goto L94
        L5d:
            r0 = 0
            r6 = r0
            goto L86
        L62:
            r0 = r11
            r1 = r6
            r0 = r0[r1]
            r9 = r0
            goto L7e
        L6b:
            r0 = r8
            r1 = r9
            boolean r0 = quick_intersecting_test(r0, r1)
            if (r0 == 0) goto L77
            r0 = 1
            return r0
        L77:
            r0 = r9
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r9 = r0
        L7e:
            r0 = r9
            if (r0 != 0) goto L6b
            int r6 = r6 + 1
        L86:
            r0 = r6
            int r1 = soot.jimple.spark.geom.heapinsE.HeapInsIntervalManager.Divisions
            if (r0 < r1) goto L62
            r0 = r8
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r8 = r0
        L94:
            r0 = r8
            if (r0 != 0) goto L5d
            int r5 = r5 + 1
        L9c:
            r0 = r5
            int r1 = soot.jimple.spark.geom.heapinsE.HeapInsIntervalManager.Divisions
            if (r0 < r1) goto L54
        La3:
            r0 = r12
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L17
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.heapinsE.HeapInsNode.heap_sensitive_intersection(soot.jimple.spark.geom.geomPA.IVarAbstraction):boolean");
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public Set<AllocNode> get_all_points_to_objects() {
        if (this.parent != this) {
            return getRepresentative().get_all_points_to_objects();
        }
        return this.pt_objs.keySet();
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void print_context_sensitive_points_to(PrintStream outPrintStream) {
        for (AllocNode obj : this.pt_objs.keySet()) {
            SegmentNode[] int_entry = find_points_to(obj);
            for (int j = 0; j < HeapInsIntervalManager.Divisions; j++) {
                SegmentNode segmentNode = int_entry[j];
                while (true) {
                    SegmentNode p = segmentNode;
                    if (p == null) {
                        break;
                    }
                    outPrintStream.println("(" + obj.toString() + ", " + p.I1 + ", " + p.I2 + ", " + p.L + ")");
                    segmentNode = p.next;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0068, code lost:
        r12 = r12 + 1;
     */
    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean pointer_interval_points_to(long r6, long r8, soot.jimple.spark.pag.AllocNode r10) {
        /*
            r5 = this;
            r0 = r5
            r1 = r10
            soot.jimple.spark.geom.dataRep.SegmentNode[] r0 = r0.find_points_to(r1)
            r11 = r0
            r0 = r11
            if (r0 != 0) goto Lf
            r0 = 0
            return r0
        Lf:
            r0 = r11
            r1 = 0
            r0 = r0[r1]
            if (r0 == 0) goto L18
            r0 = 1
            return r0
        L18:
            r0 = 1
            r12 = r0
            goto L6b
        L1e:
            r0 = r11
            r1 = r12
            r0 = r0[r1]
            r13 = r0
            goto L63
        L28:
            r0 = r13
            long r0 = r0.I1
            r1 = r13
            long r1 = r1.L
            long r0 = r0 + r1
            r14 = r0
            r0 = r6
            r1 = r13
            long r1 = r1.I1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L49
            r0 = r13
            long r0 = r0.I1
            r1 = r8
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L5a
        L49:
            r0 = r13
            long r0 = r0.I1
            r1 = r6
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L5c
            r0 = r6
            r1 = r14
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L5c
        L5a:
            r0 = 1
            return r0
        L5c:
            r0 = r13
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r13 = r0
        L63:
            r0 = r13
            if (r0 != 0) goto L28
            int r12 = r12 + 1
        L6b:
            r0 = r12
            int r1 = soot.jimple.spark.geom.heapinsE.HeapInsIntervalManager.Divisions
            if (r0 < r1) goto L1e
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.heapinsE.HeapInsNode.pointer_interval_points_to(long, long, soot.jimple.spark.pag.AllocNode):boolean");
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void remove_points_to(AllocNode obj) {
        this.pt_objs.remove(obj);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void keepPointsToOnly() {
        this.flowto = null;
        this.new_pts = null;
        this.complex_cons = null;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_new_pts_intervals() {
        int ans = 0;
        for (HeapInsIntervalManager im : this.new_pts.values()) {
            SegmentNode[] int_entry = im.getFigures();
            for (int i = 0; i < HeapInsIntervalManager.Divisions; i++) {
                SegmentNode segmentNode = int_entry[i];
                while (true) {
                    SegmentNode p = segmentNode;
                    if (p != null && p.is_new) {
                        ans++;
                        segmentNode = p.next;
                    }
                }
            }
        }
        return ans;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void get_all_context_sensitive_objects(long l, long r, PtSensVisitor visitor) {
        if (this.parent != this) {
            getRepresentative().get_all_context_sensitive_objects(l, r, visitor);
            return;
        }
        GeomPointsTo geomPTA = (GeomPointsTo) Scene.v().getPointsToAnalysis();
        for (Map.Entry<AllocNode, HeapInsIntervalManager> entry : this.pt_objs.entrySet()) {
            AllocNode obj = entry.getKey();
            HeapInsIntervalManager im = entry.getValue();
            SegmentNode[] int_entry = im.getFigures();
            SootMethod sm = obj.getMethod();
            int sm_int = 0;
            long n_contexts = 1;
            if (sm != null) {
                sm_int = geomPTA.getIDFromSootMethod(sm);
                n_contexts = geomPTA.context_size[sm_int];
            }
            for (int i = 0; i < HeapInsIntervalManager.Divisions; i++) {
                SegmentNode segmentNode = int_entry[i];
                while (true) {
                    SegmentNode p = segmentNode;
                    if (p == null) {
                        break;
                    }
                    long R = p.I1 + p.L;
                    long objL = -1;
                    long objR = -1;
                    if (i == 0) {
                        objL = p.I2;
                        objR = p.I2 + p.L;
                    } else if (l > p.I1 || p.I1 >= r) {
                        if (p.I1 <= l && l < R) {
                            if (i != 1) {
                                long d = R - l;
                                if (R > r) {
                                    d = r - l;
                                }
                                objL = (p.I2 + l) - p.I1;
                                objR = objL + d;
                            } else {
                                objL = 1;
                                objR = 1 + n_contexts;
                            }
                        }
                    } else if (i != 1) {
                        long d2 = r - p.I1;
                        if (d2 > p.L) {
                            d2 = p.L;
                        }
                        objL = p.I2;
                        objR = objL + d2;
                    } else {
                        objL = 1;
                        objR = 1 + n_contexts;
                    }
                    if (objL != -1 && objR != -1) {
                        visitor.visit(obj, objL, objR, sm_int);
                    }
                    segmentNode = p.next;
                }
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void injectPts() {
        final GeomPointsTo geomPTA = (GeomPointsTo) Scene.v().getPointsToAnalysis();
        this.pt_objs = new HashMap<>();
        this.me.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.heapinsE.HeapInsNode.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                if (geomPTA.isValidGeometricNode(n)) {
                    HeapInsNode.this.pt_objs.put((AllocNode) n, (HeapInsIntervalManager) HeapInsNode.stubManager);
                }
            }
        });
        this.new_pts = null;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean isDeadObject(AllocNode obj) {
        return this.pt_objs.get(obj) == deadManager;
    }

    private SegmentNode[] find_flowto(HeapInsNode qv) {
        HeapInsIntervalManager im = this.flowto.get(qv);
        if (im == null) {
            return null;
        }
        return im.getFigures();
    }

    private SegmentNode[] find_points_to(AllocNode obj) {
        HeapInsIntervalManager im = this.pt_objs.get(obj);
        if (im == null) {
            return null;
        }
        return im.getFigures();
    }

    private void do_pts_interval_merge() {
        for (HeapInsIntervalManager him : this.new_pts.values()) {
            him.mergeFigures(Parameters.max_pts_budget);
        }
    }

    private void do_flow_edge_interval_merge() {
        for (HeapInsIntervalManager him : this.flowto.values()) {
            him.mergeFigures(Parameters.max_cons_budget);
        }
    }

    private boolean addPointsTo(int code, AllocNode obj) {
        HeapInsIntervalManager im = this.pt_objs.get(obj);
        if (im == null) {
            im = new HeapInsIntervalManager();
            this.pt_objs.put(obj, im);
        } else if (im == deadManager) {
            return false;
        }
        if (im.addNewFigure(code, pres) != null) {
            this.new_pts.put(obj, im);
            return true;
        }
        return false;
    }

    private boolean addFlowsTo(int code, HeapInsNode qv) {
        HeapInsIntervalManager im = this.flowto.get(qv);
        if (im == null) {
            im = new HeapInsIntervalManager();
            this.flowto.put(qv, im);
        }
        return im.addNewFigure(code, pres) != null;
    }

    private static boolean add_new_points_to_tuple(SegmentNode pts, SegmentNode pe, AllocNode obj, HeapInsNode qn) {
        int code;
        if (pts.I1 == 0 || pe.I1 == 0) {
            if (pe.I2 != 0) {
                pres.I1 = pe.I2;
                pres.I2 = 0L;
                pres.L = pe.L;
                code = 1;
            } else {
                pres.I1 = 0L;
                pres.I2 = pts.I2;
                pres.L = pts.L;
                code = pts.I2 == 0 ? -1 : 0;
            }
        } else {
            long interI = pe.I1 < pts.I1 ? pts.I1 : pe.I1;
            long interJ = pe.I1 + pe.L < pts.I1 + pts.L ? pe.I1 + pe.L : pts.I1 + pts.L;
            if (interI >= interJ) {
                return false;
            }
            pres.I1 = pe.I2 == 0 ? 0L : (interI - pe.I1) + pe.I2;
            pres.I2 = pts.I2 == 0 ? 0L : (interI - pts.I1) + pts.I2;
            pres.L = interJ - interI;
            if (pres.I1 == 0) {
                code = pres.I2 == 0 ? -1 : 0;
            } else {
                code = pres.I2 == 0 ? 1 : 2;
            }
        }
        return qn.addPointsTo(code, obj);
    }

    private static boolean quick_intersecting_test(SegmentNode p, SegmentNode q) {
        if (p.I2 == 0 || q.I2 == 0) {
            return true;
        }
        if (p.I2 >= q.I2) {
            return p.I2 < q.I2 + ((q.L > 0L ? 1 : (q.L == 0L ? 0 : -1)) < 0 ? -q.L : q.L);
        }
        return q.I2 < p.I2 + ((p.L > 0L ? 1 : (p.L == 0L ? 0 : -1)) < 0 ? -p.L : p.L);
    }
}
