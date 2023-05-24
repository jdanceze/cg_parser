package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/BeginStmt.class */
public class BeginStmt extends JPegStmt {
    public BeginStmt(String obj, String ca, UnitGraph ug, SootMethod sm) {
        this.object = obj;
        this.name = "begin";
        this.caller = ca;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }
}
