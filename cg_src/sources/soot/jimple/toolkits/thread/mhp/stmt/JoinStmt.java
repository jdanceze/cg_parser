package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/JoinStmt.class */
public class JoinStmt extends JPegStmt {
    public JoinStmt(String obj, String ca, Unit un, UnitGraph ug, SootMethod sm) {
        this.object = obj;
        this.name = "join";
        this.caller = ca;
        this.unit = un;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }
}
