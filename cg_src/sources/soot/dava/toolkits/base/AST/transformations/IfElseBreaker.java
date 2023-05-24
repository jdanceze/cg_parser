package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/IfElseBreaker.class */
public class IfElseBreaker {
    ASTIfNode newIfNode = null;
    List<Object> remainingBody = null;

    public boolean isIfElseBreakingPossiblePatternOne(ASTIfElseNode node) {
        List<Object> ifBody = node.getIfBody();
        if (ifBody.size() != 1) {
            return false;
        }
        ASTNode onlyNode = (ASTNode) ifBody.get(0);
        boolean check = checkStmt(onlyNode, node);
        if (!check) {
            return false;
        }
        this.newIfNode = new ASTIfNode(node.get_Label(), node.get_Condition(), ifBody);
        this.remainingBody = node.getElseBody();
        return true;
    }

    public boolean isIfElseBreakingPossiblePatternTwo(ASTIfElseNode node) {
        List<Object> elseBody = node.getElseBody();
        if (elseBody.size() != 1) {
            return false;
        }
        ASTNode onlyNode = (ASTNode) elseBody.get(0);
        boolean check = checkStmt(onlyNode, node);
        if (!check) {
            return false;
        }
        ASTCondition cond = node.get_Condition();
        cond.flip();
        this.newIfNode = new ASTIfNode(node.get_Label(), cond, elseBody);
        this.remainingBody = node.getIfBody();
        return true;
    }

    private boolean checkStmt(ASTNode onlyNode, ASTIfElseNode node) {
        String breakLabel;
        if (!(onlyNode instanceof ASTStatementSequenceNode)) {
            return false;
        }
        ASTStatementSequenceNode stmtNode = (ASTStatementSequenceNode) onlyNode;
        List<AugmentedStmt> statements = stmtNode.getStatements();
        if (statements.size() != 1) {
            return false;
        }
        AugmentedStmt as = statements.get(0);
        Stmt stmt = as.get_Stmt();
        if (!(stmt instanceof DAbruptStmt)) {
            return false;
        }
        DAbruptStmt abStmt = (DAbruptStmt) stmt;
        if (!abStmt.is_Break() && !abStmt.is_Continue()) {
            return false;
        }
        SETNodeLabel ifLabel = node.get_Label();
        if (ifLabel != null && ifLabel.toString() != null && abStmt.is_Break() && (breakLabel = abStmt.getLabel().toString()) != null && breakLabel.compareTo(ifLabel.toString()) == 0) {
            return false;
        }
        return true;
    }

    public List<Object> createNewBody(List<Object> oldSubBody, int nodeNumber) {
        if (this.newIfNode == null) {
            return null;
        }
        List<Object> newSubBody = new ArrayList<>();
        if (oldSubBody.size() <= nodeNumber) {
            return null;
        }
        Iterator<Object> oldIt = oldSubBody.iterator();
        for (int index = 0; index != nodeNumber; index++) {
            newSubBody.add(oldIt.next());
        }
        ASTNode temp = (ASTNode) oldIt.next();
        if (!(temp instanceof ASTIfElseNode)) {
            return null;
        }
        newSubBody.add(this.newIfNode);
        newSubBody.addAll(this.remainingBody);
        while (oldIt.hasNext()) {
            newSubBody.add(oldIt.next());
        }
        return newSubBody;
    }
}
