package soot.jimple.toolkits.thread.synchronization;

import java.util.List;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.jimple.toolkits.pointer.Union;
import soot.jimple.toolkits.pointer.UnionFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/StrayRWFinder.class */
public class StrayRWFinder extends BackwardFlowAnalysis {
    FlowSet emptySet;
    Map unitToGenerateSet;
    Body body;
    SideEffectAnalysis sea;
    List tns;

    StrayRWFinder(UnitGraph graph, Body b, List tns) {
        super(graph);
        this.emptySet = new ArraySparseSet();
        this.body = b;
        this.tns = tns;
        if (G.v().Union_factory == null) {
            G.v().Union_factory = new UnionFactory() { // from class: soot.jimple.toolkits.thread.synchronization.StrayRWFinder.1
                @Override // soot.jimple.toolkits.pointer.UnionFactory
                public Union newUnion() {
                    return FullObjectSet.v();
                }
            };
        }
        this.sea = Scene.v().getSideEffectAnalysis();
        this.sea.findNTRWSets(this.body.getMethod());
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object newInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object entryInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    @Override // soot.toolkits.scalar.FlowAnalysis
    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        FlowSet in = (FlowSet) inValue;
        FlowSet out = (FlowSet) outValue;
        RWSet stmtRead = this.sea.readSet(this.body.getMethod(), (Stmt) unit);
        RWSet stmtWrite = this.sea.writeSet(this.body.getMethod(), (Stmt) unit);
        boolean addSelf = Boolean.FALSE.booleanValue();
        for (CriticalSection tn : this.tns) {
            if (stmtRead.hasNonEmptyIntersection(tn.write) || stmtWrite.hasNonEmptyIntersection(tn.read) || stmtWrite.hasNonEmptyIntersection(tn.write)) {
                addSelf = Boolean.TRUE.booleanValue();
            }
        }
        in.copy(out);
        if (addSelf) {
            CriticalSection tn2 = new CriticalSection(false, this.body.getMethod(), 0);
            tn2.entermonitor = (Stmt) unit;
            tn2.units.add((Unit) unit);
            tn2.read.union(stmtRead);
            tn2.write.union(stmtWrite);
            out.add(tn2);
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    protected void merge(Object in1, Object in2, Object out) {
        FlowSet inSet1 = ((FlowSet) in1).mo2534clone();
        FlowSet inSet2 = ((FlowSet) in2).mo2534clone();
        FlowSet outSet = (FlowSet) out;
        inSet1.union(inSet2, outSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(Object source, Object dest) {
        FlowSet sourceSet = (FlowSet) source;
        FlowSet destSet = (FlowSet) dest;
        sourceSet.copy(destSet);
    }
}
