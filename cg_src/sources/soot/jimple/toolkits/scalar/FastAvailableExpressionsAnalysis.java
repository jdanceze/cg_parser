package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/FastAvailableExpressionsAnalysis.class */
public class FastAvailableExpressionsAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Value>> {
    protected final FlowSet<Value> emptySet;
    protected final SideEffectTester st;
    protected final Map<Value, Unit> rhsToContainingStmt;
    protected final Map<Unit, FlowSet<Value>> unitToGenerateSet;

    public FastAvailableExpressionsAnalysis(DirectedGraph<Unit> dg, SootMethod m, SideEffectTester st) {
        super(dg);
        this.emptySet = new ToppedSet(new ArraySparseSet());
        this.st = st;
        this.rhsToContainingStmt = new HashMap();
        this.unitToGenerateSet = new HashMap((dg.size() * 2) + 1, 0.7f);
        for (Unit s : dg) {
            FlowSet<Value> genSet = this.emptySet.mo2534clone();
            if (s instanceof AssignStmt) {
                Value gen = ((AssignStmt) s).getRightOp();
                if ((gen instanceof Expr) || (gen instanceof FieldRef)) {
                    this.rhsToContainingStmt.put(gen, s);
                    if (!(gen instanceof NewExpr) && !(gen instanceof NewArrayExpr) && !(gen instanceof NewMultiArrayExpr) && !(gen instanceof InvokeExpr)) {
                        genSet.add(gen, genSet);
                    }
                }
            }
            this.unitToGenerateSet.put(s, genSet);
        }
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> newInitialFlow() {
        FlowSet<Value> newSet = this.emptySet.mo2534clone();
        ((ToppedSet) newSet).setTop(true);
        return newSet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> entryInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Value> in, Unit u, FlowSet<Value> out) {
        in.copy(out);
        if (((ToppedSet) in).isTop()) {
            return;
        }
        out.union(this.unitToGenerateSet.get(u), out);
        if (((ToppedSet) out).isTop()) {
            throw new RuntimeException("trying to kill on topped set!");
        }
        Iterator it = new ArrayList(out.toList()).iterator();
        while (it.hasNext()) {
            Value avail = (Value) it.next();
            if (avail instanceof FieldRef) {
                if (this.st.unitCanWriteTo(u, avail)) {
                    out.remove(avail, out);
                }
            } else {
                for (ValueBox vb : avail.getUseBoxes()) {
                    Value use = vb.getValue();
                    if (this.st.unitCanWriteTo(u, use)) {
                        out.remove(avail, out);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Value> inSet1, FlowSet<Value> inSet2, FlowSet<Value> outSet) {
        inSet1.intersection(inSet2, outSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Value> sourceSet, FlowSet<Value> destSet) {
        sourceSet.copy(destSet);
    }
}
