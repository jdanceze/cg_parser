package polyglot.ext.jl.types;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/LocalInstance_c.class */
public class LocalInstance_c extends VarInstance_c implements LocalInstance {
    protected LocalInstance_c() {
    }

    public LocalInstance_c(TypeSystem ts, Position pos, Flags flags, Type type, String name) {
        super(ts, pos, flags, type, name);
    }

    @Override // polyglot.types.LocalInstance
    public void setConstantValue(Object constantValue) {
        if (constantValue != null && !(constantValue instanceof Boolean) && !(constantValue instanceof Number) && !(constantValue instanceof Character) && !(constantValue instanceof String)) {
            throw new InternalCompilerError("Can only set constant value to a primitive or String.");
        }
        this.constantValue = constantValue;
        this.isConstant = true;
    }

    @Override // polyglot.types.LocalInstance
    public LocalInstance constantValue(Object constantValue) {
        if (this.constantValue != constantValue) {
            LocalInstance_c n = (LocalInstance_c) copy();
            n.setConstantValue(constantValue);
            return n;
        }
        return this;
    }

    @Override // polyglot.types.LocalInstance
    public LocalInstance flags(Flags flags) {
        if (!flags.equals(this.flags)) {
            LocalInstance_c n = (LocalInstance_c) copy();
            n.flags = flags;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.LocalInstance
    public LocalInstance name(String name) {
        if ((name != null && !name.equals(this.name)) || (name == null && name != this.name)) {
            LocalInstance_c n = (LocalInstance_c) copy();
            n.name = name;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.LocalInstance
    public LocalInstance type(Type type) {
        if (this.type != type) {
            LocalInstance_c n = (LocalInstance_c) copy();
            n.type = type;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.types.VarInstance_c, polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof LocalInstance) {
            LocalInstance i = (LocalInstance) o;
            return super.equalsImpl((TypeObject) i);
        }
        return false;
    }

    public String toString() {
        return new StringBuffer().append("local ").append(this.flags.translate()).append(this.type).append(Instruction.argsep).append(this.name).append(this.constantValue != null ? new StringBuffer().append(" = ").append(this.constantValue).toString() : "").toString();
    }

    @Override // polyglot.ext.jl.types.VarInstance_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return this.type.isCanonical();
    }
}
