package soot.jimple.infoflow.ipc;

import heros.InterproceduralCFG;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/ipc/MethodBasedIPCManager.class */
public abstract class MethodBasedIPCManager implements IIPCManager {
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract boolean isIPCMethod(SootMethod sootMethod);

    static {
        $assertionsDisabled = !MethodBasedIPCManager.class.desiredAssertionStatus();
    }

    @Override // soot.jimple.infoflow.ipc.IIPCManager
    public boolean isIPC(Stmt sCallSite, InterproceduralCFG<Unit, SootMethod> cfg) {
        if ($assertionsDisabled || sCallSite != null) {
            return sCallSite.containsInvokeExpr() && isIPCMethod(sCallSite.getInvokeExpr().getMethod());
        }
        throw new AssertionError();
    }
}
