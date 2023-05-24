package soot.jimple.infoflow.ipc;

import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/ipc/EmptyIPCManager.class */
public class EmptyIPCManager extends MethodBasedIPCManager {
    @Override // soot.jimple.infoflow.ipc.MethodBasedIPCManager
    public boolean isIPCMethod(SootMethod method) {
        return false;
    }

    @Override // soot.jimple.infoflow.ipc.IIPCManager
    public void updateJimpleForICC() {
    }
}
