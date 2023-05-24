package soot.jimple.infoflow.aliasing;

import soot.SootMethod;
import soot.jimple.infoflow.InfoflowManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/AbstractAliasStrategy.class */
public abstract class AbstractAliasStrategy implements IAliasingStrategy {
    protected final InfoflowManager manager;

    public AbstractAliasStrategy(InfoflowManager manager) {
        this.manager = manager;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean hasProcessedMethod(SootMethod method) {
        return true;
    }
}
