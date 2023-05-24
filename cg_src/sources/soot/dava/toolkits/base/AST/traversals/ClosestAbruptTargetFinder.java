package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.HashMap;
import soot.G;
import soot.Singletons;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/ClosestAbruptTargetFinder.class */
public class ClosestAbruptTargetFinder extends DepthFirstAdapter {
    HashMap<DAbruptStmt, ASTNode> closestNode = new HashMap<>();
    ArrayList<ASTLabeledNode> nodeStack = new ArrayList<>();

    public ClosestAbruptTargetFinder(Singletons.Global g) {
    }

    public static ClosestAbruptTargetFinder v() {
        return G.v().soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder();
    }

    public ASTNode getTarget(DAbruptStmt ab) {
        Object node = this.closestNode.get(ab);
        if (node != null) {
            return (ASTNode) node;
        }
        throw new RuntimeException("Unable to find target for AbruptStmt");
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTWhileNode(ASTWhileNode node) {
        this.nodeStack.add(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTDoWhileNode(ASTDoWhileNode node) {
        this.nodeStack.add(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        this.nodeStack.add(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTForLoopNode(ASTForLoopNode node) {
        this.nodeStack.add(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSwitchNode(ASTSwitchNode node) {
        this.nodeStack.add(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTWhileNode(ASTWhileNode node) {
        if (this.nodeStack.isEmpty()) {
            throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
        }
        this.nodeStack.remove(this.nodeStack.size() - 1);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTDoWhileNode(ASTDoWhileNode node) {
        if (this.nodeStack.isEmpty()) {
            throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
        }
        this.nodeStack.remove(this.nodeStack.size() - 1);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        if (this.nodeStack.isEmpty()) {
            throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
        }
        this.nodeStack.remove(this.nodeStack.size() - 1);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTForLoopNode(ASTForLoopNode node) {
        if (this.nodeStack.isEmpty()) {
            throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
        }
        this.nodeStack.remove(this.nodeStack.size() - 1);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void outASTSwitchNode(ASTSwitchNode node) {
        if (this.nodeStack.isEmpty()) {
            throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
        }
        this.nodeStack.remove(this.nodeStack.size() - 1);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inStmt(Stmt s) {
        if (s instanceof DAbruptStmt) {
            DAbruptStmt ab = (DAbruptStmt) s;
            SETNodeLabel label = ab.getLabel();
            if (label != null && label.toString() != null) {
                return;
            }
            if (ab.is_Break()) {
                if (this.nodeStack.size() - 1 < 0) {
                    throw new RuntimeException("nodeStack empty??" + this.nodeStack.toString());
                }
                this.closestNode.put(ab, this.nodeStack.get(this.nodeStack.size() - 1));
            } else if (ab.is_Continue()) {
                int index = this.nodeStack.size() - 1;
                if (index < 0) {
                    throw new RuntimeException("nodeStack empty??" + this.nodeStack.toString());
                }
                ASTLabeledNode aSTLabeledNode = this.nodeStack.get(index);
                while (true) {
                    ASTLabeledNode currentNode = aSTLabeledNode;
                    if (currentNode instanceof ASTSwitchNode) {
                        if (index > 0) {
                            index--;
                            aSTLabeledNode = this.nodeStack.get(index);
                        } else {
                            throw new RuntimeException("Unable to find closest break Target");
                        }
                    } else {
                        this.closestNode.put(ab, currentNode);
                        return;
                    }
                }
            }
        }
    }
}
