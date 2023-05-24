package polyglot.ext.jl.ast;

import polyglot.ast.DelFactory;
import polyglot.ast.JL;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AbstractDelFactory_c.class */
public abstract class AbstractDelFactory_c implements DelFactory {
    private DelFactory nextDelFactory;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDelFactory_c() {
        this(null);
    }

    protected AbstractDelFactory_c(DelFactory nextDelFactory) {
        this.nextDelFactory = nextDelFactory;
    }

    public DelFactory nextDelFactory() {
        return this.nextDelFactory;
    }

    protected JL composeDels(JL e1, JL e2) {
        if (e1 == null) {
            return e2;
        }
        if (e2 == null) {
            return e1;
        }
        throw new InternalCompilerError("Composition of delegates unimplemented.");
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAmbAssign() {
        JL e = delAmbAssignImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAmbAssign();
            e = composeDels(e, e2);
        }
        return postDelAmbAssign(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAmbExpr() {
        JL e = delAmbExprImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAmbExpr();
            e = composeDels(e, e2);
        }
        return postDelAmbExpr(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAmbPrefix() {
        JL e = delAmbPrefixImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAmbPrefix();
            e = composeDels(e, e2);
        }
        return postDelAmbPrefix(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAmbQualifierNode() {
        JL e = delAmbQualifierNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAmbQualifierNode();
            e = composeDels(e, e2);
        }
        return postDelAmbQualifierNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAmbReceiver() {
        JL e = delAmbReceiverImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAmbReceiver();
            e = composeDels(e, e2);
        }
        return postDelAmbReceiver(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAmbTypeNode() {
        JL e = delAmbTypeNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAmbTypeNode();
            e = composeDels(e, e2);
        }
        return postDelAmbTypeNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delArrayAccess() {
        JL e = delArrayAccessImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delArrayAccess();
            e = composeDels(e, e2);
        }
        return postDelArrayAccess(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delArrayInit() {
        JL e = delArrayInitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delArrayInit();
            e = composeDels(e, e2);
        }
        return postDelArrayInit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delArrayTypeNode() {
        JL e = delArrayTypeNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delArrayTypeNode();
            e = composeDels(e, e2);
        }
        return postDelArrayTypeNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAssert() {
        JL e = delAssertImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAssert();
            e = composeDels(e, e2);
        }
        return postDelAssert(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delAssign() {
        JL e = delAssignImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delAssign();
            e = composeDels(e, e2);
        }
        return postDelAssign(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLocalAssign() {
        JL e = delLocalAssignImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLocalAssign();
            e = composeDels(e, e2);
        }
        return postDelLocalAssign(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delFieldAssign() {
        JL e = delFieldAssignImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delFieldAssign();
            e = composeDels(e, e2);
        }
        return postDelFieldAssign(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delArrayAccessAssign() {
        JL e = delArrayAccessAssignImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delArrayAccessAssign();
            e = composeDels(e, e2);
        }
        return postDelArrayAccessAssign(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delBinary() {
        JL e = delBinaryImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delBinary();
            e = composeDels(e, e2);
        }
        return postDelBinary(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delBlock() {
        JL e = delBlockImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delBlock();
            e = composeDels(e, e2);
        }
        return postDelBlock(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delBooleanLit() {
        JL e = delBooleanLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delBooleanLit();
            e = composeDels(e, e2);
        }
        return postDelBooleanLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delBranch() {
        JL e = delBranchImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delBranch();
            e = composeDels(e, e2);
        }
        return postDelBranch(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCall() {
        JL e = delCallImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCall();
            e = composeDels(e, e2);
        }
        return postDelCall(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCanonicalTypeNode() {
        JL e = delCanonicalTypeNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCanonicalTypeNode();
            e = composeDels(e, e2);
        }
        return postDelCanonicalTypeNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCase() {
        JL e = delCaseImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCase();
            e = composeDels(e, e2);
        }
        return postDelCase(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCast() {
        JL e = delCastImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCast();
            e = composeDels(e, e2);
        }
        return postDelCast(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCatch() {
        JL e = delCatchImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCatch();
            e = composeDels(e, e2);
        }
        return postDelCatch(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCharLit() {
        JL e = delCharLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCharLit();
            e = composeDels(e, e2);
        }
        return postDelCharLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delClassBody() {
        JL e = delClassBodyImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delClassBody();
            e = composeDels(e, e2);
        }
        return postDelClassBody(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delClassDecl() {
        JL e = delClassDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delClassDecl();
            e = composeDels(e, e2);
        }
        return postDelClassDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delClassLit() {
        JL e = delClassLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delClassLit();
            e = composeDels(e, e2);
        }
        return postDelClassLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delClassMember() {
        JL e = delClassMemberImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delClassMember();
            e = composeDels(e, e2);
        }
        return postDelClassMember(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delCodeDecl() {
        JL e = delCodeDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delCodeDecl();
            e = composeDels(e, e2);
        }
        return postDelCodeDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delConditional() {
        JL e = delConditionalImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delConditional();
            e = composeDels(e, e2);
        }
        return postDelConditional(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delConstructorCall() {
        JL e = delConstructorCallImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delConstructorCall();
            e = composeDels(e, e2);
        }
        return postDelConstructorCall(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delConstructorDecl() {
        JL e = delConstructorDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delConstructorDecl();
            e = composeDels(e, e2);
        }
        return postDelConstructorDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delDo() {
        JL e = delDoImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delDo();
            e = composeDels(e, e2);
        }
        return postDelDo(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delEmpty() {
        JL e = delEmptyImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delEmpty();
            e = composeDels(e, e2);
        }
        return postDelEmpty(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delEval() {
        JL e = delEvalImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delEval();
            e = composeDels(e, e2);
        }
        return postDelEval(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delExpr() {
        JL e = delExprImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delExpr();
            e = composeDels(e, e2);
        }
        return postDelExpr(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delField() {
        JL e = delFieldImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delField();
            e = composeDels(e, e2);
        }
        return postDelField(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delFieldDecl() {
        JL e = delFieldDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delFieldDecl();
            e = composeDels(e, e2);
        }
        return postDelFieldDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delFloatLit() {
        JL e = delFloatLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delFloatLit();
            e = composeDels(e, e2);
        }
        return postDelFloatLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delFor() {
        JL e = delForImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delFor();
            e = composeDels(e, e2);
        }
        return postDelFor(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delFormal() {
        JL e = delFormalImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delFormal();
            e = composeDels(e, e2);
        }
        return postDelFormal(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delIf() {
        JL e = delIfImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delIf();
            e = composeDels(e, e2);
        }
        return postDelIf(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delImport() {
        JL e = delImportImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delImport();
            e = composeDels(e, e2);
        }
        return postDelImport(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delInitializer() {
        JL e = delInitializerImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delInitializer();
            e = composeDels(e, e2);
        }
        return postDelInitializer(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delInstanceof() {
        JL e = delInstanceofImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delInstanceof();
            e = composeDels(e, e2);
        }
        return postDelInstanceof(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delIntLit() {
        JL e = delIntLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delIntLit();
            e = composeDels(e, e2);
        }
        return postDelIntLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLabeled() {
        JL e = delLabeledImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLabeled();
            e = composeDels(e, e2);
        }
        return postDelLabeled(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLit() {
        JL e = delLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLit();
            e = composeDels(e, e2);
        }
        return postDelLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLocal() {
        JL e = delLocalImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLocal();
            e = composeDels(e, e2);
        }
        return postDelLocal(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLocalClassDecl() {
        JL e = delLocalClassDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLocalClassDecl();
            e = composeDels(e, e2);
        }
        return postDelLocalClassDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLocalDecl() {
        JL e = delLocalDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLocalDecl();
            e = composeDels(e, e2);
        }
        return postDelLocalDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delLoop() {
        JL e = delLoopImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delLoop();
            e = composeDels(e, e2);
        }
        return postDelLoop(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delMethodDecl() {
        JL e = delMethodDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delMethodDecl();
            e = composeDels(e, e2);
        }
        return postDelMethodDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delNewArray() {
        JL e = delNewArrayImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delNewArray();
            e = composeDels(e, e2);
        }
        return postDelNewArray(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delNode() {
        JL e = delNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delNode();
            e = composeDels(e, e2);
        }
        return postDelNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delNew() {
        JL e = delNewImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delNew();
            e = composeDels(e, e2);
        }
        return postDelNew(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delNullLit() {
        JL e = delNullLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delNullLit();
            e = composeDels(e, e2);
        }
        return postDelNullLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delNumLit() {
        JL e = delNumLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delNumLit();
            e = composeDels(e, e2);
        }
        return postDelNumLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delPackageNode() {
        JL e = delPackageNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delPackageNode();
            e = composeDels(e, e2);
        }
        return postDelPackageNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delProcedureDecl() {
        JL e = delProcedureDeclImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delProcedureDecl();
            e = composeDels(e, e2);
        }
        return postDelProcedureDecl(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delReturn() {
        JL e = delReturnImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delReturn();
            e = composeDels(e, e2);
        }
        return postDelReturn(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSourceCollection() {
        JL e = delSourceCollectionImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSourceCollection();
            e = composeDels(e, e2);
        }
        return postDelSourceCollection(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSourceFile() {
        JL e = delSourceFileImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSourceFile();
            e = composeDels(e, e2);
        }
        return postDelSourceFile(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSpecial() {
        JL e = delSpecialImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSpecial();
            e = composeDels(e, e2);
        }
        return postDelSpecial(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delStmt() {
        JL e = delStmtImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delStmt();
            e = composeDels(e, e2);
        }
        return postDelStmt(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delStringLit() {
        JL e = delStringLitImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delStringLit();
            e = composeDels(e, e2);
        }
        return postDelStringLit(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSwitchBlock() {
        JL e = delSwitchBlockImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSwitchBlock();
            e = composeDels(e, e2);
        }
        return postDelSwitchBlock(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSwitchElement() {
        JL e = delSwitchElementImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSwitchElement();
            e = composeDels(e, e2);
        }
        return postDelSwitchElement(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSwitch() {
        JL e = delSwitchImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSwitch();
            e = composeDels(e, e2);
        }
        return postDelSwitch(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delSynchronized() {
        JL e = delSynchronizedImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delSynchronized();
            e = composeDels(e, e2);
        }
        return postDelSynchronized(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delTerm() {
        JL e = delTermImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delTerm();
            e = composeDels(e, e2);
        }
        return postDelTerm(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delThrow() {
        JL e = delThrowImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delThrow();
            e = composeDels(e, e2);
        }
        return postDelThrow(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delTry() {
        JL e = delTryImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delTry();
            e = composeDels(e, e2);
        }
        return postDelTry(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delTypeNode() {
        JL e = delTypeNodeImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delTypeNode();
            e = composeDels(e, e2);
        }
        return postDelTypeNode(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delUnary() {
        JL e = delUnaryImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delUnary();
            e = composeDels(e, e2);
        }
        return postDelUnary(e);
    }

    @Override // polyglot.ast.DelFactory
    public final JL delWhile() {
        JL e = delWhileImpl();
        if (this.nextDelFactory != null) {
            JL e2 = this.nextDelFactory.delWhile();
            e = composeDels(e, e2);
        }
        return postDelWhile(e);
    }

    protected JL delAmbAssignImpl() {
        return delAssignImpl();
    }

    protected JL delAmbExprImpl() {
        return delExprImpl();
    }

    protected JL delAmbPrefixImpl() {
        return delNodeImpl();
    }

    protected JL delAmbQualifierNodeImpl() {
        return delNodeImpl();
    }

    protected JL delAmbReceiverImpl() {
        return delNodeImpl();
    }

    protected JL delAmbTypeNodeImpl() {
        return delTypeNodeImpl();
    }

    protected JL delArrayAccessImpl() {
        return delExprImpl();
    }

    protected JL delArrayInitImpl() {
        return delExprImpl();
    }

    protected JL delArrayTypeNodeImpl() {
        return delTypeNodeImpl();
    }

    protected JL delAssertImpl() {
        return delStmtImpl();
    }

    protected JL delAssignImpl() {
        return delExprImpl();
    }

    protected JL delLocalAssignImpl() {
        return delAssignImpl();
    }

    protected JL delFieldAssignImpl() {
        return delAssignImpl();
    }

    protected JL delArrayAccessAssignImpl() {
        return delAssignImpl();
    }

    protected JL delBinaryImpl() {
        return delExprImpl();
    }

    protected JL delBlockImpl() {
        return delStmtImpl();
    }

    protected JL delBooleanLitImpl() {
        return delLitImpl();
    }

    protected JL delBranchImpl() {
        return delStmtImpl();
    }

    protected JL delCallImpl() {
        return delExprImpl();
    }

    protected JL delCanonicalTypeNodeImpl() {
        return delTypeNodeImpl();
    }

    protected JL delCaseImpl() {
        return delSwitchElementImpl();
    }

    protected JL delCastImpl() {
        return delExprImpl();
    }

    protected JL delCatchImpl() {
        return delStmtImpl();
    }

    protected JL delCharLitImpl() {
        return delNumLitImpl();
    }

    protected JL delClassBodyImpl() {
        return delTermImpl();
    }

    protected JL delClassDeclImpl() {
        return delTermImpl();
    }

    protected JL delClassLitImpl() {
        return delLitImpl();
    }

    protected JL delClassMemberImpl() {
        return delNodeImpl();
    }

    protected JL delCodeDeclImpl() {
        return delClassMemberImpl();
    }

    protected JL delConditionalImpl() {
        return delExprImpl();
    }

    protected JL delConstructorCallImpl() {
        return delStmtImpl();
    }

    protected JL delConstructorDeclImpl() {
        return delProcedureDeclImpl();
    }

    protected JL delDoImpl() {
        return delLoopImpl();
    }

    protected JL delEmptyImpl() {
        return delStmtImpl();
    }

    protected JL delEvalImpl() {
        return delStmtImpl();
    }

    protected JL delExprImpl() {
        return delTermImpl();
    }

    protected JL delFieldImpl() {
        return delExprImpl();
    }

    protected JL delFieldDeclImpl() {
        return delClassMemberImpl();
    }

    protected JL delFloatLitImpl() {
        return delLitImpl();
    }

    protected JL delForImpl() {
        return delLoopImpl();
    }

    protected JL delFormalImpl() {
        return delNodeImpl();
    }

    protected JL delIfImpl() {
        return delStmtImpl();
    }

    protected JL delImportImpl() {
        return delNodeImpl();
    }

    protected JL delInitializerImpl() {
        return delCodeDeclImpl();
    }

    protected JL delInstanceofImpl() {
        return delExprImpl();
    }

    protected JL delIntLitImpl() {
        return delNumLitImpl();
    }

    protected JL delLabeledImpl() {
        return delStmtImpl();
    }

    protected JL delLitImpl() {
        return delExprImpl();
    }

    protected JL delLocalImpl() {
        return delExprImpl();
    }

    protected JL delLocalClassDeclImpl() {
        return delStmtImpl();
    }

    protected JL delLocalDeclImpl() {
        return delNodeImpl();
    }

    protected JL delLoopImpl() {
        return delStmtImpl();
    }

    protected JL delMethodDeclImpl() {
        return delProcedureDeclImpl();
    }

    protected JL delNewArrayImpl() {
        return delExprImpl();
    }

    protected JL delNodeImpl() {
        return null;
    }

    protected JL delNewImpl() {
        return delExprImpl();
    }

    protected JL delNullLitImpl() {
        return delLitImpl();
    }

    protected JL delNumLitImpl() {
        return delLitImpl();
    }

    protected JL delPackageNodeImpl() {
        return delNodeImpl();
    }

    protected JL delProcedureDeclImpl() {
        return delCodeDeclImpl();
    }

    protected JL delReturnImpl() {
        return delStmtImpl();
    }

    protected JL delSourceCollectionImpl() {
        return delNodeImpl();
    }

    protected JL delSourceFileImpl() {
        return delNodeImpl();
    }

    protected JL delSpecialImpl() {
        return delExprImpl();
    }

    protected JL delStmtImpl() {
        return delTermImpl();
    }

    protected JL delStringLitImpl() {
        return delLitImpl();
    }

    protected JL delSwitchBlockImpl() {
        return delSwitchElementImpl();
    }

    protected JL delSwitchElementImpl() {
        return delStmtImpl();
    }

    protected JL delSwitchImpl() {
        return delStmtImpl();
    }

    protected JL delSynchronizedImpl() {
        return delStmtImpl();
    }

    protected JL delTermImpl() {
        return delNodeImpl();
    }

    protected JL delThrowImpl() {
        return delStmtImpl();
    }

    protected JL delTryImpl() {
        return delStmtImpl();
    }

    protected JL delTypeNodeImpl() {
        return delNodeImpl();
    }

    protected JL delUnaryImpl() {
        return delExprImpl();
    }

    protected JL delWhileImpl() {
        return delLoopImpl();
    }

    protected JL postDelAmbAssign(JL del) {
        return postDelAssign(del);
    }

    protected JL postDelAmbExpr(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelAmbPrefix(JL del) {
        return postDelNode(del);
    }

    protected JL postDelAmbQualifierNode(JL del) {
        return postDelNode(del);
    }

    protected JL postDelAmbReceiver(JL del) {
        return postDelNode(del);
    }

    protected JL postDelAmbTypeNode(JL del) {
        return postDelTypeNode(del);
    }

    protected JL postDelArrayAccess(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelArrayInit(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelArrayTypeNode(JL del) {
        return postDelTypeNode(del);
    }

    protected JL postDelAssert(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelAssign(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelLocalAssign(JL del) {
        return postDelAssign(del);
    }

    protected JL postDelFieldAssign(JL del) {
        return postDelAssign(del);
    }

    protected JL postDelArrayAccessAssign(JL del) {
        return postDelAssign(del);
    }

    protected JL postDelBinary(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelBlock(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelBooleanLit(JL del) {
        return postDelLit(del);
    }

    protected JL postDelBranch(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelCall(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelCanonicalTypeNode(JL del) {
        return postDelTypeNode(del);
    }

    protected JL postDelCase(JL del) {
        return postDelSwitchElement(del);
    }

    protected JL postDelCast(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelCatch(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelCharLit(JL del) {
        return postDelNumLit(del);
    }

    protected JL postDelClassBody(JL del) {
        return postDelTerm(del);
    }

    protected JL postDelClassDecl(JL del) {
        return postDelTerm(del);
    }

    protected JL postDelClassLit(JL del) {
        return postDelLit(del);
    }

    protected JL postDelClassMember(JL del) {
        return postDelNode(del);
    }

    protected JL postDelCodeDecl(JL del) {
        return postDelClassMember(del);
    }

    protected JL postDelConditional(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelConstructorCall(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelConstructorDecl(JL del) {
        return postDelProcedureDecl(del);
    }

    protected JL postDelDo(JL del) {
        return postDelLoop(del);
    }

    protected JL postDelEmpty(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelEval(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelExpr(JL del) {
        return postDelTerm(del);
    }

    protected JL postDelField(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelFieldDecl(JL del) {
        return postDelClassMember(del);
    }

    protected JL postDelFloatLit(JL del) {
        return postDelLit(del);
    }

    protected JL postDelFor(JL del) {
        return postDelLoop(del);
    }

    protected JL postDelFormal(JL del) {
        return postDelNode(del);
    }

    protected JL postDelIf(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelImport(JL del) {
        return postDelNode(del);
    }

    protected JL postDelInitializer(JL del) {
        return postDelCodeDecl(del);
    }

    protected JL postDelInstanceof(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelIntLit(JL del) {
        return postDelNumLit(del);
    }

    protected JL postDelLabeled(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelLit(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelLocal(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelLocalClassDecl(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelLocalDecl(JL del) {
        return postDelNode(del);
    }

    protected JL postDelLoop(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelMethodDecl(JL del) {
        return postDelProcedureDecl(del);
    }

    protected JL postDelNewArray(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelNode(JL del) {
        return del;
    }

    protected JL postDelNew(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelNullLit(JL del) {
        return postDelLit(del);
    }

    protected JL postDelNumLit(JL del) {
        return postDelLit(del);
    }

    protected JL postDelPackageNode(JL del) {
        return postDelNode(del);
    }

    protected JL postDelProcedureDecl(JL del) {
        return postDelCodeDecl(del);
    }

    protected JL postDelReturn(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelSourceCollection(JL del) {
        return postDelNode(del);
    }

    protected JL postDelSourceFile(JL del) {
        return postDelNode(del);
    }

    protected JL postDelSpecial(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelStmt(JL del) {
        return postDelTerm(del);
    }

    protected JL postDelStringLit(JL del) {
        return postDelLit(del);
    }

    protected JL postDelSwitchBlock(JL del) {
        return postDelSwitchElement(del);
    }

    protected JL postDelSwitchElement(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelSwitch(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelSynchronized(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelTerm(JL del) {
        return postDelNode(del);
    }

    protected JL postDelThrow(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelTry(JL del) {
        return postDelStmt(del);
    }

    protected JL postDelTypeNode(JL del) {
        return postDelNode(del);
    }

    protected JL postDelUnary(JL del) {
        return postDelExpr(del);
    }

    protected JL postDelWhile(JL del) {
        return postDelLoop(del);
    }
}
