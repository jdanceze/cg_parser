package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.coffi.Instruction;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTUnconditionalLoopNode.class */
public class ASTUnconditionalLoopNode extends ASTLabeledNode {
    private List<Object> body;

    public ASTUnconditionalLoopNode(SETNodeLabel label, List<Object> body) {
        super(label);
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
        return new ASTUnconditionalLoopNode(get_Label(), this.body);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal("while");
        up.literal(Instruction.argsep);
        up.literal("(");
        up.literal("true");
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
        b.append("while (true)");
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
        a.caseASTUnconditionalLoopNode(this);
    }
}
