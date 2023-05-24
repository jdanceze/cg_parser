package net.bytebuddy.implementation.auxiliary;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Serializable;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodAccessorFactory;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.Throw;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.matcher.ElementMatchers;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy.class */
public class TypeProxy implements AuxiliaryType {
    public static final String REFLECTION_METHOD = "make";
    public static final String INSTANCE_FIELD = "target";
    private final TypeDescription proxiedType;
    private final Implementation.Target implementationTarget;
    private final InvocationFactory invocationFactory;
    private final boolean ignoreFinalizer;
    private final boolean serializableProxy;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$InvocationFactory.class */
    public interface InvocationFactory {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$InvocationFactory$Default.class */
        public enum Default implements InvocationFactory {
            SUPER_METHOD { // from class: net.bytebuddy.implementation.auxiliary.TypeProxy.InvocationFactory.Default.1
                @Override // net.bytebuddy.implementation.auxiliary.TypeProxy.InvocationFactory
                public Implementation.SpecialMethodInvocation invoke(Implementation.Target implementationTarget, TypeDescription proxiedType, MethodDescription instrumentedMethod) {
                    return implementationTarget.invokeDominant(instrumentedMethod.asSignatureToken());
                }
            },
            DEFAULT_METHOD { // from class: net.bytebuddy.implementation.auxiliary.TypeProxy.InvocationFactory.Default.2
                @Override // net.bytebuddy.implementation.auxiliary.TypeProxy.InvocationFactory
                public Implementation.SpecialMethodInvocation invoke(Implementation.Target implementationTarget, TypeDescription proxiedType, MethodDescription instrumentedMethod) {
                    return implementationTarget.invokeDefault(instrumentedMethod.asSignatureToken(), proxiedType);
                }
            }
        }

        Implementation.SpecialMethodInvocation invoke(Implementation.Target target, TypeDescription typeDescription, MethodDescription methodDescription);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.ignoreFinalizer == ((TypeProxy) obj).ignoreFinalizer && this.serializableProxy == ((TypeProxy) obj).serializableProxy && this.proxiedType.equals(((TypeProxy) obj).proxiedType) && this.implementationTarget.equals(((TypeProxy) obj).implementationTarget) && this.invocationFactory.equals(((TypeProxy) obj).invocationFactory);
    }

    public int hashCode() {
        return (((((((((17 * 31) + this.proxiedType.hashCode()) * 31) + this.implementationTarget.hashCode()) * 31) + this.invocationFactory.hashCode()) * 31) + (this.ignoreFinalizer ? 1 : 0)) * 31) + (this.serializableProxy ? 1 : 0);
    }

    public TypeProxy(TypeDescription proxiedType, Implementation.Target implementationTarget, InvocationFactory invocationFactory, boolean ignoreFinalizer, boolean serializableProxy) {
        this.proxiedType = proxiedType;
        this.implementationTarget = implementationTarget;
        this.invocationFactory = invocationFactory;
        this.ignoreFinalizer = ignoreFinalizer;
        this.serializableProxy = serializableProxy;
    }

    @Override // net.bytebuddy.implementation.auxiliary.AuxiliaryType
    public DynamicType make(String auxiliaryTypeName, ClassFileVersion classFileVersion, MethodAccessorFactory methodAccessorFactory) {
        return new ByteBuddy(classFileVersion).with(TypeValidation.DISABLED).ignore(this.ignoreFinalizer ? ElementMatchers.isFinalizer() : ElementMatchers.none()).subclass(this.proxiedType).name(auxiliaryTypeName).modifiers(DEFAULT_TYPE_MODIFIER).implement(this.serializableProxy ? new Class[]{Serializable.class} : new Class[0]).method(ElementMatchers.any()).intercept(new MethodCall(methodAccessorFactory)).defineMethod(REFLECTION_METHOD, TargetType.class, Ownership.STATIC).intercept(SilentConstruction.INSTANCE).make();
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$AbstractMethodErrorThrow.class */
    protected enum AbstractMethodErrorThrow implements StackManipulation {
        INSTANCE;
        
        private final StackManipulation implementation;

        @SuppressFBWarnings(value = {"SE_BAD_FIELD_STORE"}, justification = "Fields of enumerations are never serialized")
        AbstractMethodErrorThrow() {
            TypeDescription abstractMethodError = TypeDescription.ForLoadedType.of(AbstractMethodError.class);
            MethodDescription constructor = (MethodDescription) abstractMethodError.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(0))).getOnly();
            this.implementation = new StackManipulation.Compound(TypeCreation.of(abstractMethodError), Duplication.SINGLE, MethodInvocation.invoke(constructor), Throw.INSTANCE);
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return this.implementation.isValid();
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return this.implementation.apply(methodVisitor, implementationContext);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$SilentConstruction.class */
    protected enum SilentConstruction implements Implementation {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getInstrumentedType());
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$SilentConstruction$Appender.class */
        protected static class Appender implements ByteCodeAppender {
            public static final String REFLECTION_FACTORY_INTERNAL_NAME = "sun/reflect/ReflectionFactory";
            public static final String GET_REFLECTION_FACTORY_METHOD_NAME = "getReflectionFactory";
            public static final String GET_REFLECTION_FACTORY_METHOD_DESCRIPTOR = "()Lsun/reflect/ReflectionFactory;";
            public static final String NEW_CONSTRUCTOR_FOR_SERIALIZATION_METHOD_NAME = "newConstructorForSerialization";
            public static final String NEW_CONSTRUCTOR_FOR_SERIALIZATION_METHOD_DESCRIPTOR = "(Ljava/lang/Class;Ljava/lang/reflect/Constructor;)Ljava/lang/reflect/Constructor;";
            public static final String JAVA_LANG_OBJECT_DESCRIPTOR = "Ljava/lang/Object;";
            public static final String JAVA_LANG_OBJECT_INTERNAL_NAME = "java/lang/Object";
            public static final String JAVA_LANG_CONSTRUCTOR_INTERNAL_NAME = "java/lang/reflect/Constructor";
            public static final String NEW_INSTANCE_METHOD_NAME = "newInstance";
            public static final String NEW_INSTANCE_METHOD_DESCRIPTOR = "([Ljava/lang/Object;)Ljava/lang/Object;";
            public static final String JAVA_LANG_CLASS_INTERNAL_NAME = "java/lang/Class";
            public static final String GET_DECLARED_CONSTRUCTOR_METHOD_NAME = "getDeclaredConstructor";
            public static final String GET_DECLARED_CONSTRUCTOR_METHOD_DESCRIPTOR = "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;";
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            private Appender(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                methodVisitor.visitMethodInsn(184, REFLECTION_FACTORY_INTERNAL_NAME, GET_REFLECTION_FACTORY_METHOD_NAME, GET_REFLECTION_FACTORY_METHOD_DESCRIPTOR, false);
                methodVisitor.visitLdcInsn(Type.getType(this.instrumentedType.getDescriptor()));
                methodVisitor.visitLdcInsn(Type.getType(JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(3);
                methodVisitor.visitTypeInsn(189, JAVA_LANG_CLASS_INTERNAL_NAME);
                methodVisitor.visitMethodInsn(182, JAVA_LANG_CLASS_INTERNAL_NAME, GET_DECLARED_CONSTRUCTOR_METHOD_NAME, GET_DECLARED_CONSTRUCTOR_METHOD_DESCRIPTOR, false);
                methodVisitor.visitMethodInsn(182, REFLECTION_FACTORY_INTERNAL_NAME, NEW_CONSTRUCTOR_FOR_SERIALIZATION_METHOD_NAME, NEW_CONSTRUCTOR_FOR_SERIALIZATION_METHOD_DESCRIPTOR, false);
                methodVisitor.visitInsn(3);
                methodVisitor.visitTypeInsn(189, JAVA_LANG_OBJECT_INTERNAL_NAME);
                methodVisitor.visitMethodInsn(182, JAVA_LANG_CONSTRUCTOR_INTERNAL_NAME, NEW_INSTANCE_METHOD_NAME, NEW_INSTANCE_METHOD_DESCRIPTOR, false);
                methodVisitor.visitTypeInsn(192, this.instrumentedType.getInternalName());
                methodVisitor.visitInsn(176);
                return new ByteCodeAppender.Size(4, 0);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$ForSuperMethodByConstructor.class */
    public static class ForSuperMethodByConstructor implements StackManipulation {
        private final TypeDescription proxiedType;
        private final Implementation.Target implementationTarget;
        private final List<TypeDescription> constructorParameters;
        private final boolean ignoreFinalizer;
        private final boolean serializableProxy;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.ignoreFinalizer == ((ForSuperMethodByConstructor) obj).ignoreFinalizer && this.serializableProxy == ((ForSuperMethodByConstructor) obj).serializableProxy && this.proxiedType.equals(((ForSuperMethodByConstructor) obj).proxiedType) && this.implementationTarget.equals(((ForSuperMethodByConstructor) obj).implementationTarget) && this.constructorParameters.equals(((ForSuperMethodByConstructor) obj).constructorParameters);
        }

        public int hashCode() {
            return (((((((((17 * 31) + this.proxiedType.hashCode()) * 31) + this.implementationTarget.hashCode()) * 31) + this.constructorParameters.hashCode()) * 31) + (this.ignoreFinalizer ? 1 : 0)) * 31) + (this.serializableProxy ? 1 : 0);
        }

        public ForSuperMethodByConstructor(TypeDescription proxiedType, Implementation.Target implementationTarget, List<TypeDescription> constructorParameters, boolean ignoreFinalizer, boolean serializableProxy) {
            this.proxiedType = proxiedType;
            this.implementationTarget = implementationTarget;
            this.constructorParameters = constructorParameters;
            this.ignoreFinalizer = ignoreFinalizer;
            this.serializableProxy = serializableProxy;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            TypeDescription proxyType = implementationContext.register(new TypeProxy(this.proxiedType, this.implementationTarget, InvocationFactory.Default.SUPER_METHOD, this.ignoreFinalizer, this.serializableProxy));
            StackManipulation[] constructorValue = new StackManipulation[this.constructorParameters.size()];
            int index = 0;
            for (TypeDescription parameterType : this.constructorParameters) {
                int i = index;
                index++;
                constructorValue[i] = DefaultValue.of(parameterType);
            }
            return new StackManipulation.Compound(TypeCreation.of(proxyType), Duplication.SINGLE, new StackManipulation.Compound(constructorValue), MethodInvocation.invoke((MethodDescription.InDefinedShape) proxyType.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(this.constructorParameters))).getOnly()), Duplication.SINGLE, MethodVariableAccess.loadThis(), FieldAccess.forField((FieldDescription.InDefinedShape) proxyType.getDeclaredFields().filter(ElementMatchers.named(TypeProxy.INSTANCE_FIELD)).getOnly()).write()).apply(methodVisitor, implementationContext);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$ForSuperMethodByReflectionFactory.class */
    public static class ForSuperMethodByReflectionFactory implements StackManipulation {
        private final TypeDescription proxiedType;
        private final Implementation.Target implementationTarget;
        private final boolean ignoreFinalizer;
        private final boolean serializableProxy;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.ignoreFinalizer == ((ForSuperMethodByReflectionFactory) obj).ignoreFinalizer && this.serializableProxy == ((ForSuperMethodByReflectionFactory) obj).serializableProxy && this.proxiedType.equals(((ForSuperMethodByReflectionFactory) obj).proxiedType) && this.implementationTarget.equals(((ForSuperMethodByReflectionFactory) obj).implementationTarget);
        }

        public int hashCode() {
            return (((((((17 * 31) + this.proxiedType.hashCode()) * 31) + this.implementationTarget.hashCode()) * 31) + (this.ignoreFinalizer ? 1 : 0)) * 31) + (this.serializableProxy ? 1 : 0);
        }

        public ForSuperMethodByReflectionFactory(TypeDescription proxiedType, Implementation.Target implementationTarget, boolean ignoreFinalizer, boolean serializableProxy) {
            this.proxiedType = proxiedType;
            this.implementationTarget = implementationTarget;
            this.ignoreFinalizer = ignoreFinalizer;
            this.serializableProxy = serializableProxy;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            TypeDescription proxyType = implementationContext.register(new TypeProxy(this.proxiedType, this.implementationTarget, InvocationFactory.Default.SUPER_METHOD, this.ignoreFinalizer, this.serializableProxy));
            return new StackManipulation.Compound(MethodInvocation.invoke((MethodDescription.InDefinedShape) proxyType.getDeclaredMethods().filter(ElementMatchers.named(TypeProxy.REFLECTION_METHOD).and(ElementMatchers.takesArguments(0))).getOnly()), Duplication.SINGLE, MethodVariableAccess.loadThis(), FieldAccess.forField((FieldDescription.InDefinedShape) proxyType.getDeclaredFields().filter(ElementMatchers.named(TypeProxy.INSTANCE_FIELD)).getOnly()).write()).apply(methodVisitor, implementationContext);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$ForDefaultMethod.class */
    public static class ForDefaultMethod implements StackManipulation {
        private final TypeDescription proxiedType;
        private final Implementation.Target implementationTarget;
        private final boolean serializableProxy;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.serializableProxy == ((ForDefaultMethod) obj).serializableProxy && this.proxiedType.equals(((ForDefaultMethod) obj).proxiedType) && this.implementationTarget.equals(((ForDefaultMethod) obj).implementationTarget);
        }

        public int hashCode() {
            return (((((17 * 31) + this.proxiedType.hashCode()) * 31) + this.implementationTarget.hashCode()) * 31) + (this.serializableProxy ? 1 : 0);
        }

        public ForDefaultMethod(TypeDescription proxiedType, Implementation.Target implementationTarget, boolean serializableProxy) {
            this.proxiedType = proxiedType;
            this.implementationTarget = implementationTarget;
            this.serializableProxy = serializableProxy;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            TypeDescription proxyType = implementationContext.register(new TypeProxy(this.proxiedType, this.implementationTarget, InvocationFactory.Default.DEFAULT_METHOD, true, this.serializableProxy));
            return new StackManipulation.Compound(TypeCreation.of(proxyType), Duplication.SINGLE, MethodInvocation.invoke((MethodDescription.InDefinedShape) proxyType.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly()), Duplication.SINGLE, MethodVariableAccess.loadThis(), FieldAccess.forField((FieldDescription.InDefinedShape) proxyType.getDeclaredFields().filter(ElementMatchers.named(TypeProxy.INSTANCE_FIELD)).getOnly()).write()).apply(methodVisitor, implementationContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$MethodCall.class */
    public class MethodCall implements Implementation {
        private final MethodAccessorFactory methodAccessorFactory;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.methodAccessorFactory.equals(((MethodCall) obj).methodAccessorFactory) && TypeProxy.this.equals(TypeProxy.this);
        }

        public int hashCode() {
            return (((17 * 31) + this.methodAccessorFactory.hashCode()) * 31) + TypeProxy.this.hashCode();
        }

        protected MethodCall(MethodAccessorFactory methodAccessorFactory) {
            this.methodAccessorFactory = methodAccessorFactory;
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType.withField(new FieldDescription.Token(TypeProxy.INSTANCE_FIELD, 65, TypeProxy.this.implementationTarget.getInstrumentedType().asGenericType()));
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getInstrumentedType());
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$MethodCall$Appender.class */
        protected class Appender implements ByteCodeAppender {
            private final StackManipulation fieldLoadingInstruction;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldLoadingInstruction.equals(((Appender) obj).fieldLoadingInstruction) && MethodCall.this.equals(MethodCall.this);
            }

            public int hashCode() {
                return (((17 * 31) + this.fieldLoadingInstruction.hashCode()) * 31) + MethodCall.this.hashCode();
            }

            protected Appender(TypeDescription instrumentedType) {
                this.fieldLoadingInstruction = FieldAccess.forField((FieldDescription.InDefinedShape) instrumentedType.getDeclaredFields().filter(ElementMatchers.named(TypeProxy.INSTANCE_FIELD)).getOnly()).read();
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                Implementation.SpecialMethodInvocation specialMethodInvocation = TypeProxy.this.invocationFactory.invoke(TypeProxy.this.implementationTarget, TypeProxy.this.proxiedType, instrumentedMethod);
                StackManipulation.Size size = (specialMethodInvocation.isValid() ? new AccessorMethodInvocation(instrumentedMethod, specialMethodInvocation) : AbstractMethodErrorThrow.INSTANCE).apply(methodVisitor, implementationContext);
                return new ByteCodeAppender.Size(size.getMaximalSize(), instrumentedMethod.getStackSize());
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TypeProxy$MethodCall$Appender$AccessorMethodInvocation.class */
            protected class AccessorMethodInvocation implements StackManipulation {
                private final MethodDescription instrumentedMethod;
                private final Implementation.SpecialMethodInvocation specialMethodInvocation;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.instrumentedMethod.equals(((AccessorMethodInvocation) obj).instrumentedMethod) && this.specialMethodInvocation.equals(((AccessorMethodInvocation) obj).specialMethodInvocation) && Appender.this.equals(Appender.this);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.instrumentedMethod.hashCode()) * 31) + this.specialMethodInvocation.hashCode()) * 31) + Appender.this.hashCode();
                }

                protected AccessorMethodInvocation(MethodDescription instrumentedMethod, Implementation.SpecialMethodInvocation specialMethodInvocation) {
                    this.instrumentedMethod = instrumentedMethod;
                    this.specialMethodInvocation = specialMethodInvocation;
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public boolean isValid() {
                    return this.specialMethodInvocation.isValid();
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                    MethodDescription.InDefinedShape proxyMethod = MethodCall.this.methodAccessorFactory.registerAccessorFor(this.specialMethodInvocation, MethodAccessorFactory.AccessType.DEFAULT);
                    return new StackManipulation.Compound(MethodVariableAccess.loadThis(), Appender.this.fieldLoadingInstruction, MethodVariableAccess.allArgumentsOf(this.instrumentedMethod).asBridgeOf(proxyMethod), MethodInvocation.invoke(proxyMethod), MethodReturn.of(this.instrumentedMethod.getReturnType())).apply(methodVisitor, implementationContext);
                }
            }
        }
    }
}
