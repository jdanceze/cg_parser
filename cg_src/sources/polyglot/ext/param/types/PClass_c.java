package polyglot.ext.param.types;

import java.util.List;
import polyglot.ext.jl.types.TypeObject_c;
import polyglot.types.ClassType;
import polyglot.types.Package;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/PClass_c.class */
public abstract class PClass_c extends TypeObject_c implements PClass {
    /* JADX INFO: Access modifiers changed from: protected */
    public PClass_c() {
    }

    public PClass_c(TypeSystem ts) {
        this(ts, null);
    }

    public PClass_c(TypeSystem ts, Position pos) {
        super(ts, pos);
    }

    @Override // polyglot.ext.param.types.PClass
    public ClassType instantiate(Position pos, List actuals) throws SemanticException {
        ParamTypeSystem pts = (ParamTypeSystem) typeSystem();
        return pts.instantiate(pos, this, actuals);
    }

    @Override // polyglot.types.TypeObject
    public boolean isCanonical() {
        if (!clazz().isCanonical()) {
            return false;
        }
        for (Param p : formals()) {
            if (!p.isCanonical()) {
                return false;
            }
        }
        return true;
    }

    @Override // polyglot.types.Named
    public String name() {
        return clazz().name();
    }

    @Override // polyglot.types.Named
    public String fullName() {
        return clazz().fullName();
    }

    @Override // polyglot.types.Importable
    public Package package_() {
        return clazz().package_();
    }
}
