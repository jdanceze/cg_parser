package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import soot.VoidType;
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
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.MemoryEfficientGraph;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/SmartMethodInfoFlowAnalysis.class */
public class SmartMethodInfoFlowAnalysis {
    UnitGraph graph;
    SootMethod sm;
    Value thisLocal;
    InfoFlowAnalysis dfa;
    boolean refOnly;
    boolean includeInnerFields;
    HashMutableDirectedGraph<EquivalentValue> abbreviatedInfoFlowGraph;
    HashMutableDirectedGraph<EquivalentValue> infoFlowSummary;
    Ref returnRef;
    boolean printMessages;
    private static final Logger logger = LoggerFactory.getLogger(SmartMethodInfoFlowAnalysis.class);
    public static int counter = 0;

    public SmartMethodInfoFlowAnalysis(UnitGraph g, InfoFlowAnalysis dfa) {
        EquivalentValue fieldRefEqVal;
        EquivalentValue fieldRefEqVal2;
        this.graph = g;
        this.sm = g.getBody().getMethod();
        if (this.sm.isStatic()) {
            this.thisLocal = null;
        } else {
            this.thisLocal = g.getBody().getThisLocal();
        }
        this.dfa = dfa;
        this.refOnly = !dfa.includesPrimitiveInfoFlow();
        this.includeInnerFields = dfa.includesInnerFields();
        this.abbreviatedInfoFlowGraph = new MemoryEfficientGraph();
        this.infoFlowSummary = new MemoryEfficientGraph();
        this.returnRef = new ParameterRef(g.getBody().getMethod().getReturnType(), -1);
        this.printMessages = false;
        counter++;
        for (int i = 0; i < this.sm.getParameterCount(); i++) {
            EquivalentValue parameterRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(this.sm, i);
            if (!this.infoFlowSummary.containsNode(parameterRefEqVal)) {
                this.infoFlowSummary.addNode(parameterRefEqVal);
            }
        }
        for (SootField sf : this.sm.getDeclaringClass().getFields()) {
            if (sf.isStatic() || !this.sm.isStatic()) {
                if (!this.sm.isStatic()) {
                    fieldRefEqVal2 = InfoFlowAnalysis.getNodeForFieldRef(this.sm, sf, this.sm.retrieveActiveBody().getThisLocal());
                } else {
                    fieldRefEqVal2 = InfoFlowAnalysis.getNodeForFieldRef(this.sm, sf);
                }
                if (!this.infoFlowSummary.containsNode(fieldRefEqVal2)) {
                    this.infoFlowSummary.addNode(fieldRefEqVal2);
                }
            }
        }
        SootClass superclass = this.sm.getDeclaringClass();
        for (superclass = superclass.hasSuperclass() ? this.sm.getDeclaringClass().getSuperclass() : superclass; superclass.hasSuperclass(); superclass = superclass.getSuperclass()) {
            for (SootField scField : superclass.getFields()) {
                if (scField.isStatic() || !this.sm.isStatic()) {
                    if (!this.sm.isStatic()) {
                        fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, scField, this.sm.retrieveActiveBody().getThisLocal());
                    } else {
                        fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, scField);
                    }
                    if (!this.infoFlowSummary.containsNode(fieldRefEqVal)) {
                        this.infoFlowSummary.addNode(fieldRefEqVal);
                    }
                }
            }
        }
        if (!this.sm.isStatic()) {
            EquivalentValue thisRefEqVal = InfoFlowAnalysis.getNodeForThisRef(this.sm);
            if (!this.infoFlowSummary.containsNode(thisRefEqVal)) {
                this.infoFlowSummary.addNode(thisRefEqVal);
            }
        }
        EquivalentValue returnRefEqVal = new CachedEquivalentValue(this.returnRef);
        if (this.returnRef.getType() != VoidType.v() && !this.infoFlowSummary.containsNode(returnRefEqVal)) {
            this.infoFlowSummary.addNode(returnRefEqVal);
        }
        Date start = new Date();
        int counterSoFar = counter;
        if (this.printMessages) {
            logger.debug("STARTING SMART ANALYSIS FOR " + g.getBody().getMethod() + " -----");
        }
        generateAbbreviatedInfoFlowGraph();
        generateInfoFlowSummary();
        if (this.printMessages) {
            long longTime = new Date().getTime() - start.getTime();
            float time = ((float) longTime) / 1000.0f;
            logger.debug("ENDING   SMART ANALYSIS FOR " + g.getBody().getMethod() + " ----- " + ((counter - counterSoFar) + 1) + " analyses took: " + time + "s");
            logger.debug("  AbbreviatedDataFlowGraph:");
            InfoFlowAnalysis.printInfoFlowSummary(this.abbreviatedInfoFlowGraph);
            logger.debug("  DataFlowSummary:");
            InfoFlowAnalysis.printInfoFlowSummary(this.infoFlowSummary);
        }
    }

    public void generateAbbreviatedInfoFlowGraph() {
        Iterator<Unit> stmtIt = this.graph.iterator();
        while (stmtIt.hasNext()) {
            Stmt s = (Stmt) stmtIt.next();
            addFlowToCdfg(s);
        }
    }

    public void generateInfoFlowSummary() {
        Iterator<EquivalentValue> nodeIt = this.infoFlowSummary.iterator();
        while (nodeIt.hasNext()) {
            EquivalentValue node = nodeIt.next();
            List<EquivalentValue> sources = sourcesOf(node);
            for (EquivalentValue source : sources) {
                if (source.getValue() instanceof Ref) {
                    this.infoFlowSummary.addEdge(source, node);
                }
            }
        }
    }

    public List<EquivalentValue> sourcesOf(EquivalentValue node) {
        return sourcesOf(node, new HashSet(), new HashSet());
    }

    private List<EquivalentValue> sourcesOf(EquivalentValue node, Set<EquivalentValue> visitedSources, Set<EquivalentValue> visitedSinks) {
        visitedSources.add(node);
        List<EquivalentValue> ret = new LinkedList<>();
        if (!this.abbreviatedInfoFlowGraph.containsNode(node)) {
            return ret;
        }
        Set<EquivalentValue> preds = this.abbreviatedInfoFlowGraph.getPredsOfAsSet(node);
        for (EquivalentValue pred : preds) {
            if (!visitedSources.contains(pred)) {
                ret.add(pred);
                ret.addAll(sourcesOf(pred, visitedSources, visitedSinks));
            }
        }
        List<EquivalentValue> sinks = sinksOf(node, visitedSources, visitedSinks);
        for (EquivalentValue sink : sinks) {
            if (!visitedSources.contains(sink)) {
                EquivalentValue flowsToSourcesOf = new CachedEquivalentValue(new AbstractDataSource(sink.getValue()));
                if (this.abbreviatedInfoFlowGraph.getPredsOfAsSet(sink).contains(flowsToSourcesOf)) {
                    ret.addAll(sourcesOf(flowsToSourcesOf, visitedSources, visitedSinks));
                }
            }
        }
        return ret;
    }

    public List<EquivalentValue> sinksOf(EquivalentValue node) {
        return sinksOf(node, new HashSet(), new HashSet());
    }

    private List<EquivalentValue> sinksOf(EquivalentValue node, Set<EquivalentValue> visitedSources, Set<EquivalentValue> visitedSinks) {
        List<EquivalentValue> ret = new LinkedList<>();
        visitedSinks.add(node);
        if (!this.abbreviatedInfoFlowGraph.containsNode(node)) {
            return ret;
        }
        Set<EquivalentValue> succs = this.abbreviatedInfoFlowGraph.getSuccsOfAsSet(node);
        for (EquivalentValue succ : succs) {
            if (!visitedSinks.contains(succ)) {
                ret.add(succ);
                ret.addAll(sinksOf(succ, visitedSources, visitedSinks));
            }
        }
        for (EquivalentValue succ2 : succs) {
            if (succ2.getValue() instanceof AbstractDataSource) {
                Set vHolder = this.abbreviatedInfoFlowGraph.getSuccsOfAsSet(succ2);
                EquivalentValue v = vHolder.iterator().next();
                if (!visitedSinks.contains(v)) {
                    ret.addAll(sourcesOf(v, visitedSinks, visitedSinks));
                }
            }
        }
        return ret;
    }

    public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary() {
        return this.infoFlowSummary;
    }

    public HashMutableDirectedGraph<EquivalentValue> getMethodAbbreviatedInfoFlowGraph() {
        return this.abbreviatedInfoFlowGraph;
    }

    protected boolean isNonRefType(Type type) {
        return !(type instanceof RefLikeType);
    }

    protected boolean ignoreThisDataType(Type type) {
        return this.refOnly && isNonRefType(type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void handleFlowsToValue(Value sink, Value source) {
        EquivalentValue sinkEqVal;
        EquivalentValue sourceEqVal;
        if (sink instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef) sink;
            sinkEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, ifr.getField(), (Local) ifr.getBase());
        } else {
            sinkEqVal = new CachedEquivalentValue(sink);
        }
        if (source instanceof InstanceFieldRef) {
            InstanceFieldRef ifr2 = (InstanceFieldRef) source;
            sourceEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, ifr2.getField(), (Local) ifr2.getBase());
        } else {
            sourceEqVal = new CachedEquivalentValue(source);
        }
        if ((source instanceof Ref) && !this.infoFlowSummary.containsNode(sourceEqVal)) {
            this.infoFlowSummary.addNode(sourceEqVal);
        }
        if ((sink instanceof Ref) && !this.infoFlowSummary.containsNode(sinkEqVal)) {
            this.infoFlowSummary.addNode(sinkEqVal);
        }
        if (!this.abbreviatedInfoFlowGraph.containsNode(sinkEqVal)) {
            this.abbreviatedInfoFlowGraph.addNode(sinkEqVal);
        }
        if (!this.abbreviatedInfoFlowGraph.containsNode(sourceEqVal)) {
            this.abbreviatedInfoFlowGraph.addNode(sourceEqVal);
        }
        this.abbreviatedInfoFlowGraph.addEdge(sourceEqVal, sinkEqVal);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void handleFlowsToDataStructure(Value base, Value source) {
        EquivalentValue sourceEqVal;
        EquivalentValue sourcesOfBaseEqVal = new CachedEquivalentValue(new AbstractDataSource(base));
        EquivalentValue baseEqVal = new CachedEquivalentValue(base);
        if (source instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef) source;
            sourceEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.sm, ifr.getField(), (Local) ifr.getBase());
        } else {
            sourceEqVal = new CachedEquivalentValue(source);
        }
        if ((source instanceof Ref) && !this.infoFlowSummary.containsNode(sourceEqVal)) {
            this.infoFlowSummary.addNode(sourceEqVal);
        }
        if (!this.abbreviatedInfoFlowGraph.containsNode(baseEqVal)) {
            this.abbreviatedInfoFlowGraph.addNode(baseEqVal);
        }
        if (!this.abbreviatedInfoFlowGraph.containsNode(sourceEqVal)) {
            this.abbreviatedInfoFlowGraph.addNode(sourceEqVal);
        }
        if (!this.abbreviatedInfoFlowGraph.containsNode(sourcesOfBaseEqVal)) {
            this.abbreviatedInfoFlowGraph.addNode(sourcesOfBaseEqVal);
        }
        this.abbreviatedInfoFlowGraph.addEdge(sourceEqVal, sourcesOfBaseEqVal);
        this.abbreviatedInfoFlowGraph.addEdge(sourcesOfBaseEqVal, baseEqVal);
    }

    protected void handleInnerField(Value innerFieldRef) {
    }

    protected List<Value> handleInvokeExpr(InvokeExpr ie, Stmt is) {
        HashMutableDirectedGraph<EquivalentValue> dataFlowSummary = this.dfa.getInvokeInfoFlowSummary(ie, is, this.sm);
        List<Value> returnValueSources = new ArrayList<>();
        for (EquivalentValue nodeEqVal : dataFlowSummary.getNodes()) {
            if (!(nodeEqVal.getValue() instanceof Ref)) {
                throw new RuntimeException("Illegal node type in data flow summary:" + nodeEqVal.getValue() + " should be an object of type Ref.");
            }
            Value node = (Ref) nodeEqVal.getValue();
            List<Value> sources = new ArrayList<>();
            if (node instanceof ParameterRef) {
                ParameterRef param = (ParameterRef) node;
                if (param.getIndex() != -1) {
                    sources.add(ie.getArg(param.getIndex()));
                }
            } else if (node instanceof StaticFieldRef) {
                sources.add(node);
            } else if ((node instanceof InstanceFieldRef) && (ie instanceof InstanceInvokeExpr)) {
                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                if (iie.getBase() == this.thisLocal) {
                    sources.add(node);
                } else if (this.includeInnerFields) {
                    InstanceFieldRef ifr = (InstanceFieldRef) node;
                    if (!(ifr.getBase() instanceof FakeJimpleLocal)) {
                        sources.add(ifr.getBase());
                    }
                    sources.add(node);
                } else {
                    sources.add(iie.getBase());
                }
            } else if ((node instanceof InstanceFieldRef) && this.includeInnerFields) {
                InstanceFieldRef ifr2 = (InstanceFieldRef) node;
                if (!(ifr2.getBase() instanceof FakeJimpleLocal)) {
                    sources.add(ifr2.getBase());
                }
                sources.add(node);
            } else if ((node instanceof ThisRef) && (ie instanceof InstanceInvokeExpr)) {
                sources.add(((InstanceInvokeExpr) ie).getBase());
            } else {
                throw new RuntimeException("Unknown Node Type in Data Flow Graph: node " + node + " in InvokeExpr " + ie);
            }
            for (EquivalentValue sinkEqVal : dataFlowSummary.getSuccsOfAsSet(nodeEqVal)) {
                Ref sink = (Ref) sinkEqVal.getValue();
                if (sink instanceof ParameterRef) {
                    ParameterRef param2 = (ParameterRef) sink;
                    if (param2.getIndex() == -1) {
                        returnValueSources.addAll(sources);
                    } else {
                        for (Value source : sources) {
                            handleFlowsToDataStructure(ie.getArg(param2.getIndex()), source);
                        }
                    }
                } else if (sink instanceof StaticFieldRef) {
                    for (Value source2 : sources) {
                        handleFlowsToValue(sink, source2);
                    }
                } else if ((sink instanceof InstanceFieldRef) && (ie instanceof InstanceInvokeExpr)) {
                    InstanceInvokeExpr iie2 = (InstanceInvokeExpr) ie;
                    if (iie2.getBase() == this.thisLocal) {
                        for (Value source3 : sources) {
                            handleFlowsToValue(sink, source3);
                        }
                    } else if (this.includeInnerFields) {
                        for (Value source4 : sources) {
                            handleFlowsToValue(sink, source4);
                            handleInnerField(sink);
                        }
                    } else {
                        for (Value source5 : sources) {
                            handleFlowsToDataStructure(iie2.getBase(), source5);
                        }
                    }
                } else if ((sink instanceof InstanceFieldRef) && this.includeInnerFields) {
                    for (Value source6 : sources) {
                        handleFlowsToValue(sink, source6);
                        handleInnerField(sink);
                    }
                }
            }
        }
        return returnValueSources;
    }

    protected void addFlowToCdfg(Stmt stmt) {
        if (stmt instanceof IdentityStmt) {
            IdentityStmt is = (IdentityStmt) stmt;
            IdentityRef ir = (IdentityRef) is.getRightOp();
            if (!(ir instanceof JCaughtExceptionRef)) {
                if (ir instanceof ParameterRef) {
                    if (!ignoreThisDataType(ir.getType())) {
                        handleFlowsToValue(is.getLeftOp(), ir);
                    }
                } else if ((ir instanceof ThisRef) && !ignoreThisDataType(ir.getType())) {
                    handleFlowsToValue(is.getLeftOp(), ir);
                }
            }
        } else if (stmt instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt) stmt;
            Value rv = rs.getOp();
            if (!(rv instanceof Constant) && (rv instanceof Local) && !ignoreThisDataType(rv.getType())) {
                handleFlowsToValue(this.returnRef, rv);
            }
        } else if (!(stmt instanceof AssignStmt)) {
            if (stmt.containsInvokeExpr()) {
                handleInvokeExpr(stmt.getInvokeExpr(), stmt);
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
                } else if (this.includeInnerFields) {
                    sink = lv;
                    handleInnerField(sink);
                } else {
                    sink = ifr.getBase();
                    flowsToDataStructure = true;
                }
            }
            List sources = new ArrayList();
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
                } else if (this.includeInnerFields) {
                    sources.add(ifr2.getBase());
                    sources.add(rv2);
                    handleInnerField(rv2);
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
                sources.addAll(handleInvokeExpr(ie, as));
                interestingFlow = !ignoreThisDataType(ie.getType());
            }
            if (interestingFlow) {
                if (flowsToDataStructure) {
                    for (Value source : sources) {
                        handleFlowsToDataStructure(sink, source);
                    }
                    return;
                }
                for (Value source2 : sources) {
                    handleFlowsToValue(sink, source2);
                }
            }
        }
    }

    public Value getThisLocal() {
        return this.thisLocal;
    }
}
