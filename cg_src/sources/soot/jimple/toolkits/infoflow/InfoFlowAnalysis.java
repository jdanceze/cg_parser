package soot.jimple.toolkits.infoflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/InfoFlowAnalysis.class */
public class InfoFlowAnalysis {
    boolean includePrimitiveInfoFlow;
    boolean includeInnerFields;
    boolean printDebug;
    Map<SootClass, ClassInfoFlowAnalysis> classToClassInfoFlowAnalysis;
    private static final Logger logger = LoggerFactory.getLogger(InfoFlowAnalysis.class);
    static int nodecount = 0;

    public InfoFlowAnalysis(boolean includePrimitiveDataFlow, boolean includeInnerFields) {
        this(includePrimitiveDataFlow, includeInnerFields, false);
    }

    public InfoFlowAnalysis(boolean includePrimitiveDataFlow, boolean includeInnerFields, boolean printDebug) {
        this.includePrimitiveInfoFlow = includePrimitiveDataFlow;
        this.includeInnerFields = includeInnerFields;
        this.printDebug = printDebug;
        this.classToClassInfoFlowAnalysis = new HashMap();
    }

    public boolean includesPrimitiveInfoFlow() {
        return this.includePrimitiveInfoFlow;
    }

    public boolean includesInnerFields() {
        return this.includeInnerFields;
    }

    public boolean printDebug() {
        return this.printDebug;
    }

    private ClassInfoFlowAnalysis getClassInfoFlowAnalysis(SootClass sc) {
        if (!this.classToClassInfoFlowAnalysis.containsKey(sc)) {
            ClassInfoFlowAnalysis cdfa = new ClassInfoFlowAnalysis(sc, this);
            this.classToClassInfoFlowAnalysis.put(sc, cdfa);
        }
        return this.classToClassInfoFlowAnalysis.get(sc);
    }

    public SmartMethodInfoFlowAnalysis getMethodInfoFlowAnalysis(SootMethod sm) {
        ClassInfoFlowAnalysis cdfa = getClassInfoFlowAnalysis(sm.getDeclaringClass());
        return cdfa.getMethodInfoFlowAnalysis(sm);
    }

    public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod sm) {
        return getMethodInfoFlowSummary(sm, true);
    }

    public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod sm, boolean doFullAnalysis) {
        ClassInfoFlowAnalysis cdfa = getClassInfoFlowAnalysis(sm.getDeclaringClass());
        return cdfa.getMethodInfoFlowSummary(sm, doFullAnalysis);
    }

    public static EquivalentValue getNodeForFieldRef(SootMethod sm, SootField sf) {
        return getNodeForFieldRef(sm, sf, null);
    }

    public static EquivalentValue getNodeForFieldRef(SootMethod sm, SootField sf, Local realLocal) {
        if (sf.isStatic()) {
            return new CachedEquivalentValue(Jimple.v().newStaticFieldRef(sf.makeRef()));
        }
        if (sm.isConcrete() && !sm.isStatic() && sm.getDeclaringClass() == sf.getDeclaringClass() && realLocal == null) {
            JimpleLocal fakethis = new FakeJimpleLocal("fakethis", sf.getDeclaringClass().getType(), sm.retrieveActiveBody().getThisLocal());
            return new CachedEquivalentValue(Jimple.v().newInstanceFieldRef(fakethis, sf.makeRef()));
        }
        JimpleLocal fakethis2 = new FakeJimpleLocal("fakethis", sf.getDeclaringClass().getType(), realLocal);
        return new CachedEquivalentValue(Jimple.v().newInstanceFieldRef(fakethis2, sf.makeRef()));
    }

    public static EquivalentValue getNodeForParameterRef(SootMethod sm, int i) {
        return new CachedEquivalentValue(new ParameterRef(sm.getParameterType(i), i));
    }

    public static EquivalentValue getNodeForReturnRef(SootMethod sm) {
        return new CachedEquivalentValue(new ParameterRef(sm.getReturnType(), -1));
    }

    public static EquivalentValue getNodeForThisRef(SootMethod sm) {
        return new CachedEquivalentValue(new ThisRef(sm.getDeclaringClass().getType()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HashMutableDirectedGraph<EquivalentValue> getInvokeInfoFlowSummary(InvokeExpr ie, Stmt is, SootMethod context) {
        HashMutableDirectedGraph<EquivalentValue> ret = null;
        SootMethodRef methodRef = ie.getMethodRef();
        String subSig = methodRef.resolve().getSubSignature();
        CallGraph cg = Scene.v().getCallGraph();
        Iterator<Edge> edges = cg.edgesOutOf(is);
        while (edges.hasNext()) {
            Edge e = edges.next();
            SootMethod target = e.getTgt().method();
            if (target.getSubSignature().equals(subSig)) {
                HashMutableDirectedGraph<EquivalentValue> ifs = getMethodInfoFlowSummary(target, context.getDeclaringClass().isApplicationClass());
                if (ret == null) {
                    ret = ifs;
                } else {
                    for (EquivalentValue node : ifs.getNodes()) {
                        if (!ret.containsNode(node)) {
                            ret.addNode(node);
                        }
                        for (EquivalentValue succ : ifs.getSuccsOf(node)) {
                            ret.addEdge(node, succ);
                        }
                    }
                }
            }
        }
        return ret;
    }

    protected MutableDirectedGraph<EquivalentValue> getInvokeAbbreviatedInfoFlowGraph(InvokeExpr ie, SootMethod context) {
        SootMethodRef methodRef = ie.getMethodRef();
        return getMethodInfoFlowAnalysis(methodRef.resolve()).getMethodAbbreviatedInfoFlowGraph();
    }

    public static void printInfoFlowSummary(DirectedGraph<EquivalentValue> g) {
        if (g.size() > 0) {
            logger.debug("     --> ");
        }
        for (EquivalentValue node : g) {
            List<EquivalentValue> sources = g.getPredsOf(node);
            if (!sources.isEmpty()) {
                logger.debug("    [ ");
                int sourcesnamelength = 0;
                int lastnamelength = 0;
                int idx = 0;
                for (EquivalentValue t : sources) {
                    Value v = t.getValue();
                    if (v instanceof FieldRef) {
                        FieldRef fr = (FieldRef) v;
                        String name = fr.getFieldRef().name();
                        lastnamelength = name.length();
                        if (lastnamelength > sourcesnamelength) {
                            sourcesnamelength = lastnamelength;
                        }
                        logger.debug(name);
                    } else if (v instanceof ParameterRef) {
                        ParameterRef pr = (ParameterRef) v;
                        lastnamelength = 11;
                        if (11 > sourcesnamelength) {
                            sourcesnamelength = 11;
                        }
                        logger.debug("@parameter" + pr.getIndex());
                    } else {
                        String name2 = v.toString();
                        lastnamelength = name2.length();
                        if (lastnamelength > sourcesnamelength) {
                            sourcesnamelength = lastnamelength;
                        }
                        logger.debug(name2);
                    }
                    int i = idx;
                    idx++;
                    if (i < sources.size()) {
                        logger.debug("\n      ");
                    }
                }
                for (int i2 = 0; i2 < sourcesnamelength - lastnamelength; i2++) {
                    logger.debug(Instruction.argsep);
                }
                logger.debug(" ] --> " + node.toString());
            }
        }
    }

    public static void printGraphToDotFile(String filename, DirectedGraph<EquivalentValue> graph, String graphname, boolean onePage) {
        nodecount = 0;
        DotGraph canvas = new DotGraph(filename);
        if (!onePage) {
            canvas.setPageSize(8.5d, 11.0d);
        }
        canvas.setNodeShape(DotGraphConstants.NODE_SHAPE_BOX);
        canvas.setGraphLabel(graphname);
        for (EquivalentValue node : graph) {
            canvas.drawNode(getNodeName(node));
            canvas.getNode(getNodeName(node)).setLabel(getNodeLabel(node));
            for (EquivalentValue s : graph.getSuccsOf(node)) {
                canvas.drawNode(getNodeName(s));
                canvas.getNode(getNodeName(s)).setLabel(getNodeLabel(s));
                canvas.drawEdge(getNodeName(node), getNodeName(s));
            }
        }
        canvas.plot(String.valueOf(filename) + DotGraph.DOT_EXTENSION);
    }

    public static String getNodeName(Object o) {
        return getNodeLabel(o);
    }

    public static String getNodeLabel(Object o) {
        Value node = ((EquivalentValue) o).getValue();
        if (node instanceof FieldRef) {
            FieldRef fr = (FieldRef) node;
            return String.valueOf(fr.getField().getDeclaringClass().getShortName()) + "." + fr.getFieldRef().name();
        }
        return node.toString();
    }
}
