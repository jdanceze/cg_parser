package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.ConditionExpr;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTIfElseNode.class */
public class ASTIfElseNode extends ASTControlFlowNode {
    private List<Object> ifBody;
    private List<Object> elseBody;

    public ASTIfElseNode(SETNodeLabel label, ConditionExpr condition, List<Object> ifBody, List<Object> elseBody) {
        super(label, condition);
        this.ifBody = ifBody;
        this.elseBody = elseBody;
        this.subBodies.add(ifBody);
        this.subBodies.add(elseBody);
    }

    public ASTIfElseNode(SETNodeLabel label, ASTCondition condition, List<Object> ifBody, List<Object> elseBody) {
        super(label, condition);
        this.ifBody = ifBody;
        this.elseBody = elseBody;
        this.subBodies.add(ifBody);
        this.subBodies.add(elseBody);
    }

    public void replace(SETNodeLabel newLabel, ASTCondition newCond, List<Object> newBody, List<Object> bodyTwo) {
        this.ifBody = newBody;
        this.elseBody = bodyTwo;
        this.subBodies = new ArrayList();
        this.subBodies.add(newBody);
        this.subBodies.add(bodyTwo);
        set_Condition(newCond);
        set_Label(newLabel);
    }

    public void replaceBody(List<Object> ifBody, List<Object> elseBody) {
        this.ifBody = ifBody;
        this.elseBody = elseBody;
        this.subBodies = new ArrayList();
        this.subBodies.add(ifBody);
        this.subBodies.add(elseBody);
    }

    public void replaceElseBody(List<Object> elseBody) {
        this.elseBody = elseBody;
        this.subBodies = new ArrayList();
        this.subBodies.add(this.ifBody);
        this.subBodies.add(elseBody);
    }

    public List<Object> getIfBody() {
        return this.ifBody;
    }

    public List<Object> getElseBody() {
        return this.elseBody;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTIfElseNode(get_Label(), get_Condition(), this.ifBody, this.elseBody);
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
        body_toString(up, this.ifBody);
        up.decIndent();
        up.literal("}");
        up.newline();
        up.literal("else");
        up.newline();
        up.literal("{");
        up.newline();
        up.incIndent();
        body_toString(up, this.elseBody);
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
        b.append(body_toString(this.ifBody));
        b.append("}");
        b.append("\n");
        b.append("else");
        b.append("\n");
        b.append("{");
        b.append("\n");
        b.append(body_toString(this.elseBody));
        b.append("}");
        b.append("\n");
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTIfElseNode(this);
    }
}
