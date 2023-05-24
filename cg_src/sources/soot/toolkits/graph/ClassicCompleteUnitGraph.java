package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Trap;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ClassicCompleteUnitGraph.class */
public class ClassicCompleteUnitGraph extends TrapUnitGraph {
    public ClassicCompleteUnitGraph(Body body) {
        super(body);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.graph.TrapUnitGraph
    public void buildExceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
        super.buildExceptionalEdges(unitToSuccs, unitToPreds);
        for (Trap trap : this.body.getTraps()) {
            Unit catcher = trap.getHandlerUnit();
            Iterator it = new ArrayList(getPredsOf(trap.getBeginUnit())).iterator();
            while (it.hasNext()) {
                Unit pred = (Unit) it.next();
                addEdge(unitToSuccs, unitToPreds, pred, catcher);
            }
        }
    }
}
