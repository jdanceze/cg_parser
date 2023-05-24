package soot.jimple.infoflow.ipc;

import java.util.List;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/ipc/DefaultIPCManager.class */
public class DefaultIPCManager extends MethodBasedIPCManager {
    private List<String> ipcMethods;

    public DefaultIPCManager(List<String> ipcMethods) {
        this.ipcMethods = ipcMethods;
    }

    public void setSinks(List<String> ipcMethods) {
        this.ipcMethods = ipcMethods;
    }

    @Override // soot.jimple.infoflow.ipc.MethodBasedIPCManager
    public boolean isIPCMethod(SootMethod sMethod) {
        return this.ipcMethods.contains(sMethod.toString());
    }

    @Override // soot.jimple.infoflow.ipc.IIPCManager
    public void updateJimpleForICC() {
    }
}
