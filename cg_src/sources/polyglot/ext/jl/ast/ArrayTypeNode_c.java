package polyglot.ext.jl.ast;

import polyglot.ast.ArrayTypeNode;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
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
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ArrayTypeNode_c.class */
public class ArrayTypeNode_c extends TypeNode_c implements ArrayTypeNode {
    protected TypeNode base;

    public ArrayTypeNode_c(Position pos, TypeNode base) {
        super(pos);
        this.base = base;
    }

    @Override // polyglot.ast.ArrayTypeNode
    public TypeNode base() {
        return this.base;
    }

    @Override // polyglot.ast.ArrayTypeNode
    public ArrayTypeNode base(TypeNode base) {
        ArrayTypeNode_c n = (ArrayTypeNode_c) copy();
        n.base = base;
        return n;
    }

    protected ArrayTypeNode_c reconstruct(TypeNode base) {
        if (base != this.base) {
            ArrayTypeNode_c n = (ArrayTypeNode_c) copy();
            n.base = base;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode base = (TypeNode) visitChild(this.base, v);
        return reconstruct(base);
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        TypeSystem ts = tb.typeSystem();
        return type(ts.arrayOf(position(), this.base.type()));
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        TypeSystem ts = ar.typeSystem();
        NodeFactory nf = ar.nodeFactory();
        Type baseType = this.base.type();
        if (!baseType.isCanonical()) {
            throw new SemanticException(new StringBuffer().append("Base type ").append(baseType).append(" of array could not be resolved.").toString(), this.base.position());
        }
        return nf.CanonicalTypeNode(position(), ts.arrayOf(position(), baseType));
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
        print(this.base, w, tr);
        w.write("[]");
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot translate ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.TypeNode_c, polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.base.toString()).append("[]").toString();
    }
}
