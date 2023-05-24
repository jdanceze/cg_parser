package polyglot.ext.param.types;

import java.util.Iterator;
import java.util.List;
import polyglot.ext.jl.types.ClassType_c;
import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/SubstClassType_c.class */
public class SubstClassType_c extends ClassType_c implements SubstType {
    protected ClassType base;
    protected Subst subst;

    public SubstClassType_c(ParamTypeSystem ts, Position pos, ClassType base, Subst subst) {
        super(ts, pos);
        this.base = base;
        this.subst = subst;
    }

    @Override // polyglot.ext.param.types.SubstType
    public Iterator entries() {
        return this.subst.entries();
    }

    @Override // polyglot.ext.param.types.SubstType
    public Type base() {
        return this.base;
    }

    @Override // polyglot.ext.param.types.SubstType
    public Subst subst() {
        return this.subst;
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public Type superType() {
        return this.subst.substType(this.base.superType());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List interfaces() {
        return this.subst.substTypeList(this.base.interfaces());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List fields() {
        return this.subst.substFieldList(this.base.fields());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public List methods() {
        return this.subst.substMethodList(this.base.methods());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public List constructors() {
        return this.subst.substConstructorList(this.base.constructors());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public List memberClasses() {
        return this.subst.substTypeList(this.base.memberClasses());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public ClassType outer() {
        return (ClassType) this.subst.substType(this.base.outer());
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.ClassType
    public ClassType.Kind kind() {
        return this.base.kind();
    }

    @Override // polyglot.types.ClassType
    public boolean inStaticContext() {
        return this.base.inStaticContext();
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.Named
    public String fullName() {
        return this.base.fullName();
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.Named
    public String name() {
        return this.base.name();
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.Importable
    public Package package_() {
        return this.base.package_();
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.types.MemberInstance
    public Flags flags() {
        return this.base.flags();
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String translate(Resolver c) {
        return this.base.translate(c);
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject t) {
        if (t instanceof SubstType) {
            SubstType x = (SubstType) t;
            return this.base.equals(x.base()) && this.subst.equals(x.subst());
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.base.hashCode() ^ this.subst.hashCode();
    }

    @Override // polyglot.ext.jl.types.ClassType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String toString() {
        return new StringBuffer().append(this.base.toString()).append(this.subst.toString()).toString();
    }
}
