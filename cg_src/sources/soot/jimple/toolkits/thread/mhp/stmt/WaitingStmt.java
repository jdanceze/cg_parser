package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/WaitingStmt.class */
public class WaitingStmt extends JPegStmt {
    public WaitingStmt(String obj, String ca, SootMethod sm) {
        this.object = obj;
        this.name = "waiting";
        this.caller = ca;
        this.sootMethod = sm;
    }
}
