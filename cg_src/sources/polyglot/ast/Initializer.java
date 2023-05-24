package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Initializer.class */
public interface Initializer extends CodeDecl {
    Flags flags();

    Initializer flags(Flags flags);

    InitializerInstance initializerInstance();

    Initializer initializerInstance(InitializerInstance initializerInstance);
}
