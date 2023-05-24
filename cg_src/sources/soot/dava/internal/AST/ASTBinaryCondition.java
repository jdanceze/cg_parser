package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.dava.toolkits.base.misc.ConditionFlipper;
import soot.jimple.ConditionExpr;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTBinaryCondition.class */
public class ASTBinaryCondition extends ASTUnaryBinaryCondition {
    ConditionExpr condition;

    public ASTBinaryCondition(ConditionExpr condition) {
        this.condition = condition;
    }

    public ConditionExpr getConditionExpr() {
        return this.condition;
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void apply(Analysis a) {
        a.caseASTBinaryCondition(this);
    }

    public String toString() {
        return this.condition.toString();
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void toString(UnitPrinter up) {
        Jimple.v().newConditionExprBox(this.condition).toString(up);
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void flip() {
        this.condition = ConditionFlipper.flip(this.condition);
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public boolean isNotted() {
        return true;
    }
}
