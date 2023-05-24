package soot.toDex;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toDex/SynchronizedMethodTransformer.class */
public class SynchronizedMethodTransformer extends BodyTransformer {
    public SynchronizedMethodTransformer(Singletons.Global g) {
    }

    public static SynchronizedMethodTransformer v() {
        return G.v().soot_toDex_SynchronizedMethodTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (!b.getMethod().isSynchronized() || b.getMethod().isStatic()) {
            return;
        }
        Iterator<Unit> it = b.getUnits().snapshotIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (!(u instanceof IdentityStmt)) {
                if (!(u instanceof EnterMonitorStmt)) {
                    b.getUnits().insertBeforeNoRedirect(Jimple.v().newEnterMonitorStmt(b.getThisLocal()), (EnterMonitorStmt) u);
                    UnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
                    for (Unit tail : graph.getTails()) {
                        b.getUnits().insertBefore(Jimple.v().newExitMonitorStmt(b.getThisLocal()), (ExitMonitorStmt) tail);
                    }
                    return;
                }
                return;
            }
        }
    }
}
