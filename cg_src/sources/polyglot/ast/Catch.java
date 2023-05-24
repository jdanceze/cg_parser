package polyglot.ast;

import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Catch.class */
public interface Catch extends CompoundStmt {
    Type catchType();

    Formal formal();

    Catch formal(Formal formal);

    Block body();

    Catch body(Block block);
}
