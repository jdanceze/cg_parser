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
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/analysis/Analysis.class */
public interface Analysis {
    void caseASTMethodNode(ASTMethodNode aSTMethodNode);

    void caseASTSynchronizedBlockNode(ASTSynchronizedBlockNode aSTSynchronizedBlockNode);

    void caseASTLabeledBlockNode(ASTLabeledBlockNode aSTLabeledBlockNode);

    void caseASTUnconditionalLoopNode(ASTUnconditionalLoopNode aSTUnconditionalLoopNode);

    void caseASTSwitchNode(ASTSwitchNode aSTSwitchNode);

    void caseASTIfNode(ASTIfNode aSTIfNode);

    void caseASTIfElseNode(ASTIfElseNode aSTIfElseNode);

    void caseASTWhileNode(ASTWhileNode aSTWhileNode);

    void caseASTForLoopNode(ASTForLoopNode aSTForLoopNode);

    void caseASTDoWhileNode(ASTDoWhileNode aSTDoWhileNode);

    void caseASTTryNode(ASTTryNode aSTTryNode);

    void caseASTStatementSequenceNode(ASTStatementSequenceNode aSTStatementSequenceNode);

    void caseASTUnaryCondition(ASTUnaryCondition aSTUnaryCondition);

    void caseASTBinaryCondition(ASTBinaryCondition aSTBinaryCondition);

    void caseASTAndCondition(ASTAndCondition aSTAndCondition);

    void caseASTOrCondition(ASTOrCondition aSTOrCondition);

    void caseType(Type type);

    void caseDefinitionStmt(DefinitionStmt definitionStmt);

    void caseReturnStmt(ReturnStmt returnStmt);

    void caseInvokeStmt(InvokeStmt invokeStmt);

    void caseThrowStmt(ThrowStmt throwStmt);

    void caseDVariableDeclarationStmt(DVariableDeclarationStmt dVariableDeclarationStmt);

    void caseStmt(Stmt stmt);

    void caseValue(Value value);

    void caseExpr(Expr expr);

    void caseRef(Ref ref);

    void caseBinopExpr(BinopExpr binopExpr);

    void caseUnopExpr(UnopExpr unopExpr);

    void caseNewArrayExpr(NewArrayExpr newArrayExpr);

    void caseNewMultiArrayExpr(NewMultiArrayExpr newMultiArrayExpr);

    void caseInstanceOfExpr(InstanceOfExpr instanceOfExpr);

    void caseInvokeExpr(InvokeExpr invokeExpr);

    void caseInstanceInvokeExpr(InstanceInvokeExpr instanceInvokeExpr);

    void caseCastExpr(CastExpr castExpr);

    void caseArrayRef(ArrayRef arrayRef);

    void caseInstanceFieldRef(InstanceFieldRef instanceFieldRef);

    void caseStaticFieldRef(StaticFieldRef staticFieldRef);
}
