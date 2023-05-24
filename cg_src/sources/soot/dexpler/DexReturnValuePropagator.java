package soot.dexpler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexReturnValuePropagator.class */
public class DexReturnValuePropagator extends BodyTransformer {
    public static DexReturnValuePropagator v() {
        return new DexReturnValuePropagator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, DalvikThrowAnalysis.v(), true);
        LocalDefs localDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(graph);
        LocalUses localUses = null;
        LocalCreation localCreation = null;
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof ReturnStmt) {
                ReturnStmt retStmt = (ReturnStmt) u;
                if (retStmt.getOp() instanceof Local) {
                    List<Unit> defs = localDefs.getDefsOfAt((Local) retStmt.getOp(), retStmt);
                    if (defs.size() == 1 && (defs.get(0) instanceof AssignStmt)) {
                        AssignStmt assign = (AssignStmt) defs.get(0);
                        Value rightOp = assign.getRightOp();
                        Value leftOp = assign.getLeftOp();
                        if (rightOp instanceof Local) {
                            if (!isRedefined((Local) rightOp, u, assign, graph)) {
                                retStmt.setOp(rightOp);
                            }
                        } else if (rightOp instanceof Constant) {
                            retStmt.setOp(rightOp);
                        } else if (rightOp instanceof FieldRef) {
                            if (localUses == null) {
                                localUses = LocalUses.Factory.newLocalUses(body, localDefs);
                            }
                            if (localUses.getUsesOf(assign).size() == 1) {
                                if (localCreation == null) {
                                    localCreation = Scene.v().createLocalCreation(body.getLocals(), Jimple.RET);
                                }
                                Local newLocal = localCreation.newLocal(leftOp.getType());
                                assign.setLeftOp(newLocal);
                                retStmt.setOp(newLocal);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isRedefined(Local l, Unit unitUse, AssignStmt unitDef, UnitGraph graph) {
        List<Unit> workList = new ArrayList<>();
        workList.add(unitUse);
        Set<Unit> doneSet = new HashSet<>();
        while (!workList.isEmpty()) {
            Unit curStmt = workList.remove(0);
            if (doneSet.add(curStmt)) {
                for (Unit u : graph.getPredsOf(curStmt)) {
                    if (u != unitDef) {
                        if (u instanceof DefinitionStmt) {
                            DefinitionStmt defStmt = (DefinitionStmt) u;
                            if (defStmt.getLeftOp() == l) {
                                return true;
                            }
                        }
                        workList.add(u);
                    }
                }
                continue;
            }
        }
        return false;
    }
}
