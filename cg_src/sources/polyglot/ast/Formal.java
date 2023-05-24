package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Formal.class */
public interface Formal extends VarDecl {
    Formal flags(Flags flags);

    Formal type(TypeNode typeNode);

    Formal name(String str);

    Formal localInstance(LocalInstance localInstance);
}
