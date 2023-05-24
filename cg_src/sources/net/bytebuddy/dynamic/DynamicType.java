package net.bytebuddy.dynamic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.MethodManifestation;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.description.type.TypeVariableToken;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import net.bytebuddy.dynamic.scaffold.ClassWriterStrategy;
import net.bytebuddy.dynamic.scaffold.FieldRegistry;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import net.bytebuddy.dynamic.scaffold.RecordComponentRegistry;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.EqualsMethod;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.HashCodeMethod;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.ToStringMethod;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.attribute.FieldAttributeAppender;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType.class */
public interface DynamicType {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Loaded.class */
    public interface Loaded<T> extends DynamicType {
        Class<? extends T> getLoaded();

        Map<TypeDescription, Class<?>> getLoadedAuxiliaryTypes();
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Unloaded.class */
    public interface Unloaded<T> extends DynamicType {
        Loaded<T> load(ClassLoader classLoader);

        <S extends ClassLoader> Loaded<T> load(S s, ClassLoadingStrategy<? super S> classLoadingStrategy);

        Unloaded<T> include(DynamicType... dynamicTypeArr);

        Unloaded<T> include(List<? extends DynamicType> list);
    }

    TypeDescription getTypeDescription();

    byte[] getBytes();

    Map<TypeDescription, byte[]> getAuxiliaryTypes();

    Map<TypeDescription, byte[]> getAllTypes();

    Map<TypeDescription, LoadedTypeInitializer> getLoadedTypeInitializers();

    boolean hasAliveLoadedTypeInitializers();

    Map<TypeDescription, File> saveIn(File file) throws IOException;

    File inject(File file, File file2) throws IOException;

    File inject(File file) throws IOException;

    File toJar(File file) throws IOException;

    File toJar(File file, Manifest manifest) throws IOException;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder.class */
    public interface Builder<T> {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$InnerTypeDefinition.class */
        public interface InnerTypeDefinition<S> extends Builder<S> {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$InnerTypeDefinition$ForType.class */
            public interface ForType<U> extends InnerTypeDefinition<U> {
                Builder<U> asMemberType();
            }

            Builder<S> asAnonymousType();
        }

        Builder<T> visit(AsmVisitorWrapper asmVisitorWrapper);

        Builder<T> name(String str);

        Builder<T> suffix(String str);

        Builder<T> modifiers(ModifierContributor.ForType... forTypeArr);

        Builder<T> modifiers(Collection<? extends ModifierContributor.ForType> collection);

        Builder<T> modifiers(int i);

        Builder<T> merge(ModifierContributor.ForType... forTypeArr);

        Builder<T> merge(Collection<? extends ModifierContributor.ForType> collection);

        Builder<T> topLevelType();

        InnerTypeDefinition.ForType<T> innerTypeOf(Class<?> cls);

        InnerTypeDefinition.ForType<T> innerTypeOf(TypeDescription typeDescription);

        InnerTypeDefinition<T> innerTypeOf(Method method);

        InnerTypeDefinition<T> innerTypeOf(Constructor<?> constructor);

        InnerTypeDefinition<T> innerTypeOf(MethodDescription.InDefinedShape inDefinedShape);

        Builder<T> declaredTypes(Class<?>... clsArr);

        Builder<T> declaredTypes(TypeDescription... typeDescriptionArr);

        Builder<T> declaredTypes(List<? extends Class<?>> list);

        Builder<T> declaredTypes(Collection<? extends TypeDescription> collection);

        Builder<T> noNestMate();

        Builder<T> nestHost(Class<?> cls);

        Builder<T> nestHost(TypeDescription typeDescription);

        Builder<T> nestMembers(Class<?>... clsArr);

        Builder<T> nestMembers(TypeDescription... typeDescriptionArr);

        Builder<T> nestMembers(List<? extends Class<?>> list);

        Builder<T> nestMembers(Collection<? extends TypeDescription> collection);

        Builder<T> permittedSubclass(Class<?>... clsArr);

        Builder<T> permittedSubclass(TypeDescription... typeDescriptionArr);

        Builder<T> permittedSubclass(List<? extends Class<?>> list);

        Builder<T> permittedSubclass(Collection<? extends TypeDescription> collection);

        Builder<T> unsealed();

        Builder<T> attribute(TypeAttributeAppender typeAttributeAppender);

        Builder<T> annotateType(Annotation... annotationArr);

        Builder<T> annotateType(List<? extends Annotation> list);

        Builder<T> annotateType(AnnotationDescription... annotationDescriptionArr);

        Builder<T> annotateType(Collection<? extends AnnotationDescription> collection);

        MethodDefinition.ImplementationDefinition.Optional<T> implement(Type... typeArr);

        MethodDefinition.ImplementationDefinition.Optional<T> implement(List<? extends Type> list);

        MethodDefinition.ImplementationDefinition.Optional<T> implement(TypeDefinition... typeDefinitionArr);

        MethodDefinition.ImplementationDefinition.Optional<T> implement(Collection<? extends TypeDefinition> collection);

        Builder<T> initializer(ByteCodeAppender byteCodeAppender);

        Builder<T> initializer(LoadedTypeInitializer loadedTypeInitializer);

        Builder<T> require(TypeDescription typeDescription, byte[] bArr);

        Builder<T> require(TypeDescription typeDescription, byte[] bArr, LoadedTypeInitializer loadedTypeInitializer);

        Builder<T> require(DynamicType... dynamicTypeArr);

        Builder<T> require(Collection<DynamicType> collection);

        TypeVariableDefinition<T> typeVariable(String str);

        TypeVariableDefinition<T> typeVariable(String str, Type... typeArr);

        TypeVariableDefinition<T> typeVariable(String str, List<? extends Type> list);

        TypeVariableDefinition<T> typeVariable(String str, TypeDefinition... typeDefinitionArr);

        TypeVariableDefinition<T> typeVariable(String str, Collection<? extends TypeDefinition> collection);

        Builder<T> transform(ElementMatcher<? super TypeDescription.Generic> elementMatcher, Transformer<TypeVariableToken> transformer);

        FieldDefinition.Optional.Valuable<T> defineField(String str, Type type, ModifierContributor.ForField... forFieldArr);

        FieldDefinition.Optional.Valuable<T> defineField(String str, Type type, Collection<? extends ModifierContributor.ForField> collection);

        FieldDefinition.Optional.Valuable<T> defineField(String str, Type type, int i);

        FieldDefinition.Optional.Valuable<T> defineField(String str, TypeDefinition typeDefinition, ModifierContributor.ForField... forFieldArr);

        FieldDefinition.Optional.Valuable<T> defineField(String str, TypeDefinition typeDefinition, Collection<? extends ModifierContributor.ForField> collection);

        FieldDefinition.Optional.Valuable<T> defineField(String str, TypeDefinition typeDefinition, int i);

        FieldDefinition.Optional.Valuable<T> define(Field field);

        FieldDefinition.Optional.Valuable<T> define(FieldDescription fieldDescription);

        FieldDefinition.Optional<T> serialVersionUid(long j);

        FieldDefinition.Valuable<T> field(ElementMatcher<? super FieldDescription> elementMatcher);

        FieldDefinition.Valuable<T> field(LatentMatcher<? super FieldDescription> latentMatcher);

        Builder<T> ignoreAlso(ElementMatcher<? super MethodDescription> elementMatcher);

        Builder<T> ignoreAlso(LatentMatcher<? super MethodDescription> latentMatcher);

        MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String str, Type type, ModifierContributor.ForMethod... forMethodArr);

        MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String str, Type type, Collection<? extends ModifierContributor.ForMethod> collection);

        MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String str, Type type, int i);

        MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String str, TypeDefinition typeDefinition, ModifierContributor.ForMethod... forMethodArr);

        MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String str, TypeDefinition typeDefinition, Collection<? extends ModifierContributor.ForMethod> collection);

        MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String str, TypeDefinition typeDefinition, int i);

        MethodDefinition.ParameterDefinition.Initial<T> defineConstructor(ModifierContributor.ForMethod... forMethodArr);

        MethodDefinition.ParameterDefinition.Initial<T> defineConstructor(Collection<? extends ModifierContributor.ForMethod> collection);

        MethodDefinition.ParameterDefinition.Initial<T> defineConstructor(int i);

        MethodDefinition.ImplementationDefinition<T> define(Method method);

        MethodDefinition.ImplementationDefinition<T> define(Constructor<?> constructor);

        MethodDefinition.ImplementationDefinition<T> define(MethodDescription methodDescription);

        FieldDefinition.Optional<T> defineProperty(String str, Type type);

        FieldDefinition.Optional<T> defineProperty(String str, Type type, boolean z);

        FieldDefinition.Optional<T> defineProperty(String str, TypeDefinition typeDefinition);

        FieldDefinition.Optional<T> defineProperty(String str, TypeDefinition typeDefinition, boolean z);

        MethodDefinition.ImplementationDefinition<T> method(ElementMatcher<? super MethodDescription> elementMatcher);

        MethodDefinition.ImplementationDefinition<T> constructor(ElementMatcher<? super MethodDescription> elementMatcher);

        MethodDefinition.ImplementationDefinition<T> invokable(ElementMatcher<? super MethodDescription> elementMatcher);

        MethodDefinition.ImplementationDefinition<T> invokable(LatentMatcher<? super MethodDescription> latentMatcher);

        Builder<T> withHashCodeEquals();

        Builder<T> withToString();

        RecordComponentDefinition.Optional<T> defineRecordComponent(String str, Type type);

        RecordComponentDefinition.Optional<T> defineRecordComponent(String str, TypeDefinition typeDefinition);

        RecordComponentDefinition.Optional<T> define(RecordComponentDescription recordComponentDescription);

        RecordComponentDefinition<T> recordComponent(ElementMatcher<? super RecordComponentDescription> elementMatcher);

        RecordComponentDefinition<T> recordComponent(LatentMatcher<? super RecordComponentDescription> latentMatcher);

        Unloaded<T> make();

        Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy);

        Unloaded<T> make(TypePool typePool);

        Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy, TypePool typePool);

        TypeDescription toTypeDescription();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$TypeVariableDefinition.class */
        public interface TypeVariableDefinition<S> extends Builder<S> {
            TypeVariableDefinition<S> annotateTypeVariable(Annotation... annotationArr);

            TypeVariableDefinition<S> annotateTypeVariable(List<? extends Annotation> list);

            TypeVariableDefinition<S> annotateTypeVariable(AnnotationDescription... annotationDescriptionArr);

            TypeVariableDefinition<S> annotateTypeVariable(Collection<? extends AnnotationDescription> collection);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$TypeVariableDefinition$AbstractBase.class */
            public static abstract class AbstractBase<U> extends AbstractBase.Delegator<U> implements TypeVariableDefinition<U> {
                @Override // net.bytebuddy.dynamic.DynamicType.Builder.TypeVariableDefinition
                public TypeVariableDefinition<U> annotateTypeVariable(Annotation... annotation) {
                    return annotateTypeVariable(Arrays.asList(annotation));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.TypeVariableDefinition
                public TypeVariableDefinition<U> annotateTypeVariable(List<? extends Annotation> annotations) {
                    return annotateTypeVariable((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.TypeVariableDefinition
                public TypeVariableDefinition<U> annotateTypeVariable(AnnotationDescription... annotation) {
                    return annotateTypeVariable((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition.class */
        public interface FieldDefinition<S> {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition$Valuable.class */
            public interface Valuable<U> extends FieldDefinition<U> {
                Optional<U> value(boolean z);

                Optional<U> value(int i);

                Optional<U> value(long j);

                Optional<U> value(float f);

                Optional<U> value(double d);

                Optional<U> value(String str);
            }

            Optional<S> annotateField(Annotation... annotationArr);

            Optional<S> annotateField(List<? extends Annotation> list);

            Optional<S> annotateField(AnnotationDescription... annotationDescriptionArr);

            Optional<S> annotateField(Collection<? extends AnnotationDescription> collection);

            Optional<S> attribute(FieldAttributeAppender.Factory factory);

            Optional<S> transform(Transformer<FieldDescription> transformer);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition$Optional.class */
            public interface Optional<U> extends FieldDefinition<U>, Builder<U> {

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition$Optional$Valuable.class */
                public interface Valuable<V> extends Valuable<V>, Optional<V> {

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition$Optional$Valuable$AbstractBase.class */
                    public static abstract class AbstractBase<U> extends AbstractBase<U> implements Valuable<U> {
                        protected abstract Optional<U> defaultValue(Object obj);

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable
                        public Optional<U> value(boolean value) {
                            return defaultValue(Integer.valueOf(value ? 1 : 0));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable
                        public Optional<U> value(int value) {
                            return defaultValue(Integer.valueOf(value));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable
                        public Optional<U> value(long value) {
                            return defaultValue(Long.valueOf(value));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable
                        public Optional<U> value(float value) {
                            return defaultValue(Float.valueOf(value));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable
                        public Optional<U> value(double value) {
                            return defaultValue(Double.valueOf(value));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable
                        public Optional<U> value(String value) {
                            if (value == null) {
                                throw new IllegalArgumentException("Cannot set null as a default value");
                            }
                            return defaultValue(value);
                        }

                        @HashCodeAndEqualsPlugin.Enhance
                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition$Optional$Valuable$AbstractBase$Adapter.class */
                        private static abstract class Adapter<V> extends AbstractBase<V> {
                            protected final FieldAttributeAppender.Factory fieldAttributeAppenderFactory;
                            protected final Transformer<FieldDescription> transformer;
                            @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
                            protected final Object defaultValue;

                            protected abstract Optional<V> materialize(FieldAttributeAppender.Factory factory, Transformer<FieldDescription> transformer, Object obj);

                            public boolean equals(Object obj) {
                                if (this == obj) {
                                    return true;
                                }
                                if (obj != null && getClass() == obj.getClass() && this.fieldAttributeAppenderFactory.equals(((Adapter) obj).fieldAttributeAppenderFactory) && this.transformer.equals(((Adapter) obj).transformer)) {
                                    Object obj2 = this.defaultValue;
                                    Object obj3 = ((Adapter) obj).defaultValue;
                                    return obj3 != null ? obj2 != null && obj2.equals(obj3) : obj2 == null;
                                }
                                return false;
                            }

                            public int hashCode() {
                                int hashCode = ((((17 * 31) + this.fieldAttributeAppenderFactory.hashCode()) * 31) + this.transformer.hashCode()) * 31;
                                Object obj = this.defaultValue;
                                return obj != null ? hashCode + obj.hashCode() : hashCode;
                            }

                            protected Adapter(FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Transformer<FieldDescription> transformer, Object defaultValue) {
                                this.fieldAttributeAppenderFactory = fieldAttributeAppenderFactory;
                                this.transformer = transformer;
                                this.defaultValue = defaultValue;
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                            public Optional<V> attribute(FieldAttributeAppender.Factory fieldAttributeAppenderFactory) {
                                return materialize(new FieldAttributeAppender.Factory.Compound(this.fieldAttributeAppenderFactory, fieldAttributeAppenderFactory), this.transformer, this.defaultValue);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                            public Optional<V> transform(Transformer<FieldDescription> transformer) {
                                return materialize(this.fieldAttributeAppenderFactory, new Transformer.Compound(this.transformer, transformer), this.defaultValue);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase
                            protected Optional<V> defaultValue(Object defaultValue) {
                                return materialize(this.fieldAttributeAppenderFactory, this.transformer, defaultValue);
                            }
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$FieldDefinition$Optional$AbstractBase.class */
                public static abstract class AbstractBase<U> extends AbstractBase.Delegator<U> implements Optional<U> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                    public Optional<U> annotateField(Annotation... annotation) {
                        return annotateField(Arrays.asList(annotation));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                    public Optional<U> annotateField(List<? extends Annotation> annotations) {
                        return annotateField((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                    public Optional<U> annotateField(AnnotationDescription... annotation) {
                        return annotateField((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition.class */
        public interface MethodDefinition<S> extends Builder<S> {
            MethodDefinition<S> annotateMethod(Annotation... annotationArr);

            MethodDefinition<S> annotateMethod(List<? extends Annotation> list);

            MethodDefinition<S> annotateMethod(AnnotationDescription... annotationDescriptionArr);

            MethodDefinition<S> annotateMethod(Collection<? extends AnnotationDescription> collection);

            MethodDefinition<S> annotateParameter(int i, Annotation... annotationArr);

            MethodDefinition<S> annotateParameter(int i, List<? extends Annotation> list);

            MethodDefinition<S> annotateParameter(int i, AnnotationDescription... annotationDescriptionArr);

            MethodDefinition<S> annotateParameter(int i, Collection<? extends AnnotationDescription> collection);

            MethodDefinition<S> attribute(MethodAttributeAppender.Factory factory);

            MethodDefinition<S> transform(Transformer<MethodDescription> transformer);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ReceiverTypeDefinition.class */
            public interface ReceiverTypeDefinition<U> extends MethodDefinition<U> {
                MethodDefinition<U> receiverType(AnnotatedElement annotatedElement);

                MethodDefinition<U> receiverType(TypeDescription.Generic generic);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ReceiverTypeDefinition$AbstractBase.class */
                public static abstract class AbstractBase<V> extends AbstractBase<V> implements ReceiverTypeDefinition<V> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition
                    public MethodDefinition<V> receiverType(AnnotatedElement receiverType) {
                        return receiverType(TypeDescription.Generic.AnnotationReader.DISPATCHER.resolve(receiverType));
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ImplementationDefinition.class */
            public interface ImplementationDefinition<U> {

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ImplementationDefinition$Optional.class */
                public interface Optional<V> extends ImplementationDefinition<V>, Builder<V> {
                }

                ReceiverTypeDefinition<U> intercept(Implementation implementation);

                ReceiverTypeDefinition<U> withoutCode();

                ReceiverTypeDefinition<U> defaultValue(AnnotationValue<?, ?> annotationValue);

                <W> ReceiverTypeDefinition<U> defaultValue(W w, Class<? extends W> cls);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ImplementationDefinition$AbstractBase.class */
                public static abstract class AbstractBase<V> implements ImplementationDefinition<V> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public <W> ReceiverTypeDefinition<V> defaultValue(W value, Class<? extends W> type) {
                        return defaultValue(AnnotationDescription.ForLoadedAnnotation.asValue(value, type));
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$TypeVariableDefinition.class */
            public interface TypeVariableDefinition<U> extends ImplementationDefinition<U> {
                Annotatable<U> typeVariable(String str);

                Annotatable<U> typeVariable(String str, Type... typeArr);

                Annotatable<U> typeVariable(String str, List<? extends Type> list);

                Annotatable<U> typeVariable(String str, TypeDefinition... typeDefinitionArr);

                Annotatable<U> typeVariable(String str, Collection<? extends TypeDefinition> collection);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$TypeVariableDefinition$Annotatable.class */
                public interface Annotatable<V> extends TypeVariableDefinition<V> {
                    Annotatable<V> annotateTypeVariable(Annotation... annotationArr);

                    Annotatable<V> annotateTypeVariable(List<? extends Annotation> list);

                    Annotatable<V> annotateTypeVariable(AnnotationDescription... annotationDescriptionArr);

                    Annotatable<V> annotateTypeVariable(Collection<? extends AnnotationDescription> collection);

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$TypeVariableDefinition$Annotatable$AbstractBase.class */
                    public static abstract class AbstractBase<W> extends AbstractBase<W> implements Annotatable<W> {
                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition.Annotatable
                        public Annotatable<W> annotateTypeVariable(Annotation... annotation) {
                            return annotateTypeVariable(Arrays.asList(annotation));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition.Annotatable
                        public Annotatable<W> annotateTypeVariable(List<? extends Annotation> annotations) {
                            return annotateTypeVariable((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition.Annotatable
                        public Annotatable<W> annotateTypeVariable(AnnotationDescription... annotation) {
                            return annotateTypeVariable((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                        }

                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$TypeVariableDefinition$Annotatable$AbstractBase$Adapter.class */
                        protected static abstract class Adapter<X> extends AbstractBase<X> {
                            protected abstract ParameterDefinition<X> materialize();

                            protected Adapter() {
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                            public Annotatable<X> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
                                return materialize().typeVariable(symbol, bounds);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public ReceiverTypeDefinition<X> intercept(Implementation implementation) {
                                return materialize().intercept(implementation);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public ReceiverTypeDefinition<X> withoutCode() {
                                return materialize().withoutCode();
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public ReceiverTypeDefinition<X> defaultValue(AnnotationValue<?, ?> annotationValue) {
                                return materialize().defaultValue(annotationValue);
                            }

                            /* JADX WARN: Multi-variable type inference failed */
                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public <V> ReceiverTypeDefinition<X> defaultValue(V value, Class<? extends V> type) {
                                return materialize().defaultValue(value, type);
                            }
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$TypeVariableDefinition$AbstractBase.class */
                public static abstract class AbstractBase<V> extends ImplementationDefinition.AbstractBase<V> implements TypeVariableDefinition<V> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                    public Annotatable<V> typeVariable(String symbol) {
                        return typeVariable(symbol, Collections.singletonList(Object.class));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                    public Annotatable<V> typeVariable(String symbol, Type... bound) {
                        return typeVariable(symbol, Arrays.asList(bound));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                    public Annotatable<V> typeVariable(String symbol, List<? extends Type> bounds) {
                        return typeVariable(symbol, (Collection<? extends TypeDefinition>) new TypeList.Generic.ForLoadedTypes(bounds));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                    public Annotatable<V> typeVariable(String symbol, TypeDefinition... bound) {
                        return typeVariable(symbol, (Collection<? extends TypeDefinition>) Arrays.asList(bound));
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ExceptionDefinition.class */
            public interface ExceptionDefinition<U> extends TypeVariableDefinition<U> {
                ExceptionDefinition<U> throwing(Type... typeArr);

                ExceptionDefinition<U> throwing(List<? extends Type> list);

                ExceptionDefinition<U> throwing(TypeDefinition... typeDefinitionArr);

                ExceptionDefinition<U> throwing(Collection<? extends TypeDefinition> collection);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ExceptionDefinition$AbstractBase.class */
                public static abstract class AbstractBase<V> extends TypeVariableDefinition.AbstractBase<V> implements ExceptionDefinition<V> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ExceptionDefinition
                    public ExceptionDefinition<V> throwing(Type... type) {
                        return throwing(Arrays.asList(type));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ExceptionDefinition
                    public ExceptionDefinition<V> throwing(List<? extends Type> types) {
                        return throwing((Collection<? extends TypeDefinition>) new TypeList.Generic.ForLoadedTypes(types));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ExceptionDefinition
                    public ExceptionDefinition<V> throwing(TypeDefinition... type) {
                        return throwing((Collection<? extends TypeDefinition>) Arrays.asList(type));
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition.class */
            public interface ParameterDefinition<U> extends ExceptionDefinition<U> {
                Annotatable<U> withParameter(Type type, String str, ModifierContributor.ForParameter... forParameterArr);

                Annotatable<U> withParameter(Type type, String str, Collection<? extends ModifierContributor.ForParameter> collection);

                Annotatable<U> withParameter(Type type, String str, int i);

                Annotatable<U> withParameter(TypeDefinition typeDefinition, String str, ModifierContributor.ForParameter... forParameterArr);

                Annotatable<U> withParameter(TypeDefinition typeDefinition, String str, Collection<? extends ModifierContributor.ForParameter> collection);

                Annotatable<U> withParameter(TypeDefinition typeDefinition, String str, int i);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Annotatable.class */
                public interface Annotatable<V> extends ParameterDefinition<V> {
                    Annotatable<V> annotateParameter(Annotation... annotationArr);

                    Annotatable<V> annotateParameter(List<? extends Annotation> list);

                    Annotatable<V> annotateParameter(AnnotationDescription... annotationDescriptionArr);

                    Annotatable<V> annotateParameter(Collection<? extends AnnotationDescription> collection);

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Annotatable$AbstractBase.class */
                    public static abstract class AbstractBase<W> extends AbstractBase<W> implements Annotatable<W> {
                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Annotatable
                        public Annotatable<W> annotateParameter(Annotation... annotation) {
                            return annotateParameter(Arrays.asList(annotation));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Annotatable
                        public Annotatable<W> annotateParameter(List<? extends Annotation> annotations) {
                            return annotateParameter((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Annotatable
                        public Annotatable<W> annotateParameter(AnnotationDescription... annotation) {
                            return annotateParameter((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                        }

                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Annotatable$AbstractBase$Adapter.class */
                        protected static abstract class Adapter<X> extends AbstractBase<X> {
                            protected abstract ParameterDefinition<X> materialize();

                            protected Adapter() {
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                            public Annotatable<X> withParameter(TypeDefinition type, String name, int modifiers) {
                                return materialize().withParameter(type, name, modifiers);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ExceptionDefinition
                            public ExceptionDefinition<X> throwing(Collection<? extends TypeDefinition> types) {
                                return materialize().throwing(types);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                            public TypeVariableDefinition.Annotatable<X> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
                                return materialize().typeVariable(symbol, bounds);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public ReceiverTypeDefinition<X> intercept(Implementation implementation) {
                                return materialize().intercept(implementation);
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public ReceiverTypeDefinition<X> withoutCode() {
                                return materialize().withoutCode();
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public ReceiverTypeDefinition<X> defaultValue(AnnotationValue<?, ?> annotationValue) {
                                return materialize().defaultValue(annotationValue);
                            }

                            /* JADX WARN: Multi-variable type inference failed */
                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                            public <V> ReceiverTypeDefinition<X> defaultValue(V value, Class<? extends V> type) {
                                return materialize().defaultValue(value, type);
                            }
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Simple.class */
                public interface Simple<V> extends ExceptionDefinition<V> {
                    Annotatable<V> withParameter(Type type);

                    Annotatable<V> withParameter(TypeDefinition typeDefinition);

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Simple$Annotatable.class */
                    public interface Annotatable<V> extends Simple<V> {
                        Annotatable<V> annotateParameter(Annotation... annotationArr);

                        Annotatable<V> annotateParameter(List<? extends Annotation> list);

                        Annotatable<V> annotateParameter(AnnotationDescription... annotationDescriptionArr);

                        Annotatable<V> annotateParameter(Collection<? extends AnnotationDescription> collection);

                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Simple$Annotatable$AbstractBase.class */
                        public static abstract class AbstractBase<W> extends AbstractBase<W> implements Annotatable<W> {
                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple.Annotatable
                            public Annotatable<W> annotateParameter(Annotation... annotation) {
                                return annotateParameter(Arrays.asList(annotation));
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple.Annotatable
                            public Annotatable<W> annotateParameter(List<? extends Annotation> annotations) {
                                return annotateParameter((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                            }

                            @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple.Annotatable
                            public Annotatable<W> annotateParameter(AnnotationDescription... annotation) {
                                return annotateParameter((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                            }

                            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Simple$Annotatable$AbstractBase$Adapter.class */
                            protected static abstract class Adapter<X> extends AbstractBase<X> {
                                protected abstract Simple<X> materialize();

                                protected Adapter() {
                                }

                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple
                                public Annotatable<X> withParameter(TypeDefinition type) {
                                    return materialize().withParameter(type);
                                }

                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ExceptionDefinition
                                public ExceptionDefinition<X> throwing(Collection<? extends TypeDefinition> types) {
                                    return materialize().throwing(types);
                                }

                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                                public TypeVariableDefinition.Annotatable<X> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
                                    return materialize().typeVariable(symbol, bounds);
                                }

                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                                public ReceiverTypeDefinition<X> intercept(Implementation implementation) {
                                    return materialize().intercept(implementation);
                                }

                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                                public ReceiverTypeDefinition<X> withoutCode() {
                                    return materialize().withoutCode();
                                }

                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                                public ReceiverTypeDefinition<X> defaultValue(AnnotationValue<?, ?> annotationValue) {
                                    return materialize().defaultValue(annotationValue);
                                }

                                /* JADX WARN: Multi-variable type inference failed */
                                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                                public <V> ReceiverTypeDefinition<X> defaultValue(V value, Class<? extends V> type) {
                                    return materialize().defaultValue(value, type);
                                }
                            }
                        }
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Simple$AbstractBase.class */
                    public static abstract class AbstractBase<W> extends ExceptionDefinition.AbstractBase<W> implements Simple<W> {
                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple
                        public Annotatable<W> withParameter(Type type) {
                            return withParameter(TypeDefinition.Sort.describe(type));
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Initial.class */
                public interface Initial<V> extends ParameterDefinition<V>, Simple<V> {
                    ExceptionDefinition<V> withParameters(Type... typeArr);

                    ExceptionDefinition<V> withParameters(List<? extends Type> list);

                    ExceptionDefinition<V> withParameters(TypeDefinition... typeDefinitionArr);

                    ExceptionDefinition<V> withParameters(Collection<? extends TypeDefinition> collection);

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$Initial$AbstractBase.class */
                    public static abstract class AbstractBase<W> extends AbstractBase<W> implements Initial<W> {
                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple
                        public Simple.Annotatable<W> withParameter(Type type) {
                            return withParameter(TypeDefinition.Sort.describe(type));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial
                        public ExceptionDefinition<W> withParameters(Type... type) {
                            return withParameters(Arrays.asList(type));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial
                        public ExceptionDefinition<W> withParameters(List<? extends Type> types) {
                            return withParameters((Collection<? extends TypeDefinition>) new TypeList.Generic.ForLoadedTypes(types));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial
                        public ExceptionDefinition<W> withParameters(TypeDefinition... type) {
                            return withParameters((Collection<? extends TypeDefinition>) Arrays.asList(type));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial
                        public ExceptionDefinition<W> withParameters(Collection<? extends TypeDefinition> types) {
                            Simple<W> parameterDefinition = this;
                            for (TypeDefinition type : types) {
                                parameterDefinition = (Simple.Annotatable<W>) parameterDefinition.withParameter(type);
                            }
                            return parameterDefinition;
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$ParameterDefinition$AbstractBase.class */
                public static abstract class AbstractBase<V> extends ExceptionDefinition.AbstractBase<V> implements ParameterDefinition<V> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                    public Annotatable<V> withParameter(Type type, String name, ModifierContributor.ForParameter... modifierContributor) {
                        return withParameter(type, name, Arrays.asList(modifierContributor));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                    public Annotatable<V> withParameter(Type type, String name, Collection<? extends ModifierContributor.ForParameter> modifierContributors) {
                        return withParameter(type, name, ModifierContributor.Resolver.of(modifierContributors).resolve());
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                    public Annotatable<V> withParameter(Type type, String name, int modifiers) {
                        return withParameter(TypeDefinition.Sort.describe(type), name, modifiers);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                    public Annotatable<V> withParameter(TypeDefinition type, String name, ModifierContributor.ForParameter... modifierContributor) {
                        return withParameter(type, name, Arrays.asList(modifierContributor));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                    public Annotatable<V> withParameter(TypeDefinition type, String name, Collection<? extends ModifierContributor.ForParameter> modifierContributors) {
                        return withParameter(type, name, ModifierContributor.Resolver.of(modifierContributors).resolve());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$AbstractBase.class */
            public static abstract class AbstractBase<U> extends AbstractBase.Delegator<U> implements MethodDefinition<U> {
                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                public MethodDefinition<U> annotateMethod(Annotation... annotation) {
                    return annotateMethod(Arrays.asList(annotation));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                public MethodDefinition<U> annotateMethod(List<? extends Annotation> annotations) {
                    return annotateMethod((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                public MethodDefinition<U> annotateMethod(AnnotationDescription... annotation) {
                    return annotateMethod((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                public MethodDefinition<U> annotateParameter(int index, Annotation... annotation) {
                    return annotateParameter(index, Arrays.asList(annotation));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                public MethodDefinition<U> annotateParameter(int index, List<? extends Annotation> annotations) {
                    return annotateParameter(index, (Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                public MethodDefinition<U> annotateParameter(int index, AnnotationDescription... annotation) {
                    return annotateParameter(index, (Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$MethodDefinition$AbstractBase$Adapter.class */
                protected static abstract class Adapter<V> extends ReceiverTypeDefinition.AbstractBase<V> {
                    protected final MethodRegistry.Handler handler;
                    protected final MethodAttributeAppender.Factory methodAttributeAppenderFactory;
                    protected final Transformer<MethodDescription> transformer;

                    protected abstract MethodDefinition<V> materialize(MethodRegistry.Handler handler, MethodAttributeAppender.Factory factory, Transformer<MethodDescription> transformer);

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.handler.equals(((Adapter) obj).handler) && this.methodAttributeAppenderFactory.equals(((Adapter) obj).methodAttributeAppenderFactory) && this.transformer.equals(((Adapter) obj).transformer);
                    }

                    public int hashCode() {
                        return (((((17 * 31) + this.handler.hashCode()) * 31) + this.methodAttributeAppenderFactory.hashCode()) * 31) + this.transformer.hashCode();
                    }

                    protected Adapter(MethodRegistry.Handler handler, MethodAttributeAppender.Factory methodAttributeAppenderFactory, Transformer<MethodDescription> transformer) {
                        this.handler = handler;
                        this.methodAttributeAppenderFactory = methodAttributeAppenderFactory;
                        this.transformer = transformer;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                    public MethodDefinition<V> attribute(MethodAttributeAppender.Factory methodAttributeAppenderFactory) {
                        return materialize(this.handler, new MethodAttributeAppender.Factory.Compound(this.methodAttributeAppenderFactory, methodAttributeAppenderFactory), this.transformer);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                    public MethodDefinition<V> transform(Transformer<MethodDescription> transformer) {
                        return materialize(this.handler, this.methodAttributeAppenderFactory, new Transformer.Compound(this.transformer, transformer));
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$RecordComponentDefinition.class */
        public interface RecordComponentDefinition<S> {
            Optional<S> annotateRecordComponent(Annotation... annotationArr);

            Optional<S> annotateRecordComponent(List<? extends Annotation> list);

            Optional<S> annotateRecordComponent(AnnotationDescription... annotationDescriptionArr);

            Optional<S> annotateRecordComponent(Collection<? extends AnnotationDescription> collection);

            Optional<S> attribute(RecordComponentAttributeAppender.Factory factory);

            Optional<S> transform(Transformer<RecordComponentDescription> transformer);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$RecordComponentDefinition$Optional.class */
            public interface Optional<U> extends RecordComponentDefinition<U>, Builder<U> {

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$RecordComponentDefinition$Optional$AbstractBase.class */
                public static abstract class AbstractBase<U> extends AbstractBase.Delegator<U> implements Optional<U> {
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public Optional<U> annotateRecordComponent(Annotation... annotation) {
                        return annotateRecordComponent(Arrays.asList(annotation));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public Optional<U> annotateRecordComponent(List<? extends Annotation> annotations) {
                        return annotateRecordComponent((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public Optional<U> annotateRecordComponent(AnnotationDescription... annotation) {
                        return annotateRecordComponent((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase.class */
        public static abstract class AbstractBase<S> implements Builder<S> {
            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public InnerTypeDefinition.ForType<S> innerTypeOf(Class<?> type) {
                return innerTypeOf(TypeDescription.ForLoadedType.of(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public InnerTypeDefinition<S> innerTypeOf(Method method) {
                return innerTypeOf(new MethodDescription.ForLoadedMethod(method));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public InnerTypeDefinition<S> innerTypeOf(Constructor<?> constructor) {
                return innerTypeOf(new MethodDescription.ForLoadedConstructor(constructor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> declaredTypes(Class<?>... type) {
                return declaredTypes(Arrays.asList(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> declaredTypes(TypeDescription... type) {
                return declaredTypes((Collection<? extends TypeDescription>) Arrays.asList(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> declaredTypes(List<? extends Class<?>> type) {
                return declaredTypes((Collection<? extends TypeDescription>) new TypeList.ForLoadedTypes(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> noNestMate() {
                return nestHost(TargetType.DESCRIPTION);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> nestHost(Class<?> type) {
                return nestHost(TypeDescription.ForLoadedType.of(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> nestMembers(Class<?>... type) {
                return nestMembers(Arrays.asList(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> nestMembers(TypeDescription... type) {
                return nestMembers((Collection<? extends TypeDescription>) Arrays.asList(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> nestMembers(List<? extends Class<?>> types) {
                return nestMembers((Collection<? extends TypeDescription>) new TypeList.ForLoadedTypes(types));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> permittedSubclass(Class<?>... type) {
                return permittedSubclass(Arrays.asList(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> permittedSubclass(TypeDescription... type) {
                return permittedSubclass((Collection<? extends TypeDescription>) Arrays.asList(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> permittedSubclass(List<? extends Class<?>> types) {
                return permittedSubclass((Collection<? extends TypeDescription>) new TypeList.ForLoadedTypes(types));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> annotateType(Annotation... annotation) {
                return annotateType(Arrays.asList(annotation));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> annotateType(List<? extends Annotation> annotations) {
                return annotateType((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> annotateType(AnnotationDescription... annotation) {
                return annotateType((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> modifiers(ModifierContributor.ForType... modifierContributor) {
                return modifiers(Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> modifiers(Collection<? extends ModifierContributor.ForType> modifierContributors) {
                return modifiers(ModifierContributor.Resolver.of(modifierContributors).resolve());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> merge(ModifierContributor.ForType... modifierContributor) {
                return merge(Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition.Optional<S> implement(Type... interfaceType) {
                return implement(Arrays.asList(interfaceType));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition.Optional<S> implement(List<? extends Type> interfaceTypes) {
                return implement((Collection<? extends TypeDefinition>) new TypeList.Generic.ForLoadedTypes(interfaceTypes));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition.Optional<S> implement(TypeDefinition... interfaceType) {
                return implement((Collection<? extends TypeDefinition>) Arrays.asList(interfaceType));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public TypeVariableDefinition<S> typeVariable(String symbol) {
                return typeVariable(symbol, TypeDescription.Generic.OBJECT);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public TypeVariableDefinition<S> typeVariable(String symbol, Type... bound) {
                return typeVariable(symbol, Arrays.asList(bound));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public TypeVariableDefinition<S> typeVariable(String symbol, List<? extends Type> bounds) {
                return typeVariable(symbol, (Collection<? extends TypeDefinition>) new TypeList.Generic.ForLoadedTypes(bounds));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public TypeVariableDefinition<S> typeVariable(String symbol, TypeDefinition... bound) {
                return typeVariable(symbol, (Collection<? extends TypeDefinition>) Arrays.asList(bound));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public RecordComponentDefinition.Optional<S> defineRecordComponent(String name, Type type) {
                return defineRecordComponent(name, TypeDefinition.Sort.describe(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public RecordComponentDefinition.Optional<S> define(RecordComponentDescription recordComponentDescription) {
                return defineRecordComponent(recordComponentDescription.getActualName(), recordComponentDescription.getType());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public RecordComponentDefinition<S> recordComponent(ElementMatcher<? super RecordComponentDescription> matcher) {
                return recordComponent(new LatentMatcher.Resolved(matcher));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> defineField(String name, Type type, ModifierContributor.ForField... modifierContributor) {
                return defineField(name, type, Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> defineField(String name, Type type, Collection<? extends ModifierContributor.ForField> modifierContributors) {
                return defineField(name, type, ModifierContributor.Resolver.of(modifierContributors).resolve());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> defineField(String name, Type type, int modifiers) {
                return defineField(name, TypeDefinition.Sort.describe(type), modifiers);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> defineField(String name, TypeDefinition type, ModifierContributor.ForField... modifierContributor) {
                return defineField(name, type, Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> defineField(String name, TypeDefinition type, Collection<? extends ModifierContributor.ForField> modifierContributors) {
                return defineField(name, type, ModifierContributor.Resolver.of(modifierContributors).resolve());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> define(Field field) {
                return define(new FieldDescription.ForLoadedField(field));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional.Valuable<S> define(FieldDescription field) {
                return defineField(field.getName(), field.getType(), field.getModifiers());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional<S> serialVersionUid(long serialVersionUid) {
                return defineField("serialVersionUID", Long.TYPE, Visibility.PRIVATE, FieldManifestation.FINAL, Ownership.STATIC).value(serialVersionUid);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Valuable<S> field(ElementMatcher<? super FieldDescription> matcher) {
                return field(new LatentMatcher.Resolved(matcher));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> ignoreAlso(ElementMatcher<? super MethodDescription> ignoredMethods) {
                return ignoreAlso(new LatentMatcher.Resolved(ignoredMethods));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineMethod(String name, Type returnType, ModifierContributor.ForMethod... modifierContributor) {
                return defineMethod(name, returnType, Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineMethod(String name, Type returnType, Collection<? extends ModifierContributor.ForMethod> modifierContributors) {
                return defineMethod(name, returnType, ModifierContributor.Resolver.of(modifierContributors).resolve());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineMethod(String name, Type returnType, int modifiers) {
                return defineMethod(name, TypeDefinition.Sort.describe(returnType), modifiers);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineMethod(String name, TypeDefinition returnType, ModifierContributor.ForMethod... modifierContributor) {
                return defineMethod(name, returnType, Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineMethod(String name, TypeDefinition returnType, Collection<? extends ModifierContributor.ForMethod> modifierContributors) {
                return defineMethod(name, returnType, ModifierContributor.Resolver.of(modifierContributors).resolve());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineConstructor(ModifierContributor.ForMethod... modifierContributor) {
                return defineConstructor(Arrays.asList(modifierContributor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ParameterDefinition.Initial<S> defineConstructor(Collection<? extends ModifierContributor.ForMethod> modifierContributors) {
                return defineConstructor(ModifierContributor.Resolver.of(modifierContributors).resolve());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition<S> define(Method method) {
                return define(new MethodDescription.ForLoadedMethod(method));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition<S> define(Constructor<?> constructor) {
                return define(new MethodDescription.ForLoadedConstructor(constructor));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition<S> define(MethodDescription methodDescription) {
                MethodDefinition.ParameterDefinition.Initial<S> defineMethod;
                MethodDefinition.ExceptionDefinition<S> exceptionDefinition;
                if (methodDescription.isConstructor()) {
                    defineMethod = defineConstructor(methodDescription.getModifiers());
                } else {
                    defineMethod = defineMethod(methodDescription.getInternalName(), methodDescription.getReturnType(), methodDescription.getModifiers());
                }
                MethodDefinition.ParameterDefinition.Initial<S> initialParameterDefinition = defineMethod;
                ParameterList<?> parameterList = methodDescription.getParameters();
                if (parameterList.hasExplicitMetaData()) {
                    MethodDefinition.ExceptionDefinition<S> parameterDefinition = initialParameterDefinition;
                    Iterator it = parameterList.iterator();
                    while (it.hasNext()) {
                        ParameterDescription parameter = (ParameterDescription) it.next();
                        parameterDefinition = parameterDefinition.withParameter(parameter.getType(), parameter.getName(), parameter.getModifiers());
                    }
                    exceptionDefinition = parameterDefinition;
                } else {
                    exceptionDefinition = initialParameterDefinition.withParameters((Collection<? extends TypeDefinition>) parameterList.asTypeList());
                }
                MethodDefinition.TypeVariableDefinition<S> typeVariableDefinition = exceptionDefinition.throwing((Collection<? extends TypeDefinition>) methodDescription.getExceptionTypes());
                for (TypeDescription.Generic typeVariable : methodDescription.getTypeVariables()) {
                    typeVariableDefinition = typeVariableDefinition.typeVariable(typeVariable.getSymbol(), typeVariable.getUpperBounds());
                }
                return typeVariableDefinition;
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional<S> defineProperty(String name, Type type) {
                return defineProperty(name, TypeDefinition.Sort.describe(type));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional<S> defineProperty(String name, Type type, boolean readOnly) {
                return defineProperty(name, TypeDefinition.Sort.describe(type), readOnly);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional<S> defineProperty(String name, TypeDefinition type) {
                return defineProperty(name, type, false);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public FieldDefinition.Optional<S> defineProperty(String name, TypeDefinition type, boolean readOnly) {
                FieldManifestation fieldManifestation;
                if (name.length() == 0) {
                    throw new IllegalArgumentException("A bean property cannot have an empty name");
                }
                if (type.represents(Void.TYPE)) {
                    throw new IllegalArgumentException("A bean property cannot have a void type");
                }
                Builder<S> builder = this;
                if (!readOnly) {
                    builder = builder.defineMethod("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1), Void.TYPE, Visibility.PUBLIC).withParameters(type).intercept(FieldAccessor.ofField(name));
                    fieldManifestation = FieldManifestation.PLAIN;
                } else {
                    fieldManifestation = FieldManifestation.FINAL;
                }
                return builder.defineMethod(((type.represents(Boolean.TYPE) || type.represents(Boolean.class)) ? "is" : "get") + Character.toUpperCase(name.charAt(0)) + name.substring(1), type, Visibility.PUBLIC).intercept(FieldAccessor.ofField(name)).defineField(name, type, Visibility.PRIVATE, fieldManifestation);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition<S> method(ElementMatcher<? super MethodDescription> matcher) {
                return invokable(ElementMatchers.isMethod().and(matcher));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition<S> constructor(ElementMatcher<? super MethodDescription> matcher) {
                return invokable(ElementMatchers.isConstructor().and(matcher));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public MethodDefinition.ImplementationDefinition<S> invokable(ElementMatcher<? super MethodDescription> matcher) {
                return invokable(new LatentMatcher.Resolved(matcher));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> withHashCodeEquals() {
                return method(ElementMatchers.isHashCode()).intercept(HashCodeMethod.usingDefaultOffset().withIgnoredFields(ElementMatchers.isSynthetic())).method(ElementMatchers.isEquals()).intercept(EqualsMethod.isolated().withIgnoredFields(ElementMatchers.isSynthetic()));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> withToString() {
                return method(ElementMatchers.isToString()).intercept(ToStringMethod.prefixedBySimpleClassName());
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> require(TypeDescription type, byte[] binaryRepresentation) {
                return require(type, binaryRepresentation, LoadedTypeInitializer.NoOp.INSTANCE);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> require(TypeDescription type, byte[] binaryRepresentation, LoadedTypeInitializer typeInitializer) {
                return require(new Default(type, binaryRepresentation, typeInitializer, Collections.emptyList()));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Builder<S> require(DynamicType... auxiliaryType) {
                return require(Arrays.asList(auxiliaryType));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Unloaded<S> make(TypePool typePool) {
                return make(TypeResolutionStrategy.Passive.INSTANCE, typePool);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Builder
            public Unloaded<S> make() {
                return make(TypeResolutionStrategy.Passive.INSTANCE);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Delegator.class */
            public static abstract class Delegator<U> extends AbstractBase<U> {
                protected abstract Builder<U> materialize();

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> visit(AsmVisitorWrapper asmVisitorWrapper) {
                    return materialize().visit(asmVisitorWrapper);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> initializer(LoadedTypeInitializer loadedTypeInitializer) {
                    return materialize().initializer(loadedTypeInitializer);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> annotateType(Collection<? extends AnnotationDescription> annotations) {
                    return materialize().annotateType(annotations);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> attribute(TypeAttributeAppender typeAttributeAppender) {
                    return materialize().attribute(typeAttributeAppender);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> modifiers(int modifiers) {
                    return materialize().modifiers(modifiers);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> merge(Collection<? extends ModifierContributor.ForType> modifierContributors) {
                    return materialize().merge(modifierContributors);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> suffix(String suffix) {
                    return materialize().suffix(suffix);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> name(String name) {
                    return materialize().name(name);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> topLevelType() {
                    return materialize().topLevelType();
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public InnerTypeDefinition.ForType<U> innerTypeOf(TypeDescription type) {
                    return materialize().innerTypeOf(type);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public InnerTypeDefinition<U> innerTypeOf(MethodDescription.InDefinedShape methodDescription) {
                    return materialize().innerTypeOf(methodDescription);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> declaredTypes(Collection<? extends TypeDescription> types) {
                    return materialize().declaredTypes(types);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> nestHost(TypeDescription type) {
                    return materialize().nestHost(type);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> nestMembers(Collection<? extends TypeDescription> types) {
                    return materialize().nestMembers(types);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> permittedSubclass(Collection<? extends TypeDescription> types) {
                    return materialize().permittedSubclass(types);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> unsealed() {
                    return materialize().unsealed();
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ImplementationDefinition.Optional<U> implement(Collection<? extends TypeDefinition> interfaceTypes) {
                    return materialize().implement(interfaceTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> initializer(ByteCodeAppender byteCodeAppender) {
                    return materialize().initializer(byteCodeAppender);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> ignoreAlso(ElementMatcher<? super MethodDescription> ignoredMethods) {
                    return materialize().ignoreAlso(ignoredMethods);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> ignoreAlso(LatentMatcher<? super MethodDescription> ignoredMethods) {
                    return materialize().ignoreAlso(ignoredMethods);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public TypeVariableDefinition<U> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
                    return materialize().typeVariable(symbol, bounds);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> transform(ElementMatcher<? super TypeDescription.Generic> matcher, Transformer<TypeVariableToken> transformer) {
                    return materialize().transform(matcher, transformer);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public FieldDefinition.Optional.Valuable<U> defineField(String name, TypeDefinition type, int modifiers) {
                    return materialize().defineField(name, type, modifiers);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public FieldDefinition.Valuable<U> field(LatentMatcher<? super FieldDescription> matcher) {
                    return materialize().field(matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ParameterDefinition.Initial<U> defineMethod(String name, TypeDefinition returnType, int modifiers) {
                    return materialize().defineMethod(name, returnType, modifiers);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ParameterDefinition.Initial<U> defineConstructor(int modifiers) {
                    return materialize().defineConstructor(modifiers);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ImplementationDefinition<U> invokable(LatentMatcher<? super MethodDescription> matcher) {
                    return materialize().invokable(matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> require(Collection<DynamicType> auxiliaryTypes) {
                    return materialize().require(auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public RecordComponentDefinition.Optional<U> defineRecordComponent(String name, TypeDefinition type) {
                    return materialize().defineRecordComponent(name, type);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder
                public RecordComponentDefinition.Optional<U> define(RecordComponentDescription recordComponentDescription) {
                    return materialize().define(recordComponentDescription);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder
                public RecordComponentDefinition<U> recordComponent(ElementMatcher<? super RecordComponentDescription> matcher) {
                    return materialize().recordComponent(matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public RecordComponentDefinition<U> recordComponent(LatentMatcher<? super RecordComponentDescription> matcher) {
                    return materialize().recordComponent(matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder
                public Unloaded<U> make() {
                    return materialize().make();
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Unloaded<U> make(TypeResolutionStrategy typeResolutionStrategy) {
                    return materialize().make(typeResolutionStrategy);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase, net.bytebuddy.dynamic.DynamicType.Builder
                public Unloaded<U> make(TypePool typePool) {
                    return materialize().make(typePool);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Unloaded<U> make(TypeResolutionStrategy typeResolutionStrategy, TypePool typePool) {
                    return materialize().make(typeResolutionStrategy, typePool);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public TypeDescription toTypeDescription() {
                    return materialize().toTypeDescription();
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter.class */
            public static abstract class Adapter<U> extends AbstractBase<U> {
                protected final InstrumentedType.WithFlexibleName instrumentedType;
                protected final FieldRegistry fieldRegistry;
                protected final MethodRegistry methodRegistry;
                protected final RecordComponentRegistry recordComponentRegistry;
                protected final TypeAttributeAppender typeAttributeAppender;
                protected final AsmVisitorWrapper asmVisitorWrapper;
                protected final ClassFileVersion classFileVersion;
                protected final AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy;
                protected final AnnotationValueFilter.Factory annotationValueFilterFactory;
                protected final AnnotationRetention annotationRetention;
                protected final Implementation.Context.Factory implementationContextFactory;
                protected final MethodGraph.Compiler methodGraphCompiler;
                protected final TypeValidation typeValidation;
                protected final VisibilityBridgeStrategy visibilityBridgeStrategy;
                protected final ClassWriterStrategy classWriterStrategy;
                protected final LatentMatcher<? super MethodDescription> ignoredMethods;
                protected final List<? extends DynamicType> auxiliaryTypes;

                protected abstract Builder<U> materialize(InstrumentedType.WithFlexibleName withFlexibleName, FieldRegistry fieldRegistry, MethodRegistry methodRegistry, RecordComponentRegistry recordComponentRegistry, TypeAttributeAppender typeAttributeAppender, AsmVisitorWrapper asmVisitorWrapper, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy namingStrategy, AnnotationValueFilter.Factory factory, AnnotationRetention annotationRetention, Implementation.Context.Factory factory2, MethodGraph.Compiler compiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> latentMatcher, List<? extends DynamicType> list);

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.annotationRetention.equals(((Adapter) obj).annotationRetention) && this.typeValidation.equals(((Adapter) obj).typeValidation) && this.instrumentedType.equals(((Adapter) obj).instrumentedType) && this.fieldRegistry.equals(((Adapter) obj).fieldRegistry) && this.methodRegistry.equals(((Adapter) obj).methodRegistry) && this.recordComponentRegistry.equals(((Adapter) obj).recordComponentRegistry) && this.typeAttributeAppender.equals(((Adapter) obj).typeAttributeAppender) && this.asmVisitorWrapper.equals(((Adapter) obj).asmVisitorWrapper) && this.classFileVersion.equals(((Adapter) obj).classFileVersion) && this.auxiliaryTypeNamingStrategy.equals(((Adapter) obj).auxiliaryTypeNamingStrategy) && this.annotationValueFilterFactory.equals(((Adapter) obj).annotationValueFilterFactory) && this.implementationContextFactory.equals(((Adapter) obj).implementationContextFactory) && this.methodGraphCompiler.equals(((Adapter) obj).methodGraphCompiler) && this.visibilityBridgeStrategy.equals(((Adapter) obj).visibilityBridgeStrategy) && this.classWriterStrategy.equals(((Adapter) obj).classWriterStrategy) && this.ignoredMethods.equals(((Adapter) obj).ignoredMethods) && this.auxiliaryTypes.equals(((Adapter) obj).auxiliaryTypes);
                }

                public int hashCode() {
                    return (((((((((((((((((((((((((((((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.fieldRegistry.hashCode()) * 31) + this.methodRegistry.hashCode()) * 31) + this.recordComponentRegistry.hashCode()) * 31) + this.typeAttributeAppender.hashCode()) * 31) + this.asmVisitorWrapper.hashCode()) * 31) + this.classFileVersion.hashCode()) * 31) + this.auxiliaryTypeNamingStrategy.hashCode()) * 31) + this.annotationValueFilterFactory.hashCode()) * 31) + this.annotationRetention.hashCode()) * 31) + this.implementationContextFactory.hashCode()) * 31) + this.methodGraphCompiler.hashCode()) * 31) + this.typeValidation.hashCode()) * 31) + this.visibilityBridgeStrategy.hashCode()) * 31) + this.classWriterStrategy.hashCode()) * 31) + this.ignoredMethods.hashCode()) * 31) + this.auxiliaryTypes.hashCode();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public Adapter(InstrumentedType.WithFlexibleName instrumentedType, FieldRegistry fieldRegistry, MethodRegistry methodRegistry, RecordComponentRegistry recordComponentRegistry, TypeAttributeAppender typeAttributeAppender, AsmVisitorWrapper asmVisitorWrapper, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods, List<? extends DynamicType> auxiliaryTypes) {
                    this.instrumentedType = instrumentedType;
                    this.fieldRegistry = fieldRegistry;
                    this.methodRegistry = methodRegistry;
                    this.recordComponentRegistry = recordComponentRegistry;
                    this.typeAttributeAppender = typeAttributeAppender;
                    this.asmVisitorWrapper = asmVisitorWrapper;
                    this.classFileVersion = classFileVersion;
                    this.auxiliaryTypeNamingStrategy = auxiliaryTypeNamingStrategy;
                    this.annotationValueFilterFactory = annotationValueFilterFactory;
                    this.annotationRetention = annotationRetention;
                    this.implementationContextFactory = implementationContextFactory;
                    this.methodGraphCompiler = methodGraphCompiler;
                    this.typeValidation = typeValidation;
                    this.visibilityBridgeStrategy = visibilityBridgeStrategy;
                    this.classWriterStrategy = classWriterStrategy;
                    this.ignoredMethods = ignoredMethods;
                    this.auxiliaryTypes = auxiliaryTypes;
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public FieldDefinition.Optional.Valuable<U> defineField(String name, TypeDefinition type, int modifiers) {
                    return new FieldDefinitionAdapter(this, new FieldDescription.Token(name, modifiers, type.asGenericType()));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public FieldDefinition.Valuable<U> field(LatentMatcher<? super FieldDescription> matcher) {
                    return new FieldMatchAdapter(this, matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ParameterDefinition.Initial<U> defineMethod(String name, TypeDefinition returnType, int modifiers) {
                    return new MethodDefinitionAdapter(new MethodDescription.Token(name, modifiers, returnType.asGenericType()));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ParameterDefinition.Initial<U> defineConstructor(int modifiers) {
                    return new MethodDefinitionAdapter(new MethodDescription.Token(modifiers));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ImplementationDefinition<U> invokable(LatentMatcher<? super MethodDescription> matcher) {
                    return new MethodMatchAdapter(matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public MethodDefinition.ImplementationDefinition.Optional<U> implement(Collection<? extends TypeDefinition> interfaceTypes) {
                    return new OptionalMethodMatchAdapter(new TypeList.Generic.Explicit(new ArrayList(interfaceTypes)));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> ignoreAlso(LatentMatcher<? super MethodDescription> ignoredMethods) {
                    return materialize(this.instrumentedType, this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, new LatentMatcher.Disjunction(this.ignoredMethods, ignoredMethods), this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public RecordComponentDefinition.Optional<U> defineRecordComponent(String name, TypeDefinition type) {
                    return new RecordComponentDefinitionAdapter(this, new RecordComponentDescription.Token(name, type.asGenericType()));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public RecordComponentDefinition<U> recordComponent(LatentMatcher<? super RecordComponentDescription> matcher) {
                    return new RecordComponentMatchAdapter(this, matcher);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> initializer(ByteCodeAppender byteCodeAppender) {
                    return materialize(this.instrumentedType.withInitializer(byteCodeAppender), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> initializer(LoadedTypeInitializer loadedTypeInitializer) {
                    return materialize(this.instrumentedType.withInitializer(loadedTypeInitializer), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> name(String name) {
                    return materialize(this.instrumentedType.withName(name), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> suffix(String suffix) {
                    return name(this.instrumentedType.getName() + "$" + suffix);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> modifiers(int modifiers) {
                    return materialize(this.instrumentedType.withModifiers(modifiers), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> merge(Collection<? extends ModifierContributor.ForType> modifierContributors) {
                    return materialize(this.instrumentedType.withModifiers(ModifierContributor.Resolver.of(modifierContributors).resolve(this.instrumentedType.getModifiers())), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> topLevelType() {
                    return materialize(this.instrumentedType.withDeclaringType(TypeDescription.UNDEFINED).withEnclosingType(TypeDescription.UNDEFINED).withLocalClass(false), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public InnerTypeDefinition.ForType<U> innerTypeOf(TypeDescription type) {
                    return new InnerTypeDefinitionForTypeAdapter(type);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public InnerTypeDefinition<U> innerTypeOf(MethodDescription.InDefinedShape methodDescription) {
                    return methodDescription.isTypeInitializer() ? new InnerTypeDefinitionForTypeAdapter(methodDescription.getDeclaringType()) : new InnerTypeDefinitionForMethodAdapter(methodDescription);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> declaredTypes(Collection<? extends TypeDescription> types) {
                    return materialize(this.instrumentedType.withDeclaredTypes((TypeList) new TypeList.Explicit(new ArrayList(types))), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> nestHost(TypeDescription type) {
                    return materialize(this.instrumentedType.withNestHost(type), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> nestMembers(Collection<? extends TypeDescription> types) {
                    return materialize(this.instrumentedType.withNestMembers((TypeList) new TypeList.Explicit(new ArrayList(types))), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> permittedSubclass(Collection<? extends TypeDescription> types) {
                    return materialize(this.instrumentedType.withPermittedSubclasses((TypeList) new TypeList.Explicit(new ArrayList(types))), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> unsealed() {
                    return materialize(this.instrumentedType.withSealed(false), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public TypeVariableDefinition<U> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
                    return new TypeVariableDefinitionAdapter(new TypeVariableToken(symbol, new TypeList.Generic.Explicit(new ArrayList(bounds))));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> transform(ElementMatcher<? super TypeDescription.Generic> matcher, Transformer<TypeVariableToken> transformer) {
                    return materialize(this.instrumentedType.withTypeVariables(matcher, transformer), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> attribute(TypeAttributeAppender typeAttributeAppender) {
                    return materialize(this.instrumentedType, this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, new TypeAttributeAppender.Compound(this.typeAttributeAppender, typeAttributeAppender), this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> annotateType(Collection<? extends AnnotationDescription> annotations) {
                    return materialize(this.instrumentedType.withAnnotations((List<? extends AnnotationDescription>) new ArrayList(annotations)), this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> visit(AsmVisitorWrapper asmVisitorWrapper) {
                    return materialize(this.instrumentedType, this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, new AsmVisitorWrapper.Compound(this.asmVisitorWrapper, asmVisitorWrapper), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes);
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public Builder<U> require(Collection<DynamicType> auxiliaryTypes) {
                    return materialize(this.instrumentedType, this.fieldRegistry, this.methodRegistry, this.recordComponentRegistry, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, this.classWriterStrategy, this.ignoredMethods, CompoundList.of((List) this.auxiliaryTypes, (List) new ArrayList(auxiliaryTypes)));
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Builder
                public TypeDescription toTypeDescription() {
                    return this.instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$InnerTypeDefinitionForTypeAdapter.class */
                protected class InnerTypeDefinitionForTypeAdapter extends Delegator<U> implements InnerTypeDefinition.ForType<U> {
                    private final TypeDescription typeDescription;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((InnerTypeDefinitionForTypeAdapter) obj).typeDescription) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.typeDescription.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected InnerTypeDefinitionForTypeAdapter(TypeDescription typeDescription) {
                        this.typeDescription = typeDescription;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.InnerTypeDefinition
                    public Builder<U> asAnonymousType() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withDeclaringType(TypeDescription.UNDEFINED).withEnclosingType(this.typeDescription).withAnonymousClass(true), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.InnerTypeDefinition.ForType
                    public Builder<U> asMemberType() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withDeclaringType(this.typeDescription).withEnclosingType(this.typeDescription).withAnonymousClass(false).withLocalClass(false), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withDeclaringType(TypeDescription.UNDEFINED).withEnclosingType(this.typeDescription).withLocalClass(true), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$InnerTypeDefinitionForMethodAdapter.class */
                protected class InnerTypeDefinitionForMethodAdapter extends Delegator<U> implements InnerTypeDefinition<U> {
                    private final MethodDescription.InDefinedShape methodDescription;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((InnerTypeDefinitionForMethodAdapter) obj).methodDescription) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.methodDescription.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected InnerTypeDefinitionForMethodAdapter(MethodDescription.InDefinedShape methodDescription) {
                        this.methodDescription = methodDescription;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.InnerTypeDefinition
                    public Builder<U> asAnonymousType() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withDeclaringType(TypeDescription.UNDEFINED).withEnclosingMethod(this.methodDescription).withAnonymousClass(true), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withDeclaringType(TypeDescription.UNDEFINED).withEnclosingMethod(this.methodDescription).withLocalClass(true), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$TypeVariableDefinitionAdapter.class */
                protected class TypeVariableDefinitionAdapter extends TypeVariableDefinition.AbstractBase<U> {
                    private final TypeVariableToken token;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.token.equals(((TypeVariableDefinitionAdapter) obj).token) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.token.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected TypeVariableDefinitionAdapter(TypeVariableToken token) {
                        this.token = token;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.TypeVariableDefinition
                    public TypeVariableDefinition<U> annotateTypeVariable(Collection<? extends AnnotationDescription> annotations) {
                        return new TypeVariableDefinitionAdapter(new TypeVariableToken(this.token.getSymbol(), this.token.getBounds(), CompoundList.of((List) this.token.getAnnotations(), (List) new ArrayList(annotations))));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withTypeVariable(this.token), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$FieldDefinitionAdapter.class */
                protected class FieldDefinitionAdapter extends FieldDefinition.Optional.Valuable.AbstractBase.Adapter<U> {
                    private final FieldDescription.Token token;

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase.Adapter
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.token.equals(((FieldDefinitionAdapter) obj).token) && Adapter.this.equals(Adapter.this);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase.Adapter
                    public int hashCode() {
                        return (((super.hashCode() * 31) + this.token.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected FieldDefinitionAdapter(Adapter this$0, FieldDescription.Token token) {
                        this(FieldAttributeAppender.ForInstrumentedField.INSTANCE, Transformer.NoOp.make(), FieldDescription.NO_DEFAULT_VALUE, token);
                    }

                    protected FieldDefinitionAdapter(FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Transformer<FieldDescription> transformer, Object defaultValue, FieldDescription.Token token) {
                        super(fieldAttributeAppenderFactory, transformer, defaultValue);
                        this.token = token;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                    public FieldDefinition.Optional<U> annotateField(Collection<? extends AnnotationDescription> annotations) {
                        return new FieldDefinitionAdapter(this.fieldAttributeAppenderFactory, this.transformer, this.defaultValue, new FieldDescription.Token(this.token.getName(), this.token.getModifiers(), this.token.getType(), CompoundList.of((List) this.token.getAnnotations(), (List) new ArrayList(annotations))));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withField(this.token), Adapter.this.fieldRegistry.prepend(new LatentMatcher.ForFieldToken(this.token), this.fieldAttributeAppenderFactory, this.defaultValue, this.transformer), Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase.Adapter
                    protected FieldDefinition.Optional<U> materialize(FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Transformer<FieldDescription> transformer, Object defaultValue) {
                        return new FieldDefinitionAdapter(fieldAttributeAppenderFactory, transformer, defaultValue, this.token);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$FieldMatchAdapter.class */
                protected class FieldMatchAdapter extends FieldDefinition.Optional.Valuable.AbstractBase.Adapter<U> {
                    private final LatentMatcher<? super FieldDescription> matcher;

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase.Adapter
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.matcher.equals(((FieldMatchAdapter) obj).matcher) && Adapter.this.equals(Adapter.this);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase.Adapter
                    public int hashCode() {
                        return (((super.hashCode() * 31) + this.matcher.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected FieldMatchAdapter(Adapter this$0, LatentMatcher<? super FieldDescription> matcher) {
                        this(FieldAttributeAppender.NoOp.INSTANCE, Transformer.NoOp.make(), FieldDescription.NO_DEFAULT_VALUE, matcher);
                    }

                    protected FieldMatchAdapter(FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Transformer<FieldDescription> transformer, Object defaultValue, LatentMatcher<? super FieldDescription> matcher) {
                        super(fieldAttributeAppenderFactory, transformer, defaultValue);
                        this.matcher = matcher;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition
                    public FieldDefinition.Optional<U> annotateField(Collection<? extends AnnotationDescription> annotations) {
                        return attribute(new FieldAttributeAppender.Explicit(new ArrayList(annotations)));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType, Adapter.this.fieldRegistry.prepend(this.matcher, this.fieldAttributeAppenderFactory, this.defaultValue, this.transformer), Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable.AbstractBase.Adapter
                    protected FieldDefinition.Optional<U> materialize(FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Transformer<FieldDescription> transformer, Object defaultValue) {
                        return new FieldMatchAdapter(fieldAttributeAppenderFactory, transformer, defaultValue, this.matcher);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodDefinitionAdapter.class */
                protected class MethodDefinitionAdapter extends MethodDefinition.ParameterDefinition.Initial.AbstractBase<U> {
                    private final MethodDescription.Token token;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.token.equals(((MethodDefinitionAdapter) obj).token) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.token.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected MethodDefinitionAdapter(MethodDescription.Token token) {
                        this.token = token;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition
                    public MethodDefinition.ParameterDefinition.Annotatable<U> withParameter(TypeDefinition type, String name, int modifiers) {
                        return new ParameterAnnotationAdapter(new ParameterDescription.Token(type.asGenericType(), name, Integer.valueOf(modifiers)));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple
                    public MethodDefinition.ParameterDefinition.Simple.Annotatable<U> withParameter(TypeDefinition type) {
                        return new SimpleParameterAnnotationAdapter(new ParameterDescription.Token(type.asGenericType()));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ExceptionDefinition
                    public MethodDefinition.ExceptionDefinition<U> throwing(Collection<? extends TypeDefinition> types) {
                        return new MethodDefinitionAdapter(new MethodDescription.Token(this.token.getName(), this.token.getModifiers(), this.token.getTypeVariableTokens(), this.token.getReturnType(), this.token.getParameterTokens(), CompoundList.of((List) this.token.getExceptionTypes(), (List) new TypeList.Generic.Explicit(new ArrayList(types))), this.token.getAnnotations(), this.token.getDefaultValue(), this.token.getReceiverType()));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition
                    public MethodDefinition.TypeVariableDefinition.Annotatable<U> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
                        return new TypeVariableAnnotationAdapter(new TypeVariableToken(symbol, new TypeList.Generic.Explicit(new ArrayList(bounds))));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> intercept(Implementation implementation) {
                        return materialize(new MethodRegistry.Handler.ForImplementation(implementation));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> withoutCode() {
                        int modifiers;
                        Adapter adapter = Adapter.this;
                        String name = this.token.getName();
                        if ((this.token.getModifiers() & 256) == 0) {
                            modifiers = ModifierContributor.Resolver.of(MethodManifestation.ABSTRACT).resolve(this.token.getModifiers());
                        } else {
                            modifiers = this.token.getModifiers();
                        }
                        return new MethodDefinitionAdapter(new MethodDescription.Token(name, modifiers, this.token.getTypeVariableTokens(), this.token.getReturnType(), this.token.getParameterTokens(), this.token.getExceptionTypes(), this.token.getAnnotations(), this.token.getDefaultValue(), this.token.getReceiverType())).materialize(MethodRegistry.Handler.ForAbstractMethod.INSTANCE);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> defaultValue(AnnotationValue<?, ?> annotationValue) {
                        return new MethodDefinitionAdapter(new MethodDescription.Token(this.token.getName(), ModifierContributor.Resolver.of(MethodManifestation.ABSTRACT).resolve(this.token.getModifiers()), this.token.getTypeVariableTokens(), this.token.getReturnType(), this.token.getParameterTokens(), this.token.getExceptionTypes(), this.token.getAnnotations(), annotationValue, this.token.getReceiverType())).materialize(new MethodRegistry.Handler.ForAnnotationValue(annotationValue));
                    }

                    private MethodDefinition.ReceiverTypeDefinition<U> materialize(MethodRegistry.Handler handler) {
                        return new AnnotationAdapter(this, handler);
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodDefinitionAdapter$TypeVariableAnnotationAdapter.class */
                    protected class TypeVariableAnnotationAdapter extends MethodDefinition.TypeVariableDefinition.Annotatable.AbstractBase.Adapter<U> {
                        private final TypeVariableToken token;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.token.equals(((TypeVariableAnnotationAdapter) obj).token) && MethodDefinitionAdapter.this.equals(MethodDefinitionAdapter.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.token.hashCode()) * 31) + MethodDefinitionAdapter.this.hashCode();
                        }

                        protected TypeVariableAnnotationAdapter(TypeVariableToken token) {
                            this.token = token;
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition.Annotatable.AbstractBase.Adapter
                        protected MethodDefinition.ParameterDefinition<U> materialize() {
                            return new MethodDefinitionAdapter(new MethodDescription.Token(MethodDefinitionAdapter.this.token.getName(), MethodDefinitionAdapter.this.token.getModifiers(), CompoundList.of(MethodDefinitionAdapter.this.token.getTypeVariableTokens(), this.token), MethodDefinitionAdapter.this.token.getReturnType(), MethodDefinitionAdapter.this.token.getParameterTokens(), MethodDefinitionAdapter.this.token.getExceptionTypes(), MethodDefinitionAdapter.this.token.getAnnotations(), MethodDefinitionAdapter.this.token.getDefaultValue(), MethodDefinitionAdapter.this.token.getReceiverType()));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.TypeVariableDefinition.Annotatable
                        public MethodDefinition.TypeVariableDefinition.Annotatable<U> annotateTypeVariable(Collection<? extends AnnotationDescription> annotations) {
                            return new TypeVariableAnnotationAdapter(new TypeVariableToken(this.token.getSymbol(), this.token.getBounds(), CompoundList.of((List) this.token.getAnnotations(), (List) new ArrayList(annotations))));
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodDefinitionAdapter$ParameterAnnotationAdapter.class */
                    protected class ParameterAnnotationAdapter extends MethodDefinition.ParameterDefinition.Annotatable.AbstractBase.Adapter<U> {
                        private final ParameterDescription.Token token;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.token.equals(((ParameterAnnotationAdapter) obj).token) && MethodDefinitionAdapter.this.equals(MethodDefinitionAdapter.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.token.hashCode()) * 31) + MethodDefinitionAdapter.this.hashCode();
                        }

                        protected ParameterAnnotationAdapter(ParameterDescription.Token token) {
                            this.token = token;
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Annotatable
                        public MethodDefinition.ParameterDefinition.Annotatable<U> annotateParameter(Collection<? extends AnnotationDescription> annotations) {
                            return new ParameterAnnotationAdapter(new ParameterDescription.Token(this.token.getType(), CompoundList.of((List) this.token.getAnnotations(), (List) new ArrayList(annotations)), this.token.getName(), this.token.getModifiers()));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Annotatable.AbstractBase.Adapter
                        protected MethodDefinition.ParameterDefinition<U> materialize() {
                            return new MethodDefinitionAdapter(new MethodDescription.Token(MethodDefinitionAdapter.this.token.getName(), MethodDefinitionAdapter.this.token.getModifiers(), MethodDefinitionAdapter.this.token.getTypeVariableTokens(), MethodDefinitionAdapter.this.token.getReturnType(), CompoundList.of(MethodDefinitionAdapter.this.token.getParameterTokens(), this.token), MethodDefinitionAdapter.this.token.getExceptionTypes(), MethodDefinitionAdapter.this.token.getAnnotations(), MethodDefinitionAdapter.this.token.getDefaultValue(), MethodDefinitionAdapter.this.token.getReceiverType()));
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodDefinitionAdapter$SimpleParameterAnnotationAdapter.class */
                    protected class SimpleParameterAnnotationAdapter extends MethodDefinition.ParameterDefinition.Simple.Annotatable.AbstractBase.Adapter<U> {
                        private final ParameterDescription.Token token;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.token.equals(((SimpleParameterAnnotationAdapter) obj).token) && MethodDefinitionAdapter.this.equals(MethodDefinitionAdapter.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.token.hashCode()) * 31) + MethodDefinitionAdapter.this.hashCode();
                        }

                        protected SimpleParameterAnnotationAdapter(ParameterDescription.Token token) {
                            this.token = token;
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple.Annotatable
                        public MethodDefinition.ParameterDefinition.Simple.Annotatable<U> annotateParameter(Collection<? extends AnnotationDescription> annotations) {
                            return new SimpleParameterAnnotationAdapter(new ParameterDescription.Token(this.token.getType(), CompoundList.of((List) this.token.getAnnotations(), (List) new ArrayList(annotations)), this.token.getName(), this.token.getModifiers()));
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ParameterDefinition.Simple.Annotatable.AbstractBase.Adapter
                        protected MethodDefinition.ParameterDefinition.Simple<U> materialize() {
                            return new MethodDefinitionAdapter(new MethodDescription.Token(MethodDefinitionAdapter.this.token.getName(), MethodDefinitionAdapter.this.token.getModifiers(), MethodDefinitionAdapter.this.token.getTypeVariableTokens(), MethodDefinitionAdapter.this.token.getReturnType(), CompoundList.of(MethodDefinitionAdapter.this.token.getParameterTokens(), this.token), MethodDefinitionAdapter.this.token.getExceptionTypes(), MethodDefinitionAdapter.this.token.getAnnotations(), MethodDefinitionAdapter.this.token.getDefaultValue(), MethodDefinitionAdapter.this.token.getReceiverType()));
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodDefinitionAdapter$AnnotationAdapter.class */
                    public class AnnotationAdapter extends MethodDefinition.AbstractBase.Adapter<U> {
                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.AbstractBase.Adapter
                        public boolean equals(Object obj) {
                            if (super.equals(obj)) {
                                if (this == obj) {
                                    return true;
                                }
                                return obj != null && getClass() == obj.getClass() && MethodDefinitionAdapter.this.equals(MethodDefinitionAdapter.this);
                            }
                            return false;
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.AbstractBase.Adapter
                        public int hashCode() {
                            return (super.hashCode() * 31) + MethodDefinitionAdapter.this.hashCode();
                        }

                        protected AnnotationAdapter(MethodDefinitionAdapter this$1, MethodRegistry.Handler handler) {
                            this(handler, MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER, Transformer.NoOp.make());
                        }

                        protected AnnotationAdapter(MethodRegistry.Handler handler, MethodAttributeAppender.Factory methodAttributeAppenderFactory, Transformer<MethodDescription> transformer) {
                            super(handler, methodAttributeAppenderFactory, transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition
                        public MethodDefinition<U> receiverType(TypeDescription.Generic receiverType) {
                            MethodDefinitionAdapter methodDefinitionAdapter = new MethodDefinitionAdapter(new MethodDescription.Token(MethodDefinitionAdapter.this.token.getName(), MethodDefinitionAdapter.this.token.getModifiers(), MethodDefinitionAdapter.this.token.getTypeVariableTokens(), MethodDefinitionAdapter.this.token.getReturnType(), MethodDefinitionAdapter.this.token.getParameterTokens(), MethodDefinitionAdapter.this.token.getExceptionTypes(), MethodDefinitionAdapter.this.token.getAnnotations(), MethodDefinitionAdapter.this.token.getDefaultValue(), receiverType));
                            methodDefinitionAdapter.getClass();
                            return new AnnotationAdapter(this.handler, this.methodAttributeAppenderFactory, this.transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                        public MethodDefinition<U> annotateMethod(Collection<? extends AnnotationDescription> annotations) {
                            MethodDefinitionAdapter methodDefinitionAdapter = new MethodDefinitionAdapter(new MethodDescription.Token(MethodDefinitionAdapter.this.token.getName(), MethodDefinitionAdapter.this.token.getModifiers(), MethodDefinitionAdapter.this.token.getTypeVariableTokens(), MethodDefinitionAdapter.this.token.getReturnType(), MethodDefinitionAdapter.this.token.getParameterTokens(), MethodDefinitionAdapter.this.token.getExceptionTypes(), CompoundList.of((List) MethodDefinitionAdapter.this.token.getAnnotations(), (List) new ArrayList(annotations)), MethodDefinitionAdapter.this.token.getDefaultValue(), MethodDefinitionAdapter.this.token.getReceiverType()));
                            methodDefinitionAdapter.getClass();
                            return new AnnotationAdapter(this.handler, this.methodAttributeAppenderFactory, this.transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                        public MethodDefinition<U> annotateParameter(int index, Collection<? extends AnnotationDescription> annotations) {
                            List<ParameterDescription.Token> parameterTokens = new ArrayList<>(MethodDefinitionAdapter.this.token.getParameterTokens());
                            parameterTokens.set(index, new ParameterDescription.Token(MethodDefinitionAdapter.this.token.getParameterTokens().get(index).getType(), CompoundList.of((List) MethodDefinitionAdapter.this.token.getParameterTokens().get(index).getAnnotations(), (List) new ArrayList(annotations)), MethodDefinitionAdapter.this.token.getParameterTokens().get(index).getName(), MethodDefinitionAdapter.this.token.getParameterTokens().get(index).getModifiers()));
                            MethodDefinitionAdapter methodDefinitionAdapter = new MethodDefinitionAdapter(new MethodDescription.Token(MethodDefinitionAdapter.this.token.getName(), MethodDefinitionAdapter.this.token.getModifiers(), MethodDefinitionAdapter.this.token.getTypeVariableTokens(), MethodDefinitionAdapter.this.token.getReturnType(), parameterTokens, MethodDefinitionAdapter.this.token.getExceptionTypes(), MethodDefinitionAdapter.this.token.getAnnotations(), MethodDefinitionAdapter.this.token.getDefaultValue(), MethodDefinitionAdapter.this.token.getReceiverType()));
                            methodDefinitionAdapter.getClass();
                            return new AnnotationAdapter(this.handler, this.methodAttributeAppenderFactory, this.transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.AbstractBase.Adapter
                        protected MethodDefinition<U> materialize(MethodRegistry.Handler handler, MethodAttributeAppender.Factory methodAttributeAppenderFactory, Transformer<MethodDescription> transformer) {
                            return new AnnotationAdapter(handler, methodAttributeAppenderFactory, transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                        protected Builder<U> materialize() {
                            return Adapter.this.materialize(Adapter.this.instrumentedType.withMethod(MethodDefinitionAdapter.this.token), Adapter.this.fieldRegistry, Adapter.this.methodRegistry.prepend(new LatentMatcher.ForMethodToken(MethodDefinitionAdapter.this.token), this.handler, this.methodAttributeAppenderFactory, this.transformer), Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodMatchAdapter.class */
                protected class MethodMatchAdapter extends MethodDefinition.ImplementationDefinition.AbstractBase<U> {
                    private final LatentMatcher<? super MethodDescription> matcher;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.matcher.equals(((MethodMatchAdapter) obj).matcher) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.matcher.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected MethodMatchAdapter(LatentMatcher<? super MethodDescription> matcher) {
                        this.matcher = matcher;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> intercept(Implementation implementation) {
                        return materialize(new MethodRegistry.Handler.ForImplementation(implementation));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> withoutCode() {
                        return materialize(MethodRegistry.Handler.ForAbstractMethod.INSTANCE);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> defaultValue(AnnotationValue<?, ?> annotationValue) {
                        return materialize(new MethodRegistry.Handler.ForAnnotationValue(annotationValue));
                    }

                    private MethodDefinition.ReceiverTypeDefinition<U> materialize(MethodRegistry.Handler handler) {
                        return new AnnotationAdapter(this, handler);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$MethodMatchAdapter$AnnotationAdapter.class */
                    public class AnnotationAdapter extends MethodDefinition.AbstractBase.Adapter<U> {
                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.AbstractBase.Adapter
                        public boolean equals(Object obj) {
                            if (super.equals(obj)) {
                                if (this == obj) {
                                    return true;
                                }
                                return obj != null && getClass() == obj.getClass() && MethodMatchAdapter.this.equals(MethodMatchAdapter.this);
                            }
                            return false;
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.AbstractBase.Adapter
                        public int hashCode() {
                            return (super.hashCode() * 31) + MethodMatchAdapter.this.hashCode();
                        }

                        protected AnnotationAdapter(MethodMatchAdapter this$1, MethodRegistry.Handler handler) {
                            this(handler, MethodAttributeAppender.NoOp.INSTANCE, Transformer.NoOp.make());
                        }

                        protected AnnotationAdapter(MethodRegistry.Handler handler, MethodAttributeAppender.Factory methodAttributeAppenderFactory, Transformer<MethodDescription> transformer) {
                            super(handler, methodAttributeAppenderFactory, transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition
                        public MethodDefinition<U> receiverType(TypeDescription.Generic receiverType) {
                            return new AnnotationAdapter(this.handler, new MethodAttributeAppender.Factory.Compound(this.methodAttributeAppenderFactory, new MethodAttributeAppender.ForReceiverType(receiverType)), this.transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                        public MethodDefinition<U> annotateMethod(Collection<? extends AnnotationDescription> annotations) {
                            return new AnnotationAdapter(this.handler, new MethodAttributeAppender.Factory.Compound(this.methodAttributeAppenderFactory, new MethodAttributeAppender.Explicit(new ArrayList(annotations))), this.transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition
                        public MethodDefinition<U> annotateParameter(int index, Collection<? extends AnnotationDescription> annotations) {
                            return new AnnotationAdapter(this.handler, new MethodAttributeAppender.Factory.Compound(this.methodAttributeAppenderFactory, new MethodAttributeAppender.Explicit(index, new ArrayList(annotations))), this.transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.AbstractBase.Adapter
                        protected MethodDefinition<U> materialize(MethodRegistry.Handler handler, MethodAttributeAppender.Factory methodAttributeAppenderFactory, Transformer<MethodDescription> transformer) {
                            return new AnnotationAdapter(handler, methodAttributeAppenderFactory, transformer);
                        }

                        @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                        protected Builder<U> materialize() {
                            return Adapter.this.materialize(Adapter.this.instrumentedType, Adapter.this.fieldRegistry, Adapter.this.methodRegistry.prepend(MethodMatchAdapter.this.matcher, this.handler, this.methodAttributeAppenderFactory, this.transformer), Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$OptionalMethodMatchAdapter.class */
                protected class OptionalMethodMatchAdapter extends Delegator<U> implements MethodDefinition.ImplementationDefinition.Optional<U> {
                    private final TypeList.Generic interfaces;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.interfaces.equals(((OptionalMethodMatchAdapter) obj).interfaces) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.interfaces.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected OptionalMethodMatchAdapter(TypeList.Generic interfaces) {
                        this.interfaces = interfaces;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withInterfaces(this.interfaces), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry, Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> intercept(Implementation implementation) {
                        return interfaceType().intercept(implementation);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> withoutCode() {
                        return interfaceType().withoutCode();
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public MethodDefinition.ReceiverTypeDefinition<U> defaultValue(AnnotationValue<?, ?> annotationValue) {
                        return interfaceType().defaultValue(annotationValue);
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition
                    public <V> MethodDefinition.ReceiverTypeDefinition<U> defaultValue(V value, Class<? extends V> type) {
                        return interfaceType().defaultValue(value, type);
                    }

                    private MethodDefinition.ImplementationDefinition<U> interfaceType() {
                        ElementMatcher.Junction<TypeDescription> elementMatcher = ElementMatchers.none();
                        for (TypeDescription typeDescription : this.interfaces.asErasures()) {
                            elementMatcher = elementMatcher.or(ElementMatchers.isSuperTypeOf(typeDescription));
                        }
                        return materialize().invokable(ElementMatchers.isDeclaredBy(ElementMatchers.isInterface().and(elementMatcher)));
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$RecordComponentDefinitionAdapter.class */
                protected class RecordComponentDefinitionAdapter extends RecordComponentDefinition.Optional.AbstractBase<U> {
                    private final RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory;
                    private final RecordComponentDescription.Token token;
                    private final Transformer<RecordComponentDescription> transformer;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.recordComponentAttributeAppenderFactory.equals(((RecordComponentDefinitionAdapter) obj).recordComponentAttributeAppenderFactory) && this.token.equals(((RecordComponentDefinitionAdapter) obj).token) && this.transformer.equals(((RecordComponentDefinitionAdapter) obj).transformer) && Adapter.this.equals(Adapter.this);
                    }

                    public int hashCode() {
                        return (((((((17 * 31) + this.recordComponentAttributeAppenderFactory.hashCode()) * 31) + this.token.hashCode()) * 31) + this.transformer.hashCode()) * 31) + Adapter.this.hashCode();
                    }

                    protected RecordComponentDefinitionAdapter(Adapter this$0, RecordComponentDescription.Token token) {
                        this(RecordComponentAttributeAppender.ForInstrumentedRecordComponent.INSTANCE, Transformer.NoOp.make(), token);
                    }

                    protected RecordComponentDefinitionAdapter(RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory, Transformer<RecordComponentDescription> transformer, RecordComponentDescription.Token token) {
                        this.recordComponentAttributeAppenderFactory = recordComponentAttributeAppenderFactory;
                        this.transformer = transformer;
                        this.token = token;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public RecordComponentDefinition.Optional<U> annotateRecordComponent(Collection<? extends AnnotationDescription> annotations) {
                        return new RecordComponentDefinitionAdapter(this.recordComponentAttributeAppenderFactory, this.transformer, new RecordComponentDescription.Token(this.token.getName(), this.token.getType(), CompoundList.of((List) this.token.getAnnotations(), (List) new ArrayList(annotations))));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public RecordComponentDefinition.Optional<U> attribute(RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory) {
                        return new RecordComponentDefinitionAdapter(new RecordComponentAttributeAppender.Factory.Compound(this.recordComponentAttributeAppenderFactory, recordComponentAttributeAppenderFactory), this.transformer, this.token);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public RecordComponentDefinition.Optional<U> transform(Transformer<RecordComponentDescription> transformer) {
                        return new RecordComponentDefinitionAdapter(this.recordComponentAttributeAppenderFactory, new Transformer.Compound(this.transformer, transformer), this.token);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType.withRecordComponent(this.token), Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry.prepend(new LatentMatcher.ForRecordComponentToken(this.token), this.recordComponentAttributeAppenderFactory, this.transformer), Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Builder$AbstractBase$Adapter$RecordComponentMatchAdapter.class */
                protected class RecordComponentMatchAdapter extends RecordComponentDefinition.Optional.AbstractBase<U> {
                    private final LatentMatcher<? super RecordComponentDescription> matcher;
                    private final RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory;
                    private final Transformer<RecordComponentDescription> transformer;

                    protected RecordComponentMatchAdapter(Adapter this$0, LatentMatcher<? super RecordComponentDescription> matcher) {
                        this(matcher, RecordComponentAttributeAppender.NoOp.INSTANCE, Transformer.NoOp.make());
                    }

                    protected RecordComponentMatchAdapter(LatentMatcher<? super RecordComponentDescription> matcher, RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory, Transformer<RecordComponentDescription> transformer) {
                        this.matcher = matcher;
                        this.recordComponentAttributeAppenderFactory = recordComponentAttributeAppenderFactory;
                        this.transformer = transformer;
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public RecordComponentDefinition.Optional<U> annotateRecordComponent(Collection<? extends AnnotationDescription> annotations) {
                        return attribute(new RecordComponentAttributeAppender.Explicit(new ArrayList(annotations)));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public RecordComponentDefinition.Optional<U> attribute(RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory) {
                        return new RecordComponentMatchAdapter(this.matcher, new RecordComponentAttributeAppender.Factory.Compound(this.recordComponentAttributeAppenderFactory, recordComponentAttributeAppenderFactory), this.transformer);
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition
                    public RecordComponentDefinition.Optional<U> transform(Transformer<RecordComponentDescription> transformer) {
                        return new RecordComponentMatchAdapter(this.matcher, this.recordComponentAttributeAppenderFactory, new Transformer.Compound(this.transformer, transformer));
                    }

                    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator
                    protected Builder<U> materialize() {
                        return Adapter.this.materialize(Adapter.this.instrumentedType, Adapter.this.fieldRegistry, Adapter.this.methodRegistry, Adapter.this.recordComponentRegistry.prepend(this.matcher, this.recordComponentAttributeAppenderFactory, this.transformer), Adapter.this.typeAttributeAppender, Adapter.this.asmVisitorWrapper, Adapter.this.classFileVersion, Adapter.this.auxiliaryTypeNamingStrategy, Adapter.this.annotationValueFilterFactory, Adapter.this.annotationRetention, Adapter.this.implementationContextFactory, Adapter.this.methodGraphCompiler, Adapter.this.typeValidation, Adapter.this.visibilityBridgeStrategy, Adapter.this.classWriterStrategy, Adapter.this.ignoredMethods, Adapter.this.auxiliaryTypes);
                    }
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default.class */
    public static class Default implements DynamicType {
        private static final String CLASS_FILE_EXTENSION = ".class";
        private static final String MANIFEST_VERSION = "1.0";
        private static final int BUFFER_SIZE = 1024;
        private static final int FROM_BEGINNING = 0;
        private static final int END_OF_FILE = -1;
        private static final String TEMP_SUFFIX = "tmp";
        protected static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        protected final TypeDescription typeDescription;
        protected final byte[] binaryRepresentation;
        protected final LoadedTypeInitializer loadedTypeInitializer;
        protected final List<? extends DynamicType> auxiliaryTypes;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((Default) obj).typeDescription) && Arrays.equals(this.binaryRepresentation, ((Default) obj).binaryRepresentation) && this.loadedTypeInitializer.equals(((Default) obj).loadedTypeInitializer) && this.auxiliaryTypes.equals(((Default) obj).auxiliaryTypes);
        }

        public int hashCode() {
            return (((((((17 * 31) + this.typeDescription.hashCode()) * 31) + Arrays.hashCode(this.binaryRepresentation)) * 31) + this.loadedTypeInitializer.hashCode()) * 31) + this.auxiliaryTypes.hashCode();
        }

        @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "The array is not to be modified by contract")
        public Default(TypeDescription typeDescription, byte[] binaryRepresentation, LoadedTypeInitializer loadedTypeInitializer, List<? extends DynamicType> auxiliaryTypes) {
            this.typeDescription = typeDescription;
            this.binaryRepresentation = binaryRepresentation;
            this.loadedTypeInitializer = loadedTypeInitializer;
            this.auxiliaryTypes = auxiliaryTypes;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public TypeDescription getTypeDescription() {
            return this.typeDescription;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public Map<TypeDescription, byte[]> getAllTypes() {
            Map<TypeDescription, byte[]> allTypes = new LinkedHashMap<>();
            allTypes.put(this.typeDescription, this.binaryRepresentation);
            for (DynamicType auxiliaryType : this.auxiliaryTypes) {
                allTypes.putAll(auxiliaryType.getAllTypes());
            }
            return allTypes;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public Map<TypeDescription, LoadedTypeInitializer> getLoadedTypeInitializers() {
            Map<TypeDescription, LoadedTypeInitializer> classLoadingCallbacks = new HashMap<>();
            for (DynamicType auxiliaryType : this.auxiliaryTypes) {
                classLoadingCallbacks.putAll(auxiliaryType.getLoadedTypeInitializers());
            }
            classLoadingCallbacks.put(this.typeDescription, this.loadedTypeInitializer);
            return classLoadingCallbacks;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public boolean hasAliveLoadedTypeInitializers() {
            for (LoadedTypeInitializer loadedTypeInitializer : getLoadedTypeInitializers().values()) {
                if (loadedTypeInitializer.isAlive()) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        @SuppressFBWarnings(value = {"EI_EXPOSE_REP"}, justification = "The array is not to be modified by contract")
        public byte[] getBytes() {
            return this.binaryRepresentation;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public Map<TypeDescription, byte[]> getAuxiliaryTypes() {
            Map<TypeDescription, byte[]> auxiliaryTypes = new HashMap<>();
            for (DynamicType auxiliaryType : this.auxiliaryTypes) {
                auxiliaryTypes.put(auxiliaryType.getTypeDescription(), auxiliaryType.getBytes());
                auxiliaryTypes.putAll(auxiliaryType.getAuxiliaryTypes());
            }
            return auxiliaryTypes;
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public Map<TypeDescription, File> saveIn(File folder) throws IOException {
            Map<TypeDescription, File> files = new HashMap<>();
            File target = new File(folder, this.typeDescription.getName().replace('.', File.separatorChar) + ".class");
            if (target.getParentFile() != null && !target.getParentFile().isDirectory() && !target.getParentFile().mkdirs()) {
                throw new IllegalArgumentException("Could not create directory: " + target.getParentFile());
            }
            OutputStream outputStream = new FileOutputStream(target);
            try {
                outputStream.write(this.binaryRepresentation);
                outputStream.close();
                files.put(this.typeDescription, target);
                for (DynamicType auxiliaryType : this.auxiliaryTypes) {
                    files.putAll(auxiliaryType.saveIn(folder));
                }
                return files;
            } catch (Throwable th) {
                outputStream.close();
                throw th;
            }
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public File inject(File sourceJar, File targetJar) throws IOException {
            if (sourceJar.equals(targetJar)) {
                return inject(sourceJar);
            }
            return doInject(sourceJar, targetJar);
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public File inject(File jar) throws IOException {
            File temporary = doInject(jar, File.createTempFile(jar.getName(), TEMP_SUFFIX));
            boolean delete = true;
            try {
                delete = DISPATCHER.copy(temporary, jar);
                if (delete && !temporary.delete()) {
                    temporary.deleteOnExit();
                }
                return jar;
            } catch (Throwable th) {
                if (delete && !temporary.delete()) {
                    temporary.deleteOnExit();
                }
                throw th;
            }
        }

        private File doInject(File sourceJar, File targetJar) throws IOException {
            JarInputStream inputStream = new JarInputStream(new FileInputStream(sourceJar));
            try {
                if (!targetJar.isFile() && !targetJar.createNewFile()) {
                    throw new IllegalArgumentException("Could not create file: " + targetJar);
                }
                Manifest manifest = inputStream.getManifest();
                JarOutputStream outputStream = manifest == null ? new JarOutputStream(new FileOutputStream(targetJar)) : new JarOutputStream(new FileOutputStream(targetJar), manifest);
                Map<TypeDescription, byte[]> rawAuxiliaryTypes = getAuxiliaryTypes();
                Map<String, byte[]> files = new HashMap<>();
                for (Map.Entry<TypeDescription, byte[]> entry : rawAuxiliaryTypes.entrySet()) {
                    files.put(entry.getKey().getInternalName() + ".class", entry.getValue());
                }
                files.put(this.typeDescription.getInternalName() + ".class", this.binaryRepresentation);
                while (true) {
                    JarEntry jarEntry = inputStream.getNextJarEntry();
                    if (jarEntry == null) {
                        break;
                    }
                    byte[] replacement = files.remove(jarEntry.getName());
                    if (replacement == null) {
                        outputStream.putNextEntry(jarEntry);
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int index = inputStream.read(buffer);
                            if (index != -1) {
                                outputStream.write(buffer, 0, index);
                            }
                        }
                    } else {
                        outputStream.putNextEntry(new JarEntry(jarEntry.getName()));
                        outputStream.write(replacement);
                    }
                    inputStream.closeEntry();
                    outputStream.closeEntry();
                }
                for (Map.Entry<String, byte[]> entry2 : files.entrySet()) {
                    outputStream.putNextEntry(new JarEntry(entry2.getKey()));
                    outputStream.write(entry2.getValue());
                    outputStream.closeEntry();
                }
                outputStream.close();
                return targetJar;
            } finally {
                inputStream.close();
            }
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public File toJar(File file) throws IOException {
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            return toJar(file, manifest);
        }

        @Override // net.bytebuddy.dynamic.DynamicType
        public File toJar(File file, Manifest manifest) throws IOException {
            if (!file.isFile() && !file.createNewFile()) {
                throw new IllegalArgumentException("Could not create file: " + file);
            }
            JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(file), manifest);
            try {
                for (Map.Entry<TypeDescription, byte[]> entry : getAuxiliaryTypes().entrySet()) {
                    outputStream.putNextEntry(new JarEntry(entry.getKey().getInternalName() + ".class"));
                    outputStream.write(entry.getValue());
                    outputStream.closeEntry();
                }
                outputStream.putNextEntry(new JarEntry(this.typeDescription.getInternalName() + ".class"));
                outputStream.write(this.binaryRepresentation);
                outputStream.closeEntry();
                outputStream.close();
                return file;
            } catch (Throwable th) {
                outputStream.close();
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default$Dispatcher.class */
        public interface Dispatcher {
            boolean copy(File file, File file2) throws IOException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    try {
                        Class<?> path = Class.forName("java.nio.file.Path");
                        Object[] arguments = (Object[]) Array.newInstance(Class.forName("java.nio.file.CopyOption"), 1);
                        arguments[0] = Enum.valueOf(Class.forName("java.nio.file.StandardCopyOption"), "REPLACE_EXISTING");
                        return new ForJava7CapableVm(File.class.getMethod("toPath", new Class[0]), Class.forName("java.nio.file.Files").getMethod("move", path, path, arguments.getClass()), arguments);
                    } catch (Throwable th) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.DynamicType.Default.Dispatcher
                public boolean copy(File source, File target) throws IOException {
                    InputStream inputStream = new FileInputStream(source);
                    try {
                        OutputStream outputStream = new FileOutputStream(target);
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int index = inputStream.read(buffer);
                            if (index != -1) {
                                outputStream.write(buffer, 0, index);
                            } else {
                                outputStream.close();
                                return true;
                            }
                        }
                    } finally {
                        inputStream.close();
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default$Dispatcher$ForJava7CapableVm.class */
            public static class ForJava7CapableVm implements Dispatcher {
                private final Method toPath;
                private final Method move;
                private final Object[] copyOptions;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.toPath.equals(((ForJava7CapableVm) obj).toPath) && this.move.equals(((ForJava7CapableVm) obj).move) && Arrays.equals(this.copyOptions, ((ForJava7CapableVm) obj).copyOptions);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.toPath.hashCode()) * 31) + this.move.hashCode()) * 31) + Arrays.hashCode(this.copyOptions);
                }

                protected ForJava7CapableVm(Method toPath, Method move, Object[] copyOptions) {
                    this.toPath = toPath;
                    this.move = move;
                    this.copyOptions = copyOptions;
                }

                @Override // net.bytebuddy.dynamic.DynamicType.Default.Dispatcher
                public boolean copy(File source, File target) throws IOException {
                    try {
                        this.move.invoke(null, this.toPath.invoke(source, new Object[0]), this.toPath.invoke(target, new Object[0]), this.copyOptions);
                        return false;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access NIO file copy", exception);
                    } catch (InvocationTargetException exception2) {
                        Throwable cause = exception2.getCause();
                        if (cause instanceof IOException) {
                            throw ((IOException) cause);
                        }
                        throw new IllegalStateException("Cannot execute NIO file copy", cause);
                    }
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default$Unloaded.class */
        public static class Unloaded<T> extends Default implements Unloaded<T> {
            private final TypeResolutionStrategy.Resolved typeResolutionStrategy;

            @Override // net.bytebuddy.dynamic.DynamicType.Default
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeResolutionStrategy.equals(((Unloaded) obj).typeResolutionStrategy);
                }
                return false;
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Default
            public int hashCode() {
                return (super.hashCode() * 31) + this.typeResolutionStrategy.hashCode();
            }

            public Unloaded(TypeDescription typeDescription, byte[] binaryRepresentation, LoadedTypeInitializer loadedTypeInitializer, List<? extends DynamicType> auxiliaryTypes, TypeResolutionStrategy.Resolved typeResolutionStrategy) {
                super(typeDescription, binaryRepresentation, loadedTypeInitializer, auxiliaryTypes);
                this.typeResolutionStrategy = typeResolutionStrategy;
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Unloaded
            public Loaded<T> load(ClassLoader classLoader) {
                if ((classLoader instanceof InjectionClassLoader) && !((InjectionClassLoader) classLoader).isSealed()) {
                    return load((InjectionClassLoader) classLoader, InjectionClassLoader.Strategy.INSTANCE);
                }
                return load(classLoader, ClassLoadingStrategy.Default.WRAPPER);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Unloaded
            public <S extends ClassLoader> Loaded<T> load(S classLoader, ClassLoadingStrategy<? super S> classLoadingStrategy) {
                return new Loaded(this.typeDescription, this.binaryRepresentation, this.loadedTypeInitializer, this.auxiliaryTypes, this.typeResolutionStrategy.initialize(this, classLoader, classLoadingStrategy));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Unloaded
            public Unloaded<T> include(DynamicType... dynamicType) {
                return include(Arrays.asList(dynamicType));
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Unloaded
            public Unloaded<T> include(List<? extends DynamicType> dynamicType) {
                return new Unloaded(this.typeDescription, this.binaryRepresentation, this.loadedTypeInitializer, CompoundList.of((List) this.auxiliaryTypes, (List) dynamicType), this.typeResolutionStrategy);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/DynamicType$Default$Loaded.class */
        public static class Loaded<T> extends Default implements Loaded<T> {
            private final Map<TypeDescription, Class<?>> loadedTypes;

            @Override // net.bytebuddy.dynamic.DynamicType.Default
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.loadedTypes.equals(((Loaded) obj).loadedTypes);
                }
                return false;
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Default
            public int hashCode() {
                return (super.hashCode() * 31) + this.loadedTypes.hashCode();
            }

            protected Loaded(TypeDescription typeDescription, byte[] typeByte, LoadedTypeInitializer loadedTypeInitializer, List<? extends DynamicType> auxiliaryTypes, Map<TypeDescription, Class<?>> loadedTypes) {
                super(typeDescription, typeByte, loadedTypeInitializer, auxiliaryTypes);
                this.loadedTypes = loadedTypes;
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Loaded
            public Class<? extends T> getLoaded() {
                return (Class<? extends T>) this.loadedTypes.get(this.typeDescription);
            }

            @Override // net.bytebuddy.dynamic.DynamicType.Loaded
            public Map<TypeDescription, Class<?>> getLoadedAuxiliaryTypes() {
                Map<TypeDescription, Class<?>> loadedAuxiliaryTypes = new HashMap<>(this.loadedTypes);
                loadedAuxiliaryTypes.remove(this.typeDescription);
                return loadedAuxiliaryTypes;
            }
        }
    }
}
