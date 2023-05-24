package soot.jimple.spark.geom.geomPA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.G;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.geomE.FullSensitiveNodeGenerator;
import soot.jimple.spark.geom.heapinsE.HeapInsNodeGenerator;
import soot.jimple.spark.geom.helper.GeomEvaluator;
import soot.jimple.spark.geom.ptinsE.PtInsNodeGenerator;
import soot.jimple.spark.geom.utils.SootInfo;
import soot.jimple.spark.geom.utils.ZArrayNumberer;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.ContextVarNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.options.SparkOptions;
import soot.toolkits.scalar.Pair;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/GeomPointsTo.class */
public class GeomPointsTo extends PAG {
    private static final Logger logger = LoggerFactory.getLogger(GeomPointsTo.class);
    protected IWorklist worklist;
    protected IEncodingBroker nodeGenerator;
    protected TypeManager typeManager;
    protected OfflineProcessor offlineProcessor;
    public Map<Node, IVarAbstraction> consG;
    public ZArrayNumberer<IVarAbstraction> pointers;
    public ZArrayNumberer<IVarAbstraction> allocations;
    public ZArrayNumberer<PlainConstraint> constraints;
    public Set<Stmt> thread_run_callsites;
    public Set<Stmt> multiCallsites;
    public long[] context_size;
    public long[] max_context_size_block;
    public int[] block_num;
    public int max_scc_size;
    public int max_scc_id;
    public int n_func;
    public int n_calls;
    public int n_reach_methods;
    public int n_reach_user_methods;
    public int n_reach_spark_user_methods;
    public int n_init_constraints;
    public String dump_dir;
    public PrintStream ps;
    protected Map<String, Boolean> validMethods;
    protected CgEdge[] call_graph;
    protected Vector<CgEdge> obsoletedEdges;
    protected Map<Integer, LinkedList<CgEdge>> rev_call_graph;
    protected Deque<Integer> queue_cg;
    protected int[] vis_cg;
    protected int[] low_cg;
    protected int[] rep_cg;
    protected int[] indeg_cg;
    protected int[] scc_size;
    protected int pre_cnt;
    protected Map<SootMethod, Integer> func2int;
    protected Map<Integer, SootMethod> int2func;
    protected Map<Edge, CgEdge> edgeMapping;
    private boolean hasTransformed;
    private boolean hasExecuted;
    private boolean ddPrepared;

    public GeomPointsTo(SparkOptions opts) {
        super(opts);
        this.worklist = null;
        this.nodeGenerator = null;
        this.typeManager = null;
        this.offlineProcessor = null;
        this.consG = null;
        this.pointers = null;
        this.allocations = null;
        this.constraints = null;
        this.thread_run_callsites = null;
        this.multiCallsites = null;
        this.dump_dir = null;
        this.ps = null;
        this.validMethods = null;
        this.obsoletedEdges = null;
        this.rev_call_graph = null;
        this.queue_cg = null;
        this.func2int = null;
        this.int2func = null;
        this.edgeMapping = null;
        this.hasTransformed = false;
        this.hasExecuted = false;
        this.ddPrepared = false;
    }

    public String toString() {
        return "Geometric Points-To Analysis";
    }

    private void prepareContainers() {
        this.consG = new HashMap(39341);
        this.pointers = new ZArrayNumberer<>(25771);
        this.allocations = new ZArrayNumberer<>();
        this.constraints = new ZArrayNumberer<>(25771);
        this.thread_run_callsites = new HashSet(251);
        this.multiCallsites = new HashSet(251);
        this.queue_cg = new LinkedList();
        this.func2int = new HashMap(5011);
        this.int2func = new HashMap(5011);
        this.edgeMapping = new HashMap(19763);
        this.consG.clear();
        this.constraints.clear();
        this.func2int.clear();
        this.edgeMapping.clear();
    }

    public void parametrize(double spark_run_time) {
        int solver_encoding = this.opts.geom_encoding();
        if (solver_encoding == 1) {
            this.nodeGenerator = new FullSensitiveNodeGenerator();
        } else if (solver_encoding == 2) {
            this.nodeGenerator = new HeapInsNodeGenerator();
        } else if (solver_encoding == 3) {
            this.nodeGenerator = new PtInsNodeGenerator();
        }
        String encoding_name = this.nodeGenerator.getSignature();
        if (encoding_name == null) {
            throw new RuntimeException("No encoding given for geometric points-to analysis.");
        }
        if (this.nodeGenerator == null) {
            throw new RuntimeException("The encoding " + encoding_name + " is unavailable for geometric points-to analysis.");
        }
        switch (this.opts.geom_worklist()) {
            case 1:
                this.worklist = new PQ_Worklist();
                break;
            case 2:
                this.worklist = new FIFO_Worklist();
                break;
        }
        this.dump_dir = this.opts.geom_dump_verbose();
        File dir = null;
        if (!this.dump_dir.isEmpty()) {
            dir = new File(this.dump_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File log_file = new File(this.dump_dir, String.valueOf(encoding_name) + (this.opts.geom_blocking() ? "_blocked" : "_unblocked") + "_frac" + this.opts.geom_frac_base() + "_runs" + this.opts.geom_runs() + "_log.txt");
            try {
                this.ps = new PrintStream(log_file);
                logger.debug("[Geom] Analysis log can be found in: " + log_file.toString());
            } catch (FileNotFoundException e) {
                String msg = "[Geom] The dump file: " + log_file.toString() + " cannot be created. Abort.";
                logger.debug(msg);
                throw new RuntimeException(msg, e);
            }
        } else {
            this.ps = G.v().out;
        }
        String method_verify_file = this.opts.geom_verify_name();
        if (method_verify_file != null) {
            try {
                FileReader fr = new FileReader(method_verify_file);
                Scanner fin = new Scanner(fr);
                this.validMethods = new HashMap();
                while (fin.hasNextLine()) {
                    this.validMethods.put(fin.nextLine(), Boolean.FALSE);
                }
                fin.close();
                fr.close();
                logger.debug("[Geom] Read in verification file successfully.\n");
            } catch (FileNotFoundException e2) {
                this.validMethods = null;
            } catch (IOException e3) {
                logger.debug(e3.getMessage(), (Throwable) e3);
            }
        }
        Parameters.seedPts = this.opts.geom_app_only() ? 15 : Integer.MAX_VALUE;
        double mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        this.ps.println();
        this.ps.printf("[Spark] Time: %.3f s\n", Double.valueOf(spark_run_time / 1000.0d));
        this.ps.printf("[Spark] Memory: %.1f MB\n", Double.valueOf((mem / 1024.0d) / 1024.0d));
        this.typeManager = getTypeManager();
        Parameters.max_cons_budget = this.opts.geom_frac_base();
        Parameters.max_pts_budget = Parameters.max_cons_budget * 2;
        Parameters.cg_refine_times = this.opts.geom_runs();
        if (Parameters.cg_refine_times < 1) {
            Parameters.cg_refine_times = 1;
        }
        prepareContainers();
        this.ps.println("[Geom] Start working on <" + (dir == null ? "NoName" : dir.getName()) + "> with <" + encoding_name + "> encoding.");
    }

    private void preprocess() {
        this.n_func = Scene.v().getReachableMethods().size() + 1;
        this.call_graph = new CgEdge[this.n_func];
        this.n_calls = 0;
        this.n_reach_spark_user_methods = 0;
        int id = 1;
        QueueReader<MethodOrMethodContext> smList = Scene.v().getReachableMethods().listener();
        CallGraph soot_callgraph = Scene.v().getCallGraph();
        while (smList.hasNext()) {
            SootMethod func = smList.next().method();
            this.func2int.put(func, Integer.valueOf(id));
            this.int2func.put(Integer.valueOf(id), func);
            if (soot_callgraph.isEntryMethod(func) || func.isEntryMethod()) {
                CgEdge p = new CgEdge(0, id, null, this.call_graph[0]);
                this.call_graph[0] = p;
                this.n_calls++;
            }
            if (!func.isJavaLibraryMethod()) {
                this.n_reach_spark_user_methods++;
            }
            id++;
        }
        QueueReader<Edge> edgeList = Scene.v().getCallGraph().listener();
        while (edgeList.hasNext()) {
            Edge edge = edgeList.next();
            if (!edge.isClinit()) {
                SootMethod src_func = edge.src();
                SootMethod tgt_func = edge.tgt();
                int s = this.func2int.get(src_func).intValue();
                int t = this.func2int.get(tgt_func).intValue();
                CgEdge p2 = new CgEdge(s, t, edge, this.call_graph[s]);
                this.call_graph[s] = p2;
                this.edgeMapping.put(edge, p2);
                Stmt callsite = edge.srcStmt();
                if (edge.isThreadRunCall() || edge.kind().isExecutor() || edge.kind().isAsyncTask()) {
                    this.thread_run_callsites.add(callsite);
                } else if (edge.isInstance() && !edge.isSpecial()) {
                    InstanceInvokeExpr expr = (InstanceInvokeExpr) callsite.getInvokeExpr();
                    if (expr.getMethodRef().getSignature().contains("<java.lang.Thread: void start()>")) {
                        this.thread_run_callsites.add(callsite);
                    } else {
                        p2.base_var = findLocalVarNode(expr.getBase());
                        if (SootInfo.countCallEdgesForCallsite(callsite, true) > 1 && p2.base_var != null) {
                            this.multiCallsites.add(callsite);
                        }
                    }
                }
                this.n_calls++;
            }
        }
        Iterator<VarNode> it = getVarNodeNumberer().iterator();
        while (it.hasNext()) {
            VarNode vn = it.next();
            IVarAbstraction pn = makeInternalNode(vn);
            this.pointers.add((ZArrayNumberer<IVarAbstraction>) pn);
        }
        Iterator<AllocDotField> it2 = getAllocDotFieldNodeNumberer().iterator();
        while (it2.hasNext()) {
            AllocDotField adf = it2.next();
            SparkField field = adf.getField();
            if (field instanceof SootField) {
                Type decType = ((SootField) field).getDeclaringClass().getType();
                Type baseType = adf.getBase().getType();
                if (!castNeverFails(baseType, decType)) {
                }
            }
            IVarAbstraction pn2 = makeInternalNode(adf);
            this.pointers.add((ZArrayNumberer<IVarAbstraction>) pn2);
        }
        Iterator<AllocNode> it3 = getAllocNodeNumberer().iterator();
        while (it3.hasNext()) {
            AllocNode obj = it3.next();
            IVarAbstraction pn3 = makeInternalNode(obj);
            this.allocations.add((ZArrayNumberer<IVarAbstraction>) pn3);
        }
        for (Object object : allocSources()) {
            IVarAbstraction obj2 = makeInternalNode((AllocNode) object);
            Node[] succs = allocLookup((AllocNode) object);
            for (Node element0 : succs) {
                PlainConstraint cons = new PlainConstraint();
                IVarAbstraction p3 = makeInternalNode(element0);
                cons.expr.setPair(obj2, p3);
                cons.type = 0;
                this.constraints.add((ZArrayNumberer<PlainConstraint>) cons);
            }
        }
        Pair<Node, Node> intercall = new Pair<>();
        for (Object object2 : simpleSources()) {
            IVarAbstraction p4 = makeInternalNode((VarNode) object2);
            Node[] succs2 = simpleLookup((VarNode) object2);
            for (Node element02 : succs2) {
                PlainConstraint cons2 = new PlainConstraint();
                IVarAbstraction q = makeInternalNode(element02);
                cons2.expr.setPair(p4, q);
                cons2.type = 1;
                intercall.setPair((VarNode) object2, element02);
                cons2.interCallEdges = lookupEdgesForAssignment(intercall);
                this.constraints.add((ZArrayNumberer<PlainConstraint>) cons2);
            }
        }
        this.assign2edges.clear();
        for (FieldRefNode frn : loadSources()) {
            IVarAbstraction p5 = makeInternalNode(frn.getBase());
            Node[] succs3 = loadLookup(frn);
            for (Node element03 : succs3) {
                PlainConstraint cons3 = new PlainConstraint();
                IVarAbstraction q2 = makeInternalNode(element03);
                cons3.f = frn.getField();
                cons3.expr.setPair(p5, q2);
                cons3.type = 2;
                this.constraints.add((ZArrayNumberer<PlainConstraint>) cons3);
            }
        }
        for (Object object3 : storeSources()) {
            IVarAbstraction p6 = makeInternalNode((VarNode) object3);
            Node[] succs4 = storeLookup((VarNode) object3);
            for (Node element04 : succs4) {
                PlainConstraint cons4 = new PlainConstraint();
                FieldRefNode frn2 = (FieldRefNode) element04;
                IVarAbstraction q3 = makeInternalNode(frn2.getBase());
                cons4.f = frn2.getField();
                cons4.expr.setPair(p6, q3);
                cons4.type = 3;
                this.constraints.add((ZArrayNumberer<PlainConstraint>) cons4);
            }
        }
        this.n_init_constraints = this.constraints.size();
        this.low_cg = new int[this.n_func];
        this.vis_cg = new int[this.n_func];
        this.rep_cg = new int[this.n_func];
        this.indeg_cg = new int[this.n_func];
        this.scc_size = new int[this.n_func];
        this.block_num = new int[this.n_func];
        this.context_size = new long[this.n_func];
        this.max_context_size_block = new long[this.n_func];
    }

    private void mergeLocalVariables() {
        int[] count = new int[this.pointers.size()];
        Iterator<PlainConstraint> it = this.constraints.iterator();
        while (it.hasNext()) {
            PlainConstraint cons = it.next();
            IVarAbstraction my_lhs = cons.getLHS();
            IVarAbstraction my_rhs = cons.getRHS();
            switch (cons.type) {
                case 0:
                case 1:
                    int i = my_rhs.id;
                    count[i] = count[i] + 1;
                    break;
                case 2:
                    Node lhs = my_lhs.getWrappedNode();
                    int i2 = my_rhs.id;
                    count[i2] = count[i2] + lhs.getP2Set().size();
                    break;
            }
        }
        Iterator<PlainConstraint> cons_it = this.constraints.iterator();
        while (cons_it.hasNext()) {
            PlainConstraint cons2 = cons_it.next();
            if (cons2.type == 1) {
                IVarAbstraction my_lhs2 = cons2.getLHS();
                IVarAbstraction my_rhs2 = cons2.getRHS();
                Node lhs2 = my_lhs2.getWrappedNode();
                Node rhs = my_rhs2.getWrappedNode();
                if ((lhs2 instanceof LocalVarNode) && (rhs instanceof LocalVarNode)) {
                    SootMethod sm1 = ((LocalVarNode) lhs2).getMethod();
                    SootMethod sm2 = ((LocalVarNode) rhs).getMethod();
                    if (sm1 == sm2 && count[my_rhs2.id] == 1 && lhs2.getType() == rhs.getType()) {
                        my_rhs2.merge(my_lhs2);
                        cons_it.remove();
                    }
                }
            }
        }
        Iterator<PlainConstraint> it2 = this.constraints.iterator();
        while (it2.hasNext()) {
            PlainConstraint cons3 = it2.next();
            IVarAbstraction my_lhs3 = cons3.getLHS();
            IVarAbstraction my_rhs3 = cons3.getRHS();
            switch (cons3.type) {
                case 0:
                    cons3.setRHS(my_rhs3.getRepresentative());
                    break;
                case 1:
                case 2:
                case 3:
                    cons3.setLHS(my_lhs3.getRepresentative());
                    cons3.setRHS(my_rhs3.getRepresentative());
                    break;
            }
        }
    }

    private void callGraphDFS(int s) {
        int t;
        int[] iArr = this.vis_cg;
        int[] iArr2 = this.low_cg;
        int i = this.pre_cnt;
        this.pre_cnt = i + 1;
        iArr2[s] = i;
        iArr[s] = i;
        this.queue_cg.addLast(Integer.valueOf(s));
        CgEdge cgEdge = this.call_graph[s];
        while (true) {
            CgEdge p = cgEdge;
            if (p == null) {
                break;
            }
            int t2 = p.t;
            if (this.vis_cg[t2] == 0) {
                callGraphDFS(t2);
                this.low_cg[s] = Math.min(this.low_cg[s], this.low_cg[t2]);
            } else {
                this.low_cg[s] = Math.min(this.low_cg[s], this.vis_cg[t2]);
            }
            cgEdge = p.next;
        }
        if (this.low_cg[s] < this.vis_cg[s]) {
            this.scc_size[s] = 1;
            return;
        }
        this.scc_size[s] = this.queue_cg.size();
        do {
            t = this.queue_cg.getLast().intValue();
            this.queue_cg.removeLast();
            this.rep_cg[t] = s;
            int[] iArr3 = this.low_cg;
            iArr3[t] = iArr3[t] + this.n_func;
        } while (s != t);
        int[] iArr4 = this.scc_size;
        iArr4[s] = iArr4[s] - this.queue_cg.size();
        if (this.scc_size[s] > this.max_scc_size) {
            this.max_scc_size = this.scc_size[s];
            this.max_scc_id = s;
        }
    }

    private void encodeContexts(boolean connectMissedEntries) {
        CgEdge p;
        CgEdge p2;
        int n_reachable = 0;
        int n_scc_reachable = 0;
        int n_full = 0;
        long max_contexts = Long.MIN_VALUE;
        Random rGen = new Random();
        this.pre_cnt = 1;
        this.max_scc_size = 1;
        for (int i = 0; i < this.n_func; i++) {
            this.vis_cg[i] = 0;
            this.indeg_cg[i] = 0;
            this.max_context_size_block[i] = 0;
        }
        this.queue_cg.clear();
        callGraphDFS(0);
        if (connectMissedEntries) {
            for (int i2 = 1; i2 < this.n_func; i2++) {
                if (this.vis_cg[i2] == 0) {
                    callGraphDFS(i2);
                }
            }
        }
        for (int i3 = 0; i3 < this.n_func; i3++) {
            if (this.vis_cg[i3] != 0) {
                CgEdge cgEdge = this.call_graph[i3];
                while (true) {
                    CgEdge p3 = cgEdge;
                    if (p3 == null) {
                        break;
                    }
                    if (this.rep_cg[i3] == this.rep_cg[p3.t]) {
                        p3.scc_edge = true;
                    } else {
                        p3.scc_edge = false;
                        int[] iArr = this.indeg_cg;
                        int i4 = this.rep_cg[p3.t];
                        iArr[i4] = iArr[i4] + 1;
                    }
                    cgEdge = p3.next;
                }
                n_reachable++;
                if (this.rep_cg[i3] == i3) {
                    n_scc_reachable++;
                }
            }
        }
        if (connectMissedEntries) {
            for (int i5 = 1; i5 < this.n_func; i5++) {
                int rep_node = this.rep_cg[i5];
                if (this.indeg_cg[rep_node] == 0) {
                    this.call_graph[0] = new CgEdge(0, i5, null, this.call_graph[0]);
                    this.n_calls++;
                }
            }
        }
        for (int i6 = 0; i6 < this.n_func; i6++) {
            if (this.vis_cg[i6] != 0 && this.rep_cg[i6] != i6) {
                CgEdge cgEdge2 = this.call_graph[i6];
                while (true) {
                    p2 = cgEdge2;
                    if (p2.next == null) {
                        break;
                    }
                    cgEdge2 = p2.next;
                }
                p2.next = this.call_graph[this.rep_cg[i6]];
                this.call_graph[this.rep_cg[i6]] = this.call_graph[i6];
            }
        }
        this.max_context_size_block[0] = 1;
        this.queue_cg.addLast(0);
        while (!this.queue_cg.isEmpty()) {
            int i7 = this.queue_cg.getFirst().intValue();
            this.queue_cg.removeFirst();
            CgEdge cgEdge3 = this.call_graph[i7];
            while (true) {
                CgEdge p4 = cgEdge3;
                if (p4 == null) {
                    break;
                }
                if (!p4.scc_edge) {
                    int j = this.rep_cg[p4.t];
                    if (Constants.MAX_CONTEXTS - this.max_context_size_block[i7] < this.max_context_size_block[j]) {
                        long start = rGen.nextLong();
                        if (start < 0) {
                            start = -start;
                        }
                        if (start > Constants.MAX_CONTEXTS - this.max_context_size_block[i7]) {
                            start = Constants.MAX_CONTEXTS - this.max_context_size_block[i7];
                            this.max_context_size_block[j] = 9223372036854775806L;
                        } else if (this.max_context_size_block[j] < start + this.max_context_size_block[i7]) {
                            this.max_context_size_block[j] = start + this.max_context_size_block[i7];
                        }
                        p4.map_offset = start + 1;
                    } else {
                        p4.map_offset = this.max_context_size_block[j] + 1;
                        long[] jArr = this.max_context_size_block;
                        jArr[j] = jArr[j] + this.max_context_size_block[i7];
                    }
                    int[] iArr2 = this.indeg_cg;
                    int i8 = iArr2[j] - 1;
                    iArr2[j] = i8;
                    if (i8 == 0) {
                        this.queue_cg.addLast(Integer.valueOf(j));
                    }
                } else {
                    p4.map_offset = 1L;
                }
                cgEdge3 = p4.next;
            }
            if (this.max_context_size_block[i7] > max_contexts) {
                max_contexts = this.max_context_size_block[i7];
            }
        }
        for (int i9 = this.n_func - 1; i9 > -1; i9--) {
            if (this.vis_cg[i9] != 0) {
                if (this.rep_cg[i9] != i9) {
                    this.max_context_size_block[i9] = this.max_context_size_block[this.rep_cg[i9]];
                    CgEdge cgEdge4 = this.call_graph[i9];
                    while (true) {
                        p = cgEdge4;
                        if (p.next.s != i9) {
                            break;
                        }
                        cgEdge4 = p.next;
                    }
                    this.call_graph[this.rep_cg[i9]] = p.next;
                    p.next = null;
                }
                if (this.max_context_size_block[i9] == Constants.MAX_CONTEXTS) {
                    n_full++;
                }
                this.context_size[i9] = this.max_context_size_block[i9];
                this.block_num[i9] = 1;
            }
        }
        if (getOpts().geom_blocking()) {
            for (int i10 = 0; i10 < this.n_func; i10++) {
                if (this.vis_cg[i10] != 0) {
                    CgEdge cgEdge5 = this.call_graph[i10];
                    while (true) {
                        CgEdge p5 = cgEdge5;
                        if (p5 == null) {
                            break;
                        }
                        int j2 = p5.t;
                        if (j2 != i10 && p5.scc_edge) {
                            if (this.context_size[j2] <= Constants.MAX_CONTEXTS - this.max_context_size_block[i10]) {
                                p5.map_offset = this.context_size[j2] + 1;
                                long[] jArr2 = this.context_size;
                                jArr2[j2] = jArr2[j2] + this.max_context_size_block[i10];
                                int[] iArr3 = this.block_num;
                                iArr3[j2] = iArr3[j2] + 1;
                            } else {
                                int iBlock = 0;
                                if (this.block_num[j2] > 1) {
                                    iBlock = rGen.nextInt(this.block_num[j2] - 1) + 1;
                                }
                                p5.map_offset = (iBlock * this.max_context_size_block[j2]) + 1;
                            }
                        }
                        cgEdge5 = p5.next;
                    }
                }
            }
        }
        this.ps.printf("Reachable Methods = %d, in which #Condensed Nodes = %d, #Full Context Nodes = %d \n", Integer.valueOf(n_reachable - 1), Integer.valueOf(n_scc_reachable - 1), Integer.valueOf(n_full));
        this.ps.printf("Maximum SCC = %d \n", Integer.valueOf(this.max_scc_size));
        this.ps.printf("The maximum context size = %e\n", Double.valueOf(max_contexts));
    }

    private void solveConstraints() {
        IWorklist ptaList = this.worklist;
        while (ptaList.has_job()) {
            IVarAbstraction pn = ptaList.next();
            pn.do_before_propagation();
            pn.propagate(this, ptaList);
            pn.do_after_propagation();
        }
    }

    private void getCallTargets(IVarAbstraction pn, SootMethod src, Stmt callsite, ChunkedQueue<SootMethod> targetsQueue) {
        InstanceInvokeExpr iie = (InstanceInvokeExpr) callsite.getInvokeExpr();
        Local receiver = (Local) iie.getBase();
        for (AllocNode an : pn.get_all_points_to_objects()) {
            Type type = an.getType();
            if (type != null) {
                VirtualCalls.v().resolve(type, receiver.getType(), iie.getMethodRef(), src, targetsQueue);
            }
        }
    }

    private int updateCallGraph() {
        IVarAbstraction pn;
        int all_virtual_edges = 0;
        int n_obsoleted = 0;
        CallGraph cg = Scene.v().getCallGraph();
        ChunkedQueue<SootMethod> targetsQueue = new ChunkedQueue<>();
        QueueReader<SootMethod> targets = targetsQueue.reader();
        Set<SootMethod> resolvedMethods = new HashSet<>();
        Iterator<Stmt> csIt = this.multiCallsites.iterator();
        while (csIt.hasNext()) {
            Stmt callsite = csIt.next();
            Iterator<Edge> edges = cg.edgesOutOf(callsite);
            if (!edges.hasNext()) {
                csIt.remove();
            } else {
                Edge anyEdge = edges.next();
                CgEdge p = this.edgeMapping.get(anyEdge);
                SootMethod src = anyEdge.src();
                if (!isReachableMethod(src)) {
                    csIt.remove();
                } else if (edges.hasNext() && (pn = this.consG.get(p.base_var)) != null) {
                    getCallTargets(pn.getRepresentative(), src, callsite, targetsQueue);
                    resolvedMethods.clear();
                    while (targets.hasNext()) {
                        resolvedMethods.add(targets.next());
                    }
                    while (true) {
                        SootMethod tgt = anyEdge.tgt();
                        if (!resolvedMethods.contains(tgt) && !anyEdge.kind().isFake()) {
                            CgEdge p2 = this.edgeMapping.get(anyEdge);
                            p2.is_obsoleted = true;
                        }
                        if (!edges.hasNext()) {
                            break;
                        }
                        anyEdge = edges.next();
                    }
                }
            }
        }
        for (int i = 1; i < this.n_func; i++) {
            CgEdge p3 = this.call_graph[i];
            CgEdge q = null;
            while (p3 != null) {
                if (this.vis_cg[i] == 0) {
                    p3.is_obsoleted = true;
                }
                if (p3.base_var != null) {
                    all_virtual_edges++;
                }
                CgEdge temp = p3.next;
                if (!p3.is_obsoleted) {
                    p3.next = q;
                    q = p3;
                } else {
                    cg.removeEdge(p3.sootEdge);
                    n_obsoleted++;
                }
                p3 = temp;
            }
            this.call_graph[i] = q;
        }
        this.ps.printf("%d of %d virtual call edges are proved to be spurious.\n", Integer.valueOf(n_obsoleted), Integer.valueOf(all_virtual_edges));
        return n_obsoleted;
    }

    private void prepareNextRun() {
        Iterator<IVarAbstraction> it = this.pointers.iterator();
        while (it.hasNext()) {
            IVarAbstraction pn = it.next();
            if (pn.willUpdate) {
                pn.reconstruct();
            }
        }
        System.gc();
    }

    private void markReachableMethods() {
        int ans = 0;
        for (int i = 0; i < this.n_func; i++) {
            this.vis_cg[i] = 0;
        }
        this.queue_cg.clear();
        this.queue_cg.add(0);
        this.vis_cg[0] = 1;
        while (this.queue_cg.size() > 0) {
            int s = this.queue_cg.removeFirst().intValue();
            CgEdge cgEdge = this.call_graph[s];
            while (true) {
                CgEdge p = cgEdge;
                if (p == null) {
                    break;
                }
                int t = p.t;
                if (this.vis_cg[t] == 0) {
                    this.queue_cg.add(Integer.valueOf(t));
                    this.vis_cg[t] = 1;
                    ans++;
                }
                cgEdge = p.next;
            }
        }
        this.n_reach_methods = ans;
        int ans2 = 0;
        for (int i2 = 1; i2 < this.n_func; i2++) {
            SootMethod sm = this.int2func.get(Integer.valueOf(i2));
            if (this.vis_cg[i2] == 0) {
                this.func2int.remove(sm);
                this.int2func.remove(Integer.valueOf(i2));
            } else if (!sm.isJavaLibraryMethod()) {
                ans2++;
            }
        }
        this.n_reach_user_methods = ans2;
    }

    private void buildRevCallGraph() {
        this.rev_call_graph = new HashMap();
        for (int i = 0; i < this.n_func; i++) {
            CgEdge cgEdge = this.call_graph[i];
            while (true) {
                CgEdge p = cgEdge;
                if (p == null) {
                    break;
                }
                LinkedList<CgEdge> list = this.rev_call_graph.get(Integer.valueOf(p.t));
                if (list == null) {
                    list = new LinkedList<>();
                    this.rev_call_graph.put(Integer.valueOf(p.t), list);
                }
                list.add(p);
                cgEdge = p.next;
            }
        }
    }

    private void finalizeInternalData() {
        markReachableMethods();
        Iterator<IVarAbstraction> it = this.allocations.iterator();
        while (it.hasNext()) {
            IVarAbstraction po = it.next();
            SootMethod sm = ((AllocNode) po.getWrappedNode()).getMethod();
            if (sm != null && !this.func2int.containsKey(sm)) {
                it.remove();
            }
        }
        final Vector<AllocNode> removeSet = new Vector<>();
        Iterator<IVarAbstraction> it2 = this.pointers.iterator();
        while (it2.hasNext()) {
            IVarAbstraction pn = it2.next();
            Node vn = pn.getWrappedNode();
            SootMethod sm2 = null;
            if (vn instanceof LocalVarNode) {
                sm2 = ((LocalVarNode) vn).getMethod();
            } else if (vn instanceof AllocDotField) {
                sm2 = ((AllocDotField) vn).getBase().getMethod();
            }
            if (sm2 != null && !this.func2int.containsKey(sm2)) {
                pn.deleteAll();
                vn.discardP2Set();
                it2.remove();
            } else if (pn.getRepresentative() == pn) {
                removeSet.clear();
                if (pn.hasPTResult()) {
                    Set<AllocNode> objSet = pn.get_all_points_to_objects();
                    for (AllocNode obj : objSet) {
                        IVarAbstraction po2 = this.consG.get(obj);
                        if (!po2.reachable() || pn.isDeadObject(obj)) {
                            removeSet.add(obj);
                        }
                    }
                    Iterator<AllocNode> it3 = removeSet.iterator();
                    while (it3.hasNext()) {
                        pn.remove_points_to(it3.next());
                    }
                    pn.drop_duplicates();
                } else {
                    PointsToSetInternal pts = vn.getP2Set();
                    pts.forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.GeomPointsTo.1
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public void visit(Node n) {
                            IVarAbstraction pan = GeomPointsTo.this.findInternalNode(n);
                            if (pan.reachable()) {
                                removeSet.add((AllocNode) n);
                            }
                        }
                    });
                    PointsToSetInternal pts2 = vn.makeP2Set();
                    Iterator<AllocNode> it4 = removeSet.iterator();
                    while (it4.hasNext()) {
                        AllocNode an = it4.next();
                        pts2.add(an);
                    }
                }
            }
        }
        Iterator<PlainConstraint> cIt = this.constraints.iterator();
        while (cIt.hasNext()) {
            PlainConstraint cons = cIt.next();
            IVarAbstraction lhs = cons.getLHS();
            IVarAbstraction rhs = cons.getRHS();
            if (!lhs.reachable() || !rhs.reachable() || getMethodIDFromPtr(lhs) == -1 || getMethodIDFromPtr(rhs) == -1) {
                cIt.remove();
            }
        }
        this.pointers.reassign();
        this.allocations.reassign();
        this.constraints.reassign();
    }

    private void releaseUselessResources() {
        this.offlineProcessor.destroy();
        this.offlineProcessor = null;
        IFigureManager.cleanCache();
        System.gc();
    }

    private void finalizeSootData() {
        Scene.v().releaseReachableMethods();
        Scene.v().getReachableMethods();
        if (!this.opts.geom_trans()) {
            Iterator<IVarAbstraction> it = this.pointers.iterator();
            while (it.hasNext()) {
                IVarAbstraction pn = it.next();
                if (pn == pn.getRepresentative() && pn.hasPTResult()) {
                    pn.keepPointsToOnly();
                    Node vn = pn.getWrappedNode();
                    vn.discardP2Set();
                }
            }
            return;
        }
        transformToCIResult();
    }

    public void transformToCIResult() {
        Iterator<IVarAbstraction> it = this.pointers.iterator();
        while (it.hasNext()) {
            IVarAbstraction pn = it.next();
            if (pn.getRepresentative() == pn) {
                Node node = pn.getWrappedNode();
                node.discardP2Set();
                PointsToSetInternal ptSet = node.makeP2Set();
                for (AllocNode obj : pn.get_all_points_to_objects()) {
                    ptSet.add(obj);
                }
                pn.deleteAll();
            }
        }
        this.hasTransformed = true;
    }

    public void solve() {
        long prepare_time = 0;
        G.v().out.flush();
        preprocess();
        mergeLocalVariables();
        this.worklist.initialize(this.pointers.size());
        this.offlineProcessor = new OfflineProcessor(this);
        IFigureManager.cleanCache();
        int evalLevel = this.opts.geom_eval();
        GeomEvaluator ge = new GeomEvaluator(this, this.ps);
        if (evalLevel == 1) {
            ge.profileSparkBasicMetrics();
        }
        Date begin = new Date();
        int rounds = 0;
        int n_obs = 1000;
        while (rounds < Parameters.cg_refine_times && n_obs > 0) {
            this.ps.println("\n[Geom] Propagation Round " + rounds + " ==> ");
            encodeContexts(rounds == 0);
            Date prepare_begin = new Date();
            this.offlineProcessor.init();
            this.offlineProcessor.defaultFeedPtsRoutines();
            this.offlineProcessor.runOptimizations();
            Date prepare_end = new Date();
            prepare_time += prepare_end.getTime() - prepare_begin.getTime();
            if (rounds == 0 && evalLevel <= 1) {
                this.offlineProcessor.releaseSparkMem();
            }
            prepareNextRun();
            this.nodeGenerator.initFlowGraph(this);
            solveConstraints();
            n_obs = updateCallGraph();
            finalizeInternalData();
            rounds++;
        }
        if (rounds < Parameters.cg_refine_times) {
            this.ps.printf("\nThe points-to information has converged. We stop here.\n", new Object[0]);
        }
        Date end = new Date();
        long solve_time = 0 + (end.getTime() - begin.getTime());
        long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        this.ps.println();
        this.ps.printf("[Geom] Preprocessing time: %.2f s\n", Double.valueOf(prepare_time / 1000.0d));
        this.ps.printf("[Geom] Total time: %.2f s\n", Double.valueOf(solve_time / 1000.0d));
        this.ps.printf("[Geom] Memory: %.1f MB\n", Double.valueOf((mem / 1024.0d) / 1024.0d));
        if (evalLevel != 0) {
            ge.profileGeomBasicMetrics(evalLevel > 1);
            if (evalLevel > 1) {
                ge.checkCallGraph();
                ge.checkCastsSafety();
                ge.checkAliasAnalysis();
            }
        }
        finalizeSootData();
        releaseUselessResources();
        this.hasExecuted = true;
    }

    public void ddSolve(Set<Node> qryNodes) {
        if (!this.hasExecuted) {
            solve();
        }
        if (!this.ddPrepared || this.offlineProcessor == null) {
            this.offlineProcessor = new OfflineProcessor(this);
            IFigureManager.cleanCache();
            this.ddPrepared = true;
            this.ps.println();
            this.ps.println("==> Entering demand-driven mode (experimental).");
        }
        int init_size = qryNodes.size();
        if (init_size == 0) {
            this.ps.println("Please provide at least one pointer.");
            return;
        }
        Date prepare_begin = new Date();
        this.offlineProcessor.init();
        this.offlineProcessor.addUserDefPts(qryNodes);
        this.offlineProcessor.runOptimizations();
        Date prepare_end = new Date();
        long prepare_time = 0 + (prepare_end.getTime() - prepare_begin.getTime());
        Date begin = new Date();
        prepareNextRun();
        this.nodeGenerator.initFlowGraph(this);
        solveConstraints();
        Date end = new Date();
        long solve_time = 0 + (end.getTime() - begin.getTime());
        this.ps.println();
        this.ps.printf("[ddGeom] Preprocessing time: %.2f seconds\n", Double.valueOf(prepare_time / 1000.0d));
        this.ps.printf("[ddGeom] Main propagation time: %.2f seconds\n", Double.valueOf(solve_time / 1000.0d));
    }

    public void cleanResult() {
        this.consG.clear();
        this.pointers.clear();
        this.allocations.clear();
        this.constraints.clear();
        this.func2int.clear();
        this.int2func.clear();
        this.edgeMapping.clear();
        this.hasTransformed = false;
        this.hasExecuted = false;
        System.gc();
        System.gc();
        System.gc();
        System.gc();
    }

    public void keepOnly(Set<IVarAbstraction> usefulPointers) {
        Set<IVarAbstraction> reps = new HashSet<>();
        for (IVarAbstraction pn : usefulPointers) {
            reps.add(pn.getRepresentative());
        }
        usefulPointers.addAll(reps);
        Iterator<IVarAbstraction> it = this.pointers.iterator();
        while (it.hasNext()) {
            IVarAbstraction pn2 = it.next();
            if (!usefulPointers.contains(pn2)) {
                pn2.deleteAll();
            }
        }
        System.gc();
    }

    public int getIDFromSootMethod(SootMethod sm) {
        Integer ans = this.func2int.get(sm);
        if (ans == null) {
            return -1;
        }
        return ans.intValue();
    }

    public SootMethod getSootMethodFromID(int fid) {
        return this.int2func.get(Integer.valueOf(fid));
    }

    public boolean isReachableMethod(int fid) {
        return (fid == -1 || this.vis_cg[fid] == 0) ? false : true;
    }

    public boolean isReachableMethod(SootMethod sm) {
        int id = getIDFromSootMethod(sm);
        return isReachableMethod(id);
    }

    public boolean isValidMethod(SootMethod sm) {
        if (this.validMethods != null) {
            String sig = sm.toString();
            if (!this.validMethods.containsKey(sig)) {
                return false;
            }
            this.validMethods.put(sig, Boolean.TRUE);
            return true;
        }
        return true;
    }

    public void outputNotEvaluatedMethods() {
        if (this.validMethods != null) {
            this.ps.println("\nThe following methods are not evaluated because they are unreachable:");
            for (Map.Entry<String, Boolean> entry : this.validMethods.entrySet()) {
                if (!entry.getValue().booleanValue()) {
                    this.ps.println(entry.getKey());
                }
            }
            this.ps.println();
        }
    }

    public Set<SootMethod> getAllReachableMethods() {
        return this.func2int.keySet();
    }

    public CgEdge getCallEgesOutFrom(int fid) {
        return this.call_graph[fid];
    }

    public LinkedList<CgEdge> getCallEdgesInto(int fid) {
        if (this.rev_call_graph == null) {
            buildRevCallGraph();
        }
        return this.rev_call_graph.get(Integer.valueOf(fid));
    }

    public int getMethodIDFromPtr(IVarAbstraction pn) {
        SootMethod sm = null;
        int ret = 0;
        Node node = pn.getWrappedNode();
        if (node instanceof AllocNode) {
            sm = ((AllocNode) node).getMethod();
        } else if (node instanceof LocalVarNode) {
            sm = ((LocalVarNode) node).getMethod();
        } else if (node instanceof AllocDotField) {
            sm = ((AllocDotField) node).getBase().getMethod();
        }
        if (sm != null && this.func2int.containsKey(sm)) {
            int id = this.func2int.get(sm).intValue();
            ret = this.vis_cg[id] == 0 ? -1 : id;
        }
        return ret;
    }

    public IVarAbstraction makeInternalNode(Node v) {
        IVarAbstraction ret = this.consG.get(v);
        if (ret == null) {
            ret = this.nodeGenerator.generateNode(v);
            this.consG.put(v, ret);
        }
        return ret;
    }

    public IVarAbstraction findInternalNode(Node v) {
        return this.consG.get(v);
    }

    public boolean castNeverFails(Type src, Type dst) {
        return this.typeManager.castNeverFails(src, dst);
    }

    public int getNumberOfPointers() {
        return this.pointers.size();
    }

    public int getNumberOfObjects() {
        return this.allocations.size();
    }

    public int getNumberOfSparkMethods() {
        return this.n_func;
    }

    public int getNumberOfMethods() {
        return this.n_reach_methods;
    }

    public IWorklist getWorklist() {
        return this.worklist;
    }

    public IVarAbstraction findInstanceField(AllocNode obj, SparkField field) {
        AllocDotField af = findAllocDotField(obj, field);
        return this.consG.get(af);
    }

    public IVarAbstraction findAndInsertInstanceField(AllocNode obj, SparkField field) {
        AllocDotField af = findAllocDotField(obj, field);
        IVarAbstraction pn = null;
        if (af == null) {
            Type decType = ((SootField) field).getDeclaringClass().getType();
            Type baseType = obj.getType();
            if (this.typeManager.castNeverFails(baseType, decType)) {
                pn = makeInternalNode(makeAllocDotField(obj, field));
                this.pointers.add((ZArrayNumberer<IVarAbstraction>) pn);
            }
        } else {
            pn = this.consG.get(af);
        }
        return pn;
    }

    public CgEdge getInternalEdgeFromSootEdge(Edge e) {
        return this.edgeMapping.get(e);
    }

    public boolean isExceptionPointer(Node v) {
        if (v.getType() instanceof RefType) {
            SootClass sc = ((RefType) v.getType()).getSootClass();
            if (!sc.isInterface() && Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sc, Constants.exeception_type.getSootClass())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isValidGeometricNode(Node sparkNode) {
        IVarAbstraction pNode = this.consG.get(sparkNode);
        return pNode != null && pNode.reachable();
    }

    public boolean hasGeomExecuted() {
        return this.hasExecuted;
    }

    public FileOutputStream createOutputFile(String file_name) throws FileNotFoundException {
        return new FileOutputStream(new File(this.dump_dir, file_name));
    }

    private PointsToSetInternal field_p2set(PointsToSet s, final SparkField f) {
        if (!(s instanceof PointsToSetInternal)) {
            throw new RuntimeException("Base pointers must be stored in *PointsToSetInternal*.");
        }
        PointsToSetInternal bases = (PointsToSetInternal) s;
        final PointsToSetInternal ret = getSetFactory().newSet(f.getType(), this);
        bases.forall(new P2SetVisitor() { // from class: soot.jimple.spark.geom.geomPA.GeomPointsTo.2
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                Node nDotF = ((AllocNode) n).dot(f);
                if (nDotF != null) {
                    IVarAbstraction pn = GeomPointsTo.this.consG.get(nDotF);
                    if (pn == null || GeomPointsTo.this.hasTransformed || nDotF.getP2Set() != EmptyPointsToSet.v()) {
                        ret.addAll(nDotF.getP2Set(), null);
                        return;
                    }
                    for (AllocNode obj : pn.getRepresentative().get_all_points_to_objects()) {
                        ret.add(obj);
                    }
                }
            }
        });
        return ret;
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l) {
        if (!this.hasExecuted) {
            return super.reachingObjects(l);
        }
        LocalVarNode vn = findLocalVarNode(l);
        if (vn == null) {
            return EmptyPointsToSet.v();
        }
        IVarAbstraction pn = this.consG.get(vn);
        if (pn == null || this.hasTransformed || vn.getP2Set() != EmptyPointsToSet.v()) {
            return vn.getP2Set();
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        PointsToSetInternal ptSet = vn.makeP2Set();
        for (AllocNode obj : pn2.get_all_points_to_objects()) {
            ptSet.add(obj);
        }
        return ptSet;
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l) {
        if (!this.hasExecuted) {
            return super.reachingObjects(c, l);
        }
        if (this.hasTransformed || !(c instanceof Unit)) {
            return reachingObjects(l);
        }
        LocalVarNode vn = findLocalVarNode(l);
        if (vn == null) {
            return EmptyPointsToSet.v();
        }
        IVarAbstraction pn = this.consG.get(vn);
        if (pn == null) {
            return vn.getP2Set();
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        SootMethod callee = vn.getMethod();
        Edge e = Scene.v().getCallGraph().findEdge((Unit) c, callee);
        if (e == null) {
            return vn.getP2Set();
        }
        CgEdge myEdge = getInternalEdgeFromSootEdge(e);
        if (myEdge == null) {
            return vn.getP2Set();
        }
        long low = myEdge.map_offset;
        long high = low + this.max_context_size_block[myEdge.s];
        ContextVarNode cvn = vn.context(c);
        if (cvn != null) {
            PointsToSetInternal ans = cvn.getP2Set();
            if (ans != EmptyPointsToSet.v()) {
                return ans;
            }
        } else {
            cvn = makeContextVarNode(vn, c);
        }
        PointsToSetInternal ptset = cvn.makeP2Set();
        for (AllocNode an : pn2.get_all_points_to_objects()) {
            if (pn2.pointer_interval_points_to(low, high, an)) {
                ptset.add(an);
            }
        }
        return ptset;
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjects(SootField f) {
        if (!this.hasExecuted) {
            return super.reachingObjects(f);
        }
        if (!f.isStatic()) {
            throw new RuntimeException("The parameter f must be a *static* field.");
        }
        VarNode vn = findGlobalVarNode(f);
        if (vn == null) {
            return EmptyPointsToSet.v();
        }
        IVarAbstraction pn = this.consG.get(vn);
        if (pn == null || this.hasTransformed || vn.getP2Set() != EmptyPointsToSet.v()) {
            return vn.getP2Set();
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        PointsToSetInternal ptSet = vn.makeP2Set();
        for (AllocNode obj : pn2.getRepresentative().get_all_points_to_objects()) {
            ptSet.add(obj);
        }
        return ptSet;
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjects(PointsToSet s, SootField f) {
        if (!this.hasExecuted) {
            return super.reachingObjects(s, f);
        }
        return field_p2set(s, f);
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l, SootField f) {
        if (!this.hasExecuted) {
            return super.reachingObjects(l, f);
        }
        return reachingObjects(reachingObjects(l), f);
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l, SootField f) {
        if (!this.hasExecuted) {
            return super.reachingObjects(c, l, f);
        }
        return reachingObjects(reachingObjects(c, l), f);
    }

    @Override // soot.jimple.spark.pag.PAG, soot.PointsToAnalysis
    public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
        if (!this.hasExecuted) {
            return super.reachingObjectsOfArrayElement(s);
        }
        return field_p2set(s, ArrayElement.v());
    }

    public PointsToSet reachingObjects(AllocNode an, SootField f) {
        AllocDotField adf = an.dot(f);
        IVarAbstraction pn = this.consG.get(adf);
        if (adf == null) {
            return EmptyPointsToSet.v();
        }
        if (pn == null || this.hasTransformed || adf.getP2Set() != EmptyPointsToSet.v()) {
            return adf.getP2Set();
        }
        IVarAbstraction pn2 = pn.getRepresentative();
        PointsToSetInternal ptSet = adf.makeP2Set();
        for (AllocNode obj : pn2.getRepresentative().get_all_points_to_objects()) {
            ptSet.add(obj);
        }
        return ptSet;
    }
}
