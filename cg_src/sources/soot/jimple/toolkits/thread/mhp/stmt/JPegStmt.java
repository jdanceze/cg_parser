package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.tagkit.AbstractHost;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/stmt/JPegStmt.class */
public abstract class JPegStmt extends AbstractHost {
    protected String object;
    protected String name;
    protected String caller;
    protected Unit unit;
    protected UnitGraph unitGraph;
    protected SootMethod sootMethod;

    /* JADX INFO: Access modifiers changed from: protected */
    public JPegStmt() {
        this.unit = null;
        this.unitGraph = null;
        this.sootMethod = null;
    }

    protected JPegStmt(String obj, String na, String ca) {
        this.unit = null;
        this.unitGraph = null;
        this.sootMethod = null;
        this.object = obj;
        this.name = na;
        this.caller = ca;
    }

    protected JPegStmt(String obj, String na, String ca, SootMethod sm) {
        this.unit = null;
        this.unitGraph = null;
        this.sootMethod = null;
        this.object = obj;
        this.name = na;
        this.caller = ca;
        this.sootMethod = sm;
    }

    protected JPegStmt(String obj, String na, String ca, UnitGraph ug, SootMethod sm) {
        this.unit = null;
        this.unitGraph = null;
        this.sootMethod = null;
        this.object = obj;
        this.name = na;
        this.caller = ca;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }

    protected JPegStmt(String obj, String na, String ca, Unit un, UnitGraph ug, SootMethod sm) {
        this.unit = null;
        this.unitGraph = null;
        this.sootMethod = null;
        this.object = obj;
        this.name = na;
        this.caller = ca;
        this.unit = un;
        this.unitGraph = ug;
        this.sootMethod = sm;
    }

    protected void setUnit(Unit un) {
        this.unit = un;
    }

    protected void setUnitGraph(UnitGraph ug) {
        this.unitGraph = ug;
    }

    public UnitGraph getUnitGraph() {
        if (!containUnitGraph()) {
            throw new RuntimeException("This statement does not contain UnitGraph!");
        }
        return this.unitGraph;
    }

    public boolean containUnitGraph() {
        if (this.unitGraph == null) {
            return false;
        }
        return true;
    }

    public Unit getUnit() {
        if (!containUnit()) {
            throw new RuntimeException("This statement does not contain Unit!");
        }
        return this.unit;
    }

    public boolean containUnit() {
        if (this.unit == null) {
            return false;
        }
        return true;
    }

    public String getObject() {
        return this.object;
    }

    protected void setObject(String ob) {
        this.object = ob;
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String na) {
        this.name = na;
    }

    public String getCaller() {
        return this.caller;
    }

    protected void setCaller(String ca) {
        this.caller = ca;
    }

    public SootMethod getMethod() {
        return this.sootMethod;
    }

    public String toString() {
        if (this.sootMethod != null) {
            return "(" + getObject() + ", " + getName() + ", " + getCaller() + "," + this.sootMethod + ")";
        }
        return "(" + getObject() + ", " + getName() + ", " + getCaller() + ")";
    }

    public String testToString() {
        if (containUnit()) {
            if (this.sootMethod != null) {
                return "(" + getObject() + ", " + getName() + ", " + getCaller() + ", " + getUnit() + "," + this.sootMethod + ")";
            }
            return "(" + getObject() + ", " + getName() + ", " + getCaller() + ", " + getUnit() + ")";
        }
        return "(" + getObject() + ", " + getName() + ", " + getCaller() + ")";
    }
}
