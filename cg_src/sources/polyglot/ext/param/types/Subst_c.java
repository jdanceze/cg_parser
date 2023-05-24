package polyglot.ext.param.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import polyglot.ext.param.Topics;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.util.CachingTransformingList;
import polyglot.util.InternalCompilerError;
import polyglot.util.Transformation;
import polyglot.util.TypeInputStream;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/Subst_c.class */
public class Subst_c implements Subst {
    protected Map subst;
    protected transient Map cache = new HashMap();
    protected transient ParamTypeSystem ts;

    public Subst_c(ParamTypeSystem ts, Map subst, Map cache) {
        this.ts = ts;
        this.subst = subst;
        this.cache.putAll(cache);
    }

    @Override // polyglot.ext.param.types.Subst
    public ParamTypeSystem typeSystem() {
        return this.ts;
    }

    @Override // polyglot.ext.param.types.Subst
    public Iterator entries() {
        return substitutions().entrySet().iterator();
    }

    @Override // polyglot.ext.param.types.Subst
    public Map substitutions() {
        return Collections.unmodifiableMap(this.subst);
    }

    protected Type uncachedSubstType(Type t) {
        if (t.isArray()) {
            ArrayType at = t.toArray();
            return at.base(substType(at.base()));
        } else if (t instanceof SubstType) {
            Type tbase = ((SubstType) t).base();
            Map tsubst = ((SubstType) t).subst().substitutions();
            Map newSubst = new HashMap();
            for (Map.Entry e : tsubst.entrySet()) {
                Object formal = e.getKey();
                Object actual = e.getValue();
                newSubst.put(formal, substSubstValue(actual));
            }
            newSubst.putAll(this.subst);
            return this.ts.subst(tbase, newSubst, this.cache);
        } else if (t instanceof ClassType) {
            return substClassType((ClassType) t);
        } else {
            return t;
        }
    }

    protected Object substSubstValue(Object value) {
        return value;
    }

    public ClassType substClassType(ClassType t) {
        return new SubstClassType_c(this.ts, t.position(), t, this);
    }

    @Override // polyglot.ext.param.types.Subst
    public Type substType(Type t) {
        if (t == null || t == this) {
            return t;
        }
        Type cached = (Type) this.cache.get(t);
        if (cached == null) {
            cached = uncachedSubstType(t);
            this.cache.put(t, cached);
            if (Report.should_report(Topics.subst, 2)) {
                Report.report(2, new StringBuffer().append("substType(").append(t).append(": ").append(t.getClass().getName()).append(") = ").append(cached).append(": ").append(cached.getClass().getName()).toString());
            }
        }
        return cached;
    }

    @Override // polyglot.ext.param.types.Subst
    public PClass substPClass(PClass pclazz) {
        MuPClass newPclazz = this.ts.mutablePClass(pclazz.position());
        newPclazz.formals(pclazz.formals());
        newPclazz.clazz((ClassType) substType(pclazz.clazz()));
        return newPclazz;
    }

    @Override // polyglot.ext.param.types.Subst
    public FieldInstance substField(FieldInstance fi) {
        ReferenceType ct = (ReferenceType) substType(fi.container());
        Type t = substType(fi.type());
        return fi.type(t).container(ct);
    }

    @Override // polyglot.ext.param.types.Subst
    public MethodInstance substMethod(MethodInstance mi) {
        ReferenceType ct = (ReferenceType) substType(mi.container());
        Type rt = substType(mi.returnType());
        List formalTypes = mi.formalTypes();
        List formalTypes2 = substTypeList(formalTypes);
        List throwTypes = mi.throwTypes();
        return mi.returnType(rt).formalTypes(formalTypes2).throwTypes(substTypeList(throwTypes)).container(ct);
    }

    @Override // polyglot.ext.param.types.Subst
    public ConstructorInstance substConstructor(ConstructorInstance ci) {
        ClassType ct = (ClassType) substType(ci.container());
        List formalTypes = ci.formalTypes();
        List formalTypes2 = substTypeList(formalTypes);
        List throwTypes = ci.throwTypes();
        return ci.formalTypes(formalTypes2).throwTypes(substTypeList(throwTypes)).container(ct);
    }

    @Override // polyglot.ext.param.types.Subst
    public List substTypeList(List list) {
        return new CachingTransformingList(list, (Transformation) new TypeXform(this));
    }

    @Override // polyglot.ext.param.types.Subst
    public List substMethodList(List list) {
        return new CachingTransformingList(list, (Transformation) new MethodXform(this));
    }

    @Override // polyglot.ext.param.types.Subst
    public List substConstructorList(List list) {
        return new CachingTransformingList(list, (Transformation) new ConstructorXform(this));
    }

    @Override // polyglot.ext.param.types.Subst
    public List substFieldList(List list) {
        return new CachingTransformingList(list, (Transformation) new FieldXform(this));
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/Subst_c$TypeXform.class */
    public class TypeXform implements Transformation {
        private final Subst_c this$0;

        public TypeXform(Subst_c this$0) {
            this.this$0 = this$0;
        }

        @Override // polyglot.util.Transformation
        public Object transform(Object o) {
            if (!(o instanceof Type)) {
                throw new InternalCompilerError(new StringBuffer().append(o).append(" is not a type.").toString());
            }
            return this.this$0.substType((Type) o);
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/Subst_c$FieldXform.class */
    public class FieldXform implements Transformation {
        private final Subst_c this$0;

        public FieldXform(Subst_c this$0) {
            this.this$0 = this$0;
        }

        @Override // polyglot.util.Transformation
        public Object transform(Object o) {
            if (!(o instanceof FieldInstance)) {
                throw new InternalCompilerError(new StringBuffer().append(o).append(" is not a field.").toString());
            }
            return this.this$0.substField((FieldInstance) o);
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/Subst_c$MethodXform.class */
    public class MethodXform implements Transformation {
        private final Subst_c this$0;

        public MethodXform(Subst_c this$0) {
            this.this$0 = this$0;
        }

        @Override // polyglot.util.Transformation
        public Object transform(Object o) {
            if (!(o instanceof MethodInstance)) {
                throw new InternalCompilerError(new StringBuffer().append(o).append(" is not a method.").toString());
            }
            return this.this$0.substMethod((MethodInstance) o);
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/Subst_c$ConstructorXform.class */
    public class ConstructorXform implements Transformation {
        private final Subst_c this$0;

        public ConstructorXform(Subst_c this$0) {
            this.this$0 = this$0;
        }

        @Override // polyglot.util.Transformation
        public Object transform(Object o) {
            if (!(o instanceof ConstructorInstance)) {
                throw new InternalCompilerError(new StringBuffer().append(o).append(" is not a constructor.").toString());
            }
            return this.this$0.substConstructor((ConstructorInstance) o);
        }
    }

    public boolean equals(Object o) {
        if (o instanceof Subst) {
            return this.subst.equals(((Subst) o).substitutions());
        }
        return false;
    }

    public int hashCode() {
        return this.subst.hashCode();
    }

    public String toString() {
        String str = "[";
        Iterator iter = this.subst.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            str = new StringBuffer().append(str).append("<").append(key).append(": ").append(this.subst.get(key)).append(">").toString();
            if (iter.hasNext()) {
                str = new StringBuffer().append(str).append(", ").toString();
            }
        }
        return new StringBuffer().append(str).append("]").toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (in instanceof TypeInputStream) {
            this.ts = (ParamTypeSystem) ((TypeInputStream) in).getTypeSystem();
        }
        this.cache = new HashMap();
        in.defaultReadObject();
    }
}
