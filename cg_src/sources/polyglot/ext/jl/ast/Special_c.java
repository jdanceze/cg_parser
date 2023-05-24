package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Special_c.class */
public class Special_c extends Expr_c implements Special {
    protected Special.Kind kind;
    protected TypeNode qualifier;

    public Special_c(Position pos, Special.Kind kind, TypeNode qualifier) {
        super(pos);
        this.kind = kind;
        this.qualifier = qualifier;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ast.Special
    public Special.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ast.Special
    public Special kind(Special.Kind kind) {
        Special_c n = (Special_c) copy();
        n.kind = kind;
        return n;
    }

    @Override // polyglot.ast.Special
    public TypeNode qualifier() {
        return this.qualifier;
    }

    @Override // polyglot.ast.Special
    public Special qualifier(TypeNode qualifier) {
        Special_c n = (Special_c) copy();
        n.qualifier = qualifier;
        return n;
    }

    protected Special_c reconstruct(TypeNode qualifier) {
        if (qualifier != this.qualifier) {
            Special_c n = (Special_c) copy();
            n.qualifier = qualifier;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode qualifier = (TypeNode) visitChild(this.qualifier, v);
        return reconstruct(qualifier);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        ClassType t;
        TypeSystem ts = tc.typeSystem();
        Context c = tc.context();
        if (this.qualifier == null) {
            t = c.currentClass();
        } else if (!this.qualifier.type().isClass()) {
            throw new SemanticException(new StringBuffer().append("Qualified ").append(this.kind).append(" expression must be of a class type").toString(), this.qualifier.position());
        } else {
            t = this.qualifier.type().toClass();
            if (!c.currentClass().hasEnclosingInstance(t)) {
                throw new SemanticException(new StringBuffer().append("The nested class \"").append(c.currentClass()).append("\" does not have ").append("an enclosing instance of type \"").append(t).append("\".").toString(), this.qualifier.position());
            }
        }
        if (c.inStaticContext() && ts.equals(t, c.currentClass())) {
            throw new SemanticException("Cannot access a non-static field or method, or refer to \"this\" or \"super\" from a static context.", position());
        }
        if (this.kind == Special.THIS) {
            return type(t);
        }
        if (this.kind == Special.SUPER) {
            return type(t.superType());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.qualifier != null ? new StringBuffer().append(this.qualifier).append(".").toString() : "").append(this.kind).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.qualifier != null) {
            print(this.qualifier, w, tr);
            w.write(".");
        }
        w.write(this.kind.toString());
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.kind != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(kind ").append(this.kind).append(")").toString());
            w.end();
        }
    }
}
