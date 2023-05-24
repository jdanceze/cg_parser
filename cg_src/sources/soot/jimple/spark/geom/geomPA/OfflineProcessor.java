package soot.jimple.spark.geom.geomPA;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import soot.SootClass;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.utils.ZArrayNumberer;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/OfflineProcessor.class */
public class OfflineProcessor {
    private boolean visitedFlag;
    GeomPointsTo geomPTA;
    ZArrayNumberer<IVarAbstraction> int2var;
    ArrayList<off_graph_edge> varGraph;
    int[] pre;
    int[] low;
    int[] count;
    int[] rep;
    int[] repsize;
    Deque<Integer> queue;
    int pre_cnt;
    int n_var;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/OfflineProcessor$off_graph_edge.class */
    public class off_graph_edge {
        int s;
        int t;
        IVarAbstraction base_var;
        off_graph_edge next;

        off_graph_edge() {
        }
    }

    public OfflineProcessor(GeomPointsTo pta) {
        this.int2var = pta.pointers;
        int size = this.int2var.size();
        this.varGraph = new ArrayList<>(size);
        this.queue = new LinkedList();
        this.pre = new int[size];
        this.low = new int[size];
        this.count = new int[size];
        this.rep = new int[size];
        this.repsize = new int[size];
        this.geomPTA = pta;
        for (int i = 0; i < size; i++) {
            this.varGraph.add(null);
        }
    }

    public void init() {
        this.n_var = this.int2var.size();
        for (int i = 0; i < this.n_var; i++) {
            this.varGraph.set(i, null);
            this.int2var.get(i).willUpdate = false;
        }
    }

    public void defaultFeedPtsRoutines() {
        switch (Parameters.seedPts) {
            case Integer.MAX_VALUE:
                for (int i = 0; i < this.n_var; i++) {
                    IVarAbstraction pn = this.int2var.get(i);
                    if (pn != null && pn.getRepresentative() == pn) {
                        pn.willUpdate = true;
                    }
                }
                return;
            case 15:
                setAllUserCodeVariablesUseful();
                break;
        }
        Set<Node> multiBaseptrs = new HashSet<>();
        for (Stmt callsite : this.geomPTA.multiCallsites) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr) callsite.getInvokeExpr();
            VarNode vn = this.geomPTA.findLocalVarNode(iie.getBase());
            multiBaseptrs.add(vn);
        }
        addUserDefPts(multiBaseptrs);
    }

    public void addUserDefPts(Set<Node> initVars) {
        for (Node vn : initVars) {
            IVarAbstraction pn = this.geomPTA.findInternalNode(vn);
            if (pn != null) {
                IVarAbstraction pn2 = pn.getRepresentative();
                if (pn2.reachable()) {
                    pn2.willUpdate = true;
                }
            }
        }
    }

    public void releaseSparkMem() {
        for (int i = 0; i < this.n_var; i++) {
            IVarAbstraction pn = this.int2var.get(i);
            if (pn == pn.getRepresentative() && pn.willUpdate) {
                Node vn = pn.getWrappedNode();
                vn.discardP2Set();
            }
        }
        System.gc();
        System.gc();
        System.gc();
        System.gc();
    }

    public void runOptimizations() {
        buildDependenceGraph();
        distillConstraints();
        buildImpactGraph();
        computeWeightsForPts();
    }

    public void destroy() {
        this.pre = null;
        this.low = null;
        this.count = null;
        this.rep = null;
        this.repsize = null;
        this.varGraph = null;
        this.queue = null;
    }

    protected void buildDependenceGraph() {
        Iterator<PlainConstraint> it = this.geomPTA.constraints.iterator();
        while (it.hasNext()) {
            PlainConstraint cons = it.next();
            final IVarAbstraction lhs = cons.getLHS();
            final IVarAbstraction rhs = cons.getRHS();
            final SparkField field = cons.f;
            switch (cons.type) {
                case 1:
                    add_graph_edge(rhs.id, lhs.id);
                    break;
                case 2:
                    IVarAbstraction rep = lhs.getRepresentative();
                    if (!rep.hasPTResult()) {
                        lhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.OfflineProcessor.1
                            @Override // soot.jimple.spark.sets.P2SetVisitor
                            public void visit(Node n) {
                                IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode) n, field);
                                if (padf == null || !padf.reachable()) {
                                    return;
                                }
                                off_graph_edge e = OfflineProcessor.this.add_graph_edge(rhs.id, padf.id);
                                e.base_var = lhs;
                            }
                        });
                        break;
                    } else {
                        for (AllocNode o : rep.get_all_points_to_objects()) {
                            IVarAbstraction padf = this.geomPTA.findInstanceField(o, field);
                            if (padf != null && padf.reachable()) {
                                off_graph_edge e = add_graph_edge(rhs.id, padf.id);
                                e.base_var = lhs;
                            }
                        }
                        break;
                    }
                case 3:
                    IVarAbstraction rep2 = rhs.getRepresentative();
                    if (!rep2.hasPTResult()) {
                        rhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.OfflineProcessor.2
                            @Override // soot.jimple.spark.sets.P2SetVisitor
                            public void visit(Node n) {
                                IVarAbstraction padf2 = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode) n, field);
                                if (padf2 == null || !padf2.reachable()) {
                                    return;
                                }
                                off_graph_edge e2 = OfflineProcessor.this.add_graph_edge(padf2.id, lhs.id);
                                e2.base_var = rhs;
                            }
                        });
                        break;
                    } else {
                        for (AllocNode o2 : rep2.get_all_points_to_objects()) {
                            IVarAbstraction padf2 = this.geomPTA.findInstanceField(o2, field);
                            if (padf2 != null && padf2.reachable()) {
                                off_graph_edge e2 = add_graph_edge(padf2.id, lhs.id);
                                e2.base_var = rhs;
                            }
                        }
                        break;
                    }
            }
        }
    }

    protected void setAllUserCodeVariablesUseful() {
        SootClass sc;
        for (int i = 0; i < this.n_var; i++) {
            IVarAbstraction pn = this.int2var.get(i);
            if (pn == pn.getRepresentative()) {
                Node node = pn.getWrappedNode();
                int sm_id = this.geomPTA.getMethodIDFromPtr(pn);
                if (this.geomPTA.isReachableMethod(sm_id) && (node instanceof VarNode)) {
                    boolean defined_in_lib = false;
                    if (node instanceof LocalVarNode) {
                        defined_in_lib = ((LocalVarNode) node).getMethod().isJavaLibraryMethod();
                    } else if ((node instanceof GlobalVarNode) && (sc = ((GlobalVarNode) node).getDeclaringClass()) != null) {
                        defined_in_lib = sc.isJavaLibraryClass();
                    }
                    if (!defined_in_lib && !this.geomPTA.isExceptionPointer(node)) {
                        pn.willUpdate = true;
                    }
                }
            }
        }
    }

    protected void computeReachablePts() {
        this.queue.clear();
        for (int i = 0; i < this.n_var; i++) {
            if (this.int2var.get(i).willUpdate) {
                this.queue.add(Integer.valueOf(i));
            }
        }
        while (!this.queue.isEmpty()) {
            int i2 = this.queue.getFirst().intValue();
            this.queue.removeFirst();
            off_graph_edge off_graph_edgeVar = this.varGraph.get(i2);
            while (true) {
                off_graph_edge p = off_graph_edgeVar;
                if (p == null) {
                    break;
                }
                IVarAbstraction pn = this.int2var.get(p.t);
                if (!pn.willUpdate) {
                    pn.willUpdate = true;
                    this.queue.add(Integer.valueOf(p.t));
                }
                IVarAbstraction pn2 = p.base_var;
                if (pn2 != null && !pn2.willUpdate) {
                    pn2.willUpdate = true;
                    this.queue.add(Integer.valueOf(pn2.id));
                }
                off_graph_edgeVar = p.next;
            }
        }
    }

    protected void distillConstraints() {
        computeReachablePts();
        Iterator<PlainConstraint> it = this.geomPTA.constraints.iterator();
        while (it.hasNext()) {
            PlainConstraint cons = it.next();
            IVarAbstraction pn = cons.getRHS();
            final SparkField field = cons.f;
            this.visitedFlag = false;
            switch (cons.type) {
                case 0:
                case 1:
                case 2:
                    this.visitedFlag = pn.willUpdate;
                    break;
                case 3:
                    IVarAbstraction pn2 = pn.getRepresentative();
                    if (!pn2.hasPTResult()) {
                        pn2.getWrappedNode().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.OfflineProcessor.3
                            @Override // soot.jimple.spark.sets.P2SetVisitor
                            public void visit(Node n) {
                                IVarAbstraction padf;
                                if (OfflineProcessor.this.visitedFlag || (padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode) n, field)) == null || !padf.reachable()) {
                                    return;
                                }
                                OfflineProcessor.this.visitedFlag |= padf.willUpdate;
                            }
                        });
                        break;
                    } else {
                        for (AllocNode o : pn2.get_all_points_to_objects()) {
                            IVarAbstraction padf = this.geomPTA.findInstanceField(o, field);
                            if (padf != null && padf.reachable()) {
                                this.visitedFlag |= padf.willUpdate;
                                if (this.visitedFlag) {
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    break;
            }
            cons.isActive = this.visitedFlag;
        }
    }

    protected void buildImpactGraph() {
        for (int i = 0; i < this.n_var; i++) {
            this.varGraph.set(i, null);
        }
        this.queue.clear();
        Iterator<PlainConstraint> it = this.geomPTA.constraints.iterator();
        while (it.hasNext()) {
            PlainConstraint cons = it.next();
            if (cons.isActive) {
                final IVarAbstraction lhs = cons.getLHS();
                final IVarAbstraction rhs = cons.getRHS();
                final SparkField field = cons.f;
                switch (cons.type) {
                    case 0:
                        this.queue.add(Integer.valueOf(rhs.id));
                        continue;
                    case 1:
                        add_graph_edge(lhs.id, rhs.id);
                        continue;
                    case 2:
                        IVarAbstraction rep = lhs.getRepresentative();
                        if (!rep.hasPTResult()) {
                            lhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.OfflineProcessor.4
                                @Override // soot.jimple.spark.sets.P2SetVisitor
                                public void visit(Node n) {
                                    IVarAbstraction padf = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode) n, field);
                                    if (padf == null || !padf.reachable()) {
                                        return;
                                    }
                                    OfflineProcessor.this.add_graph_edge(padf.id, rhs.id);
                                }
                            });
                            break;
                        } else {
                            for (AllocNode o : rep.get_all_points_to_objects()) {
                                IVarAbstraction padf = this.geomPTA.findInstanceField(o, field);
                                if (padf != null && padf.reachable()) {
                                    add_graph_edge(padf.id, rhs.id);
                                }
                            }
                            continue;
                        }
                        break;
                    case 3:
                        IVarAbstraction rep2 = rhs.getRepresentative();
                        if (!rep2.hasPTResult()) {
                            rhs.getWrappedNode().getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.OfflineProcessor.5
                                @Override // soot.jimple.spark.sets.P2SetVisitor
                                public void visit(Node n) {
                                    IVarAbstraction padf2 = OfflineProcessor.this.geomPTA.findInstanceField((AllocNode) n, field);
                                    if (padf2 == null || !padf2.reachable()) {
                                        return;
                                    }
                                    OfflineProcessor.this.add_graph_edge(lhs.id, padf2.id);
                                }
                            });
                            break;
                        } else {
                            for (AllocNode o2 : rep2.get_all_points_to_objects()) {
                                IVarAbstraction padf2 = this.geomPTA.findInstanceField(o2, field);
                                if (padf2 != null && padf2.reachable()) {
                                    add_graph_edge(lhs.id, padf2.id);
                                }
                            }
                            continue;
                        }
                        break;
                }
            }
        }
    }

    protected void computeWeightsForPts() {
        this.pre_cnt = 0;
        for (int i = 0; i < this.n_var; i++) {
            this.pre[i] = -1;
            this.count[i] = 0;
            this.rep[i] = i;
            this.repsize[i] = 1;
            this.int2var.get(i).top_value = Integer.MIN_VALUE;
        }
        for (int i2 = 0; i2 < this.n_var; i2++) {
            if (this.pre[i2] == -1) {
                tarjan_scc(i2);
            }
        }
        for (int i3 = 0; i3 < this.n_var; i3++) {
            int s = find_parent(i3);
            for (off_graph_edge p = this.varGraph.get(i3); p != null; p = p.next) {
                int t = find_parent(p.t);
                if (t != s) {
                    int[] iArr = this.count;
                    iArr[t] = iArr[t] + 1;
                }
            }
        }
        for (int i4 = 0; i4 < this.n_var; i4++) {
            off_graph_edge p2 = this.varGraph.get(i4);
            if (p2 != null && this.rep[i4] != i4) {
                int t2 = find_parent(i4);
                while (p2.next != null) {
                    p2 = p2.next;
                }
                p2.next = this.varGraph.get(t2);
                this.varGraph.set(t2, this.varGraph.get(i4));
                this.varGraph.set(i4, null);
            }
        }
        this.queue.clear();
        for (int i5 = 0; i5 < this.n_var; i5++) {
            if (this.rep[i5] == i5 && this.count[i5] == 0) {
                this.queue.addLast(Integer.valueOf(i5));
            }
        }
        int i6 = 0;
        while (!this.queue.isEmpty()) {
            int s2 = this.queue.getFirst().intValue();
            this.queue.removeFirst();
            this.int2var.get(s2).top_value = i6;
            i6 += this.repsize[s2];
            off_graph_edge off_graph_edgeVar = this.varGraph.get(s2);
            while (true) {
                off_graph_edge p3 = off_graph_edgeVar;
                if (p3 == null) {
                    break;
                }
                int t3 = find_parent(p3.t);
                if (t3 != s2) {
                    int[] iArr2 = this.count;
                    int i7 = iArr2[t3] - 1;
                    iArr2[t3] = i7;
                    if (i7 == 0) {
                        this.queue.addLast(Integer.valueOf(t3));
                    }
                }
                off_graph_edgeVar = p3.next;
            }
        }
        for (int i8 = this.n_var - 1; i8 > -1; i8--) {
            if (this.rep[i8] != i8) {
                IVarAbstraction node = this.int2var.get(find_parent(i8));
                IVarAbstraction me = this.int2var.get(i8);
                me.top_value = (node.top_value + this.repsize[node.id]) - 1;
                int[] iArr3 = this.repsize;
                int i9 = node.id;
                iArr3[i9] = iArr3[i9] - 1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public off_graph_edge add_graph_edge(int s, int t) {
        off_graph_edge e = new off_graph_edge();
        e.s = s;
        e.t = t;
        e.next = this.varGraph.get(s);
        this.varGraph.set(s, e);
        return e;
    }

    private void tarjan_scc(int s) {
        int t;
        int[] iArr = this.pre;
        int[] iArr2 = this.low;
        int i = this.pre_cnt;
        this.pre_cnt = i + 1;
        iArr2[s] = i;
        iArr[s] = i;
        this.queue.addLast(Integer.valueOf(s));
        off_graph_edge off_graph_edgeVar = this.varGraph.get(s);
        while (true) {
            off_graph_edge p = off_graph_edgeVar;
            if (p == null) {
                break;
            }
            int t2 = p.t;
            if (this.pre[t2] == -1) {
                tarjan_scc(t2);
            }
            if (this.low[t2] < this.low[s]) {
                this.low[s] = this.low[t2];
            }
            off_graph_edgeVar = p.next;
        }
        if (this.low[s] < this.pre[s]) {
            return;
        }
        int w = s;
        do {
            t = this.queue.getLast().intValue();
            this.queue.removeLast();
            int[] iArr3 = this.low;
            iArr3[t] = iArr3[t] + this.n_var;
            w = merge_nodes(w, t);
        } while (t != s);
    }

    private int find_parent(int v) {
        if (v == this.rep[v]) {
            return v;
        }
        int[] iArr = this.rep;
        int find_parent = find_parent(this.rep[v]);
        iArr[v] = find_parent;
        return find_parent;
    }

    private int merge_nodes(int v1, int v2) {
        int v12 = find_parent(v1);
        int v22 = find_parent(v2);
        if (v12 != v22) {
            if (this.repsize[v12] < this.repsize[v22]) {
                v12 = v22;
                v22 = v12;
            }
            this.rep[v22] = v12;
            int[] iArr = this.repsize;
            int i = v12;
            iArr[i] = iArr[i] + this.repsize[v22];
        }
        return v12;
    }
}
