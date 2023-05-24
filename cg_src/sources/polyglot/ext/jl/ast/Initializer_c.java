package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.CodeDecl;
import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Initializer_c.class */
public class Initializer_c extends Term_c implements Initializer {
    protected Flags flags;
    protected Block body;
    protected InitializerInstance ii;

    public Initializer_c(Position pos, Flags flags, Block body) {
        super(pos);
        this.flags = flags;
        this.body = body;
    }

    @Override // polyglot.ast.Initializer
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.ast.Initializer
    public Initializer flags(Flags flags) {
        Initializer_c n = (Initializer_c) copy();
        n.flags = flags;
        return n;
    }

    @Override // polyglot.ast.Initializer
    public InitializerInstance initializerInstance() {
        return this.ii;
    }

    @Override // polyglot.ast.CodeDecl
    public CodeInstance codeInstance() {
        return initializerInstance();
    }

    @Override // polyglot.ast.Initializer
    public Initializer initializerInstance(InitializerInstance ii) {
        Initializer_c n = (Initializer_c) copy();
        n.ii = ii;
        return n;
    }

    @Override // polyglot.ast.CodeDecl
    public Block body() {
        return this.body;
    }

    @Override // polyglot.ast.CodeDecl
    public CodeDecl body(Block body) {
        Initializer_c n = (Initializer_c) copy();
        n.body = body;
        return n;
    }

    protected Initializer_c reconstruct(Block body) {
        if (body != this.body) {
            Initializer_c n = (Initializer_c) copy();
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Block body = (Block) visitChild(this.body, v);
        return reconstruct(body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushCode(this.ii);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        return tb.pushCode();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return body().entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(body(), this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        TypeSystem ts = tb.typeSystem();
        ClassType ct = tb.currentClass();
        InitializerInstance ii = ts.initializerInstance(position(), ct, this.flags);
        return initializerInstance(ii);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor addMembersEnter(AddMemberVisitor am) {
        return am.bypassChildren(this);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SUPER || ar.kind() == AmbiguityRemover.SIGNATURES) {
            return ar.bypassChildren(this);
        }
        return ar;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        try {
            ts.checkInitializerFlags(flags());
            if (flags().isStatic() && initializerInstance().container().toClass().isInnerClass()) {
                throw new SemanticException("Inner classes cannot declare static initializers.", position());
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
                if (initializerInstance().flags().isStatic()) {
                    throw new SemanticException(new StringBuffer().append("A static initializer block may not throw a ").append(t).append(".").toString(), ec.exceptionPosition(t));
                }
                if (!initializerInstance().container().toClass().isAnonymous()) {
                    throw new SemanticException(new StringBuffer().append("An instance initializer block may not throw a ").append(t).append(".").toString(), ec.exceptionPosition(t));
                }
            }
        }
        return super.exceptionCheck(ec);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(this.flags.translate());
        printBlock(this.body, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.ii != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(instance ").append(this.ii).append(")").toString());
            w.end();
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.flags.translate()).append("{ ... }").toString();
    }
}
