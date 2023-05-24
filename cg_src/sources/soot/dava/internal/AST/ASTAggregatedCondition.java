package soot.dava.internal.AST;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTAggregatedCondition.class */
public abstract class ASTAggregatedCondition extends ASTCondition {
    ASTCondition left;
    ASTCondition right;
    boolean not = false;

    public ASTAggregatedCondition(ASTCondition left, ASTCondition right) {
        this.left = left;
        this.right = right;
    }

    public ASTCondition getLeftOp() {
        return this.left;
    }

    public ASTCondition getRightOp() {
        return this.right;
    }

    public void setLeftOp(ASTCondition left) {
        this.left = left;
    }

    public void setRightOp(ASTCondition right) {
        this.right = right;
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void flip() {
        if (this.not) {
            this.not = false;
        } else {
            this.not = true;
        }
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public boolean isNotted() {
        return this.not;
    }
}
