package soot.jimple.spark.pag;

import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/ArrayElement.class */
public class ArrayElement implements SparkField {
    private int number = 0;

    public ArrayElement(Singletons.Global g) {
    }

    public static ArrayElement v() {
        return G.v().soot_jimple_spark_pag_ArrayElement();
    }

    public ArrayElement() {
        Scene.v().getFieldNumberer().add(this);
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    @Override // soot.jimple.spark.pag.SparkField
    public Type getType() {
        return Scene.v().getObjectType();
    }
}
