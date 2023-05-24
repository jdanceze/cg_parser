package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.DavaUnitPrinter;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTAndCondition.class */
public class ASTAndCondition extends ASTAggregatedCondition {
    public ASTAndCondition(ASTCondition left, ASTCondition right) {
        super(left, right);
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void apply(Analysis a) {
        a.caseASTAndCondition(this);
    }

    public String toString() {
        if (this.left instanceof ASTUnaryBinaryCondition) {
            if (this.right instanceof ASTUnaryBinaryCondition) {
                if (this.not) {
                    return "!(" + this.left.toString() + " && " + this.right.toString() + ")";
                }
                return String.valueOf(this.left.toString()) + " && " + this.right.toString();
            } else if (this.not) {
                return "!(" + this.left.toString() + " && (" + this.right.toString() + " ))";
            } else {
                return String.valueOf(this.left.toString()) + " && (" + this.right.toString() + " )";
            }
        } else if (this.right instanceof ASTUnaryBinaryCondition) {
            if (this.not) {
                return "!(( " + this.left.toString() + ") && " + this.right.toString() + ")";
            }
            return "( " + this.left.toString() + ") && " + this.right.toString();
        } else if (this.not) {
            return "!(( " + this.left.toString() + ") && (" + this.right.toString() + " ))";
        } else {
            return "( " + this.left.toString() + ") && (" + this.right.toString() + " )";
        }
    }

    @Override // soot.dava.internal.AST.ASTCondition
    public void toString(UnitPrinter up) {
        if (up instanceof DavaUnitPrinter) {
            if (this.not) {
                ((DavaUnitPrinter) up).addNot();
                ((DavaUnitPrinter) up).addLeftParen();
            }
            if (this.left instanceof ASTUnaryBinaryCondition) {
                if (this.right instanceof ASTUnaryBinaryCondition) {
                    this.left.toString(up);
                    ((DavaUnitPrinter) up).addAggregatedAnd();
                    this.right.toString(up);
                } else {
                    this.left.toString(up);
                    ((DavaUnitPrinter) up).addAggregatedAnd();
                    ((DavaUnitPrinter) up).addLeftParen();
                    this.right.toString(up);
                    ((DavaUnitPrinter) up).addRightParen();
                }
            } else if (this.right instanceof ASTUnaryBinaryCondition) {
                ((DavaUnitPrinter) up).addLeftParen();
                this.left.toString(up);
                ((DavaUnitPrinter) up).addRightParen();
                ((DavaUnitPrinter) up).addAggregatedAnd();
                this.right.toString(up);
            } else {
                ((DavaUnitPrinter) up).addLeftParen();
                this.left.toString(up);
                ((DavaUnitPrinter) up).addRightParen();
                ((DavaUnitPrinter) up).addAggregatedAnd();
                ((DavaUnitPrinter) up).addLeftParen();
                this.right.toString(up);
                ((DavaUnitPrinter) up).addRightParen();
            }
            if (this.not) {
                ((DavaUnitPrinter) up).addRightParen();
                return;
            }
            return;
        }
        throw new RuntimeException();
    }
}
