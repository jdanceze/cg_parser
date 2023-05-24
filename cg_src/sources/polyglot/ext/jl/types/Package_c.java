package polyglot.ext.jl.types;

import polyglot.types.Package;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/Package_c.class */
public class Package_c extends TypeObject_c implements Package {
    protected Package prefix;
    protected String name;
    protected String fullname;

    /* JADX INFO: Access modifiers changed from: protected */
    public Package_c() {
        this.fullname = null;
    }

    public Package_c(TypeSystem ts) {
        this(ts, null, null);
    }

    public Package_c(TypeSystem ts, String name) {
        this(ts, null, name);
    }

    public Package_c(TypeSystem ts, Package prefix, String name) {
        super(ts);
        this.fullname = null;
        this.prefix = prefix;
        this.name = name;
    }

    @Override // polyglot.types.Qualifier
    public boolean isType() {
        return false;
    }

    @Override // polyglot.types.Qualifier
    public boolean isPackage() {
        return true;
    }

    @Override // polyglot.types.Qualifier
    public Type toType() {
        return null;
    }

    @Override // polyglot.types.Qualifier
    public Package toPackage() {
        return this;
    }

    @Override // polyglot.types.Package
    public Package prefix() {
        return this.prefix;
    }

    @Override // polyglot.types.Named
    public String name() {
        return this.name;
    }

    @Override // polyglot.types.Package
    public String translate(Resolver c) {
        if (prefix() == null) {
            return name();
        }
        return new StringBuffer().append(prefix().translate(c)).append(".").append(name()).toString();
    }

    @Override // polyglot.types.Named
    public String fullName() {
        if (this.fullname == null) {
            this.fullname = prefix() == null ? this.name : new StringBuffer().append(prefix().fullName()).append(".").append(this.name).toString();
        }
        return this.fullname;
    }

    public String toString() {
        return prefix() == null ? this.name : new StringBuffer().append(prefix().toString()).append(".").append(this.name).toString();
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        Package p;
        if (!(o instanceof Package) || (p = (Package) o) == null) {
            return false;
        }
        return prefix() == null ? p.prefix() == null && name().equals(p.name()) : prefix().equals(p.prefix()) && name().equals(p.name());
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        return true;
    }
}
