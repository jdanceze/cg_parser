package net.bytebuddy.implementation.bytecode.member;

import java.util.List;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.jar.asm.Handle;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation.class */
public enum MethodInvocation {
    VIRTUAL(182, 5, 182, 5),
    INTERFACE(185, 9, 185, 9),
    STATIC(184, 6, 184, 6),
    SPECIAL(183, 7, 183, 7),
    SPECIAL_CONSTRUCTOR(183, 8, 183, 8),
    VIRTUAL_PRIVATE(182, 5, 183, 7),
    INTERFACE_PRIVATE(185, 9, 183, 7);
    
    private final int opcode;
    private final int handle;
    private final int legacyOpcode;
    private final int legacyHandle;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$WithImplicitInvocationTargetType.class */
    public interface WithImplicitInvocationTargetType extends StackManipulation {
        StackManipulation virtual(TypeDescription typeDescription);

        StackManipulation special(TypeDescription typeDescription);

        StackManipulation dynamic(String str, TypeDescription typeDescription, List<? extends TypeDescription> list, List<?> list2);

        StackManipulation onHandle(HandleType handleType);
    }

    MethodInvocation(int opcode, int handle, int legacyOpcode, int legacyHandle) {
        this.opcode = opcode;
        this.handle = handle;
        this.legacyOpcode = legacyOpcode;
        this.legacyHandle = legacyHandle;
    }

    public static WithImplicitInvocationTargetType invoke(MethodDescription.InDefinedShape methodDescription) {
        if (methodDescription.isTypeInitializer()) {
            return IllegalInvocation.INSTANCE;
        }
        if (methodDescription.isStatic()) {
            MethodInvocation methodInvocation = STATIC;
            methodInvocation.getClass();
            return new Invocation(methodInvocation, methodDescription);
        } else if (methodDescription.isConstructor()) {
            MethodInvocation methodInvocation2 = SPECIAL_CONSTRUCTOR;
            methodInvocation2.getClass();
            return new Invocation(methodInvocation2, methodDescription);
        } else if (methodDescription.isPrivate()) {
            MethodInvocation methodInvocation3 = methodDescription.getDeclaringType().isInterface() ? INTERFACE_PRIVATE : VIRTUAL_PRIVATE;
            methodInvocation3.getClass();
            return new Invocation(methodInvocation3, methodDescription);
        } else if (methodDescription.getDeclaringType().isInterface()) {
            MethodInvocation methodInvocation4 = INTERFACE;
            methodInvocation4.getClass();
            return new Invocation(methodInvocation4, methodDescription);
        } else {
            MethodInvocation methodInvocation5 = VIRTUAL;
            methodInvocation5.getClass();
            return new Invocation(methodInvocation5, methodDescription);
        }
    }

    public static WithImplicitInvocationTargetType invoke(MethodDescription methodDescription) {
        MethodDescription.InDefinedShape declaredMethod = methodDescription.asDefined();
        if (declaredMethod.getReturnType().asErasure().equals(methodDescription.getReturnType().asErasure())) {
            return invoke(declaredMethod);
        }
        return OfGenericMethod.of(methodDescription, invoke(declaredMethod));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$IllegalInvocation.class */
    public enum IllegalInvocation implements WithImplicitInvocationTargetType {
        INSTANCE;

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation virtual(TypeDescription invocationTarget) {
            return StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation special(TypeDescription invocationTarget) {
            return StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation dynamic(String methodName, TypeDescription returnType, List<? extends TypeDescription> methodType, List<?> arguments) {
            return StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation onHandle(HandleType type) {
            return StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return false;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return StackManipulation.Illegal.INSTANCE.apply(methodVisitor, implementationContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$OfGenericMethod.class */
    public static class OfGenericMethod implements WithImplicitInvocationTargetType {
        private final TypeDescription targetType;
        private final WithImplicitInvocationTargetType invocation;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.targetType.equals(((OfGenericMethod) obj).targetType) && this.invocation.equals(((OfGenericMethod) obj).invocation);
        }

        public int hashCode() {
            return (((17 * 31) + this.targetType.hashCode()) * 31) + this.invocation.hashCode();
        }

        protected OfGenericMethod(TypeDescription targetType, WithImplicitInvocationTargetType invocation) {
            this.targetType = targetType;
            this.invocation = invocation;
        }

        protected static WithImplicitInvocationTargetType of(MethodDescription methodDescription, WithImplicitInvocationTargetType invocation) {
            return new OfGenericMethod(methodDescription.getReturnType().asErasure(), invocation);
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation virtual(TypeDescription invocationTarget) {
            return new StackManipulation.Compound(this.invocation.virtual(invocationTarget), TypeCasting.to(this.targetType));
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation special(TypeDescription invocationTarget) {
            return new StackManipulation.Compound(this.invocation.special(invocationTarget), TypeCasting.to(this.targetType));
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation dynamic(String methodName, TypeDescription returnType, List<? extends TypeDescription> methodType, List<?> arguments) {
            return this.invocation.dynamic(methodName, returnType, methodType, arguments);
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation onHandle(HandleType type) {
            return new StackManipulation.Compound(this.invocation.onHandle(type), TypeCasting.to(this.targetType));
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return this.invocation.isValid();
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return new StackManipulation.Compound(this.invocation, TypeCasting.to(this.targetType)).apply(methodVisitor, implementationContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$Invocation.class */
    public class Invocation implements WithImplicitInvocationTargetType {
        private final TypeDescription typeDescription;
        private final MethodDescription.InDefinedShape methodDescription;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && MethodInvocation.this.equals(MethodInvocation.this) && this.typeDescription.equals(((Invocation) obj).typeDescription) && this.methodDescription.equals(((Invocation) obj).methodDescription);
        }

        public int hashCode() {
            return (((((17 * 31) + this.typeDescription.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + MethodInvocation.this.hashCode();
        }

        protected Invocation(MethodInvocation this$0, MethodDescription.InDefinedShape methodDescription) {
            this(methodDescription, methodDescription.getDeclaringType());
        }

        protected Invocation(MethodDescription.InDefinedShape methodDescription, TypeDescription typeDescription) {
            this.typeDescription = typeDescription;
            this.methodDescription = methodDescription;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            methodVisitor.visitMethodInsn((MethodInvocation.this.opcode == MethodInvocation.this.legacyOpcode || implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V11)) ? MethodInvocation.this.opcode : MethodInvocation.this.legacyOpcode, this.typeDescription.getInternalName(), this.methodDescription.getInternalName(), this.methodDescription.getDescriptor(), this.typeDescription.isInterface());
            int parameterSize = this.methodDescription.getStackSize();
            int returnValueSize = this.methodDescription.getReturnType().getStackSize().getSize();
            return new StackManipulation.Size(returnValueSize - parameterSize, Math.max(0, returnValueSize - parameterSize));
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation virtual(TypeDescription invocationTarget) {
            if (this.methodDescription.isConstructor() || this.methodDescription.isStatic()) {
                return StackManipulation.Illegal.INSTANCE;
            }
            if (this.methodDescription.isPrivate()) {
                return this.methodDescription.getDeclaringType().equals(invocationTarget) ? this : StackManipulation.Illegal.INSTANCE;
            } else if (invocationTarget.isInterface()) {
                if (this.methodDescription.getDeclaringType().represents(Object.class)) {
                    return this;
                }
                MethodInvocation methodInvocation = MethodInvocation.INTERFACE;
                methodInvocation.getClass();
                return new Invocation(this.methodDescription, invocationTarget);
            } else {
                MethodInvocation methodInvocation2 = MethodInvocation.VIRTUAL;
                methodInvocation2.getClass();
                return new Invocation(this.methodDescription, invocationTarget);
            }
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation special(TypeDescription invocationTarget) {
            if (this.methodDescription.isSpecializableFor(invocationTarget)) {
                MethodInvocation methodInvocation = MethodInvocation.SPECIAL;
                methodInvocation.getClass();
                return new Invocation(this.methodDescription, invocationTarget);
            }
            return StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation dynamic(String methodName, TypeDescription returnType, List<? extends TypeDescription> methodType, List<?> arguments) {
            return this.methodDescription.isInvokeBootstrap() ? new DynamicInvocation(methodName, returnType, new TypeList.Explicit(methodType), this.methodDescription.asDefined(), arguments) : StackManipulation.Illegal.INSTANCE;
        }

        @Override // net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType
        public StackManipulation onHandle(HandleType type) {
            return new HandleInvocation(this.methodDescription, type);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$DynamicInvocation.class */
    protected class DynamicInvocation implements StackManipulation {
        private final String methodName;
        private final TypeDescription returnType;
        private final List<? extends TypeDescription> parameterTypes;
        private final MethodDescription.InDefinedShape bootstrapMethod;
        private final List<?> arguments;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && MethodInvocation.this.equals(MethodInvocation.this) && this.methodName.equals(((DynamicInvocation) obj).methodName) && this.returnType.equals(((DynamicInvocation) obj).returnType) && this.parameterTypes.equals(((DynamicInvocation) obj).parameterTypes) && this.bootstrapMethod.equals(((DynamicInvocation) obj).bootstrapMethod) && this.arguments.equals(((DynamicInvocation) obj).arguments);
        }

        public int hashCode() {
            return (((((((((((17 * 31) + this.methodName.hashCode()) * 31) + this.returnType.hashCode()) * 31) + this.parameterTypes.hashCode()) * 31) + this.bootstrapMethod.hashCode()) * 31) + this.arguments.hashCode()) * 31) + MethodInvocation.this.hashCode();
        }

        public DynamicInvocation(String methodName, TypeDescription returnType, List<? extends TypeDescription> parameterTypes, MethodDescription.InDefinedShape bootstrapMethod, List<?> arguments) {
            this.methodName = methodName;
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
            this.bootstrapMethod = bootstrapMethod;
            this.arguments = arguments;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            StringBuilder stringBuilder = new StringBuilder("(");
            for (TypeDescription parameterType : this.parameterTypes) {
                stringBuilder.append(parameterType.getDescriptor());
            }
            String methodDescriptor = stringBuilder.append(')').append(this.returnType.getDescriptor()).toString();
            methodVisitor.visitInvokeDynamicInsn(this.methodName, methodDescriptor, new Handle((MethodInvocation.this.handle == MethodInvocation.this.legacyHandle || implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V11)) ? MethodInvocation.this.handle : MethodInvocation.this.legacyHandle, this.bootstrapMethod.getDeclaringType().getInternalName(), this.bootstrapMethod.getInternalName(), this.bootstrapMethod.getDescriptor(), this.bootstrapMethod.getDeclaringType().isInterface()), this.arguments.toArray(new Object[0]));
            int stackSize = this.returnType.getStackSize().getSize() - StackSize.of(this.parameterTypes);
            return new StackManipulation.Size(stackSize, Math.max(stackSize, 0));
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$HandleInvocation.class */
    protected static class HandleInvocation implements StackManipulation {
        private static final String METHOD_HANDLE = "java/lang/invoke/MethodHandle";
        private final MethodDescription.InDefinedShape methodDescription;
        private final HandleType type;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.type.equals(((HandleInvocation) obj).type) && this.methodDescription.equals(((HandleInvocation) obj).methodDescription);
        }

        public int hashCode() {
            return (((17 * 31) + this.methodDescription.hashCode()) * 31) + this.type.hashCode();
        }

        protected HandleInvocation(MethodDescription.InDefinedShape methodDescription, HandleType type) {
            this.methodDescription = methodDescription;
            this.type = type;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            String descriptor;
            String methodName = this.type.getMethodName();
            if (this.methodDescription.isStatic() || this.methodDescription.isConstructor()) {
                descriptor = this.methodDescription.getDescriptor();
            } else {
                descriptor = "(" + this.methodDescription.getDeclaringType().getDescriptor() + this.methodDescription.getDescriptor().substring(1);
            }
            methodVisitor.visitMethodInsn(182, METHOD_HANDLE, methodName, descriptor, false);
            int parameterSize = 1 + this.methodDescription.getStackSize();
            int returnValueSize = this.methodDescription.getReturnType().getStackSize().getSize();
            return new StackManipulation.Size(returnValueSize - parameterSize, Math.max(0, returnValueSize - parameterSize));
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/member/MethodInvocation$HandleType.class */
    public enum HandleType {
        EXACT("invokeExact"),
        REGULAR("invoke");
        
        private final String methodName;

        HandleType(String methodName) {
            this.methodName = methodName;
        }

        protected String getMethodName() {
            return this.methodName;
        }
    }
}
