package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import polyglot.main.Options;
import polyglot.types.ArrayType;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/ArrayType_c.class */
public class ArrayType_c extends ReferenceType_c implements ArrayType {
    protected Type base;
    protected List fields;
    protected List methods;
    protected List interfaces;

    protected ArrayType_c() {
    }

    public ArrayType_c(TypeSystem ts, Position pos, Type base) {
        super(ts, pos);
        this.base = base;
        this.methods = null;
        this.fields = null;
        this.interfaces = null;
    }

    void init() {
        if (this.methods == null) {
            this.methods = new ArrayList(1);
            this.methods.add(this.ts.methodInstance(position(), this, this.ts.Public(), this.ts.Object(), "clone", Collections.EMPTY_LIST, Collections.EMPTY_LIST));
        }
        if (this.fields == null) {
            this.fields = new ArrayList(2);
            this.fields.add(this.ts.fieldInstance(position(), this, this.ts.Public().Final(), this.ts.Int(), XMLConstants.LENGTH_ATTRIBUTE));
        }
        if (this.interfaces == null) {
            this.interfaces = new ArrayList(2);
            this.interfaces.add(this.ts.Cloneable());
            this.interfaces.add(this.ts.Serializable());
        }
    }

    @Override // polyglot.types.ArrayType
    public Type base() {
        return this.base;
    }

    @Override // polyglot.types.ArrayType
    public ArrayType base(Type base) {
        if (base == this.base) {
            return this;
        }
        ArrayType_c n = (ArrayType_c) copy();
        n.base = base;
        return n;
    }

    @Override // polyglot.types.ArrayType
    public Type ultimateBase() {
        if (base().isArray()) {
            return base().toArray().ultimateBase();
        }
        return base();
    }

    @Override // polyglot.types.ArrayType
    public int dims() {
        return 1 + (base().isArray() ? base().toArray().dims() : 0);
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String toString() {
        return new StringBuffer().append(base().toString()).append("[]").toString();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String translate(Resolver c) {
        if (Options.global.cppBackend()) {
            String s = new StringBuffer().append("jmatch_array< ").append(base().translate(c)).toString();
            if (base().isReference()) {
                s = new StringBuffer().append(s).append("*").toString();
            }
            return new StringBuffer().append(s).append(" > ").toString();
        }
        return new StringBuffer().append(base().translate(c)).append("[]").toString();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return base().isCanonical();
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isArray() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public ArrayType toArray() {
        return this;
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List methods() {
        init();
        return Collections.unmodifiableList(this.methods);
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List fields() {
        init();
        return Collections.unmodifiableList(this.fields);
    }

    @Override // polyglot.types.ArrayType
    public MethodInstance cloneMethod() {
        return (MethodInstance) methods().get(0);
    }

    @Override // polyglot.types.ReferenceType
    public FieldInstance fieldNamed(String name) {
        FieldInstance fi = lengthField();
        if (name.equals(fi.name())) {
            return fi;
        }
        return null;
    }

    @Override // polyglot.types.ArrayType
    public FieldInstance lengthField() {
        return (FieldInstance) fields().get(0);
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public Type superType() {
        return this.ts.Object();
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List interfaces() {
        init();
        return Collections.unmodifiableList(this.interfaces);
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return base().hashCode() << 1;
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject t) {
        if (t instanceof ArrayType) {
            ArrayType a = (ArrayType) t;
            return this.ts.equals(base(), a.base());
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isImplicitCastValidImpl(Type toType) {
        if (toType.isArray()) {
            if (base().isPrimitive() || toType.toArray().base().isPrimitive()) {
                return this.ts.equals(base(), toType.toArray().base());
            }
            return this.ts.isImplicitCastValid(base(), toType.toArray().base());
        }
        return this.ts.isSubtype(this, toType);
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isCastValidImpl(Type toType) {
        if (toType.isReference()) {
            if (toType.isArray()) {
                Type fromBase = base();
                Type toBase = toType.toArray().base();
                if (fromBase.isPrimitive()) {
                    return this.ts.equals(toBase, fromBase);
                }
                if (toBase.isPrimitive() || fromBase.isNull() || toBase.isNull()) {
                    return false;
                }
                return this.ts.isCastValid(fromBase, toBase);
            }
            return this.ts.isSubtype(this, toType);
        }
        return false;
    }
}
