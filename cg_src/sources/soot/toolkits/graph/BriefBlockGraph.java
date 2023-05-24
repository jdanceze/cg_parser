package soot.toolkits.graph;

import soot.Body;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/BriefBlockGraph.class */
public class BriefBlockGraph extends BlockGraph {
    public BriefBlockGraph(Body body) {
        this(new BriefUnitGraph(body));
    }

    public BriefBlockGraph(BriefUnitGraph unitGraph) {
        super(unitGraph);
        PhaseDumper.v().dumpGraph(this, this.mBody);
    }
}
