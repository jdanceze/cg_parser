package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.NopUnit;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/NopEliminator.class */
public class NopEliminator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(NopEliminator.class);

    public NopEliminator(Singletons.Global g) {
    }

    public static NopEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_NopEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "] Removing nops...");
        }
        Chain<Trap> traps = b.getTraps();
        Chain<Unit> units = b.getUnits();
        Iterator<Unit> stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Unit u = stmtIt.next();
            if (u instanceof NopUnit) {
                boolean keepNop = false;
                if (units.getLast() == u) {
                    for (Trap t : traps) {
                        if (t.getEndUnit() == u) {
                            keepNop = true;
                        }
                    }
                }
                if (!keepNop) {
                    units.remove(u);
                }
            }
        }
    }
}
