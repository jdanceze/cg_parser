package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.Named;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/TopLevelDecl.class */
public interface TopLevelDecl extends Node {
    Flags flags();

    String name();

    Named declaration();
}
