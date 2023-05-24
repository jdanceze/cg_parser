package net.bytebuddy.implementation.bytecode;

import java.util.Arrays;
import java.util.Collection;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.implementation.bytecode.StackManipulation;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/StackSize.class */
public enum StackSize {
    ZERO(0),
    SINGLE(1),
    DOUBLE(2);
    
    private final int size;

    StackSize(int size) {
        this.size = size;
    }

    public static StackSize of(Class<?> type) {
        if (type == Void.TYPE) {
            return ZERO;
        }
        if (type == Double.TYPE || type == Long.TYPE) {
            return DOUBLE;
        }
        return SINGLE;
    }

    public static StackSize of(int size) {
        switch (size) {
            case 0:
                return ZERO;
            case 1:
                return SINGLE;
            case 2:
                return DOUBLE;
            default:
                throw new IllegalArgumentException("Unexpected stack size value: " + size);
        }
    }

    public static int of(TypeDefinition... typeDefinition) {
        return of(Arrays.asList(typeDefinition));
    }

    public static int of(Collection<? extends TypeDefinition> typeDefinitions) {
        int size = 0;
        for (TypeDefinition typeDefinition : typeDefinitions) {
            size += typeDefinition.getStackSize().getSize();
        }
        return size;
    }

    public int getSize() {
        return this.size;
    }

    public StackManipulation.Size toIncreasingSize() {
        return new StackManipulation.Size(getSize(), getSize());
    }

    public StackManipulation.Size toDecreasingSize() {
        return new StackManipulation.Size((-1) * getSize(), 0);
    }

    public StackSize maximum(StackSize stackSize) {
        switch (this) {
            case DOUBLE:
                return this;
            case SINGLE:
                switch (stackSize) {
                    case DOUBLE:
                        return stackSize;
                    case SINGLE:
                    case ZERO:
                        return this;
                    default:
                        throw new AssertionError();
                }
            case ZERO:
                return stackSize;
            default:
                throw new AssertionError();
        }
    }
}
