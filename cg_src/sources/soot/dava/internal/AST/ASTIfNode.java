package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.ConditionExpr;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTIfNode.class */
public class ASTIfNode extends ASTControlFlowNode {
    private List<Object> body;

    public ASTIfNode(SETNodeLabel label, ConditionExpr condition, List<Object> body) {
        super(label, condition);
        this.body = body;
        this.subBodies.add(body);
    }

    public ASTIfNode(SETNodeLabel label, ASTCondition condition, List<Object> body) {
        super(label, condition);
        this.body = body;
        this.subBodies.add(body);
    }

    public List<Object> getIfBody() {
        return this.body;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTIfNode(get_Label(), get_Condition(), this.body);
    }

    public void replace(SETNodeLabel label, ASTCondition condition, List<Object> body) {
        this.body = body;
        this.subBodies = new ArrayList();
        this.subBodies.add(body);
        set_Condition(condition);
        set_Label(label);
    }

    public void replaceBody(List<Object> body) {
        this.body = body;
        this.subBodies = new ArrayList();
        this.subBodies.add(body);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal(Jimple.IF);
        up.literal(Instruction.argsep);
        up.literal("(");
        this.condition.toString(up);
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
        b.append("if (");
        b.append(get_Condition().toString());
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
        a.caseASTIfNode(this);
    }
}
