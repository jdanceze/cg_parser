package soot.toolkits.graph;

import soot.Body;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/CompleteUnitGraph.class */
public class CompleteUnitGraph extends ExceptionalUnitGraph {
    public CompleteUnitGraph(Body b) {
        super(b, PedanticThrowAnalysis.v(), false);
    }
}
