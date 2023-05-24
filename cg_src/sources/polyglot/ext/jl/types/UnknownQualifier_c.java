package polyglot.ext.jl.types;

import android.provider.MediaStore;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownQualifier;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/UnknownQualifier_c.class */
public class UnknownQualifier_c extends TypeObject_c implements UnknownQualifier {
    public UnknownQualifier_c(TypeSystem ts) {
        super(ts);
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        return false;
    }

    @Override // polyglot.types.Qualifier
    public boolean isPackage() {
        return false;
    }

    @Override // polyglot.types.Qualifier
    public boolean isType() {
        return false;
    }

    @Override // polyglot.types.Qualifier
    public Package toPackage() {
        return null;
    }

    @Override // polyglot.types.Qualifier
    public Type toType() {
        return null;
    }

    public String toString() {
        return MediaStore.UNKNOWN_STRING;
    }
}
