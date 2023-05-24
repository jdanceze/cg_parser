package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/OrAggregatorOne.class */
public class OrAggregatorOne extends DepthFirstAdapter {
    public OrAggregatorOne() {
    }

    public OrAggregatorOne(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
        ASTLabeledBlockNode secondLabeledBlockNode;
        String innerLabel;
        ASTCondition newCond;
        String outerLabel = node.get_Label().toString();
        if (outerLabel == null || (secondLabeledBlockNode = isLabelWithinLabel(node)) == null || (innerLabel = secondLabeledBlockNode.get_Label().toString()) == null) {
            return;
        }
        List secondLabelsBodies = getSecondLabeledBlockBodies(secondLabeledBlockNode);
        boolean allIfs = checkAllAreIfsWithProperBreaks(secondLabelsBodies.iterator(), outerLabel, innerLabel);
        if (!allIfs) {
            return;
        }
        List<ASTCondition> conditions = getConditions(secondLabelsBodies.iterator());
        Iterator<ASTCondition> condIt = conditions.iterator();
        ASTCondition aSTCondition = null;
        while (true) {
            newCond = aSTCondition;
            if (!condIt.hasNext()) {
                break;
            }
            ASTCondition next = condIt.next();
            if (newCond == null) {
                aSTCondition = next;
            } else {
                aSTCondition = new ASTOrCondition(newCond, next);
            }
        }
        List<Object> newIfBody = new ArrayList<>();
        List<Object> subBodies = node.get_SubBodies();
        List labeledBlockBody = (List) subBodies.get(0);
        Iterator subBodiesIt = labeledBlockBody.iterator();
        subBodiesIt.next();
        while (subBodiesIt.hasNext()) {
            ASTNode temp = (ASTNode) subBodiesIt.next();
            newIfBody.add(temp);
        }
        Object newNode = new ASTIfNode(new SETNodeLabel(), newCond, newIfBody);
        List<Object> newLabeledBlockBody = new ArrayList<>();
        newLabeledBlockBody.add(newNode);
        G.v().ASTTransformations_modified = true;
        node.replaceBody(newLabeledBlockBody);
        UselessLabelFinder.v().findAndKill(node);
    }

    private ASTLabeledBlockNode isLabelWithinLabel(ASTLabeledBlockNode node) {
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() == 0) {
            node.set_Label(new SETNodeLabel());
            return null;
        }
        List bodies = (List) subBodies.get(0);
        if (bodies.size() == 0) {
            node.set_Label(new SETNodeLabel());
            return null;
        }
        ASTNode firstBody = (ASTNode) bodies.get(0);
        if (!(firstBody instanceof ASTLabeledBlockNode)) {
            return null;
        }
        return (ASTLabeledBlockNode) firstBody;
    }

    private List getSecondLabeledBlockBodies(ASTLabeledBlockNode secondLabeledBlockNode) {
        List<Object> secondLabelsSubBodies = secondLabeledBlockNode.get_SubBodies();
        if (secondLabelsSubBodies.size() == 0) {
            secondLabeledBlockNode.set_Label(new SETNodeLabel());
            return null;
        }
        List secondLabelsBodies = (List) secondLabelsSubBodies.get(0);
        return secondLabelsBodies;
    }

    private boolean checkAllAreIfsWithProperBreaks(Iterator it, String outerLabel, String innerLabel) {
        String labelBroken;
        while (it.hasNext()) {
            ASTNode secondLabelsBody = (ASTNode) it.next();
            Stmt stmt = isIfNodeWithOneStatement(secondLabelsBody);
            if (stmt == null || (labelBroken = breaksLabel(stmt)) == null) {
                return false;
            }
            if (labelBroken.compareTo(innerLabel) != 0 || !it.hasNext()) {
                if (labelBroken.compareTo(outerLabel) != 0 || it.hasNext()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String breaksLabel(Stmt stmt) {
        if (!(stmt instanceof DAbruptStmt)) {
            return null;
        }
        DAbruptStmt abStmt = (DAbruptStmt) stmt;
        if (!abStmt.is_Break()) {
            return null;
        }
        SETNodeLabel label = abStmt.getLabel();
        return label.toString();
    }

    private Stmt isIfNodeWithOneStatement(ASTNode secondLabelsBody) {
        if (!(secondLabelsBody instanceof ASTIfNode)) {
            return null;
        }
        ASTIfNode ifNode = (ASTIfNode) secondLabelsBody;
        List<Object> ifSubBodies = ifNode.get_SubBodies();
        if (ifSubBodies.size() != 1) {
            return null;
        }
        List ifBody = (List) ifSubBodies.get(0);
        if (ifBody.size() != 1) {
            return null;
        }
        ASTNode ifBodysBody = (ASTNode) ifBody.get(0);
        if (!(ifBodysBody instanceof ASTStatementSequenceNode)) {
            return null;
        }
        List<AugmentedStmt> statements = ((ASTStatementSequenceNode) ifBodysBody).getStatements();
        if (statements.size() != 1) {
            return null;
        }
        AugmentedStmt as = statements.get(0);
        Stmt s = as.get_Stmt();
        return s;
    }

    private List<ASTCondition> getConditions(Iterator it) {
        List<ASTCondition> toReturn = new ArrayList<>();
        while (it.hasNext()) {
            ASTIfNode node = (ASTIfNode) it.next();
            ASTCondition cond = node.get_Condition();
            if (it.hasNext()) {
                toReturn.add(cond);
            } else {
                cond.flip();
                toReturn.add(cond);
            }
        }
        return toReturn;
    }
}
