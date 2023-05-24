package polyglot.types;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ReferenceType.class */
public interface ReferenceType extends Type {
    Type superType();

    List interfaces();

    List fields();

    List methods();

    FieldInstance fieldNamed(String str);

    List methodsNamed(String str);

    List methods(String str, List list);

    boolean hasMethod(MethodInstance methodInstance);

    boolean hasMethodImpl(MethodInstance methodInstance);
}
