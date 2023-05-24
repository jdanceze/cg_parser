package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/PushLabeledBlockIn.class */
public class PushLabeledBlockIn extends DepthFirstAdapter {
    public PushLabeledBlockIn() {
    }

    public PushLabeledBlockIn(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
        List subBody;
        int nodeNumber;
        String label = node.get_Label().toString();
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() == 1 && (nodeNumber = checkForBreak((subBody = (List) subBodies.get(0)), label)) > -1) {
            if (subBody.size() < nodeNumber) {
                throw new RuntimeException("Please submit this benchmark as a bug");
            }
            if (nodeNumber + 1 != subBody.size()) {
                return;
            }
            ASTNode temp = (ASTNode) subBody.get(nodeNumber);
            if (!(temp instanceof ASTLabeledNode)) {
                return;
            }
            ASTLabeledNode tempNode = (ASTLabeledNode) temp;
            String innerLabel = tempNode.get_Label().toString();
            if (innerLabel != null) {
                if (subBody.size() == 1) {
                    boolean done = replaceBreakLabels(temp, label, innerLabel);
                    if (done) {
                        node.set_Label(new SETNodeLabel());
                        G.v().ASTTransformations_modified = true;
                        return;
                    }
                    return;
                }
                return;
            }
            SETNodeLabel newLabel = new SETNodeLabel();
            newLabel.set_Name(label);
            tempNode.set_Label(newLabel);
            node.set_Label(new SETNodeLabel());
            G.v().ASTTransformations_modified = true;
        }
    }

    private boolean replaceBreakLabels(ASTNode node, String toReplace, String replaceWith) {
        List<ASTNode> subBody;
        boolean toReturn = false;
        List<Object> subBodies = node.get_SubBodies();
        Iterator<Object> subIt = subBodies.iterator();
        while (subIt.hasNext()) {
            if (node instanceof ASTTryNode) {
                ASTTryNode.container subBodyContainer = (ASTTryNode.container) subIt.next();
                subBody = (List) subBodyContainer.o;
            } else {
                subBody = (List) subIt.next();
            }
            for (ASTNode temp : subBody) {
                if (temp instanceof ASTStatementSequenceNode) {
                    ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode) temp;
                    for (AugmentedStmt as : stmtSeq.getStatements()) {
                        Stmt s = as.get_Stmt();
                        String labelBroken = isAbrupt(s);
                        if (labelBroken != null && labelBroken.compareTo(toReplace) == 0) {
                            replaceLabel(s, replaceWith);
                            toReturn = true;
                        }
                    }
                } else {
                    boolean returnVal = replaceBreakLabels(temp, toReplace, replaceWith);
                    if (returnVal) {
                        toReturn = true;
                    }
                }
            }
        }
        return toReturn;
    }

    private int checkForBreak(List ASTNodeBody, String outerLabel) {
        Iterator it = ASTNodeBody.iterator();
        int nodeNumber = 0;
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (temp instanceof ASTStatementSequenceNode) {
                ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode) temp;
                for (AugmentedStmt as : stmtSeq.getStatements()) {
                    Stmt s = as.get_Stmt();
                    String labelBroken = breaksLabel(s);
                    if (labelBroken != null && outerLabel != null && labelBroken.compareTo(outerLabel) == 0) {
                        return nodeNumber;
                    }
                }
                continue;
            } else {
                List<Object> subBodies = temp.get_SubBodies();
                Iterator<Object> subIt = subBodies.iterator();
                while (subIt.hasNext()) {
                    if (temp instanceof ASTTryNode) {
                        ASTTryNode.container subBody = (ASTTryNode.container) subIt.next();
                        if (checkForBreak((List) subBody.o, outerLabel) > -1) {
                            return nodeNumber;
                        }
                    } else if (checkForBreak((List) subIt.next(), outerLabel) > -1) {
                        return nodeNumber;
                    }
                }
                continue;
            }
            nodeNumber++;
        }
        return -1;
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

    private String isAbrupt(Stmt stmt) {
        if (!(stmt instanceof DAbruptStmt)) {
            return null;
        }
        DAbruptStmt abStmt = (DAbruptStmt) stmt;
        if (abStmt.is_Break() || abStmt.is_Continue()) {
            SETNodeLabel label = abStmt.getLabel();
            return label.toString();
        }
        return null;
    }

    private void replaceLabel(Stmt s, String replaceWith) {
        DAbruptStmt abStmt = (DAbruptStmt) s;
        SETNodeLabel label = abStmt.getLabel();
        label.set_Name(replaceWith);
    }
}
