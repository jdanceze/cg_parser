package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ArrayType.class */
public interface ArrayType extends ReferenceType {
    Type base();

    ArrayType base(Type type);

    Type ultimateBase();

    FieldInstance lengthField();

    MethodInstance cloneMethod();

    int dims();
}
