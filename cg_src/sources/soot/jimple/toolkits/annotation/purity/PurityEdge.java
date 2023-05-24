package soot.jimple.toolkits.annotation.purity;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityEdge.class */
public class PurityEdge {
    private final String field;
    private final PurityNode source;
    private final PurityNode target;
    private final boolean inside;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityEdge(PurityNode source, String field, PurityNode target, boolean inside) {
        this.source = source;
        this.field = field;
        this.target = target;
        this.inside = inside;
    }

    public String getField() {
        return this.field;
    }

    public PurityNode getTarget() {
        return this.target;
    }

    public PurityNode getSource() {
        return this.source;
    }

    public boolean isInside() {
        return this.inside;
    }

    public int hashCode() {
        return this.field.hashCode() + this.target.hashCode() + this.source.hashCode() + (this.inside ? 69 : 0);
    }

    public boolean equals(Object o) {
        if (!(o instanceof PurityEdge)) {
            return false;
        }
        PurityEdge e = (PurityEdge) o;
        return this.source.equals(e.source) && this.field.equals(e.field) && this.target.equals(e.target) && this.inside == e.inside;
    }

    public String toString() {
        if (this.inside) {
            return String.valueOf(this.source.toString()) + " = " + this.field + " => " + this.target.toString();
        }
        return String.valueOf(this.source.toString()) + " - " + this.field + " -> " + this.target.toString();
    }
}
