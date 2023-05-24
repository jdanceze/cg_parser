package soot.jimple.toolkits.scalar.pre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.EquivalentValue;
import soot.SideEffectTester;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/EarliestnessComputation.class */
public class EarliestnessComputation {
    private final Map<Unit, FlowSet<EquivalentValue>> unitToEarliest;

    public EarliestnessComputation(UnitGraph unitGraph, UpSafetyAnalysis upSafe, DownSafetyAnalysis downSafe, SideEffectTester sideEffect) {
        this(unitGraph, upSafe, downSafe, sideEffect, new ArraySparseSet());
    }

    public EarliestnessComputation(UnitGraph unitGraph, UpSafetyAnalysis upSafe, DownSafetyAnalysis downSafe, SideEffectTester sideEffect, FlowSet<EquivalentValue> set) {
        this.unitToEarliest = new HashMap(unitGraph.size() + 1, 0.7f);
        Iterator<Unit> it = unitGraph.iterator();
        while (it.hasNext()) {
            Unit currentUnit = it.next();
            FlowSet<EquivalentValue> earliest = set.emptySet();
            this.unitToEarliest.put(currentUnit, earliest);
            FlowSet<EquivalentValue> downSafeSet = downSafe.getFlowBefore(currentUnit).mo2534clone();
            List<Unit> predList = unitGraph.getPredsOf(currentUnit);
            if (predList.isEmpty()) {
                earliest.union(downSafeSet);
            } else {
                for (Unit predecessor : predList) {
                    Iterator<EquivalentValue> downSafeIt = downSafeSet.iterator();
                    while (downSafeIt.hasNext()) {
                        EquivalentValue equiVal = downSafeIt.next();
                        Value avail = equiVal.getValue();
                        if (avail instanceof FieldRef) {
                            if (sideEffect.unitCanWriteTo(predecessor, avail)) {
                                earliest.add(equiVal);
                                downSafeIt.remove();
                            }
                        } else {
                            Iterator<ValueBox> it2 = avail.getUseBoxes().iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    break;
                                }
                                ValueBox useBox = it2.next();
                                Value use = useBox.getValue();
                                if (sideEffect.unitCanWriteTo(predecessor, use)) {
                                    earliest.add(equiVal);
                                    downSafeIt.remove();
                                    break;
                                }
                            }
                        }
                    }
                    Iterator<EquivalentValue> downSafeIt2 = downSafeSet.iterator();
                    while (downSafeIt2.hasNext()) {
                        EquivalentValue equiVal2 = downSafeIt2.next();
                        FlowSet<EquivalentValue> preDown = downSafe.getFlowBefore(predecessor);
                        FlowSet<EquivalentValue> preUp = upSafe.getFlowBefore(predecessor);
                        if (!preDown.contains(equiVal2) && !preUp.contains(equiVal2)) {
                            earliest.add(equiVal2);
                            downSafeIt2.remove();
                        }
                    }
                }
            }
        }
    }

    public FlowSet<EquivalentValue> getFlowBefore(Object node) {
        return this.unitToEarliest.get(node);
    }
}
