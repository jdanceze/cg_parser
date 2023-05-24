package soot;
/* loaded from: gencallgraphv3.jar:soot/TypeSwitch.class */
public class TypeSwitch<T> implements ITypeSwitch {
    T result;

    @Override // soot.ITypeSwitch
    public void caseArrayType(ArrayType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseBooleanType(BooleanType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseByteType(ByteType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseCharType(CharType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseDoubleType(DoubleType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseFloatType(FloatType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseIntType(IntType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseLongType(LongType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseRefType(RefType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseShortType(ShortType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseStmtAddressType(StmtAddressType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseUnknownType(UnknownType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseVoidType(VoidType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseAnySubType(AnySubType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseNullType(NullType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void caseErroneousType(ErroneousType t) {
        defaultCase(t);
    }

    @Override // soot.ITypeSwitch
    public void defaultCase(Type t) {
    }

    @Override // soot.ITypeSwitch
    @Deprecated
    public void caseDefault(Type t) {
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}
