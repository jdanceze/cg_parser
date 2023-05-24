package net.bytebuddy;

import android.view.InputDevice;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.EnumerationState;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.VisibilityBridgeStrategy;
import net.bytebuddy.dynamic.scaffold.ClassWriterStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.inline.DecoratingDynamicTypeBuilder;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import net.bytebuddy.dynamic.scaffold.inline.RebaseDynamicTypeBuilder;
import net.bytebuddy.dynamic.scaffold.inline.RedefinitionDynamicTypeBuilder;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.SubclassDynamicTypeBuilder;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy.class */
public class ByteBuddy {
    private static final String BYTE_BUDDY_DEFAULT_PREFIX = "ByteBuddy";
    private static final String BYTE_BUDDY_DEFAULT_SUFFIX = "auxiliary";
    protected final ClassFileVersion classFileVersion;
    protected final NamingStrategy namingStrategy;
    protected final AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy;
    protected final AnnotationValueFilter.Factory annotationValueFilterFactory;
    protected final AnnotationRetention annotationRetention;
    protected final Implementation.Context.Factory implementationContextFactory;
    protected final MethodGraph.Compiler methodGraphCompiler;
    protected final InstrumentedType.Factory instrumentedTypeFactory;
    protected final LatentMatcher<? super MethodDescription> ignoredMethods;
    protected final TypeValidation typeValidation;
    protected final VisibilityBridgeStrategy visibilityBridgeStrategy;
    protected final ClassWriterStrategy classWriterStrategy;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.annotationRetention.equals(((ByteBuddy) obj).annotationRetention) && this.typeValidation.equals(((ByteBuddy) obj).typeValidation) && this.classFileVersion.equals(((ByteBuddy) obj).classFileVersion) && this.namingStrategy.equals(((ByteBuddy) obj).namingStrategy) && this.auxiliaryTypeNamingStrategy.equals(((ByteBuddy) obj).auxiliaryTypeNamingStrategy) && this.annotationValueFilterFactory.equals(((ByteBuddy) obj).annotationValueFilterFactory) && this.implementationContextFactory.equals(((ByteBuddy) obj).implementationContextFactory) && this.methodGraphCompiler.equals(((ByteBuddy) obj).methodGraphCompiler) && this.instrumentedTypeFactory.equals(((ByteBuddy) obj).instrumentedTypeFactory) && this.ignoredMethods.equals(((ByteBuddy) obj).ignoredMethods) && this.visibilityBridgeStrategy.equals(((ByteBuddy) obj).visibilityBridgeStrategy) && this.classWriterStrategy.equals(((ByteBuddy) obj).classWriterStrategy);
    }

    public int hashCode() {
        return (((((((((((((((((((((((17 * 31) + this.classFileVersion.hashCode()) * 31) + this.namingStrategy.hashCode()) * 31) + this.auxiliaryTypeNamingStrategy.hashCode()) * 31) + this.annotationValueFilterFactory.hashCode()) * 31) + this.annotationRetention.hashCode()) * 31) + this.implementationContextFactory.hashCode()) * 31) + this.methodGraphCompiler.hashCode()) * 31) + this.instrumentedTypeFactory.hashCode()) * 31) + this.ignoredMethods.hashCode()) * 31) + this.typeValidation.hashCode()) * 31) + this.visibilityBridgeStrategy.hashCode()) * 31) + this.classWriterStrategy.hashCode();
    }

    public ByteBuddy() {
        this(ClassFileVersion.ofThisVm(ClassFileVersion.JAVA_V6));
    }

    public ByteBuddy(ClassFileVersion classFileVersion) {
        this(classFileVersion, new NamingStrategy.SuffixingRandom(BYTE_BUDDY_DEFAULT_PREFIX), new AuxiliaryType.NamingStrategy.SuffixingRandom(BYTE_BUDDY_DEFAULT_SUFFIX), AnnotationValueFilter.Default.APPEND_DEFAULTS, AnnotationRetention.ENABLED, Implementation.Context.Default.Factory.INSTANCE, MethodGraph.Compiler.DEFAULT, InstrumentedType.Factory.Default.MODIFIABLE, TypeValidation.ENABLED, VisibilityBridgeStrategy.Default.ALWAYS, ClassWriterStrategy.Default.CONSTANT_POOL_RETAINING, new LatentMatcher.Resolved(ElementMatchers.isSynthetic().or(ElementMatchers.isDefaultFinalizer())));
    }

    protected ByteBuddy(ClassFileVersion classFileVersion, NamingStrategy namingStrategy, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, InstrumentedType.Factory instrumentedTypeFactory, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods) {
        this.classFileVersion = classFileVersion;
        this.namingStrategy = namingStrategy;
        this.auxiliaryTypeNamingStrategy = auxiliaryTypeNamingStrategy;
        this.annotationValueFilterFactory = annotationValueFilterFactory;
        this.annotationRetention = annotationRetention;
        this.implementationContextFactory = implementationContextFactory;
        this.methodGraphCompiler = methodGraphCompiler;
        this.instrumentedTypeFactory = instrumentedTypeFactory;
        this.typeValidation = typeValidation;
        this.visibilityBridgeStrategy = visibilityBridgeStrategy;
        this.classWriterStrategy = classWriterStrategy;
        this.ignoredMethods = ignoredMethods;
    }

    public <T> DynamicType.Builder<T> subclass(Class<T> superType) {
        return (DynamicType.Builder<T>) subclass(TypeDescription.ForLoadedType.of(superType));
    }

    public <T> DynamicType.Builder<T> subclass(Class<T> superType, ConstructorStrategy constructorStrategy) {
        return (DynamicType.Builder<T>) subclass(TypeDescription.ForLoadedType.of(superType), constructorStrategy);
    }

    public DynamicType.Builder<?> subclass(Type superType) {
        return subclass(TypeDefinition.Sort.describe(superType));
    }

    public DynamicType.Builder<?> subclass(Type superType, ConstructorStrategy constructorStrategy) {
        return subclass(TypeDefinition.Sort.describe(superType), constructorStrategy);
    }

    public DynamicType.Builder<?> subclass(TypeDefinition superType) {
        return subclass(superType, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING);
    }

    public DynamicType.Builder<?> subclass(TypeDefinition superType, ConstructorStrategy constructorStrategy) {
        TypeDescription.Generic actualSuperType;
        TypeList.Generic interfaceTypes;
        if (superType.isPrimitive() || superType.isArray() || superType.isFinal()) {
            throw new IllegalArgumentException("Cannot subclass primitive, array or final types: " + superType);
        }
        if (superType.isInterface()) {
            actualSuperType = TypeDescription.Generic.OBJECT;
            interfaceTypes = new TypeList.Generic.Explicit(superType);
        } else {
            actualSuperType = superType.asGenericType();
            interfaceTypes = new TypeList.Generic.Empty();
        }
        return new SubclassDynamicTypeBuilder(this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(superType.asGenericType()), ModifierContributor.Resolver.of(Visibility.PUBLIC, TypeManifestation.PLAIN).resolve(superType.getModifiers()), actualSuperType).withInterfaces(interfaceTypes), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, constructorStrategy);
    }

    public DynamicType.Builder<?> makeInterface() {
        return makeInterface((Collection<? extends TypeDefinition>) Collections.emptyList());
    }

    public <T> DynamicType.Builder<T> makeInterface(Class<T> interfaceType) {
        return (DynamicType.Builder<T>) makeInterface(Collections.singletonList(interfaceType));
    }

    public DynamicType.Builder<?> makeInterface(Type... interfaceType) {
        return makeInterface(Arrays.asList(interfaceType));
    }

    public DynamicType.Builder<?> makeInterface(List<? extends Type> interfaceTypes) {
        return makeInterface((Collection<? extends TypeDefinition>) new TypeList.Generic.ForLoadedTypes(interfaceTypes));
    }

    public DynamicType.Builder<?> makeInterface(TypeDefinition... interfaceType) {
        return makeInterface((Collection<? extends TypeDefinition>) Arrays.asList(interfaceType));
    }

    public DynamicType.Builder<?> makeInterface(Collection<? extends TypeDefinition> interfaceTypes) {
        return subclass(Object.class, (ConstructorStrategy) ConstructorStrategy.Default.NO_CONSTRUCTORS).implement(interfaceTypes).modifiers(TypeManifestation.INTERFACE, Visibility.PUBLIC);
    }

    public DynamicType.Builder<?> makePackage(String name) {
        return new SubclassDynamicTypeBuilder(this.instrumentedTypeFactory.subclass(name + "." + PackageDescription.PACKAGE_CLASS_NAME, 5632, TypeDescription.Generic.OBJECT), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, ConstructorStrategy.Default.NO_CONSTRUCTORS);
    }

    public DynamicType.Builder<?> makeRecord() {
        TypeDescription.Generic record = InstrumentedType.Default.of(JavaType.RECORD.getTypeStub().getName(), TypeDescription.Generic.OBJECT, Visibility.PUBLIC).withMethod(new MethodDescription.Token(4)).withMethod(new MethodDescription.Token("hashCode", InputDevice.SOURCE_GAMEPAD, TypeDescription.ForLoadedType.of(Integer.TYPE).asGenericType())).withMethod(new MethodDescription.Token("equals", InputDevice.SOURCE_GAMEPAD, TypeDescription.ForLoadedType.of(Boolean.TYPE).asGenericType(), Collections.singletonList(TypeDescription.Generic.OBJECT))).withMethod(new MethodDescription.Token("toString", InputDevice.SOURCE_GAMEPAD, TypeDescription.ForLoadedType.of(String.class).asGenericType())).asGenericType();
        return new SubclassDynamicTypeBuilder(this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(record), 17, record).withRecord(true), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, RecordConstructorStrategy.INSTANCE).method(ElementMatchers.isHashCode()).intercept(RecordObjectMethod.HASH_CODE).method(ElementMatchers.isEquals()).intercept(RecordObjectMethod.EQUALS).method(ElementMatchers.isToString()).intercept(RecordObjectMethod.TO_STRING);
    }

    public DynamicType.Builder<? extends Annotation> makeAnnotation() {
        return new SubclassDynamicTypeBuilder(this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(TypeDescription.Generic.ANNOTATION), ModifierContributor.Resolver.of(Visibility.PUBLIC, TypeManifestation.ANNOTATION).resolve(), TypeDescription.Generic.OBJECT).withInterfaces((TypeList.Generic) new TypeList.Generic.Explicit(TypeDescription.Generic.ANNOTATION)), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, ConstructorStrategy.Default.NO_CONSTRUCTORS);
    }

    public DynamicType.Builder<? extends Enum<?>> makeEnumeration(String... value) {
        return makeEnumeration(Arrays.asList(value));
    }

    public DynamicType.Builder<? extends Enum<?>> makeEnumeration(Collection<? extends String> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Require at least one enumeration constant");
        }
        TypeDescription.Generic enumType = TypeDescription.Generic.Builder.parameterizedType(Enum.class, TargetType.class).build();
        return new SubclassDynamicTypeBuilder(this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(enumType), ModifierContributor.Resolver.of(Visibility.PUBLIC, TypeManifestation.FINAL, EnumerationState.ENUMERATION).resolve(), enumType), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, ConstructorStrategy.Default.NO_CONSTRUCTORS).defineConstructor(Visibility.PRIVATE).withParameters(String.class, Integer.TYPE).intercept(SuperMethodCall.INSTANCE).defineMethod("valueOf", TargetType.class, Visibility.PUBLIC, Ownership.STATIC).withParameters(String.class).intercept(MethodCall.invoke((MethodDescription) enumType.getDeclaredMethods().filter(ElementMatchers.named("valueOf").and(ElementMatchers.takesArguments(Class.class, String.class))).getOnly()).withOwnType().withArgument(0).withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)).defineMethod("values", TargetType[].class, Visibility.PUBLIC, Ownership.STATIC).intercept(new EnumerationImplementation(new ArrayList(values)));
    }

    public <T> DynamicType.Builder<T> redefine(Class<T> type) {
        return redefine(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
    }

    public <T> DynamicType.Builder<T> redefine(Class<T> type, ClassFileLocator classFileLocator) {
        return redefine(TypeDescription.ForLoadedType.of(type), classFileLocator);
    }

    public <T> DynamicType.Builder<T> redefine(TypeDescription type, ClassFileLocator classFileLocator) {
        if (type.isArray() || type.isPrimitive()) {
            throw new IllegalArgumentException("Cannot redefine array or primitive type: " + type);
        }
        return new RedefinitionDynamicTypeBuilder(this.instrumentedTypeFactory.represent(type), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, type, classFileLocator);
    }

    public <T> DynamicType.Builder<T> rebase(Class<T> type) {
        return rebase(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
    }

    public <T> DynamicType.Builder<T> rebase(Class<T> type, ClassFileLocator classFileLocator) {
        return rebase(TypeDescription.ForLoadedType.of(type), classFileLocator);
    }

    public <T> DynamicType.Builder<T> rebase(Class<T> type, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer) {
        return rebase(TypeDescription.ForLoadedType.of(type), classFileLocator, methodNameTransformer);
    }

    public <T> DynamicType.Builder<T> rebase(TypeDescription type, ClassFileLocator classFileLocator) {
        return rebase(type, classFileLocator, MethodNameTransformer.Suffixing.withRandomSuffix());
    }

    public <T> DynamicType.Builder<T> rebase(TypeDescription type, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer) {
        if (type.isArray() || type.isPrimitive()) {
            throw new IllegalArgumentException("Cannot rebase array or primitive type: " + type);
        }
        return new RebaseDynamicTypeBuilder(this.instrumentedTypeFactory.represent(type), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, type, classFileLocator, methodNameTransformer);
    }

    public DynamicType.Builder<?> rebase(Package aPackage, ClassFileLocator classFileLocator) {
        return rebase(new PackageDescription.ForLoadedPackage(aPackage), classFileLocator);
    }

    public DynamicType.Builder<?> rebase(PackageDescription aPackage, ClassFileLocator classFileLocator) {
        return rebase(new TypeDescription.ForPackageDescription(aPackage), classFileLocator);
    }

    public <T> DynamicType.Builder<T> decorate(Class<T> type) {
        return decorate(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
    }

    public <T> DynamicType.Builder<T> decorate(Class<T> type, ClassFileLocator classFileLocator) {
        return decorate(TypeDescription.ForLoadedType.of(type), classFileLocator);
    }

    public <T> DynamicType.Builder<T> decorate(TypeDescription type, ClassFileLocator classFileLocator) {
        if (type.isArray() || type.isPrimitive()) {
            throw new IllegalArgumentException("Cannot decorate array or primitive type: " + type);
        }
        return new DecoratingDynamicTypeBuilder(type, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.classWriterStrategy, this.ignoredMethods, classFileLocator);
    }

    public ByteBuddy with(ClassFileVersion classFileVersion) {
        return new ByteBuddy(classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(NamingStrategy namingStrategy) {
        return new ByteBuddy(this.classFileVersion, namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(AnnotationValueFilter.Factory annotationValueFilterFactory) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(AnnotationRetention annotationRetention) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(Implementation.Context.Factory implementationContextFactory) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(MethodGraph.Compiler methodGraphCompiler) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(InstrumentedType.Factory instrumentedTypeFactory) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(TypeValidation typeValidation) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(VisibilityBridgeStrategy visibilityBridgeStrategy) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy with(ClassWriterStrategy classWriterStrategy) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, classWriterStrategy, this.ignoredMethods);
    }

    public ByteBuddy ignore(ElementMatcher<? super MethodDescription> ignoredMethods) {
        return ignore(new LatentMatcher.Resolved(ignoredMethods));
    }

    public ByteBuddy ignore(LatentMatcher<? super MethodDescription> ignoredMethods) {
        return new ByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, ignoredMethods);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy$EnumerationImplementation.class */
    public static class EnumerationImplementation implements Implementation {
        protected static final String CLONE_METHOD_NAME = "clone";
        protected static final String ENUM_VALUE_OF_METHOD_NAME = "valueOf";
        protected static final String ENUM_VALUES_METHOD_NAME = "values";
        private static final int ENUM_FIELD_MODIFIERS = 25;
        private static final String ENUM_VALUES = "$VALUES";
        private final List<String> values;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.values.equals(((EnumerationImplementation) obj).values);
        }

        public int hashCode() {
            return (17 * 31) + this.values.hashCode();
        }

        protected EnumerationImplementation(List<String> values) {
            this.values = values;
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            for (String value : this.values) {
                instrumentedType = instrumentedType.withField(new FieldDescription.Token(value, 16409, TargetType.DESCRIPTION.asGenericType()));
            }
            return instrumentedType.withField(new FieldDescription.Token(ENUM_VALUES, 4121, TypeDescription.ArrayProjection.of(TargetType.DESCRIPTION).asGenericType())).withInitializer(new InitializationAppender(this.values));
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new ValuesMethodAppender(implementationTarget.getInstrumentedType());
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy$EnumerationImplementation$ValuesMethodAppender.class */
        protected static class ValuesMethodAppender implements ByteCodeAppender {
            private final TypeDescription instrumentedType;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ValuesMethodAppender) obj).instrumentedType);
            }

            public int hashCode() {
                return (17 * 31) + this.instrumentedType.hashCode();
            }

            protected ValuesMethodAppender(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                FieldDescription valuesField = (FieldDescription) this.instrumentedType.getDeclaredFields().filter(ElementMatchers.named(EnumerationImplementation.ENUM_VALUES)).getOnly();
                MethodDescription cloneMethod = (MethodDescription) TypeDescription.Generic.OBJECT.getDeclaredMethods().filter(ElementMatchers.named(EnumerationImplementation.CLONE_METHOD_NAME)).getOnly();
                return new ByteCodeAppender.Size(new StackManipulation.Compound(FieldAccess.forField(valuesField).read(), MethodInvocation.invoke(cloneMethod).virtual(valuesField.getType().asErasure()), TypeCasting.to(valuesField.getType().asErasure()), MethodReturn.REFERENCE).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy$EnumerationImplementation$InitializationAppender.class */
        protected static class InitializationAppender implements ByteCodeAppender {
            private final List<String> values;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.values.equals(((InitializationAppender) obj).values);
            }

            public int hashCode() {
                return (17 * 31) + this.values.hashCode();
            }

            protected InitializationAppender(List<String> values) {
                this.values = values;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                TypeDescription instrumentedType = instrumentedMethod.getDeclaringType().asErasure();
                MethodDescription enumConstructor = (MethodDescription) instrumentedType.getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.takesArguments(String.class, Integer.TYPE))).getOnly();
                int ordinal = 0;
                StackManipulation stackManipulation = StackManipulation.Trivial.INSTANCE;
                List<FieldDescription> enumerationFields = new ArrayList<>(this.values.size());
                for (String value : this.values) {
                    FieldDescription fieldDescription = (FieldDescription) instrumentedType.getDeclaredFields().filter(ElementMatchers.named(value)).getOnly();
                    int i = ordinal;
                    ordinal++;
                    stackManipulation = new StackManipulation.Compound(stackManipulation, TypeCreation.of(instrumentedType), Duplication.SINGLE, new TextConstant(value), IntegerConstant.forValue(i), MethodInvocation.invoke(enumConstructor), FieldAccess.forField(fieldDescription).write());
                    enumerationFields.add(fieldDescription);
                }
                List<StackManipulation> fieldGetters = new ArrayList<>(this.values.size());
                for (FieldDescription fieldDescription2 : enumerationFields) {
                    fieldGetters.add(FieldAccess.forField(fieldDescription2).read());
                }
                return new ByteCodeAppender.Size(new StackManipulation.Compound(stackManipulation, ArrayFactory.forType(instrumentedType.asGenericType()).withValues(fieldGetters), FieldAccess.forField((FieldDescription.InDefinedShape) instrumentedType.getDeclaredFields().filter(ElementMatchers.named(EnumerationImplementation.ENUM_VALUES)).getOnly()).write()).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy$RecordConstructorStrategy.class */
    protected enum RecordConstructorStrategy implements ConstructorStrategy, Implementation {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
        public List<MethodDescription.Token> extractConstructors(TypeDescription instrumentedType) {
            List<ParameterDescription.Token> tokens = new ArrayList<>(instrumentedType.getRecordComponents().size());
            for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType.getRecordComponents()) {
                tokens.add(new ParameterDescription.Token(recordComponent.getType(), recordComponent.getDeclaredAnnotations().filter(ElementMatchers.targetsElement(ElementType.CONSTRUCTOR))));
            }
            return Collections.singletonList(new MethodDescription.Token("<init>", 1, Collections.emptyList(), TypeDescription.Generic.VOID, tokens, Collections.emptyList(), Collections.emptyList(), AnnotationValue.UNDEFINED, TypeDescription.Generic.UNDEFINED));
        }

        @Override // net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
        public MethodRegistry inject(TypeDescription instrumentedType, MethodRegistry methodRegistry) {
            return methodRegistry.prepend(new LatentMatcher.Resolved(ElementMatchers.isConstructor().and(ElementMatchers.takesGenericArguments(instrumentedType.getRecordComponents().asTypeList()))), new MethodRegistry.Handler.ForImplementation(this), MethodAttributeAppender.ForInstrumentedMethod.EXCLUDING_RECEIVER, Transformer.NoOp.make());
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            return new Appender(implementationTarget.getInstrumentedType());
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType.getRecordComponents()) {
                instrumentedType = instrumentedType.withField(new FieldDescription.Token(recordComponent.getActualName(), 18, recordComponent.getType(), recordComponent.getDeclaredAnnotations().filter(ElementMatchers.targetsElement(ElementType.FIELD)))).withMethod(new MethodDescription.Token(recordComponent.getActualName(), 1, Collections.emptyList(), recordComponent.getType(), Collections.emptyList(), Collections.emptyList(), recordComponent.getDeclaredAnnotations().filter(ElementMatchers.targetsElement(ElementType.METHOD)), AnnotationValue.UNDEFINED, TypeDescription.Generic.UNDEFINED));
            }
            return instrumentedType;
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy$RecordConstructorStrategy$Appender.class */
        protected static class Appender implements ByteCodeAppender {
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

            protected Appender(TypeDescription instrumentedType) {
                this.instrumentedType = instrumentedType;
            }

            @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
            public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                if (instrumentedMethod.isMethod()) {
                    return new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(), FieldAccess.forField((FieldDescription.InDefinedShape) this.instrumentedType.getDeclaredFields().filter(ElementMatchers.named(instrumentedMethod.getName())).getOnly()).read(), MethodReturn.of(instrumentedMethod.getReturnType())).apply(methodVisitor, implementationContext, instrumentedMethod);
                }
                List<StackManipulation> stackManipulations = new ArrayList<>((this.instrumentedType.getRecordComponents().size() * 3) + 2);
                stackManipulations.add(MethodVariableAccess.loadThis());
                stackManipulations.add(MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.Latent(JavaType.RECORD.getTypeStub(), new MethodDescription.Token(1))));
                int offset = 1;
                for (RecordComponentDescription.InDefinedShape recordComponent : this.instrumentedType.getRecordComponents()) {
                    stackManipulations.add(MethodVariableAccess.loadThis());
                    stackManipulations.add(MethodVariableAccess.of(recordComponent.getType()).loadFrom(offset));
                    stackManipulations.add(FieldAccess.forField((FieldDescription.InDefinedShape) this.instrumentedType.getDeclaredFields().filter(ElementMatchers.named(recordComponent.getActualName())).getOnly()).write());
                    offset += recordComponent.getType().getStackSize().getSize();
                }
                stackManipulations.add(MethodReturn.VOID);
                return new ByteCodeAppender.Simple(stackManipulations).apply(methodVisitor, implementationContext, instrumentedMethod);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ByteBuddy$RecordObjectMethod.class */
    protected enum RecordObjectMethod implements Implementation {
        HASH_CODE("hashCode", StackManipulation.Trivial.INSTANCE, Integer.TYPE, new Class[0]),
        EQUALS("equals", MethodVariableAccess.REFERENCE.loadFrom(1), Boolean.TYPE, Object.class),
        TO_STRING("toString", StackManipulation.Trivial.INSTANCE, String.class, new Class[0]);
        
        private final String name;
        private final StackManipulation stackManipulation;
        private final TypeDescription returnType;
        private final List<? extends TypeDescription> arguments;

        RecordObjectMethod(String name, StackManipulation stackManipulation, Class cls, Class... clsArr) {
            this.name = name;
            this.stackManipulation = stackManipulation;
            this.returnType = TypeDescription.ForLoadedType.of(cls);
            this.arguments = new TypeList.ForLoadedTypes(clsArr);
        }

        @Override // net.bytebuddy.implementation.Implementation
        public ByteCodeAppender appender(Implementation.Target implementationTarget) {
            StringBuilder stringBuilder = new StringBuilder();
            List<Object> methodHandles = new ArrayList<>(implementationTarget.getInstrumentedType().getRecordComponents().size());
            for (RecordComponentDescription.InDefinedShape recordComponent : implementationTarget.getInstrumentedType().getRecordComponents()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(";");
                }
                stringBuilder.append(recordComponent.getActualName());
                methodHandles.add(JavaConstant.MethodHandle.ofGetter((FieldDescription.InDefinedShape) implementationTarget.getInstrumentedType().getDeclaredFields().filter(ElementMatchers.named(recordComponent.getActualName())).getOnly()).asConstantPoolValue());
            }
            return new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(), this.stackManipulation, MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.Latent(JavaType.OBJECT_METHODS.getTypeStub(), new MethodDescription.Token("bootstrap", 9, TypeDescription.Generic.OBJECT, Arrays.asList(JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().asGenericType(), TypeDescription.STRING.asGenericType(), JavaType.TYPE_DESCRIPTOR.getTypeStub().asGenericType(), TypeDescription.CLASS.asGenericType(), TypeDescription.STRING.asGenericType(), TypeDescription.ArrayProjection.of(JavaType.METHOD_HANDLE.getTypeStub()).asGenericType())))).dynamic(this.name, this.returnType, CompoundList.of(implementationTarget.getInstrumentedType(), this.arguments), CompoundList.of(Arrays.asList(net.bytebuddy.jar.asm.Type.getType(implementationTarget.getInstrumentedType().getDescriptor()), stringBuilder.toString()), (List) methodHandles)), MethodReturn.of(this.returnType));
        }

        @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }
    }
}
