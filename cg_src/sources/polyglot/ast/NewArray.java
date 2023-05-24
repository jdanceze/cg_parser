package polyglot.ast;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/NewArray.class */
public interface NewArray extends Expr {
    TypeNode baseType();

    NewArray baseType(TypeNode typeNode);

    int numDims();

    List dims();

    NewArray dims(List list);

    int additionalDims();

    NewArray additionalDims(int i);

    ArrayInit init();

    NewArray init(ArrayInit arrayInit);
}
