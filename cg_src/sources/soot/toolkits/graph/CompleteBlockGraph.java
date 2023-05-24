package soot.toolkits.graph;

import soot.Body;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/CompleteBlockGraph.class */
public class CompleteBlockGraph extends ExceptionalBlockGraph {
    public CompleteBlockGraph(Body b) {
        super(new CompleteUnitGraph(b));
    }
}
