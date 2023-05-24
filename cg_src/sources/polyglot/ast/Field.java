package polyglot.ast;

import polyglot.types.FieldInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Field.class */
public interface Field extends Variable {
    FieldInstance fieldInstance();

    Field fieldInstance(FieldInstance fieldInstance);

    Receiver target();

    Field target(Receiver receiver);

    boolean isTargetImplicit();

    Field targetImplicit(boolean z);

    String name();

    Field name(String str);
}
