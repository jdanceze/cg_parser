package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.ASTAnalysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTLabeledNode.class */
public abstract class ASTLabeledNode extends ASTNode {
    private SETNodeLabel label;

    public ASTLabeledNode(SETNodeLabel label) {
        set_Label(label);
    }

    public SETNodeLabel get_Label() {
        return this.label;
    }

    public void set_Label(SETNodeLabel label) {
        this.label = label;
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void perform_Analysis(ASTAnalysis a) {
        perform_AnalysisOnSubBodies(a);
    }

    public void label_toString(UnitPrinter up) {
        if (this.label.toString() != null) {
            up.literal(this.label.toString());
            up.literal(":");
            up.newline();
        }
    }

    public String label_toString() {
        if (this.label.toString() == null) {
            return new String();
        }
        StringBuffer b = new StringBuffer();
        b.append(this.label.toString());
        b.append(":");
        b.append("\n");
        return b.toString();
    }
}
