package net.bytebuddy.implementation.bind.annotation;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.ExceptionMethod;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodAccessorFactory;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy.class */
public @interface FieldProxy {
    boolean serializableProxy() default false;

    String value() default "";

    Class<?> declaringType() default void.class;

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder.class */
    public static class Binder extends TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding<FieldProxy> {
        private static final MethodDescription.InDefinedShape DECLARING_TYPE;
        private static final MethodDescription.InDefinedShape FIELD_NAME;
        private static final MethodDescription.InDefinedShape SERIALIZABLE_PROXY;
        private final FieldResolver.Factory fieldResolverFactory;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.fieldResolverFactory.equals(((Binder) obj).fieldResolverFactory);
        }

        public int hashCode() {
            return (17 * 31) + this.fieldResolverFactory.hashCode();
        }

        static {
            MethodList<MethodDescription.InDefinedShape> methodList = TypeDescription.ForLoadedType.of(FieldProxy.class).getDeclaredMethods();
            DECLARING_TYPE = (MethodDescription.InDefinedShape) methodList.filter(ElementMatchers.named("declaringType")).getOnly();
            FIELD_NAME = (MethodDescription.InDefinedShape) methodList.filter(ElementMatchers.named("value")).getOnly();
            SERIALIZABLE_PROXY = (MethodDescription.InDefinedShape) methodList.filter(ElementMatchers.named("serializableProxy")).getOnly();
        }

        public static TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldProxy> install(Class<?> type) {
            return install(TypeDescription.ForLoadedType.of(type));
        }

        public static TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldProxy> install(TypeDescription typeDescription) {
            if (!typeDescription.isInterface()) {
                throw new IllegalArgumentException(typeDescription + " is not an interface");
            }
            if (!typeDescription.getInterfaces().isEmpty()) {
                throw new IllegalArgumentException(typeDescription + " must not extend other interfaces");
            }
            if (!typeDescription.isPublic()) {
                throw new IllegalArgumentException(typeDescription + " is not public");
            }
            MethodList methodCandidates = typeDescription.getDeclaredMethods().filter(ElementMatchers.isAbstract());
            if (methodCandidates.size() != 2) {
                throw new IllegalArgumentException(typeDescription + " does not declare exactly two non-abstract methods");
            }
            MethodList getterCandidates = methodCandidates.filter(ElementMatchers.isGetter(Object.class));
            if (getterCandidates.size() != 1) {
                throw new IllegalArgumentException(typeDescription + " does not declare a getter with an Object type");
            }
            MethodList setterCandidates = methodCandidates.filter(ElementMatchers.isSetter(Object.class));
            if (setterCandidates.size() != 1) {
                throw new IllegalArgumentException(typeDescription + " does not declare a setter with an Object type");
            }
            return new Binder(typeDescription, (MethodDescription.InDefinedShape) getterCandidates.getOnly(), (MethodDescription.InDefinedShape) setterCandidates.getOnly());
        }

        public static TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldProxy> install(Class<?> getterType, Class<?> setterType) {
            return install(TypeDescription.ForLoadedType.of(getterType), TypeDescription.ForLoadedType.of(setterType));
        }

        public static TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldProxy> install(TypeDescription getterType, TypeDescription setterType) {
            MethodDescription.InDefinedShape getterMethod = onlyMethod(getterType);
            if (!getterMethod.getReturnType().asErasure().represents(Object.class)) {
                throw new IllegalArgumentException(getterMethod + " must take a single Object-typed parameter");
            }
            if (getterMethod.getParameters().size() != 0) {
                throw new IllegalArgumentException(getterMethod + " must not declare parameters");
            }
            MethodDescription.InDefinedShape setterMethod = onlyMethod(setterType);
            if (!setterMethod.getReturnType().asErasure().represents(Void.TYPE)) {
                throw new IllegalArgumentException(setterMethod + " must return void");
            }
            if (setterMethod.getParameters().size() != 1 || !((ParameterDescription.InDefinedShape) setterMethod.getParameters().get(0)).getType().asErasure().represents(Object.class)) {
                throw new IllegalArgumentException(setterMethod + " must declare a single Object-typed parameters");
            }
            return new Binder(getterMethod, setterMethod);
        }

        private static MethodDescription.InDefinedShape onlyMethod(TypeDescription typeDescription) {
            if (!typeDescription.isInterface()) {
                throw new IllegalArgumentException(typeDescription + " is not an interface");
            }
            if (!typeDescription.getInterfaces().isEmpty()) {
                throw new IllegalArgumentException(typeDescription + " must not extend other interfaces");
            }
            if (!typeDescription.isPublic()) {
                throw new IllegalArgumentException(typeDescription + " is not public");
            }
            MethodList methodCandidates = typeDescription.getDeclaredMethods().filter(ElementMatchers.isAbstract());
            if (methodCandidates.size() != 1) {
                throw new IllegalArgumentException(typeDescription + " must declare exactly one abstract method");
            }
            return (MethodDescription.InDefinedShape) methodCandidates.getOnly();
        }

        protected Binder(MethodDescription.InDefinedShape getterMethod, MethodDescription.InDefinedShape setterMethod) {
            this(new FieldResolver.Factory.Simplex(getterMethod, setterMethod));
        }

        protected Binder(TypeDescription proxyType, MethodDescription.InDefinedShape getterMethod, MethodDescription.InDefinedShape setterMethod) {
            this(new FieldResolver.Factory.Duplex(proxyType, getterMethod, setterMethod));
        }

        protected Binder(FieldResolver.Factory fieldResolverFactory) {
            this.fieldResolverFactory = fieldResolverFactory;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public Class<FieldProxy> getHandledType() {
            return FieldProxy.class;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding
        protected String fieldName(AnnotationDescription.Loadable<FieldProxy> annotation) {
            return (String) annotation.getValue(FIELD_NAME).resolve(String.class);
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding
        protected TypeDescription declaringType(AnnotationDescription.Loadable<FieldProxy> annotation) {
            return (TypeDescription) annotation.getValue(DECLARING_TYPE).resolve(TypeDescription.class);
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding
        protected MethodDelegationBinder.ParameterBinding<?> bind(FieldDescription fieldDescription, AnnotationDescription.Loadable<FieldProxy> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner) {
            FieldResolver fieldResolver = this.fieldResolverFactory.resolve(target.getType().asErasure(), fieldDescription);
            if (fieldResolver.isResolved()) {
                return new MethodDelegationBinder.ParameterBinding.Anonymous(new AccessorProxy(fieldDescription, implementationTarget.getInstrumentedType(), fieldResolver, assigner, ((Boolean) annotation.getValue(SERIALIZABLE_PROXY).resolve(Boolean.class)).booleanValue()));
            }
            return MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver.class */
        protected interface FieldResolver {
            boolean isResolved();

            TypeDescription getProxyType();

            DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$Factory.class */
            public interface Factory {
                FieldResolver resolve(TypeDescription typeDescription, FieldDescription fieldDescription);

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$Factory$Duplex.class */
                public static class Duplex implements Factory {
                    private final TypeDescription proxyType;
                    private final MethodDescription.InDefinedShape getterMethod;
                    private final MethodDescription.InDefinedShape setterMethod;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.proxyType.equals(((Duplex) obj).proxyType) && this.getterMethod.equals(((Duplex) obj).getterMethod) && this.setterMethod.equals(((Duplex) obj).setterMethod);
                    }

                    public int hashCode() {
                        return (((((17 * 31) + this.proxyType.hashCode()) * 31) + this.getterMethod.hashCode()) * 31) + this.setterMethod.hashCode();
                    }

                    protected Duplex(TypeDescription proxyType, MethodDescription.InDefinedShape getterMethod, MethodDescription.InDefinedShape setterMethod) {
                        this.proxyType = proxyType;
                        this.getterMethod = getterMethod;
                        this.setterMethod = setterMethod;
                    }

                    @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver.Factory
                    public FieldResolver resolve(TypeDescription parameterType, FieldDescription fieldDescription) {
                        if (parameterType.equals(this.proxyType)) {
                            return new ForGetterSetterPair(this.proxyType, this.getterMethod, this.setterMethod);
                        }
                        throw new IllegalStateException("Cannot use @FieldProxy on a non-installed type");
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$Factory$Simplex.class */
                public static class Simplex implements Factory {
                    private final MethodDescription.InDefinedShape getterMethod;
                    private final MethodDescription.InDefinedShape setterMethod;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.getterMethod.equals(((Simplex) obj).getterMethod) && this.setterMethod.equals(((Simplex) obj).setterMethod);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.getterMethod.hashCode()) * 31) + this.setterMethod.hashCode();
                    }

                    protected Simplex(MethodDescription.InDefinedShape getterMethod, MethodDescription.InDefinedShape setterMethod) {
                        this.getterMethod = getterMethod;
                        this.setterMethod = setterMethod;
                    }

                    @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver.Factory
                    public FieldResolver resolve(TypeDescription parameterType, FieldDescription fieldDescription) {
                        if (parameterType.equals(this.getterMethod.getDeclaringType())) {
                            return new ForGetter(this.getterMethod);
                        }
                        if (parameterType.equals(this.setterMethod.getDeclaringType())) {
                            return fieldDescription.isFinal() ? Unresolved.INSTANCE : new ForSetter(this.setterMethod);
                        }
                        throw new IllegalStateException("Cannot use @FieldProxy on a non-installed type");
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$Unresolved.class */
            public enum Unresolved implements FieldResolver {
                INSTANCE;

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public boolean isResolved() {
                    return false;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public TypeDescription getProxyType() {
                    throw new IllegalStateException("Cannot read type for unresolved field resolver");
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory) {
                    throw new IllegalStateException("Cannot apply unresolved field resolver");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$ForGetter.class */
            public static class ForGetter implements FieldResolver {
                private final MethodDescription.InDefinedShape getterMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.getterMethod.equals(((ForGetter) obj).getterMethod);
                }

                public int hashCode() {
                    return (17 * 31) + this.getterMethod.hashCode();
                }

                protected ForGetter(MethodDescription.InDefinedShape getterMethod) {
                    this.getterMethod = getterMethod;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public boolean isResolved() {
                    return true;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public TypeDescription getProxyType() {
                    return this.getterMethod.getDeclaringType();
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory) {
                    return builder.method(ElementMatchers.definedMethod(ElementMatchers.is(this.getterMethod))).intercept(new FieldGetter(fieldDescription, assigner, methodAccessorFactory));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$ForSetter.class */
            public static class ForSetter implements FieldResolver {
                private final MethodDescription.InDefinedShape setterMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.setterMethod.equals(((ForSetter) obj).setterMethod);
                }

                public int hashCode() {
                    return (17 * 31) + this.setterMethod.hashCode();
                }

                protected ForSetter(MethodDescription.InDefinedShape setterMethod) {
                    this.setterMethod = setterMethod;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public boolean isResolved() {
                    return true;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public TypeDescription getProxyType() {
                    return this.setterMethod.getDeclaringType();
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory) {
                    return builder.method(ElementMatchers.is(this.setterMethod)).intercept(new FieldSetter(fieldDescription, assigner, methodAccessorFactory));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldResolver$ForGetterSetterPair.class */
            public static class ForGetterSetterPair implements FieldResolver {
                private final TypeDescription proxyType;
                private final MethodDescription.InDefinedShape getterMethod;
                private final MethodDescription.InDefinedShape setterMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.proxyType.equals(((ForGetterSetterPair) obj).proxyType) && this.getterMethod.equals(((ForGetterSetterPair) obj).getterMethod) && this.setterMethod.equals(((ForGetterSetterPair) obj).setterMethod);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.proxyType.hashCode()) * 31) + this.getterMethod.hashCode()) * 31) + this.setterMethod.hashCode();
                }

                protected ForGetterSetterPair(TypeDescription proxyType, MethodDescription.InDefinedShape getterMethod, MethodDescription.InDefinedShape setterMethod) {
                    this.proxyType = proxyType;
                    this.getterMethod = getterMethod;
                    this.setterMethod = setterMethod;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public boolean isResolved() {
                    return true;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public TypeDescription getProxyType() {
                    return this.proxyType;
                }

                @Override // net.bytebuddy.implementation.bind.annotation.FieldProxy.Binder.FieldResolver
                public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory) {
                    return builder.method(ElementMatchers.is(this.getterMethod)).intercept(new FieldGetter(fieldDescription, assigner, methodAccessorFactory)).method(ElementMatchers.is(this.setterMethod)).intercept(fieldDescription.isFinal() ? ExceptionMethod.throwing(UnsupportedOperationException.class, "Cannot set final field " + fieldDescription) : new FieldSetter(fieldDescription, assigner, methodAccessorFactory));
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$StaticFieldConstructor.class */
        protected enum StaticFieldConstructor implements Implementation {
            INSTANCE;
            
            private final MethodDescription objectTypeDefaultConstructor = (MethodDescription) TypeDescription.OBJECT.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly();

            StaticFieldConstructor() {
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.Implementation
            public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                return new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(), MethodInvocation.invoke(this.objectTypeDefaultConstructor), MethodReturn.VOID);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$InstanceFieldConstructor.class */
        protected static class InstanceFieldConstructor implements Implementation {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((InstanceFieldConstructor) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected InstanceFieldConstructor(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType.withField(new FieldDescription.Token("instance", 18, this.instrumentedType.asGenericType()));
            }

            @Override // net.bytebuddy.implementation.Implementation
            public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                return new Appender(implementationTarget);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$InstanceFieldConstructor$Appender.class */
            protected static class Appender implements ByteCodeAppender {
                private final FieldDescription fieldDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((Appender) obj).fieldDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.fieldDescription.hashCode();
                }

                protected Appender(Implementation.Target implementationTarget) {
                    this.fieldDescription = (FieldDescription) implementationTarget.getInstrumentedType().getDeclaredFields().filter(ElementMatchers.named("instance")).getOnly();
                }

                @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                    StackManipulation.Size stackSize = new StackManipulation.Compound(MethodVariableAccess.loadThis(), MethodInvocation.invoke(StaticFieldConstructor.INSTANCE.objectTypeDefaultConstructor), MethodVariableAccess.allArgumentsOf(instrumentedMethod.asDefined()).prependThisReference(), FieldAccess.forField(this.fieldDescription).write(), MethodReturn.VOID).apply(methodVisitor, implementationContext);
                    return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldGetter.class */
        protected static class FieldGetter implements Implementation {
            private final FieldDescription fieldDescription;
            private final Assigner assigner;
            private final MethodAccessorFactory methodAccessorFactory;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((FieldGetter) obj).fieldDescription) && this.assigner.equals(((FieldGetter) obj).assigner) && this.methodAccessorFactory.equals(((FieldGetter) obj).methodAccessorFactory);
            }

            public int hashCode() {
                return (((((17 * 31) + this.fieldDescription.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.methodAccessorFactory.hashCode();
            }

            protected FieldGetter(FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory) {
                this.fieldDescription = fieldDescription;
                this.assigner = assigner;
                this.methodAccessorFactory = methodAccessorFactory;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.Implementation
            public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                return new Appender(implementationTarget);
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldGetter$Appender.class */
            protected class Appender implements ByteCodeAppender {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((Appender) obj).typeDescription) && FieldGetter.this.equals(FieldGetter.this);
                }

                public int hashCode() {
                    return (((17 * 31) + this.typeDescription.hashCode()) * 31) + FieldGetter.this.hashCode();
                }

                protected Appender(Implementation.Target implementationTarget) {
                    this.typeDescription = implementationTarget.getInstrumentedType();
                }

                @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                    MethodDescription getterMethod = FieldGetter.this.methodAccessorFactory.registerGetterFor(FieldGetter.this.fieldDescription, MethodAccessorFactory.AccessType.DEFAULT);
                    StackManipulation[] stackManipulationArr = new StackManipulation[4];
                    stackManipulationArr[0] = FieldGetter.this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : new StackManipulation.Compound(MethodVariableAccess.loadThis(), FieldAccess.forField((FieldDescription.InDefinedShape) this.typeDescription.getDeclaredFields().filter(ElementMatchers.named("instance")).getOnly()).read());
                    stackManipulationArr[1] = MethodInvocation.invoke(getterMethod);
                    stackManipulationArr[2] = FieldGetter.this.assigner.assign(getterMethod.getReturnType(), instrumentedMethod.getReturnType(), Assigner.Typing.DYNAMIC);
                    stackManipulationArr[3] = MethodReturn.of(instrumentedMethod.getReturnType().asErasure());
                    StackManipulation.Size stackSize = new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext);
                    return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldSetter.class */
        protected static class FieldSetter implements Implementation {
            private final FieldDescription fieldDescription;
            private final Assigner assigner;
            private final MethodAccessorFactory methodAccessorFactory;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((FieldSetter) obj).fieldDescription) && this.assigner.equals(((FieldSetter) obj).assigner) && this.methodAccessorFactory.equals(((FieldSetter) obj).methodAccessorFactory);
            }

            public int hashCode() {
                return (((((17 * 31) + this.fieldDescription.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.methodAccessorFactory.hashCode();
            }

            protected FieldSetter(FieldDescription fieldDescription, Assigner assigner, MethodAccessorFactory methodAccessorFactory) {
                this.fieldDescription = fieldDescription;
                this.assigner = assigner;
                this.methodAccessorFactory = methodAccessorFactory;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.implementation.Implementation
            public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                return new Appender(implementationTarget);
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$FieldSetter$Appender.class */
            protected class Appender implements ByteCodeAppender {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((Appender) obj).typeDescription) && FieldSetter.this.equals(FieldSetter.this);
                }

                public int hashCode() {
                    return (((17 * 31) + this.typeDescription.hashCode()) * 31) + FieldSetter.this.hashCode();
                }

                protected Appender(Implementation.Target implementationTarget) {
                    this.typeDescription = implementationTarget.getInstrumentedType();
                }

                @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                    TypeDescription.Generic parameterType = ((ParameterDescription) instrumentedMethod.getParameters().get(0)).getType();
                    MethodDescription setterMethod = FieldSetter.this.methodAccessorFactory.registerSetterFor(FieldSetter.this.fieldDescription, MethodAccessorFactory.AccessType.DEFAULT);
                    StackManipulation[] stackManipulationArr = new StackManipulation[5];
                    stackManipulationArr[0] = FieldSetter.this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : new StackManipulation.Compound(MethodVariableAccess.loadThis(), FieldAccess.forField((FieldDescription.InDefinedShape) this.typeDescription.getDeclaredFields().filter(ElementMatchers.named("instance")).getOnly()).read());
                    stackManipulationArr[1] = MethodVariableAccess.of(parameterType).loadFrom(1);
                    stackManipulationArr[2] = FieldSetter.this.assigner.assign(parameterType, ((ParameterDescription) setterMethod.getParameters().get(0)).getType(), Assigner.Typing.DYNAMIC);
                    stackManipulationArr[3] = MethodInvocation.invoke(setterMethod);
                    stackManipulationArr[4] = MethodReturn.VOID;
                    StackManipulation.Size stackSize = new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext);
                    return new ByteCodeAppender.Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldProxy$Binder$AccessorProxy.class */
        protected class AccessorProxy implements AuxiliaryType, StackManipulation {
            protected static final String FIELD_NAME = "instance";
            private final FieldDescription fieldDescription;
            private final TypeDescription instrumentedType;
            private final FieldResolver fieldResolver;
            private final Assigner assigner;
            private final boolean serializableProxy;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.serializableProxy == ((AccessorProxy) obj).serializableProxy && this.fieldDescription.equals(((AccessorProxy) obj).fieldDescription) && this.instrumentedType.equals(((AccessorProxy) obj).instrumentedType) && this.fieldResolver.equals(((AccessorProxy) obj).fieldResolver) && this.assigner.equals(((AccessorProxy) obj).assigner) && Binder.this.equals(Binder.this);
            }

            public int hashCode() {
                return (((((((((((17 * 31) + this.fieldDescription.hashCode()) * 31) + this.instrumentedType.hashCode()) * 31) + this.fieldResolver.hashCode()) * 31) + this.assigner.hashCode()) * 31) + (this.serializableProxy ? 1 : 0)) * 31) + Binder.this.hashCode();
            }

            protected AccessorProxy(FieldDescription fieldDescription, TypeDescription instrumentedType, FieldResolver fieldResolver, Assigner assigner, boolean serializableProxy) {
                this.fieldDescription = fieldDescription;
                this.instrumentedType = instrumentedType;
                this.fieldResolver = fieldResolver;
                this.assigner = assigner;
                this.serializableProxy = serializableProxy;
            }

            @Override // net.bytebuddy.implementation.auxiliary.AuxiliaryType
            public DynamicType make(String auxiliaryTypeName, ClassFileVersion classFileVersion, MethodAccessorFactory methodAccessorFactory) {
                List singletonList;
                FieldResolver fieldResolver = this.fieldResolver;
                DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial<?> defineConstructor = new ByteBuddy(classFileVersion).with(TypeValidation.DISABLED).subclass(this.fieldResolver.getProxyType(), ConstructorStrategy.Default.NO_CONSTRUCTORS).name(auxiliaryTypeName).modifiers(DEFAULT_TYPE_MODIFIER).implement(this.serializableProxy ? new Class[]{Serializable.class} : new Class[0]).defineConstructor(new ModifierContributor.ForMethod[0]);
                if (this.fieldDescription.isStatic()) {
                    singletonList = Collections.emptyList();
                } else {
                    singletonList = Collections.singletonList(this.instrumentedType);
                }
                return fieldResolver.apply(defineConstructor.withParameters((Collection<? extends TypeDefinition>) singletonList).intercept(this.fieldDescription.isStatic() ? StaticFieldConstructor.INSTANCE : new InstanceFieldConstructor(this.instrumentedType)), this.fieldDescription, this.assigner, methodAccessorFactory).make();
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return true;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                TypeDescription auxiliaryType = implementationContext.register(this);
                StackManipulation[] stackManipulationArr = new StackManipulation[4];
                stackManipulationArr[0] = TypeCreation.of(auxiliaryType);
                stackManipulationArr[1] = Duplication.SINGLE;
                stackManipulationArr[2] = this.fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                stackManipulationArr[3] = MethodInvocation.invoke((MethodDescription.InDefinedShape) auxiliaryType.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly());
                return new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext);
            }
        }
    }
}
