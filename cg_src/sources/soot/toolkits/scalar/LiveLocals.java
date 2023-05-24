package soot.toolkits.scalar;

import java.util.List;
import soot.Local;
import soot.Unit;
import soot.toolkits.graph.DirectedBodyGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LiveLocals.class */
public interface LiveLocals {
    List<Local> getLiveLocalsBefore(Unit unit);

    List<Local> getLiveLocalsAfter(Unit unit);

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LiveLocals$Factory.class */
    public static final class Factory {
        private Factory() {
        }

        public static LiveLocals newLiveLocals(DirectedBodyGraph<Unit> graph) {
            return new SimpleLiveLocals(graph);
        }
    }
}
