package soot.jimple.spark.geom.geomE;

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
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomE/FullSensitiveNode.class */
public class FullSensitiveNode extends IVarAbstraction {
    public Map<FullSensitiveNode, GeometricManager> flowto;
    public Map<AllocNode, GeometricManager> pt_objs;
    public Map<AllocNode, GeometricManager> new_pts;
    public Vector<PlainConstraint> complex_cons;
    public static String[] symbols = {"/", "[]"};

    static {
        stubManager = new GeometricManager();
        pres = new RectangleNode(1L, 1L, Constants.MAX_CONTEXTS, Constants.MAX_CONTEXTS);
        stubManager.addNewFigure(1, pres);
        deadManager = new GeometricManager();
    }

    public FullSensitiveNode(Node thisVar) {
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
        this.flowto = new HashMap();
        this.pt_objs = new HashMap();
        this.new_pts = new HashMap();
        this.complex_cons = null;
        this.lrf_value = 0;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void keepPointsToOnly() {
        this.flowto = null;
        this.new_pts = null;
        this.complex_cons = null;
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
                                this.pt_objs.put(obj, (GeometricManager) deadManager);
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
        if (this.new_pts.size() > 0) {
            for (GeometricManager gm : this.new_pts.values()) {
                gm.flush();
            }
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
        pres.I1 = I1;
        pres.I2 = I2;
        pres.L = L;
        return addPointsTo(0, obj);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_points_to_4(AllocNode obj, long I1, long I2, long L1, long L2) {
        pres.I1 = I1;
        pres.I2 = I2;
        pres.L = L1;
        pres.L_prime = L2;
        return addPointsTo(1, obj);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_simple_constraint_3(IVarAbstraction qv, long I1, long I2, long L) {
        pres.I1 = I1;
        pres.I2 = I2;
        pres.L = L;
        return addFlowsTo(0, qv);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_simple_constraint_4(IVarAbstraction qv, long I1, long I2, long L1, long L2) {
        pres.I1 = I1;
        pres.I2 = I2;
        pres.L = L1;
        pres.L_prime = L2;
        return addFlowsTo(1, qv);
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
        for (GeometricManager gm : this.pt_objs.values()) {
            gm.removeUselessSegments();
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void propagate(GeomPointsTo ptAnalyzer, IWorklist worklist) {
        if (this.pt_objs.size() == 0) {
            return;
        }
        if (this.complex_cons != null) {
            for (Map.Entry<AllocNode, GeometricManager> entry : this.new_pts.entrySet()) {
                AllocNode obj = entry.getKey();
                SegmentNode[] entry_pts = entry.getValue().getFigures();
                Iterator<PlainConstraint> it = this.complex_cons.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    PlainConstraint pcons = it.next();
                    FullSensitiveNode objn = (FullSensitiveNode) ptAnalyzer.findInstanceField(obj, pcons.f);
                    if (objn == null) {
                        this.pt_objs.put(obj, (GeometricManager) deadManager);
                        entry.setValue((GeometricManager) deadManager);
                    } else if (objn.willUpdate) {
                        FullSensitiveNode qn = (FullSensitiveNode) pcons.otherSide;
                        for (int i = 0; i < 2; i++) {
                            SegmentNode segmentNode = entry_pts[i];
                            while (true) {
                                SegmentNode pts = segmentNode;
                                if (pts != null && pts.is_new) {
                                    switch (pcons.type) {
                                        case 2:
                                            if (instantiateLoadConstraint(objn, qn, pts, (pcons.code << 8) | i)) {
                                                worklist.push(objn);
                                                break;
                                            } else {
                                                break;
                                            }
                                        case 3:
                                            if (instantiateStoreConstraint(qn, objn, pts, (pcons.code << 8) | i)) {
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
        if (this.flowto.size() == 0) {
            return;
        }
        for (Map.Entry<FullSensitiveNode, GeometricManager> entry1 : this.flowto.entrySet()) {
            boolean added = false;
            FullSensitiveNode qn2 = entry1.getKey();
            GeometricManager gm1 = entry1.getValue();
            SegmentNode[] entry_pe = gm1.getFigures();
            if (gm1.isThereUnprocessedFigures()) {
                for (Map.Entry<AllocNode, GeometricManager> entry2 : this.pt_objs.entrySet()) {
                    AllocNode obj2 = entry2.getKey();
                    GeometricManager gm2 = entry2.getValue();
                    if (gm2 != deadManager && ptAnalyzer.castNeverFails(obj2.getType(), qn2.getType())) {
                        SegmentNode[] entry_pts2 = gm2.getFigures();
                        boolean hasNewPointsTo = gm2.isThereUnprocessedFigures();
                        for (int j = 0; j < 2; j++) {
                            SegmentNode segmentNode2 = entry_pe[j];
                            while (true) {
                                SegmentNode pe = segmentNode2;
                                if (pe != null && (pe.is_new || hasNewPointsTo)) {
                                    for (int i2 = 0; i2 < 2; i2++) {
                                        SegmentNode segmentNode3 = entry_pts2[i2];
                                        while (true) {
                                            SegmentNode pts2 = segmentNode3;
                                            if (pts2 != null && (pts2.is_new || pe.is_new)) {
                                                if (reasonAndPropagate(qn2, obj2, pts2, pe, (i2 << 8) | j)) {
                                                    added = true;
                                                }
                                                segmentNode3 = pts2.next;
                                            }
                                        }
                                    }
                                    segmentNode2 = pe.next;
                                }
                            }
                        }
                    }
                }
                gm1.flush();
            } else {
                for (Map.Entry<AllocNode, GeometricManager> entry22 : this.new_pts.entrySet()) {
                    AllocNode obj3 = entry22.getKey();
                    GeometricManager gm22 = entry22.getValue();
                    if (gm22 != deadManager && ptAnalyzer.castNeverFails(obj3.getType(), qn2.getType())) {
                        SegmentNode[] entry_pts3 = gm22.getFigures();
                        for (int i3 = 0; i3 < 2; i3++) {
                            SegmentNode segmentNode4 = entry_pts3[i3];
                            while (true) {
                                SegmentNode pts3 = segmentNode4;
                                if (pts3 != null && pts3.is_new) {
                                    for (int j2 = 0; j2 < 2; j2++) {
                                        SegmentNode segmentNode5 = entry_pe[j2];
                                        while (true) {
                                            SegmentNode pe2 = segmentNode5;
                                            if (pe2 == null) {
                                                break;
                                            }
                                            if (reasonAndPropagate(qn2, obj3, pts3, pe2, (i3 << 8) | j2)) {
                                                added = true;
                                            }
                                            segmentNode5 = pe2.next;
                                        }
                                    }
                                    segmentNode4 = pts3.next;
                                }
                            }
                        }
                    }
                }
            }
            if (added) {
                worklist.push(qn2);
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean isDeadObject(AllocNode obj) {
        return this.pt_objs.get(obj) == deadManager;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_pts_intervals(AllocNode obj) {
        int ret = 0;
        SegmentNode[] int_entry = find_points_to(obj);
        for (int j = 0; j < 2; j++) {
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
        SegmentNode[] int_entry = find_flowto((FullSensitiveNode) qv);
        for (int j = 0; j < 2; j++) {
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

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00a8, code lost:
        r6 = r6 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00bc, code lost:
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
            soot.jimple.spark.geom.geomE.FullSensitiveNode r0 = (soot.jimple.spark.geom.geomE.FullSensitiveNode) r0
            r7 = r0
            r0 = r3
            soot.SootMethod r0 = r0.enclosingMethod()
            r1 = r4
            soot.SootMethod r1 = r1.enclosingMethod()
            if (r0 != r1) goto L15
            r0 = 1
            goto L16
        L15:
            r0 = 0
        L16:
            r12 = r0
            r0 = r3
            java.util.Map<soot.jimple.spark.pag.AllocNode, soot.jimple.spark.geom.geomE.GeometricManager> r0 = r0.pt_objs
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
            r13 = r0
            goto Lc4
        L2b:
            r0 = r13
            java.lang.Object r0 = r0.next()
            soot.jimple.spark.pag.AllocNode r0 = (soot.jimple.spark.pag.AllocNode) r0
            r14 = r0
            r0 = r14
            boolean r0 = r0 instanceof soot.jimple.spark.pag.ClassConstantNode
            if (r0 != 0) goto Lc4
            r0 = r14
            boolean r0 = r0 instanceof soot.jimple.spark.pag.StringConstantNode
            if (r0 == 0) goto L4a
            goto Lc4
        L4a:
            r0 = r7
            r1 = r14
            soot.jimple.spark.geom.dataRep.SegmentNode[] r0 = r0.find_points_to(r1)
            r11 = r0
            r0 = r11
            if (r0 != 0) goto L5b
            goto Lc4
        L5b:
            r0 = r3
            r1 = r14
            soot.jimple.spark.geom.dataRep.SegmentNode[] r0 = r0.find_points_to(r1)
            r10 = r0
            r0 = 0
            r5 = r0
            goto Lbf
        L68:
            r0 = r10
            r1 = r5
            r0 = r0[r1]
            r8 = r0
            goto Lb7
        L71:
            r0 = 0
            r6 = r0
            goto Lab
        L76:
            r0 = r11
            r1 = r6
            r0 = r0[r1]
            r9 = r0
            goto La3
        L7f:
            r0 = r12
            if (r0 == 0) goto L90
            r0 = r8
            r1 = r9
            boolean r0 = r0.intersect(r1)
            if (r0 == 0) goto L9c
            r0 = 1
            return r0
        L90:
            r0 = r8
            r1 = r9
            boolean r0 = r0.projYIntersect(r1)
            if (r0 == 0) goto L9c
            r0 = 1
            return r0
        L9c:
            r0 = r9
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r9 = r0
        La3:
            r0 = r9
            if (r0 != 0) goto L7f
            int r6 = r6 + 1
        Lab:
            r0 = r6
            r1 = 2
            if (r0 < r1) goto L76
            r0 = r8
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r8 = r0
        Lb7:
            r0 = r8
            if (r0 != 0) goto L71
            int r5 = r5 + 1
        Lbf:
            r0 = r5
            r1 = 2
            if (r0 < r1) goto L68
        Lc4:
            r0 = r13
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L2b
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.geomE.FullSensitiveNode.heap_sensitive_intersection(soot.jimple.spark.geom.geomPA.IVarAbstraction):boolean");
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
            for (int j = 0; j < 2; j++) {
                SegmentNode segmentNode = int_entry[j];
                while (true) {
                    SegmentNode p = segmentNode;
                    if (p == null) {
                        break;
                    }
                    outPrintStream.print("(" + obj.toString() + ", " + p.I1 + ", " + p.I2 + ", " + p.L + ", ");
                    if (p instanceof RectangleNode) {
                        outPrintStream.print(String.valueOf(((RectangleNode) p).L_prime) + ", ");
                    }
                    outPrintStream.println(String.valueOf(symbols[j]) + ")");
                    segmentNode = p.next;
                }
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void injectPts() {
        final GeomPointsTo geomPTA = (GeomPointsTo) Scene.v().getPointsToAnalysis();
        this.pt_objs = new HashMap();
        this.me.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomE.FullSensitiveNode.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                if (geomPTA.isValidGeometricNode(n)) {
                    FullSensitiveNode.this.pt_objs.put((AllocNode) n, (GeometricManager) FullSensitiveNode.stubManager);
                }
            }
        });
        this.new_pts = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0058, code lost:
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
            r0 = 0
            r12 = r0
            goto L5b
        Le:
            r0 = r11
            r1 = r12
            r0 = r0[r1]
            r13 = r0
            goto L53
        L18:
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
            if (r0 > 0) goto L39
            r0 = r13
            long r0 = r0.I1
            r1 = r8
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L4a
        L39:
            r0 = r13
            long r0 = r0.I1
            r1 = r6
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L4c
            r0 = r6
            r1 = r14
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L4c
        L4a:
            r0 = 1
            return r0
        L4c:
            r0 = r13
            soot.jimple.spark.geom.dataRep.SegmentNode r0 = r0.next
            r13 = r0
        L53:
            r0 = r13
            if (r0 != 0) goto L18
            int r12 = r12 + 1
        L5b:
            r0 = r12
            r1 = 2
            if (r0 < r1) goto Le
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.spark.geom.geomE.FullSensitiveNode.pointer_interval_points_to(long, long, soot.jimple.spark.pag.AllocNode):boolean");
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void remove_points_to(AllocNode obj) {
        this.pt_objs.remove(obj);
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void get_all_context_sensitive_objects(long l, long r, PtSensVisitor visitor) {
        if (this.parent != this) {
            getRepresentative().get_all_context_sensitive_objects(l, r, visitor);
            return;
        }
        GeomPointsTo geomPTA = (GeomPointsTo) Scene.v().getPointsToAnalysis();
        for (Map.Entry<AllocNode, GeometricManager> entry : this.pt_objs.entrySet()) {
            AllocNode obj = entry.getKey();
            SootMethod sm = obj.getMethod();
            int sm_int = geomPTA.getIDFromSootMethod(sm);
            if (sm_int != -1) {
                GeometricManager gm = entry.getValue();
                SegmentNode[] int_entry = gm.getFigures();
                for (int i = 0; i < 2; i++) {
                    SegmentNode segmentNode = int_entry[i];
                    while (true) {
                        SegmentNode p = segmentNode;
                        if (p == null) {
                            break;
                        }
                        long L = p.I1;
                        long R = L + p.L;
                        long objL = -1;
                        long objR = -1;
                        if (l > L || L >= r) {
                            if (L <= l && l < R) {
                                if (i == 0) {
                                    long d = R - l;
                                    if (R > r) {
                                        d = r - l;
                                    }
                                    objL = (p.I2 + l) - L;
                                    objR = objL + d;
                                } else {
                                    objL = p.I2;
                                    objR = p.I2 + ((RectangleNode) p).L_prime;
                                }
                            }
                        } else if (i == 0) {
                            long d2 = r - L;
                            if (R < r) {
                                d2 = p.L;
                            }
                            objL = p.I2;
                            objR = objL + d2;
                        } else {
                            objL = p.I2;
                            objR = p.I2 + ((RectangleNode) p).L_prime;
                        }
                        if (objL != -1 && objR != -1) {
                            visitor.visit(obj, objL, objR, sm_int);
                        }
                        segmentNode = p.next;
                    }
                }
            }
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_new_pts_intervals() {
        int ans = 0;
        for (GeometricManager gm : this.new_pts.values()) {
            SegmentNode[] int_entry = gm.getFigures();
            for (int i = 0; i < 2; i++) {
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

    private boolean addPointsTo(int code, AllocNode obj) {
        GeometricManager gm = this.pt_objs.get(obj);
        if (gm == null) {
            gm = new GeometricManager();
            this.pt_objs.put(obj, gm);
        } else if (gm == deadManager) {
            return false;
        }
        SegmentNode p = gm.addNewFigure(code, pres);
        if (p != null) {
            this.new_pts.put(obj, gm);
            return true;
        }
        return false;
    }

    private boolean addFlowsTo(int code, IVarAbstraction qv) {
        GeometricManager gm = this.flowto.get(qv);
        if (gm == null) {
            gm = new GeometricManager();
            this.flowto.put((FullSensitiveNode) qv, gm);
        }
        if (gm.addNewFigure(code, pres) != null) {
            return true;
        }
        return false;
    }

    private void do_pts_interval_merge() {
        for (GeometricManager gm : this.new_pts.values()) {
            gm.mergeFigures(Parameters.max_pts_budget);
        }
    }

    private void do_flow_edge_interval_merge() {
        for (GeometricManager gm : this.flowto.values()) {
            gm.mergeFigures(Parameters.max_cons_budget);
        }
    }

    private SegmentNode[] find_flowto(FullSensitiveNode qv) {
        GeometricManager im = this.flowto.get(qv);
        if (im == null) {
            return null;
        }
        return im.getFigures();
    }

    private SegmentNode[] find_points_to(AllocNode obj) {
        GeometricManager im = this.pt_objs.get(obj);
        if (im == null) {
            return null;
        }
        return im.getFigures();
    }

    private static int infer_pts_is_one_to_one(SegmentNode pts, SegmentNode pe, int code) {
        long interI = pe.I1 < pts.I1 ? pts.I1 : pe.I1;
        long interJ = pe.I1 + pe.L < pts.I1 + pts.L ? pe.I1 + pe.L : pts.I1 + pts.L;
        if (interI < interJ) {
            switch (code) {
                case 0:
                    pres.I1 = (interI - pe.I1) + pe.I2;
                    pres.I2 = (interI - pts.I1) + pts.I2;
                    pres.L = interJ - interI;
                    return 0;
                case 1:
                    pres.I1 = pe.I2;
                    pres.I2 = (interI - pts.I1) + pts.I2;
                    pres.L = ((RectangleNode) pe).L_prime;
                    pres.L_prime = interJ - interI;
                    return 1;
                default:
                    return -1;
            }
        }
        return -1;
    }

    private static int infer_pts_is_many_to_many(RectangleNode pts, SegmentNode pe, int code) {
        long interI = pe.I1 < pts.I1 ? pts.I1 : pe.I1;
        long interJ = pe.I1 + pe.L < pts.I1 + pts.L ? pe.I1 + pe.L : pts.I1 + pts.L;
        if (interI < interJ) {
            switch (code) {
                case 0:
                    pres.I1 = (interI - pe.I1) + pe.I2;
                    pres.I2 = pts.I2;
                    pres.L = interJ - interI;
                    pres.L_prime = pts.L_prime;
                    return 1;
                case 1:
                    pres.I1 = pe.I2;
                    pres.I2 = pts.I2;
                    pres.L = ((RectangleNode) pe).L_prime;
                    pres.L_prime = pts.L_prime;
                    return 1;
                default:
                    return 1;
            }
        }
        return -1;
    }

    private static boolean reasonAndPropagate(FullSensitiveNode qn, AllocNode obj, SegmentNode pts, SegmentNode pe, int code) {
        int ret_type = -1;
        switch (code >> 8) {
            case 0:
                ret_type = infer_pts_is_one_to_one(pts, pe, code & 255);
                break;
            case 1:
                ret_type = infer_pts_is_many_to_many((RectangleNode) pts, pe, code & 255);
                break;
        }
        if (ret_type != -1) {
            return qn.addPointsTo(ret_type, obj);
        }
        return false;
    }

    private static boolean instantiateLoadConstraint(FullSensitiveNode objn, FullSensitiveNode qn, SegmentNode pts, int code) {
        int ret_type = -1;
        if ((code >> 8) == 0) {
            pres.I1 = pts.I2;
            pres.I2 = pts.I1;
            switch (code & 255) {
                case 0:
                    pres.L = pts.L;
                    ret_type = 0;
                    break;
                case 1:
                    pres.L = ((RectangleNode) pts).L_prime;
                    pres.L_prime = pts.L;
                    ret_type = 1;
                    break;
            }
        } else {
            pres.I1 = pts.I2;
            pres.I2 = 1L;
            pres.L_prime = 1L;
            switch (code & 255) {
                case 0:
                    pres.L = pts.L;
                    ret_type = 1;
                    break;
                case 1:
                    pres.L = ((RectangleNode) pts).L_prime;
                    ret_type = 1;
                    break;
            }
        }
        return objn.addFlowsTo(ret_type, qn);
    }

    private static boolean instantiateStoreConstraint(FullSensitiveNode qn, FullSensitiveNode objn, SegmentNode pts, int code) {
        int ret_type = -1;
        if ((code >> 8) == 0) {
            pres.I1 = pts.I1;
            pres.I2 = pts.I2;
            pres.L = pts.L;
            switch (code & 255) {
                case 0:
                    ret_type = 0;
                    break;
                case 1:
                    pres.L_prime = ((RectangleNode) pts).L_prime;
                    ret_type = 1;
                    break;
            }
        } else {
            pres.I1 = 1L;
            pres.I2 = pts.I2;
            pres.L = 1L;
            switch (code & 255) {
                case 0:
                    pres.L_prime = pts.L;
                    ret_type = 1;
                    break;
                case 1:
                    pres.L_prime = ((RectangleNode) pts).L_prime;
                    ret_type = 1;
                    break;
            }
        }
        return qn.addFlowsTo(ret_type, objn);
    }
}
