package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTLabeledBlockNode.class */
public class ASTLabeledBlockNode extends ASTLabeledNode {
    private List<Object> body;

    public ASTLabeledBlockNode(SETNodeLabel label, List<Object> body) {
        super(label);
        this.body = body;
        this.subBodies.add(body);
    }

    public void replaceBody(List<Object> body) {
        this.body = body;
        this.subBodies = new ArrayList();
        this.subBodies.add(body);
    }

    public int size() {
        return this.body.size();
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTLabeledBlockNode(get_Label(), this.body);
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        label_toString(up);
        up.literal("{");
        up.newline();
        up.incIndent();
        body_toString(up, this.body);
        up.decIndent();
        up.literal("} //end ");
        label_toString(up);
        up.newline();
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(label_toString());
        b.append("{");
        b.append("\n");
        b.append(body_toString(this.body));
        b.append("} //");
        b.append(label_toString());
        b.append("\n");
        return b.toString();
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTLabeledBlockNode(this);
    }
}
