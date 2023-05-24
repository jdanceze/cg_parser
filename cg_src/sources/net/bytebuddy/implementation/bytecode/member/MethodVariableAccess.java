package net.bytebuddy.implementation.bytecode.member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess.class */
public enum MethodVariableAccess {
    INTEGER(21, 54, StackSize.SINGLE),
    LONG(22, 55, StackSize.DOUBLE),
    FLOAT(23, 56, StackSize.SINGLE),
    DOUBLE(24, 57, StackSize.DOUBLE),
    REFERENCE(25, 58, StackSize.SINGLE);
    
    private final int loadOpcode;
    private final int storeOpcode;
    private final StackSize size;

    MethodVariableAccess(int loadOpcode, int storeOpcode, StackSize stackSize) {
        this.loadOpcode = loadOpcode;
        this.size = stackSize;
        this.storeOpcode = storeOpcode;
    }

    public static MethodVariableAccess of(TypeDefinition typeDefinition) {
        if (typeDefinition.isPrimitive()) {
            if (typeDefinition.represents(Long.TYPE)) {
                return LONG;
            }
            if (typeDefinition.represents(Double.TYPE)) {
                return DOUBLE;
            }
            if (typeDefinition.represents(Float.TYPE)) {
                return FLOAT;
            }
            if (typeDefinition.represents(Void.TYPE)) {
                throw new IllegalArgumentException("Variable type cannot be void");
            }
            return INTEGER;
        }
        return REFERENCE;
    }

    public static MethodLoading allArgumentsOf(MethodDescription methodDescription) {
        return new MethodLoading(methodDescription, MethodLoading.TypeCastingHandler.NoOp.INSTANCE);
    }

    public static StackManipulation loadThis() {
        return REFERENCE.loadFrom(0);
    }

    public StackManipulation loadFrom(int offset) {
        return new OffsetLoading(offset);
    }

    public StackManipulation storeAt(int offset) {
        return new OffsetWriting(offset);
    }

    public StackManipulation increment(int offset, int value) {
        if (this != INTEGER) {
            throw new IllegalStateException("Cannot increment type: " + this);
        }
        return new OffsetIncrementing(offset, value);
    }

    public static StackManipulation load(ParameterDescription parameterDescription) {
        return of(parameterDescription.getType()).loadFrom(parameterDescription.getOffset());
    }

    public static StackManipulation store(ParameterDescription parameterDescription) {
        return of(parameterDescription.getType()).storeAt(parameterDescription.getOffset());
    }

    public static StackManipulation increment(ParameterDescription parameterDescription, int value) {
        return of(parameterDescription.getType()).increment(parameterDescription.getOffset(), value);
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$MethodLoading.class */
    public static class MethodLoading implements StackManipulation {
        private final MethodDescription methodDescription;
        private final TypeCastingHandler typeCastingHandler;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((MethodLoading) obj).methodDescription) && this.typeCastingHandler.equals(((MethodLoading) obj).typeCastingHandler);
        }

        public int hashCode() {
            return (((17 * 31) + this.methodDescription.hashCode()) * 31) + this.typeCastingHandler.hashCode();
        }

        protected MethodLoading(MethodDescription methodDescription, TypeCastingHandler typeCastingHandler) {
            this.methodDescription = methodDescription;
            this.typeCastingHandler = typeCastingHandler;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            List<StackManipulation> stackManipulations = new ArrayList<>();
            Iterator it = this.methodDescription.getParameters().iterator();
            while (it.hasNext()) {
                ParameterDescription parameterDescription = (ParameterDescription) it.next();
                TypeDescription parameterType = parameterDescription.getType().asErasure();
                stackManipulations.add(MethodVariableAccess.of(parameterType).loadFrom(parameterDescription.getOffset()));
                stackManipulations.add(this.typeCastingHandler.ofIndex(parameterType, parameterDescription.getIndex()));
            }
            return new StackManipulation.Compound(stackManipulations).apply(methodVisitor, implementationContext);
        }

        public StackManipulation prependThisReference() {
            return this.methodDescription.isStatic() ? this : new StackManipulation.Compound(MethodVariableAccess.loadThis(), this);
        }

        public MethodLoading asBridgeOf(MethodDescription bridgeTarget) {
            return new MethodLoading(this.methodDescription, new TypeCastingHandler.ForBridgeTarget(bridgeTarget));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$MethodLoading$TypeCastingHandler.class */
        public interface TypeCastingHandler {
            StackManipulation ofIndex(TypeDescription typeDescription, int i);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$MethodLoading$TypeCastingHandler$NoOp.class */
            public enum NoOp implements TypeCastingHandler {
                INSTANCE;

                @Override // net.bytebuddy.implementation.bytecode.member.MethodVariableAccess.MethodLoading.TypeCastingHandler
                public StackManipulation ofIndex(TypeDescription parameterType, int index) {
                    return StackManipulation.Trivial.INSTANCE;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$MethodLoading$TypeCastingHandler$ForBridgeTarget.class */
            public static class ForBridgeTarget implements TypeCastingHandler {
                private final MethodDescription bridgeTarget;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.bridgeTarget.equals(((ForBridgeTarget) obj).bridgeTarget);
                }

                public int hashCode() {
                    return (17 * 31) + this.bridgeTarget.hashCode();
                }

                public ForBridgeTarget(MethodDescription bridgeTarget) {
                    this.bridgeTarget = bridgeTarget;
                }

                @Override // net.bytebuddy.implementation.bytecode.member.MethodVariableAccess.MethodLoading.TypeCastingHandler
                public StackManipulation ofIndex(TypeDescription parameterType, int index) {
                    TypeDescription targetType = ((ParameterDescription) this.bridgeTarget.getParameters().get(index)).getType().asErasure();
                    return parameterType.equals(targetType) ? StackManipulation.Trivial.INSTANCE : TypeCasting.to(targetType);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$OffsetLoading.class */
    public class OffsetLoading implements StackManipulation {
        private final int offset;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.offset == ((OffsetLoading) obj).offset && MethodVariableAccess.this.equals(MethodVariableAccess.this);
        }

        public int hashCode() {
            return (((17 * 31) + this.offset) * 31) + MethodVariableAccess.this.hashCode();
        }

        protected OffsetLoading(int offset) {
            this.offset = offset;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitVarInsn(MethodVariableAccess.this.loadOpcode, this.offset);
            return MethodVariableAccess.this.size.toIncreasingSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$OffsetWriting.class */
    public class OffsetWriting implements StackManipulation {
        private final int offset;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.offset == ((OffsetWriting) obj).offset && MethodVariableAccess.this.equals(MethodVariableAccess.this);
        }

        public int hashCode() {
            return (((17 * 31) + this.offset) * 31) + MethodVariableAccess.this.hashCode();
        }

        protected OffsetWriting(int offset) {
            this.offset = offset;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitVarInsn(MethodVariableAccess.this.storeOpcode, this.offset);
            return MethodVariableAccess.this.size.toDecreasingSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodVariableAccess$OffsetIncrementing.class */
    public static class OffsetIncrementing implements StackManipulation {
        private final int offset;
        private final int value;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.offset == ((OffsetIncrementing) obj).offset && this.value == ((OffsetIncrementing) obj).value;
        }

        public int hashCode() {
            return (((17 * 31) + this.offset) * 31) + this.value;
        }

        protected OffsetIncrementing(int offset, int value) {
            this.offset = offset;
            this.value = value;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitIincInsn(this.offset, this.value);
            return new StackManipulation.Size(0, 0);
        }
    }
}
