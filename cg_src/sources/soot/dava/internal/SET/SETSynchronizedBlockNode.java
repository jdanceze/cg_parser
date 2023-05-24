package soot.dava.internal.SET;

import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.finders.ExceptionNode;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETSynchronizedBlockNode.class */
public class SETSynchronizedBlockNode extends SETNode {
    private Value local;

    public SETSynchronizedBlockNode(ExceptionNode en, Value local) {
        super(en.get_Body());
        add_SubBody(en.get_TryBody());
        add_SubBody(en.get_CatchBody());
        this.local = local;
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        return ((SETNode) this.body2childChain.get(this.subBodies.get(0)).getLast()).get_NaturalExits();
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        return new ASTSynchronizedBlockNode(get_Label(), emit_ASTBody(this.body2childChain.get(this.subBodies.get(0))), this.local);
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return ((SETNode) this.body2childChain.get(this.subBodies.get(0)).getFirst()).get_EntryStmt();
    }

    @Override // soot.dava.internal.SET.SETNode
    protected boolean resolve(SETNode parent) {
        for (IterableSet subBody : parent.get_SubBodies()) {
            if (subBody.intersects(get_Body())) {
                return subBody.isSupersetOf(get_Body());
            }
        }
        return true;
    }
}
