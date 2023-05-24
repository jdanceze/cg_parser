package soot.jimple.toolkits.annotation.purity;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityGlobalNode.class */
public class PurityGlobalNode implements PurityNode {
    public static final PurityGlobalNode node = new PurityGlobalNode();

    private PurityGlobalNode() {
    }

    public String toString() {
        return "GBL";
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof PurityGlobalNode;
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
        return false;
    }
}
