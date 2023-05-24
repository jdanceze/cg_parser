package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/Type.class */
public interface Type extends Qualifier {
    String translate(Resolver resolver);

    ArrayType arrayOf();

    ArrayType arrayOf(int i);

    ClassType toClass();

    NullType toNull();

    ReferenceType toReference();

    PrimitiveType toPrimitive();

    ArrayType toArray();

    boolean isSubtype(Type type);

    boolean descendsFrom(Type type);

    boolean isCastValid(Type type);

    boolean isImplicitCastValid(Type type);

    boolean numericConversionValid(Object obj);

    boolean numericConversionValid(long j);

    boolean isSubtypeImpl(Type type);

    boolean descendsFromImpl(Type type);

    boolean isCastValidImpl(Type type);

    boolean isImplicitCastValidImpl(Type type);

    boolean numericConversionValidImpl(Object obj);

    boolean numericConversionValidImpl(long j);

    boolean isPrimitive();

    boolean isVoid();

    boolean isBoolean();

    boolean isChar();

    boolean isByte();

    boolean isShort();

    boolean isInt();

    boolean isLong();

    boolean isFloat();

    boolean isDouble();

    boolean isIntOrLess();

    boolean isLongOrLess();

    boolean isNumeric();

    boolean isReference();

    boolean isNull();

    boolean isArray();

    boolean isClass();

    boolean isThrowable();

    boolean isUncheckedException();

    boolean isComparable(Type type);

    String toString();
}
