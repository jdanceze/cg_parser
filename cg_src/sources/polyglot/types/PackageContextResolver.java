package polyglot.types;

import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/PackageContextResolver.class */
public class PackageContextResolver implements Resolver {
    protected Package p;
    protected TypeSystem ts;
    protected Resolver cr;

    public PackageContextResolver(TypeSystem ts, Package p, Resolver cr) {
        this.ts = ts;
        this.p = p;
        this.cr = cr;
    }

    public Package package_() {
        return this.p;
    }

    public Resolver outer() {
        return this.cr;
    }

    @Override // polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (!StringUtil.isNameShort(name)) {
            throw new InternalCompilerError(new StringBuffer().append("Cannot lookup qualified name ").append(name).toString());
        }
        if (this.cr == null) {
            return this.ts.createPackage(this.p, name);
        }
        try {
            return this.cr.find(new StringBuffer().append(this.p.fullName()).append(".").append(name).toString());
        } catch (NoClassException e) {
            if (!e.getClassName().equals(new StringBuffer().append(this.p.fullName()).append(".").append(name).toString())) {
                throw e;
            }
            return this.ts.createPackage(this.p, name);
        }
    }

    public String toString() {
        return new StringBuffer().append("(package-context ").append(this.p.toString()).append(")").toString();
    }
}
