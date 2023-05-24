package soot.jimple.toolkits.thread.mhp.findobject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.TargetMethodsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/findobject/MultiRunStatementsFinder.class */
public class MultiRunStatementsFinder extends ForwardFlowAnalysis<Unit, BitSet> {
    Set<Unit> multiRunStatements;
    protected Map<Object, Integer> nodeToIndex;
    protected int lastIndex;

    public MultiRunStatementsFinder(UnitGraph g, SootMethod sm, Set<SootMethod> multiCalledMethods, CallGraph cg) {
        super(g);
        this.multiRunStatements = new HashSet();
        this.lastIndex = 0;
        this.nodeToIndex = new HashMap();
        doAnalysis();
        findMultiCalledMethodsIntra(multiCalledMethods, cg);
    }

    private void findMultiCalledMethodsIntra(Set<SootMethod> multiCalledMethods, CallGraph callGraph) {
        Iterator<Unit> it = this.multiRunStatements.iterator();
        while (it.hasNext()) {
            Stmt stmt = (Stmt) it.next();
            if (stmt.containsInvokeExpr()) {
                InvokeExpr invokeExpr = stmt.getInvokeExpr();
                List<SootMethod> targetList = new ArrayList<>();
                SootMethod method = invokeExpr.getMethod();
                if (invokeExpr instanceof StaticInvokeExpr) {
                    targetList.add(method);
                } else if ((invokeExpr instanceof InstanceInvokeExpr) && method.isConcrete() && !method.getDeclaringClass().isLibraryClass()) {
                    TargetMethodsFinder tmd = new TargetMethodsFinder();
                    targetList = tmd.find(stmt, callGraph, true, true);
                }
                if (targetList != null) {
                    for (SootMethod obj : targetList) {
                        if (!obj.isNative()) {
                            multiCalledMethods.add(obj);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(BitSet in1, BitSet in2, BitSet out) {
        out.clear();
        out.or(in1);
        out.or(in2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(BitSet in, Unit unit, BitSet out) {
        out.clear();
        out.or(in);
        if (!out.get(indexOf(unit))) {
            out.set(indexOf(unit));
        } else {
            this.multiRunStatements.add(unit);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(BitSet source, BitSet dest) {
        dest.clear();
        dest.or(source);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public BitSet entryInitialFlow() {
        return new BitSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public BitSet newInitialFlow() {
        return new BitSet();
    }

    public FlowSet getMultiRunStatements() {
        FlowSet res = new ArraySparseSet();
        for (Unit u : this.multiRunStatements) {
            res.add(u);
        }
        return res;
    }

    protected int indexOf(Object o) {
        Integer index = this.nodeToIndex.get(o);
        if (index == null) {
            index = Integer.valueOf(this.lastIndex);
            this.nodeToIndex.put(o, index);
            this.lastIndex++;
        }
        return index.intValue();
    }
}
