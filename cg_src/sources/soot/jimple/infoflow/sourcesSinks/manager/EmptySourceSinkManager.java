package soot.jimple.infoflow.sourcesSinks.manager;

import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/EmptySourceSinkManager.class */
public class EmptySourceSinkManager extends MethodBasedSourceSinkManager {
    @Override // soot.jimple.infoflow.sourcesSinks.manager.MethodBasedSourceSinkManager
    public SourceInfo getSourceMethodInfo(SootMethod sMethod) {
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.MethodBasedSourceSinkManager
    public SinkInfo getSinkMethodInfo(SootMethod sMethod) {
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public void initialize() {
    }
}
