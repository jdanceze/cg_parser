package soot.jimple.spark.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.Type;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.ContextManager;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.options.Options;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/solver/OnFlyCallGraph.class */
public class OnFlyCallGraph {
    protected final OnFlyCallGraphBuilder ofcgb;
    protected final ReachableMethods reachableMethods;
    protected final QueueReader<MethodOrMethodContext> reachablesReader;
    protected final QueueReader<Edge> callEdges;
    protected final CallGraph callGraph = Scene.v().internalMakeCallGraph();
    private static final Logger logger;
    private PAG pag;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !OnFlyCallGraph.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(OnFlyCallGraph.class);
    }

    public ReachableMethods reachableMethods() {
        return this.reachableMethods;
    }

    public CallGraph callGraph() {
        return this.callGraph;
    }

    public OnFlyCallGraph(PAG pag, boolean appOnly) {
        this.pag = pag;
        Scene.v().setCallGraph(this.callGraph);
        ContextManager cm = CallGraphBuilder.makeContextManager(this.callGraph);
        this.reachableMethods = Scene.v().getReachableMethods();
        this.ofcgb = createOnFlyCallGraphBuilder(cm, this.reachableMethods, appOnly);
        this.reachablesReader = this.reachableMethods.listener();
        this.callEdges = cm.callGraph().listener();
    }

    protected OnFlyCallGraphBuilder createOnFlyCallGraphBuilder(ContextManager cm, ReachableMethods reachableMethods, boolean appOnly) {
        return new OnFlyCallGraphBuilder(cm, reachableMethods, appOnly);
    }

    public void build() {
        this.ofcgb.processReachables();
        processReachables();
        processCallEdges();
    }

    private void processReachables() {
        this.reachableMethods.update();
        while (this.reachablesReader.hasNext()) {
            MethodOrMethodContext m = this.reachablesReader.next();
            MethodPAG mpag = MethodPAG.v(this.pag, m.method());
            try {
                mpag.build();
            } catch (Exception e) {
                String msg = String.format("An error occurred while processing %s in callgraph", mpag.getMethod());
                if (Options.v().allow_cg_errors()) {
                    logger.error(msg, (Throwable) e);
                } else {
                    throw new RuntimeException(msg, e);
                }
            }
            mpag.addToPAG(m.context());
        }
    }

    private void processCallEdges() {
        while (this.callEdges.hasNext()) {
            Edge e = this.callEdges.next();
            MethodPAG amp = MethodPAG.v(this.pag, e.tgt());
            amp.build();
            amp.addToPAG(e.tgtCtxt());
            this.pag.addCallTarget(e);
        }
    }

    public OnFlyCallGraphBuilder ofcgb() {
        return this.ofcgb;
    }

    public void updatedFieldRef(final AllocDotField df, PointsToSetInternal ptsi) {
        if (df.getField() == ArrayElement.v() && this.ofcgb.wantArrayField(df)) {
            ptsi.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.OnFlyCallGraph.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    OnFlyCallGraph.this.ofcgb.addInvokeArgType(df, (Context) null, n.getType());
                }
            });
        }
    }

    public void updatedNode(VarNode vn) {
        Object r = vn.getVariable();
        if (!(r instanceof Local)) {
            return;
        }
        final Local receiver = (Local) r;
        final Context context = vn.context();
        PointsToSetInternal p2set = vn.getP2Set().getNewSet();
        if (this.ofcgb.wantTypes(receiver)) {
            p2set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.OnFlyCallGraph.2
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    if (n instanceof AllocNode) {
                        OnFlyCallGraph.this.ofcgb.addType(receiver, context, n.getType(), (AllocNode) n);
                    }
                }
            });
        }
        if (this.ofcgb.wantStringConstants(receiver)) {
            p2set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.OnFlyCallGraph.3
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n) {
                    if (n instanceof StringConstantNode) {
                        String constant = ((StringConstantNode) n).getString();
                        OnFlyCallGraph.this.ofcgb.addStringConstant(receiver, context, constant);
                        return;
                    }
                    OnFlyCallGraph.this.ofcgb.addStringConstant(receiver, context, null);
                }
            });
        }
        if (this.ofcgb.wantInvokeArg(receiver)) {
            p2set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.solver.OnFlyCallGraph.4
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public void visit(Node n) {
                    if (n instanceof AllocNode) {
                        AllocNode an = (AllocNode) n;
                        OnFlyCallGraph.this.ofcgb.addInvokeArgDotField(receiver, OnFlyCallGraph.this.pag.makeAllocDotField(an, ArrayElement.v()));
                        if (!OnFlyCallGraph.$assertionsDisabled && !(an.getNewExpr() instanceof NewArrayExpr)) {
                            throw new AssertionError();
                        }
                        NewArrayExpr nae = (NewArrayExpr) an.getNewExpr();
                        if (!(nae.getSize() instanceof IntConstant)) {
                            OnFlyCallGraph.this.ofcgb.setArgArrayNonDetSize(receiver, context);
                            return;
                        }
                        IntConstant sizeConstant = (IntConstant) nae.getSize();
                        OnFlyCallGraph.this.ofcgb.addPossibleArgArraySize(receiver, sizeConstant.value, context);
                    }
                }
            });
            for (Type ty : this.pag.reachingObjectsOfArrayElement(p2set).possibleTypes()) {
                this.ofcgb.addInvokeArgType(receiver, context, ty);
            }
        }
    }

    public void mergedWith(Node n1, Node n2) {
    }
}
