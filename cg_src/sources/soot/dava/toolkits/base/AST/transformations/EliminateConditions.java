package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.BooleanType;
import soot.Value;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTControlFlowNode;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/EliminateConditions.class */
public class EliminateConditions extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    public boolean modified;
    ASTParentNodeFinder finder;
    ASTMethodNode AST;
    List<Object> bodyContainingNode;

    public EliminateConditions(ASTMethodNode AST) {
        this.modified = false;
        this.bodyContainingNode = null;
        this.finder = new ASTParentNodeFinder();
        this.AST = AST;
    }

    public EliminateConditions(boolean verbose, ASTMethodNode AST) {
        super(verbose);
        this.modified = false;
        this.bodyContainingNode = null;
        this.finder = new ASTParentNodeFinder();
        this.AST = AST;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void normalRetrieving(ASTNode node) {
        this.modified = false;
        if (node instanceof ASTSwitchNode) {
            do {
                this.modified = false;
                dealWithSwitchNode((ASTSwitchNode) node);
            } while (this.modified);
            return;
        }
        Iterator<Object> sbit = node.get_SubBodies().iterator();
        while (sbit.hasNext()) {
            List<ASTNode> subBody = (List) sbit.next();
            temp = null;
            Boolean returned = null;
            for (ASTNode temp : subBody) {
                if (temp instanceof ASTControlFlowNode) {
                    this.bodyContainingNode = null;
                    returned = eliminate(temp);
                    if (returned != null && canChange(returned, temp)) {
                        break;
                    }
                    if (DEBUG) {
                        System.out.println("returned is null" + temp.getClass());
                    }
                    this.bodyContainingNode = null;
                }
                temp.apply(this);
            }
            boolean changed = change(returned, temp);
            if (changed) {
                this.modified = true;
            }
        }
        if (this.modified) {
            normalRetrieving(node);
        }
    }

    public Boolean eliminate(ASTNode node) {
        if (node instanceof ASTControlFlowNode) {
            ASTCondition cond = ((ASTControlFlowNode) node).get_Condition();
            if (cond == null || !(cond instanceof ASTUnaryCondition)) {
                return null;
            }
            ASTUnaryCondition unary = (ASTUnaryCondition) cond;
            Value unaryValue = unary.getValue();
            boolean notted = false;
            if (unaryValue instanceof DNotExpr) {
                notted = true;
                unaryValue = ((DNotExpr) unaryValue).getOp();
            }
            Boolean isBoolean = isBooleanConstant(unaryValue);
            if (isBoolean == null) {
                return null;
            }
            boolean trueOrFalse = isBoolean.booleanValue();
            if (notted) {
                trueOrFalse = !trueOrFalse;
            }
            this.AST.apply(this.finder);
            Object temp = this.finder.getParentOf(node);
            if (temp == null) {
                return null;
            }
            ASTNode parent = (ASTNode) temp;
            List<Object> subBodies = parent.get_SubBodies();
            Iterator<Object> it = subBodies.iterator();
            while (it.hasNext()) {
                this.bodyContainingNode = (List) it.next();
                int index = this.bodyContainingNode.indexOf(node);
                if (index < 0) {
                    this.bodyContainingNode = null;
                } else {
                    return new Boolean(trueOrFalse);
                }
            }
            return null;
        }
        return null;
    }

    public Boolean isBooleanConstant(Value internal) {
        if (!(internal instanceof DIntConstant)) {
            return null;
        }
        if (DEBUG) {
            System.out.println("Found Constant");
        }
        DIntConstant intConst = (DIntConstant) internal;
        if (!(intConst.type instanceof BooleanType)) {
            return null;
        }
        if (DEBUG) {
            System.out.println("Found Boolean Constant");
        }
        if (intConst.value == 1) {
            return new Boolean(true);
        }
        if (intConst.value == 0) {
            return new Boolean(false);
        }
        throw new RuntimeException("BooleanType found with value different than 0 or 1");
    }

    public Boolean eliminateForTry(ASTNode node) {
        if (node instanceof ASTControlFlowNode) {
            ASTCondition cond = ((ASTControlFlowNode) node).get_Condition();
            if (cond == null || !(cond instanceof ASTUnaryCondition)) {
                return null;
            }
            ASTUnaryCondition unary = (ASTUnaryCondition) cond;
            Value unaryValue = unary.getValue();
            boolean notted = false;
            if (unaryValue instanceof DNotExpr) {
                notted = true;
                unaryValue = ((DNotExpr) unaryValue).getOp();
            }
            Boolean isBoolean = isBooleanConstant(unaryValue);
            if (isBoolean == null) {
                return null;
            }
            boolean trueOrFalse = isBoolean.booleanValue();
            if (notted) {
                trueOrFalse = !trueOrFalse;
            }
            this.AST.apply(this.finder);
            Object temp = this.finder.getParentOf(node);
            if (temp == null) {
                return null;
            }
            if (!(temp instanceof ASTTryNode)) {
                throw new RuntimeException("eliminateTry called when parent was not a try node");
            }
            ASTTryNode parent = (ASTTryNode) temp;
            List<Object> tryBody = parent.get_TryBody();
            int index = tryBody.indexOf(node);
            if (index >= 0) {
                this.bodyContainingNode = tryBody;
                return new Boolean(trueOrFalse);
            }
            List<Object> catchList = parent.get_CatchList();
            Iterator<Object> it = catchList.iterator();
            while (it.hasNext()) {
                ASTTryNode.container catchBody = (ASTTryNode.container) it.next();
                List<Object> body = (List) catchBody.o;
                int index2 = body.indexOf(node);
                if (index2 >= 0) {
                    this.bodyContainingNode = body;
                    return new Boolean(trueOrFalse);
                }
            }
            return null;
        }
        return null;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTTryNode(ASTTryNode node) {
        this.modified = false;
        inASTTryNode(node);
        Iterator<Object> it = node.get_TryBody().iterator();
        Boolean returned = null;
        ASTNode temp = null;
        while (it.hasNext()) {
            temp = (ASTNode) it.next();
            if (temp instanceof ASTControlFlowNode) {
                this.bodyContainingNode = null;
                returned = eliminateForTry(temp);
                if (returned != null && canChange(returned, temp)) {
                    break;
                }
                this.bodyContainingNode = null;
            }
            temp.apply(this);
        }
        boolean changed = change(returned, temp);
        if (changed) {
            this.modified = true;
        }
        List<Object> catchList = node.get_CatchList();
        Iterator<Object> it2 = catchList.iterator();
        while (it2.hasNext()) {
            ASTTryNode.container catchBody = (ASTTryNode.container) it2.next();
            List<ASTNode> body = (List) catchBody.o;
            Boolean returned2 = null;
            temp = null;
            for (ASTNode temp2 : body) {
                if (temp2 instanceof ASTControlFlowNode) {
                    this.bodyContainingNode = null;
                    returned2 = eliminateForTry(temp2);
                    if (returned2 != null && canChange(returned2, temp2)) {
                        break;
                    }
                    this.bodyContainingNode = null;
                }
                temp2.apply(this);
            }
            boolean changed2 = change(returned2, temp2);
            if (changed2) {
                this.modified = true;
            }
        }
        outASTTryNode(node);
        if (this.modified) {
            caseASTTryNode(node);
        }
    }

    public boolean canChange(Boolean returned, ASTNode temp) {
        return true;
    }

    public boolean change(Boolean returned, ASTNode temp) {
        if (this.bodyContainingNode != null && returned != null && temp != null) {
            int index = this.bodyContainingNode.indexOf(temp);
            if (DEBUG) {
                System.out.println("in change");
            }
            if (temp instanceof ASTIfNode) {
                this.bodyContainingNode.remove(temp);
                if (returned.booleanValue()) {
                    String label = ((ASTLabeledNode) temp).get_Label().toString();
                    if (label != null) {
                        ASTLabeledBlockNode labeledNode = new ASTLabeledBlockNode(((ASTLabeledNode) temp).get_Label(), (List) temp.get_SubBodies().get(0));
                        this.bodyContainingNode.add(index, labeledNode);
                    } else {
                        this.bodyContainingNode.addAll(index, (List) temp.get_SubBodies().get(0));
                    }
                }
                if (DEBUG) {
                    System.out.println("Removed if" + temp);
                    return true;
                }
                return true;
            } else if (temp instanceof ASTIfElseNode) {
                this.bodyContainingNode.remove(temp);
                if (returned.booleanValue()) {
                    String label2 = ((ASTLabeledNode) temp).get_Label().toString();
                    if (label2 != null) {
                        ASTLabeledBlockNode labeledNode2 = new ASTLabeledBlockNode(((ASTLabeledNode) temp).get_Label(), (List) temp.get_SubBodies().get(0));
                        this.bodyContainingNode.add(index, labeledNode2);
                        return true;
                    }
                    this.bodyContainingNode.addAll(index, (List) temp.get_SubBodies().get(0));
                    return true;
                }
                String label3 = ((ASTLabeledNode) temp).get_Label().toString();
                if (label3 != null) {
                    ASTLabeledBlockNode labeledNode3 = new ASTLabeledBlockNode(((ASTLabeledNode) temp).get_Label(), (List) temp.get_SubBodies().get(1));
                    this.bodyContainingNode.add(index, labeledNode3);
                    return true;
                }
                this.bodyContainingNode.addAll(index, (List) temp.get_SubBodies().get(1));
                return true;
            } else if ((temp instanceof ASTWhileNode) && !returned.booleanValue()) {
                this.bodyContainingNode.remove(temp);
                return true;
            } else if ((temp instanceof ASTDoWhileNode) && !returned.booleanValue()) {
                this.bodyContainingNode.remove(temp);
                this.bodyContainingNode.addAll(index, (List) temp.get_SubBodies().get(0));
                return true;
            } else if ((temp instanceof ASTForLoopNode) && !returned.booleanValue()) {
                this.bodyContainingNode.remove(temp);
                ASTStatementSequenceNode newNode = new ASTStatementSequenceNode(((ASTForLoopNode) temp).getInit());
                this.bodyContainingNode.add(index, newNode);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void dealWithSwitchNode(ASTSwitchNode node) {
        List<Object> indexList = node.getIndexList();
        Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
        for (Object currentIndex : indexList) {
            List<ASTNode> body = index2BodyList.get(currentIndex);
            if (body != null) {
                Boolean returned = null;
                temp = null;
                for (ASTNode temp : body) {
                    if (temp instanceof ASTControlFlowNode) {
                        this.bodyContainingNode = null;
                        returned = eliminate(temp);
                        if (returned != null && canChange(returned, temp)) {
                            break;
                        }
                        this.bodyContainingNode = null;
                    }
                    temp.apply(this);
                }
                boolean changed = change(returned, temp);
                if (changed) {
                    this.modified = true;
                }
            }
        }
    }
}
