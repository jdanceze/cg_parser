package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.UnopExpr;
import soot.jimple.internal.JCaughtExceptionRef;
import soot.toolkits.graph.MemoryEfficientGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/SimpleMethodInfoFlowAnalysis.class */
public class SimpleMethodInfoFlowAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Pair<EquivalentValue, EquivalentValue>>> {
    SootMethod sm;
    Value thisLocal;
    InfoFlowAnalysis dfa;
    boolean refOnly;
    MutableDirectedGraph<EquivalentValue> infoFlowGraph;
    Ref returnRef;
    FlowSet<Pair<EquivalentValue, EquivalentValue>> entrySet;
    FlowSet<Pair<EquivalentValue, EquivalentValue>> emptySet;
    boolean printMessages;
    private static final Logger logger = LoggerFactory.getLogger(SimpleMethodInfoFlowAnalysis.class);
    public static int counter = 0;

    public SimpleMethodInfoFlowAnalysis(UnitGraph g, InfoFlowAnalysis dfa, boolean ignoreNonRefTypeFlow) {
        this(g, dfa, ignoreNonRefTypeFlow, true);
        counter++;
        for (int i = 0; i < this.sm.getParameterCount(); i++) {
            EquivalentValue parameterRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(this.sm, i);
            if (!this.infoFlowGraph.containsNode(parameterRefEqVal)) {
                this.infoFlowGraph.addNode(parameterRefEqVal);
            }
        }
        for (SootField sf : this.sm.getDeclaringClass().getFields()) {
            EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, sf);
            if (!this.infoFlowGraph.containsNode(fieldRefEqVal)) {
                this.infoFlowGraph.addNode(fieldRefEqVal);
            }
        }
        SootClass superclass = this.sm.getDeclaringClass();
        for (superclass = superclass.hasSuperclass() ? this.sm.getDeclaringClass().getSuperclass() : superclass; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
            for (SootField scField : superclass.getFields()) {
                EquivalentValue fieldRefEqVal2 = InfoFlowAnalysis.getNodeForFieldRef(this.sm, scField);
                if (!this.infoFlowGraph.containsNode(fieldRefEqVal2)) {
                    this.infoFlowGraph.addNode(fieldRefEqVal2);
                }
            }
        }
        EquivalentValue thisRefEqVal = InfoFlowAnalysis.getNodeForThisRef(this.sm);
        if (!this.infoFlowGraph.containsNode(thisRefEqVal)) {
            this.infoFlowGraph.addNode(thisRefEqVal);
        }
        EquivalentValue returnRefEqVal = new CachedEquivalentValue(this.returnRef);
        if (!this.infoFlowGraph.containsNode(returnRefEqVal)) {
            this.infoFlowGraph.addNode(returnRefEqVal);
        }
        if (this.printMessages) {
            logger.debug("STARTING ANALYSIS FOR " + g.getBody().getMethod() + " -----");
        }
        doFlowInsensitiveAnalysis();
        if (this.printMessages) {
            logger.debug("ENDING   ANALYSIS FOR " + g.getBody().getMethod() + " -----");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleMethodInfoFlowAnalysis(UnitGraph g, InfoFlowAnalysis dfa, boolean ignoreNonRefTypeFlow, boolean dummyDontRunAnalysisYet) {
        super(g);
        this.sm = g.getBody().getMethod();
        if (this.sm.isStatic()) {
            this.thisLocal = null;
        } else {
            this.thisLocal = g.getBody().getThisLocal();
        }
        this.dfa = dfa;
        this.refOnly = ignoreNonRefTypeFlow;
        this.infoFlowGraph = new MemoryEfficientGraph();
        this.returnRef = new ParameterRef(g.getBody().getMethod().getReturnType(), -1);
        this.entrySet = new ArraySparseSet();
        this.emptySet = new ArraySparseSet();
        this.printMessages = false;
    }

    /* JADX WARN: Incorrect condition in loop: B:12:0x004f */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void doFlowInsensitiveAnalysis() {
        /*
            r5 = this;
            r0 = r5
            soot.toolkits.scalar.FlowSet r0 = r0.newInitialFlow()
            r6 = r0
            r0 = 1
            r7 = r0
            goto L4e
        La:
            r0 = r6
            int r0 = r0.size()
            r8 = r0
            r0 = r5
            soot.toolkits.graph.DirectedGraph<N> r0 = r0.graph
            java.util.Iterator r0 = r0.iterator()
            r9 = r0
            goto L33
        L1f:
            r0 = r9
            java.lang.Object r0 = r0.next()
            soot.Unit r0 = (soot.Unit) r0
            r10 = r0
            r0 = r5
            r1 = r6
            r2 = r10
            r3 = r6
            r0.flowThrough(r1, r2, r3)
        L33:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L1f
            r0 = r6
            int r0 = r0.size()
            r1 = r8
            if (r0 <= r1) goto L4c
            r0 = 1
            r7 = r0
            goto L4e
        L4c:
            r0 = 0
            r7 = r0
        L4e:
            r0 = r7
            if (r0 != 0) goto La
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.infoflow.SimpleMethodInfoFlowAnalysis.doFlowInsensitiveAnalysis():void");
    }

    public MutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary() {
        return this.infoFlowGraph;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Pair<EquivalentValue, EquivalentValue>> in1, FlowSet<Pair<EquivalentValue, EquivalentValue>> in2, FlowSet<Pair<EquivalentValue, EquivalentValue>> out) {
        in1.union(in2, out);
    }

    protected boolean isNonRefType(Type type) {
        return !(type instanceof RefLikeType);
    }

    protected boolean ignoreThisDataType(Type type) {
        return this.refOnly && isNonRefType(type);
    }

    public boolean isInterestingSource(Value source) {
        return source instanceof Ref;
    }

    public boolean isTrackableSource(Value source) {
        return isInterestingSource(source) || (source instanceof Ref);
    }

    public boolean isInterestingSink(Value sink) {
        return sink instanceof Ref;
    }

    public boolean isTrackableSink(Value sink) {
        return isInterestingSink(sink) || (sink instanceof Ref) || (sink instanceof Local);
    }

    private ArrayList<Value> getDirectSources(Value v, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
        ArrayList<Value> ret = new ArrayList<>();
        EquivalentValue vEqVal = new CachedEquivalentValue(v);
        for (Pair<EquivalentValue, EquivalentValue> pair : fs) {
            if (pair.getO1().equals(vEqVal)) {
                ret.add(pair.getO2().getValue());
            }
        }
        return ret;
    }

    protected void handleFlowsToValue(Value sink, Value initialSource, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
        if (!isTrackableSink(sink)) {
            return;
        }
        List<Value> sources = getDirectSources(initialSource, fs);
        if (isTrackableSource(initialSource)) {
            sources.add(initialSource);
        }
        for (Value source : sources) {
            EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
            EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
            if (!sinkEqVal.equals(sourceEqVal)) {
                Pair<EquivalentValue, EquivalentValue> pair = new Pair<>(sinkEqVal, sourceEqVal);
                if (!fs.contains(pair)) {
                    fs.add(pair);
                    if (isInterestingSource(source) && isInterestingSink(sink)) {
                        if (!this.infoFlowGraph.containsNode(sinkEqVal)) {
                            this.infoFlowGraph.addNode(sinkEqVal);
                        }
                        if (!this.infoFlowGraph.containsNode(sourceEqVal)) {
                            this.infoFlowGraph.addNode(sourceEqVal);
                        }
                        this.infoFlowGraph.addEdge(sourceEqVal, sinkEqVal);
                        if (this.printMessages) {
                            logger.debug("      Found " + source + " flows to " + sink);
                        }
                    }
                }
            }
        }
    }

    protected void handleFlowsToDataStructure(Value base, Value initialSource, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
        List<Value> sinks = getDirectSources(base, fs);
        if (isTrackableSink(base)) {
            sinks.add(base);
        }
        List<Value> sources = getDirectSources(initialSource, fs);
        if (isTrackableSource(initialSource)) {
            sources.add(initialSource);
        }
        for (Value source : sources) {
            EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
            for (Value sink : sinks) {
                if (isTrackableSink(sink)) {
                    EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
                    if (!sinkEqVal.equals(sourceEqVal)) {
                        Pair<EquivalentValue, EquivalentValue> pair = new Pair<>(sinkEqVal, sourceEqVal);
                        if (!fs.contains(pair)) {
                            fs.add(pair);
                            if (isInterestingSource(source) && isInterestingSink(sink)) {
                                if (!this.infoFlowGraph.containsNode(sinkEqVal)) {
                                    this.infoFlowGraph.addNode(sinkEqVal);
                                }
                                if (!this.infoFlowGraph.containsNode(sourceEqVal)) {
                                    this.infoFlowGraph.addNode(sourceEqVal);
                                }
                                this.infoFlowGraph.addEdge(sourceEqVal, sinkEqVal);
                                if (this.printMessages) {
                                    logger.debug("      Found " + source + " flows to " + sink);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected List<Value> handleInvokeExpr(InvokeExpr ie, Stmt is, FlowSet<Pair<EquivalentValue, EquivalentValue>> fs) {
        MutableDirectedGraph<EquivalentValue> dataFlowGraph = this.dfa.getInvokeInfoFlowSummary(ie, is, this.sm);
        List<Value> returnValueSources = new ArrayList<>();
        for (EquivalentValue nodeEqVal : dataFlowGraph.getNodes()) {
            if (!(nodeEqVal.getValue() instanceof Ref)) {
                throw new RuntimeException("Illegal node type in data flow graph:" + nodeEqVal.getValue() + " should be an object of type Ref.");
            }
            Value node = (Ref) nodeEqVal.getValue();
            Value source = null;
            if (node instanceof ParameterRef) {
                ParameterRef param = (ParameterRef) node;
                if (param.getIndex() != -1) {
                    source = ie.getArg(param.getIndex());
                }
            } else if (node instanceof StaticFieldRef) {
                source = node;
            } else if ((ie instanceof InstanceInvokeExpr) && (node instanceof InstanceFieldRef)) {
                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                source = iie.getBase();
            }
            for (EquivalentValue sinkEqVal : dataFlowGraph.getSuccsOf(nodeEqVal)) {
                Ref sink = (Ref) sinkEqVal.getValue();
                if (sink instanceof ParameterRef) {
                    ParameterRef param2 = (ParameterRef) sink;
                    if (param2.getIndex() == -1) {
                        returnValueSources.add(source);
                    } else {
                        handleFlowsToDataStructure(ie.getArg(param2.getIndex()), source, fs);
                    }
                } else if (sink instanceof StaticFieldRef) {
                    handleFlowsToValue(sink, source, fs);
                } else if ((ie instanceof InstanceInvokeExpr) && (sink instanceof InstanceFieldRef)) {
                    InstanceInvokeExpr iie2 = (InstanceInvokeExpr) ie;
                    handleFlowsToDataStructure(iie2.getBase(), source, fs);
                }
            }
        }
        return returnValueSources;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Pair<EquivalentValue, EquivalentValue>> in, Unit unit, FlowSet<Pair<EquivalentValue, EquivalentValue>> out) {
        Stmt stmt = (Stmt) unit;
        if (in != out) {
            in.copy(out);
        }
        if (stmt instanceof IdentityStmt) {
            IdentityStmt is = (IdentityStmt) stmt;
            IdentityRef ir = (IdentityRef) is.getRightOp();
            if (!(ir instanceof JCaughtExceptionRef)) {
                if (ir instanceof ParameterRef) {
                    if (!ignoreThisDataType(ir.getType())) {
                        handleFlowsToValue(is.getLeftOp(), ir, out);
                    }
                } else if ((ir instanceof ThisRef) && !ignoreThisDataType(ir.getType())) {
                    handleFlowsToValue(is.getLeftOp(), ir, out);
                }
            }
        } else if (stmt instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt) stmt;
            Value rv = rs.getOp();
            if (!(rv instanceof Constant) && (rv instanceof Local) && !ignoreThisDataType(rv.getType())) {
                handleFlowsToValue(this.returnRef, rv, out);
            }
        } else if (!(stmt instanceof AssignStmt)) {
            if (stmt.containsInvokeExpr()) {
                handleInvokeExpr(stmt.getInvokeExpr(), stmt, out);
            }
        } else {
            AssignStmt as = (AssignStmt) stmt;
            Value lv = as.getLeftOp();
            Value rv2 = as.getRightOp();
            Value sink = null;
            boolean flowsToDataStructure = false;
            if (lv instanceof Local) {
                sink = lv;
            } else if (lv instanceof ArrayRef) {
                sink = ((ArrayRef) lv).getBase();
                flowsToDataStructure = true;
            } else if (lv instanceof StaticFieldRef) {
                sink = lv;
            } else if (lv instanceof InstanceFieldRef) {
                InstanceFieldRef ifr = (InstanceFieldRef) lv;
                if (ifr.getBase() == this.thisLocal) {
                    sink = lv;
                } else {
                    sink = ifr.getBase();
                    flowsToDataStructure = true;
                }
            }
            List<Value> sources = new ArrayList<>();
            boolean interestingFlow = true;
            if (rv2 instanceof Local) {
                sources.add(rv2);
                interestingFlow = !ignoreThisDataType(rv2.getType());
            } else if (rv2 instanceof Constant) {
                sources.add(rv2);
                interestingFlow = !ignoreThisDataType(rv2.getType());
            } else if (rv2 instanceof ArrayRef) {
                ArrayRef ar = (ArrayRef) rv2;
                sources.add(ar.getBase());
                interestingFlow = !ignoreThisDataType(ar.getType());
            } else if (rv2 instanceof StaticFieldRef) {
                sources.add(rv2);
                interestingFlow = !ignoreThisDataType(rv2.getType());
            } else if (rv2 instanceof InstanceFieldRef) {
                InstanceFieldRef ifr2 = (InstanceFieldRef) rv2;
                if (ifr2.getBase() == this.thisLocal) {
                    sources.add(rv2);
                    interestingFlow = !ignoreThisDataType(rv2.getType());
                } else {
                    sources.add(ifr2.getBase());
                    interestingFlow = !ignoreThisDataType(ifr2.getType());
                }
            } else if (rv2 instanceof AnyNewExpr) {
                sources.add(rv2);
                interestingFlow = !ignoreThisDataType(rv2.getType());
            } else if (rv2 instanceof BinopExpr) {
                BinopExpr be = (BinopExpr) rv2;
                sources.add(be.getOp1());
                sources.add(be.getOp2());
                interestingFlow = !ignoreThisDataType(be.getType());
            } else if (rv2 instanceof CastExpr) {
                CastExpr ce = (CastExpr) rv2;
                sources.add(ce.getOp());
                interestingFlow = !ignoreThisDataType(ce.getType());
            } else if (rv2 instanceof InstanceOfExpr) {
                InstanceOfExpr ioe = (InstanceOfExpr) rv2;
                sources.add(ioe.getOp());
                interestingFlow = !ignoreThisDataType(ioe.getType());
            } else if (rv2 instanceof UnopExpr) {
                UnopExpr ue = (UnopExpr) rv2;
                sources.add(ue.getOp());
                interestingFlow = !ignoreThisDataType(ue.getType());
            } else if (rv2 instanceof InvokeExpr) {
                InvokeExpr ie = (InvokeExpr) rv2;
                sources.addAll(handleInvokeExpr(ie, as, out));
                interestingFlow = !ignoreThisDataType(ie.getType());
            }
            if (interestingFlow) {
                if (flowsToDataStructure) {
                    for (Value source : sources) {
                        handleFlowsToDataStructure(sink, source, out);
                    }
                    return;
                }
                for (Value source2 : sources) {
                    handleFlowsToValue(sink, source2, out);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Pair<EquivalentValue, EquivalentValue>> source, FlowSet<Pair<EquivalentValue, EquivalentValue>> dest) {
        source.copy(dest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Pair<EquivalentValue, EquivalentValue>> entryInitialFlow() {
        return this.entrySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Pair<EquivalentValue, EquivalentValue>> newInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    public void addToEntryInitialFlow(Value source, Value sink) {
        EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
        EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
        if (sinkEqVal.equals(sourceEqVal)) {
            return;
        }
        Pair<EquivalentValue, EquivalentValue> pair = new Pair<>(sinkEqVal, sourceEqVal);
        if (!this.entrySet.contains(pair)) {
            this.entrySet.add(pair);
        }
    }

    public void addToNewInitialFlow(Value source, Value sink) {
        EquivalentValue sinkEqVal = new CachedEquivalentValue(sink);
        EquivalentValue sourceEqVal = new CachedEquivalentValue(source);
        if (sinkEqVal.equals(sourceEqVal)) {
            return;
        }
        Pair<EquivalentValue, EquivalentValue> pair = new Pair<>(sinkEqVal, sourceEqVal);
        if (!this.emptySet.contains(pair)) {
            this.emptySet.add(pair);
        }
    }

    public Value getThisLocal() {
        return this.thisLocal;
    }
}
