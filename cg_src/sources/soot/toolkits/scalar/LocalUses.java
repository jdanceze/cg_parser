package soot.toolkits.scalar;

import java.util.List;
import soot.Body;
import soot.G;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalUses.class */
public interface LocalUses {
    List<UnitValueBoxPair> getUsesOf(Unit unit);

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalUses$Factory.class */
    public static final class Factory {
        private Factory() {
        }

        public static LocalUses newLocalUses(Body body, LocalDefs localDefs) {
            return new SimpleLocalUses(body, localDefs);
        }

        public static LocalUses newLocalUses(UnitGraph graph, LocalDefs localDefs) {
            return newLocalUses(graph.getBody(), localDefs);
        }

        public static LocalUses newLocalUses(Body body) {
            return newLocalUses(body, G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(body));
        }

        public static LocalUses newLocalUses(UnitGraph graph) {
            return newLocalUses(graph.getBody(), G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(graph));
        }
    }
}
