package polyglot.ast;

import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Disamb.class */
public interface Disamb {
    Node disambiguate(Ambiguous ambiguous, ContextVisitor contextVisitor, Position position, Prefix prefix, String str) throws SemanticException;
}
