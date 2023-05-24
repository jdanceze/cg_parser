package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/WaitStmt.class */
public class WaitStmt extends JPegStmt {
    public WaitStmt(String obj, String ca, Unit un, UnitGraph ug, SootMethod sm) {
        this.object = obj;
        this.name = "wait";
        this.caller = ca;
        this.unit = un;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }
}
