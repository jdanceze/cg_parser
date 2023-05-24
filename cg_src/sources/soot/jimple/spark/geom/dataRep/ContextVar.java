package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.Node;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/ContextVar.class */
public abstract class ContextVar implements Numberable {
    public Node var = null;
    public int id = -1;

    public abstract boolean contains(ContextVar contextVar);

    public abstract boolean merge(ContextVar contextVar);

    public abstract boolean intersect(ContextVar contextVar);

    @Override // soot.util.Numberable
    public void setNumber(int number) {
        this.id = number;
    }

    @Override // soot.util.Numberable
    public int getNumber() {
        return this.id;
    }
}
