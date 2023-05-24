package polyglot.ext.jl.types;

import polyglot.types.NullType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/NullType_c.class */
public class NullType_c extends Type_c implements NullType {
    protected NullType_c() {
    }

    public NullType_c(TypeSystem ts) {
        super(ts);
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String translate(Resolver c) {
        throw new InternalCompilerError("Cannot translate a null type.");
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String toString() {
        return "type(null)";
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject t) {
        return t instanceof NullType;
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return 6060842;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isNull() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public NullType toNull() {
        return this;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean descendsFromImpl(Type ancestor) {
        return !ancestor.isNull() && ancestor.isReference();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isImplicitCastValidImpl(Type toType) {
        return toType.isNull() || toType.isReference();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isCastValidImpl(Type toType) {
        return toType.isNull() || toType.isReference();
    }
}
