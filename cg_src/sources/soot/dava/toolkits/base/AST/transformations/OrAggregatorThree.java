package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/OrAggregatorThree.class */
public class OrAggregatorThree {
    public static void checkAndTransform(ASTNode node, ASTIfNode ifOne, ASTIfNode ifTwo, int nodeNumber, int subBodyNumber) {
        if (!(node instanceof ASTIfElseNode)) {
            List<Object> subBodies = node.get_SubBodies();
            if (subBodies.size() != 1) {
                throw new RuntimeException("Please report this benchmark to the programmer");
            }
            List<Object> onlySubBody = (List) subBodies.get(0);
            List<Object> newBody = createNewNodeBody(onlySubBody, nodeNumber, ifOne, ifTwo);
            if (newBody == null) {
                return;
            }
            if (node instanceof ASTMethodNode) {
                ((ASTMethodNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            } else if (node instanceof ASTSynchronizedBlockNode) {
                ((ASTSynchronizedBlockNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            } else if (node instanceof ASTLabeledBlockNode) {
                ((ASTLabeledBlockNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            } else if (node instanceof ASTUnconditionalLoopNode) {
                ((ASTUnconditionalLoopNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            } else if (node instanceof ASTIfNode) {
                ((ASTIfNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            } else if (node instanceof ASTWhileNode) {
                ((ASTWhileNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            } else if (node instanceof ASTDoWhileNode) {
                ((ASTDoWhileNode) node).replaceBody(newBody);
                G.v().ASTTransformations_modified = true;
            }
        } else if (subBodyNumber != 0 && subBodyNumber != 1) {
        } else {
            List<Object> subBodies2 = node.get_SubBodies();
            if (subBodies2.size() != 2) {
                throw new RuntimeException("Please report this benchmark to the programmer");
            }
            List<Object> toModifySubBody = (List) subBodies2.get(subBodyNumber);
            List<Object> newBody2 = createNewNodeBody(toModifySubBody, nodeNumber, ifOne, ifTwo);
            if (newBody2 == null) {
                return;
            }
            if (subBodyNumber == 0) {
                G.v().ASTTransformations_modified = true;
                ((ASTIfElseNode) node).replaceBody(newBody2, (List) subBodies2.get(1));
            } else if (subBodyNumber == 1) {
                G.v().ASTTransformations_modified = true;
                ((ASTIfElseNode) node).replaceBody((List) subBodies2.get(0), newBody2);
            }
        }
    }

    public static List<Object> createNewNodeBody(List<Object> oldSubBody, int nodeNumber, ASTIfNode ifOne, ASTIfNode ifTwo) {
        if (!matchPattern(ifOne, ifTwo)) {
            return null;
        }
        List<Object> newSubBody = new ArrayList<>();
        Iterator<Object> it = oldSubBody.iterator();
        for (int index = 0; index != nodeNumber; index++) {
            if (!it.hasNext()) {
                return null;
            }
            newSubBody.add(it.next());
        }
        ASTNode isItIfOne = (ASTNode) it.next();
        if (!(isItIfOne instanceof ASTIfNode)) {
            return null;
        }
        ASTNode isItIfTwo = (ASTNode) it.next();
        if (!(isItIfTwo instanceof ASTIfNode) || !matchPattern((ASTIfNode) isItIfOne, (ASTIfNode) isItIfTwo)) {
            return null;
        }
        ASTIfNode firstOne = (ASTIfNode) isItIfOne;
        ASTIfNode secondOne = (ASTIfNode) isItIfTwo;
        ASTCondition firstCond = firstOne.get_Condition();
        ASTCondition secondCond = secondOne.get_Condition();
        ASTCondition newCond = new ASTOrCondition(firstCond, secondCond);
        ASTIfNode newNode = new ASTIfNode(firstOne.get_Label(), newCond, firstOne.getIfBody());
        newSubBody.add(newNode);
        while (it.hasNext()) {
            newSubBody.add(it.next());
        }
        return newSubBody;
    }

    private static boolean matchPattern(ASTIfNode one, ASTIfNode two) {
        List<Object> subBodiesOne = one.get_SubBodies();
        List<Object> subBodiesTwo = two.get_SubBodies();
        if (subBodiesOne.size() != 1 || subBodiesTwo.size() != 1) {
            return false;
        }
        List onlySubBodyOne = (List) subBodiesOne.get(0);
        List onlySubBodyTwo = (List) subBodiesTwo.get(0);
        if (onlySubBodyOne.size() != 1 || onlySubBodyTwo.size() != 1) {
            return false;
        }
        ASTNode onlyASTNodeOne = (ASTNode) onlySubBodyOne.get(0);
        ASTNode onlyASTNodeTwo = (ASTNode) onlySubBodyTwo.get(0);
        if (!(onlyASTNodeOne instanceof ASTStatementSequenceNode) || !(onlyASTNodeTwo instanceof ASTStatementSequenceNode)) {
            return false;
        }
        ASTStatementSequenceNode stmtSeqOne = (ASTStatementSequenceNode) onlyASTNodeOne;
        ASTStatementSequenceNode stmtSeqTwo = (ASTStatementSequenceNode) onlyASTNodeTwo;
        List<AugmentedStmt> stmtsOne = stmtSeqOne.getStatements();
        List<AugmentedStmt> stmtsTwo = stmtSeqTwo.getStatements();
        if (stmtsOne.size() != 1 || stmtsTwo.size() != 1) {
            return false;
        }
        AugmentedStmt asOne = stmtsOne.get(0);
        AugmentedStmt asTwo = stmtsTwo.get(0);
        Stmt s1 = asOne.get_Stmt();
        Stmt s2 = asTwo.get_Stmt();
        if (s1.toString().compareTo(s2.toString()) != 0) {
            return false;
        }
        if ((s1 instanceof DAbruptStmt) && (s2 instanceof DAbruptStmt)) {
            return true;
        }
        if ((s1 instanceof ReturnStmt) && (s2 instanceof ReturnStmt)) {
            return true;
        }
        if ((s1 instanceof ReturnVoidStmt) && (s2 instanceof ReturnVoidStmt)) {
            return true;
        }
        return false;
    }
}
