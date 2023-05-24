package polyglot.ext.jl.types;

import android.provider.MediaStore;
import polyglot.types.Resolver;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownType;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/UnknownType_c.class */
public class UnknownType_c extends Type_c implements UnknownType {
    protected UnknownType_c() {
    }

    public UnknownType_c(TypeSystem ts) {
        super(ts);
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return false;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String translate(Resolver c) {
        throw new InternalCompilerError("Cannot translate an unknown type.");
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String toString() {
        return MediaStore.UNKNOWN_STRING;
    }
}
