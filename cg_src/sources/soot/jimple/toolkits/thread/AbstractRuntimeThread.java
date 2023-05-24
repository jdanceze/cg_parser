package soot.jimple.toolkits.thread;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethod;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/AbstractRuntimeThread.class */
public class AbstractRuntimeThread {
    Stmt joinStmt;
    Stmt startStmt = null;
    SootMethod startStmtMethod = null;
    List<Object> methods = new ArrayList();
    List<Object> runMethods = new ArrayList();
    boolean runsMany = false;
    boolean runsOnce = false;
    boolean runsOneAtATime = false;
    boolean startStmtHasMultipleReachingObjects = false;
    boolean startStmtMayBeRunMultipleTimes = false;
    boolean startMethodIsReentrant = false;
    boolean startMethodMayHappenInParallel = false;
    boolean isMainThread = false;

    public void setStartStmt(Stmt startStmt) {
        this.startStmt = startStmt;
    }

    public void setJoinStmt(Stmt joinStmt) {
        this.joinStmt = joinStmt;
    }

    public void setStartStmtMethod(SootMethod startStmtMethod) {
        this.startStmtMethod = startStmtMethod;
    }

    public SootMethod getStartStmtMethod() {
        return this.startStmtMethod;
    }

    public boolean containsMethod(Object method) {
        return this.methods.contains(method);
    }

    public void addMethod(Object method) {
        this.methods.add(method);
    }

    public void addRunMethod(Object method) {
        this.runMethods.add(method);
    }

    public List<Object> getRunMethods() {
        return this.runMethods;
    }

    public int methodCount() {
        return this.methods.size();
    }

    public Object getMethod(int methodNum) {
        return this.methods.get(methodNum);
    }

    public void setStartStmtHasMultipleReachingObjects() {
        this.startStmtHasMultipleReachingObjects = true;
    }

    public void setStartStmtMayBeRunMultipleTimes() {
        this.startStmtMayBeRunMultipleTimes = true;
    }

    public void setStartMethodIsReentrant() {
        this.startMethodIsReentrant = true;
    }

    public void setStartMethodMayHappenInParallel() {
        this.startMethodMayHappenInParallel = true;
    }

    public void setRunsMany() {
        this.runsMany = true;
        this.runsOnce = false;
        this.runsOneAtATime = false;
    }

    public void setRunsOnce() {
        this.runsMany = false;
        this.runsOnce = true;
        this.runsOneAtATime = false;
    }

    public void setRunsOneAtATime() {
        this.runsMany = false;
        this.runsOnce = false;
        this.runsOneAtATime = true;
    }

    public void setIsMainThread() {
        this.isMainThread = true;
    }

    public String toString() {
        String ret;
        String ret2;
        String ret3 = String.valueOf(this.isMainThread ? "Main Thread" : "User Thread") + " (" + (this.runsMany ? "Multi,  " : this.runsOnce ? "Single, " : this.runsOneAtATime ? "At-Once," : "ERROR");
        if (this.startStmtHasMultipleReachingObjects) {
            String ret4 = String.valueOf(ret3) + "MRO,";
            if (this.startMethodIsReentrant) {
                ret = String.valueOf(ret4) + "SMR";
            } else if (this.startMethodMayHappenInParallel) {
                ret = String.valueOf(ret4) + "MSP";
            } else if (this.startStmtMayBeRunMultipleTimes) {
                ret = String.valueOf(ret4) + "RMT";
            } else {
                ret = String.valueOf(ret4) + "ROT";
            }
        } else if (this.isMainThread) {
            ret = String.valueOf(ret3) + "---,---";
        } else {
            ret = String.valueOf(ret3) + "SRO,---";
        }
        String ret5 = String.valueOf(ret) + "): ";
        if (!this.isMainThread) {
            ret2 = String.valueOf(ret5) + "Started in " + this.startStmtMethod + " by " + this.startStmt + "\n";
        } else {
            ret2 = String.valueOf(ret5) + "\n";
        }
        if (this.joinStmt != null) {
            ret2 = String.valueOf(ret2) + "                               Joined  in " + this.startStmtMethod + " by " + this.joinStmt + "\n";
        }
        return String.valueOf(ret2) + this.methods.toString();
    }
}
