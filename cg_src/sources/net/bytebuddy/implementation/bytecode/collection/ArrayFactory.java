package net.bytebuddy.implementation.bytecode.collection;

import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.jar.asm.MethodVisitor;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/collection/ArrayFactory.class */
public class ArrayFactory implements CollectionFactory {
    private final TypeDescription.Generic componentType;
    private final ArrayCreator arrayCreator;
    @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
    private final StackManipulation.Size sizeDecrease;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.componentType.equals(((ArrayFactory) obj).componentType) && this.arrayCreator.equals(((ArrayFactory) obj).arrayCreator);
    }

    public int hashCode() {
        return (((17 * 31) + this.componentType.hashCode()) * 31) + this.arrayCreator.hashCode();
    }

    protected ArrayFactory(TypeDescription.Generic componentType, ArrayCreator arrayCreator) {
        this.componentType = componentType;
        this.arrayCreator = arrayCreator;
        this.sizeDecrease = StackSize.DOUBLE.toDecreasingSize().aggregate(componentType.getStackSize().toDecreasingSize());
    }

    public static ArrayFactory forType(TypeDescription.Generic componentType) {
        return new ArrayFactory(componentType, makeArrayCreatorFor(componentType));
    }

    private static ArrayCreator makeArrayCreatorFor(TypeDefinition componentType) {
        if (!componentType.isPrimitive()) {
            return new ArrayCreator.ForReferenceType(componentType.asErasure());
        }
        if (componentType.represents(Boolean.TYPE)) {
            return ArrayCreator.ForPrimitiveType.BOOLEAN;
        }
        if (componentType.represents(Byte.TYPE)) {
            return ArrayCreator.ForPrimitiveType.BYTE;
        }
        if (componentType.represents(Short.TYPE)) {
            return ArrayCreator.ForPrimitiveType.SHORT;
        }
        if (componentType.represents(Character.TYPE)) {
            return ArrayCreator.ForPrimitiveType.CHARACTER;
        }
        if (componentType.represents(Integer.TYPE)) {
            return ArrayCreator.ForPrimitiveType.INTEGER;
        }
        if (componentType.represents(Long.TYPE)) {
            return ArrayCreator.ForPrimitiveType.LONG;
        }
        if (componentType.represents(Float.TYPE)) {
            return ArrayCreator.ForPrimitiveType.FLOAT;
        }
        if (componentType.represents(Double.TYPE)) {
            return ArrayCreator.ForPrimitiveType.DOUBLE;
        }
        throw new IllegalArgumentException("Cannot create array of type " + componentType);
    }

    @Override // net.bytebuddy.implementation.bytecode.collection.CollectionFactory
    public StackManipulation withValues(List<? extends StackManipulation> stackManipulations) {
        return new ArrayStackManipulation(stackManipulations);
    }

    @Override // net.bytebuddy.implementation.bytecode.collection.CollectionFactory
    public TypeDescription.Generic getComponentType() {
        return this.componentType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/collection/ArrayFactory$ArrayCreator.class */
    public interface ArrayCreator extends StackManipulation {
        public static final StackManipulation.Size ARRAY_CREATION_SIZE_CHANGE = StackSize.ZERO.toDecreasingSize();

        int getStorageOpcode();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/collection/ArrayFactory$ArrayCreator$ForPrimitiveType.class */
        public enum ForPrimitiveType implements ArrayCreator {
            BOOLEAN(4, 84),
            BYTE(8, 84),
            SHORT(9, 86),
            CHARACTER(5, 85),
            INTEGER(10, 79),
            LONG(11, 80),
            FLOAT(6, 81),
            DOUBLE(7, 82);
            
            private final int creationOpcode;
            private final int storageOpcode;

            ForPrimitiveType(int creationOpcode, int storageOpcode) {
                this.creationOpcode = creationOpcode;
                this.storageOpcode = storageOpcode;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return true;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitIntInsn(188, this.creationOpcode);
                return ARRAY_CREATION_SIZE_CHANGE;
            }

            @Override // net.bytebuddy.implementation.bytecode.collection.ArrayFactory.ArrayCreator
            public int getStorageOpcode() {
                return this.storageOpcode;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/collection/ArrayFactory$ArrayCreator$ForReferenceType.class */
        public static class ForReferenceType implements ArrayCreator {
            private final String internalTypeName;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.internalTypeName.equals(((ForReferenceType) obj).internalTypeName);
            }

            public int hashCode() {
                return (17 * 31) + this.internalTypeName.hashCode();
            }

            protected ForReferenceType(TypeDescription referenceType) {
                this.internalTypeName = referenceType.getInternalName();
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return true;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitTypeInsn(189, this.internalTypeName);
                return ARRAY_CREATION_SIZE_CHANGE;
            }

            @Override // net.bytebuddy.implementation.bytecode.collection.ArrayFactory.ArrayCreator
            public int getStorageOpcode() {
                return 83;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/collection/ArrayFactory$ArrayStackManipulation.class */
    public class ArrayStackManipulation implements StackManipulation {
        private final List<? extends StackManipulation> stackManipulations;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.stackManipulations.equals(((ArrayStackManipulation) obj).stackManipulations) && ArrayFactory.this.equals(ArrayFactory.this);
        }

        public int hashCode() {
            return (((17 * 31) + this.stackManipulations.hashCode()) * 31) + ArrayFactory.this.hashCode();
        }

        protected ArrayStackManipulation(List<? extends StackManipulation> stackManipulations) {
            this.stackManipulations = stackManipulations;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            for (StackManipulation stackManipulation : this.stackManipulations) {
                if (!stackManipulation.isValid()) {
                    return false;
                }
            }
            return ArrayFactory.this.arrayCreator.isValid();
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            StackManipulation.Size size = IntegerConstant.forValue(this.stackManipulations.size()).apply(methodVisitor, implementationContext);
            StackManipulation.Size size2 = size.aggregate(ArrayFactory.this.arrayCreator.apply(methodVisitor, implementationContext));
            int index = 0;
            for (StackManipulation stackManipulation : this.stackManipulations) {
                methodVisitor.visitInsn(89);
                int i = index;
                index++;
                StackManipulation.Size size3 = size2.aggregate(StackSize.SINGLE.toIncreasingSize()).aggregate(IntegerConstant.forValue(i).apply(methodVisitor, implementationContext)).aggregate(stackManipulation.apply(methodVisitor, implementationContext));
                methodVisitor.visitInsn(ArrayFactory.this.arrayCreator.getStorageOpcode());
                size2 = size3.aggregate(ArrayFactory.this.sizeDecrease);
            }
            return size2;
        }
    }
}
