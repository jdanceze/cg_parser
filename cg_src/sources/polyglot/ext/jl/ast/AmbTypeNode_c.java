package polyglot.ext.jl.ast;

import polyglot.ast.AmbTypeNode;
import polyglot.ast.Node;
import polyglot.ast.QualifierNode;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AmbTypeNode_c.class */
public class AmbTypeNode_c extends TypeNode_c implements AmbTypeNode {
    protected QualifierNode qual;
    protected String name;

    public AmbTypeNode_c(Position pos, QualifierNode qual, String name) {
        super(pos);
        this.qual = qual;
        this.name = name;
    }

    @Override // polyglot.ast.AmbTypeNode
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.AmbTypeNode
    public AmbTypeNode name(String name) {
        AmbTypeNode_c n = (AmbTypeNode_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.AmbTypeNode
    public QualifierNode qual() {
        return this.qual;
    }

    @Override // polyglot.ast.AmbTypeNode
    public AmbTypeNode qual(QualifierNode qual) {
        AmbTypeNode_c n = (AmbTypeNode_c) copy();
        n.qual = qual;
        return n;
    }

    protected AmbTypeNode_c reconstruct(QualifierNode qual) {
        if (qual != this.qual) {
            AmbTypeNode_c n = (AmbTypeNode_c) copy();
            n.qual = qual;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        return type(tb.typeSystem().unknownType(position()));
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        QualifierNode qual = (QualifierNode) visitChild(this.qual, v);
        return reconstruct(qual);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover sc) throws SemanticException {
        Node n = sc.nodeFactory().disamb().disambiguate(this, sc, position(), this.qual, this.name);
        if (n instanceof TypeNode) {
            return n;
        }
        throw new SemanticException(new StringBuffer().append("Could not find type \"").append(this.qual == null ? this.name : new StringBuffer().append(this.qual.toString()).append(".").append(this.name).toString()).append("\".").toString(), position());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot type check ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot exception check ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.qual != null) {
            print(this.qual, w, tr);
            w.write(".");
        }
        w.write(this.name);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot translate ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.qual == null ? this.name : new StringBuffer().append(this.qual.toString()).append(".").append(this.name).toString()).append("{amb}").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name \"").append(this.name).append("\")").toString());
        w.end();
    }
}
