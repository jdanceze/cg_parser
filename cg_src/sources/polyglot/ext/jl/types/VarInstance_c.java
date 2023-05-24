package polyglot.ext.jl.types;

import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/VarInstance_c.class */
public abstract class VarInstance_c extends TypeObject_c implements VarInstance {
    protected Flags flags;
    protected Type type;
    protected String name;
    protected Object constantValue;
    protected boolean isConstant;

    /* JADX INFO: Access modifiers changed from: protected */
    public VarInstance_c() {
    }

    public VarInstance_c(TypeSystem ts, Position pos, Flags flags, Type type, String name) {
        super(ts, pos);
        this.flags = flags;
        this.type = type;
        this.name = name;
    }

    @Override // polyglot.types.VarInstance
    public boolean isConstant() {
        return this.isConstant;
    }

    @Override // polyglot.types.VarInstance
    public Object constantValue() {
        return this.constantValue;
    }

    @Override // polyglot.types.VarInstance
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.types.VarInstance
    public Type type() {
        return this.type;
    }

    @Override // polyglot.types.VarInstance
    public String name() {
        return this.name;
    }

    @Override // polyglot.ext.jl.types.TypeObject_c
    public int hashCode() {
        return this.flags.hashCode() + this.name.hashCode();
    }

    @Override // polyglot.ext.jl.types.TypeObject_c, polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject o) {
        if (o instanceof VarInstance) {
            VarInstance i = (VarInstance) o;
            return this.flags.equals(i.flags()) && this.ts.equals(this.type, i.type()) && this.name.equals(i.name());
        }
        return false;
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        return true;
    }

    @Override // polyglot.types.VarInstance
    public void setType(Type type) {
        this.type = type;
    }

    @Override // polyglot.types.VarInstance
    public void setFlags(Flags flags) {
        this.flags = flags;
    }
}
