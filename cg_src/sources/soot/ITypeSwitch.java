package soot;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/ITypeSwitch.class */
interface ITypeSwitch extends Switch {
    void caseArrayType(ArrayType arrayType);

    void caseBooleanType(BooleanType booleanType);

    void caseByteType(ByteType byteType);

    void caseCharType(CharType charType);

    void caseDoubleType(DoubleType doubleType);

    void caseFloatType(FloatType floatType);

    void caseIntType(IntType intType);

    void caseLongType(LongType longType);

    void caseRefType(RefType refType);

    void caseShortType(ShortType shortType);

    void caseStmtAddressType(StmtAddressType stmtAddressType);

    void caseUnknownType(UnknownType unknownType);

    void caseVoidType(VoidType voidType);

    void caseAnySubType(AnySubType anySubType);

    void caseNullType(NullType nullType);

    void caseErroneousType(ErroneousType erroneousType);

    void defaultCase(Type type);

    @Deprecated
    void caseDefault(Type type);
}
