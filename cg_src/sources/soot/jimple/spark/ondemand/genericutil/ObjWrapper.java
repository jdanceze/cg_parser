package soot.jimple.spark.ondemand.genericutil;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/ObjWrapper.class */
public class ObjWrapper {
    public final Object wrapped;

    public ObjWrapper(Object wrapped) {
        this.wrapped = wrapped;
    }

    public String toString() {
        return "wrapped " + this.wrapped;
    }
}
