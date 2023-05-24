package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/AndAggregator.class */
public class AndAggregator extends DepthFirstAdapter {
    public AndAggregator() {
    }

    public AndAggregator(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTIfNode(ASTIfNode node) {
        List<Object> bodies = node.get_SubBodies();
        if (bodies.size() == 1) {
            List body = (List) bodies.get(0);
            if (body.size() == 1) {
                ASTNode bodyNode = (ASTNode) body.get(0);
                if (bodyNode instanceof ASTIfNode) {
                    ASTCondition outerCond = node.get_Condition();
                    ASTCondition innerCond = ((ASTIfNode) bodyNode).get_Condition();
                    SETNodeLabel outerLabel = node.get_Label();
                    SETNodeLabel innerLabel = ((ASTIfNode) bodyNode).get_Label();
                    SETNodeLabel newLabel = null;
                    if (outerLabel.toString() == null && innerLabel.toString() == null) {
                        newLabel = outerLabel;
                    } else if (outerLabel.toString() != null && innerLabel.toString() == null) {
                        newLabel = outerLabel;
                    } else if (outerLabel.toString() == null && innerLabel.toString() != null) {
                        newLabel = innerLabel;
                    } else if (outerLabel.toString() != null && innerLabel.toString() != null) {
                        newLabel = outerLabel;
                        changeUses(outerLabel.toString(), innerLabel.toString(), bodyNode);
                    }
                    ASTCondition newCond = new ASTAndCondition(outerCond, innerCond);
                    List<Object> newBodyList = ((ASTIfNode) bodyNode).get_SubBodies();
                    if (newBodyList.size() == 1) {
                        List<Object> newBody = (List) newBodyList.get(0);
                        node.replace(newLabel, newCond, newBody);
                        G.v().ASTTransformations_modified = true;
                    }
                }
            }
        }
    }

    private void changeUses(String to, String from, ASTNode node) {
        List<ASTNode> subBodyNodes;
        List<Object> subBodies = node.get_SubBodies();
        Iterator<Object> it = subBodies.iterator();
        while (it.hasNext()) {
            if (node instanceof ASTStatementSequenceNode) {
                ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode) node;
                for (AugmentedStmt as : stmtSeq.getStatements()) {
                    Stmt s = as.get_Stmt();
                    if (s instanceof DAbruptStmt) {
                        DAbruptStmt abStmt = (DAbruptStmt) s;
                        if (abStmt.is_Break() || abStmt.is_Continue()) {
                            SETNodeLabel label = abStmt.getLabel();
                            String labelBroken = label.toString();
                            if (labelBroken != null && labelBroken.compareTo(from) == 0) {
                                label.set_Name(to);
                            }
                        }
                    }
                }
            } else {
                if (node instanceof ASTTryNode) {
                    ASTTryNode.container subBody = (ASTTryNode.container) it.next();
                    subBodyNodes = (List) subBody.o;
                } else {
                    subBodyNodes = (List) it.next();
                }
                for (ASTNode aSTNode : subBodyNodes) {
                    changeUses(to, from, aSTNode);
                }
            }
        }
    }
}
