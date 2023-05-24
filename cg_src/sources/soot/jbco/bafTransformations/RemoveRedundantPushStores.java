package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.baf.PushInst;
import soot.baf.StoreInst;
import soot.jbco.IJbcoTransform;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/RemoveRedundantPushStores.class */
public class RemoveRedundantPushStores extends BodyTransformer implements IJbcoTransform {
    public static String[] dependancies = {"bb.jbco_rrps"};
    public static String name = "bb.jbco_rrps";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        boolean changed = true;
        PatchingChain<Unit> units = b.getUnits();
        while (changed) {
            changed = false;
            Unit prevprevprev = null;
            Unit prevprev = null;
            Unit prev = null;
            ExceptionalUnitGraph eug = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
            Iterator<Unit> it = units.snapshotIterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Unit u = it.next();
                if (prev != null && (prev instanceof PushInst) && (u instanceof StoreInst) && prevprev != null && (prevprev instanceof StoreInst) && prevprevprev != null && (prevprevprev instanceof PushInst)) {
                    Local lprev = ((StoreInst) prevprev).getLocal();
                    Local l = ((StoreInst) u).getLocal();
                    if (l == lprev && eug.getSuccsOf(prevprevprev).size() == 1 && eug.getSuccsOf(prevprev).size() == 1) {
                        fixJumps(prevprevprev, prev, b.getTraps());
                        fixJumps(prevprev, u, b.getTraps());
                        units.remove(prevprevprev);
                        units.remove(prevprev);
                        changed = true;
                        break;
                    }
                }
                prevprevprev = prevprev;
                prevprev = prev;
                prev = u;
            }
        }
    }

    private void fixJumps(Unit from, Unit to, Chain<Trap> t) {
        from.redirectJumpsToThisTo(to);
        for (Trap trap : t) {
            if (trap.getBeginUnit() == from) {
                trap.setBeginUnit(to);
            }
            if (trap.getEndUnit() == from) {
                trap.setEndUnit(to);
            }
            if (trap.getHandlerUnit() == from) {
                trap.setHandlerUnit(to);
            }
        }
    }
}
