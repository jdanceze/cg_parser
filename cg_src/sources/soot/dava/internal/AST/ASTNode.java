package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.AbstractUnit;
import soot.UnitPrinter;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTNode.class */
public abstract class ASTNode extends AbstractUnit {
    public static final String TAB = "    ";
    public static final String NEWLINE = "\n";
    protected List<Object> subBodies = new ArrayList();

    public abstract void toString(UnitPrinter unitPrinter);

    public abstract void perform_Analysis(ASTAnalysis aSTAnalysis);

    /* JADX INFO: Access modifiers changed from: protected */
    public void body_toString(UnitPrinter up, List<Object> body) {
        Iterator<Object> it = body.iterator();
        while (it.hasNext()) {
            ((ASTNode) it.next()).toString(up);
            if (it.hasNext()) {
                up.newline();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String body_toString(List<Object> body) {
        StringBuffer b = new StringBuffer();
        Iterator<Object> it = body.iterator();
        while (it.hasNext()) {
            b.append(((ASTNode) it.next()).toString());
            if (it.hasNext()) {
                b.append("\n");
            }
        }
        return b.toString();
    }

    public List<Object> get_SubBodies() {
        return this.subBodies;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void perform_AnalysisOnSubBodies(ASTAnalysis a) {
        Iterator it;
        for (Object subBody : this.subBodies) {
            if (this instanceof ASTTryNode) {
                it = ((List) ((ASTTryNode.container) subBody).o).iterator();
            } else {
                it = ((List) subBody).iterator();
            }
            while (it.hasNext()) {
                ((ASTNode) it.next()).perform_Analysis(a);
            }
        }
        a.analyseASTNode(this);
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return false;
    }

    @Override // soot.Unit
    public boolean branches() {
        return false;
    }

    public void apply(Analysis a) {
        throw new RuntimeException("Analysis invoked apply method on ASTNode");
    }
}
