package soot.dava.internal.SET;

import java.util.List;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.misc.ConditionFlipper;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETIfElseNode.class */
public class SETIfElseNode extends SETDagNode {
    private IterableSet ifBody;
    private IterableSet elseBody;

    public SETIfElseNode(AugmentedStmt characterizingStmt, IterableSet body, IterableSet ifBody, IterableSet elseBody) {
        super(characterizingStmt, body);
        this.ifBody = ifBody;
        this.elseBody = elseBody;
        add_SubBody(ifBody);
        add_SubBody(elseBody);
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        IterableSet c = new IterableSet();
        IterableSet ifChain = this.body2childChain.get(this.ifBody);
        if (!ifChain.isEmpty()) {
            c.addAll(((SETNode) ifChain.getLast()).get_NaturalExits());
        }
        IterableSet elseChain = this.body2childChain.get(this.elseBody);
        if (!elseChain.isEmpty()) {
            c.addAll(((SETNode) elseChain.getLast()).get_NaturalExits());
        }
        return c;
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        List<Object> astBody0 = emit_ASTBody(this.body2childChain.get(this.ifBody));
        List<Object> astBody1 = emit_ASTBody(this.body2childChain.get(this.elseBody));
        ConditionExpr ce = (ConditionExpr) ((IfStmt) get_CharacterizingStmt().get_Stmt()).getCondition();
        if (astBody0.isEmpty()) {
            astBody0 = astBody1;
            astBody1 = astBody0;
            ce = ConditionFlipper.flip(ce);
        }
        if (astBody1.isEmpty()) {
            return new ASTIfNode(get_Label(), ce, astBody0);
        }
        return new ASTIfElseNode(get_Label(), ce, astBody0, astBody1);
    }
}
