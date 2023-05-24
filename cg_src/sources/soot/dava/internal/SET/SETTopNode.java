package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETTopNode.class */
public class SETTopNode extends SETNode {
    public SETTopNode(IterableSet body) {
        super(body);
        add_SubBody(body);
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        return new IterableSet();
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        return new ASTMethodNode(emit_ASTBody(this.body2childChain.get(this.subBodies.get(0))));
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        throw new RuntimeException("Not implemented.");
    }

    @Override // soot.dava.internal.SET.SETNode
    protected boolean resolve(SETNode parent) {
        throw new RuntimeException("Attempting auto-nest a SETTopNode.");
    }
}
