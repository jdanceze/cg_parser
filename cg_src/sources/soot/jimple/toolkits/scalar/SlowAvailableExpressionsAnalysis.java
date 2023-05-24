package soot.jimple.toolkits.scalar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayFlowUniverse;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/SlowAvailableExpressionsAnalysis.class */
public class SlowAvailableExpressionsAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Value>> {
    protected final Map<Unit, BoundedFlowSet<Value>> unitToGenerateSet;
    protected final Map<Unit, BoundedFlowSet<Value>> unitToPreserveSet;
    protected final Map<Value, Stmt> rhsToContainingStmt;
    protected final FlowSet<Value> emptySet;
    private final HashMap<Value, EquivalentValue> valueToEquivValue;

    public SlowAvailableExpressionsAnalysis(DirectedGraph<Unit> dg) {
        super(dg);
        this.valueToEquivValue = new HashMap<>();
        this.rhsToContainingStmt = new HashMap();
        HashSet<Value> exprs = new HashSet<>();
        Map<EquivalentValue, Chain<EquivalentValue>> containingExprs = new HashMap<>();
        Map<EquivalentValue, Chain<Value>> equivValToSiblingList = new HashMap<>();
        UnitGraph g = (UnitGraph) dg;
        Iterator<Unit> it = g.getBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt s = (Stmt) u;
            if (s instanceof AssignStmt) {
                Value v = ((AssignStmt) s).getRightOp();
                this.rhsToContainingStmt.put(v, s);
                EquivalentValue ev = this.valueToEquivValue.get(v);
                if (ev == null) {
                    HashMap<Value, EquivalentValue> hashMap = this.valueToEquivValue;
                    EquivalentValue equivalentValue = new EquivalentValue(v);
                    ev = equivalentValue;
                    hashMap.put(v, equivalentValue);
                }
                Chain<Value> sibList = equivValToSiblingList.get(ev);
                if (sibList == null) {
                    Chain<Value> hashChain = new HashChain<>();
                    sibList = hashChain;
                    equivValToSiblingList.put(ev, hashChain);
                }
                if (!sibList.contains(v)) {
                    sibList.add(v);
                }
                if ((v instanceof Expr) && !exprs.contains(v)) {
                    exprs.add(v);
                    for (ValueBox vb : v.getUseBoxes()) {
                        Value o = vb.getValue();
                        EquivalentValue eo = this.valueToEquivValue.get(o);
                        if (eo == null) {
                            HashMap<Value, EquivalentValue> hashMap2 = this.valueToEquivValue;
                            EquivalentValue equivalentValue2 = new EquivalentValue(o);
                            eo = equivalentValue2;
                            hashMap2.put(o, equivalentValue2);
                        }
                        Chain<Value> sibList2 = equivValToSiblingList.get(eo);
                        if (sibList2 == null) {
                            Chain<Value> hashChain2 = new HashChain<>();
                            sibList2 = hashChain2;
                            equivValToSiblingList.put(eo, hashChain2);
                        }
                        if (!sibList2.contains(o)) {
                            sibList2.add(o);
                        }
                        Chain<EquivalentValue> l = containingExprs.get(eo);
                        if (l == null) {
                            Chain<EquivalentValue> hashChain3 = new HashChain<>();
                            l = hashChain3;
                            containingExprs.put(eo, hashChain3);
                        }
                        if (!l.contains(ev)) {
                            l.add(ev);
                        }
                    }
                }
            }
        }
        FlowUniverse<Value> exprUniv = new ArrayFlowUniverse<>((Value[]) exprs.toArray(new Value[exprs.size()]));
        this.emptySet = new ArrayPackedSet(exprUniv);
        this.unitToPreserveSet = new HashMap((g.size() * 2) + 1, 0.7f);
        Iterator<Unit> it2 = g.iterator();
        while (it2.hasNext()) {
            Unit s2 = it2.next();
            ArrayPackedSet arrayPackedSet = new ArrayPackedSet(exprUniv);
            for (ValueBox box : s2.getDefBoxes()) {
                Chain<EquivalentValue> c = containingExprs.get(this.valueToEquivValue.get(box.getValue()));
                if (c != null) {
                    for (EquivalentValue container : c) {
                        for (Value sibVal : equivValToSiblingList.get(container)) {
                            arrayPackedSet.add(sibVal);
                        }
                    }
                }
            }
            arrayPackedSet.complement(arrayPackedSet);
            this.unitToPreserveSet.put(s2, arrayPackedSet);
        }
        this.unitToGenerateSet = new HashMap((g.size() * 2) + 1, 0.7f);
        Iterator<Unit> it3 = g.iterator();
        while (it3.hasNext()) {
            Unit s3 = it3.next();
            ArrayPackedSet arrayPackedSet2 = new ArrayPackedSet(exprUniv);
            if (s3 instanceof AssignStmt) {
                AssignStmt as = (AssignStmt) s3;
                Value gen = as.getRightOp();
                if ((gen instanceof Expr) && !(gen instanceof NewExpr) && !(gen instanceof NewArrayExpr) && !(gen instanceof NewMultiArrayExpr) && !(gen instanceof InvokeExpr)) {
                    arrayPackedSet2.add(gen);
                }
            }
            arrayPackedSet2.intersection(this.unitToPreserveSet.get(s3), arrayPackedSet2);
            this.unitToGenerateSet.put(s3, arrayPackedSet2);
        }
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> newInitialFlow() {
        BoundedFlowSet<Value> out = (BoundedFlowSet) this.emptySet.mo2534clone();
        out.complement(out);
        return out;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Value> entryInitialFlow() {
        return this.emptySet.mo2534clone();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<Value> in, Unit unit, FlowSet<Value> out) {
        in.intersection(this.unitToPreserveSet.get(unit), out);
        out.union(this.unitToGenerateSet.get(unit));
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
