package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.Value;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTUnaryCondition.class */
public class ASTUnaryCondition extends ASTUnaryBinaryCondition {
    Value value;

    public ASTUnaryCondition(Value value) {
        this.value = value;
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void apply(Analysis a) {
        a.caseASTUnaryCondition(this);
    }

    public Value getValue() {
        return this.value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String toString() {
        return this.value.toString();
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void toString(UnitPrinter up) {
        this.value.toString(up);
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void flip() {
        if (this.value instanceof DNotExpr) {
            this.value = ((DNotExpr) this.value).getOp();
        } else {
            this.value = new DNotExpr(this.value);
        }
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public boolean isNotted() {
        return this.value instanceof DNotExpr;
    }
}
