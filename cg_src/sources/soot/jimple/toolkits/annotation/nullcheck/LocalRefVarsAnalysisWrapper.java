package soot.jimple.toolkits.annotation.nullcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/LocalRefVarsAnalysisWrapper.class */
public class LocalRefVarsAnalysisWrapper {
    private static final boolean computeChecks = true;
    private static final boolean discardKTop = true;
    private final BranchedRefVarsAnalysis analysis;
    private final Map<Unit, List<RefIntPair>> unitToVarsBefore;
    private final Map<Unit, List<RefIntPair>> unitToVarsAfterFall;
    private final Map<Unit, List<List<RefIntPair>>> unitToListsOfVarsAfterBranches;
    private final Map<Unit, List<Object>> unitToVarsNeedCheck;
    private final Map<Unit, List<RefIntPair>> unitToVarsDontNeedCheck;

    public LocalRefVarsAnalysisWrapper(ExceptionalUnitGraph graph) {
        this.analysis = new BranchedRefVarsAnalysis(graph);
        int size = (graph.size() * 2) + 1;
        this.unitToVarsBefore = new HashMap(size, 0.7f);
        this.unitToVarsAfterFall = new HashMap(size, 0.7f);
        this.unitToListsOfVarsAfterBranches = new HashMap(size, 0.7f);
        this.unitToVarsNeedCheck = new HashMap(size, 0.7f);
        this.unitToVarsDontNeedCheck = new HashMap(size, 0.7f);
        Iterator<Unit> it = graph.iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            this.unitToVarsAfterFall.put(s, Collections.unmodifiableList(buildList(this.analysis.getFallFlowAfter(s))));
            List<FlowSet<RefIntPair>> branchesFlowsets = this.analysis.getBranchFlowAfter(s);
            List<List<RefIntPair>> lst = new ArrayList<>(branchesFlowsets.size());
            for (FlowSet<RefIntPair> set : branchesFlowsets) {
                lst.add(Collections.unmodifiableList(buildList(set)));
            }
            this.unitToListsOfVarsAfterBranches.put(s, lst);
            FlowSet<RefIntPair> set2 = this.analysis.getFlowBefore(s);
            this.unitToVarsBefore.put(s, Collections.unmodifiableList(buildList(set2)));
            ArrayList<RefIntPair> dontNeedCheckVars = new ArrayList<>();
            ArrayList<Object> needCheckVars = new ArrayList<>();
            HashSet<Value> allChecksSet = new HashSet<>(5, 0.7f);
            allChecksSet.addAll(this.analysis.unitToArrayRefChecksSet.get(s));
            allChecksSet.addAll(this.analysis.unitToInstanceFieldRefChecksSet.get(s));
            allChecksSet.addAll(this.analysis.unitToInstanceInvokeExprChecksSet.get(s));
            allChecksSet.addAll(this.analysis.unitToLengthExprChecksSet.get(s));
            Iterator<Value> it2 = allChecksSet.iterator();
            while (it2.hasNext()) {
                Value v = it2.next();
                int vInfo = this.analysis.anyRefInfo(v, set2);
                switch (vInfo) {
                    case 0:
                        needCheckVars.add(this.analysis.getKRefIntPair(new EquivalentValue(v), vInfo));
                        break;
                    case 99:
                        needCheckVars.add(v);
                        break;
                    default:
                        dontNeedCheckVars.add(this.analysis.getKRefIntPair(new EquivalentValue(v), vInfo));
                        break;
                }
            }
            this.unitToVarsNeedCheck.put(s, Collections.unmodifiableList(needCheckVars));
            this.unitToVarsDontNeedCheck.put(s, Collections.unmodifiableList(dontNeedCheckVars));
        }
    }

    private List<RefIntPair> buildList(FlowSet<RefIntPair> set) {
        List<RefIntPair> lst = new ArrayList<>();
        for (EquivalentValue r : this.analysis.refTypeValues) {
            int refInfo = this.analysis.refInfo(r, set);
            if (refInfo != 99) {
                lst.add(this.analysis.getKRefIntPair(r, refInfo));
            }
        }
        return lst;
    }

    public List<RefIntPair> getVarsBefore(Unit s) {
        return this.unitToVarsBefore.get(s);
    }

    public List<RefIntPair> getVarsAfterFall(Unit s) {
        return this.unitToVarsAfterFall.get(s);
    }

    public List<List<RefIntPair>> getListsOfVarsAfterBranch(Unit s) {
        return this.unitToListsOfVarsAfterBranches.get(s);
    }

    public List<Object> getVarsNeedCheck(Unit s) {
        return this.unitToVarsNeedCheck.get(s);
    }

    public List<RefIntPair> getVarsDontNeedCheck(Unit s) {
        return this.unitToVarsDontNeedCheck.get(s);
    }
}
