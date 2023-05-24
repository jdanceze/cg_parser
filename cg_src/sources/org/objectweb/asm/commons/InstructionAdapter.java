package org.objectweb.asm.commons;

import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
/* loaded from: gencallgraphv3.jar:asm-commons-9.4.jar:org/objectweb/asm/commons/InstructionAdapter.class */
public class InstructionAdapter extends MethodVisitor {
    public static final Type OBJECT_TYPE = Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR);

    public InstructionAdapter(MethodVisitor methodVisitor) {
        this(Opcodes.ASM9, methodVisitor);
        if (getClass() != InstructionAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected InstructionAdapter(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInsn(int opcode) {
        switch (opcode) {
            case 0:
                nop();
                return;
            case 1:
                aconst(null);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                iconst(opcode - 3);
                return;
            case 9:
            case 10:
                lconst(opcode - 9);
                return;
            case 11:
            case 12:
            case 13:
                fconst(opcode - 11);
                return;
            case 14:
            case 15:
                dconst(opcode - 14);
                return;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 132:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 192:
            case 193:
            default:
                throw new IllegalArgumentException();
            case 46:
                aload(Type.INT_TYPE);
                return;
            case 47:
                aload(Type.LONG_TYPE);
                return;
            case 48:
                aload(Type.FLOAT_TYPE);
                return;
            case 49:
                aload(Type.DOUBLE_TYPE);
                return;
            case 50:
                aload(OBJECT_TYPE);
                return;
            case 51:
                aload(Type.BYTE_TYPE);
                return;
            case 52:
                aload(Type.CHAR_TYPE);
                return;
            case 53:
                aload(Type.SHORT_TYPE);
                return;
            case 79:
                astore(Type.INT_TYPE);
                return;
            case 80:
                astore(Type.LONG_TYPE);
                return;
            case 81:
                astore(Type.FLOAT_TYPE);
                return;
            case 82:
                astore(Type.DOUBLE_TYPE);
                return;
            case 83:
                astore(OBJECT_TYPE);
                return;
            case 84:
                astore(Type.BYTE_TYPE);
                return;
            case 85:
                astore(Type.CHAR_TYPE);
                return;
            case 86:
                astore(Type.SHORT_TYPE);
                return;
            case 87:
                pop();
                return;
            case 88:
                pop2();
                return;
            case 89:
                dup();
                return;
            case 90:
                dupX1();
                return;
            case 91:
                dupX2();
                return;
            case 92:
                dup2();
                return;
            case 93:
                dup2X1();
                return;
            case 94:
                dup2X2();
                return;
            case 95:
                swap();
                return;
            case 96:
                add(Type.INT_TYPE);
                return;
            case 97:
                add(Type.LONG_TYPE);
                return;
            case 98:
                add(Type.FLOAT_TYPE);
                return;
            case 99:
                add(Type.DOUBLE_TYPE);
                return;
            case 100:
                sub(Type.INT_TYPE);
                return;
            case 101:
                sub(Type.LONG_TYPE);
                return;
            case 102:
                sub(Type.FLOAT_TYPE);
                return;
            case 103:
                sub(Type.DOUBLE_TYPE);
                return;
            case 104:
                mul(Type.INT_TYPE);
                return;
            case 105:
                mul(Type.LONG_TYPE);
                return;
            case 106:
                mul(Type.FLOAT_TYPE);
                return;
            case 107:
                mul(Type.DOUBLE_TYPE);
                return;
            case 108:
                div(Type.INT_TYPE);
                return;
            case 109:
                div(Type.LONG_TYPE);
                return;
            case 110:
                div(Type.FLOAT_TYPE);
                return;
            case 111:
                div(Type.DOUBLE_TYPE);
                return;
            case 112:
                rem(Type.INT_TYPE);
                return;
            case 113:
                rem(Type.LONG_TYPE);
                return;
            case 114:
                rem(Type.FLOAT_TYPE);
                return;
            case 115:
                rem(Type.DOUBLE_TYPE);
                return;
            case 116:
                neg(Type.INT_TYPE);
                return;
            case 117:
                neg(Type.LONG_TYPE);
                return;
            case 118:
                neg(Type.FLOAT_TYPE);
                return;
            case 119:
                neg(Type.DOUBLE_TYPE);
                return;
            case 120:
                shl(Type.INT_TYPE);
                return;
            case 121:
                shl(Type.LONG_TYPE);
                return;
            case 122:
                shr(Type.INT_TYPE);
                return;
            case 123:
                shr(Type.LONG_TYPE);
                return;
            case 124:
                ushr(Type.INT_TYPE);
                return;
            case 125:
                ushr(Type.LONG_TYPE);
                return;
            case 126:
                and(Type.INT_TYPE);
                return;
            case 127:
                and(Type.LONG_TYPE);
                return;
            case 128:
                or(Type.INT_TYPE);
                return;
            case 129:
                or(Type.LONG_TYPE);
                return;
            case 130:
                xor(Type.INT_TYPE);
                return;
            case 131:
                xor(Type.LONG_TYPE);
                return;
            case 133:
                cast(Type.INT_TYPE, Type.LONG_TYPE);
                return;
            case 134:
                cast(Type.INT_TYPE, Type.FLOAT_TYPE);
                return;
            case 135:
                cast(Type.INT_TYPE, Type.DOUBLE_TYPE);
                return;
            case 136:
                cast(Type.LONG_TYPE, Type.INT_TYPE);
                return;
            case 137:
                cast(Type.LONG_TYPE, Type.FLOAT_TYPE);
                return;
            case 138:
                cast(Type.LONG_TYPE, Type.DOUBLE_TYPE);
                return;
            case 139:
                cast(Type.FLOAT_TYPE, Type.INT_TYPE);
                return;
            case 140:
                cast(Type.FLOAT_TYPE, Type.LONG_TYPE);
                return;
            case 141:
                cast(Type.FLOAT_TYPE, Type.DOUBLE_TYPE);
                return;
            case 142:
                cast(Type.DOUBLE_TYPE, Type.INT_TYPE);
                return;
            case 143:
                cast(Type.DOUBLE_TYPE, Type.LONG_TYPE);
                return;
            case 144:
                cast(Type.DOUBLE_TYPE, Type.FLOAT_TYPE);
                return;
            case 145:
                cast(Type.INT_TYPE, Type.BYTE_TYPE);
                return;
            case 146:
                cast(Type.INT_TYPE, Type.CHAR_TYPE);
                return;
            case 147:
                cast(Type.INT_TYPE, Type.SHORT_TYPE);
                return;
            case 148:
                lcmp();
                return;
            case 149:
                cmpl(Type.FLOAT_TYPE);
                return;
            case 150:
                cmpg(Type.FLOAT_TYPE);
                return;
            case 151:
                cmpl(Type.DOUBLE_TYPE);
                return;
            case 152:
                cmpg(Type.DOUBLE_TYPE);
                return;
            case 172:
                areturn(Type.INT_TYPE);
                return;
            case 173:
                areturn(Type.LONG_TYPE);
                return;
            case 174:
                areturn(Type.FLOAT_TYPE);
                return;
            case 175:
                areturn(Type.DOUBLE_TYPE);
                return;
            case 176:
                areturn(OBJECT_TYPE);
                return;
            case 177:
                areturn(Type.VOID_TYPE);
                return;
            case 190:
                arraylength();
                return;
            case 191:
                athrow();
                return;
            case 194:
                monitorenter();
                return;
            case 195:
                monitorexit();
                return;
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int opcode, int operand) {
        switch (opcode) {
            case 16:
                iconst(operand);
                return;
            case 17:
                iconst(operand);
                return;
            case 188:
                switch (operand) {
                    case 4:
                        newarray(Type.BOOLEAN_TYPE);
                        return;
                    case 5:
                        newarray(Type.CHAR_TYPE);
                        return;
                    case 6:
                        newarray(Type.FLOAT_TYPE);
                        return;
                    case 7:
                        newarray(Type.DOUBLE_TYPE);
                        return;
                    case 8:
                        newarray(Type.BYTE_TYPE);
                        return;
                    case 9:
                        newarray(Type.SHORT_TYPE);
                        return;
                    case 10:
                        newarray(Type.INT_TYPE);
                        return;
                    case 11:
                        newarray(Type.LONG_TYPE);
                        return;
                    default:
                        throw new IllegalArgumentException();
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int opcode, int varIndex) {
        switch (opcode) {
            case 21:
                load(varIndex, Type.INT_TYPE);
                return;
            case 22:
                load(varIndex, Type.LONG_TYPE);
                return;
            case 23:
                load(varIndex, Type.FLOAT_TYPE);
                return;
            case 24:
                load(varIndex, Type.DOUBLE_TYPE);
                return;
            case 25:
                load(varIndex, OBJECT_TYPE);
                return;
            case 54:
                store(varIndex, Type.INT_TYPE);
                return;
            case 55:
                store(varIndex, Type.LONG_TYPE);
                return;
            case 56:
                store(varIndex, Type.FLOAT_TYPE);
                return;
            case 57:
                store(varIndex, Type.DOUBLE_TYPE);
                return;
            case 58:
                store(varIndex, OBJECT_TYPE);
                return;
            case 169:
                ret(varIndex);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        Type objectType = Type.getObjectType(type);
        switch (opcode) {
            case 187:
                anew(objectType);
                return;
            case 188:
            case 190:
            case 191:
            default:
                throw new IllegalArgumentException();
            case 189:
                newarray(objectType);
                return;
            case 192:
                checkcast(objectType);
                return;
            case 193:
                instanceOf(objectType);
                return;
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        switch (opcode) {
            case 178:
                getstatic(owner, name, descriptor);
                return;
            case 179:
                putstatic(owner, name, descriptor);
                return;
            case 180:
                getfield(owner, name, descriptor);
                return;
            case 181:
                putfield(owner, name, descriptor);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
        if (this.api < 327680 && (opcodeAndSource & 256) == 0) {
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
            return;
        }
        int opcode = opcodeAndSource & (-257);
        switch (opcode) {
            case 182:
                invokevirtual(owner, name, descriptor, isInterface);
                return;
            case 183:
                invokespecial(owner, name, descriptor, isInterface);
                return;
            case 184:
                invokestatic(owner, name, descriptor, isInterface);
                return;
            case 185:
                invokeinterface(owner, name, descriptor);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        invokedynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int opcode, Label label) {
        switch (opcode) {
            case 153:
                ifeq(label);
                return;
            case 154:
                ifne(label);
                return;
            case 155:
                iflt(label);
                return;
            case 156:
                ifge(label);
                return;
            case 157:
                ifgt(label);
                return;
            case 158:
                ifle(label);
                return;
            case 159:
                ificmpeq(label);
                return;
            case 160:
                ificmpne(label);
                return;
            case 161:
                ificmplt(label);
                return;
            case 162:
                ificmpge(label);
                return;
            case 163:
                ificmpgt(label);
                return;
            case 164:
                ificmple(label);
                return;
            case 165:
                ifacmpeq(label);
                return;
            case 166:
                ifacmpne(label);
                return;
            case 167:
                goTo(label);
                return;
            case 168:
                jsr(label);
                return;
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
                throw new IllegalArgumentException();
            case 198:
                ifnull(label);
                return;
            case 199:
                ifnonnull(label);
                return;
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        mark(label);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object value) {
        if (this.api < 327680 && ((value instanceof Handle) || ((value instanceof Type) && ((Type) value).getSort() == 11))) {
            throw new UnsupportedOperationException("This feature requires ASM5");
        }
        if (this.api < 458752 && (value instanceof ConstantDynamic)) {
            throw new UnsupportedOperationException("This feature requires ASM7");
        }
        if (value instanceof Integer) {
            iconst(((Integer) value).intValue());
        } else if (value instanceof Byte) {
            iconst(((Byte) value).intValue());
        } else if (value instanceof Character) {
            iconst(((Character) value).charValue());
        } else if (value instanceof Short) {
            iconst(((Short) value).intValue());
        } else if (value instanceof Boolean) {
            iconst(((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof Float) {
            fconst(((Float) value).floatValue());
        } else if (value instanceof Long) {
            lconst(((Long) value).longValue());
        } else if (value instanceof Double) {
            dconst(((Double) value).doubleValue());
        } else if (value instanceof String) {
            aconst(value);
        } else if (value instanceof Type) {
            tconst((Type) value);
        } else if (value instanceof Handle) {
            hconst((Handle) value);
        } else if (value instanceof ConstantDynamic) {
            cconst((ConstantDynamic) value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int varIndex, int increment) {
        iinc(varIndex, increment);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        tableswitch(min, max, dflt, labels);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        lookupswitch(dflt, keys, labels);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        multianewarray(descriptor, numDimensions);
    }

    public void nop() {
        this.mv.visitInsn(0);
    }

    public void aconst(Object value) {
        if (value == null) {
            this.mv.visitInsn(1);
        } else {
            this.mv.visitLdcInsn(value);
        }
    }

    public void iconst(int intValue) {
        if (intValue >= -1 && intValue <= 5) {
            this.mv.visitInsn(3 + intValue);
        } else if (intValue >= -128 && intValue <= 127) {
            this.mv.visitIntInsn(16, intValue);
        } else if (intValue >= -32768 && intValue <= 32767) {
            this.mv.visitIntInsn(17, intValue);
        } else {
            this.mv.visitLdcInsn(Integer.valueOf(intValue));
        }
    }

    public void lconst(long longValue) {
        if (longValue == 0 || longValue == 1) {
            this.mv.visitInsn(9 + ((int) longValue));
        } else {
            this.mv.visitLdcInsn(Long.valueOf(longValue));
        }
    }

    public void fconst(float floatValue) {
        int bits = Float.floatToIntBits(floatValue);
        if (bits == 0 || bits == 1065353216 || bits == 1073741824) {
            this.mv.visitInsn(11 + ((int) floatValue));
        } else {
            this.mv.visitLdcInsn(Float.valueOf(floatValue));
        }
    }

    public void dconst(double doubleValue) {
        long bits = Double.doubleToLongBits(doubleValue);
        if (bits == 0 || bits == 4607182418800017408L) {
            this.mv.visitInsn(14 + ((int) doubleValue));
        } else {
            this.mv.visitLdcInsn(Double.valueOf(doubleValue));
        }
    }

    public void tconst(Type type) {
        this.mv.visitLdcInsn(type);
    }

    public void hconst(Handle handle) {
        this.mv.visitLdcInsn(handle);
    }

    public void cconst(ConstantDynamic constantDynamic) {
        this.mv.visitLdcInsn(constantDynamic);
    }

    public void load(int varIndex, Type type) {
        this.mv.visitVarInsn(type.getOpcode(21), varIndex);
    }

    public void aload(Type type) {
        this.mv.visitInsn(type.getOpcode(46));
    }

    public void store(int varIndex, Type type) {
        this.mv.visitVarInsn(type.getOpcode(54), varIndex);
    }

    public void astore(Type type) {
        this.mv.visitInsn(type.getOpcode(79));
    }

    public void pop() {
        this.mv.visitInsn(87);
    }

    public void pop2() {
        this.mv.visitInsn(88);
    }

    public void dup() {
        this.mv.visitInsn(89);
    }

    public void dup2() {
        this.mv.visitInsn(92);
    }

    public void dupX1() {
        this.mv.visitInsn(90);
    }

    public void dupX2() {
        this.mv.visitInsn(91);
    }

    public void dup2X1() {
        this.mv.visitInsn(93);
    }

    public void dup2X2() {
        this.mv.visitInsn(94);
    }

    public void swap() {
        this.mv.visitInsn(95);
    }

    public void add(Type type) {
        this.mv.visitInsn(type.getOpcode(96));
    }

    public void sub(Type type) {
        this.mv.visitInsn(type.getOpcode(100));
    }

    public void mul(Type type) {
        this.mv.visitInsn(type.getOpcode(104));
    }

    public void div(Type type) {
        this.mv.visitInsn(type.getOpcode(108));
    }

    public void rem(Type type) {
        this.mv.visitInsn(type.getOpcode(112));
    }

    public void neg(Type type) {
        this.mv.visitInsn(type.getOpcode(116));
    }

    public void shl(Type type) {
        this.mv.visitInsn(type.getOpcode(120));
    }

    public void shr(Type type) {
        this.mv.visitInsn(type.getOpcode(122));
    }

    public void ushr(Type type) {
        this.mv.visitInsn(type.getOpcode(124));
    }

    public void and(Type type) {
        this.mv.visitInsn(type.getOpcode(126));
    }

    public void or(Type type) {
        this.mv.visitInsn(type.getOpcode(128));
    }

    public void xor(Type type) {
        this.mv.visitInsn(type.getOpcode(130));
    }

    public void iinc(int varIndex, int increment) {
        this.mv.visitIincInsn(varIndex, increment);
    }

    public void cast(Type from, Type to) {
        cast(this.mv, from, to);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void cast(MethodVisitor methodVisitor, Type from, Type to) {
        if (from != to) {
            if (from == Type.DOUBLE_TYPE) {
                if (to == Type.FLOAT_TYPE) {
                    methodVisitor.visitInsn(144);
                } else if (to == Type.LONG_TYPE) {
                    methodVisitor.visitInsn(143);
                } else {
                    methodVisitor.visitInsn(142);
                    cast(methodVisitor, Type.INT_TYPE, to);
                }
            } else if (from == Type.FLOAT_TYPE) {
                if (to == Type.DOUBLE_TYPE) {
                    methodVisitor.visitInsn(141);
                } else if (to == Type.LONG_TYPE) {
                    methodVisitor.visitInsn(140);
                } else {
                    methodVisitor.visitInsn(139);
                    cast(methodVisitor, Type.INT_TYPE, to);
                }
            } else if (from == Type.LONG_TYPE) {
                if (to == Type.DOUBLE_TYPE) {
                    methodVisitor.visitInsn(138);
                } else if (to == Type.FLOAT_TYPE) {
                    methodVisitor.visitInsn(137);
                } else {
                    methodVisitor.visitInsn(136);
                    cast(methodVisitor, Type.INT_TYPE, to);
                }
            } else if (to == Type.BYTE_TYPE) {
                methodVisitor.visitInsn(145);
            } else if (to == Type.CHAR_TYPE) {
                methodVisitor.visitInsn(146);
            } else if (to == Type.DOUBLE_TYPE) {
                methodVisitor.visitInsn(135);
            } else if (to == Type.FLOAT_TYPE) {
                methodVisitor.visitInsn(134);
            } else if (to == Type.LONG_TYPE) {
                methodVisitor.visitInsn(133);
            } else if (to == Type.SHORT_TYPE) {
                methodVisitor.visitInsn(147);
            }
        }
    }

    public void lcmp() {
        this.mv.visitInsn(148);
    }

    public void cmpl(Type type) {
        this.mv.visitInsn(type == Type.FLOAT_TYPE ? 149 : 151);
    }

    public void cmpg(Type type) {
        this.mv.visitInsn(type == Type.FLOAT_TYPE ? 150 : 152);
    }

    public void ifeq(Label label) {
        this.mv.visitJumpInsn(153, label);
    }

    public void ifne(Label label) {
        this.mv.visitJumpInsn(154, label);
    }

    public void iflt(Label label) {
        this.mv.visitJumpInsn(155, label);
    }

    public void ifge(Label label) {
        this.mv.visitJumpInsn(156, label);
    }

    public void ifgt(Label label) {
        this.mv.visitJumpInsn(157, label);
    }

    public void ifle(Label label) {
        this.mv.visitJumpInsn(158, label);
    }

    public void ificmpeq(Label label) {
        this.mv.visitJumpInsn(159, label);
    }

    public void ificmpne(Label label) {
        this.mv.visitJumpInsn(160, label);
    }

    public void ificmplt(Label label) {
        this.mv.visitJumpInsn(161, label);
    }

    public void ificmpge(Label label) {
        this.mv.visitJumpInsn(162, label);
    }

    public void ificmpgt(Label label) {
        this.mv.visitJumpInsn(163, label);
    }

    public void ificmple(Label label) {
        this.mv.visitJumpInsn(164, label);
    }

    public void ifacmpeq(Label label) {
        this.mv.visitJumpInsn(165, label);
    }

    public void ifacmpne(Label label) {
        this.mv.visitJumpInsn(166, label);
    }

    public void goTo(Label label) {
        this.mv.visitJumpInsn(167, label);
    }

    public void jsr(Label label) {
        this.mv.visitJumpInsn(168, label);
    }

    public void ret(int varIndex) {
        this.mv.visitVarInsn(169, varIndex);
    }

    public void tableswitch(int min, int max, Label dflt, Label... labels) {
        this.mv.visitTableSwitchInsn(min, max, dflt, labels);
    }

    public void lookupswitch(Label dflt, int[] keys, Label[] labels) {
        this.mv.visitLookupSwitchInsn(dflt, keys, labels);
    }

    public void areturn(Type type) {
        this.mv.visitInsn(type.getOpcode(172));
    }

    public void getstatic(String owner, String name, String descriptor) {
        this.mv.visitFieldInsn(178, owner, name, descriptor);
    }

    public void putstatic(String owner, String name, String descriptor) {
        this.mv.visitFieldInsn(179, owner, name, descriptor);
    }

    public void getfield(String owner, String name, String descriptor) {
        this.mv.visitFieldInsn(180, owner, name, descriptor);
    }

    public void putfield(String owner, String name, String descriptor) {
        this.mv.visitFieldInsn(181, owner, name, descriptor);
    }

    @Deprecated
    public void invokevirtual(String owner, String name, String descriptor) {
        if (this.api >= 327680) {
            invokevirtual(owner, name, descriptor, false);
        } else {
            this.mv.visitMethodInsn(182, owner, name, descriptor);
        }
    }

    public void invokevirtual(String owner, String name, String descriptor, boolean isInterface) {
        if (this.api < 327680) {
            if (isInterface) {
                throw new UnsupportedOperationException("INVOKEVIRTUAL on interfaces require ASM 5");
            }
            invokevirtual(owner, name, descriptor);
            return;
        }
        this.mv.visitMethodInsn(182, owner, name, descriptor, isInterface);
    }

    @Deprecated
    public void invokespecial(String owner, String name, String descriptor) {
        if (this.api >= 327680) {
            invokespecial(owner, name, descriptor, false);
        } else {
            this.mv.visitMethodInsn(183, owner, name, descriptor, false);
        }
    }

    public void invokespecial(String owner, String name, String descriptor, boolean isInterface) {
        if (this.api < 327680) {
            if (isInterface) {
                throw new UnsupportedOperationException("INVOKESPECIAL on interfaces require ASM 5");
            }
            invokespecial(owner, name, descriptor);
            return;
        }
        this.mv.visitMethodInsn(183, owner, name, descriptor, isInterface);
    }

    @Deprecated
    public void invokestatic(String owner, String name, String descriptor) {
        if (this.api >= 327680) {
            invokestatic(owner, name, descriptor, false);
        } else {
            this.mv.visitMethodInsn(184, owner, name, descriptor, false);
        }
    }

    public void invokestatic(String owner, String name, String descriptor, boolean isInterface) {
        if (this.api < 327680) {
            if (isInterface) {
                throw new UnsupportedOperationException("INVOKESTATIC on interfaces require ASM 5");
            }
            invokestatic(owner, name, descriptor);
            return;
        }
        this.mv.visitMethodInsn(184, owner, name, descriptor, isInterface);
    }

    public void invokeinterface(String owner, String name, String descriptor) {
        this.mv.visitMethodInsn(185, owner, name, descriptor, true);
    }

    public void invokedynamic(String name, String descriptor, Handle bootstrapMethodHandle, Object[] bootstrapMethodArguments) {
        this.mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    public void anew(Type type) {
        this.mv.visitTypeInsn(187, type.getInternalName());
    }

    public void newarray(Type type) {
        newarray(this.mv, type);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void newarray(MethodVisitor methodVisitor, Type type) {
        int arrayType;
        switch (type.getSort()) {
            case 1:
                arrayType = 4;
                break;
            case 2:
                arrayType = 5;
                break;
            case 3:
                arrayType = 8;
                break;
            case 4:
                arrayType = 9;
                break;
            case 5:
                arrayType = 10;
                break;
            case 6:
                arrayType = 6;
                break;
            case 7:
                arrayType = 11;
                break;
            case 8:
                arrayType = 7;
                break;
            default:
                methodVisitor.visitTypeInsn(189, type.getInternalName());
                return;
        }
        methodVisitor.visitIntInsn(188, arrayType);
    }

    public void arraylength() {
        this.mv.visitInsn(190);
    }

    public void athrow() {
        this.mv.visitInsn(191);
    }

    public void checkcast(Type type) {
        this.mv.visitTypeInsn(192, type.getInternalName());
    }

    public void instanceOf(Type type) {
        this.mv.visitTypeInsn(193, type.getInternalName());
    }

    public void monitorenter() {
        this.mv.visitInsn(194);
    }

    public void monitorexit() {
        this.mv.visitInsn(195);
    }

    public void multianewarray(String descriptor, int numDimensions) {
        this.mv.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    public void ifnull(Label label) {
        this.mv.visitJumpInsn(198, label);
    }

    public void ifnonnull(Label label) {
        this.mv.visitJumpInsn(199, label);
    }

    public void mark(Label label) {
        this.mv.visitLabel(label);
    }
}
