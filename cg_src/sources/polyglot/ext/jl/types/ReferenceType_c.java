package polyglot.ext.jl.types;

import java.util.LinkedList;
import java.util.List;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/ReferenceType_c.class */
public abstract class ReferenceType_c extends Type_c implements ReferenceType {
    public abstract List methods();

    public abstract List fields();

    public abstract Type superType();

    public abstract List interfaces();

    /* JADX INFO: Access modifiers changed from: protected */
    public ReferenceType_c() {
    }

    public ReferenceType_c(TypeSystem ts) {
        this(ts, null);
    }

    public ReferenceType_c(TypeSystem ts, Position pos) {
        super(ts, pos);
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isReference() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public ReferenceType toReference() {
        return this;
    }

    @Override // polyglot.types.ReferenceType
    public final boolean hasMethod(MethodInstance mi) {
        return this.ts.hasMethod(this, mi);
    }

    @Override // polyglot.types.ReferenceType
    public boolean hasMethodImpl(MethodInstance mi) {
        for (MethodInstance mj : methods()) {
            if (this.ts.isSameMethod(mi, mj)) {
                return true;
            }
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean descendsFromImpl(Type ancestor) {
        if (!ancestor.isCanonical() || ancestor.isNull() || this.ts.equals(this, ancestor) || !ancestor.isReference()) {
            return false;
        }
        if (this.ts.equals(ancestor, this.ts.Object())) {
            return true;
        }
        for (Type parentType : interfaces()) {
            if (this.ts.isSubtype(parentType, ancestor)) {
                return true;
            }
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isImplicitCastValidImpl(Type toType) {
        return this.ts.isSubtype(this, toType);
    }

    @Override // polyglot.types.ReferenceType
    public List methodsNamed(String name) {
        List l = new LinkedList();
        for (MethodInstance mi : methods()) {
            if (mi.name().equals(name)) {
                l.add(mi);
            }
        }
        return l;
    }

    @Override // polyglot.types.ReferenceType
    public List methods(String name, List argTypes) {
        List l = new LinkedList();
        for (MethodInstance mi : methodsNamed(name)) {
            if (mi.hasFormals(argTypes)) {
                l.add(mi);
            }
        }
        return l;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isCastValidImpl(Type toType) {
        if (toType.isReference()) {
            return this.ts.isSubtype(this, toType) || this.ts.isSubtype(toType, this);
        }
        return false;
    }
}
