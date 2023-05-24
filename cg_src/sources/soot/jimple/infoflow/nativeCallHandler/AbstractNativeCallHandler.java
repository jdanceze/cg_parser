package soot.jimple.infoflow.nativeCallHandler;

import soot.jimple.infoflow.InfoflowManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/nativeCallHandler/AbstractNativeCallHandler.class */
public abstract class AbstractNativeCallHandler implements INativeCallHandler {
    protected InfoflowManager manager;

    @Override // soot.jimple.infoflow.nativeCallHandler.INativeCallHandler
    public void initialize(InfoflowManager manager) {
        this.manager = manager;
    }

    @Override // soot.jimple.infoflow.nativeCallHandler.INativeCallHandler
    public void shutdown() {
    }
}
