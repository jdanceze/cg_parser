package polyglot.ast;

import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/TypeNode.class */
public interface TypeNode extends Receiver, QualifierNode {
    TypeNode type(Type type);
}
