package soot.toolkits.scalar;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/UnusedLocalEliminator.class */
public class UnusedLocalEliminator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(UnusedLocalEliminator.class);

    public UnusedLocalEliminator(Singletons.Global g) {
    }

    public static UnusedLocalEliminator v() {
        return G.v().soot_toolkits_scalar_UnusedLocalEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Eliminating unused locals...");
        }
        Chain<Local> locals = body.getLocals();
        int numLocals = locals.size();
        int[] oldNumbers = new int[numLocals];
        int i = 0;
        for (Local local : locals) {
            oldNumbers[i] = local.getNumber();
            local.setNumber(i);
            i++;
        }
        BitSet usedLocals = new BitSet(numLocals);
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            for (ValueBox vb : s.getUseBoxes()) {
                Value v = vb.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    usedLocals.set(l.getNumber());
                }
            }
            for (ValueBox vb2 : s.getDefBoxes()) {
                Value v2 = vb2.getValue();
                if (v2 instanceof Local) {
                    Local l2 = (Local) v2;
                    usedLocals.set(l2.getNumber());
                }
            }
        }
        Iterator<Local> localIt = locals.iterator();
        while (localIt.hasNext()) {
            Local local2 = localIt.next();
            int lno = local2.getNumber();
            if (!usedLocals.get(lno)) {
                localIt.remove();
            } else {
                local2.setNumber(oldNumbers[lno]);
            }
        }
    }
}
