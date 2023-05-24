package soot.jimple.toolkits.annotation.purity;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityGraphBox.class */
public class PurityGraphBox {
    public PurityGraph g = new PurityGraph();

    public int hashCode() {
        return this.g.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PurityGraphBox) {
            PurityGraphBox oo = (PurityGraphBox) o;
            return this.g.equals(oo.g);
        }
        return false;
    }
}
