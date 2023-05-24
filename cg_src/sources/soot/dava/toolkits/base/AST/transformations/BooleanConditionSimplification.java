package soot.dava.toolkits.base.AST.transformations;

import soot.BooleanType;
import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.NeExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/BooleanConditionSimplification.class */
public class BooleanConditionSimplification extends DepthFirstAdapter {
    public BooleanConditionSimplification(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    public BooleanConditionSimplification() {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTIfNode(ASTIfNode node) {
        ASTCondition condition = node.get_Condition();
        if (condition instanceof ASTBinaryCondition) {
            ConditionExpr condExpr = ((ASTBinaryCondition) condition).getConditionExpr();
            Value unary = checkBooleanUse(condExpr);
            if (unary != null) {
                node.set_Condition(new ASTUnaryCondition(unary));
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTIfElseNode(ASTIfElseNode node) {
        ASTCondition condition = node.get_Condition();
        if (condition instanceof ASTBinaryCondition) {
            ConditionExpr condExpr = ((ASTBinaryCondition) condition).getConditionExpr();
            Value unary = checkBooleanUse(condExpr);
            if (unary != null) {
                node.set_Condition(new ASTUnaryCondition(unary));
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTWhileNode(ASTWhileNode node) {
        ASTCondition condition = node.get_Condition();
        if (condition instanceof ASTBinaryCondition) {
            ConditionExpr condExpr = ((ASTBinaryCondition) condition).getConditionExpr();
            Value unary = checkBooleanUse(condExpr);
            if (unary != null) {
                node.set_Condition(new ASTUnaryCondition(unary));
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTDoWhileNode(ASTDoWhileNode node) {
        ASTCondition condition = node.get_Condition();
        if (condition instanceof ASTBinaryCondition) {
            ConditionExpr condExpr = ((ASTBinaryCondition) condition).getConditionExpr();
            Value unary = checkBooleanUse(condExpr);
            if (unary != null) {
                node.set_Condition(new ASTUnaryCondition(unary));
            }
        }
    }

    private Value checkBooleanUse(ConditionExpr condition) {
        if ((condition instanceof NeExpr) || (condition instanceof EqExpr)) {
            Value op1 = condition.getOp1();
            Value op2 = condition.getOp2();
            if (op1 instanceof DIntConstant) {
                Type op1Type = ((DIntConstant) op1).type;
                if (op1Type instanceof BooleanType) {
                    return decideCondition(op2, ((DIntConstant) op1).toString(), condition);
                }
                return null;
            } else if (op2 instanceof DIntConstant) {
                Type op2Type = ((DIntConstant) op2).type;
                if (op2Type instanceof BooleanType) {
                    return decideCondition(op1, ((DIntConstant) op2).toString(), condition);
                }
                return null;
            } else {
                return null;
            }
        }
        return null;
    }

    private Value decideCondition(Value A, String truthString, ConditionExpr condition) {
        int truthValue;
        boolean notEqual;
        if (truthString.compareTo("false") == 0) {
            truthValue = 0;
        } else if (truthString.compareTo("true") == 0) {
            truthValue = 1;
        } else {
            throw new RuntimeException();
        }
        if (condition instanceof NeExpr) {
            notEqual = true;
        } else if (condition instanceof EqExpr) {
            notEqual = false;
        } else {
            throw new RuntimeException();
        }
        if (notEqual && truthValue == 0) {
            return A;
        }
        if (notEqual && truthValue == 1) {
            if (A instanceof DNotExpr) {
                return ((DNotExpr) A).getOp();
            }
            return new DNotExpr(A);
        } else if (!notEqual && truthValue == 0) {
            if (A instanceof DNotExpr) {
                return ((DNotExpr) A).getOp();
            }
            return new DNotExpr(A);
        } else if (!notEqual && truthValue == 1) {
            return A;
        } else {
            throw new RuntimeException();
        }
    }
}
