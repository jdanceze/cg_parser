package soot.toolkits.exceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.toolkits.graph.UnitGraph;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/TrapTransformer.class */
public abstract class TrapTransformer extends BodyTransformer {
    public Set<Unit> getUnitsWithMonitor(UnitGraph ug) {
        MultiMap<Unit, Value> unitMonitors = new HashMultiMap<>();
        List<Unit> workList = new ArrayList<>();
        Set<Unit> doneSet = new HashSet<>();
        for (Unit head : ug.getHeads()) {
            workList.add(head);
        }
        while (!workList.isEmpty()) {
            Unit curUnit = workList.remove(0);
            boolean hasChanged = false;
            Value exitValue = null;
            if (curUnit instanceof EnterMonitorStmt) {
                EnterMonitorStmt ems = (EnterMonitorStmt) curUnit;
                hasChanged = unitMonitors.put(curUnit, ems.getOp());
            } else if (curUnit instanceof ExitMonitorStmt) {
                ExitMonitorStmt ems2 = (ExitMonitorStmt) curUnit;
                exitValue = ems2.getOp();
            }
            for (Unit pred : ug.getPredsOf(curUnit)) {
                for (Value v : unitMonitors.get(pred)) {
                    if (v != exitValue && unitMonitors.put(curUnit, v)) {
                        hasChanged = true;
                    }
                }
            }
            if (doneSet.add(curUnit) || hasChanged) {
                workList.addAll(ug.getSuccsOf(curUnit));
            }
        }
        return unitMonitors.keySet();
    }
}
