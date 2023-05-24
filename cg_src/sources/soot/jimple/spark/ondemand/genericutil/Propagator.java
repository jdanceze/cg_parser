package soot.jimple.spark.ondemand.genericutil;

import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/Propagator.class */
public class Propagator<T> {
    private final Set<T> marked;
    private final Stack<T> worklist;

    public Propagator(Set<T> marked, Stack<T> worklist) {
        this.marked = marked;
        this.worklist = worklist;
    }

    public void prop(T val) {
        if (this.marked.add(val)) {
            this.worklist.push(val);
        }
    }
}
