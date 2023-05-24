package soot.dava.toolkits.base.AST.traversals;

import java.util.List;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/traversals/InitializationDeclarationShortcut.class */
public class InitializationDeclarationShortcut extends DepthFirstAdapter {
    AugmentedStmt ofInterest;
    boolean possible;
    Local definedLocal;
    int seenBefore;

    public InitializationDeclarationShortcut(AugmentedStmt ofInterest) {
        this.possible = false;
        this.definedLocal = null;
        this.seenBefore = 0;
        this.ofInterest = ofInterest;
    }

    public InitializationDeclarationShortcut(boolean verbose, AugmentedStmt ofInterest) {
        super(verbose);
        this.possible = false;
        this.definedLocal = null;
        this.seenBefore = 0;
        this.ofInterest = ofInterest;
    }

    public boolean isShortcutPossible() {
        return this.possible;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTMethodNode(ASTMethodNode node) {
        Stmt s = this.ofInterest.get_Stmt();
        if (!(s instanceof DefinitionStmt)) {
            this.possible = false;
            return;
        }
        Value defined = ((DefinitionStmt) s).getLeftOp();
        if (!(defined instanceof Local)) {
            this.possible = false;
            return;
        }
        List declaredLocals = node.getDeclaredLocals();
        if (!declaredLocals.contains(defined)) {
            this.possible = false;
        } else {
            this.definedLocal = (Local) defined;
        }
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inDefinitionStmt(DefinitionStmt s) {
        if (this.definedLocal == null) {
            return;
        }
        Value defined = s.getLeftOp();
        if ((defined instanceof Local) && defined.equals(this.definedLocal)) {
            if (s.equals(this.ofInterest.get_Stmt())) {
                if (this.seenBefore == 0) {
                    this.possible = true;
                    return;
                } else {
                    this.possible = false;
                    return;
                }
            }
            this.seenBefore++;
        }
    }
}
