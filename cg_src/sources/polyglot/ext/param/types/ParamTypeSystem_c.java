package polyglot.ext.param.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import polyglot.ext.jl.types.TypeSystem_c;
import polyglot.types.ClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/ParamTypeSystem_c.class */
public abstract class ParamTypeSystem_c extends TypeSystem_c implements ParamTypeSystem {
    @Override // polyglot.ext.param.types.ParamTypeSystem
    public MuPClass mutablePClass(Position pos) {
        return new MuPClass_c(this, pos);
    }

    @Override // polyglot.ext.param.types.ParamTypeSystem
    public ClassType instantiate(Position pos, PClass base, List actuals) throws SemanticException {
        checkInstantiation(pos, base, actuals);
        return uncheckedInstantiate(pos, base, actuals);
    }

    protected void checkInstantiation(Position pos, PClass base, List actuals) throws SemanticException {
        if (base.formals().size() != actuals.size()) {
            throw new SemanticException(new StringBuffer().append("Wrong number of actual parameters for instantiation of \"").append(base).append("\".").toString(), pos);
        }
    }

    protected ClassType uncheckedInstantiate(Position pos, PClass base, List actuals) {
        Map substMap = new HashMap();
        Iterator i = base.formals().iterator();
        Iterator j = actuals.iterator();
        while (i.hasNext() && j.hasNext()) {
            Object formal = i.next();
            Object actual = j.next();
            substMap.put(formal, actual);
        }
        if (i.hasNext() || j.hasNext()) {
            throw new InternalCompilerError(new StringBuffer().append("Wrong number of actual parameters for instantiation of \"").append(base).append("\".").toString(), pos);
        }
        Type inst = subst(base.clazz(), substMap, new HashMap());
        if (!inst.isClass()) {
            throw new InternalCompilerError("Instantiating a PClass produced something other than a ClassType.", pos);
        }
        return inst.toClass();
    }

    @Override // polyglot.ext.param.types.ParamTypeSystem
    public Type subst(Type t, Map substMap) {
        return subst(t, substMap, new HashMap());
    }

    @Override // polyglot.ext.param.types.ParamTypeSystem
    public Type subst(Type t, Map substMap, Map cache) {
        return subst(substMap, cache).substType(t);
    }

    @Override // polyglot.ext.param.types.ParamTypeSystem
    public Subst subst(Map substMap, Map cache) {
        return new Subst_c(this, substMap, cache);
    }
}
