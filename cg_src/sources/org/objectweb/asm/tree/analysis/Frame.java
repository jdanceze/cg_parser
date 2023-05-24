package org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Value;
/* loaded from: gencallgraphv3.jar:asm-analysis-9.4.jar:org/objectweb/asm/tree/analysis/Frame.class */
public class Frame<V extends Value> {
    private static final int MAX_STACK_SIZE = 65536;
    private V returnValue;
    private V[] values;
    private int numLocals;
    private int numStack;
    private int maxStack;

    public Frame(int numLocals, int maxStack) {
        this.values = (V[]) new Value[numLocals + (maxStack >= 0 ? maxStack : 4)];
        this.numLocals = numLocals;
        this.numStack = 0;
        this.maxStack = maxStack >= 0 ? maxStack : 65536;
    }

    public Frame(Frame<? extends V> frame) {
        this(frame.numLocals, frame.values.length - frame.numLocals);
        init(frame);
    }

    public Frame<V> init(Frame<? extends V> frame) {
        this.returnValue = frame.returnValue;
        if (this.values.length < frame.values.length) {
            this.values = (V[]) ((Value[]) frame.values.clone());
        } else {
            System.arraycopy(frame.values, 0, this.values, 0, frame.values.length);
        }
        this.numLocals = frame.numLocals;
        this.numStack = frame.numStack;
        this.maxStack = frame.maxStack;
        return this;
    }

    public void initJumpTarget(int opcode, LabelNode target) {
    }

    public void setReturn(V v) {
        this.returnValue = v;
    }

    public int getLocals() {
        return this.numLocals;
    }

    public int getMaxStackSize() {
        return this.maxStack;
    }

    public V getLocal(int index) {
        if (index >= this.numLocals) {
            throw new IndexOutOfBoundsException("Trying to get an inexistant local variable " + index);
        }
        return this.values[index];
    }

    public void setLocal(int index, V value) {
        if (index >= this.numLocals) {
            throw new IndexOutOfBoundsException("Trying to set an inexistant local variable " + index);
        }
        this.values[index] = value;
    }

    public int getStackSize() {
        return this.numStack;
    }

    public V getStack(int index) {
        return this.values[this.numLocals + index];
    }

    public void setStack(int index, V value) {
        this.values[this.numLocals + index] = value;
    }

    public void clearStack() {
        this.numStack = 0;
    }

    public V pop() {
        if (this.numStack == 0) {
            throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
        }
        V[] vArr = this.values;
        int i = this.numLocals;
        int i2 = this.numStack - 1;
        this.numStack = i2;
        return vArr[i + i2];
    }

    public void push(V value) {
        if (this.numLocals + this.numStack >= this.values.length) {
            if (this.numLocals + this.numStack >= this.maxStack) {
                throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
            }
            V[] oldValues = this.values;
            this.values = (V[]) new Value[2 * this.values.length];
            System.arraycopy(oldValues, 0, this.values, 0, oldValues.length);
        }
        V[] vArr = this.values;
        int i = this.numLocals;
        int i2 = this.numStack;
        this.numStack = i2 + 1;
        vArr[i + i2] = value;
    }

    public void execute(AbstractInsnNode insn, Interpreter<V> interpreter) throws AnalyzerException {
        Value local;
        switch (insn.getOpcode()) {
            case 0:
            case 167:
            case 169:
                return;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                push(interpreter.newOperation(insn));
                return;
            case 19:
            case 20:
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
            case 196:
            default:
                throw new AnalyzerException(insn, "Illegal opcode " + insn.getOpcode());
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                push(interpreter.copyOperation(insn, getLocal(((VarInsnNode) insn).var)));
                return;
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
                V value2 = pop();
                push(interpreter.binaryOperation(insn, pop(), value2));
                return;
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                V value1 = interpreter.copyOperation(insn, pop());
                int varIndex = ((VarInsnNode) insn).var;
                setLocal(varIndex, value1);
                if (value1.getSize() == 2) {
                    setLocal(varIndex + 1, interpreter.newEmptyValue(varIndex + 1));
                }
                if (varIndex > 0 && (local = getLocal(varIndex - 1)) != null && local.getSize() == 2) {
                    setLocal(varIndex - 1, interpreter.newEmptyValue(varIndex - 1));
                    return;
                }
                return;
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
                V value3 = pop();
                V value22 = pop();
                interpreter.ternaryOperation(insn, pop(), value22, value3);
                return;
            case 87:
                if (pop().getSize() == 2) {
                    throw new AnalyzerException(insn, "Illegal use of POP");
                }
                return;
            case 88:
                if (pop().getSize() == 1 && pop().getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of POP2");
                }
                return;
            case 89:
                V value12 = pop();
                if (value12.getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of DUP");
                }
                push(value12);
                push(interpreter.copyOperation(insn, value12));
                return;
            case 90:
                V value13 = pop();
                V value23 = pop();
                if (value13.getSize() != 1 || value23.getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of DUP_X1");
                }
                push(interpreter.copyOperation(insn, value13));
                push(value23);
                push(value13);
                return;
            case 91:
                V value14 = pop();
                if (value14.getSize() != 1 || !executeDupX2(insn, value14, interpreter)) {
                    throw new AnalyzerException(insn, "Illegal use of DUP_X2");
                }
                return;
            case 92:
                V value15 = pop();
                if (value15.getSize() == 1) {
                    V value24 = pop();
                    if (value24.getSize() == 1) {
                        push(value24);
                        push(value15);
                        push(interpreter.copyOperation(insn, value24));
                        push(interpreter.copyOperation(insn, value15));
                        return;
                    }
                    throw new AnalyzerException(insn, "Illegal use of DUP2");
                }
                push(value15);
                push(interpreter.copyOperation(insn, value15));
                return;
            case 93:
                V value16 = pop();
                if (value16.getSize() == 1) {
                    V value25 = pop();
                    if (value25.getSize() == 1) {
                        V value32 = pop();
                        if (value32.getSize() == 1) {
                            push(interpreter.copyOperation(insn, value25));
                            push(interpreter.copyOperation(insn, value16));
                            push(value32);
                            push(value25);
                            push(value16);
                            return;
                        }
                    }
                } else {
                    V value26 = pop();
                    if (value26.getSize() == 1) {
                        push(interpreter.copyOperation(insn, value16));
                        push(value26);
                        push(value16);
                        return;
                    }
                }
                throw new AnalyzerException(insn, "Illegal use of DUP2_X1");
            case 94:
                V value17 = pop();
                if (value17.getSize() == 1) {
                    V value27 = pop();
                    if (value27.getSize() == 1) {
                        V value33 = pop();
                        if (value33.getSize() == 1) {
                            V value4 = pop();
                            if (value4.getSize() == 1) {
                                push(interpreter.copyOperation(insn, value27));
                                push(interpreter.copyOperation(insn, value17));
                                push(value4);
                                push(value33);
                                push(value27);
                                push(value17);
                                return;
                            }
                        } else {
                            push(interpreter.copyOperation(insn, value27));
                            push(interpreter.copyOperation(insn, value17));
                            push(value33);
                            push(value27);
                            push(value17);
                            return;
                        }
                    }
                } else if (executeDupX2(insn, value17, interpreter)) {
                    return;
                }
                throw new AnalyzerException(insn, "Illegal use of DUP2_X2");
            case 95:
                V value28 = pop();
                V value18 = pop();
                if (value18.getSize() != 1 || value28.getSize() != 1) {
                    throw new AnalyzerException(insn, "Illegal use of SWAP");
                }
                push(interpreter.copyOperation(insn, value28));
                push(interpreter.copyOperation(insn, value18));
                return;
            case 116:
            case 117:
            case 118:
            case 119:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 132:
                int varIndex2 = ((IincInsnNode) insn).var;
                setLocal(varIndex2, interpreter.unaryOperation(insn, getLocal(varIndex2)));
                return;
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
                interpreter.unaryOperation(insn, pop());
                return;
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 181:
                V value29 = pop();
                interpreter.binaryOperation(insn, pop(), value29);
                return;
            case 168:
                push(interpreter.newOperation(insn));
                return;
            case 170:
            case 171:
                interpreter.unaryOperation(insn, pop());
                return;
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
                V value19 = pop();
                interpreter.unaryOperation(insn, value19);
                interpreter.returnOperation(insn, value19, this.returnValue);
                return;
            case 177:
                if (this.returnValue != null) {
                    throw new AnalyzerException(insn, "Incompatible return type");
                }
                return;
            case 178:
                push(interpreter.newOperation(insn));
                return;
            case 179:
                interpreter.unaryOperation(insn, pop());
                return;
            case 180:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                executeInvokeInsn(insn, ((MethodInsnNode) insn).desc, interpreter);
                return;
            case 186:
                executeInvokeInsn(insn, ((InvokeDynamicInsnNode) insn).desc, interpreter);
                return;
            case 187:
                push(interpreter.newOperation(insn));
                return;
            case 188:
            case 189:
            case 190:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 191:
                interpreter.unaryOperation(insn, pop());
                return;
            case 192:
            case 193:
                push(interpreter.unaryOperation(insn, pop()));
                return;
            case 194:
            case 195:
                interpreter.unaryOperation(insn, pop());
                return;
            case 197:
                List<V> valueList = new ArrayList<>();
                for (int i = ((MultiANewArrayInsnNode) insn).dims; i > 0; i--) {
                    valueList.add(0, pop());
                }
                push(interpreter.naryOperation(insn, valueList));
                return;
            case 198:
            case 199:
                interpreter.unaryOperation(insn, pop());
                return;
        }
    }

    private boolean executeDupX2(AbstractInsnNode insn, V value1, Interpreter<V> interpreter) throws AnalyzerException {
        V value2 = pop();
        if (value2.getSize() == 1) {
            V value3 = pop();
            if (value3.getSize() == 1) {
                push(interpreter.copyOperation(insn, value1));
                push(value3);
                push(value2);
                push(value1);
                return true;
            }
            return false;
        }
        push(interpreter.copyOperation(insn, value1));
        push(value2);
        push(value1);
        return true;
    }

    private void executeInvokeInsn(AbstractInsnNode insn, String methodDescriptor, Interpreter<V> interpreter) throws AnalyzerException {
        ArrayList<V> valueList = new ArrayList<>();
        for (int i = Type.getArgumentTypes(methodDescriptor).length; i > 0; i--) {
            valueList.add(0, pop());
        }
        if (insn.getOpcode() != 184 && insn.getOpcode() != 186) {
            valueList.add(0, pop());
        }
        if (Type.getReturnType(methodDescriptor) == Type.VOID_TYPE) {
            interpreter.naryOperation(insn, valueList);
        } else {
            push(interpreter.naryOperation(insn, valueList));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean merge(Frame<? extends V> frame, Interpreter<V> interpreter) throws AnalyzerException {
        if (this.numStack != frame.numStack) {
            throw new AnalyzerException(null, "Incompatible stack heights");
        }
        boolean changed = false;
        for (int i = 0; i < this.numLocals + this.numStack; i++) {
            V v = interpreter.merge(this.values[i], frame.values[i]);
            if (!v.equals(this.values[i])) {
                this.values[i] = v;
                changed = true;
            }
        }
        return changed;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean merge(Frame<? extends V> frame, boolean[] localsUsed) {
        boolean changed = false;
        for (int i = 0; i < this.numLocals; i++) {
            if (!localsUsed[i] && !this.values[i].equals(frame.values[i])) {
                this.values[i] = frame.values[i];
                changed = true;
            }
        }
        return changed;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getLocals(); i++) {
            stringBuilder.append(getLocal(i));
        }
        stringBuilder.append(' ');
        for (int i2 = 0; i2 < getStackSize(); i2++) {
            stringBuilder.append(getStack(i2).toString());
        }
        return stringBuilder.toString();
    }
}
