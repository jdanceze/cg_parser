package soot.jimple.spark.geom.dataRep;

import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/ShapeNode.class */
public abstract class ShapeNode {
    public long I1;
    public long I2;
    public long E1;
    public boolean is_new = true;
    public ShapeNode next = null;

    public abstract ShapeNode makeDuplicate();

    public abstract boolean inclusionTest(ShapeNode shapeNode);

    public abstract boolean coverThisXValue(long j);

    public abstract void printSelf(PrintStream printStream);

    public abstract void copy(ShapeNode shapeNode);
}
