package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.ClassDecl;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/LocalClassDecl_c.class */
public class LocalClassDecl_c extends Stmt_c implements LocalClassDecl {
    protected ClassDecl decl;

    public LocalClassDecl_c(Position pos, ClassDecl decl) {
        super(pos);
        this.decl = decl;
    }

    @Override // polyglot.ast.LocalClassDecl
    public ClassDecl decl() {
        return this.decl;
    }

    public LocalClassDecl decl(ClassDecl decl) {
        LocalClassDecl_c n = (LocalClassDecl_c) copy();
        n.decl = decl;
        return n;
    }

    protected LocalClassDecl_c reconstruct(ClassDecl decl) {
        if (decl != this.decl) {
            LocalClassDecl_c n = (LocalClassDecl_c) copy();
            n.decl = decl;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return decl().entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(decl(), this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        ClassDecl decl = (ClassDecl) visitChild(this.decl, v);
        return reconstruct(decl);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void addDecls(Context c) {
        if (!this.decl.type().toClass().isLocal()) {
            throw new InternalCompilerError(new StringBuffer().append("Non-local ").append(this.decl.type()).append(" found in method body.").toString());
        }
        c.addNamed(this.decl.type().toClass());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        return ar.bypassChildren(this);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.ALL) {
            Job sj = ar.job().spawn(ar.context(), this.decl, Pass.CLEAN_SUPER, Pass.ADD_MEMBERS_ALL);
            if (!sj.status()) {
                if (!sj.reportedErrors()) {
                    throw new SemanticException(new StringBuffer().append("Could not disambiguate local class \"").append(this.decl.name()).append("\".").toString(), position());
                }
                throw new SemanticException();
            }
            ClassDecl d = (ClassDecl) sj.ast();
            LocalClassDecl n = decl(d);
            return n.visitChildren(ar);
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return this.decl.toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        printBlock(this.decl, w, tr);
        w.write(";");
    }
}
