package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/LocalDecl_c.class */
public class LocalDecl_c extends Stmt_c implements LocalDecl {
    protected Flags flags;
    protected TypeNode type;
    protected String name;
    protected Expr init;
    protected LocalInstance li;

    public LocalDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
        super(pos);
        this.flags = flags;
        this.type = type;
        this.name = name;
        this.init = init;
    }

    @Override // polyglot.ast.VarDecl
    public Type declType() {
        return this.type.type();
    }

    @Override // polyglot.ast.VarDecl
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.ast.LocalDecl
    public LocalDecl flags(Flags flags) {
        LocalDecl_c n = (LocalDecl_c) copy();
        n.flags = flags;
        return n;
    }

    @Override // polyglot.ast.VarDecl
    public TypeNode type() {
        return this.type;
    }

    @Override // polyglot.ast.LocalDecl
    public LocalDecl type(TypeNode type) {
        if (type == this.type) {
            return this;
        }
        LocalDecl_c n = (LocalDecl_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ast.VarDecl
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.LocalDecl
    public LocalDecl name(String name) {
        if (name.equals(this.name)) {
            return this;
        }
        LocalDecl_c n = (LocalDecl_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.LocalDecl
    public Expr init() {
        return this.init;
    }

    @Override // polyglot.ast.LocalDecl
    public LocalDecl init(Expr init) {
        if (init == this.init) {
            return this;
        }
        LocalDecl_c n = (LocalDecl_c) copy();
        n.init = init;
        return n;
    }

    @Override // polyglot.ast.LocalDecl
    public LocalDecl localInstance(LocalInstance li) {
        if (li == this.li) {
            return this;
        }
        LocalDecl_c n = (LocalDecl_c) copy();
        n.li = li;
        return n;
    }

    @Override // polyglot.ast.VarDecl
    public LocalInstance localInstance() {
        return this.li;
    }

    protected LocalDecl_c reconstruct(TypeNode type, Expr init) {
        if (this.type != type || this.init != init) {
            LocalDecl_c n = (LocalDecl_c) copy();
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
    public Context enterScope(Node child, Context c) {
        if (child == this.init) {
            c.addVariable(this.li);
        }
        return super.enterScope(child, c);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void addDecls(Context c) {
        c.addVariable(this.li);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        LocalDecl_c n = (LocalDecl_c) super.buildTypes(tb);
        TypeSystem ts = tb.typeSystem();
        LocalInstance li = ts.localInstance(position(), Flags.NONE, ts.unknownType(position()), name());
        return n.localInstance(li);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        TypeSystem ts = ar.typeSystem();
        LocalInstance li = ts.localInstance(position(), flags(), declType(), name());
        return localInstance(li);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
        Context c = tc.context();
        LocalInstance outerLocal = null;
        try {
            outerLocal = c.findLocal(this.li.name());
        } catch (SemanticException e) {
        }
        if (outerLocal != null && c.isLocal(this.li.name())) {
            throw new SemanticException(new StringBuffer().append("Local variable \"").append(this.name).append("\" multiply defined.  ").append("Previous definition at ").append(outerLocal.position()).append(".").toString(), position());
        }
        return super.typeCheckEnter(tc);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        LocalInstance li = this.li;
        if (li.flags().isFinal() && init() != null && init().isConstant()) {
            Object value = init().constantValue();
            li = li.constantValue(value);
        }
        try {
            ts.checkLocalFlags(this.flags);
            if (this.init != null) {
                if (this.init instanceof ArrayInit) {
                    ((ArrayInit) this.init).typeCheckElements(this.type.type());
                } else if (!ts.isImplicitCastValid(this.init.type(), this.type.type()) && !ts.equals(this.init.type(), this.type.type()) && !ts.numericConversionValid(this.type.type(), this.init.constantValue())) {
                    throw new SemanticException(new StringBuffer().append("The type of the variable initializer \"").append(this.init.type()).append("\" does not match that of ").append("the declaration \"").append(this.type.type()).append("\".").toString(), this.init.position());
                }
            }
            return localInstance(li);
        } catch (SemanticException e) {
            throw new SemanticException(e.getMessage(), position());
        }
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

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.flags.translate()).append(this.type).append(Instruction.argsep).append(this.name).append(this.init != null ? new StringBuffer().append(" = ").append(this.init).toString() : "").append(";").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        boolean printSemi = tr.appendSemicolon(true);
        boolean printType = tr.printType(true);
        w.write(this.flags.translate());
        if (printType) {
            print(this.type, w, tr);
            w.write(Instruction.argsep);
        }
        w.write(this.name);
        if (this.init != null) {
            w.write(" =");
            w.allowBreak(2, Instruction.argsep);
            print(this.init, w, tr);
        }
        if (printSemi) {
            w.write(";");
        }
        tr.printType(printType);
        tr.appendSemicolon(printSemi);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.li != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(instance ").append(this.li).append(")").toString());
            w.end();
        }
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name ").append(this.name).append(")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        if (init() != null) {
            return init().entry();
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (init() != null) {
            v.visitCFG(init(), this);
        }
        return succs;
    }
}
