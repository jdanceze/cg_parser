package soot.dava.toolkits.base.AST.traversals;

import java.util.HashMap;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/LabelToNodeMapper.class */
public class LabelToNodeMapper extends DepthFirstAdapter {
    private final HashMap<String, ASTLabeledNode> labelsToNode;

    public LabelToNodeMapper() {
        this.labelsToNode = new HashMap<>();
    }

    public LabelToNodeMapper(boolean verbose) {
        super(verbose);
        this.labelsToNode = new HashMap<>();
    }

    public Object getTarget(String label) {
        return this.labelsToNode.get(label);
    }

    private void addToMap(ASTLabeledNode node) {
        String str = node.get_Label().toString();
        if (str != null) {
            this.labelsToNode.put(str, node);
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTLabeledBlockNode(ASTLabeledBlockNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTTryNode(ASTTryNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTDoWhileNode(ASTDoWhileNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTForLoopNode(ASTForLoopNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfElseNode(ASTIfElseNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTIfNode(ASTIfNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTWhileNode(ASTWhileNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSwitchNode(ASTSwitchNode node) {
        addToMap(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        addToMap(node);
    }
}
