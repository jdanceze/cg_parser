package soot.jimple.spark.geom.helper;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.Local;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.geom.utils.Histogram;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/helper/GeomEvaluator.class */
public class GeomEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(GeomEvaluator.class);
    private GeomPointsTo ptsProvider;
    private PrintStream outputer;
    private EvalResults evalRes = new EvalResults();
    private boolean solved;

    public GeomEvaluator(GeomPointsTo gpts, PrintStream ps) {
        this.ptsProvider = gpts;
        this.outputer = ps;
    }

    public void profileSparkBasicMetrics() {
        int n_legal_var = 0;
        int[] limits = {1, 5, 10, 25, 50, 75, 100};
        this.evalRes.pts_size_bar_spark = new Histogram(limits);
        Iterator<IVarAbstraction> it = this.ptsProvider.pointers.iterator();
        while (it.hasNext()) {
            IVarAbstraction pn = it.next();
            Node var = pn.getWrappedNode();
            if (!this.ptsProvider.isExceptionPointer(var)) {
                n_legal_var++;
                int size = var.getP2Set().size();
                this.evalRes.pts_size_bar_spark.addNumber(size);
                this.evalRes.total_spark_pts += size;
                if (size > this.evalRes.max_pts_spark) {
                    this.evalRes.max_pts_spark = size;
                }
            }
        }
        this.evalRes.avg_spark_pts = this.evalRes.total_spark_pts / n_legal_var;
    }

    public void profileGeomBasicMetrics(boolean testSpark) {
        int n_legal_var = 0;
        int n_alloc_dot_fields = 0;
        int[] limits = {1, 5, 10, 25, 50, 75, 100};
        this.evalRes.pts_size_bar_geom = new Histogram(limits);
        if (testSpark) {
            this.evalRes.total_spark_pts = 0L;
            this.evalRes.max_pts_spark = 0;
            this.evalRes.pts_size_bar_spark = new Histogram(limits);
        }
        for (SootMethod sm : this.ptsProvider.getAllReachableMethods()) {
            if (sm.isConcrete()) {
                if (!sm.hasActiveBody()) {
                    sm.retrieveActiveBody();
                }
                this.evalRes.loc += sm.getActiveBody().getUnits().size();
            }
        }
        Iterator<IVarAbstraction> it = this.ptsProvider.pointers.iterator();
        while (it.hasNext()) {
            IVarAbstraction pn = it.next();
            if (pn.hasPTResult()) {
                IVarAbstraction pn2 = pn.getRepresentative();
                Node var = pn2.getWrappedNode();
                if (!this.ptsProvider.isExceptionPointer(var)) {
                    if (var instanceof AllocDotField) {
                        n_alloc_dot_fields++;
                    }
                    n_legal_var++;
                    if (testSpark) {
                        int size = var.getP2Set().size();
                        this.evalRes.pts_size_bar_spark.addNumber(size);
                        this.evalRes.total_spark_pts += size;
                        if (size > this.evalRes.max_pts_spark) {
                            this.evalRes.max_pts_spark = size;
                        }
                    }
                    int size2 = pn2.num_of_diff_objs();
                    this.evalRes.pts_size_bar_geom.addNumber(size2);
                    this.evalRes.total_geom_ins_pts += size2;
                    if (size2 > this.evalRes.max_pts_geom) {
                        this.evalRes.max_pts_geom = size2;
                    }
                }
            }
        }
        this.evalRes.avg_geom_ins_pts = this.evalRes.total_geom_ins_pts / n_legal_var;
        if (testSpark) {
            this.evalRes.avg_spark_pts = this.evalRes.total_spark_pts / n_legal_var;
        }
        this.outputer.println("");
        this.outputer.println("----------Statistical Result of geomPTA <Data Format: geomPTA (SPARK)>----------");
        this.outputer.printf("Lines of code (jimple): %.1fK\n", Double.valueOf(this.evalRes.loc / 1000.0d));
        this.outputer.printf("Reachable Methods: %d (%d)\n", Integer.valueOf(this.ptsProvider.getNumberOfMethods()), Integer.valueOf(this.ptsProvider.getNumberOfSparkMethods()));
        this.outputer.printf("Reachable User Methods: %d (%d)\n", Integer.valueOf(this.ptsProvider.n_reach_user_methods), Integer.valueOf(this.ptsProvider.n_reach_spark_user_methods));
        this.outputer.println("#All Pointers: " + this.ptsProvider.getNumberOfPointers());
        this.outputer.println("#Core Pointers: " + n_legal_var + ", in which #AllocDot Fields: " + n_alloc_dot_fields);
        this.outputer.printf("Total/Average Projected Points-to Tuples [core pointers]: %d (%d) / %.3f (%.3f) \n", Long.valueOf(this.evalRes.total_geom_ins_pts), Long.valueOf(this.evalRes.total_spark_pts), Double.valueOf(this.evalRes.avg_geom_ins_pts), Double.valueOf(this.evalRes.avg_spark_pts));
        this.outputer.println("The largest points-to set size [core pointers]: " + this.evalRes.max_pts_geom + " (" + this.evalRes.max_pts_spark + ")");
        this.outputer.println();
        this.evalRes.pts_size_bar_geom.printResult(this.outputer, "Points-to Set Sizes Distribution [core pointers]:", this.evalRes.pts_size_bar_spark);
    }

    private void test_1cfa_call_graph(LocalVarNode vn, SootMethod caller, SootMethod callee_signature, Histogram ce_range) {
        IVarAbstraction pn = this.ptsProvider.findInternalNode(vn);
        if (pn == null) {
            return;
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        Set<SootMethod> tgts = new HashSet<>();
        Set<AllocNode> set = pn2.get_all_points_to_objects();
        LinkedList<CgEdge> list = this.ptsProvider.getCallEdgesInto(this.ptsProvider.getIDFromSootMethod(caller));
        FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
        Iterator<CgEdge> it = list.iterator();
        while (it.hasNext()) {
            CgEdge p = it.next();
            long l = p.map_offset;
            long r = l + this.ptsProvider.max_context_size_block[p.s];
            tgts.clear();
            for (AllocNode obj : set) {
                if (pn2.pointer_interval_points_to(l, r, obj)) {
                    Type t = obj.getType();
                    if (t != null) {
                        if (t instanceof AnySubType) {
                            t = ((AnySubType) t).getBase();
                        } else if (t instanceof ArrayType) {
                            t = Scene.v().getObjectType();
                        }
                        try {
                            tgts.add(hierarchy.resolveConcreteDispatch(((RefType) t).getSootClass(), callee_signature));
                        } catch (Exception e) {
                            logger.debug(e.getMessage(), (Throwable) e);
                        }
                    }
                }
            }
            tgts.remove(null);
            ce_range.addNumber(tgts.size());
        }
    }

    public void checkCallGraph() {
        int[] limits = {1, 2, 4, 8};
        this.evalRes.total_call_edges = new Histogram(limits);
        CallGraph cg = Scene.v().getCallGraph();
        for (Stmt callsite : this.ptsProvider.multiCallsites) {
            Iterator<Edge> edges = cg.edgesOutOf(callsite);
            if (edges.hasNext()) {
                this.evalRes.n_callsites++;
                Edge anyEdge = edges.next();
                SootMethod src = anyEdge.src();
                if (this.ptsProvider.isReachableMethod(src) && this.ptsProvider.isValidMethod(src)) {
                    CgEdge p = this.ptsProvider.getInternalEdgeFromSootEdge(anyEdge);
                    LocalVarNode vn = (LocalVarNode) p.base_var;
                    int edge_cnt = 1;
                    while (edges.hasNext()) {
                        edge_cnt++;
                        edges.next();
                    }
                    this.evalRes.n_geom_call_edges += edge_cnt;
                    if (edge_cnt == 1) {
                        this.evalRes.n_geom_solved_all++;
                    }
                    if (!src.isJavaLibraryMethod()) {
                        InvokeExpr ie = callsite.getInvokeExpr();
                        if (edge_cnt == 1) {
                            this.evalRes.n_geom_solved_app++;
                            if (this.ptsProvider.getOpts().verbose()) {
                                this.outputer.println();
                                this.outputer.println("<<<<<<<<<   Additional Solved Call   >>>>>>>>>>");
                                this.outputer.println(src.toString());
                                this.outputer.println(ie.toString());
                            }
                        } else {
                            Histogram call_edges = new Histogram(limits);
                            test_1cfa_call_graph(vn, src, ie.getMethod(), call_edges);
                            this.evalRes.total_call_edges.merge(call_edges);
                        }
                        this.evalRes.n_geom_user_edges += edge_cnt;
                        this.evalRes.n_user_callsites++;
                    }
                }
            }
        }
        this.ptsProvider.ps.println();
        this.ptsProvider.ps.println("--------> Virtual Callsites Evaluation <---------");
        this.ptsProvider.ps.printf("Total virtual callsites (app code): %d (%d)\n", Integer.valueOf(this.evalRes.n_callsites), Integer.valueOf(this.evalRes.n_user_callsites));
        this.ptsProvider.ps.printf("Total virtual call edges (app code): %d (%d)\n", Integer.valueOf(this.evalRes.n_geom_call_edges), Integer.valueOf(this.evalRes.n_geom_user_edges));
        this.ptsProvider.ps.printf("Virtual callsites additionally solved by geomPTA compared to SPARK (app code) = %d (%d)\n", Integer.valueOf(this.evalRes.n_geom_solved_all), Integer.valueOf(this.evalRes.n_geom_solved_app));
        this.evalRes.total_call_edges.printResult(this.ptsProvider.ps, "Testing of unsolved callsites on 1-CFA call graph: ");
        if (this.ptsProvider.getOpts().verbose()) {
            this.ptsProvider.outputNotEvaluatedMethods();
        }
    }

    public void checkAliasAnalysis() {
        LocalVarNode vn;
        IVarAbstraction pn;
        HashSet hashSet = new HashSet();
        ArrayList<IVarAbstraction> al = new ArrayList<>();
        Value[] values = new Value[2];
        for (SootMethod sm : this.ptsProvider.getAllReachableMethods()) {
            if (!sm.isJavaLibraryMethod() && sm.isConcrete()) {
                if (!sm.hasActiveBody()) {
                    sm.retrieveActiveBody();
                }
                if (this.ptsProvider.isValidMethod(sm)) {
                    Iterator<Unit> stmts = sm.getActiveBody().getUnits().iterator();
                    while (stmts.hasNext()) {
                        Stmt st = (Stmt) stmts.next();
                        if (st instanceof AssignStmt) {
                            AssignStmt a = (AssignStmt) st;
                            values[0] = a.getLeftOp();
                            values[1] = a.getRightOp();
                            for (Value v : values) {
                                if (v instanceof InstanceFieldRef) {
                                    InstanceFieldRef ifr = (InstanceFieldRef) v;
                                    SootField field = ifr.getField();
                                    if ((field.getType() instanceof RefType) && (vn = this.ptsProvider.findLocalVarNode((Local) ifr.getBase())) != null && !this.ptsProvider.isExceptionPointer(vn) && (pn = this.ptsProvider.findInternalNode(vn)) != null) {
                                        IVarAbstraction pn2 = pn.getRepresentative();
                                        if (pn2.hasPTResult()) {
                                            hashSet.add(pn2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        hashSet.remove(null);
        al.addAll(hashSet);
        Date begin = new Date();
        int size = al.size();
        for (int i = 0; i < size; i++) {
            IVarAbstraction pn3 = al.get(i);
            VarNode n1 = (VarNode) pn3.getWrappedNode();
            for (int j = i + 1; j < size; j++) {
                IVarAbstraction qn = al.get(j);
                VarNode n2 = (VarNode) qn.getWrappedNode();
                if (pn3.heap_sensitive_intersection(qn)) {
                    this.evalRes.n_hs_alias++;
                }
                if (n1.getP2Set().hasNonEmptyIntersection(n2.getP2Set())) {
                    this.evalRes.n_hi_alias++;
                }
            }
        }
        this.evalRes.n_alias_pairs = (size * (size - 1)) / 2;
        Date end = new Date();
        this.ptsProvider.ps.println();
        this.ptsProvider.ps.println("--------> Alias Pairs Evaluation <---------");
        this.ptsProvider.ps.println("Number of pointer pairs in app code: " + this.evalRes.n_alias_pairs);
        this.ptsProvider.ps.printf("Heap sensitive alias pairs (by Geom): %d, Percentage = %.3f%%\n", Long.valueOf(this.evalRes.n_hs_alias), Double.valueOf((this.evalRes.n_hs_alias / this.evalRes.n_alias_pairs) * 100.0d));
        this.ptsProvider.ps.printf("Heap insensitive alias pairs (by SPARK): %d, Percentage = %.3f%%\n", Long.valueOf(this.evalRes.n_hi_alias), Double.valueOf((this.evalRes.n_hi_alias / this.evalRes.n_alias_pairs) * 100.0d));
        this.ptsProvider.ps.printf("Using time: %dms \n", Long.valueOf(end.getTime() - begin.getTime()));
        this.ptsProvider.ps.println();
    }

    public void checkCastsSafety() {
        IVarAbstraction pn;
        for (SootMethod sm : this.ptsProvider.getAllReachableMethods()) {
            if (!sm.isJavaLibraryMethod() && sm.isConcrete()) {
                if (!sm.hasActiveBody()) {
                    sm.retrieveActiveBody();
                }
                if (this.ptsProvider.isValidMethod(sm)) {
                    Iterator<Unit> stmts = sm.getActiveBody().getUnits().iterator();
                    while (stmts.hasNext()) {
                        Stmt st = (Stmt) stmts.next();
                        if (st instanceof AssignStmt) {
                            Value rhs = ((AssignStmt) st).getRightOp();
                            Value lhs = ((AssignStmt) st).getLeftOp();
                            if ((rhs instanceof CastExpr) && (lhs.getType() instanceof RefLikeType)) {
                                Value v = ((CastExpr) rhs).getOp();
                                VarNode node = this.ptsProvider.findLocalVarNode(v);
                                if (node != null && (pn = this.ptsProvider.findInternalNode(node)) != null) {
                                    IVarAbstraction pn2 = pn.getRepresentative();
                                    if (pn2.hasPTResult()) {
                                        this.evalRes.total_casts++;
                                        final Type targetType = (RefLikeType) ((CastExpr) rhs).getCastType();
                                        this.solved = true;
                                        Set<AllocNode> set = pn2.get_all_points_to_objects();
                                        for (AllocNode obj : set) {
                                            this.solved = this.ptsProvider.castNeverFails(obj.getType(), targetType);
                                            if (!this.solved) {
                                                break;
                                            }
                                        }
                                        if (this.solved) {
                                            this.evalRes.geom_solved_casts++;
                                        }
                                        this.solved = true;
                                        node.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.helper.GeomEvaluator.1
                                            @Override // soot.jimple.spark.sets.P2SetVisitor
                                            public void visit(Node arg0) {
                                                if (!GeomEvaluator.this.solved) {
                                                    return;
                                                }
                                                GeomEvaluator.this.solved = GeomEvaluator.this.ptsProvider.castNeverFails(arg0.getType(), targetType);
                                            }
                                        });
                                        if (this.solved) {
                                            this.evalRes.spark_solved_casts++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.ptsProvider.ps.println();
        this.ptsProvider.ps.println("-----------> Static Casts Safety Evaluation <------------");
        this.ptsProvider.ps.println("Total casts (app code): " + this.evalRes.total_casts);
        this.ptsProvider.ps.println("Safe casts: Geom = " + this.evalRes.geom_solved_casts + ", SPARK = " + this.evalRes.spark_solved_casts);
    }

    public void estimateHeapDefuseGraph() {
        IVarAbstraction pn;
        Map<IVarAbstraction, int[]> defUseCounterForGeom = new HashMap<>();
        final Map<AllocDotField, int[]> defUseCounterForSpark = new HashMap<>();
        Date begin = new Date();
        for (SootMethod sm : this.ptsProvider.getAllReachableMethods()) {
            if (!sm.isJavaLibraryMethod() && sm.isConcrete()) {
                if (!sm.hasActiveBody()) {
                    sm.retrieveActiveBody();
                }
                if (this.ptsProvider.isValidMethod(sm)) {
                    Iterator<Unit> stmts = sm.getActiveBody().getUnits().iterator();
                    while (stmts.hasNext()) {
                        Stmt st = (Stmt) stmts.next();
                        if (st instanceof AssignStmt) {
                            AssignStmt a = (AssignStmt) st;
                            final Value lValue = a.getLeftOp();
                            Value rValue = a.getRightOp();
                            InstanceFieldRef ifr = null;
                            if (lValue instanceof InstanceFieldRef) {
                                ifr = (InstanceFieldRef) lValue;
                            } else if (rValue instanceof InstanceFieldRef) {
                                ifr = (InstanceFieldRef) rValue;
                            }
                            if (ifr != null) {
                                final SootField field = ifr.getField();
                                LocalVarNode vn = this.ptsProvider.findLocalVarNode((Local) ifr.getBase());
                                if (vn != null && (pn = this.ptsProvider.findInternalNode(vn)) != null) {
                                    IVarAbstraction pn2 = pn.getRepresentative();
                                    if (pn2.hasPTResult()) {
                                        vn.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.helper.GeomEvaluator.2
                                            @Override // soot.jimple.spark.sets.P2SetVisitor
                                            public void visit(Node n) {
                                                IVarAbstraction padf = GeomEvaluator.this.ptsProvider.findAndInsertInstanceField((AllocNode) n, field);
                                                AllocDotField adf = (AllocDotField) padf.getWrappedNode();
                                                int[] defUseUnit = (int[]) defUseCounterForSpark.get(adf);
                                                if (defUseUnit == null) {
                                                    defUseUnit = new int[2];
                                                    defUseCounterForSpark.put(adf, defUseUnit);
                                                }
                                                if (lValue instanceof InstanceFieldRef) {
                                                    int[] iArr = defUseUnit;
                                                    iArr[0] = iArr[0] + 1;
                                                    return;
                                                }
                                                int[] iArr2 = defUseUnit;
                                                iArr2[1] = iArr2[1] + 1;
                                            }
                                        });
                                        Set<AllocNode> objsSet = pn2.get_all_points_to_objects();
                                        for (AllocNode obj : objsSet) {
                                            IVarAbstraction padf = this.ptsProvider.findAndInsertInstanceField(obj, field);
                                            int[] defUseUnit = defUseCounterForGeom.get(padf);
                                            if (defUseUnit == null) {
                                                defUseUnit = new int[2];
                                                defUseCounterForGeom.put(padf, defUseUnit);
                                            }
                                            if (lValue instanceof InstanceFieldRef) {
                                                int[] iArr = defUseUnit;
                                                iArr[0] = iArr[0] + 1;
                                            } else {
                                                int[] iArr2 = defUseUnit;
                                                iArr2[1] = iArr2[1] + 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int[] defUseUnit2 : defUseCounterForSpark.values()) {
            this.evalRes.n_spark_du_pairs += defUseUnit2[0] * defUseUnit2[1];
        }
        for (int[] defUseUnit3 : defUseCounterForGeom.values()) {
            this.evalRes.n_geom_du_pairs += defUseUnit3[0] * defUseUnit3[1];
        }
        Date end = new Date();
        this.ptsProvider.ps.println();
        this.ptsProvider.ps.println("-----------> Heap Def Use Graph Evaluation <------------");
        this.ptsProvider.ps.println("The edges in the heap def-use graph is (by Geom): " + this.evalRes.n_geom_du_pairs);
        this.ptsProvider.ps.println("The edges in the heap def-use graph is (by Spark): " + this.evalRes.n_spark_du_pairs);
        this.ptsProvider.ps.printf("Using time: %dms \n", Long.valueOf(end.getTime() - begin.getTime()));
        this.ptsProvider.ps.println();
    }
}
