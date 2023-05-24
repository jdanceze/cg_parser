package soot.jimple.toolkits.scalar.pre;

import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.SideEffectTester;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/DownSafetyAnalysis.class */
public class DownSafetyAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
    private final SideEffectTester sideEffect;
    private final Map<Unit, EquivalentValue> unitToGenerateMap;
    private final BoundedFlowSet<EquivalentValue> set;

    @Deprecated
    public DownSafetyAnalysis(DirectedGraph<Unit> dg) {
        super(dg);
        throw new RuntimeException("Don't use this Constructor!");
    }

    public DownSafetyAnalysis(DirectedGraph<Unit> dg, Map<Unit, EquivalentValue> unitToGen, SideEffectTester sideEffect) {
        this(dg, unitToGen, sideEffect, new ArrayPackedSet(new CollectionFlowUniverse(unitToGen.values())));
    }

    public DownSafetyAnalysis(DirectedGraph<Unit> dg, Map<Unit, EquivalentValue> unitToGen, SideEffectTester sideEffect, BoundedFlowSet<EquivalentValue> set) {
        super(dg);
        this.sideEffect = sideEffect;
        this.set = set;
        this.unitToGenerateMap = unitToGen;
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<EquivalentValue> newInitialFlow() {
        return this.set.topSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<EquivalentValue> entryInitialFlow() {
        return this.set.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<EquivalentValue> in, Unit u, FlowSet<EquivalentValue> out) {
        in.copy(out);
        Iterator<EquivalentValue> outIt = out.iterator();
        while (outIt.hasNext()) {
            EquivalentValue equiVal = outIt.next();
            Value avail = equiVal.getValue();
            if (avail instanceof FieldRef) {
                if (this.sideEffect.unitCanWriteTo(u, avail)) {
                    outIt.remove();
                }
            } else {
                Iterator<ValueBox> it = avail.getUseBoxes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ValueBox next = it.next();
                    Value use = next.getValue();
                    if (this.sideEffect.unitCanWriteTo(u, use)) {
                        outIt.remove();
                        break;
                    }
                }
            }
        }
        EquivalentValue add = this.unitToGenerateMap.get(u);
        if (add != null) {
            out.add(add, out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<EquivalentValue> in1, FlowSet<EquivalentValue> in2, FlowSet<EquivalentValue> out) {
        in1.intersection(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<EquivalentValue> source, FlowSet<EquivalentValue> dest) {
        source.copy(dest);
    }
}
