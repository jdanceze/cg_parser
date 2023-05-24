package soot.dava.toolkits.base.AST.transformations;

import soot.Value;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DDecrementStmt;
import soot.dava.internal.javaRep.DIncrementStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.AddExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/DecrementIncrementStmtCreation.class */
public class DecrementIncrementStmtCreation extends DepthFirstAdapter {
    public DecrementIncrementStmtCreation() {
    }

    public DecrementIncrementStmtCreation(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
                Value left = ((DefinitionStmt) s).getLeftOp();
                Value right = ((DefinitionStmt) s).getRightOp();
                if (right instanceof SubExpr) {
                    Value op1 = ((SubExpr) right).getOp1();
                    Value op2 = ((SubExpr) right).getOp2();
                    if (left.toString().compareTo(op1.toString()) == 0 && (op2 instanceof IntConstant)) {
                        if (((IntConstant) op2).value == 1) {
                            DDecrementStmt newStmt = new DDecrementStmt(left, right);
                            as.set_Stmt(newStmt);
                        } else if (((IntConstant) op2).value == -1) {
                            DIncrementStmt newStmt2 = new DIncrementStmt(left, right);
                            as.set_Stmt(newStmt2);
                        }
                    }
                } else if (right instanceof AddExpr) {
                    Value op12 = ((AddExpr) right).getOp1();
                    Value op22 = ((AddExpr) right).getOp2();
                    if (left.toString().compareTo(op12.toString()) == 0 && (op22 instanceof IntConstant)) {
                        if (((IntConstant) op22).value == 1) {
                            DIncrementStmt newStmt3 = new DIncrementStmt(left, right);
                            as.set_Stmt(newStmt3);
                        } else if (((IntConstant) op22).value == -1) {
                            DDecrementStmt newStmt4 = new DDecrementStmt(left, right);
                            as.set_Stmt(newStmt4);
                        }
                    }
                }
            }
        }
    }
}
