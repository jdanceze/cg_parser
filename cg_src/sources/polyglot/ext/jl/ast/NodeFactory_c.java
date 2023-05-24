package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.AmbAssign;
import polyglot.ast.AmbExpr;
import polyglot.ast.AmbPrefix;
import polyglot.ast.AmbQualifierNode;
import polyglot.ast.AmbReceiver;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.ArrayInit;
import polyglot.ast.ArrayTypeNode;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.BooleanLit;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.CharLit;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.Conditional;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.DelFactory;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.ExtFactory;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.FloatLit;
import polyglot.ast.For;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Import;
import polyglot.ast.Initializer;
import polyglot.ast.Instanceof;
import polyglot.ast.IntLit;
import polyglot.ast.Labeled;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.NullLit;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.SourceCollection;
import polyglot.ast.SourceFile;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.ast.StringLit;
import polyglot.ast.Switch;
import polyglot.ast.SwitchBlock;
import polyglot.ast.Synchronized;
import polyglot.ast.Throw;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/NodeFactory_c.class */
public class NodeFactory_c extends AbstractNodeFactory_c {
    private final ExtFactory extFactory;
    private final DelFactory delFactory;

    public NodeFactory_c() {
        this(new AbstractExtFactory_c() { // from class: polyglot.ext.jl.ast.NodeFactory_c.1
        }, new AbstractDelFactory_c() { // from class: polyglot.ext.jl.ast.NodeFactory_c.2
        });
    }

    public NodeFactory_c(ExtFactory extFactory) {
        this(extFactory, new AbstractDelFactory_c() { // from class: polyglot.ext.jl.ast.NodeFactory_c.3
        });
    }

    public NodeFactory_c(ExtFactory extFactory, DelFactory delFactory) {
        this.extFactory = extFactory;
        this.delFactory = delFactory;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExtFactory extFactory() {
        return this.extFactory;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DelFactory delFactory() {
        return this.delFactory;
    }

    protected final ExtFactory findExtFactInstance(Class c) {
        ExtFactory extFactory = extFactory();
        while (true) {
            ExtFactory e = extFactory;
            if (e != null) {
                if (c.isInstance(e)) {
                    return e;
                }
                extFactory = e.nextExtFactory();
            } else {
                return null;
            }
        }
    }

    @Override // polyglot.ast.NodeFactory
    public AmbPrefix AmbPrefix(Position pos, Prefix prefix, String name) {
        AmbPrefix n = new AmbPrefix_c(pos, prefix, name);
        return (AmbPrefix) ((AmbPrefix) n.ext(this.extFactory.extAmbPrefix())).del(this.delFactory.delAmbPrefix());
    }

    @Override // polyglot.ast.NodeFactory
    public AmbReceiver AmbReceiver(Position pos, Prefix prefix, String name) {
        AmbReceiver n = new AmbReceiver_c(pos, prefix, name);
        return (AmbReceiver) ((AmbReceiver) n.ext(this.extFactory.extAmbReceiver())).del(this.delFactory.delAmbReceiver());
    }

    @Override // polyglot.ast.NodeFactory
    public AmbQualifierNode AmbQualifierNode(Position pos, QualifierNode qualifier, String name) {
        AmbQualifierNode n = new AmbQualifierNode_c(pos, qualifier, name);
        return (AmbQualifierNode) ((AmbQualifierNode) n.ext(this.extFactory.extAmbQualifierNode())).del(this.delFactory.delAmbQualifierNode());
    }

    @Override // polyglot.ast.NodeFactory
    public AmbExpr AmbExpr(Position pos, String name) {
        AmbExpr n = new AmbExpr_c(pos, name);
        return (AmbExpr) ((AmbExpr) n.ext(this.extFactory.extAmbExpr())).del(this.delFactory.delAmbExpr());
    }

    @Override // polyglot.ast.NodeFactory
    public AmbTypeNode AmbTypeNode(Position pos, QualifierNode qualifier, String name) {
        AmbTypeNode n = new AmbTypeNode_c(pos, qualifier, name);
        return (AmbTypeNode) ((AmbTypeNode) n.ext(this.extFactory.extAmbTypeNode())).del(this.delFactory.delAmbTypeNode());
    }

    @Override // polyglot.ast.NodeFactory
    public ArrayAccess ArrayAccess(Position pos, Expr base, Expr index) {
        ArrayAccess n = new ArrayAccess_c(pos, base, index);
        return (ArrayAccess) ((ArrayAccess) n.ext(this.extFactory.extArrayAccess())).del(this.delFactory.delArrayAccess());
    }

    @Override // polyglot.ast.NodeFactory
    public ArrayInit ArrayInit(Position pos, List elements) {
        ArrayInit n = new ArrayInit_c(pos, elements);
        return (ArrayInit) ((ArrayInit) n.ext(this.extFactory.extArrayInit())).del(this.delFactory.delArrayInit());
    }

    @Override // polyglot.ast.NodeFactory
    public Assert Assert(Position pos, Expr cond, Expr errorMessage) {
        Assert n = new Assert_c(pos, cond, errorMessage);
        return (Assert) ((Assert) n.ext(this.extFactory.extAssert())).del(this.delFactory.delAssert());
    }

    @Override // polyglot.ast.NodeFactory
    public Assign Assign(Position pos, Expr left, Assign.Operator op, Expr right) {
        if (left instanceof Local) {
            return LocalAssign(pos, (Local) left, op, right);
        }
        if (left instanceof Field) {
            return FieldAssign(pos, (Field) left, op, right);
        }
        if (left instanceof ArrayAccess) {
            return ArrayAccessAssign(pos, (ArrayAccess) left, op, right);
        }
        return AmbAssign(pos, left, op, right);
    }

    @Override // polyglot.ast.NodeFactory
    public LocalAssign LocalAssign(Position pos, Local left, Assign.Operator op, Expr right) {
        LocalAssign n = new LocalAssign_c(pos, left, op, right);
        return (LocalAssign) ((LocalAssign) n.ext(this.extFactory.extLocalAssign())).del(this.delFactory.delLocalAssign());
    }

    @Override // polyglot.ast.NodeFactory
    public FieldAssign FieldAssign(Position pos, Field left, Assign.Operator op, Expr right) {
        FieldAssign n = new FieldAssign_c(pos, left, op, right);
        return (FieldAssign) ((FieldAssign) n.ext(this.extFactory.extFieldAssign())).del(this.delFactory.delFieldAssign());
    }

    @Override // polyglot.ast.NodeFactory
    public ArrayAccessAssign ArrayAccessAssign(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
        ArrayAccessAssign n = new ArrayAccessAssign_c(pos, left, op, right);
        return (ArrayAccessAssign) ((ArrayAccessAssign) n.ext(this.extFactory.extArrayAccessAssign())).del(this.delFactory.delArrayAccessAssign());
    }

    @Override // polyglot.ast.NodeFactory
    public AmbAssign AmbAssign(Position pos, Expr left, Assign.Operator op, Expr right) {
        AmbAssign n = new AmbAssign_c(pos, left, op, right);
        return (AmbAssign) ((AmbAssign) n.ext(this.extFactory.extAmbAssign())).del(this.delFactory.delAmbAssign());
    }

    @Override // polyglot.ast.NodeFactory
    public Binary Binary(Position pos, Expr left, Binary.Operator op, Expr right) {
        Binary n = new Binary_c(pos, left, op, right);
        return (Binary) ((Binary) n.ext(this.extFactory.extBinary())).del(this.delFactory.delBinary());
    }

    @Override // polyglot.ast.NodeFactory
    public Block Block(Position pos, List statements) {
        Block n = new Block_c(pos, statements);
        return (Block) ((Block) n.ext(this.extFactory.extBlock())).del(this.delFactory.delBlock());
    }

    @Override // polyglot.ast.NodeFactory
    public SwitchBlock SwitchBlock(Position pos, List statements) {
        SwitchBlock n = new SwitchBlock_c(pos, statements);
        return (SwitchBlock) ((SwitchBlock) n.ext(this.extFactory.extSwitchBlock())).del(this.delFactory.delSwitchBlock());
    }

    @Override // polyglot.ast.NodeFactory
    public BooleanLit BooleanLit(Position pos, boolean value) {
        BooleanLit n = new BooleanLit_c(pos, value);
        return (BooleanLit) ((BooleanLit) n.ext(this.extFactory.extBooleanLit())).del(this.delFactory.delBooleanLit());
    }

    @Override // polyglot.ast.NodeFactory
    public Branch Branch(Position pos, Branch.Kind kind, String label) {
        Branch n = new Branch_c(pos, kind, label);
        return (Branch) ((Branch) n.ext(this.extFactory.extBranch())).del(this.delFactory.delBranch());
    }

    @Override // polyglot.ast.NodeFactory
    public Call Call(Position pos, Receiver target, String name, List args) {
        Call n = new Call_c(pos, target, name, args);
        return (Call) ((Call) n.ext(this.extFactory.extCall())).del(this.delFactory.delCall());
    }

    @Override // polyglot.ast.NodeFactory
    public Case Case(Position pos, Expr expr) {
        Case n = new Case_c(pos, expr);
        return (Case) ((Case) n.ext(this.extFactory.extCase())).del(this.delFactory.delCase());
    }

    @Override // polyglot.ast.NodeFactory
    public Cast Cast(Position pos, TypeNode type, Expr expr) {
        Cast n = new Cast_c(pos, type, expr);
        return (Cast) ((Cast) n.ext(this.extFactory.extCast())).del(this.delFactory.delCast());
    }

    @Override // polyglot.ast.NodeFactory
    public Catch Catch(Position pos, Formal formal, Block body) {
        Catch n = new Catch_c(pos, formal, body);
        return (Catch) ((Catch) n.ext(this.extFactory.extCatch())).del(this.delFactory.delCatch());
    }

    @Override // polyglot.ast.NodeFactory
    public CharLit CharLit(Position pos, char value) {
        CharLit n = new CharLit_c(pos, value);
        return (CharLit) ((CharLit) n.ext(this.extFactory.extCharLit())).del(this.delFactory.delCharLit());
    }

    @Override // polyglot.ast.NodeFactory
    public ClassBody ClassBody(Position pos, List members) {
        ClassBody n = new ClassBody_c(pos, members);
        return (ClassBody) ((ClassBody) n.ext(this.extFactory.extClassBody())).del(this.delFactory.delClassBody());
    }

    @Override // polyglot.ast.NodeFactory
    public ClassDecl ClassDecl(Position pos, Flags flags, String name, TypeNode superClass, List interfaces, ClassBody body) {
        ClassDecl n = new ClassDecl_c(pos, flags, name, superClass, interfaces, body);
        return (ClassDecl) ((ClassDecl) n.ext(this.extFactory.extClassDecl())).del(this.delFactory.delClassDecl());
    }

    @Override // polyglot.ast.NodeFactory
    public ClassLit ClassLit(Position pos, TypeNode typeNode) {
        ClassLit n = new ClassLit_c(pos, typeNode);
        return (ClassLit) ((ClassLit) n.ext(this.extFactory.extClassLit())).del(this.delFactory.delClassLit());
    }

    @Override // polyglot.ast.NodeFactory
    public Conditional Conditional(Position pos, Expr cond, Expr consequent, Expr alternative) {
        Conditional n = new Conditional_c(pos, cond, consequent, alternative);
        return (Conditional) ((Conditional) n.ext(this.extFactory.extConditional())).del(this.delFactory.delConditional());
    }

    @Override // polyglot.ast.NodeFactory
    public ConstructorCall ConstructorCall(Position pos, ConstructorCall.Kind kind, Expr outer, List args) {
        ConstructorCall n = new ConstructorCall_c(pos, kind, outer, args);
        return (ConstructorCall) ((ConstructorCall) n.ext(this.extFactory.extConstructorCall())).del(this.delFactory.delConstructorCall());
    }

    @Override // polyglot.ast.NodeFactory
    public ConstructorDecl ConstructorDecl(Position pos, Flags flags, String name, List formals, List throwTypes, Block body) {
        ConstructorDecl n = new ConstructorDecl_c(pos, flags, name, formals, throwTypes, body);
        return (ConstructorDecl) ((ConstructorDecl) n.ext(this.extFactory.extConstructorDecl())).del(this.delFactory.delConstructorDecl());
    }

    @Override // polyglot.ast.NodeFactory
    public FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        FieldDecl n = new FieldDecl_c(pos, flags, type, name, init);
        return (FieldDecl) ((FieldDecl) n.ext(this.extFactory.extFieldDecl())).del(this.delFactory.delFieldDecl());
    }

    @Override // polyglot.ast.NodeFactory
    public Do Do(Position pos, Stmt body, Expr cond) {
        Do n = new Do_c(pos, body, cond);
        return (Do) ((Do) n.ext(this.extFactory.extDo())).del(this.delFactory.delDo());
    }

    @Override // polyglot.ast.NodeFactory
    public Empty Empty(Position pos) {
        Empty n = new Empty_c(pos);
        return (Empty) ((Empty) n.ext(this.extFactory.extEmpty())).del(this.delFactory.delEmpty());
    }

    @Override // polyglot.ast.NodeFactory
    public Eval Eval(Position pos, Expr expr) {
        Eval n = new Eval_c(pos, expr);
        return (Eval) ((Eval) n.ext(this.extFactory.extEval())).del(this.delFactory.delEval());
    }

    @Override // polyglot.ast.NodeFactory
    public Field Field(Position pos, Receiver target, String name) {
        Field n = new Field_c(pos, target, name);
        return (Field) ((Field) n.ext(this.extFactory.extField())).del(this.delFactory.delField());
    }

    @Override // polyglot.ast.NodeFactory
    public FloatLit FloatLit(Position pos, FloatLit.Kind kind, double value) {
        FloatLit n = new FloatLit_c(pos, kind, value);
        return (FloatLit) ((FloatLit) n.ext(this.extFactory.extFloatLit())).del(this.delFactory.delFloatLit());
    }

    @Override // polyglot.ast.NodeFactory
    public For For(Position pos, List inits, Expr cond, List iters, Stmt body) {
        For n = new For_c(pos, inits, cond, iters, body);
        return (For) ((For) n.ext(this.extFactory.extFor())).del(this.delFactory.delFor());
    }

    @Override // polyglot.ast.NodeFactory
    public Formal Formal(Position pos, Flags flags, TypeNode type, String name) {
        Formal n = new Formal_c(pos, flags, type, name);
        return (Formal) ((Formal) n.ext(this.extFactory.extFormal())).del(this.delFactory.delFormal());
    }

    @Override // polyglot.ast.NodeFactory
    public If If(Position pos, Expr cond, Stmt consequent, Stmt alternative) {
        If n = new If_c(pos, cond, consequent, alternative);
        return (If) ((If) n.ext(this.extFactory.extIf())).del(this.delFactory.delIf());
    }

    @Override // polyglot.ast.NodeFactory
    public Import Import(Position pos, Import.Kind kind, String name) {
        Import n = new Import_c(pos, kind, name);
        return (Import) ((Import) n.ext(this.extFactory.extImport())).del(this.delFactory.delImport());
    }

    @Override // polyglot.ast.NodeFactory
    public Initializer Initializer(Position pos, Flags flags, Block body) {
        Initializer n = new Initializer_c(pos, flags, body);
        return (Initializer) ((Initializer) n.ext(this.extFactory.extInitializer())).del(this.delFactory.delInitializer());
    }

    @Override // polyglot.ast.NodeFactory
    public Instanceof Instanceof(Position pos, Expr expr, TypeNode type) {
        Instanceof n = new Instanceof_c(pos, expr, type);
        return (Instanceof) ((Instanceof) n.ext(this.extFactory.extInstanceof())).del(this.delFactory.delInstanceof());
    }

    @Override // polyglot.ast.NodeFactory
    public IntLit IntLit(Position pos, IntLit.Kind kind, long value) {
        IntLit n = new IntLit_c(pos, kind, value);
        return (IntLit) ((IntLit) n.ext(this.extFactory.extIntLit())).del(this.delFactory.delIntLit());
    }

    @Override // polyglot.ast.NodeFactory
    public Labeled Labeled(Position pos, String label, Stmt body) {
        Labeled n = new Labeled_c(pos, label, body);
        return (Labeled) ((Labeled) n.ext(this.extFactory.extLabeled())).del(this.delFactory.delLabeled());
    }

    @Override // polyglot.ast.NodeFactory
    public Local Local(Position pos, String name) {
        Local n = new Local_c(pos, name);
        return (Local) ((Local) n.ext(this.extFactory.extLocal())).del(this.delFactory.delLocal());
    }

    @Override // polyglot.ast.NodeFactory
    public LocalClassDecl LocalClassDecl(Position pos, ClassDecl decl) {
        LocalClassDecl n = new LocalClassDecl_c(pos, decl);
        return (LocalClassDecl) ((LocalClassDecl) n.ext(this.extFactory.extLocalClassDecl())).del(this.delFactory.delLocalClassDecl());
    }

    @Override // polyglot.ast.NodeFactory
    public LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        LocalDecl n = new LocalDecl_c(pos, flags, type, name, init);
        return (LocalDecl) ((LocalDecl) n.ext(this.extFactory.extLocalDecl())).del(this.delFactory.delLocalDecl());
    }

    @Override // polyglot.ast.NodeFactory
    public MethodDecl MethodDecl(Position pos, Flags flags, TypeNode returnType, String name, List formals, List throwTypes, Block body) {
        MethodDecl n = new MethodDecl_c(pos, flags, returnType, name, formals, throwTypes, body);
        return (MethodDecl) ((MethodDecl) n.ext(this.extFactory.extMethodDecl())).del(this.delFactory.delMethodDecl());
    }

    @Override // polyglot.ast.NodeFactory
    public New New(Position pos, Expr outer, TypeNode objectType, List args, ClassBody body) {
        New n = new New_c(pos, outer, objectType, args, body);
        return (New) ((New) n.ext(this.extFactory.extNew())).del(this.delFactory.delNew());
    }

    @Override // polyglot.ast.NodeFactory
    public NewArray NewArray(Position pos, TypeNode base, List dims, int addDims, ArrayInit init) {
        NewArray n = new NewArray_c(pos, base, dims, addDims, init);
        return (NewArray) ((NewArray) n.ext(this.extFactory.extNewArray())).del(this.delFactory.delNewArray());
    }

    @Override // polyglot.ast.NodeFactory
    public NullLit NullLit(Position pos) {
        NullLit n = new NullLit_c(pos);
        return (NullLit) ((NullLit) n.ext(this.extFactory.extNullLit())).del(this.delFactory.delNullLit());
    }

    @Override // polyglot.ast.NodeFactory
    public Return Return(Position pos, Expr expr) {
        Return n = new Return_c(pos, expr);
        return (Return) ((Return) n.ext(this.extFactory.extReturn())).del(this.delFactory.delReturn());
    }

    @Override // polyglot.ast.NodeFactory
    public SourceCollection SourceCollection(Position pos, List sources) {
        SourceCollection n = new SourceCollection_c(pos, sources);
        return (SourceCollection) ((SourceCollection) n.ext(this.extFactory.extSourceCollection())).del(this.delFactory.delSourceCollection());
    }

    @Override // polyglot.ast.NodeFactory
    public SourceFile SourceFile(Position pos, PackageNode packageName, List imports, List decls) {
        SourceFile n = new SourceFile_c(pos, packageName, imports, decls);
        return (SourceFile) ((SourceFile) n.ext(this.extFactory.extSourceFile())).del(this.delFactory.delSourceFile());
    }

    @Override // polyglot.ast.NodeFactory
    public Special Special(Position pos, Special.Kind kind, TypeNode outer) {
        Special n = new Special_c(pos, kind, outer);
        return (Special) ((Special) n.ext(this.extFactory.extSpecial())).del(this.delFactory.delSpecial());
    }

    @Override // polyglot.ast.NodeFactory
    public StringLit StringLit(Position pos, String value) {
        StringLit n = new StringLit_c(pos, value);
        return (StringLit) ((StringLit) n.ext(this.extFactory.extStringLit())).del(this.delFactory.delStringLit());
    }

    @Override // polyglot.ast.NodeFactory
    public Switch Switch(Position pos, Expr expr, List elements) {
        Switch n = new Switch_c(pos, expr, elements);
        return (Switch) ((Switch) n.ext(this.extFactory.extSwitch())).del(this.delFactory.delSwitch());
    }

    @Override // polyglot.ast.NodeFactory
    public Synchronized Synchronized(Position pos, Expr expr, Block body) {
        Synchronized n = new Synchronized_c(pos, expr, body);
        return (Synchronized) ((Synchronized) n.ext(this.extFactory.extSynchronized())).del(this.delFactory.delSynchronized());
    }

    @Override // polyglot.ast.NodeFactory
    public Throw Throw(Position pos, Expr expr) {
        Throw n = new Throw_c(pos, expr);
        return (Throw) ((Throw) n.ext(this.extFactory.extThrow())).del(this.delFactory.delThrow());
    }

    @Override // polyglot.ast.NodeFactory
    public Try Try(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock) {
        Try n = new Try_c(pos, tryBlock, catchBlocks, finallyBlock);
        return (Try) ((Try) n.ext(this.extFactory.extTry())).del(this.delFactory.delTry());
    }

    @Override // polyglot.ast.NodeFactory
    public ArrayTypeNode ArrayTypeNode(Position pos, TypeNode base) {
        ArrayTypeNode n = new ArrayTypeNode_c(pos, base);
        return (ArrayTypeNode) ((ArrayTypeNode) n.ext(this.extFactory.extArrayTypeNode())).del(this.delFactory.delArrayTypeNode());
    }

    @Override // polyglot.ast.NodeFactory
    public CanonicalTypeNode CanonicalTypeNode(Position pos, Type type) {
        if (!type.isCanonical()) {
            throw new InternalCompilerError("Cannot construct a canonical type node for a non-canonical type.");
        }
        CanonicalTypeNode n = new CanonicalTypeNode_c(pos, type);
        return (CanonicalTypeNode) ((CanonicalTypeNode) n.ext(this.extFactory.extCanonicalTypeNode())).del(this.delFactory.delCanonicalTypeNode());
    }

    @Override // polyglot.ast.NodeFactory
    public PackageNode PackageNode(Position pos, Package p) {
        PackageNode n = new PackageNode_c(pos, p);
        return (PackageNode) ((PackageNode) n.ext(this.extFactory.extPackageNode())).del(this.delFactory.delPackageNode());
    }

    @Override // polyglot.ast.NodeFactory
    public Unary Unary(Position pos, Unary.Operator op, Expr expr) {
        Unary n = new Unary_c(pos, op, expr);
        return (Unary) ((Unary) n.ext(this.extFactory.extUnary())).del(this.delFactory.delUnary());
    }

    @Override // polyglot.ast.NodeFactory
    public While While(Position pos, Expr cond, Stmt body) {
        While n = new While_c(pos, cond, body);
        return (While) ((While) n.ext(this.extFactory.extWhile())).del(this.delFactory.delWhile());
    }
}
