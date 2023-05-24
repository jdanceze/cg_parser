package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
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
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/ASTCleanerTwo.class */
public class ASTCleanerTwo extends DepthFirstAdapter {
    public ASTCleanerTwo() {
    }

    public ASTCleanerTwo(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void normalRetrieving(ASTNode node) {
        List<Object> newBody;
        if (node instanceof ASTSwitchNode) {
            dealWithSwitchNode((ASTSwitchNode) node);
            return;
        }
        Iterator<Object> sbit = node.get_SubBodies().iterator();
        int subBodyNumber = 0;
        while (sbit.hasNext()) {
            List<Object> subBody = (List) sbit.next();
            Iterator<Object> it = subBody.iterator();
            int nodeNumber = 0;
            while (it.hasNext()) {
                ASTNode temp = (ASTNode) it.next();
                if (temp instanceof ASTIfElseNode) {
                    IfElseBreaker breaker = new IfElseBreaker();
                    boolean success = false;
                    if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode) temp)) {
                        success = true;
                    } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode) temp)) {
                        success = true;
                    }
                    if (success && (newBody = breaker.createNewBody(subBody, nodeNumber)) != null) {
                        if (node instanceof ASTIfElseNode) {
                            if (subBodyNumber == 0) {
                                List<Object> subBodies = node.get_SubBodies();
                                List<Object> ifElseBody = (List) subBodies.get(1);
                                ((ASTIfElseNode) node).replaceBody(newBody, ifElseBody);
                                G.v().ASTTransformations_modified = true;
                                return;
                            } else if (subBodyNumber == 1) {
                                List<Object> subBodies2 = node.get_SubBodies();
                                List<Object> ifBody = (List) subBodies2.get(0);
                                ((ASTIfElseNode) node).replaceBody(ifBody, newBody);
                                G.v().ASTTransformations_modified = true;
                                return;
                            } else {
                                throw new RuntimeException("Please report benchmark to programmer");
                            }
                        } else if (node instanceof ASTMethodNode) {
                            ((ASTMethodNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTSynchronizedBlockNode) {
                            ((ASTSynchronizedBlockNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTLabeledBlockNode) {
                            ((ASTLabeledBlockNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTUnconditionalLoopNode) {
                            ((ASTUnconditionalLoopNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTIfNode) {
                            ((ASTIfNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTWhileNode) {
                            ((ASTWhileNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTDoWhileNode) {
                            ((ASTDoWhileNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else if (node instanceof ASTForLoopNode) {
                            ((ASTForLoopNode) node).replaceBody(newBody);
                            G.v().ASTTransformations_modified = true;
                            return;
                        } else {
                            throw new RuntimeException("Please report benchmark to programmer");
                        }
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
        inASTTryNode(node);
        List<Object> tryBody = node.get_TryBody();
        Iterator<Object> it = tryBody.iterator();
        int nodeNumber = 0;
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            if (temp instanceof ASTIfElseNode) {
                IfElseBreaker breaker = new IfElseBreaker();
                boolean success = false;
                if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode) temp)) {
                    success = true;
                } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode) temp)) {
                    success = true;
                }
                if (G.v().ASTTransformations_modified) {
                    return;
                }
                if (success && (newBody2 = breaker.createNewBody(tryBody, nodeNumber)) != null) {
                    node.replaceTryBody(newBody2);
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
                if (temp2 instanceof ASTIfElseNode) {
                    IfElseBreaker breaker2 = new IfElseBreaker();
                    boolean success2 = false;
                    if (breaker2.isIfElseBreakingPossiblePatternOne((ASTIfElseNode) temp2)) {
                        success2 = true;
                    } else if (breaker2.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode) temp2)) {
                        success2 = true;
                    }
                    if (G.v().ASTTransformations_modified) {
                        return;
                    }
                    if (success2 && (newBody = breaker2.createNewBody(body, nodeNumber2)) != null) {
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
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        for (Object currentIndex : indexList) {
            List<Object> body = index2BodyList.get(currentIndex);
            if (body != null) {
                Iterator<Object> itBody = body.iterator();
                int nodeNumber = 0;
                while (itBody.hasNext()) {
                    ASTNode temp = (ASTNode) itBody.next();
                    if (temp instanceof ASTIfElseNode) {
                        IfElseBreaker breaker = new IfElseBreaker();
                        boolean success = false;
                        if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode) temp)) {
                            success = true;
                        } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode) temp)) {
                            success = true;
                        }
                        if (G.v().ASTTransformations_modified) {
                            return;
                        }
                        if (success && (newBody = breaker.createNewBody(body, nodeNumber)) != null) {
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
