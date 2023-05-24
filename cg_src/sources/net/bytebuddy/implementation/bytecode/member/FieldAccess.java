package net.bytebuddy.implementation.bytecode.member;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess.class */
public enum FieldAccess {
    STATIC(179, 178, StackSize.ZERO),
    INSTANCE(181, 180, StackSize.SINGLE);
    
    private final int putterOpcode;
    private final int getterOpcode;
    private final int targetSizeChange;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess$Defined.class */
    public interface Defined {
        StackManipulation read();

        StackManipulation write();
    }

    FieldAccess(int putterOpcode, int getterOpcode, StackSize targetSizeChange) {
        this.putterOpcode = putterOpcode;
        this.getterOpcode = getterOpcode;
        this.targetSizeChange = targetSizeChange.getSize();
    }

    public static StackManipulation forEnumeration(EnumerationDescription enumerationDescription) {
        FieldList fieldList = enumerationDescription.getEnumerationType().getDeclaredFields().filter(ElementMatchers.named(enumerationDescription.getValue()));
        if (fieldList.size() == 1 && ((FieldDescription.InDefinedShape) fieldList.getOnly()).isStatic() && ((FieldDescription.InDefinedShape) fieldList.getOnly()).isPublic() && ((FieldDescription.InDefinedShape) fieldList.getOnly()).isEnum()) {
            FieldAccess fieldAccess = STATIC;
            fieldAccess.getClass();
            return new AccessDispatcher((FieldDescription.InDefinedShape) fieldList.getOnly()).read();
        }
        return StackManipulation.Illegal.INSTANCE;
    }

    public static Defined forField(FieldDescription.InDefinedShape fieldDescription) {
        if (fieldDescription.isStatic()) {
            FieldAccess fieldAccess = STATIC;
            fieldAccess.getClass();
            return new AccessDispatcher(fieldDescription);
        }
        FieldAccess fieldAccess2 = INSTANCE;
        fieldAccess2.getClass();
        return new AccessDispatcher(fieldDescription);
    }

    public static Defined forField(FieldDescription fieldDescription) {
        FieldDescription.InDefinedShape declaredField = fieldDescription.asDefined();
        if (fieldDescription.getType().asErasure().equals(declaredField.getType().asErasure())) {
            return forField(declaredField);
        }
        return OfGenericField.of(fieldDescription, forField(declaredField));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess$OfGenericField.class */
    public static class OfGenericField implements Defined {
        private final TypeDefinition targetType;
        private final Defined defined;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.targetType.equals(((OfGenericField) obj).targetType) && this.defined.equals(((OfGenericField) obj).defined);
        }

        public int hashCode() {
            return (((17 * 31) + this.targetType.hashCode()) * 31) + this.defined.hashCode();
        }

        protected OfGenericField(TypeDefinition targetType, Defined defined) {
            this.targetType = targetType;
            this.defined = defined;
        }

        protected static Defined of(FieldDescription fieldDescription, Defined fieldAccess) {
            return new OfGenericField(fieldDescription.getType(), fieldAccess);
        }

        @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.Defined
        public StackManipulation read() {
            return new StackManipulation.Compound(this.defined.read(), TypeCasting.to(this.targetType));
        }

        @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.Defined
        public StackManipulation write() {
            return this.defined.write();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess$AccessDispatcher.class */
    public class AccessDispatcher implements Defined {
        private final FieldDescription.InDefinedShape fieldDescription;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && FieldAccess.this.equals(FieldAccess.this) && this.fieldDescription.equals(((AccessDispatcher) obj).fieldDescription);
        }

        public int hashCode() {
            return (((17 * 31) + this.fieldDescription.hashCode()) * 31) + FieldAccess.this.hashCode();
        }

        protected AccessDispatcher(FieldDescription.InDefinedShape fieldDescription) {
            this.fieldDescription = fieldDescription;
        }

        @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.Defined
        public StackManipulation read() {
            return new FieldGetInstruction();
        }

        @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.Defined
        public StackManipulation write() {
            return new FieldPutInstruction();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess$AccessDispatcher$AbstractFieldInstruction.class */
        private abstract class AbstractFieldInstruction implements StackManipulation {
            protected abstract int getOpcode();

            protected abstract StackManipulation.Size resolveSize(StackSize stackSize);

            private AbstractFieldInstruction() {
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return true;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                methodVisitor.visitFieldInsn(getOpcode(), AccessDispatcher.this.fieldDescription.getDeclaringType().getInternalName(), AccessDispatcher.this.fieldDescription.getInternalName(), AccessDispatcher.this.fieldDescription.getDescriptor());
                return resolveSize(AccessDispatcher.this.fieldDescription.getType().getStackSize());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess$AccessDispatcher$FieldGetInstruction.class */
        public class FieldGetInstruction extends AbstractFieldInstruction {
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && AccessDispatcher.this.equals(AccessDispatcher.this);
            }

            public int hashCode() {
                return (17 * 31) + AccessDispatcher.this.hashCode();
            }

            protected FieldGetInstruction() {
                super();
            }

            @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.AccessDispatcher.AbstractFieldInstruction
            protected int getOpcode() {
                return FieldAccess.this.getterOpcode;
            }

            @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.AccessDispatcher.AbstractFieldInstruction
            protected StackManipulation.Size resolveSize(StackSize fieldSize) {
                int sizeChange = fieldSize.getSize() - FieldAccess.this.targetSizeChange;
                return new StackManipulation.Size(sizeChange, sizeChange);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/FieldAccess$AccessDispatcher$FieldPutInstruction.class */
        protected class FieldPutInstruction extends AbstractFieldInstruction {
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && AccessDispatcher.this.equals(AccessDispatcher.this);
            }

            public int hashCode() {
                return (17 * 31) + AccessDispatcher.this.hashCode();
            }

            protected FieldPutInstruction() {
                super();
            }

            @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.AccessDispatcher.AbstractFieldInstruction
            protected int getOpcode() {
                return FieldAccess.this.putterOpcode;
            }

            @Override // net.bytebuddy.implementation.bytecode.member.FieldAccess.AccessDispatcher.AbstractFieldInstruction
            protected StackManipulation.Size resolveSize(StackSize fieldSize) {
                return new StackManipulation.Size((-1) * (fieldSize.getSize() + FieldAccess.this.targetSizeChange), 0);
            }
        }
    }
}
