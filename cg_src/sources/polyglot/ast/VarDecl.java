package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/VarDecl.class */
public interface VarDecl extends Term {
    Type declType();

    Flags flags();

    TypeNode type();

    String name();

    LocalInstance localInstance();
}
