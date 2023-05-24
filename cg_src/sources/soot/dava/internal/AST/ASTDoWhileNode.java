package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.ConditionExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTDoWhileNode.class */
public class ASTDoWhileNode extends ASTControlFlowNode {
    private List<Object> body;

    public ASTDoWhileNode(SETNodeLabel label, ConditionExpr ce, List<Object> body) {
        super(label, ce);
        this.body = body;
        this.subBodies.add(body);
    }

    public ASTDoWhileNode(SETNodeLabel label, ASTCondition ce, List<Object> body) {
        super(label, ce);
        this.body = body;
        this.subBodies.add(body);
    }

    public void replaceBody(List<Object> body) {
        this.body = body;
        this.subBodies = new ArrayList();
        this.subBodies.add(body);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTDoWhileNode(get_Label(), get_Condition(), this.body);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal("do");
        up.newline();
        up.literal("{");
        up.newline();
        up.incIndent();
        body_toString(up, this.body);
        up.decIndent();
        up.literal("}");
        up.newline();
        up.literal("while");
        up.literal(Instruction.argsep);
        up.literal("(");
        this.condition.toString(up);
        up.literal(")");
        up.literal(";");
        up.newline();
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(label_toString());
        b.append("do");
        b.append("\n");
        b.append("{");
        b.append("\n");
        b.append(body_toString(this.body));
        b.append("}");
        b.append("\n");
        b.append("while (");
        b.append(get_Condition().toString());
        b.append(");");
        b.append("\n");
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTDoWhileNode(this);
    }
}
