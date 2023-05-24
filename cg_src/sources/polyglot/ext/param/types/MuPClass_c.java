package polyglot.ext.param.types;

import java.util.LinkedList;
import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.TypedList;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/MuPClass_c.class */
public class MuPClass_c extends PClass_c implements MuPClass {
    protected List formals;
    protected ClassType clazz;
    static Class class$polyglot$ext$param$types$Param;

    protected MuPClass_c() {
    }

    public MuPClass_c(TypeSystem ts) {
        this(ts, null);
    }

    public MuPClass_c(TypeSystem ts, Position pos) {
        super(ts, pos);
        Class cls;
        LinkedList linkedList = new LinkedList();
        if (class$polyglot$ext$param$types$Param == null) {
            cls = class$("polyglot.ext.param.types.Param");
            class$polyglot$ext$param$types$Param = cls;
        } else {
            cls = class$polyglot$ext$param$types$Param;
        }
        this.formals = new TypedList(linkedList, cls, false);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ext.param.types.PClass
    public List formals() {
        return this.formals;
    }

    @Override // polyglot.ext.param.types.PClass
    public ClassType clazz() {
        return this.clazz;
    }

    @Override // polyglot.ext.param.types.MuPClass
    public void formals(List formals) {
        this.formals = formals;
    }

    @Override // polyglot.ext.param.types.MuPClass
    public void addFormal(Param param) {
        formals().add(param);
    }

    @Override // polyglot.ext.param.types.MuPClass
    public void clazz(ClassType clazz) {
        this.clazz = clazz;
    }
}
