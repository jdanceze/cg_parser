package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.ArraySet;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FastColorer.class */
public class FastColorer {
    private FastColorer() {
    }

    public static <G> void unsplitAssignColorsToLocals(Body unitBody, Map<Local, G> localToGroup, Map<Local, Integer> localToColor, Map<G, Integer> groupToColorCount) {
        ExceptionalUnitGraph unitGraph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(unitBody, PedanticThrowAnalysis.v(), Options.v().omit_excepting_unit_edges());
        UnitInterferenceGraph intGraph = new UnitInterferenceGraph(unitBody, localToGroup, new SimpleLiveLocals(unitGraph), unitGraph);
        Map<Local, String> localToOriginalName = new HashMap<>();
        for (Local local : intGraph.getLocals()) {
            String name = local.getName();
            int signIndex = name.indexOf(35);
            if (signIndex >= 0) {
                name = name.substring(0, signIndex);
            }
            localToOriginalName.put(local, name);
        }
        Map<StringGroupPair, List<Integer>> originalNameAndGroupToColors = new HashMap<>();
        int[] freeColors = new int[10];
        for (Local local2 : intGraph.getLocals()) {
            if (!localToColor.containsKey(local2)) {
                G group = localToGroup.get(local2);
                int colorCount = groupToColorCount.get(group).intValue();
                if (freeColors.length < colorCount) {
                    freeColors = new int[Math.max(freeColors.length * 2, colorCount)];
                }
                Arrays.fill(freeColors, 0, colorCount, 1);
                Local[] interferences = intGraph.getInterferencesOf(local2);
                if (interferences != null) {
                    for (Local element : interferences) {
                        if (localToColor.containsKey(element)) {
                            int usedColor = localToColor.get(element).intValue();
                            freeColors[usedColor] = 0;
                        }
                    }
                }
                StringGroupPair key = new StringGroupPair(localToOriginalName.get(local2), group);
                List<Integer> originalNameColors = originalNameAndGroupToColors.get(key);
                if (originalNameColors == null) {
                    originalNameColors = new ArrayList<>();
                    originalNameAndGroupToColors.put(key, originalNameColors);
                }
                boolean found = false;
                Integer assignedColor = 0;
                for (Integer color : originalNameColors) {
                    if (freeColors[color.intValue()] == 1) {
                        found = true;
                        assignedColor = color;
                    }
                }
                if (!found) {
                    assignedColor = Integer.valueOf(colorCount);
                    groupToColorCount.put(group, Integer.valueOf(colorCount + 1));
                    originalNameColors.add(assignedColor);
                }
                localToColor.put(local2, assignedColor);
            }
        }
    }

    public static <G> void assignColorsToLocals(Body unitBody, Map<Local, G> localToGroup, Map<Local, Integer> localToColor, Map<G, Integer> groupToColorCount) {
        ExceptionalUnitGraph unitGraph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(unitBody, PedanticThrowAnalysis.v(), Options.v().omit_excepting_unit_edges());
        final UnitInterferenceGraph intGraph = new UnitInterferenceGraph(unitBody, localToGroup, new SimpleLiveLocals(unitGraph), unitGraph);
        List<Local> sortedLocals = new ArrayList<>(intGraph.getLocals());
        Collections.sort(sortedLocals, new Comparator<Local>() { // from class: soot.toolkits.scalar.FastColorer.1
            @Override // java.util.Comparator
            public int compare(Local o1, Local o2) {
                return UnitInterferenceGraph.this.getInterferenceCount(o2) - UnitInterferenceGraph.this.getInterferenceCount(o1);
            }
        });
        for (Local local : sortedLocals) {
            if (!localToColor.containsKey(local)) {
                G group = localToGroup.get(local);
                int colorCount = groupToColorCount.get(group).intValue();
                BitSet blockedColors = new BitSet(colorCount);
                Local[] interferences = intGraph.getInterferencesOf(local);
                if (interferences != null) {
                    for (Local element : interferences) {
                        if (localToColor.containsKey(element)) {
                            blockedColors.set(localToColor.get(element).intValue());
                        }
                    }
                }
                int assignedColor = -1;
                int i = 0;
                while (true) {
                    if (i < colorCount) {
                        if (blockedColors.get(i)) {
                            i++;
                        } else {
                            assignedColor = i;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (assignedColor < 0) {
                    assignedColor = colorCount;
                    groupToColorCount.put(group, Integer.valueOf(colorCount + 1));
                }
                localToColor.put(local, Integer.valueOf(assignedColor));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FastColorer$UnitInterferenceGraph.class */
    public static class UnitInterferenceGraph {
        final Map<Local, Set<Local>> localToLocals;
        final List<Local> locals;

        public UnitInterferenceGraph(Body body, Map<Local, ? extends Object> localToGroup, LiveLocals liveLocals, ExceptionalUnitGraph unitGraph) {
            this.locals = new ArrayList(body.getLocals());
            this.localToLocals = new HashMap((body.getLocalCount() * 2) + 1, 0.7f);
            Iterator<Unit> it = body.getUnits().iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                List<ValueBox> defBoxes = unit.getDefBoxes();
                if (!defBoxes.isEmpty()) {
                    if (defBoxes.size() != 1) {
                        throw new RuntimeException("invalid number of def boxes");
                    }
                    Value defValue = defBoxes.get(0).getValue();
                    if (defValue instanceof Local) {
                        Local defLocal = (Local) defValue;
                        Set<Local> liveLocalsAtUnit = new HashSet<>();
                        for (Unit succ : unitGraph.getSuccsOf(unit)) {
                            liveLocalsAtUnit.addAll(liveLocals.getLiveLocalsBefore(succ));
                        }
                        for (Local otherLocal : liveLocalsAtUnit) {
                            if (localToGroup.get(otherLocal).equals(localToGroup.get(defLocal))) {
                                setInterference(defLocal, otherLocal);
                            }
                        }
                    }
                }
            }
        }

        public List<Local> getLocals() {
            return this.locals;
        }

        public void setInterference(Local l1, Local l2) {
            Set<Local> locals = this.localToLocals.get(l1);
            if (locals == null) {
                locals = new ArraySet<>();
                this.localToLocals.put(l1, locals);
            }
            locals.add(l2);
            Set<Local> locals2 = this.localToLocals.get(l2);
            if (locals2 == null) {
                locals2 = new ArraySet<>();
                this.localToLocals.put(l2, locals2);
            }
            locals2.add(l1);
        }

        public int getInterferenceCount(Local l) {
            Set<Local> localSet = this.localToLocals.get(l);
            if (localSet == null) {
                return 0;
            }
            return localSet.size();
        }

        public Local[] getInterferencesOf(Local l) {
            Set<Local> localSet = this.localToLocals.get(l);
            if (localSet == null) {
                return null;
            }
            return (Local[]) localSet.toArray(new Local[localSet.size()]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FastColorer$StringGroupPair.class */
    public static class StringGroupPair {
        private final String string;
        private final Object group;

        public StringGroupPair(String s, Object g) {
            this.string = s;
            this.group = g;
        }

        public boolean equals(Object p) {
            if (p instanceof StringGroupPair) {
                StringGroupPair temp = (StringGroupPair) p;
                return this.string.equals(temp.string) && this.group.equals(temp.group);
            }
            return false;
        }

        public int hashCode() {
            return (this.string.hashCode() * 101) + this.group.hashCode() + 17;
        }
    }
}
