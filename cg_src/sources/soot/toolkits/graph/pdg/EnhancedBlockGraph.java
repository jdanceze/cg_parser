package soot.toolkits.graph.pdg;

import soot.Body;
import soot.toolkits.graph.BlockGraph;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/EnhancedBlockGraph.class */
public class EnhancedBlockGraph extends BlockGraph {
    public EnhancedBlockGraph(Body body) {
        this(new EnhancedUnitGraph(body));
    }

    public EnhancedBlockGraph(EnhancedUnitGraph unitGraph) {
        super(unitGraph);
        PhaseDumper.v().dumpGraph(this, this.mBody);
    }
}
