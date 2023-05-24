package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTForLoopNode.class */
public class ASTForLoopNode extends ASTControlFlowNode {
    private List<AugmentedStmt> init;
    private List<AugmentedStmt> update;
    private List<Object> body;

    public ASTForLoopNode(SETNodeLabel label, List<AugmentedStmt> init, ASTCondition condition, List<AugmentedStmt> update, List<Object> body) {
        super(label, condition);
        this.body = body;
        this.init = init;
        this.update = update;
        this.subBodies.add(body);
    }

    public List<AugmentedStmt> getInit() {
        return this.init;
    }

    public List<AugmentedStmt> getUpdate() {
        return this.update;
    }

    public void replaceBody(List<Object> body) {
        this.body = body;
        this.subBodies = new ArrayList();
        this.subBodies.add(body);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTForLoopNode(get_Label(), this.init, get_Condition(), this.update, this.body);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal("for");
        up.literal(Instruction.argsep);
        up.literal("(");
        Iterator<AugmentedStmt> it = this.init.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            Unit u = as.get_Stmt();
            u.toString(up);
            if (it.hasNext()) {
                up.literal(" , ");
            }
        }
        up.literal("; ");
        this.condition.toString(up);
        up.literal("; ");
        Iterator<AugmentedStmt> it2 = this.update.iterator();
        while (it2.hasNext()) {
            AugmentedStmt as2 = it2.next();
            Unit u2 = as2.get_Stmt();
            u2.toString(up);
            if (it2.hasNext()) {
                up.literal(" , ");
            }
        }
        up.literal(")");
        up.newline();
        up.literal("{");
        up.newline();
        up.incIndent();
        body_toString(up, this.body);
        up.decIndent();
        up.literal("}");
        up.newline();
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(label_toString());
        b.append("for (");
        Iterator<AugmentedStmt> it = this.init.iterator();
        while (it.hasNext()) {
            b.append(it.next().get_Stmt().toString());
            if (it.hasNext()) {
                b.append(" , ");
            }
        }
        b.append("; ");
        b.append(get_Condition().toString());
        b.append("; ");
        Iterator<AugmentedStmt> it2 = this.update.iterator();
        while (it2.hasNext()) {
            b.append(it2.next().get_Stmt().toString());
            if (it2.hasNext()) {
                b.append(" , ");
            }
        }
        b.append(")");
        b.append("\n");
        b.append("{");
        b.append("\n");
        b.append(body_toString(this.body));
        b.append("}");
        b.append("\n");
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTForLoopNode(this);
    }
}
