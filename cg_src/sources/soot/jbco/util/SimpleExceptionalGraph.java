package soot.jbco.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.toolkits.graph.TrapUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jbco/util/SimpleExceptionalGraph.class */
public class SimpleExceptionalGraph extends TrapUnitGraph {
    public SimpleExceptionalGraph(Body body) {
        super(body);
        int size = this.unitChain.size();
        this.unitToSuccs = new HashMap((size * 2) + 1, 0.7f);
        this.unitToPreds = new HashMap((size * 2) + 1, 0.7f);
        buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
        buildSimpleExceptionalEdges(this.unitToSuccs, this.unitToPreds);
        buildHeadsAndTails();
    }

    protected void buildSimpleExceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
        for (Trap trap : this.body.getTraps()) {
            Unit handler = trap.getHandlerUnit();
            for (Unit pred : unitToPreds.get(trap.getBeginUnit())) {
                addEdge(unitToSuccs, unitToPreds, pred, handler);
            }
        }
    }
}
