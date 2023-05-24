package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/NotifyAllStmt.class */
public class NotifyAllStmt extends JPegStmt {
    public NotifyAllStmt(String obj, String ca, Unit un, UnitGraph ug, SootMethod sm) {
        this.object = obj;
        this.name = "notifyAll";
        this.caller = ca;
        this.unit = un;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }
}
