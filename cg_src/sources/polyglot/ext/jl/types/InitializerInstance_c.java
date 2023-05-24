package polyglot.ext.jl.types;

import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.ReferenceType;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/InitializerInstance_c.class */
public class InitializerInstance_c extends TypeObject_c implements InitializerInstance {
    protected ClassType container;
    protected Flags flags;

    protected InitializerInstance_c() {
    }

    public InitializerInstance_c(TypeSystem ts, Position pos, ClassType container, Flags flags) {
        super(ts, pos);
        this.container = container;
        this.flags = flags;
    }

    @Override // polyglot.types.MemberInstance
    public ReferenceType container() {
        return this.container;
    }

    @Override // polyglot.types.InitializerInstance
    public InitializerInstance container(ClassType container) {
        if (this.container != container) {
            InitializerInstance_c n = (InitializerInstance_c) copy();
            n.container = container;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.MemberInstance
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.types.InitializerInstance
    public InitializerInstance flags(Flags flags) {
        if (!flags.equals(this.flags)) {
            InitializerInstance_c n = (InitializerInstance_c) copy();
            n.flags = flags;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.container.hashCode() + this.flags.hashCode();
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof InitializerInstance) {
            InitializerInstance i = (InitializerInstance) o;
            return this.flags.equals(i.flags()) && this.ts.equals(this.container, i.container());
        }
        return false;
    }

    public String toString() {
        return new StringBuffer().append(this.flags.translate()).append("initializer").toString();
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        return true;
    }
}
