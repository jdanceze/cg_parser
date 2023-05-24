package polyglot.ext.jl.types;

import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/FieldInstance_c.class */
public class FieldInstance_c extends VarInstance_c implements FieldInstance {
    protected ReferenceType container;

    protected FieldInstance_c() {
    }

    public FieldInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, Type type, String name) {
        super(ts, pos, flags, type, name);
        this.container = container;
    }

    @Override // polyglot.types.MemberInstance
    public ReferenceType container() {
        return this.container;
    }

    @Override // polyglot.types.FieldInstance
    public void setConstantValue(Object constantValue) {
        if (constantValue != null && !(constantValue instanceof Boolean) && !(constantValue instanceof Number) && !(constantValue instanceof Character) && !(constantValue instanceof String)) {
            throw new InternalCompilerError("Can only set constant value to a primitive or String.");
        }
        this.constantValue = constantValue;
        this.isConstant = true;
    }

    @Override // polyglot.types.FieldInstance
    public FieldInstance constantValue(Object constantValue) {
        if (this.constantValue != constantValue) {
            FieldInstance_c n = (FieldInstance_c) copy();
            n.setConstantValue(constantValue);
            return n;
        }
        return this;
    }

    @Override // polyglot.types.FieldInstance
    public FieldInstance container(ReferenceType container) {
        if (this.container != container) {
            FieldInstance_c n = (FieldInstance_c) copy();
            n.container = container;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.FieldInstance
    public FieldInstance flags(Flags flags) {
        if (!flags.equals(this.flags)) {
            FieldInstance_c n = (FieldInstance_c) copy();
            n.flags = flags;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.FieldInstance
    public FieldInstance name(String name) {
        if ((name != null && !name.equals(this.name)) || (name == null && name != this.name)) {
            FieldInstance_c n = (FieldInstance_c) copy();
            n.name = name;
            return n;
        }
        return this;
    }

    @Override // polyglot.types.FieldInstance
    public FieldInstance type(Type type) {
        if (this.type != type) {
            FieldInstance_c n = (FieldInstance_c) copy();
            n.type = type;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.types.VarInstance_c, polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof FieldInstance) {
            FieldInstance i = (FieldInstance) o;
            return super.equalsImpl((TypeObject) i) && this.ts.equals(this.container, i.container());
        }
        return false;
    }

    public String toString() {
        Object v = this.constantValue;
        if (v instanceof String) {
            String s = (String) v;
            if (s.length() > 8) {
                s = new StringBuffer().append(s.substring(0, 8)).append("...").toString();
            }
            v = new StringBuffer().append("\"").append(s).append("\"").toString();
        }
        return new StringBuffer().append("field ").append(this.flags.translate()).append(this.type).append(Instruction.argsep).append(this.name).append(v != null ? new StringBuffer().append(" = ").append(v).toString() : "").toString();
    }

    @Override // polyglot.ext.jl.types.VarInstance_c, polyglot.types.TypeObject
    public boolean isCanonical() {
        return this.container.isCanonical() && this.type.isCanonical();
    }
}
