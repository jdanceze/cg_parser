package net.bytebuddy.utility.visitor;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.jar.asm.Handle;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.OpenedClassReader;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/visitor/StackAwareMethodVisitor.class */
public class StackAwareMethodVisitor extends MethodVisitor {
    private static final int[] SIZE_CHANGE = new int[202];
    private List<StackSize> current;
    private final Map<Label, List<StackSize>> sizes;
    private int freeIndex;

    static {
        for (int index = 0; index < SIZE_CHANGE.length; index++) {
            SIZE_CHANGE[index] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEEEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(index) - 'E';
        }
    }

    public StackAwareMethodVisitor(MethodVisitor methodVisitor, MethodDescription instrumentedMethod) {
        super(OpenedClassReader.ASM_API, methodVisitor);
        this.current = new ArrayList();
        this.sizes = new HashMap();
        this.freeIndex = instrumentedMethod.getStackSize();
    }

    private void adjustStack(int delta) {
        adjustStack(delta, 0);
    }

    private void adjustStack(int delta, int offset) {
        if (delta > 2) {
            throw new IllegalStateException("Cannot push multiple values onto the operand stack: " + delta);
        }
        if (delta > 0) {
            int position = this.current.size();
            while (offset > 0 && position > 0) {
                position--;
                offset -= this.current.get(position).getSize();
            }
            if (offset < 0) {
                throw new IllegalStateException("Unexpected offset underflow: " + offset);
            }
            this.current.add(position, StackSize.of(delta));
        } else if (offset != 0) {
            throw new IllegalStateException("Cannot specify non-zero offset " + offset + " for non-incrementing value: " + delta);
        } else {
            while (delta < 0) {
                if (this.current.isEmpty()) {
                    return;
                }
                delta += this.current.remove(this.current.size() - 1).getSize();
            }
            if (delta == 1) {
                this.current.add(StackSize.SINGLE);
            } else if (delta != 0) {
                throw new IllegalStateException("Unexpected remainder on the operand stack: " + delta);
            }
        }
    }

    public void drainStack() {
        doDrain(this.current);
    }

    public int drainStack(int store, int load, StackSize size) {
        int difference = this.current.get(this.current.size() - 1).getSize() - size.getSize();
        if (this.current.size() == 1 && difference == 0) {
            return 0;
        }
        super.visitVarInsn(store, this.freeIndex);
        if (difference == 1) {
            super.visitInsn(87);
        } else if (difference != 0) {
            throw new IllegalStateException("Unexpected remainder on the operand stack: " + difference);
        }
        doDrain(this.current.subList(0, this.current.size() - 1));
        super.visitVarInsn(load, this.freeIndex);
        return this.freeIndex + size.getSize();
    }

    private void doDrain(List<StackSize> stackSizes) {
        ListIterator<StackSize> iterator = stackSizes.listIterator(stackSizes.size());
        while (iterator.hasPrevious()) {
            StackSize current = iterator.previous();
            switch (current) {
                case SINGLE:
                    super.visitInsn(87);
                    break;
                case DOUBLE:
                    super.visitInsn(88);
                    break;
                default:
                    throw new IllegalStateException("Unexpected stack size: " + current);
            }
        }
    }

    public void register(Label label, List<StackSize> stackSizes) {
        this.sizes.put(label, stackSizes);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitInsn(int opcode) {
        switch (opcode) {
            case 47:
            case 49:
                adjustStack(-2);
                adjustStack(2);
                break;
            case 90:
            case 93:
                adjustStack(SIZE_CHANGE[opcode], SIZE_CHANGE[opcode] + 1);
                break;
            case 91:
            case 94:
                adjustStack(SIZE_CHANGE[opcode], SIZE_CHANGE[opcode] + 2);
                break;
            case 133:
            case 135:
            case 140:
            case 141:
                adjustStack(-1);
                adjustStack(2);
                break;
            case 136:
            case 137:
            case 142:
            case 144:
                adjustStack(-2);
                adjustStack(1);
                break;
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 191:
                this.current.clear();
                break;
            default:
                adjustStack(SIZE_CHANGE[opcode]);
                break;
        }
        super.visitInsn(opcode);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitIntInsn(int opcode, int operand) {
        adjustStack(SIZE_CHANGE[opcode]);
        super.visitIntInsn(opcode, operand);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    @SuppressFBWarnings(value = {"SF_SWITCH_NO_DEFAULT"}, justification = "No default behavior is applied")
    public void visitVarInsn(int opcode, int variable) {
        switch (opcode) {
            case 54:
            case 56:
            case 58:
                this.freeIndex = Math.max(this.freeIndex, variable + 1);
                break;
            case 55:
            case 57:
                this.freeIndex = Math.max(this.freeIndex, variable + 2);
                break;
            case 169:
                this.current.clear();
                break;
        }
        adjustStack(SIZE_CHANGE[opcode]);
        super.visitVarInsn(opcode, variable);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        adjustStack(SIZE_CHANGE[opcode]);
        super.visitTypeInsn(opcode, type);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        int baseline = Type.getType(descriptor).getSize();
        switch (opcode) {
            case 178:
                adjustStack(baseline);
                break;
            case 179:
                adjustStack(-baseline);
                break;
            case 180:
                adjustStack(-1);
                adjustStack(baseline);
                break;
            case 181:
                adjustStack((-baseline) - 1);
                break;
            default:
                throw new IllegalStateException("Unexpected opcode: " + opcode);
        }
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        int baseline = Type.getArgumentsAndReturnSizes(descriptor);
        adjustStack((-(baseline >> 2)) + (opcode == 184 ? 1 : 0));
        adjustStack(baseline & 3);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrap, Object... bootstrapArguments) {
        int baseline = Type.getArgumentsAndReturnSizes(descriptor);
        adjustStack((-(baseline >> 2)) + 1);
        adjustStack(baseline & 3);
        super.visitInvokeDynamicInsn(name, descriptor, bootstrap, bootstrapArguments);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitLdcInsn(Object value) {
        adjustStack(((value instanceof Long) || (value instanceof Double)) ? 2 : 1);
        super.visitLdcInsn(value);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String descriptor, int dimension) {
        adjustStack(1 - dimension);
        super.visitMultiANewArrayInsn(descriptor, dimension);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitJumpInsn(int opcode, Label label) {
        adjustStack(SIZE_CHANGE[opcode]);
        this.sizes.put(label, new ArrayList(opcode == 168 ? CompoundList.of(this.current, StackSize.SINGLE) : this.current));
        if (opcode == 167) {
            this.current.clear();
        }
        super.visitJumpInsn(opcode, label);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitLabel(Label label) {
        List<StackSize> current = this.sizes.get(label);
        if (current != null) {
            this.current = new ArrayList(current);
        }
        super.visitLabel(label);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitTableSwitchInsn(int minimum, int maximum, Label defaultOption, Label... option) {
        adjustStack(-1);
        List<StackSize> current = new ArrayList<>(this.current);
        this.sizes.put(defaultOption, current);
        for (Label label : option) {
            this.sizes.put(label, current);
        }
        super.visitTableSwitchInsn(minimum, maximum, defaultOption, option);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label defaultOption, int[] key, Label[] option) {
        adjustStack(-1);
        List<StackSize> current = new ArrayList<>(this.current);
        this.sizes.put(defaultOption, current);
        for (Label label : option) {
            this.sizes.put(label, current);
        }
        super.visitLookupSwitchInsn(defaultOption, key, option);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.sizes.put(handler, Collections.singletonList(StackSize.SINGLE));
        super.visitTryCatchBlock(start, end, handler, type);
    }
}
