package polyglot.ext.jl.types;

import java.util.Collection;
import java.util.List;
import polyglot.main.Options;
import polyglot.types.ClassType;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.Named;
import polyglot.types.Package;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.Translator;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/ClassType_c.class */
public abstract class ClassType_c extends ReferenceType_c implements ClassType {
    @Override // polyglot.types.ClassType
    public abstract ClassType.Kind kind();

    @Override // polyglot.types.ClassType
    public abstract ClassType outer();

    @Override // polyglot.types.Named
    public abstract String name();

    @Override // polyglot.types.Importable
    public abstract Package package_();

    @Override // polyglot.types.MemberInstance
    public abstract Flags flags();

    @Override // polyglot.types.ClassType
    public abstract List constructors();

    @Override // polyglot.types.ClassType
    public abstract List memberClasses();

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public abstract List methods();

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public abstract List fields();

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public abstract List interfaces();

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.types.ReferenceType
    public abstract Type superType();

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassType_c() {
    }

    public ClassType_c(TypeSystem ts) {
        this(ts, null);
    }

    public ClassType_c(TypeSystem ts, Position pos) {
        super(ts, pos);
    }

    @Override // polyglot.types.MemberInstance
    public ReferenceType container() {
        if (!isMember()) {
            throw new InternalCompilerError("Non-member classes cannot have container classes.");
        }
        if (outer() == null) {
            throw new InternalCompilerError("Nested classes must have outer classes.");
        }
        return outer();
    }

    @Override // polyglot.types.Named
    public String fullName() {
        String name;
        if (isAnonymous()) {
            if (superType() != null) {
                name = new StringBuffer().append("<anon subtype of ").append(superType().toString()).append(">").toString();
            } else {
                name = "<anon subtype of unknown>";
            }
        } else {
            name = name();
        }
        if (isTopLevel() && package_() != null) {
            return new StringBuffer().append(package_().fullName()).append(".").append(name).toString();
        }
        if (isMember() && (container() instanceof Named)) {
            return new StringBuffer().append(((Named) container()).fullName()).append(".").append(name).toString();
        }
        return name;
    }

    @Override // polyglot.types.ClassType
    public boolean isTopLevel() {
        return kind() == ClassType.TOP_LEVEL;
    }

    @Override // polyglot.types.ClassType
    public boolean isMember() {
        return kind() == ClassType.MEMBER;
    }

    @Override // polyglot.types.ClassType
    public boolean isLocal() {
        return kind() == ClassType.LOCAL;
    }

    @Override // polyglot.types.ClassType
    public boolean isAnonymous() {
        return kind() == ClassType.ANONYMOUS;
    }

    @Override // polyglot.types.ClassType
    public final boolean isInner() {
        return isNested();
    }

    @Override // polyglot.types.ClassType
    public boolean isNested() {
        return kind() == ClassType.MEMBER || kind() == ClassType.LOCAL || kind() == ClassType.ANONYMOUS;
    }

    @Override // polyglot.types.ClassType
    public boolean isInnerClass() {
        return (flags().isInterface() || !isNested() || flags().isStatic() || inStaticContext()) ? false : true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isClass() {
        return true;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public ClassType toClass() {
        return this;
    }

    @Override // polyglot.types.ReferenceType
    public FieldInstance fieldNamed(String name) {
        for (FieldInstance fi : fields()) {
            if (fi.name().equals(name)) {
                return fi;
            }
        }
        return null;
    }

    @Override // polyglot.types.ClassType
    public ClassType memberClassNamed(String name) {
        for (ClassType t : memberClasses()) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        return null;
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean descendsFromImpl(Type ancestor) {
        if (!ancestor.isCanonical() || ancestor.isNull() || this.ts.equals(this, ancestor) || !ancestor.isReference()) {
            return false;
        }
        if (this.ts.equals(ancestor, this.ts.Object())) {
            return true;
        }
        if (!flags().isInterface()) {
            if (this.ts.equals(this, this.ts.Object()) || superType() == null) {
                return false;
            }
            if (this.ts.isSubtype(superType(), ancestor)) {
                return true;
            }
        }
        for (Type parentType : interfaces()) {
            if (this.ts.isSubtype(parentType, ancestor)) {
                return true;
            }
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isThrowable() {
        return this.ts.isSubtype(this, this.ts.Throwable());
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isUncheckedException() {
        if (isThrowable()) {
            Collection<Type> c = this.ts.uncheckedExceptions();
            for (Type t : c) {
                if (this.ts.isSubtype(this, t)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isImplicitCastValidImpl(Type toType) {
        if (toType.isClass()) {
            return this.ts.isSubtype(this, toType);
        }
        return false;
    }

    @Override // polyglot.ext.jl.types.ReferenceType_c, polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public boolean isCastValidImpl(Type toType) {
        if (toType.isCanonical() && toType.isReference()) {
            if (toType.isArray()) {
                return this.ts.isSubtype(toType, this);
            }
            if (toType.isClass()) {
                boolean fromInterface = flags().isInterface();
                boolean toInterface = toType.toClass().flags().isInterface();
                boolean fromFinal = flags().isFinal();
                boolean toFinal = toType.toClass().flags().isFinal();
                if (!fromInterface) {
                    if (!toInterface) {
                        return this.ts.isSubtype(this, toType) || this.ts.isSubtype(toType, this);
                    } else if (fromFinal) {
                        return this.ts.isSubtype(this, toType);
                    } else {
                        return true;
                    }
                } else if ((toInterface || toFinal) && toFinal) {
                    return this.ts.isSubtype(toType, this);
                } else {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // polyglot.types.ClassType
    public final boolean isEnclosed(ClassType maybe_outer) {
        return this.ts.isEnclosed(this, maybe_outer);
    }

    @Override // polyglot.types.ClassType
    public final boolean hasEnclosingInstance(ClassType encl) {
        return this.ts.hasEnclosingInstance(this, encl);
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String translate(Resolver c) {
        if (isTopLevel()) {
            if (package_() == null) {
                return name();
            }
            if (c != null) {
                try {
                    Named x = c.find(name());
                    if (this.ts.equals(this, x)) {
                        return name();
                    }
                } catch (SemanticException e) {
                }
            }
            if (Options.global.cppBackend()) {
                return Translator.cScope(new StringBuffer().append(package_().translate(c)).append(".").append(name()).toString());
            }
            return new StringBuffer().append(package_().translate(c)).append(".").append(name()).toString();
        } else if (isMember()) {
            if (container().toClass().isAnonymous()) {
                return name();
            }
            if (c != null) {
                try {
                    Named x2 = c.find(name());
                    if (this.ts.equals(this, x2)) {
                        return name();
                    }
                } catch (SemanticException e2) {
                }
            }
            return new StringBuffer().append(container().translate(c)).append(".").append(name()).toString();
        } else if (isLocal()) {
            return name();
        } else {
            throw new InternalCompilerError("Cannot translate an anonymous class.");
        }
    }

    @Override // polyglot.ext.jl.types.Type_c, polyglot.types.Type
    public String toString() {
        if (isTopLevel()) {
            if (package_() != null) {
                return new StringBuffer().append(package_().toString()).append(".").append(name()).toString();
            }
            return name();
        } else if (isMember()) {
            return new StringBuffer().append(container().toString()).append(".").append(name()).toString();
        } else {
            if (isLocal()) {
                return name();
            }
            if (superType() != null) {
                return new StringBuffer().append("<anon subtype of ").append(superType().toString()).append(">").toString();
            }
            return "<anon subtype of unknown>";
        }
    }

    @Override // polyglot.types.ClassType
    public boolean isEnclosedImpl(ClassType maybe_outer) {
        if (isTopLevel()) {
            return false;
        }
        if (outer() != null) {
            return outer().equals(maybe_outer) || outer().isEnclosed(maybe_outer);
        }
        throw new InternalCompilerError("Non top-level classes must have outer classes.");
    }

    @Override // polyglot.types.ClassType
    public boolean hasEnclosingInstanceImpl(ClassType encl) {
        if (equals(encl)) {
            return true;
        }
        if (!isInnerClass() || inStaticContext()) {
            return false;
        }
        return outer().hasEnclosingInstance(encl);
    }
}
