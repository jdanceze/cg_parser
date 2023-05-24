package soot.jimple.toolkits.annotation.purity;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityParamNode.class */
public class PurityParamNode implements PurityNode {
    private final int id;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityParamNode(int id) {
        this.id = id;
    }

    public String toString() {
        return "P_" + this.id;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object o) {
        return (o instanceof PurityParamNode) && this.id == ((PurityParamNode) o).id;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isInside() {
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isLoad() {
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isParam() {
        return true;
    }
}
