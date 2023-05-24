package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.options.Options;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.interaction.FlowInfo;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ForwardBranchedFlowAnalysis.class */
public abstract class ForwardBranchedFlowAnalysis<A> extends BranchedFlowAnalysis<Unit, A> {
    private static final Logger logger = LoggerFactory.getLogger(ForwardBranchedFlowAnalysis.class);

    public ForwardBranchedFlowAnalysis(UnitGraph graph) {
        super(graph);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public boolean isForward() {
        return true;
    }

    private void accumulateAfterFlowSets(Unit s, A[] aArr, List<A> previousAfterFlows) {
        int repCount = 0;
        previousAfterFlows.clear();
        if (s.fallsThrough()) {
            copy(this.unitToAfterFallFlow.get(s).get(0), aArr[0]);
            repCount = 0 + 1;
            previousAfterFlows.add(aArr[0]);
        }
        if (s.branches()) {
            for (A fs : getBranchFlowAfter(s)) {
                copy(fs, aArr[repCount]);
                int i = repCount;
                repCount++;
                previousAfterFlows.add(aArr[i]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void doAnalysis() {
        TreeSet<Unit> changedUnits = new TreeSet<>(new Comparator<Unit>() { // from class: soot.toolkits.scalar.ForwardBranchedFlowAnalysis.1
            final Map<Unit, Integer> numbers = new HashMap();

            {
                int i = 1;
                for (Unit u : new PseudoTopologicalOrderer().newList(ForwardBranchedFlowAnalysis.this.graph, false)) {
                    this.numbers.put(u, Integer.valueOf(i));
                    i++;
                }
            }

            @Override // java.util.Comparator
            public int compare(Unit o1, Unit o2) {
                return this.numbers.get(o1).intValue() - this.numbers.get(o2).intValue();
            }
        });
        int numNodes = this.graph.size();
        Map<Unit, ArrayList<A>> unitToIncomingFlowSets = new HashMap<>((numNodes * 2) + 1, 0.7f);
        for (Unit s : this.graph) {
            unitToIncomingFlowSets.put(s, new ArrayList<>());
        }
        int numComputations = 0;
        int maxBranchSize = 0;
        Chain<Unit> sl = ((UnitGraph) this.graph).getBody().getUnits();
        for (Unit s2 : this.graph) {
            changedUnits.add(s2);
            this.unitToBeforeFlow.put(s2, newInitialFlow());
            if (s2.fallsThrough()) {
                List<A> fl = new ArrayList<>();
                fl.add(newInitialFlow());
                this.unitToAfterFallFlow.put(s2, fl);
                Unit succ = sl.getSuccOf(s2);
                if (succ != null) {
                    unitToIncomingFlowSets.get(succ).addAll(fl);
                }
            } else {
                this.unitToAfterFallFlow.put(s2, new ArrayList());
            }
            List<UnitBox> unitBoxes = s2.getUnitBoxes();
            List<A> l = new ArrayList<>();
            if (s2.branches()) {
                for (UnitBox ub : unitBoxes) {
                    A f = newInitialFlow();
                    l.add(f);
                    unitToIncomingFlowSets.get(ub.getUnit()).add(f);
                }
            }
            this.unitToAfterBranchFlow.put(s2, l);
            if (unitBoxes.size() > maxBranchSize) {
                maxBranchSize = unitBoxes.size();
            }
        }
        List<Unit> heads = this.graph.getHeads();
        for (Unit s3 : heads) {
            this.unitToBeforeFlow.put(s3, entryInitialFlow());
        }
        if (treatTrapHandlersAsEntries()) {
            for (Trap trap : ((UnitGraph) this.graph).getBody().getTraps()) {
                this.unitToBeforeFlow.put(trap.getHandlerUnit(), entryInitialFlow());
            }
        }
        Object[] flowRepositories = new Object[maxBranchSize + 1];
        Object[] previousFlowRepositories = new Object[maxBranchSize + 1];
        for (int i = 0; i < maxBranchSize + 1; i++) {
            flowRepositories[i] = newInitialFlow();
            previousFlowRepositories[i] = newInitialFlow();
        }
        List<A> previousAfterFlows = new ArrayList<>();
        List<A> afterFlows = new ArrayList<>();
        while (!changedUnits.isEmpty()) {
            Unit s4 = changedUnits.first();
            changedUnits.remove(s4);
            accumulateAfterFlowSets(s4, previousFlowRepositories, previousAfterFlows);
            A beforeFlow = getFlowBefore(s4);
            Iterator<A> preds = unitToIncomingFlowSets.get(s4).iterator();
            if (preds.hasNext()) {
                copy(preds.next(), beforeFlow);
                while (preds.hasNext()) {
                    A otherBranchFlow = preds.next();
                    A newBeforeFlow = newInitialFlow();
                    merge(s4, beforeFlow, otherBranchFlow, newBeforeFlow);
                    copy(newBeforeFlow, beforeFlow);
                }
                if (heads.contains(s4)) {
                    mergeInto(s4, beforeFlow, entryInitialFlow());
                }
            }
            List<A> afterFallFlow = this.unitToAfterFallFlow.get(s4);
            List<A> afterBranchFlow = getBranchFlowAfter(s4);
            if (Options.v().interactive_mode()) {
                InteractionHandler ih = InteractionHandler.v();
                A savedFlow = newInitialFlow();
                copy(beforeFlow, savedFlow);
                FlowInfo<A, Unit> fi = new FlowInfo<>(savedFlow, s4, true);
                if (ih.getStopUnitList() != null && ih.getStopUnitList().contains(s4)) {
                    ih.handleStopAtNodeEvent(s4);
                }
                ih.handleBeforeAnalysisEvent(fi);
            }
            flowThrough(beforeFlow, s4, afterFallFlow, afterBranchFlow);
            if (Options.v().interactive_mode()) {
                List<A> l2 = new ArrayList<>();
                if (!afterFallFlow.isEmpty()) {
                    l2.addAll(afterFallFlow);
                }
                if (!afterBranchFlow.isEmpty()) {
                    l2.addAll(afterBranchFlow);
                }
                FlowInfo<List<A>, Unit> fi2 = new FlowInfo<>(l2, s4, false);
                InteractionHandler.v().handleAfterAnalysisEvent(fi2);
            }
            numComputations++;
            accumulateAfterFlowSets(s4, flowRepositories, afterFlows);
            if (!afterFlows.equals(previousAfterFlows)) {
                for (Unit succ2 : this.graph.getSuccsOf(s4)) {
                    changedUnits.add(succ2);
                }
            }
        }
        Timers.v().totalFlowNodes += numNodes;
        Timers.v().totalFlowComputations += numComputations;
    }
}
