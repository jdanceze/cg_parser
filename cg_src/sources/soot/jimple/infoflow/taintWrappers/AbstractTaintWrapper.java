package soot.jimple.infoflow.taintWrappers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/taintWrappers/AbstractTaintWrapper.class */
public abstract class AbstractTaintWrapper implements ITaintPropagationWrapper {
    protected InfoflowManager manager;
    private final AtomicInteger wrapperHits = new AtomicInteger(0);
    private final AtomicInteger wrapperMisses = new AtomicInteger(0);

    protected abstract boolean isExclusiveInternal(Stmt stmt, AccessPath accessPath);

    public abstract Set<AccessPath> getTaintsForMethodInternal(Stmt stmt, AccessPath accessPath);

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public void initialize(InfoflowManager manager) {
        this.manager = manager;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public boolean isExclusive(Stmt stmt, Abstraction taintedPath) {
        if (isExclusiveInternal(stmt, taintedPath.getAccessPath())) {
            this.wrapperHits.incrementAndGet();
            return true;
        }
        this.wrapperMisses.incrementAndGet();
        return false;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public Set<Abstraction> getTaintsForMethod(Stmt stmt, Abstraction d1, Abstraction taintedPath) {
        Set<AccessPath> aps = getTaintsForMethodInternal(stmt, taintedPath.getAccessPath());
        if (aps == null || aps.isEmpty()) {
            return null;
        }
        Set<Abstraction> res = new HashSet<>(aps.size());
        for (AccessPath ap : aps) {
            if (ap == taintedPath.getAccessPath()) {
                res.add(taintedPath);
            } else {
                res.add(taintedPath.deriveNewAbstraction(ap, stmt));
            }
        }
        return res;
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public int getWrapperHits() {
        return this.wrapperHits.get();
    }

    @Override // soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper
    public int getWrapperMisses() {
        return this.wrapperMisses.get();
    }
}
