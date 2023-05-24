package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETDoWhileNode.class */
public class SETDoWhileNode extends SETCycleNode {
    private AugmentedStmt entryPoint;

    public SETDoWhileNode(AugmentedStmt characterizingStmt, AugmentedStmt entryPoint, IterableSet body) {
        super(characterizingStmt, body);
        this.entryPoint = entryPoint;
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
        return new ASTDoWhileNode(get_Label(), (ConditionExpr) ((IfStmt) get_CharacterizingStmt().get_Stmt()).getCondition(), emit_ASTBody(this.body2childChain.get(this.subBodies.get(0))));
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return this.entryPoint;
    }
}
