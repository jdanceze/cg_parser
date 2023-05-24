package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETLabeledBlockNode.class */
public class SETLabeledBlockNode extends SETNode {
    public SETLabeledBlockNode(IterableSet body) {
        super(body);
        add_SubBody(body);
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        return ((SETNode) this.body2childChain.get(this.subBodies.get(0)).getLast()).get_NaturalExits();
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        return new ASTLabeledBlockNode(get_Label(), emit_ASTBody(this.body2childChain.get(this.subBodies.get(0))));
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return ((SETNode) this.body2childChain.get(this.subBodies.get(0)).getFirst()).get_EntryStmt();
    }

    @Override // soot.dava.internal.SET.SETNode
    protected boolean resolve(SETNode parent) {
        throw new RuntimeException("Attempting auto-nest a SETLabeledBlockNode.");
    }
}
