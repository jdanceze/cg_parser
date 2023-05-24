package soot.dava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
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
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.grimp.internal.GNewInvokeExpr;
import soot.grimp.internal.GThrowStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/dava/MethodCallFinder.class */
public class MethodCallFinder extends DepthFirstAdapter {
    ASTMethodNode underAnalysis;
    DavaStaticBlockCleaner cleaner;

    public MethodCallFinder(DavaStaticBlockCleaner cleaner) {
        this.cleaner = cleaner;
        this.underAnalysis = null;
    }

    public MethodCallFinder(boolean verbose, DavaStaticBlockCleaner cleaner) {
        super(verbose);
        this.cleaner = cleaner;
        this.underAnalysis = null;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTMethodNode(ASTMethodNode node) {
        this.underAnalysis = node;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inInvokeStmt(InvokeStmt s) {
        InvokeExpr invokeExpr = s.getInvokeExpr();
        SootMethod maybeInline = invokeExpr.getMethod();
        ASTMethodNode toInlineASTMethod = this.cleaner.inline(maybeInline);
        if (toInlineASTMethod == null) {
            return;
        }
        List<Object> subBodies = toInlineASTMethod.get_SubBodies();
        if (subBodies.size() != 1) {
            throw new RuntimeException("Found ASTMEthod node with more than one subBodies");
        }
        List body = (List) subBodies.get(0);
        ASTParentNodeFinder finder = new ASTParentNodeFinder();
        this.underAnalysis.apply(finder);
        List<ASTStatementSequenceNode> newChangedBodyPart = createChangedBodyPart(s, body, finder);
        boolean replaced = replaceSubBody(s, newChangedBodyPart, finder);
        if (replaced) {
            StaticDefinitionFinder defFinder = new StaticDefinitionFinder(maybeInline);
            toInlineASTMethod.apply(defFinder);
            if (defFinder.anyFinalFieldDefined()) {
                SootClass runtime = Scene.v().loadClassAndSupport("java.lang.RuntimeException");
                if (runtime.declaresMethod("void <init>(java.lang.String)")) {
                    SootMethod sootMethod = runtime.getMethod("void <init>(java.lang.String)");
                    SootMethodRef methodRef = sootMethod.makeRef();
                    RefType myRefType = RefType.v(runtime);
                    StringConstant tempString = StringConstant.v("This method used to have a definition of a final variable. Dava inlined the definition into the static initializer");
                    List list = new ArrayList();
                    list.add(tempString);
                    GNewInvokeExpr newInvokeExpr = new GNewInvokeExpr(myRefType, methodRef, list);
                    GThrowStmt throwStmt = new GThrowStmt(newInvokeExpr);
                    AugmentedStmt augStmt = new AugmentedStmt(throwStmt);
                    List<AugmentedStmt> sequence = new ArrayList<>();
                    sequence.add(augStmt);
                    Object seqNode = new ASTStatementSequenceNode(sequence);
                    List<Object> subBody = new ArrayList<>();
                    subBody.add(seqNode);
                    toInlineASTMethod.replaceBody(subBody);
                }
            }
        }
    }

    public List<Object> getSubBodyFromSingleSubBodyNode(ASTNode node) {
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 1) {
            throw new RuntimeException("Found a single subBody node with more than 1 subBodies");
        }
        return (List) subBodies.get(0);
    }

    public List<Object> createNewSubBody(List<Object> orignalBody, List<ASTStatementSequenceNode> partNewBody, Object stmtSeqNode) {
        Object temp;
        List<Object> newBody = new ArrayList<>();
        Iterator<Object> it = orignalBody.iterator();
        while (it.hasNext() && (temp = it.next()) != stmtSeqNode) {
            newBody.add(temp);
        }
        newBody.addAll(partNewBody);
        while (it.hasNext()) {
            newBody.add(it.next());
        }
        return newBody;
    }

    public boolean replaceSubBody(InvokeStmt s, List<ASTStatementSequenceNode> newChangedBodyPart, ASTParentNodeFinder finder) {
        List<Object> subBodyToReplace;
        Object stmtSeqNode = finder.getParentOf(s);
        Object ParentOfStmtSeq = finder.getParentOf(stmtSeqNode);
        if (ParentOfStmtSeq == null) {
            throw new RuntimeException("MethodCall FInder: parent of stmt seq node not found");
        }
        ASTNode node = (ASTNode) ParentOfStmtSeq;
        if (node instanceof ASTMethodNode) {
            List<Object> subBodyToReplace2 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody = createNewSubBody(subBodyToReplace2, newChangedBodyPart, stmtSeqNode);
            ((ASTMethodNode) node).replaceBody(newBody);
            return true;
        } else if (node instanceof ASTSynchronizedBlockNode) {
            List<Object> subBodyToReplace3 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody2 = createNewSubBody(subBodyToReplace3, newChangedBodyPart, stmtSeqNode);
            ((ASTSynchronizedBlockNode) node).replaceBody(newBody2);
            return true;
        } else if (node instanceof ASTLabeledBlockNode) {
            List<Object> subBodyToReplace4 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody3 = createNewSubBody(subBodyToReplace4, newChangedBodyPart, stmtSeqNode);
            ((ASTLabeledBlockNode) node).replaceBody(newBody3);
            return true;
        } else if (node instanceof ASTUnconditionalLoopNode) {
            List<Object> subBodyToReplace5 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody4 = createNewSubBody(subBodyToReplace5, newChangedBodyPart, stmtSeqNode);
            ((ASTUnconditionalLoopNode) node).replaceBody(newBody4);
            return true;
        } else if (node instanceof ASTIfNode) {
            List<Object> subBodyToReplace6 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody5 = createNewSubBody(subBodyToReplace6, newChangedBodyPart, stmtSeqNode);
            ((ASTIfNode) node).replaceBody(newBody5);
            return true;
        } else if (node instanceof ASTWhileNode) {
            List<Object> subBodyToReplace7 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody6 = createNewSubBody(subBodyToReplace7, newChangedBodyPart, stmtSeqNode);
            ((ASTWhileNode) node).replaceBody(newBody6);
            return true;
        } else if (node instanceof ASTDoWhileNode) {
            List<Object> subBodyToReplace8 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody7 = createNewSubBody(subBodyToReplace8, newChangedBodyPart, stmtSeqNode);
            ((ASTDoWhileNode) node).replaceBody(newBody7);
            return true;
        } else if (node instanceof ASTForLoopNode) {
            List<Object> subBodyToReplace9 = getSubBodyFromSingleSubBodyNode(node);
            List<Object> newBody8 = createNewSubBody(subBodyToReplace9, newChangedBodyPart, stmtSeqNode);
            ((ASTForLoopNode) node).replaceBody(newBody8);
            return true;
        } else if (node instanceof ASTIfElseNode) {
            List<Object> subBodies = node.get_SubBodies();
            if (subBodies.size() != 2) {
                throw new RuntimeException("Found an ifelse ASTNode which does not have two bodies");
            }
            List<Object> ifBody = (List) subBodies.get(0);
            List<Object> elseBody = (List) subBodies.get(1);
            int subBodyNumber = -1;
            Iterator<Object> it = ifBody.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object temp = it.next();
                if (temp == stmtSeqNode) {
                    subBodyNumber = 0;
                    break;
                }
            }
            if (subBodyNumber != 0) {
                Iterator<Object> it2 = elseBody.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Object temp2 = it2.next();
                    if (temp2 == stmtSeqNode) {
                        subBodyNumber = 1;
                        break;
                    }
                }
            }
            if (subBodyNumber == 0) {
                subBodyToReplace = ifBody;
            } else if (subBodyNumber == 1) {
                subBodyToReplace = elseBody;
            } else {
                throw new RuntimeException("Could not find the related ASTNode in the method");
            }
            List<Object> newBody9 = createNewSubBody(subBodyToReplace, newChangedBodyPart, stmtSeqNode);
            if (subBodyNumber == 0) {
                ((ASTIfElseNode) node).replaceBody(newBody9, elseBody);
                return true;
            } else if (subBodyNumber == 1) {
                ((ASTIfElseNode) node).replaceBody(ifBody, newBody9);
                return true;
            } else {
                return false;
            }
        } else if (node instanceof ASTTryNode) {
            List<Object> tryBody = ((ASTTryNode) node).get_TryBody();
            Iterator<Object> it3 = tryBody.iterator();
            boolean inTryBody = false;
            while (true) {
                if (!it3.hasNext()) {
                    break;
                }
                ASTNode temp3 = (ASTNode) it3.next();
                if (temp3 == stmtSeqNode) {
                    inTryBody = true;
                    break;
                }
            }
            if (!inTryBody) {
                return false;
            }
            List<Object> newBody10 = createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTTryNode) node).replaceTryBody(newBody10);
            return true;
        } else if (node instanceof ASTSwitchNode) {
            List<Object> indexList = ((ASTSwitchNode) node).getIndexList();
            Map<Object, List<Object>> index2BodyList = ((ASTSwitchNode) node).getIndex2BodyList();
            for (Object currentIndex : indexList) {
                List<Object> body = index2BodyList.get(currentIndex);
                if (body != null) {
                    boolean found = false;
                    Iterator<Object> itBody = body.iterator();
                    while (true) {
                        if (!itBody.hasNext()) {
                            break;
                        }
                        ASTNode temp4 = (ASTNode) itBody.next();
                        if (temp4 == stmtSeqNode) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        List<Object> newBody11 = createNewSubBody(body, newChangedBodyPart, stmtSeqNode);
                        index2BodyList.put(currentIndex, newBody11);
                        ((ASTSwitchNode) node).replaceIndex2BodyList(index2BodyList);
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public List<ASTStatementSequenceNode> createChangedBodyPart(InvokeStmt s, List body, ASTParentNodeFinder finder) {
        Object parent = finder.getParentOf(s);
        if (parent == null) {
            throw new RuntimeException("MethodCall FInder: parent of invoke stmt not found");
        }
        ASTNode parentNode = (ASTNode) parent;
        if (!(parentNode instanceof ASTStatementSequenceNode)) {
            throw new RuntimeException("MethodCall FInder: parent node not a stmt seq node");
        }
        ASTStatementSequenceNode orignal = (ASTStatementSequenceNode) parentNode;
        List<AugmentedStmt> newInitialNode = new ArrayList<>();
        Iterator<AugmentedStmt> it = orignal.getStatements().iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            Stmt tempStmt = as.get_Stmt();
            if (tempStmt == s) {
                break;
            }
            newInitialNode.add(as);
        }
        List<AugmentedStmt> newSecondNode = new ArrayList<>();
        while (it.hasNext()) {
            newSecondNode.add(it.next());
        }
        List<ASTStatementSequenceNode> toReturn = new ArrayList<>();
        if (newInitialNode.size() != 0) {
            toReturn.add(new ASTStatementSequenceNode(newInitialNode));
        }
        toReturn.addAll(body);
        if (newSecondNode.size() != 0) {
            toReturn.add(new ASTStatementSequenceNode(newSecondNode));
        }
        return toReturn;
    }
}
