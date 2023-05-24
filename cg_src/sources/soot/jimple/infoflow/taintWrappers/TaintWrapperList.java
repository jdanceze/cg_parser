package soot.jimple.infoflow.taintWrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/TaintWrapperList.class */
public class TaintWrapperList implements IReversibleTaintWrapper {
    private List<ITaintPropagationWrapper> wrappers = new ArrayList();
    private AtomicInteger hits = new AtomicInteger();
    private AtomicInteger misses = new AtomicInteger();

    public TaintWrapperList(ITaintPropagationWrapper... wrappers) {
        for (ITaintPropagationWrapper w : wrappers) {
            if (w != null) {
                addWrapper(w);
            }
        }
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public void initialize(InfoflowManager manager) {
        for (ITaintPropagationWrapper w : this.wrappers) {
            w.initialize(manager);
        }
    }

    public void addWrapper(ITaintPropagationWrapper wrapper) {
        this.wrappers.add(wrapper);
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public Set<Abstraction> getTaintsForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        for (ITaintPropagationWrapper w : this.wrappers) {
            Set<Abstraction> curAbsSet = w.getTaintsForMethod(stmt, d1, taintedPath);
            if (curAbsSet != null && !curAbsSet.isEmpty()) {
                this.hits.incrementAndGet();
                return curAbsSet;
            }
        }
        this.misses.incrementAndGet();
        return null;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean isExclusive(Stmt stmt, Abstraction taintedPath) {
        for (ITaintPropagationWrapper w : this.wrappers) {
            if (w.isExclusive(stmt, taintedPath)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(SootMethod method) {
        for (ITaintPropagationWrapper w : this.wrappers) {
            if (w.supportsCallee(method)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean supportsCallee(Stmt callSite) {
        for (ITaintPropagationWrapper w : this.wrappers) {
            if (w.supportsCallee(callSite)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public int getWrapperHits() {
        return this.hits.get();
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public int getWrapperMisses() {
        return this.misses.get();
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public Set<Abstraction> getAliasesForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        for (ITaintPropagationWrapper w : this.wrappers) {
            Set<Abstraction> curAbsSet = w.getAliasesForMethod(stmt, d1, taintedPath);
            if (curAbsSet != null && !curAbsSet.isEmpty()) {
                return curAbsSet;
            }
        }
        return null;
    }

    @Override // soot.jimple.infoflow.taintWrappers.IReversibleTaintWrapper
    public Set<Abstraction> getInverseTaintsForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        Set<Abstraction> curAbsSet;
        for (ITaintPropagationWrapper w : this.wrappers) {
            if ((w instanceof IReversibleTaintWrapper) && (curAbsSet = ((IReversibleTaintWrapper) w).getInverseTaintsForMethod(stmt, d1, taintedPath)) != null && !curAbsSet.isEmpty()) {
                return curAbsSet;
            }
        }
        return null;
    }
}
