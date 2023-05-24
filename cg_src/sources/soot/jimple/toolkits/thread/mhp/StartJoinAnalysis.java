package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Hierarchy;
import soot.JavaMethods;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.jimple.toolkits.pointer.LocalMustAliasAnalysis;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/StartJoinAnalysis.class */
public class StartJoinAnalysis extends ForwardFlowAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(StartJoinAnalysis.class);
    Set<Stmt> startStatements;
    Set<Stmt> joinStatements;
    Hierarchy hierarchy;
    Map<Stmt, List<SootMethod>> startToRunMethods;
    Map<Stmt, List<AllocNode>> startToAllocNodes;
    Map<Stmt, Stmt> startToJoin;

    public StartJoinAnalysis(UnitGraph g, SootMethod sm, CallGraph callGraph, PAG pag) {
        super(g);
        this.startStatements = new HashSet();
        this.joinStatements = new HashSet();
        this.hierarchy = Scene.v().getActiveHierarchy();
        this.startToRunMethods = new HashMap();
        this.startToAllocNodes = new HashMap();
        this.startToJoin = new HashMap();
        doFlowInsensitiveSingleIterationAnalysis();
        if (!this.startStatements.isEmpty()) {
            MHGPostDominatorsFinder pd = new MHGPostDominatorsFinder(new BriefUnitGraph(sm.getActiveBody()));
            LocalMustAliasAnalysis lma = new LocalMustAliasAnalysis(g);
            TransitiveTargets runMethodTargets = new TransitiveTargets(callGraph, new Filter(new RunMethodsPred()));
            for (Stmt start : this.startStatements) {
                List<SootMethod> runMethodsList = new ArrayList<>();
                List<AllocNode> allocNodesList = new ArrayList<>();
                Value startObject = ((InstanceInvokeExpr) start.getInvokeExpr()).getBase();
                PointsToSetInternal pts = (PointsToSetInternal) pag.reachingObjects((Local) startObject);
                List<AllocNode> mayAlias = getMayAliasList(pts);
                if (mayAlias.size() >= 1) {
                    Iterator<MethodOrMethodContext> mayRunIt = runMethodTargets.iterator(start);
                    while (mayRunIt.hasNext()) {
                        SootMethod runMethod = (SootMethod) mayRunIt.next();
                        if (runMethod.getSubSignature().equals(JavaMethods.SIG_RUN)) {
                            runMethodsList.add(runMethod);
                        }
                    }
                    if (runMethodsList.isEmpty() && ((RefType) startObject.getType()).getSootClass().isApplicationClass()) {
                        List<SootClass> threadClasses = this.hierarchy.getSubclassesOfIncluding(((RefType) startObject.getType()).getSootClass());
                        for (SootClass currentClass : threadClasses) {
                            SootMethod currentMethod = currentClass.getMethodUnsafe(JavaMethods.SIG_RUN);
                            if (currentMethod != null) {
                                runMethodsList.add(currentMethod);
                            }
                        }
                    }
                    for (AllocNode allocNode : mayAlias) {
                        allocNodesList.add(allocNode);
                        if (runMethodsList.isEmpty()) {
                            throw new RuntimeException("Can't find run method for: " + startObject);
                        }
                    }
                    this.startToRunMethods.put(start, runMethodsList);
                    this.startToAllocNodes.put(start, allocNodesList);
                    for (Stmt join : this.joinStatements) {
                        Value joinObject = ((InstanceInvokeExpr) join.getInvokeExpr()).getBase();
                        List barriers = new ArrayList();
                        barriers.addAll(g.getSuccsOf((Unit) join));
                        if (lma.mustAlias((Local) startObject, start, (Local) joinObject, join) && pd.getDominators(start).contains(join)) {
                            this.startToJoin.put(start, join);
                        }
                    }
                }
            }
        }
    }

    private List<AllocNode> getMayAliasList(PointsToSetInternal pts) {
        List<AllocNode> list = new ArrayList<>();
        final HashSet<AllocNode> ret = new HashSet<>();
        pts.forall(new P2SetVisitor() { // from class: soot.jimple.toolkits.thread.mhp.StartJoinAnalysis.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public void visit(Node n) {
                ret.add((AllocNode) n);
            }
        });
        Iterator<AllocNode> it = ret.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public Set<Stmt> getStartStatements() {
        return this.startStatements;
    }

    public Set<Stmt> getJoinStatements() {
        return this.joinStatements;
    }

    public Map<Stmt, List<SootMethod>> getStartToRunMethods() {
        return this.startToRunMethods;
    }

    public Map<Stmt, List<AllocNode>> getStartToAllocNodes() {
        return this.startToAllocNodes;
    }

    public Map<Stmt, Stmt> getStartToJoin() {
        return this.startToJoin;
    }

    public void doFlowInsensitiveSingleIterationAnalysis() {
        FlowSet fs = (FlowSet) newInitialFlow();
        for (Stmt s : this.graph) {
            flowThrough(fs, s, fs);
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    protected void merge(Object in1, Object in2, Object out) {
        FlowSet inSet1 = (FlowSet) in1;
        FlowSet inSet2 = (FlowSet) in2;
        FlowSet outSet = (FlowSet) out;
        inSet1.intersection(inSet2, outSet);
    }

    @Override // soot.toolkits.scalar.FlowAnalysis
    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        Stmt stmt = (Stmt) unit;
        if (stmt.containsInvokeExpr()) {
            InvokeExpr ie = stmt.getInvokeExpr();
            if (ie instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                SootMethod invokeMethod = ie.getMethod();
                if (invokeMethod.getName().equals("start")) {
                    RefType baseType = (RefType) iie.getBase().getType();
                    if (!baseType.getSootClass().isInterface()) {
                        List<SootClass> superClasses = this.hierarchy.getSuperclassesOfIncluding(baseType.getSootClass());
                        for (SootClass sootClass : superClasses) {
                            if (sootClass.getName().equals("java.lang.Thread") && !this.startStatements.contains(stmt)) {
                                this.startStatements.add(stmt);
                            }
                        }
                    }
                }
                if (invokeMethod.getName().equals("join")) {
                    RefType baseType2 = (RefType) iie.getBase().getType();
                    if (!baseType2.getSootClass().isInterface()) {
                        List<SootClass> superClasses2 = this.hierarchy.getSuperclassesOfIncluding(baseType2.getSootClass());
                        for (SootClass sootClass2 : superClasses2) {
                            if (sootClass2.getName().equals("java.lang.Thread") && !this.joinStatements.contains(stmt)) {
                                this.joinStatements.add(stmt);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(Object source, Object dest) {
        FlowSet sourceSet = (FlowSet) source;
        FlowSet destSet = (FlowSet) dest;
        sourceSet.copy(destSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object entryInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object newInitialFlow() {
        return new ArraySparseSet();
    }
}
