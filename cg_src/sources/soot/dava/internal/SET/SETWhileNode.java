package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETWhileNode.class */
public class SETWhileNode extends SETCycleNode {
    public SETWhileNode(AugmentedStmt characterizingStmt, IterableSet body) {
        super(characterizingStmt, body);
        IterableSet subBody = (IterableSet) body.clone();
        subBody.remove(characterizingStmt);
        add_SubBody(subBody);
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        IterableSet c = new IterableSet();
        c.add(get_CharacterizingStmt());
        return c;
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        return new ASTWhileNode(get_Label(), (ConditionExpr) ((IfStmt) get_CharacterizingStmt().get_Stmt()).getCondition(), emit_ASTBody(this.body2childChain.get(this.subBodies.get(0))));
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return get_CharacterizingStmt();
    }
}
