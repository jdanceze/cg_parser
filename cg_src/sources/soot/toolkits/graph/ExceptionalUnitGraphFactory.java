package soot.toolkits.graph;

import soot.Body;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalUnitGraphFactory.class */
public class ExceptionalUnitGraphFactory {
    public ExceptionalUnitGraphFactory(Singletons.Global g) {
    }

    public static ExceptionalUnitGraphFactory v() {
        return G.v().soot_toolkits_graph_ExceptionalUnitGraphFactory();
    }

    public static ExceptionalUnitGraph createExceptionalUnitGraph(Body body) {
        return v().newExceptionalUnitGraph(body, Scene.v().getDefaultThrowAnalysis(), Options.v().omit_excepting_unit_edges());
    }

    public static ExceptionalUnitGraph createExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis) {
        return v().newExceptionalUnitGraph(body, throwAnalysis, Options.v().omit_excepting_unit_edges());
    }

    public static ExceptionalUnitGraph createExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis, boolean omitExceptingUnitEdges) {
        return v().newExceptionalUnitGraph(body, throwAnalysis, omitExceptingUnitEdges);
    }

    protected ExceptionalUnitGraph newExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis, boolean omitExceptingUnitEdges) {
        return new ExceptionalUnitGraph(body, throwAnalysis, omitExceptingUnitEdges);
    }
}
