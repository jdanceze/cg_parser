package polyglot.ext.jl.ast;

import polyglot.ast.AmbPrefix;
import polyglot.ast.Node;
import polyglot.ast.Prefix;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AmbPrefix_c.class */
public class AmbPrefix_c extends Node_c implements AmbPrefix {
    protected Prefix prefix;
    protected String name;

    public AmbPrefix_c(Position pos, Prefix prefix, String name) {
        super(pos);
        this.prefix = prefix;
        this.name = name;
    }

    @Override // polyglot.ast.AmbPrefix
    public String name() {
        return this.name;
    }

    public AmbPrefix name(String name) {
        AmbPrefix_c n = (AmbPrefix_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.AmbPrefix
    public Prefix prefix() {
        return this.prefix;
    }

    public AmbPrefix prefix(Prefix prefix) {
        AmbPrefix_c n = (AmbPrefix_c) copy();
        n.prefix = prefix;
        return n;
    }

    protected AmbPrefix_c reconstruct(Prefix prefix) {
        if (prefix != this.prefix) {
            AmbPrefix_c n = (AmbPrefix_c) copy();
            n.prefix = prefix;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Prefix prefix = (Prefix) visitChild(this.prefix, v);
        return reconstruct(prefix);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        return ar.nodeFactory().disamb().disambiguate(this, ar, position(), this.prefix, this.name);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot type check ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot exception check ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.prefix != null) {
            print(this.prefix, w, tr);
            w.write(".");
        }
        w.write(this.name);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot translate ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.prefix == null ? this.name : new StringBuffer().append(this.prefix.toString()).append(".").append(this.name).toString()).append("{amb}").toString();
    }
}
