package polyglot.ext.jl.types;

import polyglot.types.PlaceHolder;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/PlaceHolder_c.class */
public class PlaceHolder_c implements PlaceHolder {
    String name;

    protected PlaceHolder_c() {
    }

    public PlaceHolder_c(Type t) {
        if (t.isClass()) {
            this.name = t.typeSystem().getTransformedClassName(t.toClass());
            return;
        }
        throw new InternalCompilerError(new StringBuffer().append("Cannot serialize ").append(t).append(".").toString());
    }

    @Override // polyglot.types.PlaceHolder
    public TypeObject resolve(TypeSystem ts) {
        try {
            return ts.systemResolver().find(this.name);
        } catch (SemanticException se) {
            throw new InternalCompilerError(se);
        }
    }

    public String translate(Resolver c) {
        throw new InternalCompilerError("Cannot translate place holder type.");
    }

    public String toString() {
        return new StringBuffer().append("PlaceHolder(").append(this.name).append(")").toString();
    }
}
