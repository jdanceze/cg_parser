package polyglot.ext.param.types;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/Subst.class */
public interface Subst extends Serializable {
    Iterator entries();

    ParamTypeSystem typeSystem();

    Map substitutions();

    Type substType(Type type);

    PClass substPClass(PClass pClass);

    FieldInstance substField(FieldInstance fieldInstance);

    MethodInstance substMethod(MethodInstance methodInstance);

    ConstructorInstance substConstructor(ConstructorInstance constructorInstance);

    List substTypeList(List list);

    List substMethodList(List list);

    List substConstructorList(List list);

    List substFieldList(List list);
}
