package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/ConstructorInstance_c.class */
public class ConstructorInstance_c extends ProcedureInstance_c implements ConstructorInstance {
    protected ConstructorInstance_c() {
    }

    public ConstructorInstance_c(TypeSystem ts, Position pos, ClassType container, Flags flags, List formalTypes, List excTypes) {
        super(ts, pos, container, flags, formalTypes, excTypes);
    }

    @Override // polyglot.types.ConstructorInstance
    public ConstructorInstance flags(Flags flags) {
        if (!flags.equals(this.flags)) {
            ConstructorInstance_c n = (ConstructorInstance_c) copy();
            n.flags = flags;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.ConstructorInstance
    public ConstructorInstance formalTypes(List l) {
        if (!CollectionUtil.equals(this.formalTypes, l)) {
            ConstructorInstance_c n = (ConstructorInstance_c) copy();
            n.formalTypes = new ArrayList(l);
            return n;
        }
        return this;
    }

    @Override // polyglot.types.ConstructorInstance
    public ConstructorInstance throwTypes(List l) {
        if (!CollectionUtil.equals(this.excTypes, l)) {
            ConstructorInstance_c n = (ConstructorInstance_c) copy();
            n.excTypes = new ArrayList(l);
            return n;
        }
        return this;
    }

    @Override // polyglot.types.ConstructorInstance
    public ConstructorInstance container(ClassType container) {
        if (this.container != container) {
            ConstructorInstance_c n = (ConstructorInstance_c) copy();
            n.container = container;
            return n;
        }
        return this;
    }

    public String toString() {
        return new StringBuffer().append(designator()).append(Instruction.argsep).append(this.flags.translate()).append(signature()).toString();
    }

    @Override // polyglot.types.ProcedureInstance
    public String signature() {
        return new StringBuffer().append(this.container).append("(").append(TypeSystem_c.listToString(this.formalTypes)).append(")").toString();
    }

    @Override // polyglot.types.ProcedureInstance
    public String designator() {
        return "constructor";
    }

    @Override // polyglot.ext.jl.types.ProcedureInstance_c, polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof ConstructorInstance) {
            return super.equalsImpl(o);
        }
        return false;
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        return this.container.isCanonical() && listIsCanonical(this.formalTypes) && listIsCanonical(this.excTypes);
    }
}
