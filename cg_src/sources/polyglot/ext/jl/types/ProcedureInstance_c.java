package polyglot.ext.jl.types;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.types.Flags;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.util.TypedList;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/ProcedureInstance_c.class */
public abstract class ProcedureInstance_c extends TypeObject_c implements ProcedureInstance {
    protected ReferenceType container;
    protected Flags flags;
    protected List formalTypes;
    protected List excTypes;
    static Class class$polyglot$types$Type;

    /* JADX INFO: Access modifiers changed from: protected */
    public ProcedureInstance_c() {
    }

    public ProcedureInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, List formalTypes, List excTypes) {
        super(ts, pos);
        Class cls;
        Class cls2;
        this.container = container;
        this.flags = flags;
        if (class$polyglot$types$Type == null) {
            cls = class$("polyglot.types.Type");
            class$polyglot$types$Type = cls;
        } else {
            cls = class$polyglot$types$Type;
        }
        this.formalTypes = TypedList.copyAndCheck(formalTypes, cls, true);
        if (class$polyglot$types$Type == null) {
            cls2 = class$("polyglot.types.Type");
            class$polyglot$types$Type = cls2;
        } else {
            cls2 = class$polyglot$types$Type;
        }
        this.excTypes = TypedList.copyAndCheck(excTypes, cls2, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.types.MemberInstance
    public ReferenceType container() {
        return this.container;
    }

    @Override // polyglot.types.MemberInstance
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.types.ProcedureInstance
    public List formalTypes() {
        return Collections.unmodifiableList(this.formalTypes);
    }

    @Override // polyglot.types.ProcedureInstance
    public List throwTypes() {
        return Collections.unmodifiableList(this.excTypes);
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.container.hashCode() + this.flags.hashCode();
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof ProcedureInstance) {
            ProcedureInstance i = (ProcedureInstance) o;
            return this.flags.equals(i.flags()) && this.ts.equals(this.container, i.container()) && this.ts.hasFormals(this, i.formalTypes());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean listIsCanonical(List l) {
        Iterator i = l.iterator();
        while (i.hasNext()) {
            TypeObject o = (TypeObject) i.next();
            if (!o.isCanonical()) {
                return false;
            }
        }
        return true;
    }

    @Override // polyglot.types.ProcedureInstance
    public final boolean moreSpecific(ProcedureInstance p) {
        return this.ts.moreSpecific(this, p);
    }

    @Override // polyglot.types.ProcedureInstance
    public boolean moreSpecificImpl(ProcedureInstance p) {
        ReferenceType t1 = container();
        ReferenceType t2 = p.container();
        if (t1.isClass() && t2.isClass()) {
            if (!t1.isSubtype(t2) && !t1.toClass().isEnclosed(t2.toClass())) {
                return false;
            }
        } else if (!t1.isSubtype(t2)) {
            return false;
        }
        return p.callValid(formalTypes());
    }

    @Override // polyglot.types.ProcedureInstance
    public final boolean hasFormals(List formalTypes) {
        return this.ts.hasFormals(this, formalTypes);
    }

    @Override // polyglot.types.ProcedureInstance
    public boolean hasFormalsImpl(List formalTypes) {
        List l1 = formalTypes();
        Iterator i1 = l1.iterator();
        Iterator i2 = formalTypes.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            Type t1 = (Type) i1.next();
            Type t2 = (Type) i2.next();
            if (!this.ts.equals(t1, t2)) {
                return false;
            }
        }
        return (i1.hasNext() || i2.hasNext()) ? false : true;
    }

    @Override // polyglot.types.ProcedureInstance
    public final boolean throwsSubset(ProcedureInstance p) {
        return this.ts.throwsSubset(this, p);
    }

    @Override // polyglot.types.ProcedureInstance
    public boolean throwsSubsetImpl(ProcedureInstance p) {
        SubtypeSet s1 = new SubtypeSet(this.ts.Throwable());
        SubtypeSet s2 = new SubtypeSet(this.ts.Throwable());
        s1.addAll(throwTypes());
        s2.addAll(p.throwTypes());
        Iterator i = s1.iterator();
        while (i.hasNext()) {
            Type t = (Type) i.next();
            if (!this.ts.isUncheckedException(t) && !s2.contains(t)) {
                return false;
            }
        }
        return true;
    }

    @Override // polyglot.types.ProcedureInstance
    public final boolean callValid(List argTypes) {
        return this.ts.callValid(this, argTypes);
    }

    @Override // polyglot.types.ProcedureInstance
    public boolean callValidImpl(List argTypes) {
        List l1 = formalTypes();
        Iterator i1 = l1.iterator();
        Iterator i2 = argTypes.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            Type t1 = (Type) i1.next();
            Type t2 = (Type) i2.next();
            if (!this.ts.isImplicitCastValid(t2, t1)) {
                return false;
            }
        }
        return (i1.hasNext() || i2.hasNext()) ? false : true;
    }
}
