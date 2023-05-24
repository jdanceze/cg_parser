package soot.dava.toolkits.base.AST.analysis;

import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/analysis/AnalysisAdapter.class */
public class AnalysisAdapter implements Analysis {
    public void defaultCase(Object o) {
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTMethodNode(ASTMethodNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTLabeledBlockNode(ASTLabeledBlockNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTSwitchNode(ASTSwitchNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTIfNode(ASTIfNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTIfElseNode(ASTIfElseNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTWhileNode(ASTWhileNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTForLoopNode(ASTForLoopNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTDoWhileNode(ASTDoWhileNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTTryNode(ASTTryNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
        defaultCase(node);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTUnaryCondition(ASTUnaryCondition uc) {
        defaultCase(uc);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTBinaryCondition(ASTBinaryCondition bc) {
        defaultCase(bc);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTAndCondition(ASTAndCondition ac) {
        defaultCase(ac);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseASTOrCondition(ASTOrCondition oc) {
        defaultCase(oc);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseType(Type t) {
        defaultCase(t);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseDefinitionStmt(DefinitionStmt s) {
        defaultCase(s);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseReturnStmt(ReturnStmt s) {
        defaultCase(s);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInvokeStmt(InvokeStmt s) {
        defaultCase(s);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseThrowStmt(ThrowStmt s) {
        defaultCase(s);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseDVariableDeclarationStmt(DVariableDeclarationStmt s) {
        defaultCase(s);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseStmt(Stmt s) {
        defaultCase(s);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseValue(Value v) {
        defaultCase(v);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseExpr(Expr e) {
        defaultCase(e);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseRef(Ref r) {
        defaultCase(r);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseBinopExpr(BinopExpr be) {
        defaultCase(be);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseUnopExpr(UnopExpr ue) {
        defaultCase(ue);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseNewArrayExpr(NewArrayExpr nae) {
        defaultCase(nae);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
        defaultCase(nmae);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInstanceOfExpr(InstanceOfExpr ioe) {
        defaultCase(ioe);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInvokeExpr(InvokeExpr ie) {
        defaultCase(ie);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInstanceInvokeExpr(InstanceInvokeExpr iie) {
        defaultCase(iie);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseCastExpr(CastExpr ce) {
        defaultCase(ce);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseArrayRef(ArrayRef ar) {
        defaultCase(ar);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseInstanceFieldRef(InstanceFieldRef ifr) {
        defaultCase(ifr);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.Analysis
    public void caseStaticFieldRef(StaticFieldRef sfr) {
        defaultCase(sfr);
    }
}
