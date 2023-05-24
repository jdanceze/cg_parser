package org.powermock.core.bytebuddy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/Frame.class */
public class Frame {
    private Deque<Object> stack = new LinkedList();
    private List<LocalVariable> locals;

    public static Frame beforeConstructorCall(Iterable<? extends ParameterDescription> constructorParameters) {
        List<LocalVariable> locals = new ArrayList<>();
        locals.add(LocalVariable.UNINITIALIZED_THIS);
        int maxLocals = 1;
        for (ParameterDescription sourceParameter : constructorParameters) {
            TypeDescription.Generic type = sourceParameter.getType();
            locals.add(LocalVariable.from(type));
            maxLocals += type.getStackSize().getSize();
        }
        return new Frame(locals);
    }

    public Frame(List<LocalVariable> locals) {
        this.locals = Collections.unmodifiableList(locals);
    }

    public Frame addTopToLocals(int count) {
        List<LocalVariable> locals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            locals.add(LocalVariable.TOP);
        }
        return new Frame(CompoundList.of((List) this.locals, (List) locals));
    }

    public Frame addLocalVariable(LocalVariable localVariable) {
        return new Frame(CompoundList.of(this.locals, localVariable));
    }

    public Frame addLocalVariables(ParameterList<ParameterDescription.InDefinedShape> types) {
        List<LocalVariable> frameLocals = new ArrayList<>();
        for (ParameterDescription parameter : types) {
            TypeDescription.Generic type = parameter.getType();
            frameLocals.add(LocalVariable.from(type));
        }
        return new Frame(CompoundList.of((List) this.locals, (List) frameLocals));
    }

    public Object[] locals() {
        Object[] frameLocals = new Object[this.locals.size()];
        for (int i = 0; i < this.locals.size(); i++) {
            frameLocals[i] = this.locals.get(i).getType();
        }
        return frameLocals;
    }

    public int localSize() {
        return this.locals.size();
    }

    public int maxLocalVariableIndex() {
        int localStackSize = 0;
        for (LocalVariable localVariable : this.locals) {
            localStackSize += localVariable.getStackSize().getSize();
        }
        return localStackSize;
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/Frame$LocalVariable.class */
    public static class LocalVariable {
        public static final LocalVariable UNINITIALIZED_THIS = new LocalVariable(Opcodes.UNINITIALIZED_THIS, StackSize.SINGLE);
        public static final LocalVariable TOP = new LocalVariable(Opcodes.TOP, StackSize.SINGLE);
        public static final LocalVariable DOUBLE = new LocalVariable(Opcodes.DOUBLE, StackSize.DOUBLE);
        private final Object type;
        private final StackSize stackSize;

        public static LocalVariable from(TypeDescription.Generic type) {
            if (type.represents(Double.TYPE)) {
                return DOUBLE;
            }
            return new LocalVariable(type.getTypeName().replace('.', '/'), type.getStackSize());
        }

        private LocalVariable(Object type, StackSize size) {
            this.type = type;
            this.stackSize = size;
        }

        public Object getType() {
            return this.type;
        }

        public StackSize getStackSize() {
            return this.stackSize;
        }
    }
}
