package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/IfElseSplitter.class */
public class IfElseSplitter extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    boolean targeted;
    ASTMethodNode methodNode;
    ASTNode parent;
    ASTIfElseNode toReplace;
    ASTIfNode toInsert;
    List<Object> bodyAfterInsert;
    boolean transform;

    public IfElseSplitter() {
        this.targeted = false;
        this.transform = false;
    }

    public IfElseSplitter(boolean verbose) {
        super(verbose);
        this.targeted = false;
        this.transform = false;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTMethodNode(ASTMethodNode node) {
        this.methodNode = node;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTMethodNode(ASTMethodNode a) {
        List<Object> subBody;
        if (!this.transform) {
            return;
        }
        List<Object> parentBodies = this.parent.get_SubBodies();
        Iterator<Object> it = parentBodies.iterator();
        while (it.hasNext()) {
            if (this.parent instanceof ASTTryNode) {
                subBody = (List) ((ASTTryNode.container) it.next()).o;
            } else {
                subBody = (List) it.next();
            }
            if (subBody.indexOf(this.toReplace) > -1) {
                subBody.add(subBody.indexOf(this.toReplace), this.toInsert);
                subBody.addAll(subBody.indexOf(this.toReplace), this.bodyAfterInsert);
                subBody.remove(this.toReplace);
                G.v().ASTTransformations_modified = true;
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTIfElseNode(ASTIfElseNode node) {
        if (this.transform) {
            return;
        }
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 2) {
            throw new DecompilationException("IfelseNode without two subBodies. report to developer");
        }
        List<Object> ifBody = (List) subBodies.get(0);
        List<Object> elseBody = (List) subBodies.get(1);
        boolean patternMatched = tryBodyPattern(ifBody, node.get_Label(), elseBody);
        List<Object> newIfBody = null;
        List<Object> outerScopeBody = null;
        boolean negateIfCondition = false;
        if (patternMatched) {
            if (DEBUG) {
                System.out.println("First pattern matched");
            }
            newIfBody = ifBody;
            outerScopeBody = elseBody;
            negateIfCondition = false;
        } else {
            boolean patternMatched2 = tryBodyPattern(elseBody, node.get_Label(), ifBody);
            if (patternMatched2) {
                if (DEBUG) {
                    System.out.println("Second pattern matched");
                }
                newIfBody = elseBody;
                outerScopeBody = ifBody;
                negateIfCondition = true;
            }
        }
        if (newIfBody != null && outerScopeBody != null) {
            ASTCondition cond = node.get_Condition();
            if (negateIfCondition) {
                cond.flip();
            }
            ASTIfNode newNode = new ASTIfNode(node.get_Label(), cond, newIfBody);
            if (DEBUG) {
                System.out.println("New IF Node is: " + newNode.toString());
                System.out.println("Outer scope body list is:\n");
                for (int i = 0; i < outerScopeBody.size(); i++) {
                    System.out.println("\n\n " + outerScopeBody.get(i).toString());
                }
            }
            ASTParentNodeFinder finder = new ASTParentNodeFinder();
            this.methodNode.apply(finder);
            Object returned = finder.getParentOf(node);
            if (returned == null) {
                return;
            }
            this.parent = (ASTNode) returned;
            this.toReplace = node;
            this.toInsert = newNode;
            this.bodyAfterInsert = outerScopeBody;
            this.transform = true;
        }
    }

    public boolean tryBodyPattern(List<Object> body, SETNodeLabel label, List<Object> otherBody) {
        Stmt lastStmt = getLastStmt(body);
        if (lastStmt != null) {
            if ((!(lastStmt instanceof ReturnStmt) && !(lastStmt instanceof ReturnVoidStmt) && !(lastStmt instanceof DAbruptStmt)) || bodyTargetsLabel(label, body) || bodyTargetsLabel(label, otherBody)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean bodyTargetsLabel(SETNodeLabel label, List<Object> body) {
        if (label == null || label.toString() == null) {
            return false;
        }
        final String strLabel = label.toString();
        Iterator<Object> it = body.iterator();
        this.targeted = false;
        while (it.hasNext()) {
            ASTNode temp = (ASTNode) it.next();
            temp.apply(new DepthFirstAdapter() { // from class: soot.dava.toolkits.base.AST.transformations.IfElseSplitter.1
                @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
                public void inStmt(Stmt s) {
                    if (!(s instanceof DAbruptStmt)) {
                        return;
                    }
                    DAbruptStmt abrupt = (DAbruptStmt) s;
                    SETNodeLabel label2 = abrupt.getLabel();
                    if (label2 != null && label2.toString() != null && label2.toString().equals(strLabel)) {
                        IfElseSplitter.this.targeted = true;
                    }
                }
            });
            if (this.targeted) {
                break;
            }
        }
        return this.targeted;
    }

    public Stmt getLastStmt(List<Object> body) {
        if (body.size() == 0) {
            return null;
        }
        ASTNode lastNode = (ASTNode) body.get(body.size() - 1);
        if (!(lastNode instanceof ASTStatementSequenceNode)) {
            return null;
        }
        ASTStatementSequenceNode stmtNode = (ASTStatementSequenceNode) lastNode;
        List<AugmentedStmt> stmts = stmtNode.getStatements();
        if (stmts.size() == 0) {
            return null;
        }
        AugmentedStmt lastStmt = stmts.get(stmts.size() - 1);
        return lastStmt.get_Stmt();
    }
}
