package soot.jimple.toolkits.infoflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.FieldRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.MemoryEfficientGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/ClassInfoFlowAnalysis.class */
public class ClassInfoFlowAnalysis {
    SootClass sootClass;
    InfoFlowAnalysis dfa;
    Map<SootMethod, SmartMethodInfoFlowAnalysis> methodToInfoFlowAnalysis = new HashMap();
    Map<SootMethod, HashMutableDirectedGraph<EquivalentValue>> methodToInfoFlowSummary = new HashMap();
    private static final Logger logger = LoggerFactory.getLogger(ClassInfoFlowAnalysis.class);
    public static int methodCount = 0;

    public ClassInfoFlowAnalysis(SootClass sootClass, InfoFlowAnalysis dfa) {
        this.sootClass = sootClass;
        this.dfa = dfa;
    }

    public SmartMethodInfoFlowAnalysis getMethodInfoFlowAnalysis(SootMethod method) {
        if (!this.methodToInfoFlowAnalysis.containsKey(method)) {
            methodCount++;
            if (!this.methodToInfoFlowSummary.containsKey(method)) {
                HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = simpleConservativeInfoFlowAnalysis(method);
                this.methodToInfoFlowSummary.put(method, dataFlowGraph);
            }
            if (method.isConcrete()) {
                Body b = method.retrieveActiveBody();
                UnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
                SmartMethodInfoFlowAnalysis smdfa = new SmartMethodInfoFlowAnalysis(g, this.dfa);
                this.methodToInfoFlowAnalysis.put(method, smdfa);
                this.methodToInfoFlowSummary.remove(method);
                this.methodToInfoFlowSummary.put(method, smdfa.getMethodInfoFlowSummary());
                return smdfa;
            }
        }
        return this.methodToInfoFlowAnalysis.get(method);
    }

    public MutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod method) {
        return getMethodInfoFlowSummary(method, true);
    }

    public HashMutableDirectedGraph<EquivalentValue> getMethodInfoFlowSummary(SootMethod method, boolean doFullAnalysis) {
        if (!this.methodToInfoFlowSummary.containsKey(method)) {
            methodCount++;
            HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = simpleConservativeInfoFlowAnalysis(method);
            this.methodToInfoFlowSummary.put(method, dataFlowGraph);
            if (method.isConcrete() && doFullAnalysis) {
                Body b = method.retrieveActiveBody();
                UnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
                SmartMethodInfoFlowAnalysis smdfa = new SmartMethodInfoFlowAnalysis(g, this.dfa);
                this.methodToInfoFlowAnalysis.put(method, smdfa);
                this.methodToInfoFlowSummary.remove(method);
                this.methodToInfoFlowSummary.put(method, smdfa.getMethodInfoFlowSummary());
            }
        }
        return this.methodToInfoFlowSummary.get(method);
    }

    private HashMutableDirectedGraph<EquivalentValue> simpleConservativeInfoFlowAnalysis(SootMethod sm) {
        if (!sm.isConcrete()) {
            return triviallyConservativeInfoFlowAnalysis(sm);
        }
        Body b = sm.retrieveActiveBody();
        UnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
        HashSet<EquivalentValue> fieldsStaticsParamsAccessed = new HashSet<>();
        Iterator<Unit> it = g.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt s = (Stmt) u;
            if (s instanceof IdentityStmt) {
                IdentityStmt is = (IdentityStmt) s;
                IdentityRef ir = (IdentityRef) is.getRightOp();
                if (ir instanceof ParameterRef) {
                    ParameterRef pr = (ParameterRef) ir;
                    fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForParameterRef(sm, pr.getIndex()));
                }
            }
            if (s.containsFieldRef()) {
                FieldRef ref = s.getFieldRef();
                if (ref instanceof StaticFieldRef) {
                    StaticFieldRef sfr = (StaticFieldRef) ref;
                    fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForFieldRef(sm, sfr.getField()));
                } else if (ref instanceof InstanceFieldRef) {
                    InstanceFieldRef ifr = (InstanceFieldRef) ref;
                    Value base = ifr.getBase();
                    if ((base instanceof Local) && (this.dfa.includesInnerFields() || (!sm.isStatic() && base.equivTo(b.getThisLocal())))) {
                        fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForFieldRef(sm, ifr.getField()));
                    }
                }
            }
        }
        HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = new MemoryEfficientGraph<>();
        Iterator<EquivalentValue> accessedIt1 = fieldsStaticsParamsAccessed.iterator();
        while (accessedIt1.hasNext()) {
            EquivalentValue o = accessedIt1.next();
            dataFlowGraph.addNode(o);
        }
        for (int i = 0; i < sm.getParameterCount(); i++) {
            EquivalentValue parameterRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(sm, i);
            if (!dataFlowGraph.containsNode(parameterRefEqVal)) {
                dataFlowGraph.addNode(parameterRefEqVal);
            }
        }
        for (SootField sf : sm.getDeclaringClass().getFields()) {
            if (sf.isStatic() || !sm.isStatic()) {
                EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
                if (!dataFlowGraph.containsNode(fieldRefEqVal)) {
                    dataFlowGraph.addNode(fieldRefEqVal);
                }
            }
        }
        SootClass superclass = sm.getDeclaringClass();
        if (superclass.hasSuperclass()) {
            superclass = sm.getDeclaringClass().getSuperclass();
        }
        while (superclass.hasSuperclass()) {
            for (SootField scField : superclass.getFields()) {
                if (scField.isStatic() || !sm.isStatic()) {
                    EquivalentValue fieldRefEqVal2 = InfoFlowAnalysis.getNodeForFieldRef(sm, scField);
                    if (!dataFlowGraph.containsNode(fieldRefEqVal2)) {
                        dataFlowGraph.addNode(fieldRefEqVal2);
                    }
                }
            }
            superclass = superclass.getSuperclass();
        }
        ParameterRef returnValueRef = null;
        if (sm.getReturnType() != VoidType.v()) {
            returnValueRef = new ParameterRef(sm.getReturnType(), -1);
            dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForReturnRef(sm));
        }
        if (!sm.isStatic()) {
            dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForThisRef(sm));
            fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForThisRef(sm));
        }
        Iterator<EquivalentValue> accessedIt12 = fieldsStaticsParamsAccessed.iterator();
        while (accessedIt12.hasNext()) {
            EquivalentValue r = accessedIt12.next();
            Ref rRef = (Ref) r.getValue();
            if ((rRef.getType() instanceof RefLikeType) || this.dfa.includesPrimitiveInfoFlow()) {
                Iterator<EquivalentValue> accessedIt2 = fieldsStaticsParamsAccessed.iterator();
                while (accessedIt2.hasNext()) {
                    EquivalentValue s2 = accessedIt2.next();
                    Ref sRef = (Ref) s2.getValue();
                    if (!(rRef instanceof ThisRef) || !(sRef instanceof InstanceFieldRef)) {
                        if (!(sRef instanceof ThisRef) || !(rRef instanceof InstanceFieldRef)) {
                            if (!(sRef instanceof ParameterRef) || !this.dfa.includesInnerFields()) {
                                if (sRef.getType() instanceof RefLikeType) {
                                    dataFlowGraph.addEdge(r, s2);
                                }
                            }
                        }
                    }
                }
                if (returnValueRef != null && ((returnValueRef.getType() instanceof RefLikeType) || this.dfa.includesPrimitiveInfoFlow())) {
                    dataFlowGraph.addEdge(r, InfoFlowAnalysis.getNodeForReturnRef(sm));
                }
            }
        }
        return dataFlowGraph;
    }

    public HashMutableDirectedGraph<EquivalentValue> triviallyConservativeInfoFlowAnalysis(SootMethod sm) {
        HashSet<EquivalentValue> fieldsStaticsParamsAccessed = new HashSet<>();
        for (int i = 0; i < sm.getParameterCount(); i++) {
            EquivalentValue parameterRefEqVal = InfoFlowAnalysis.getNodeForParameterRef(sm, i);
            fieldsStaticsParamsAccessed.add(parameterRefEqVal);
        }
        for (SootField sf : sm.getDeclaringClass().getFields()) {
            if (sf.isStatic() || !sm.isStatic()) {
                EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
                fieldsStaticsParamsAccessed.add(fieldRefEqVal);
            }
        }
        SootClass superclass = sm.getDeclaringClass();
        if (superclass.hasSuperclass()) {
            superclass = sm.getDeclaringClass().getSuperclass();
        }
        while (superclass.hasSuperclass()) {
            for (SootField scField : superclass.getFields()) {
                if (scField.isStatic() || !sm.isStatic()) {
                    EquivalentValue fieldRefEqVal2 = InfoFlowAnalysis.getNodeForFieldRef(sm, scField);
                    fieldsStaticsParamsAccessed.add(fieldRefEqVal2);
                }
            }
            superclass = superclass.getSuperclass();
        }
        HashMutableDirectedGraph<EquivalentValue> dataFlowGraph = new MemoryEfficientGraph<>();
        Iterator<EquivalentValue> accessedIt1 = fieldsStaticsParamsAccessed.iterator();
        while (accessedIt1.hasNext()) {
            EquivalentValue o = accessedIt1.next();
            dataFlowGraph.addNode(o);
        }
        ParameterRef returnValueRef = null;
        if (sm.getReturnType() != VoidType.v()) {
            returnValueRef = new ParameterRef(sm.getReturnType(), -1);
            dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForReturnRef(sm));
        }
        if (!sm.isStatic()) {
            dataFlowGraph.addNode(InfoFlowAnalysis.getNodeForThisRef(sm));
            fieldsStaticsParamsAccessed.add(InfoFlowAnalysis.getNodeForThisRef(sm));
        }
        Iterator<EquivalentValue> accessedIt12 = fieldsStaticsParamsAccessed.iterator();
        while (accessedIt12.hasNext()) {
            EquivalentValue r = accessedIt12.next();
            Ref rRef = (Ref) r.getValue();
            if ((rRef.getType() instanceof RefLikeType) || this.dfa.includesPrimitiveInfoFlow()) {
                Iterator<EquivalentValue> accessedIt2 = fieldsStaticsParamsAccessed.iterator();
                while (accessedIt2.hasNext()) {
                    EquivalentValue s = accessedIt2.next();
                    Ref sRef = (Ref) s.getValue();
                    if (!(rRef instanceof ThisRef) || !(sRef instanceof InstanceFieldRef)) {
                        if (!(sRef instanceof ThisRef) || !(rRef instanceof InstanceFieldRef)) {
                            if (sRef.getType() instanceof RefLikeType) {
                                dataFlowGraph.addEdge(r, s);
                            }
                        }
                    }
                }
                if (returnValueRef != null && ((returnValueRef.getType() instanceof RefLikeType) || this.dfa.includesPrimitiveInfoFlow())) {
                    dataFlowGraph.addEdge(r, InfoFlowAnalysis.getNodeForReturnRef(sm));
                }
            }
        }
        return dataFlowGraph;
    }
}
