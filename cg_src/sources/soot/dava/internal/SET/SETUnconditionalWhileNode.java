package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETUnconditionalWhileNode.class */
public class SETUnconditionalWhileNode extends SETCycleNode {
    public SETUnconditionalWhileNode(IterableSet body) {
        super((AugmentedStmt) body.getFirst(), body);
        add_SubBody(body);
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        return new IterableSet();
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        return new ASTUnconditionalLoopNode(get_Label(), emit_ASTBody(this.body2childChain.get(this.subBodies.get(0))));
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return get_CharacterizingStmt();
    }
}
