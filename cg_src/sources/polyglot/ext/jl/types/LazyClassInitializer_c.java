package polyglot.ext.jl.types;

import polyglot.types.LazyClassInitializer;
import polyglot.types.ParsedClassType;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/LazyClassInitializer_c.class */
public class LazyClassInitializer_c implements LazyClassInitializer {
    TypeSystem ts;

    public LazyClassInitializer_c(TypeSystem ts) {
        this.ts = ts;
    }

    @Override // polyglot.types.LazyClassInitializer
    public boolean fromClassFile() {
        return false;
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initConstructors(ParsedClassType ct) {
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initMethods(ParsedClassType ct) {
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initFields(ParsedClassType ct) {
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initMemberClasses(ParsedClassType ct) {
    }

    @Override // polyglot.types.LazyClassInitializer
    public void initInterfaces(ParsedClassType ct) {
    }
}
