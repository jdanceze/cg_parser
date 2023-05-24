package polyglot.ast;

import java.util.List;
import polyglot.types.SemanticException;
import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ArrayInit.class */
public interface ArrayInit extends Expr {
    List elements();

    ArrayInit elements(List list);

    void typeCheckElements(Type type) throws SemanticException;
}
