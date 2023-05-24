package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.NullType;
import soot.ShortType;
import soot.Type;
import soot.TypeSwitch;
import soot.baf.InstSwitch;
import soot.baf.PrimitiveCastInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BPrimitiveCastInst.class */
public class BPrimitiveCastInst extends AbstractInst implements PrimitiveCastInst {
    Type fromType;
    protected Type toType;

    public BPrimitiveCastInst(Type fromType, Type toType) {
        if (fromType instanceof NullType) {
            throw new RuntimeException("invalid fromType " + fromType);
        }
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BPrimitiveCastInst(getFromType(), this.toType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return AbstractJasminClass.sizeOfType(this.fromType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return AbstractJasminClass.sizeOfType(this.toType);
    }

    @Override // soot.baf.PrimitiveCastInst
    public Type getFromType() {
        return this.fromType;
    }

    @Override // soot.baf.PrimitiveCastInst
    public void setFromType(Type t) {
        this.fromType = t;
    }

    @Override // soot.baf.PrimitiveCastInst
    public Type getToType() {
        return this.toType;
    }

    @Override // soot.baf.PrimitiveCastInst
    public void setToType(Type t) {
        this.toType = t;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        TypeSwitch<String> sw = new TypeSwitch<String>() { // from class: soot.baf.internal.BPrimitiveCastInst.1
            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void defaultCase(Type ty) {
                throw new RuntimeException("invalid fromType " + BPrimitiveCastInst.this.fromType);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseDoubleType(DoubleType ty) {
                if (IntType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("d2i");
                } else if (LongType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("d2l");
                } else if (FloatType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("d2f");
                } else {
                    throw new RuntimeException("invalid toType from double: " + BPrimitiveCastInst.this.toType);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseFloatType(FloatType ty) {
                if (IntType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("f2i");
                } else if (LongType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("f2l");
                } else if (DoubleType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("f2d");
                } else {
                    throw new RuntimeException("invalid toType from float: " + BPrimitiveCastInst.this.toType);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseIntType(IntType ty) {
                emitIntToTypeCast();
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseBooleanType(BooleanType ty) {
                emitIntToTypeCast();
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseByteType(ByteType ty) {
                emitIntToTypeCast();
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseCharType(CharType ty) {
                emitIntToTypeCast();
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseShortType(ShortType ty) {
                emitIntToTypeCast();
            }

            private void emitIntToTypeCast() {
                if (ByteType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("i2b");
                } else if (CharType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("i2c");
                } else if (ShortType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("i2s");
                } else if (FloatType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("i2f");
                } else if (LongType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("i2l");
                } else if (DoubleType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("i2d");
                } else if (IntType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("");
                } else if (BooleanType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("");
                } else {
                    throw new RuntimeException("invalid toType from int: " + BPrimitiveCastInst.this.toType);
                }
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseLongType(LongType ty) {
                if (IntType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("l2i");
                } else if (FloatType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("l2f");
                } else if (DoubleType.v().equals(BPrimitiveCastInst.this.toType)) {
                    setResult("l2d");
                } else {
                    throw new RuntimeException("invalid toType from long: " + BPrimitiveCastInst.this.toType);
                }
            }
        };
        this.fromType.apply(sw);
        return sw.getResult();
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return String.valueOf(getName()) + getParameters();
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).casePrimitiveCastInst(this);
    }
}
