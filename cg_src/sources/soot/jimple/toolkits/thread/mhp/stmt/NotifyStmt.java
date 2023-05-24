package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/NotifyStmt.class */
public class NotifyStmt extends JPegStmt {
    public NotifyStmt(String obj, String ca, Unit un, UnitGraph ug, SootMethod sm) {
        this.object = obj;
        this.name = "notify";
        this.caller = ca;
        this.unit = un;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }
}
