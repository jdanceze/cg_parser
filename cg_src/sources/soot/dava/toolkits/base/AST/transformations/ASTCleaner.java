package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/ASTCleaner.class */
public class ASTCleaner extends DepthFirstAdapter {
    public ASTCleaner() {
    }

    public ASTCleaner(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void normalRetrieving(ASTNode node) {
        if (node instanceof ASTSwitchNode) {
            dealWithSwitchNode((ASTSwitchNode) node);
            return;
        }
        int subBodyNumber = 0;
        for (Object subBody : node.get_SubBodies()) {
            Iterator it = ((List) subBody).iterator();
            int nodeNumber = 0;
            while (it.hasNext()) {
                ASTNode temp = (ASTNode) it.next();
                if (temp instanceof ASTLabeledBlockNode) {
                    ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode) temp;
                    SETNodeLabel label = labelBlock.get_Label();
                    if (label.toString() == null) {
                        UselessLabeledBlockRemover.removeLabeledBlock(node, labelBlock, subBodyNumber, nodeNumber);
                        if (G.v().ASTTransformations_modified) {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (temp instanceof ASTIfElseNode) {
                    List<Object> elseBody = ((ASTIfElseNode) temp).getElseBody();
                    if (elseBody.size() == 0) {
                        EmptyElseRemover.removeElseBody(node, (ASTIfElseNode) temp, subBodyNumber, nodeNumber);
                    }
                } else if ((temp instanceof ASTIfNode) && it.hasNext()) {
                    ASTNode nextNode = (ASTNode) ((List) subBody).get(nodeNumber + 1);
                    if (nextNode instanceof ASTIfNode) {
                        OrAggregatorThree.checkAndTransform(node, (ASTIfNode) temp, (ASTIfNode) nextNode, nodeNumber, subBodyNumber);
                        if (G.v().ASTTransformations_modified) {
                            return;
                        }
                    } else {
                        continue;
                    }
                }
                temp.apply(this);
                nodeNumber++;
            }
            subBodyNumber++;
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTTryNode(ASTTryNode node) {
        List<Object> newBody;
        List<Object> newBody2;
        List<Object> newBody3;
        List<Object> newBody4;
        List<Object> newBody5;
        List<Object> newBody6;
        inASTTryNode(node);
        List<Object> tryBody = node.get_TryBody();
        Iterator<Object> it = tryBody.iterator();
        int nodeNumber = 0;
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (temp instanceof ASTLabeledBlockNode) {
                ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode) temp;
                SETNodeLabel label = labelBlock.get_Label();
                if (label.toString() == null && (newBody6 = UselessLabeledBlockRemover.createNewSubBody(tryBody, nodeNumber, labelBlock)) != null) {
                    node.replaceTryBody(newBody6);
                    G.v().ASTTransformations_modified = true;
                }
            } else if (temp instanceof ASTIfElseNode) {
                List<Object> elseBody = ((ASTIfElseNode) temp).getElseBody();
                if (elseBody.size() == 0 && (newBody5 = EmptyElseRemover.createNewNodeBody(tryBody, nodeNumber, (ASTIfElseNode) temp)) != null) {
                    node.replaceTryBody(newBody5);
                    G.v().ASTTransformations_modified = true;
                    return;
                }
            } else if ((temp instanceof ASTIfNode) && it.hasNext()) {
                ASTNode nextNode = (ASTNode) tryBody.get(nodeNumber + 1);
                if ((nextNode instanceof ASTIfNode) && (newBody4 = OrAggregatorThree.createNewNodeBody(tryBody, nodeNumber, (ASTIfNode) temp, (ASTIfNode) nextNode)) != null) {
                    node.replaceTryBody(newBody4);
                    G.v().ASTTransformations_modified = true;
                    return;
                }
            }
            temp.apply(this);
            nodeNumber++;
        }
        Map<Object, Object> exceptionMap = node.get_ExceptionMap();
        Map<Object, Object> paramMap = node.get_ParamMap();
        List<Object> catchList = node.get_CatchList();
        Iterator<Object> it2 = catchList.iterator();
        while (it2.hasNext()) {
            ASTTryNode.container catchBody = (ASTTryNode.container) it2.next();
            SootClass sootClass = (SootClass) exceptionMap.get(catchBody);
            Type type = sootClass.getType();
            caseType(type);
            Local local = (Local) paramMap.get(catchBody);
            decideCaseExprOrRef(local);
            List<Object> body = (List) catchBody.o;
            Iterator<Object> itBody = body.iterator();
            int nodeNumber2 = 0;
            while (itBody.hasNext()) {
                ASTNode temp2 = (ASTNode) itBody.next();
                if (temp2 instanceof ASTLabeledBlockNode) {
                    ASTLabeledBlockNode labelBlock2 = (ASTLabeledBlockNode) temp2;
                    SETNodeLabel label2 = labelBlock2.get_Label();
                    if (label2.toString() == null && (newBody3 = UselessLabeledBlockRemover.createNewSubBody(body, nodeNumber2, labelBlock2)) != null) {
                        catchBody.replaceBody(newBody3);
                        G.v().ASTTransformations_modified = true;
                    }
                } else if (temp2 instanceof ASTIfElseNode) {
                    List<Object> elseBody2 = ((ASTIfElseNode) temp2).getElseBody();
                    if (elseBody2.size() == 0 && (newBody2 = EmptyElseRemover.createNewNodeBody(body, nodeNumber2, (ASTIfElseNode) temp2)) != null) {
                        catchBody.replaceBody(newBody2);
                        G.v().ASTTransformations_modified = true;
                        return;
                    }
                } else if ((temp2 instanceof ASTIfNode) && itBody.hasNext()) {
                    ASTNode nextNode2 = (ASTNode) body.get(nodeNumber2 + 1);
                    if ((nextNode2 instanceof ASTIfNode) && (newBody = OrAggregatorThree.createNewNodeBody(body, nodeNumber2, (ASTIfNode) temp2, (ASTIfNode) nextNode2)) != null) {
                        catchBody.replaceBody(newBody);
                        G.v().ASTTransformations_modified = true;
                        return;
                    }
                }
                temp2.apply(this);
                nodeNumber2++;
            }
        }
        outASTTryNode(node);
    }

    private void dealWithSwitchNode(ASTSwitchNode node) {
        List<Object> newBody;
        List<Object> newBody2;
        List<Object> newBody3;
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        for (Object currentIndex : indexList) {
            List<Object> body = index2BodyList.get(currentIndex);
            if (body != null) {
                Iterator<Object> itBody = body.iterator();
                int nodeNumber = 0;
                while (itBody.hasNext()) {
                    ASTNode temp = (ASTNode) itBody.next();
                    if (temp instanceof ASTLabeledBlockNode) {
                        ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode) temp;
                        SETNodeLabel label = labelBlock.get_Label();
                        if (label.toString() == null && (newBody3 = UselessLabeledBlockRemover.createNewSubBody(body, nodeNumber, labelBlock)) != null) {
                            index2BodyList.put(currentIndex, newBody3);
                            node.replaceIndex2BodyList(index2BodyList);
                            G.v().ASTTransformations_modified = true;
                        }
                    } else if (temp instanceof ASTIfElseNode) {
                        List<Object> elseBody = ((ASTIfElseNode) temp).getElseBody();
                        if (elseBody.size() == 0 && (newBody2 = EmptyElseRemover.createNewNodeBody(body, nodeNumber, (ASTIfElseNode) temp)) != null) {
                            index2BodyList.put(currentIndex, newBody2);
                            node.replaceIndex2BodyList(index2BodyList);
                            G.v().ASTTransformations_modified = true;
                            return;
                        }
                    } else if ((temp instanceof ASTIfNode) && itBody.hasNext()) {
                        ASTNode nextNode = (ASTNode) body.get(nodeNumber + 1);
                        if ((nextNode instanceof ASTIfNode) && (newBody = OrAggregatorThree.createNewNodeBody(body, nodeNumber, (ASTIfNode) temp, (ASTIfNode) nextNode)) != null) {
                            index2BodyList.put(currentIndex, newBody);
                            node.replaceIndex2BodyList(index2BodyList);
                            G.v().ASTTransformations_modified = true;
                            return;
                        }
                    }
                    temp.apply(this);
                    nodeNumber++;
                }
                continue;
            }
        }
    }
}
