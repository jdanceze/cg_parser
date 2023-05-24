package soot.dava.internal.AST;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.ASTWalker;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.dava.toolkits.base.AST.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/AST/ASTStatementSequenceNode.class */
public class ASTStatementSequenceNode extends ASTNode {
    private List<AugmentedStmt> statementSequence;

    public ASTStatementSequenceNode(List<AugmentedStmt> statementSequence) {
        this.statementSequence = statementSequence;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new ASTStatementSequenceNode(this.statementSequence);
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void perform_Analysis(ASTAnalysis a) {
        if (a.getAnalysisDepth() > 0) {
            for (AugmentedStmt as : this.statementSequence) {
                ASTWalker.v().walk_stmt(a, as.get_Stmt());
            }
        }
        if (a instanceof TryContentsFinder) {
            TryContentsFinder.v().add_ExceptionSet(this, TryContentsFinder.v().remove_CurExceptionSet());
        }
    }

    @Override // soot.dava.internal.AST.ASTNode, soot.Unit
    public void toString(UnitPrinter up) {
        for (AugmentedStmt as : this.statementSequence) {
            Unit u = as.get_Stmt();
            up.startUnit(u);
            u.toString(up);
            up.literal(";");
            up.endUnit(u);
            up.newline();
        }
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        for (AugmentedStmt as : this.statementSequence) {
            b.append(as.get_Stmt().toString());
            b.append(";");
            b.append("\n");
        }
        return b.toString();
    }

    public List<AugmentedStmt> getStatements() {
        return this.statementSequence;
    }

    @Override // soot.dava.internal.AST.ASTNode
    public void apply(Analysis a) {
        a.caseASTStatementSequenceNode(this);
    }

    public void setStatements(List<AugmentedStmt> statementSequence) {
        this.statementSequence = statementSequence;
    }
}
