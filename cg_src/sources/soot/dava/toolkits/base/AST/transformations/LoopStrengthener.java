package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/LoopStrengthener.class */
public class LoopStrengthener extends DepthFirstAdapter {
    public LoopStrengthener() {
    }

    public LoopStrengthener(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void normalRetrieving(ASTNode node) {
        ASTNode oneNode;
        if (node instanceof ASTSwitchNode) {
            dealWithSwitchNode((ASTSwitchNode) node);
            return;
        }
        int subBodyNumber = 0;
        for (Object subBody : node.get_SubBodies()) {
            int nodeNumber = 0;
            for (ASTNode temp : (List) subBody) {
                if (((temp instanceof ASTWhileNode) || (temp instanceof ASTUnconditionalLoopNode) || (temp instanceof ASTDoWhileNode)) && (oneNode = getOnlySubNode(temp)) != null) {
                    List<ASTNode> newNode = null;
                    if (oneNode instanceof ASTIfNode) {
                        newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode) oneNode);
                    } else if (oneNode instanceof ASTIfElseNode) {
                        newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode) oneNode);
                    }
                    if (newNode != null) {
                        replaceNode(node, subBodyNumber, nodeNumber, temp, newNode);
                        UselessLabelFinder.v().findAndKill(node);
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
        ASTNode oneNode;
        ASTNode oneNode2;
        inASTTryNode(node);
        List<Object> tryBody = node.get_TryBody();
        Iterator<Object> it = tryBody.iterator();
        int nodeNumber = 0;
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (((temp instanceof ASTWhileNode) || (temp instanceof ASTUnconditionalLoopNode) || (temp instanceof ASTDoWhileNode)) && (oneNode2 = getOnlySubNode(temp)) != null) {
                List<ASTNode> newNode = null;
                if (oneNode2 instanceof ASTIfNode) {
                    newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode) oneNode2);
                } else if (oneNode2 instanceof ASTIfElseNode) {
                    newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode) oneNode2);
                }
                if (newNode != null) {
                    List<Object> newBody = createNewSubBody(tryBody, nodeNumber, temp, newNode);
                    if (newBody != null) {
                        node.replaceTryBody(newBody);
                        G.v().ASTTransformations_modified = true;
                    }
                    UselessLabelFinder.v().findAndKill(node);
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
                if (((temp2 instanceof ASTWhileNode) || (temp2 instanceof ASTUnconditionalLoopNode) || (temp2 instanceof ASTDoWhileNode)) && (oneNode = getOnlySubNode(temp2)) != null) {
                    List<ASTNode> newNode2 = null;
                    if (oneNode instanceof ASTIfNode) {
                        newNode2 = StrengthenByIf.getNewNode(temp2, (ASTIfNode) oneNode);
                    } else if (oneNode instanceof ASTIfElseNode) {
                        newNode2 = StrengthenByIfElse.getNewNode(temp2, (ASTIfElseNode) oneNode);
                    }
                    if (newNode2 != null) {
                        List<Object> newBody2 = createNewSubBody(body, nodeNumber2, temp2, newNode2);
                        if (newBody2 != null) {
                            catchBody.replaceBody(newBody2);
                            G.v().ASTTransformations_modified = true;
                        }
                        UselessLabelFinder.v().findAndKill(node);
                    }
                }
                temp2.apply(this);
                nodeNumber2++;
            }
        }
        outASTTryNode(node);
    }

    private void dealWithSwitchNode(ASTSwitchNode node) {
        ASTNode oneNode;
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        for (Object currentIndex : indexList) {
            List<Object> body = index2BodyList.get(currentIndex);
            if (body != null) {
                Iterator<Object> itBody = body.iterator();
                int nodeNumber = 0;
                while (itBody.hasNext()) {
                    ASTNode temp = (ASTNode) itBody.next();
                    if (((temp instanceof ASTWhileNode) || (temp instanceof ASTUnconditionalLoopNode) || (temp instanceof ASTDoWhileNode)) && (oneNode = getOnlySubNode(temp)) != null) {
                        List<ASTNode> newNode = null;
                        if (oneNode instanceof ASTIfNode) {
                            newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode) oneNode);
                        } else if (oneNode instanceof ASTIfElseNode) {
                            newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode) oneNode);
                        }
                        if (newNode != null) {
                            List<Object> newBody = createNewSubBody(body, nodeNumber, temp, newNode);
                            if (newBody != null) {
                                index2BodyList.put(currentIndex, newBody);
                                node.replaceIndex2BodyList(index2BodyList);
                                G.v().ASTTransformations_modified = true;
                            }
                            UselessLabelFinder.v().findAndKill(node);
                        }
                    }
                    temp.apply(this);
                    nodeNumber++;
                }
            }
        }
    }

    private ASTNode getOnlySubNode(ASTNode node) {
        if (!(node instanceof ASTWhileNode) && !(node instanceof ASTDoWhileNode) && !(node instanceof ASTUnconditionalLoopNode)) {
            return null;
        }
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 1) {
            return null;
        }
        List subBody = (List) subBodies.get(0);
        if (subBody.size() != 1) {
            return null;
        }
        return (ASTNode) subBody.get(0);
    }

    private void replaceNode(ASTNode node, int subBodyNumber, int nodeNumber, ASTNode loopNode, List<ASTNode> newNode) {
        if (!(node instanceof ASTIfElseNode)) {
            List<Object> subBodies = node.get_SubBodies();
            if (subBodies.size() != 1) {
                throw new RuntimeException("Please report this benchmark to the programmer");
            }
            List<Object> onlySubBody = (List) subBodies.get(0);
            List<Object> newBody = createNewSubBody(onlySubBody, nodeNumber, loopNode, newNode);
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
            List<Object> newBody2 = createNewSubBody(toModifySubBody, nodeNumber, loopNode, newNode);
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

    public static List<Object> createNewSubBody(List<Object> oldSubBody, int nodeNumber, ASTNode oldNode, List<ASTNode> newNode) {
        List<Object> newSubBody = new ArrayList<>();
        Iterator<Object> it = oldSubBody.iterator();
        for (int index = 0; index != nodeNumber; index++) {
            if (!it.hasNext()) {
                return null;
            }
            newSubBody.add(it.next());
        }
        ASTNode toRemove = (ASTNode) it.next();
        if (toRemove.toString().compareTo(oldNode.toString()) != 0) {
            System.out.println("The replace nodes dont match please report benchmark to developer");
            return null;
        }
        newSubBody.addAll(newNode);
        while (it.hasNext()) {
            newSubBody.add(it.next());
        }
        return newSubBody;
    }
}
