package net.bytebuddy.implementation.bytecode.assign.primitive;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/PrimitiveUnboxingDelegate.class */
public enum PrimitiveUnboxingDelegate implements StackManipulation {
    BOOLEAN(Boolean.class, Boolean.TYPE, StackSize.ZERO, "booleanValue", "()Z"),
    BYTE(Byte.class, Byte.TYPE, StackSize.ZERO, "byteValue", "()B"),
    SHORT(Short.class, Short.TYPE, StackSize.ZERO, "shortValue", "()S"),
    CHARACTER(Character.class, Character.TYPE, StackSize.ZERO, "charValue", "()C"),
    INTEGER(Integer.class, Integer.TYPE, StackSize.ZERO, "intValue", "()I"),
    LONG(Long.class, Long.TYPE, StackSize.SINGLE, "longValue", "()J"),
    FLOAT(Float.class, Float.TYPE, StackSize.ZERO, "floatValue", "()F"),
    DOUBLE(Double.class, Double.TYPE, StackSize.SINGLE, "doubleValue", "()D");
    
    private final TypeDescription wrapperType;
    private final TypeDescription primitiveType;
    private final StackManipulation.Size size;
    private final String unboxingMethodName;
    private final String unboxingMethodDescriptor;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/PrimitiveUnboxingDelegate$UnboxingResponsible.class */
    public interface UnboxingResponsible {
        StackManipulation assignUnboxedTo(TypeDescription.Generic generic, Assigner assigner, Assigner.Typing typing);
    }

    PrimitiveUnboxingDelegate(Class cls, Class cls2, StackSize sizeDifference, String unboxingMethodName, String unboxingMethodDescriptor) {
        this.size = sizeDifference.toIncreasingSize();
        this.wrapperType = TypeDescription.ForLoadedType.of(cls);
        this.primitiveType = TypeDescription.ForLoadedType.of(cls2);
        this.unboxingMethodName = unboxingMethodName;
        this.unboxingMethodDescriptor = unboxingMethodDescriptor;
    }

    public static PrimitiveUnboxingDelegate forPrimitive(TypeDefinition typeDefinition) {
        if (typeDefinition.represents(Boolean.TYPE)) {
            return BOOLEAN;
        }
        if (typeDefinition.represents(Byte.TYPE)) {
            return BYTE;
        }
        if (typeDefinition.represents(Short.TYPE)) {
            return SHORT;
        }
        if (typeDefinition.represents(Character.TYPE)) {
            return CHARACTER;
        }
        if (typeDefinition.represents(Integer.TYPE)) {
            return INTEGER;
        }
        if (typeDefinition.represents(Long.TYPE)) {
            return LONG;
        }
        if (typeDefinition.represents(Float.TYPE)) {
            return FLOAT;
        }
        if (typeDefinition.represents(Double.TYPE)) {
            return DOUBLE;
        }
        throw new IllegalArgumentException("Expected non-void primitive type instead of " + typeDefinition);
    }

    public static UnboxingResponsible forReferenceType(TypeDefinition typeDefinition) {
        if (typeDefinition.isPrimitive()) {
            throw new IllegalArgumentException("Expected reference type instead of " + typeDefinition);
        }
        if (typeDefinition.represents(Boolean.class)) {
            return ExplicitlyTypedUnboxingResponsible.BOOLEAN;
        }
        if (typeDefinition.represents(Byte.class)) {
            return ExplicitlyTypedUnboxingResponsible.BYTE;
        }
        if (typeDefinition.represents(Short.class)) {
            return ExplicitlyTypedUnboxingResponsible.SHORT;
        }
        if (typeDefinition.represents(Character.class)) {
            return ExplicitlyTypedUnboxingResponsible.CHARACTER;
        }
        if (typeDefinition.represents(Integer.class)) {
            return ExplicitlyTypedUnboxingResponsible.INTEGER;
        }
        if (typeDefinition.represents(Long.class)) {
            return ExplicitlyTypedUnboxingResponsible.LONG;
        }
        if (typeDefinition.represents(Float.class)) {
            return ExplicitlyTypedUnboxingResponsible.FLOAT;
        }
        if (typeDefinition.represents(Double.class)) {
            return ExplicitlyTypedUnboxingResponsible.DOUBLE;
        }
        return new ImplicitlyTypedUnboxingResponsible(typeDefinition.asGenericType());
    }

    protected TypeDescription.Generic getWrapperType() {
        return this.wrapperType.asGenericType();
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public boolean isValid() {
        return true;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        methodVisitor.visitMethodInsn(182, this.wrapperType.asErasure().getInternalName(), this.unboxingMethodName, this.unboxingMethodDescriptor, false);
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/PrimitiveUnboxingDelegate$ExplicitlyTypedUnboxingResponsible.class */
    public enum ExplicitlyTypedUnboxingResponsible implements UnboxingResponsible {
        BOOLEAN(PrimitiveUnboxingDelegate.BOOLEAN),
        BYTE(PrimitiveUnboxingDelegate.BYTE),
        SHORT(PrimitiveUnboxingDelegate.SHORT),
        CHARACTER(PrimitiveUnboxingDelegate.CHARACTER),
        INTEGER(PrimitiveUnboxingDelegate.INTEGER),
        LONG(PrimitiveUnboxingDelegate.LONG),
        FLOAT(PrimitiveUnboxingDelegate.FLOAT),
        DOUBLE(PrimitiveUnboxingDelegate.DOUBLE);
        
        private final PrimitiveUnboxingDelegate primitiveUnboxingDelegate;

        ExplicitlyTypedUnboxingResponsible(PrimitiveUnboxingDelegate primitiveUnboxingDelegate) {
            this.primitiveUnboxingDelegate = primitiveUnboxingDelegate;
        }

        @Override // net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveUnboxingDelegate.UnboxingResponsible
        public StackManipulation assignUnboxedTo(TypeDescription.Generic targetType, Assigner assigner, Assigner.Typing typing) {
            return new StackManipulation.Compound(this.primitiveUnboxingDelegate, PrimitiveWideningDelegate.forPrimitive(this.primitiveUnboxingDelegate.primitiveType).widenTo(targetType));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/PrimitiveUnboxingDelegate$ImplicitlyTypedUnboxingResponsible.class */
    public static class ImplicitlyTypedUnboxingResponsible implements UnboxingResponsible {
        private final TypeDescription.Generic originalType;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.originalType.equals(((ImplicitlyTypedUnboxingResponsible) obj).originalType);
        }

        public int hashCode() {
            return (17 * 31) + this.originalType.hashCode();
        }

        protected ImplicitlyTypedUnboxingResponsible(TypeDescription.Generic originalType) {
            this.originalType = originalType;
        }

        @Override // net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveUnboxingDelegate.UnboxingResponsible
        public StackManipulation assignUnboxedTo(TypeDescription.Generic target, Assigner assigner, Assigner.Typing typing) {
            PrimitiveUnboxingDelegate primitiveUnboxingDelegate = PrimitiveUnboxingDelegate.forPrimitive(target);
            return new StackManipulation.Compound(assigner.assign(this.originalType, primitiveUnboxingDelegate.getWrapperType(), typing), primitiveUnboxingDelegate);
        }
    }
}
