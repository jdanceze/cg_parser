package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/EmptySwitchEliminator.class */
public class EmptySwitchEliminator extends BodyTransformer {
    public EmptySwitchEliminator(Singletons.Global g) {
    }

    public static EmptySwitchEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_EmptySwitchEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Unit defaultTarget;
        Chain<Unit> units = b.getUnits();
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof LookupSwitchStmt) {
                LookupSwitchStmt sw = (LookupSwitchStmt) u;
                if (sw.getTargetCount() == 0 && (defaultTarget = sw.getDefaultTarget()) != null) {
                    units.swapWith(sw, Jimple.v().newGotoStmt(defaultTarget));
                }
            }
        }
    }
}
