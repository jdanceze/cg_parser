package soot.jimple.toolkits.scalar.pre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/pre/LatestComputation.class */
public class LatestComputation {
    private final Map<Unit, FlowSet<EquivalentValue>> unitToLatest;

    public LatestComputation(UnitGraph unitGraph, DelayabilityAnalysis delayed, Map<Unit, EquivalentValue> equivRhsMap) {
        this(unitGraph, delayed, equivRhsMap, new ArrayPackedSet(new CollectionFlowUniverse(equivRhsMap.values())));
    }

    public LatestComputation(UnitGraph unitGraph, DelayabilityAnalysis delayed, Map<Unit, EquivalentValue> equivRhsMap, BoundedFlowSet<EquivalentValue> set) {
        this.unitToLatest = new HashMap(unitGraph.size() + 1, 0.7f);
        Iterator<Unit> it = unitGraph.iterator();
        while (it.hasNext()) {
            Unit currentUnit = it.next();
            FlowSet<EquivalentValue> delaySet = delayed.getFlowBefore(currentUnit);
            FlowSet<EquivalentValue> succCompSet = set.topSet();
            for (Unit successor : unitGraph.getSuccsOf(currentUnit)) {
                succCompSet.intersection(delayed.getFlowBefore(successor), succCompSet);
            }
            if (equivRhsMap.get(currentUnit) != null) {
                succCompSet.remove(equivRhsMap.get(currentUnit));
            }
            FlowSet<EquivalentValue> latest = delaySet.emptySet();
            delaySet.difference(succCompSet, latest);
            this.unitToLatest.put(currentUnit, latest);
        }
    }

    public FlowSet<EquivalentValue> getFlowBefore(Object node) {
        return this.unitToLatest.get(node);
    }
}
