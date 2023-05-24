package polyglot.ext.jl.ast;

import polyglot.ast.AmbReceiver;
import polyglot.ast.Node;
import polyglot.ast.Prefix;
import polyglot.ast.Receiver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.TypeBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AmbReceiver_c.class */
public class AmbReceiver_c extends AmbPrefix_c implements AmbReceiver {
    protected Type type;

    public AmbReceiver_c(Position pos, Prefix prefix, String name) {
        super(pos, prefix, name);
    }

    @Override // polyglot.ast.Typed
    public Type type() {
        return this.type;
    }

    public AmbReceiver type(Type type) {
        AmbReceiver_c n = (AmbReceiver_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        return type(tb.typeSystem().unknownType(position()));
    }

    @Override // polyglot.ext.jl.ast.AmbPrefix_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        Node n = super.disambiguate(ar);
        if (n instanceof Receiver) {
            return n;
        }
        throw new SemanticException(new StringBuffer().append("Could not find type, field, or local variable \"").append(this.prefix == null ? this.name : new StringBuffer().append(this.prefix.toString()).append(".").append(this.name).toString()).append("\".").toString(), position());
    }
}
