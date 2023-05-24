package soot.toolkits.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.options.Options;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/TrapUnitGraph.class */
public class TrapUnitGraph extends UnitGraph {
    public TrapUnitGraph(Body body) {
        super(body);
        int size = this.unitChain.size();
        if (Options.v().time()) {
            Timers.v().graphTimer.start();
        }
        this.unitToSuccs = new HashMap((size * 2) + 1, 0.7f);
        this.unitToPreds = new HashMap((size * 2) + 1, 0.7f);
        buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
        buildExceptionalEdges(this.unitToSuccs, this.unitToPreds);
        buildHeadsAndTails();
        if (Options.v().time()) {
            Timers.v().graphTimer.end();
        }
        PhaseDumper.v().dumpGraph(this, body);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildExceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
        for (Trap trap : this.body.getTraps()) {
            Unit catcher = trap.getHandlerUnit();
            Unit first = trap.getBeginUnit();
            Unit last = this.unitChain.getPredOf(trap.getEndUnit());
            Iterator<Unit> unitIt = this.unitChain.iterator(first, last);
            while (unitIt.hasNext()) {
                Unit trapped = unitIt.next();
                addEdge(unitToSuccs, unitToPreds, trapped, catcher);
            }
        }
    }
}
