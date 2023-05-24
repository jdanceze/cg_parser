package soot.toolkits.scalar;

import com.google.common.collect.Maps;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Singletons;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SmartLocalDefsPool.class */
public class SmartLocalDefsPool {
    protected Map<Body, Pair<Long, SmartLocalDefs>> pool = Maps.newHashMap();

    public SmartLocalDefs getSmartLocalDefsFor(Body b) {
        Pair<Long, SmartLocalDefs> modCountAndSLD = this.pool.get(b);
        if (modCountAndSLD != null && modCountAndSLD.o1.longValue() == b.getModificationCount()) {
            return modCountAndSLD.o2;
        }
        ExceptionalUnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
        SmartLocalDefs newSLD = new SmartLocalDefs(g, new SimpleLiveLocals(g));
        this.pool.put(b, new Pair<>(Long.valueOf(b.getModificationCount()), newSLD));
        return newSLD;
    }

    public void clear() {
        this.pool.clear();
    }

    public static SmartLocalDefsPool v() {
        return G.v().soot_toolkits_scalar_SmartLocalDefsPool();
    }

    public SmartLocalDefsPool(Singletons.Global g) {
    }

    public void invalidate(Body b) {
        this.pool.remove(b);
    }
}
