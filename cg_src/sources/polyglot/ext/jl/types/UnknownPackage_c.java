package polyglot.ext.jl.types;

import android.provider.MediaStore;
import polyglot.types.Resolver;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownPackage;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/UnknownPackage_c.class */
public class UnknownPackage_c extends Package_c implements UnknownPackage {
    protected UnknownPackage_c() {
    }

    public UnknownPackage_c(TypeSystem ts) {
        super(ts);
    }

    @Override // polyglot.ext.jl.types.Package_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return false;
    }

    @Override // polyglot.ext.jl.types.Package_c, polyglot.types.Package
    public String translate(Resolver c) {
        throw new InternalCompilerError("Cannot translate an unknown package.");
    }

    @Override // polyglot.ext.jl.types.Package_c
    public String toString() {
        return MediaStore.UNKNOWN_STRING;
    }
}
