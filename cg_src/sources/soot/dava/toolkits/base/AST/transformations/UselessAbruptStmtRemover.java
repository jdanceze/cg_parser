package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.LabelToNodeMapper;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/UselessAbruptStmtRemover.class */
public class UselessAbruptStmtRemover extends DepthFirstAdapter {
    public static boolean DEBUG = false;
    ASTParentNodeFinder finder;
    ASTMethodNode methodNode;
    LabelToNodeMapper mapper;

    public UselessAbruptStmtRemover() {
        this.finder = null;
    }

    public UselessAbruptStmtRemover(boolean verbose) {
        super(verbose);
        this.finder = null;
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inASTMethodNode(ASTMethodNode node) {
        this.methodNode = node;
        this.mapper = new LabelToNodeMapper();
        this.methodNode.apply(this.mapper);
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x013b, code lost:
        if (soot.dava.toolkits.base.AST.transformations.UselessAbruptStmtRemover.DEBUG == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x013e, code lost:
        java.lang.System.out.println("\t\tAncestorsParent is a loop shouldnt remove abrupt stmt");
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0146, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:?, code lost:
        return;
     */
    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter, soot.dava.toolkits.base.AST.analysis.AnalysisAdapter, soot.dava.toolkits.base.AST.analysis.Analysis
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void caseASTStatementSequenceNode(soot.dava.internal.AST.ASTStatementSequenceNode r6) {
        /*
            Method dump skipped, instructions count: 438
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dava.toolkits.base.AST.transformations.UselessAbruptStmtRemover.caseASTStatementSequenceNode(soot.dava.internal.AST.ASTStatementSequenceNode):void");
    }

    public boolean checkChildLastInParent(ASTNode child, ASTNode parent) {
        List subBody;
        List<Object> subBodies = parent.get_SubBodies();
        Iterator<Object> it = subBodies.iterator();
        while (it.hasNext()) {
            if (parent instanceof ASTTryNode) {
                subBody = (List) ((ASTTryNode.container) it.next()).o;
            } else {
                subBody = (List) it.next();
            }
            if (subBody.contains(child)) {
                if (subBody.indexOf(child) != subBody.size() - 1) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}
