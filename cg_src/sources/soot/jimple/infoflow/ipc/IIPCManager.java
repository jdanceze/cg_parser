package soot.jimple.infoflow.ipc;

import heros.InterproceduralCFG;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/ipc/IIPCManager.class */
public interface IIPCManager {
    boolean isIPC(Stmt stmt, InterproceduralCFG<Unit, SootMethod> interproceduralCFG);

    void updateJimpleForICC();
}
