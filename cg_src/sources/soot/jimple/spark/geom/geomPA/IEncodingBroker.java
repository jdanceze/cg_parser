package soot.jimple.spark.geom.geomPA;

import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/IEncodingBroker.class */
public abstract class IEncodingBroker {
    public abstract IVarAbstraction generateNode(Node node);

    public abstract void initFlowGraph(GeomPointsTo geomPointsTo);

    public abstract String getSignature();
}
