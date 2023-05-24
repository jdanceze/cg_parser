package soot.dava.toolkits.base.AST.transformations;

import soot.BooleanType;
import soot.IntType;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DShortcutIf;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.jimple.internal.ImmediateBox;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/ShortcutIfGenerator.class */
public class ShortcutIfGenerator extends DepthFirstAdapter {
    public ShortcutIfGenerator() {
    }

    public ShortcutIfGenerator(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
        Type rightType;
        ValueBox OpBox;
        for (AugmentedStmt as : node.getStatements()) {
            Stmt s = as.get_Stmt();
            if (s instanceof DefinitionStmt) {
                DefinitionStmt ds = (DefinitionStmt) s;
                ValueBox rightBox = ds.getRightOpBox();
                Value right = rightBox.getValue();
                if (right instanceof CastExpr) {
                    rightType = ((CastExpr) right).getCastType();
                    OpBox = ((CastExpr) right).getOpBox();
                } else {
                    rightType = ds.getLeftOp().getType();
                    OpBox = rightBox;
                }
                if (rightType instanceof IntType) {
                    Value Op = OpBox.getValue();
                    if (Op.getType() instanceof BooleanType) {
                        ImmediateBox trueBox = new ImmediateBox(IntConstant.v(1));
                        ImmediateBox falseBox = new ImmediateBox(IntConstant.v(0));
                        DShortcutIf shortcut = new DShortcutIf(OpBox, trueBox, falseBox);
                        if (this.DEBUG) {
                            System.out.println("created: " + shortcut);
                        }
                        rightBox.setValue(shortcut);
                    }
                }
            }
        }
    }
}
