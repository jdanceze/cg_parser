package polyglot.ext.param.types;

import java.util.List;
import polyglot.types.ClassType;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/MuPClass.class */
public interface MuPClass extends PClass {
    void formals(List list);

    void addFormal(Param param);

    void clazz(ClassType classType);
}
