package net.bytebuddy.implementation.bytecode.assign.primitive;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/PrimitiveWideningDelegate.class */
public enum PrimitiveWideningDelegate {
    BOOLEAN(StackManipulation.Trivial.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE),
    BYTE(StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, StackManipulation.Trivial.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, new StackManipulation(133, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(134, StackSize.ZERO.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(133, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }),
    SHORT(StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, new StackManipulation(133, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(134, StackSize.ZERO.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(135, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }),
    CHARACTER(StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, StackManipulation.Trivial.INSTANCE, new StackManipulation(133, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(134, StackSize.ZERO.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(135, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }),
    INTEGER(StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, new StackManipulation(133, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(134, StackSize.ZERO.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(135, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }),
    LONG(StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, new StackManipulation(137, StackSize.SINGLE.toDecreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }, new StackManipulation(138, StackSize.ZERO.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }),
    FLOAT(StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE, new StackManipulation(141, StackSize.SINGLE.toIncreasingSize()) { // from class: net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveWideningDelegate.WideningStackManipulation
        private final int conversionOpcode;
        private final StackManipulation.Size size;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.conversionOpcode == ((WideningStackManipulation) obj).conversionOpcode && this.size.equals(((WideningStackManipulation) obj).size);
        }

        public int hashCode() {
            return (((17 * 31) + this.conversionOpcode) * 31) + this.size.hashCode();
        }

        {
            this.conversionOpcode = conversionOpcode;
            this.size = size;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitInsn(this.conversionOpcode);
            return this.size;
        }
    }),
    DOUBLE(StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Illegal.INSTANCE, StackManipulation.Trivial.INSTANCE);
    
    private final StackManipulation toBooleanStackManipulation;
    private final StackManipulation toByteStackManipulation;
    private final StackManipulation toShortStackManipulation;
    private final StackManipulation toCharacterStackManipulation;
    private final StackManipulation toIntegerStackManipulation;
    private final StackManipulation toLongStackManipulation;
    private final StackManipulation toFloatStackManipulation;
    private final StackManipulation toDoubleStackManipulation;

    PrimitiveWideningDelegate(StackManipulation toBooleanStackManipulation, StackManipulation toByteStackManipulation, StackManipulation toShortStackManipulation, StackManipulation toCharacterStackManipulation, StackManipulation toIntegerStackManipulation, StackManipulation toLongStackManipulation, StackManipulation toFloatStackManipulation, StackManipulation toDoubleStackManipulation) {
        this.toBooleanStackManipulation = toBooleanStackManipulation;
        this.toByteStackManipulation = toByteStackManipulation;
        this.toShortStackManipulation = toShortStackManipulation;
        this.toCharacterStackManipulation = toCharacterStackManipulation;
        this.toIntegerStackManipulation = toIntegerStackManipulation;
        this.toLongStackManipulation = toLongStackManipulation;
        this.toFloatStackManipulation = toFloatStackManipulation;
        this.toDoubleStackManipulation = toDoubleStackManipulation;
    }

    public static PrimitiveWideningDelegate forPrimitive(TypeDefinition typeDefinition) {
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
        throw new IllegalArgumentException("Not a primitive, non-void type: " + typeDefinition);
    }

    public StackManipulation widenTo(TypeDefinition typeDefinition) {
        if (typeDefinition.represents(Boolean.TYPE)) {
            return this.toBooleanStackManipulation;
        }
        if (typeDefinition.represents(Byte.TYPE)) {
            return this.toByteStackManipulation;
        }
        if (typeDefinition.represents(Short.TYPE)) {
            return this.toShortStackManipulation;
        }
        if (typeDefinition.represents(Character.TYPE)) {
            return this.toCharacterStackManipulation;
        }
        if (typeDefinition.represents(Integer.TYPE)) {
            return this.toIntegerStackManipulation;
        }
        if (typeDefinition.represents(Long.TYPE)) {
            return this.toLongStackManipulation;
        }
        if (typeDefinition.represents(Float.TYPE)) {
            return this.toFloatStackManipulation;
        }
        if (typeDefinition.represents(Double.TYPE)) {
            return this.toDoubleStackManipulation;
        }
        throw new IllegalArgumentException("Not a primitive non-void type: " + typeDefinition);
    }
}
