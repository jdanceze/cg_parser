package soot.jimple.spark.sets;

import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/sets/P2SetVisitor.class */
public abstract class P2SetVisitor {
    protected boolean returnValue = false;

    public abstract void visit(Node node);

    public boolean getReturnValue() {
        return this.returnValue;
    }
}
