package polyglot.ext.jl.ast;

import polyglot.ast.Ext;
import polyglot.ast.ExtFactory;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AbstractExtFactory_c.class */
public abstract class AbstractExtFactory_c implements ExtFactory {
    private ExtFactory nextExtFactory;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractExtFactory_c() {
        this(null);
    }

    protected AbstractExtFactory_c(ExtFactory nextExtFactory) {
        this.nextExtFactory = nextExtFactory;
    }

    @Override // polyglot.ast.ExtFactory
    public ExtFactory nextExtFactory() {
        return this.nextExtFactory;
    }

    protected Ext composeExts(Ext e1, Ext e2) {
        return e1 == null ? e2 : e2 == null ? e1 : e2.ext(composeExts(e1, e2.ext()));
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAmbAssign() {
        Ext e = extAmbAssignImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAmbAssign();
            e = composeExts(e, e2);
        }
        return postExtAmbAssign(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAmbExpr() {
        Ext e = extAmbExprImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAmbExpr();
            e = composeExts(e, e2);
        }
        return postExtAmbExpr(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAmbPrefix() {
        Ext e = extAmbPrefixImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAmbPrefix();
            e = composeExts(e, e2);
        }
        return postExtAmbPrefix(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAmbQualifierNode() {
        Ext e = extAmbQualifierNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAmbQualifierNode();
            e = composeExts(e, e2);
        }
        return postExtAmbQualifierNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAmbReceiver() {
        Ext e = extAmbReceiverImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAmbReceiver();
            e = composeExts(e, e2);
        }
        return postExtAmbReceiver(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAmbTypeNode() {
        Ext e = extAmbTypeNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAmbTypeNode();
            e = composeExts(e, e2);
        }
        return postExtAmbTypeNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extArrayAccess() {
        Ext e = extArrayAccessImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extArrayAccess();
            e = composeExts(e, e2);
        }
        return postExtArrayAccess(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extArrayInit() {
        Ext e = extArrayInitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extArrayInit();
            e = composeExts(e, e2);
        }
        return postExtArrayInit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extArrayTypeNode() {
        Ext e = extArrayTypeNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extArrayTypeNode();
            e = composeExts(e, e2);
        }
        return postExtArrayTypeNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAssert() {
        Ext e = extAssertImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAssert();
            e = composeExts(e, e2);
        }
        return postExtAssert(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extAssign() {
        Ext e = extAssignImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extAssign();
            e = composeExts(e, e2);
        }
        return postExtAssign(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLocalAssign() {
        Ext e = extLocalAssignImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLocalAssign();
            e = composeExts(e, e2);
        }
        return postExtLocalAssign(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extFieldAssign() {
        Ext e = extFieldAssignImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extFieldAssign();
            e = composeExts(e, e2);
        }
        return postExtFieldAssign(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extArrayAccessAssign() {
        Ext e = extArrayAccessAssignImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extArrayAccessAssign();
            e = composeExts(e, e2);
        }
        return postExtArrayAccessAssign(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extBinary() {
        Ext e = extBinaryImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extBinary();
            e = composeExts(e, e2);
        }
        return postExtBinary(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extBlock() {
        Ext e = extBlockImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extBlock();
            e = composeExts(e, e2);
        }
        return postExtBlock(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extBooleanLit() {
        Ext e = extBooleanLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extBooleanLit();
            e = composeExts(e, e2);
        }
        return postExtBooleanLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extBranch() {
        Ext e = extBranchImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extBranch();
            e = composeExts(e, e2);
        }
        return postExtBranch(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCall() {
        Ext e = extCallImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCall();
            e = composeExts(e, e2);
        }
        return postExtCall(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCanonicalTypeNode() {
        Ext e = extCanonicalTypeNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCanonicalTypeNode();
            e = composeExts(e, e2);
        }
        return postExtCanonicalTypeNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCase() {
        Ext e = extCaseImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCase();
            e = composeExts(e, e2);
        }
        return postExtCase(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCast() {
        Ext e = extCastImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCast();
            e = composeExts(e, e2);
        }
        return postExtCast(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCatch() {
        Ext e = extCatchImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCatch();
            e = composeExts(e, e2);
        }
        return postExtCatch(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCharLit() {
        Ext e = extCharLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCharLit();
            e = composeExts(e, e2);
        }
        return postExtCharLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extClassBody() {
        Ext e = extClassBodyImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extClassBody();
            e = composeExts(e, e2);
        }
        return postExtClassBody(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extClassDecl() {
        Ext e = extClassDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extClassDecl();
            e = composeExts(e, e2);
        }
        return postExtClassDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extClassLit() {
        Ext e = extClassLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extClassLit();
            e = composeExts(e, e2);
        }
        return postExtClassLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extClassMember() {
        Ext e = extClassMemberImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extClassMember();
            e = composeExts(e, e2);
        }
        return postExtClassMember(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extCodeDecl() {
        Ext e = extCodeDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extCodeDecl();
            e = composeExts(e, e2);
        }
        return postExtCodeDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extConditional() {
        Ext e = extConditionalImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extConditional();
            e = composeExts(e, e2);
        }
        return postExtConditional(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extConstructorCall() {
        Ext e = extConstructorCallImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extConstructorCall();
            e = composeExts(e, e2);
        }
        return postExtConstructorCall(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extConstructorDecl() {
        Ext e = extConstructorDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extConstructorDecl();
            e = composeExts(e, e2);
        }
        return postExtConstructorDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extDo() {
        Ext e = extDoImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extDo();
            e = composeExts(e, e2);
        }
        return postExtDo(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extEmpty() {
        Ext e = extEmptyImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extEmpty();
            e = composeExts(e, e2);
        }
        return postExtEmpty(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extEval() {
        Ext e = extEvalImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extEval();
            e = composeExts(e, e2);
        }
        return postExtEval(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extExpr() {
        Ext e = extExprImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extExpr();
            e = composeExts(e, e2);
        }
        return postExtExpr(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extField() {
        Ext e = extFieldImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extField();
            e = composeExts(e, e2);
        }
        return postExtField(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extFieldDecl() {
        Ext e = extFieldDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extFieldDecl();
            e = composeExts(e, e2);
        }
        return postExtFieldDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extFloatLit() {
        Ext e = extFloatLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extFloatLit();
            e = composeExts(e, e2);
        }
        return postExtFloatLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extFor() {
        Ext e = extForImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extFor();
            e = composeExts(e, e2);
        }
        return postExtFor(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extFormal() {
        Ext e = extFormalImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extFormal();
            e = composeExts(e, e2);
        }
        return postExtFormal(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extIf() {
        Ext e = extIfImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extIf();
            e = composeExts(e, e2);
        }
        return postExtIf(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extImport() {
        Ext e = extImportImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extImport();
            e = composeExts(e, e2);
        }
        return postExtImport(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extInitializer() {
        Ext e = extInitializerImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extInitializer();
            e = composeExts(e, e2);
        }
        return postExtInitializer(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extInstanceof() {
        Ext e = extInstanceofImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extInstanceof();
            e = composeExts(e, e2);
        }
        return postExtInstanceof(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extIntLit() {
        Ext e = extIntLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extIntLit();
            e = composeExts(e, e2);
        }
        return postExtIntLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLabeled() {
        Ext e = extLabeledImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLabeled();
            e = composeExts(e, e2);
        }
        return postExtLabeled(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLit() {
        Ext e = extLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLit();
            e = composeExts(e, e2);
        }
        return postExtLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLocal() {
        Ext e = extLocalImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLocal();
            e = composeExts(e, e2);
        }
        return postExtLocal(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLocalClassDecl() {
        Ext e = extLocalClassDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLocalClassDecl();
            e = composeExts(e, e2);
        }
        return postExtLocalClassDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLocalDecl() {
        Ext e = extLocalDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLocalDecl();
            e = composeExts(e, e2);
        }
        return postExtLocalDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extLoop() {
        Ext e = extLoopImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extLoop();
            e = composeExts(e, e2);
        }
        return postExtLoop(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extMethodDecl() {
        Ext e = extMethodDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extMethodDecl();
            e = composeExts(e, e2);
        }
        return postExtMethodDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extNewArray() {
        Ext e = extNewArrayImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extNewArray();
            e = composeExts(e, e2);
        }
        return postExtNewArray(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extNode() {
        Ext e = extNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extNode();
            e = composeExts(e, e2);
        }
        return postExtNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extNew() {
        Ext e = extNewImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extNew();
            e = composeExts(e, e2);
        }
        return postExtNew(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extNullLit() {
        Ext e = extNullLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extNullLit();
            e = composeExts(e, e2);
        }
        return postExtNullLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extNumLit() {
        Ext e = extNumLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extNumLit();
            e = composeExts(e, e2);
        }
        return postExtNumLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extPackageNode() {
        Ext e = extPackageNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extPackageNode();
            e = composeExts(e, e2);
        }
        return postExtPackageNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extProcedureDecl() {
        Ext e = extProcedureDeclImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extProcedureDecl();
            e = composeExts(e, e2);
        }
        return postExtProcedureDecl(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extReturn() {
        Ext e = extReturnImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extReturn();
            e = composeExts(e, e2);
        }
        return postExtReturn(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSourceCollection() {
        Ext e = extSourceCollectionImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSourceCollection();
            e = composeExts(e, e2);
        }
        return postExtSourceCollection(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSourceFile() {
        Ext e = extSourceFileImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSourceFile();
            e = composeExts(e, e2);
        }
        return postExtSourceFile(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSpecial() {
        Ext e = extSpecialImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSpecial();
            e = composeExts(e, e2);
        }
        return postExtSpecial(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extStmt() {
        Ext e = extStmtImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extStmt();
            e = composeExts(e, e2);
        }
        return postExtStmt(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extStringLit() {
        Ext e = extStringLitImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extStringLit();
            e = composeExts(e, e2);
        }
        return postExtStringLit(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSwitchBlock() {
        Ext e = extSwitchBlockImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSwitchBlock();
            e = composeExts(e, e2);
        }
        return postExtSwitchBlock(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSwitchElement() {
        Ext e = extSwitchElementImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSwitchElement();
            e = composeExts(e, e2);
        }
        return postExtSwitchElement(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSwitch() {
        Ext e = extSwitchImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSwitch();
            e = composeExts(e, e2);
        }
        return postExtSwitch(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extSynchronized() {
        Ext e = extSynchronizedImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extSynchronized();
            e = composeExts(e, e2);
        }
        return postExtSynchronized(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extTerm() {
        Ext e = extTermImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extTerm();
            e = composeExts(e, e2);
        }
        return postExtTerm(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extThrow() {
        Ext e = extThrowImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extThrow();
            e = composeExts(e, e2);
        }
        return postExtThrow(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extTry() {
        Ext e = extTryImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extTry();
            e = composeExts(e, e2);
        }
        return postExtTry(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extTypeNode() {
        Ext e = extTypeNodeImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extTypeNode();
            e = composeExts(e, e2);
        }
        return postExtTypeNode(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extUnary() {
        Ext e = extUnaryImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extUnary();
            e = composeExts(e, e2);
        }
        return postExtUnary(e);
    }

    @Override // polyglot.ast.ExtFactory
    public final Ext extWhile() {
        Ext e = extWhileImpl();
        if (this.nextExtFactory != null) {
            Ext e2 = this.nextExtFactory.extWhile();
            e = composeExts(e, e2);
        }
        return postExtWhile(e);
    }

    protected Ext extAmbAssignImpl() {
        return extAssignImpl();
    }

    protected Ext extAmbExprImpl() {
        return extExprImpl();
    }

    protected Ext extAmbPrefixImpl() {
        return extNodeImpl();
    }

    protected Ext extAmbQualifierNodeImpl() {
        return extNodeImpl();
    }

    protected Ext extAmbReceiverImpl() {
        return extNodeImpl();
    }

    protected Ext extAmbTypeNodeImpl() {
        return extTypeNodeImpl();
    }

    protected Ext extArrayAccessImpl() {
        return extExprImpl();
    }

    protected Ext extArrayInitImpl() {
        return extExprImpl();
    }

    protected Ext extArrayTypeNodeImpl() {
        return extTypeNodeImpl();
    }

    protected Ext extAssertImpl() {
        return extStmtImpl();
    }

    protected Ext extAssignImpl() {
        return extExprImpl();
    }

    protected Ext extLocalAssignImpl() {
        return extAssignImpl();
    }

    protected Ext extFieldAssignImpl() {
        return extAssignImpl();
    }

    protected Ext extArrayAccessAssignImpl() {
        return extAssignImpl();
    }

    protected Ext extBinaryImpl() {
        return extExprImpl();
    }

    protected Ext extBlockImpl() {
        return extStmtImpl();
    }

    protected Ext extBooleanLitImpl() {
        return extLitImpl();
    }

    protected Ext extBranchImpl() {
        return extStmtImpl();
    }

    protected Ext extCallImpl() {
        return extExprImpl();
    }

    protected Ext extCanonicalTypeNodeImpl() {
        return extTypeNodeImpl();
    }

    protected Ext extCaseImpl() {
        return extSwitchElementImpl();
    }

    protected Ext extCastImpl() {
        return extExprImpl();
    }

    protected Ext extCatchImpl() {
        return extStmtImpl();
    }

    protected Ext extCharLitImpl() {
        return extNumLitImpl();
    }

    protected Ext extClassBodyImpl() {
        return extTermImpl();
    }

    protected Ext extClassDeclImpl() {
        return extTermImpl();
    }

    protected Ext extClassLitImpl() {
        return extLitImpl();
    }

    protected Ext extClassMemberImpl() {
        return extNodeImpl();
    }

    protected Ext extCodeDeclImpl() {
        return extClassMemberImpl();
    }

    protected Ext extConditionalImpl() {
        return extExprImpl();
    }

    protected Ext extConstructorCallImpl() {
        return extStmtImpl();
    }

    protected Ext extConstructorDeclImpl() {
        return extProcedureDeclImpl();
    }

    protected Ext extDoImpl() {
        return extLoopImpl();
    }

    protected Ext extEmptyImpl() {
        return extStmtImpl();
    }

    protected Ext extEvalImpl() {
        return extStmtImpl();
    }

    protected Ext extExprImpl() {
        return extTermImpl();
    }

    protected Ext extFieldImpl() {
        return extExprImpl();
    }

    protected Ext extFieldDeclImpl() {
        return extClassMemberImpl();
    }

    protected Ext extFloatLitImpl() {
        return extLitImpl();
    }

    protected Ext extForImpl() {
        return extLoopImpl();
    }

    protected Ext extFormalImpl() {
        return extNodeImpl();
    }

    protected Ext extIfImpl() {
        return extStmtImpl();
    }

    protected Ext extImportImpl() {
        return extNodeImpl();
    }

    protected Ext extInitializerImpl() {
        return extCodeDeclImpl();
    }

    protected Ext extInstanceofImpl() {
        return extExprImpl();
    }

    protected Ext extIntLitImpl() {
        return extNumLitImpl();
    }

    protected Ext extLabeledImpl() {
        return extStmtImpl();
    }

    protected Ext extLitImpl() {
        return extExprImpl();
    }

    protected Ext extLocalImpl() {
        return extExprImpl();
    }

    protected Ext extLocalClassDeclImpl() {
        return extStmtImpl();
    }

    protected Ext extLocalDeclImpl() {
        return extStmtImpl();
    }

    protected Ext extLoopImpl() {
        return extStmtImpl();
    }

    protected Ext extMethodDeclImpl() {
        return extProcedureDeclImpl();
    }

    protected Ext extNewArrayImpl() {
        return extExprImpl();
    }

    protected Ext extNodeImpl() {
        return null;
    }

    protected Ext extNewImpl() {
        return extExprImpl();
    }

    protected Ext extNullLitImpl() {
        return extLitImpl();
    }

    protected Ext extNumLitImpl() {
        return extLitImpl();
    }

    protected Ext extPackageNodeImpl() {
        return extNodeImpl();
    }

    protected Ext extProcedureDeclImpl() {
        return extCodeDeclImpl();
    }

    protected Ext extReturnImpl() {
        return extStmtImpl();
    }

    protected Ext extSourceCollectionImpl() {
        return extNodeImpl();
    }

    protected Ext extSourceFileImpl() {
        return extNodeImpl();
    }

    protected Ext extSpecialImpl() {
        return extExprImpl();
    }

    protected Ext extStmtImpl() {
        return extTermImpl();
    }

    protected Ext extStringLitImpl() {
        return extLitImpl();
    }

    protected Ext extSwitchBlockImpl() {
        return extSwitchElementImpl();
    }

    protected Ext extSwitchElementImpl() {
        return extStmtImpl();
    }

    protected Ext extSwitchImpl() {
        return extStmtImpl();
    }

    protected Ext extSynchronizedImpl() {
        return extStmtImpl();
    }

    protected Ext extTermImpl() {
        return extNodeImpl();
    }

    protected Ext extThrowImpl() {
        return extStmtImpl();
    }

    protected Ext extTryImpl() {
        return extStmtImpl();
    }

    protected Ext extTypeNodeImpl() {
        return extNodeImpl();
    }

    protected Ext extUnaryImpl() {
        return extExprImpl();
    }

    protected Ext extWhileImpl() {
        return extLoopImpl();
    }

    protected Ext postExtAmbAssign(Ext ext) {
        return postExtAssign(ext);
    }

    protected Ext postExtAmbExpr(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtAmbPrefix(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtAmbQualifierNode(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtAmbReceiver(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtAmbTypeNode(Ext ext) {
        return postExtTypeNode(ext);
    }

    protected Ext postExtArrayAccess(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtArrayInit(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtArrayTypeNode(Ext ext) {
        return postExtTypeNode(ext);
    }

    protected Ext postExtAssert(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtAssign(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtLocalAssign(Ext ext) {
        return postExtAssign(ext);
    }

    protected Ext postExtFieldAssign(Ext ext) {
        return postExtAssign(ext);
    }

    protected Ext postExtArrayAccessAssign(Ext ext) {
        return postExtAssign(ext);
    }

    protected Ext postExtBinary(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtBlock(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtBooleanLit(Ext ext) {
        return postExtLit(ext);
    }

    protected Ext postExtBranch(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtCall(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtCanonicalTypeNode(Ext ext) {
        return postExtTypeNode(ext);
    }

    protected Ext postExtCase(Ext ext) {
        return postExtSwitchElement(ext);
    }

    protected Ext postExtCast(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtCatch(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtCharLit(Ext ext) {
        return postExtNumLit(ext);
    }

    protected Ext postExtClassBody(Ext ext) {
        return postExtTerm(ext);
    }

    protected Ext postExtClassDecl(Ext ext) {
        return postExtTerm(ext);
    }

    protected Ext postExtClassLit(Ext ext) {
        return postExtLit(ext);
    }

    protected Ext postExtClassMember(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtCodeDecl(Ext ext) {
        return postExtClassMember(ext);
    }

    protected Ext postExtConditional(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtConstructorCall(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtConstructorDecl(Ext ext) {
        return postExtProcedureDecl(ext);
    }

    protected Ext postExtDo(Ext ext) {
        return postExtLoop(ext);
    }

    protected Ext postExtEmpty(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtEval(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtExpr(Ext ext) {
        return postExtTerm(ext);
    }

    protected Ext postExtField(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtFieldDecl(Ext ext) {
        return postExtClassMember(ext);
    }

    protected Ext postExtFloatLit(Ext ext) {
        return postExtLit(ext);
    }

    protected Ext postExtFor(Ext ext) {
        return postExtLoop(ext);
    }

    protected Ext postExtFormal(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtIf(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtImport(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtInitializer(Ext ext) {
        return postExtCodeDecl(ext);
    }

    protected Ext postExtInstanceof(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtIntLit(Ext ext) {
        return postExtNumLit(ext);
    }

    protected Ext postExtLabeled(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtLit(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtLocal(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtLocalClassDecl(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtLocalDecl(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtLoop(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtMethodDecl(Ext ext) {
        return postExtProcedureDecl(ext);
    }

    protected Ext postExtNewArray(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtNode(Ext ext) {
        return ext;
    }

    protected Ext postExtNew(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtNullLit(Ext ext) {
        return postExtLit(ext);
    }

    protected Ext postExtNumLit(Ext ext) {
        return postExtLit(ext);
    }

    protected Ext postExtPackageNode(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtProcedureDecl(Ext ext) {
        return postExtCodeDecl(ext);
    }

    protected Ext postExtReturn(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtSourceCollection(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtSourceFile(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtSpecial(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtStmt(Ext ext) {
        return postExtTerm(ext);
    }

    protected Ext postExtStringLit(Ext ext) {
        return postExtLit(ext);
    }

    protected Ext postExtSwitchBlock(Ext ext) {
        return postExtSwitchElement(ext);
    }

    protected Ext postExtSwitchElement(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtSwitch(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtSynchronized(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtTerm(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtThrow(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtTry(Ext ext) {
        return postExtStmt(ext);
    }

    protected Ext postExtTypeNode(Ext ext) {
        return postExtNode(ext);
    }

    protected Ext postExtUnary(Ext ext) {
        return postExtExpr(ext);
    }

    protected Ext postExtWhile(Ext ext) {
        return postExtLoop(ext);
    }
}
