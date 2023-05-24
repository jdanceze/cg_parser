package soot.jimple.infoflow.problems;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Unit;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/TaintPropagationResults.class */
public class TaintPropagationResults {
    protected final InfoflowManager manager;
    protected final MyConcurrentHashMap<AbstractionAtSink, Abstraction> results = new MyConcurrentHashMap<>();
    protected final Set<OnTaintPropagationResultAdded> resultAddedHandlers = new HashSet();

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/TaintPropagationResults$OnTaintPropagationResultAdded.class */
    public interface OnTaintPropagationResultAdded {
        boolean onResultAvailable(AbstractionAtSink abstractionAtSink);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaintPropagationResults(InfoflowManager manager) {
        this.manager = manager;
    }

    public boolean addResult(AbstractionAtSink resultAbs) {
        if (this.manager.getConfig().getIgnoreFlowsInSystemPackages() && SystemClassHandler.v().isClassInSystemPackage(this.manager.getICFG().getMethodOf(resultAbs.getSinkStmt()).getDeclaringClass().getName())) {
            return true;
        }
        Abstraction abs = resultAbs.getAbstraction();
        Abstraction abs2 = abs.deriveNewAbstraction(abs.getAccessPath(), resultAbs.getSinkStmt());
        abs2.setCorrespondingCallSite(resultAbs.getSinkStmt());
        IMemoryManager<Abstraction, Unit> memoryManager = this.manager.getForwardSolver().getMemoryManager();
        if (memoryManager != null) {
            abs2 = memoryManager.handleMemoryObject(abs2);
            if (abs2 == null) {
                return true;
            }
        }
        AbstractionAtSink resultAbs2 = new AbstractionAtSink(resultAbs.getSinkDefinition(), abs2, resultAbs.getSinkStmt());
        Abstraction newAbs = this.results.putIfAbsentElseGet((MyConcurrentHashMap<AbstractionAtSink, Abstraction>) resultAbs2, (AbstractionAtSink) resultAbs2.getAbstraction());
        if (newAbs != resultAbs2.getAbstraction()) {
            newAbs.addNeighbor(resultAbs2.getAbstraction());
        }
        boolean continueAnalysis = true;
        for (OnTaintPropagationResultAdded handler : this.resultAddedHandlers) {
            if (!handler.onResultAvailable(resultAbs2)) {
                continueAnalysis = false;
            }
        }
        return continueAnalysis;
    }

    public boolean isEmpty() {
        return this.results.isEmpty();
    }

    public Set<AbstractionAtSink> getResults() {
        return this.results.keySet();
    }

    public void addResultAvailableHandler(OnTaintPropagationResultAdded handler) {
        this.resultAddedHandlers.add(handler);
    }

    public int size() {
        if (this.results == null) {
            return 0;
        }
        return this.results.size();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.results == null ? 0 : this.results.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaintPropagationResults other = (TaintPropagationResults) obj;
        if (this.results == null) {
            if (other.results != null) {
                return false;
            }
            return true;
        } else if (!this.results.equals(other.results)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.results != null && !this.results.isEmpty()) {
            Iterator it = this.results.keySet().iterator();
            while (it.hasNext()) {
                AbstractionAtSink aas = (AbstractionAtSink) it.next();
                sb.append("Abstraction: ");
                sb.append(aas.getAbstraction());
                sb.append(" at ");
                sb.append(aas.getSinkStmt());
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
