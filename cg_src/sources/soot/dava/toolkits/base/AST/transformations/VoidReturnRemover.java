package soot.dava.toolkits.base.AST.transformations;

import java.util.List;
import soot.SootClass;
import soot.SootMethod;
import soot.VoidType;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/VoidReturnRemover.class */
public class VoidReturnRemover {
    public static void cleanClass(SootClass s) {
        List<SootMethod> methods = s.getMethods();
        for (SootMethod sootMethod : methods) {
            removeReturn(sootMethod);
        }
    }

    private static void removeReturn(SootMethod method) {
        if (!(method.getReturnType() instanceof VoidType) || !method.hasActiveBody()) {
            return;
        }
        Chain units = ((DavaBody) method.getActiveBody()).getUnits();
        if (units.size() != 1) {
            return;
        }
        ASTNode AST = (ASTNode) units.getFirst();
        if (!(AST instanceof ASTMethodNode)) {
            throw new RuntimeException("Starting node of DavaBody AST is not an ASTMethodNode");
        }
        ASTMethodNode node = (ASTMethodNode) AST;
        List<Object> subBodies = node.get_SubBodies();
        if (subBodies.size() != 1) {
            return;
        }
        List subBody = (List) subBodies.get(0);
        if (subBody.size() == 0) {
            return;
        }
        ASTNode last = (ASTNode) subBody.get(subBody.size() - 1);
        if (!(last instanceof ASTStatementSequenceNode)) {
            return;
        }
        List<AugmentedStmt> stmts = ((ASTStatementSequenceNode) last).getStatements();
        if (stmts.size() == 0) {
            subBody.remove(subBody.size() - 1);
            return;
        }
        AugmentedStmt lastas = stmts.get(stmts.size() - 1);
        Stmt lastStmt = lastas.get_Stmt();
        if (!(lastStmt instanceof ReturnVoidStmt)) {
            return;
        }
        stmts.remove(stmts.size() - 1);
        if (stmts.size() == 0) {
            subBody.remove(subBody.size() - 1);
        }
    }
}
