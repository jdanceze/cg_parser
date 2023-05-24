package polyglot.ext.jl.ast;

import polyglot.ast.Node;
import polyglot.ast.TypeNode;
import polyglot.types.Qualifier;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/TypeNode_c.class */
public abstract class TypeNode_c extends Node_c implements TypeNode {
    protected Type type;

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public abstract void prettyPrint(CodeWriter codeWriter, PrettyPrinter prettyPrinter);

    public TypeNode_c(Position pos) {
        super(pos);
    }

    @Override // polyglot.ast.QualifierNode
    public Qualifier qualifier() {
        return type();
    }

    @Override // polyglot.ast.Typed
    public Type type() {
        return this.type;
    }

    @Override // polyglot.ast.TypeNode
    public TypeNode type(Type type) {
        TypeNode_c n = (TypeNode_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        if (this.type == null) {
            TypeSystem ts = tb.typeSystem();
            return type(ts.unknownType(position()));
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        if (this.type != null) {
            return this.type.toString();
        }
        return "<unknown type>";
    }
}
