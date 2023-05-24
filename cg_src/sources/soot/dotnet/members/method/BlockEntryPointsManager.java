package soot.dotnet.members.method;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.TableSwitchStmt;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/method/BlockEntryPointsManager.class */
public class BlockEntryPointsManager {
    private final HashMap<String, Unit> methodBlockEntryPoints = new HashMap<>();
    public final HashMap<Unit, String> gotoTargetsInBody = new HashMap<>();

    public void putBlockEntryPoint(String blockName, Unit entryUnit) {
        this.methodBlockEntryPoints.put(blockName, entryUnit);
    }

    public Unit getBlockEntryPoint(String blockName) {
        return this.methodBlockEntryPoints.get(blockName);
    }

    public void swapGotoEntriesInJBody(Body jb) {
        Unit unitToSwap;
        Unit unitToSwap2;
        Unit unitToSwap3;
        Unit unitToSwap4;
        this.methodBlockEntryPoints.put("RETURNLEAVE", jb.getUnits().getLast());
        Iterator<Unit> it = jb.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit instanceof GotoStmt) {
                String entryPointString = this.gotoTargetsInBody.get(((GotoStmt) unit).getTarget());
                if (entryPointString != null && (unitToSwap4 = this.methodBlockEntryPoints.get(entryPointString)) != null) {
                    ((GotoStmt) unit).setTarget(unitToSwap4);
                }
            }
            if (unit instanceof IfStmt) {
                String entryPointString2 = this.gotoTargetsInBody.get(((IfStmt) unit).getTarget());
                if (entryPointString2 != null && (unitToSwap3 = this.methodBlockEntryPoints.get(entryPointString2)) != null) {
                    ((IfStmt) unit).setTarget(unitToSwap3);
                }
            }
            if (unit instanceof TableSwitchStmt) {
                TableSwitchStmt tableSwitchStmt = (TableSwitchStmt) unit;
                List<Unit> targets = tableSwitchStmt.getTargets();
                for (int i = 0; i < targets.size(); i++) {
                    Unit target = targets.get(i);
                    String entryPointString3 = this.gotoTargetsInBody.get(target);
                    if (entryPointString3 != null && (unitToSwap2 = this.methodBlockEntryPoints.get(entryPointString3)) != null) {
                        tableSwitchStmt.setTarget(i, unitToSwap2);
                    }
                }
                String entryPointStringDefault = this.gotoTargetsInBody.get(tableSwitchStmt.getDefaultTarget());
                if (entryPointStringDefault != null && (unitToSwap = this.methodBlockEntryPoints.get(entryPointStringDefault)) != null) {
                    tableSwitchStmt.setDefaultTarget(unitToSwap);
                }
            }
        }
    }

    public void swapGotoEntryUnit(Unit in, Unit out) {
        for (Map.Entry<String, Unit> set : this.methodBlockEntryPoints.entrySet()) {
            if (set.getValue() == out) {
                set.setValue(in);
            }
        }
    }
}
