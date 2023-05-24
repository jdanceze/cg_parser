package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/StrengthenByIfElse.class */
public class StrengthenByIfElse {
    public static List<ASTNode> getNewNode(ASTNode loopNode, ASTIfElseNode ifElseNode) {
        ASTNode newWhileNode;
        ASTNode newWhileNode2;
        List<Object> elseBody = ifElseNode.getElseBody();
        if (elseBody.size() != 1) {
            return null;
        }
        ASTNode tempNode = (ASTNode) elseBody.get(0);
        if (!(tempNode instanceof ASTStatementSequenceNode)) {
            return null;
        }
        List<AugmentedStmt> statements = ((ASTStatementSequenceNode) tempNode).getStatements();
        Iterator<AugmentedStmt> stmtIt = statements.iterator();
        while (stmtIt.hasNext()) {
            AugmentedStmt as = stmtIt.next();
            Stmt stmt = as.get_Stmt();
            if (stmt instanceof DAbruptStmt) {
                DAbruptStmt abStmt = (DAbruptStmt) stmt;
                if (!abStmt.is_Break() || stmtIt.hasNext()) {
                    return null;
                }
                SETNodeLabel label = abStmt.getLabel();
                String labelBroken = label.toString();
                String loopLabel = ((ASTLabeledNode) loopNode).get_Label().toString();
                if (labelBroken != null && loopLabel != null && labelBroken.compareTo(loopLabel) == 0) {
                    if (((loopNode instanceof ASTWhileNode) && statements.size() != 1) || (newWhileNode = makeWhileNode(ifElseNode, loopNode)) == null) {
                        return null;
                    }
                    List<ASTNode> toReturn = new ArrayList<>();
                    toReturn.add(newWhileNode);
                    if (statements.size() != 1) {
                        Iterator<AugmentedStmt> tempIt = statements.iterator();
                        List<AugmentedStmt> newStmts = new ArrayList<>();
                        while (tempIt.hasNext()) {
                            AugmentedStmt tempStmt = tempIt.next();
                            if (tempIt.hasNext()) {
                                newStmts.add(tempStmt);
                            }
                        }
                        toReturn.add(new ASTStatementSequenceNode(newStmts));
                    }
                    return toReturn;
                }
            } else if ((stmt instanceof ReturnStmt) || (stmt instanceof ReturnVoidStmt)) {
                if (!(loopNode instanceof ASTUnconditionalLoopNode) || stmtIt.hasNext() || (newWhileNode2 = makeWhileNode(ifElseNode, loopNode)) == null) {
                    return null;
                }
                List<ASTNode> toReturn2 = new ArrayList<>();
                toReturn2.add(newWhileNode2);
                toReturn2.add(new ASTStatementSequenceNode(new ArrayList<>(statements)));
                return toReturn2;
            }
        }
        return null;
    }

    private static ASTWhileNode makeWhileNode(ASTIfElseNode ifElseNode, ASTNode loopNode) {
        ASTCondition newCond;
        ASTCondition innerCond = ifElseNode.get_Condition();
        if (loopNode instanceof ASTWhileNode) {
            ASTCondition outerCond = ((ASTWhileNode) loopNode).get_Condition();
            newCond = new ASTAndCondition(outerCond, innerCond);
        } else if (loopNode instanceof ASTUnconditionalLoopNode) {
            newCond = innerCond;
        } else {
            return null;
        }
        List<Object> loopBody = ifElseNode.getIfBody();
        SETNodeLabel newLabel = ((ASTLabeledNode) loopNode).get_Label();
        return new ASTWhileNode(newLabel, newCond, loopBody);
    }
}
