package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfNode;
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
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/StrengthenByIf.class */
public class StrengthenByIf {
    public static List<ASTNode> getNewNode(ASTNode loopNode, ASTIfNode ifNode) {
        List<Object> ifBody = ifNode.getIfBody();
        String label = isItOnlyBreak(ifBody);
        if (label != null) {
            if (((ASTLabeledNode) loopNode).get_Label().toString() != null && ((ASTLabeledNode) loopNode).get_Label().toString().compareTo(label) == 0) {
                if (loopNode instanceof ASTWhileNode) {
                    ASTCondition outerCond = ((ASTWhileNode) loopNode).get_Condition();
                    ASTCondition innerCond = ifNode.get_Condition();
                    innerCond.flip();
                    ASTCondition newCond = new ASTAndCondition(outerCond, innerCond);
                    List<Object> newWhileBody = new ArrayList<>();
                    SETNodeLabel newLabel = new SETNodeLabel();
                    List<ASTNode> toReturn = new ArrayList<>();
                    toReturn.add(new ASTWhileNode(newLabel, newCond, newWhileBody));
                    return toReturn;
                } else if (!(loopNode instanceof ASTDoWhileNode) && (loopNode instanceof ASTUnconditionalLoopNode)) {
                    ASTCondition innerCond2 = ifNode.get_Condition();
                    innerCond2.flip();
                    List<Object> newWhileBody2 = new ArrayList<>();
                    SETNodeLabel newLabel2 = new SETNodeLabel();
                    List<ASTNode> toReturn2 = new ArrayList<>();
                    toReturn2.add(new ASTWhileNode(newLabel2, innerCond2, newWhileBody2));
                    return toReturn2;
                } else {
                    return null;
                }
            }
            return null;
        } else if ((loopNode instanceof ASTUnconditionalLoopNode) && ifBody.size() == 1) {
            ASTNode tempNode = (ASTNode) ifBody.get(0);
            if (tempNode instanceof ASTStatementSequenceNode) {
                List<AugmentedStmt> statements = ((ASTStatementSequenceNode) tempNode).getStatements();
                Iterator<AugmentedStmt> stIt = statements.iterator();
                while (stIt.hasNext()) {
                    AugmentedStmt as = stIt.next();
                    Stmt stmt = as.get_Stmt();
                    if ((stmt instanceof DAbruptStmt) && !stIt.hasNext()) {
                        DAbruptStmt abStmt = (DAbruptStmt) stmt;
                        if (abStmt.is_Break()) {
                            String loopLabel = ((ASTLabeledNode) loopNode).get_Label().toString();
                            String breakLabel = abStmt.getLabel().toString();
                            if (loopLabel != null && breakLabel != null && loopLabel.compareTo(breakLabel) == 0) {
                                ASTCondition innerCond3 = ifNode.get_Condition();
                                innerCond3.flip();
                                List<Object> newWhileBody3 = new ArrayList<>();
                                SETNodeLabel newLabel3 = ((ASTUnconditionalLoopNode) loopNode).get_Label();
                                List<ASTNode> toReturn3 = new ArrayList<>();
                                toReturn3.add(new ASTWhileNode(newLabel3, innerCond3, newWhileBody3));
                                Iterator<AugmentedStmt> tempIt = statements.iterator();
                                List<AugmentedStmt> newStmts = new ArrayList<>();
                                while (tempIt.hasNext()) {
                                    AugmentedStmt tempStmt = tempIt.next();
                                    if (tempIt.hasNext()) {
                                        newStmts.add(tempStmt);
                                    }
                                }
                                toReturn3.add(new ASTStatementSequenceNode(newStmts));
                                return toReturn3;
                            }
                        } else {
                            continue;
                        }
                    } else if ((stmt instanceof ReturnStmt) || (stmt instanceof ReturnVoidStmt)) {
                        if (!stIt.hasNext()) {
                            ASTCondition innerCond4 = ifNode.get_Condition();
                            innerCond4.flip();
                            List<Object> newWhileBody4 = new ArrayList<>();
                            SETNodeLabel newLabel4 = new SETNodeLabel();
                            List<ASTNode> toReturn4 = new ArrayList<>();
                            toReturn4.add(new ASTWhileNode(newLabel4, innerCond4, newWhileBody4));
                            List<AugmentedStmt> newStmts2 = new ArrayList<>();
                            for (AugmentedStmt augmentedStmt : statements) {
                                newStmts2.add(augmentedStmt);
                            }
                            toReturn4.add(new ASTStatementSequenceNode(newStmts2));
                            return toReturn4;
                        }
                    }
                }
                return null;
            }
            return null;
        } else {
            return null;
        }
    }

    private static String isItOnlyBreak(List<Object> body) {
        if (body.size() != 1) {
            return null;
        }
        ASTNode tempNode = (ASTNode) body.get(0);
        if (!(tempNode instanceof ASTStatementSequenceNode)) {
            return null;
        }
        List<AugmentedStmt> statements = ((ASTStatementSequenceNode) tempNode).getStatements();
        if (statements.size() != 1) {
            return null;
        }
        AugmentedStmt as = statements.get(0);
        Stmt stmt = as.get_Stmt();
        if (!(stmt instanceof DAbruptStmt)) {
            return null;
        }
        DAbruptStmt abStmt = (DAbruptStmt) stmt;
        if (!abStmt.is_Break()) {
            return null;
        }
        return abStmt.getLabel().toString();
    }
}
