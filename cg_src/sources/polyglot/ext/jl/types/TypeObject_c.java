package polyglot.ext.jl.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypeInputStream;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/types/TypeObject_c.class */
public abstract class TypeObject_c implements TypeObject {
    protected transient TypeSystem ts;
    protected Position position;

    /* JADX INFO: Access modifiers changed from: protected */
    public TypeObject_c() {
    }

    public TypeObject_c(TypeSystem ts) {
        this(ts, null);
    }

    public TypeObject_c(TypeSystem ts, Position pos) {
        this.ts = ts;
        this.position = pos;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            return (TypeObject_c) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }

    @Override // polyglot.types.TypeObject
    public TypeSystem typeSystem() {
        return this.ts;
    }

    @Override // polyglot.types.TypeObject
    public Position position() {
        return this.position;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (in instanceof TypeInputStream) {
            this.ts = ((TypeInputStream) in).getTypeSystem();
        }
        in.defaultReadObject();
    }

    public final boolean equals(Object o) {
        return (o instanceof TypeObject) && this.ts.equals(this, (TypeObject) o);
    }

    public int hashCode() {
        return super.hashCode();
    }

    @Override // polyglot.types.TypeObject
    public boolean equalsImpl(TypeObject t) {
        return t == this;
    }

    void equalsImpl(Object o) {
    }
}
