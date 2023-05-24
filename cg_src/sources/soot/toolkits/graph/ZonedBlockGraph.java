package soot.toolkits.graph;

import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ZonedBlockGraph.class */
public class ZonedBlockGraph extends BlockGraph {
    public ZonedBlockGraph(Body body) {
        this(new BriefUnitGraph(body));
    }

    public ZonedBlockGraph(BriefUnitGraph unitGraph) {
        super(unitGraph);
        PhaseDumper.v().dumpGraph(this, this.mBody);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.graph.BlockGraph
    public Set<Unit> computeLeaders(UnitGraph unitGraph) {
        Body body = unitGraph.getBody();
        if (body != this.mBody) {
            throw new RuntimeException("ZonedBlockGraph.computeLeaders() called with a UnitGraph that doesn't match its mBody.");
        }
        Set<Unit> leaders = super.computeLeaders(unitGraph);
        for (Trap trap : body.getTraps()) {
            leaders.add(trap.getBeginUnit());
            leaders.add(trap.getEndUnit());
        }
        return leaders;
    }
}
