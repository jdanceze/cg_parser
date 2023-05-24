package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/AmbTypeNode.class */
public interface AmbTypeNode extends TypeNode, Ambiguous {
    QualifierNode qual();

    AmbTypeNode qual(QualifierNode qualifierNode);

    String name();

    AmbTypeNode name(String str);
}
