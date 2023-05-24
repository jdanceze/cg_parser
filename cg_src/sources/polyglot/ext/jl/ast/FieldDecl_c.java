package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/FieldDecl_c.class */
public class FieldDecl_c extends Term_c implements FieldDecl {
    protected Flags flags;
    protected TypeNode type;
    protected String name;
    protected Expr init;
    protected FieldInstance fi;
    protected InitializerInstance ii;

    public FieldDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        super(pos);
        this.flags = flags;
        this.type = type;
        this.name = name;
        this.init = init;
    }

    @Override // polyglot.ast.FieldDecl
    public InitializerInstance initializerInstance() {
        return this.ii;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldDecl initializerInstance(InitializerInstance ii) {
        FieldDecl_c n = (FieldDecl_c) copy();
        n.ii = ii;
        return n;
    }

    @Override // polyglot.ast.FieldDecl
    public Type declType() {
        return this.type.type();
    }

    @Override // polyglot.ast.FieldDecl
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldDecl flags(Flags flags) {
        FieldDecl_c n = (FieldDecl_c) copy();
        n.flags = flags;
        return n;
    }

    @Override // polyglot.ast.FieldDecl
    public TypeNode type() {
        return this.type;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldDecl type(TypeNode type) {
        FieldDecl_c n = (FieldDecl_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ast.FieldDecl
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldDecl name(String name) {
        FieldDecl_c n = (FieldDecl_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.FieldDecl
    public Expr init() {
        return this.init;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldDecl init(Expr init) {
        FieldDecl_c n = (FieldDecl_c) copy();
        n.init = init;
        return n;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldDecl fieldInstance(FieldInstance fi) {
        FieldDecl_c n = (FieldDecl_c) copy();
        n.fi = fi;
        return n;
    }

    @Override // polyglot.ast.FieldDecl
    public FieldInstance fieldInstance() {
        return this.fi;
    }

    protected FieldDecl_c reconstruct(TypeNode type, Expr init) {
        if (this.type != type || this.init != init) {
            FieldDecl_c n = (FieldDecl_c) copy();
            n.type = type;
            n.init = init;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode type = (TypeNode) visitChild(this.type, v);
        Expr init = (Expr) visitChild(this.init, v);
        return reconstruct(type, init);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        return tb.pushCode();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        FieldDecl n;
        TypeSystem ts = tb.typeSystem();
        if (this.init != null) {
            ClassType ct = tb.currentClass();
            Flags f = this.flags.isStatic() ? Flags.STATIC : Flags.NONE;
            InitializerInstance ii = ts.initializerInstance(this.init.position(), ct, f);
            n = initializerInstance(ii);
        } else {
            n = this;
        }
        FieldInstance fi = ts.fieldInstance(n.position(), ts.Object(), Flags.NONE, ts.unknownType(position()), n.name());
        return n.fieldInstance(fi);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SUPER) {
            return ar.bypassChildren(this);
        }
        if (ar.kind() == AmbiguityRemover.SIGNATURES && this.init != null) {
            return ar.bypass(this.init);
        }
        return ar;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SIGNATURES) {
            Context c = ar.context();
            TypeSystem ts = ar.typeSystem();
            ParsedClassType ct = c.currentClassScope();
            Flags f = this.flags;
            if (ct.flags().isInterface()) {
                f = f.Public().Static().Final();
            }
            FieldInstance fi = ts.fieldInstance(position(), ct, f, declType(), this.name);
            return flags(f).fieldInstance(fi);
        }
        if (ar.kind() == AmbiguityRemover.ALL) {
            checkFieldInstanceConstant();
        }
        return this;
    }

    protected void checkFieldInstanceConstant() {
        FieldInstance fi = this.fi;
        if (this.init != null && fi.flags().isFinal() && this.init.isConstant()) {
            Object value = this.init.constantValue();
            fi.setConstantValue(value);
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor addMembersEnter(AddMemberVisitor am) {
        ParsedClassType ct = am.context().currentClassScope();
        FieldInstance fi = this.fi;
        if (fi == null) {
            throw new InternalCompilerError("null field instance");
        }
        if (Report.should_report(Report.types, 5)) {
            Report.report(5, new StringBuffer().append("adding ").append(fi).append(" to ").append(ct).toString());
        }
        ct.addField(fi);
        return am.bypassChildren(this);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        if (this.ii != null) {
            return c.pushCode(this.ii);
        }
        return c;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        checkFieldInstanceConstant();
        try {
            ts.checkFieldFlags(this.flags);
            if (tc.context().currentClass().flags().isInterface() && (this.flags.isProtected() || this.flags.isPrivate())) {
                throw new SemanticException("Interface members must be public.", position());
            }
            if (this.init != null) {
                if (this.init instanceof ArrayInit) {
                    ((ArrayInit) this.init).typeCheckElements(this.type.type());
                } else if (!ts.isImplicitCastValid(this.init.type(), this.type.type()) && !ts.equals(this.init.type(), this.type.type()) && !ts.numericConversionValid(this.type.type(), this.init.constantValue())) {
                    throw new SemanticException(new StringBuffer().append("The type of the variable initializer \"").append(this.init.type()).append("\" does not match that of ").append("the declaration \"").append(this.type.type()).append("\".").toString(), this.init.position());
                }
            }
            if (flags().isStatic() && fieldInstance().container().toClass().isInnerClass() && (!flags().isFinal() || this.init == null || !this.init.isConstant())) {
                throw new SemanticException("Inner classes cannot declare static fields, unless they are compile-time constant fields.", position());
            }
            return this;
        } catch (SemanticException e) {
            throw new SemanticException(e.getMessage(), position());
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        ec.typeSystem();
        SubtypeSet s = ec.throwsSet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            Type t = (Type) i.next();
            if (!t.isUncheckedException()) {
                ec.throwsSet().clear();
                throw new SemanticException(new StringBuffer().append("A field initializer may not throw a ").append(t).append(".").toString(), position());
            }
        }
        ec.throwsSet().clear();
        return super.exceptionCheck(ec);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.init) {
            TypeSystem ts = av.typeSystem();
            if (ts.numericConversionValid(this.type.type(), child.constantValue())) {
                return child.type();
            }
            return this.type.type();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.init != null ? this.init.entry() : this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.init != null) {
            v.visitCFG(this.init, this);
        }
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.flags.translate()).append(this.type).append(Instruction.argsep).append(this.name).append(this.init != null ? new StringBuffer().append(" = ").append(this.init).toString() : "").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        boolean isInterface = (this.fi == null || this.fi.container() == null || !this.fi.container().toClass().flags().isInterface()) ? false : true;
        Flags f = this.flags;
        if (isInterface) {
            f = f.clearPublic().clearStatic().clearFinal();
        }
        w.write(f.translate());
        print(this.type, w, tr);
        w.write(Instruction.argsep);
        w.write(this.name);
        if (this.init != null) {
            w.write(" =");
            w.allowBreak(2, Instruction.argsep);
            print(this.init, w, tr);
        }
        w.write(";");
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.fi != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(instance ").append(this.fi).append(")").toString());
            w.end();
        }
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name ").append(this.name).append(")").toString());
        w.end();
    }
}
