package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EntryPoints;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.Type;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CallGraphBuilder.class */
public class CallGraphBuilder {
    private static final Logger logger;
    private final PointsToAnalysis pa;
    private final ReachableMethods reachables;
    private final OnFlyCallGraphBuilder ofcgb;
    private final CallGraph cg;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CallGraphBuilder.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(CallGraphBuilder.class);
    }

    public CallGraphBuilder() {
        logger.warn("using incomplete callgraph containing only application classes.");
        this.pa = DumbPointerAnalysis.v();
        this.cg = Scene.v().internalMakeCallGraph();
        Scene.v().setCallGraph(this.cg);
        List<MethodOrMethodContext> entryPoints = new ArrayList<>();
        entryPoints.addAll(EntryPoints.v().methodsOfApplicationClasses());
        entryPoints.addAll(EntryPoints.v().implicit());
        this.reachables = new ReachableMethods(this.cg, entryPoints);
        this.ofcgb = new OnFlyCallGraphBuilder(new ContextInsensitiveContextManager(this.cg), this.reachables, true);
    }

    public CallGraphBuilder(PointsToAnalysis pa) {
        this.pa = pa;
        this.cg = Scene.v().internalMakeCallGraph();
        Scene.v().setCallGraph(this.cg);
        this.reachables = Scene.v().getReachableMethods();
        this.ofcgb = createCGBuilder(makeContextManager(this.cg), this.reachables);
    }

    protected OnFlyCallGraphBuilder createCGBuilder(ContextManager cm, ReachableMethods reachables2) {
        return new OnFlyCallGraphBuilder(cm, this.reachables);
    }

    public CallGraph getCallGraph() {
        return this.cg;
    }

    public ReachableMethods reachables() {
        return this.reachables;
    }

    public static ContextManager makeContextManager(CallGraph cg) {
        return new ContextInsensitiveContextManager(cg);
    }

    public void build() {
        MethodOrMethodContext momc;
        QueueReader<MethodOrMethodContext> worklist = this.reachables.listener();
        do {
            this.ofcgb.processReachables();
            this.reachables.update();
            if (worklist.hasNext()) {
                momc = worklist.next();
            } else {
                return;
            }
        } while (process(momc));
    }

    protected boolean process(MethodOrMethodContext momc) {
        processReceivers(momc);
        processBases(momc);
        processArrays(momc);
        processStringConstants(momc);
        return true;
    }

    protected void processStringConstants(MethodOrMethodContext momc) {
        List<Local> stringConstants = this.ofcgb.methodToStringConstants().get(momc.method());
        if (stringConstants != null) {
            for (Local stringConstant : stringConstants) {
                Collection<String> possibleStringConstants = this.pa.reachingObjects(stringConstant).possibleStringConstants();
                if (possibleStringConstants == null) {
                    this.ofcgb.addStringConstant(stringConstant, momc.context(), null);
                } else {
                    for (String constant : possibleStringConstants) {
                        this.ofcgb.addStringConstant(stringConstant, momc.context(), constant);
                    }
                }
            }
        }
    }

    protected void processArrays(final MethodOrMethodContext momc) {
        List<Local> argArrays = this.ofcgb.methodToInvokeBases().get(momc.method());
        if (argArrays != null) {
            for (final Local argArray : argArrays) {
                PointsToSet pts = this.pa.reachingObjects(argArray);
                if (pts instanceof PointsToSetInternal) {
                    PointsToSetInternal ptsi = (PointsToSetInternal) pts;
                    ptsi.forall(new P2SetVisitor() { // from class: soot.jimple.toolkits.callgraph.CallGraphBuilder.1
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public void visit(Node n) {
                            if (!CallGraphBuilder.$assertionsDisabled && !(n instanceof AllocNode)) {
                                throw new AssertionError();
                            }
                            AllocNode an = (AllocNode) n;
                            Object newExpr = an.getNewExpr();
                            CallGraphBuilder.this.ofcgb.addInvokeArgDotField(argArray, an.dot(ArrayElement.v()));
                            if (newExpr instanceof NewArrayExpr) {
                                NewArrayExpr nae = (NewArrayExpr) newExpr;
                                Value size = nae.getSize();
                                if (!(size instanceof IntConstant)) {
                                    CallGraphBuilder.this.ofcgb.setArgArrayNonDetSize(argArray, momc.context());
                                    return;
                                }
                                IntConstant arrSize = (IntConstant) size;
                                CallGraphBuilder.this.ofcgb.addPossibleArgArraySize(argArray, arrSize.value, momc.context());
                            }
                        }
                    });
                }
                for (Type t : this.pa.reachingObjectsOfArrayElement(pts).possibleTypes()) {
                    this.ofcgb.addInvokeArgType(argArray, momc.context(), t);
                }
            }
        }
    }

    protected void processBases(MethodOrMethodContext momc) {
        List<Local> bases = this.ofcgb.methodToInvokeArgs().get(momc.method());
        if (bases != null) {
            for (Local base : bases) {
                for (Type ty : this.pa.reachingObjects(base).possibleTypes()) {
                    this.ofcgb.addBaseType(base, momc.context(), ty);
                }
            }
        }
    }

    protected void processReceivers(MethodOrMethodContext momc) {
        List<Local> receivers = this.ofcgb.methodToReceivers().get(momc.method());
        if (receivers != null) {
            for (Local receiver : receivers) {
                for (Type type : this.pa.reachingObjects(receiver).possibleTypes()) {
                    this.ofcgb.addType(receiver, momc.context(), type, null);
                }
            }
        }
    }
}
