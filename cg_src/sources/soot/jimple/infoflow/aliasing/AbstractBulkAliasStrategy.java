package soot.jimple.infoflow.aliasing;

import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/AbstractBulkAliasStrategy.class */
public abstract class AbstractBulkAliasStrategy extends AbstractAliasStrategy {
    public AbstractBulkAliasStrategy(InfoflowManager manager) {
        super(manager);
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isInteractive() {
        return false;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean mayAlias(AccessPath ap1, AccessPath ap2) {
        return false;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isLazyAnalysis() {
        return false;
    }
}
