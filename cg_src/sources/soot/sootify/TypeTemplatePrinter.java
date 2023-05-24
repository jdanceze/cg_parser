package soot.sootify;

import soot.AnySubType;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.ErroneousType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.ShortType;
import soot.StmtAddressType;
import soot.Type;
import soot.TypeSwitch;
import soot.UnknownType;
import soot.VoidType;
/* loaded from: gencallgraphv3.jar:soot/sootify/TypeTemplatePrinter.class */
public class TypeTemplatePrinter extends TypeSwitch {
    private String varName;
    private final TemplatePrinter p;

    public void printAssign(String v, Type t) {
        String oldName = this.varName;
        this.varName = v;
        t.apply(this);
        this.varName = oldName;
    }

    public TypeTemplatePrinter(TemplatePrinter p) {
        this.p = p;
    }

    public void setVariableName(String name) {
        this.varName = name;
    }

    private void emit(String rhs) {
        this.p.println("Type " + this.varName + " = " + rhs + ';');
    }

    private void emitSpecial(String type, String rhs) {
        this.p.println(String.valueOf(type) + ' ' + this.varName + " = " + rhs + ';');
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseAnySubType(AnySubType t) {
        throw new IllegalArgumentException("cannot print this type");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseArrayType(ArrayType t) {
        printAssign("baseType", t.getElementType());
        this.p.println("int numDimensions=" + t.numDimensions + ';');
        emit("ArrayType.v(baseType, numDimensions)");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseBooleanType(BooleanType t) {
        emit("BooleanType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseByteType(ByteType t) {
        emit("ByteType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseCharType(CharType t) {
        emit("CharType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void defaultCase(Type t) {
        throw new IllegalArgumentException("cannot print this type");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseDoubleType(DoubleType t) {
        emit("DoubleType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseErroneousType(ErroneousType t) {
        throw new IllegalArgumentException("cannot print this type");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseFloatType(FloatType t) {
        emit("FloatType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseIntType(IntType t) {
        emit("IntType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseLongType(LongType t) {
        emit("LongType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseNullType(NullType t) {
        emit("NullType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseRefType(RefType t) {
        emitSpecial("RefType", "RefType.v(\"" + t.getClassName() + "\")");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseShortType(ShortType t) {
        emit("ShortType.v()");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseStmtAddressType(StmtAddressType t) {
        throw new IllegalArgumentException("cannot print this type");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseUnknownType(UnknownType t) {
        throw new IllegalArgumentException("cannot print this type");
    }

    @Override // soot.TypeSwitch, soot.ITypeSwitch
    public void caseVoidType(VoidType t) {
        emit("VoidType.v()");
    }
}
