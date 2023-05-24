package soot.jimple.toolkits.annotation.purity;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityThisNode.class */
public class PurityThisNode extends PurityParamNode {
    public static PurityThisNode node = new PurityThisNode();

    private PurityThisNode() {
        super(-1);
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityParamNode
    public String toString() {
        return "this";
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityParamNode, soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isInside() {
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityParamNode, soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isLoad() {
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityParamNode, soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isParam() {
        return true;
    }
}
