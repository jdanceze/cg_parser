package soot.dexpler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.toolkits.exceptions.TrapTransformer;
import soot.toolkits.graph.ExceptionalGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
/* loaded from: gencallgraphv3.jar:soot/dexpler/TrapMinimizer.class */
public class TrapMinimizer extends TrapTransformer {
    public TrapMinimizer(Singletons.Global g) {
    }

    public static TrapMinimizer v() {
        return G.v().soot_dexpler_TrapMinimizer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (b.getTraps().size() == 0) {
            return;
        }
        ExceptionalUnitGraph eug = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b, DalvikThrowAnalysis.v(), Options.v().omit_excepting_unit_edges());
        Set<Unit> unitsWithMonitor = getUnitsWithMonitor(eug);
        Map<Trap, List<Trap>> replaceTrapBy = new HashMap<>(b.getTraps().size());
        for (Trap tr : b.getTraps()) {
            List<Trap> newTraps = new ArrayList<>();
            Unit firstTrapStmt = tr.getBeginUnit();
            boolean goesToHandler = false;
            boolean updateTrap = false;
            Unit beginUnit = tr.getBeginUnit();
            while (true) {
                Unit u = beginUnit;
                if (u == tr.getEndUnit()) {
                    break;
                }
                if (goesToHandler) {
                    goesToHandler = false;
                } else {
                    firstTrapStmt = u;
                }
                if (tr.getException().getName().equals("java.lang.Throwable") && unitsWithMonitor.contains(u)) {
                    goesToHandler = true;
                }
                if (!goesToHandler && DalvikThrowAnalysis.v().mightThrow(u).catchableAs(tr.getException().getType())) {
                    Iterator<ExceptionalUnitGraph.ExceptionDest> it = eug.getExceptionDests(u).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ExceptionalGraph.ExceptionDest<Unit> ed = it.next();
                        if (ed.getTrap() == tr) {
                            goesToHandler = true;
                            break;
                        }
                    }
                }
                if (!goesToHandler) {
                    updateTrap = true;
                    if (firstTrapStmt != u) {
                        Trap t = Jimple.v().newTrap(tr.getException(), firstTrapStmt, u, tr.getHandlerUnit());
                        newTraps.add(t);
                    }
                } else if (b.getUnits().getSuccOf((UnitPatchingChain) u) == tr.getEndUnit() && updateTrap) {
                    Trap t2 = Jimple.v().newTrap(tr.getException(), firstTrapStmt, tr.getEndUnit(), tr.getHandlerUnit());
                    newTraps.add(t2);
                }
                beginUnit = b.getUnits().getSuccOf((UnitPatchingChain) u);
            }
            if (updateTrap) {
                replaceTrapBy.put(tr, newTraps);
            }
        }
        for (Trap k : replaceTrapBy.keySet()) {
            b.getTraps().insertAfter((List<List<Trap>>) replaceTrapBy.get(k), (List<Trap>) k);
            b.getTraps().remove(k);
        }
    }
}
