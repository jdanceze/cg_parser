package soot.jimple.spark.geom.geomPA;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/IWorklist.class */
public interface IWorklist {
    void initialize(int i);

    boolean has_job();

    IVarAbstraction next();

    void push(IVarAbstraction iVarAbstraction);

    int size();

    void clear();
}
