package soot.toolkits.scalar;

import soot.Body;
import soot.Singletons;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.SimpleLocalDefs;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalDefsFactory.class */
public class LocalDefsFactory {
    public LocalDefsFactory(Singletons.Global global) {
        if (global == null) {
            throw new NullPointerException("Cannot instantiate LocalDefsFactory with null Singletons.Global");
        }
    }

    public LocalDefs newLocalDefs(Body body) {
        return newLocalDefs(body, false);
    }

    public LocalDefs newLocalDefs(Body body, boolean expectUndefined) {
        return newLocalDefs(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body), expectUndefined);
    }

    public LocalDefs newLocalDefs(UnitGraph graph) {
        return newLocalDefs(graph, false);
    }

    public LocalDefs newLocalDefs(UnitGraph graph, boolean expectUndefined) {
        return new SimpleLocalDefs(graph, expectUndefined ? SimpleLocalDefs.FlowAnalysisMode.OmitSSA : SimpleLocalDefs.FlowAnalysisMode.Automatic);
    }

    public LocalDefs newLocalDefsFlowInsensitive(UnitGraph graph) {
        return new SimpleLocalDefs(graph, SimpleLocalDefs.FlowAnalysisMode.FlowInsensitive);
    }
}
