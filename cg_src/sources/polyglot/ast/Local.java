package polyglot.ast;

import polyglot.types.LocalInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Local.class */
public interface Local extends Variable {
    String name();

    Local name(String str);

    LocalInstance localInstance();

    Local localInstance(LocalInstance localInstance);
}
