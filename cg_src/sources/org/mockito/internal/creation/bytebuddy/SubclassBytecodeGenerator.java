package org.mockito.internal.creation.bytebuddy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.SynchronizationState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.loading.MultipleParentClassLoader;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.util.StringUtil;
import org.mockito.mock.SerializableMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/SubclassBytecodeGenerator.class */
class SubclassBytecodeGenerator implements BytecodeGenerator {
    private static final String CODEGEN_PACKAGE = "org.mockito.codegen.";
    private final SubclassLoader loader;
    private final ModuleHandler handler;
    private final ByteBuddy byteBuddy;
    private final Random random;
    private final Implementation readReplace;
    private final ElementMatcher<? super MethodDescription> matcher;
    private final Implementation dispatcher;
    private final Implementation hashCode;
    private final Implementation equals;
    private final Implementation writeReplace;

    public SubclassBytecodeGenerator() {
        this(new SubclassInjectionLoader());
    }

    public SubclassBytecodeGenerator(SubclassLoader loader) {
        this(loader, null, ElementMatchers.any());
    }

    public SubclassBytecodeGenerator(Implementation readReplace, ElementMatcher<? super MethodDescription> matcher) {
        this(new SubclassInjectionLoader(), readReplace, matcher);
    }

    protected SubclassBytecodeGenerator(SubclassLoader loader, Implementation readReplace, ElementMatcher<? super MethodDescription> matcher) {
        this.dispatcher = MethodDelegation.to((Class<?>) MockMethodInterceptor.DispatcherDefaultingToRealMethod.class);
        this.hashCode = MethodDelegation.to((Class<?>) MockMethodInterceptor.ForHashCode.class);
        this.equals = MethodDelegation.to((Class<?>) MockMethodInterceptor.ForEquals.class);
        this.writeReplace = MethodDelegation.to((Class<?>) MockMethodInterceptor.ForWriteReplace.class);
        this.loader = loader;
        this.readReplace = readReplace;
        this.matcher = matcher;
        this.byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED);
        this.random = new Random();
        this.handler = ModuleHandler.make(this.byteBuddy, loader, this.random);
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        String typeName;
        Annotation[] annotations;
        MethodAttributeAppender.Factory factory;
        ClassLoader classLoader = new MultipleParentClassLoader.Builder().appendMostSpecific(getAllTypes(features.mockedType)).appendMostSpecific(features.interfaces).appendMostSpecific(Thread.currentThread().getContextClassLoader()).appendMostSpecific(MockAccess.class).build();
        boolean localMock = classLoader == features.mockedType.getClassLoader() && features.serializableMode != SerializableMode.ACROSS_CLASSLOADERS && !isComingFromJDK(features.mockedType) && (this.loader.isDisrespectingOpenness() || this.handler.isOpened(features.mockedType, MockAccess.class));
        if (localMock || ((this.loader instanceof MultipleParentClassLoader) && !isComingFromJDK(features.mockedType))) {
            typeName = features.mockedType.getName();
        } else {
            typeName = InjectionBase.class.getPackage().getName() + "." + features.mockedType.getSimpleName();
        }
        String name = String.format("%s$%s$%d", typeName, "MockitoMock", Integer.valueOf(Math.abs(this.random.nextInt())));
        if (localMock) {
            this.handler.adjustModuleGraph(features.mockedType, MockAccess.class, false, true);
            for (Class<?> iFace : features.interfaces) {
                this.handler.adjustModuleGraph(iFace, features.mockedType, true, false);
                this.handler.adjustModuleGraph(features.mockedType, iFace, false, true);
            }
        } else {
            boolean exported = this.handler.isExported(features.mockedType);
            Iterator<Class<?>> it = features.interfaces.iterator();
            while (exported && it.hasNext()) {
                exported = this.handler.isExported(it.next());
            }
            if (exported) {
                assertVisibility(features.mockedType);
                for (Class<?> iFace2 : features.interfaces) {
                    assertVisibility(iFace2);
                }
            } else {
                Class<?> hook = this.handler.injectionBase(classLoader, typeName);
                assertVisibility(features.mockedType);
                this.handler.adjustModuleGraph(features.mockedType, hook, true, false);
                for (Class<?> iFace3 : features.interfaces) {
                    assertVisibility(iFace3);
                    this.handler.adjustModuleGraph(iFace3, hook, true, false);
                }
            }
        }
        DynamicType.Builder<T> ignoreAlso = this.byteBuddy.subclass((Class) features.mockedType).name(name).ignoreAlso(isGroovyMethod());
        if (features.stripAnnotations) {
            annotations = new Annotation[0];
        } else {
            annotations = features.mockedType.getAnnotations();
        }
        DynamicType.Builder.MethodDefinition<T> transform = ignoreAlso.annotateType(annotations).implement((List<? extends Type>) new ArrayList(features.interfaces)).method(this.matcher).intercept(this.dispatcher).transform(Transformer.ForMethod.withModifiers(SynchronizationState.PLAIN));
        if (features.stripAnnotations) {
            factory = MethodAttributeAppender.NoOp.INSTANCE;
        } else {
            factory = MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER;
        }
        DynamicType.Builder<T> builder = transform.attribute(factory).method(ElementMatchers.isHashCode()).intercept(this.hashCode).method(ElementMatchers.isEquals()).intercept(this.equals).serialVersionUid(42L).defineField("mockitoInterceptor", MockMethodInterceptor.class, Visibility.PRIVATE).implement(MockAccess.class).intercept(FieldAccessor.ofBeanProperty());
        if (features.serializableMode == SerializableMode.ACROSS_CLASSLOADERS) {
            builder = builder.implement(ByteBuddyCrossClassLoaderSerializationSupport.CrossClassLoaderSerializableMock.class).intercept(this.writeReplace);
        }
        if (this.readReplace != null) {
            builder = builder.defineMethod("readObject", Void.TYPE, Visibility.PRIVATE).withParameters(ObjectInputStream.class).throwing(ClassNotFoundException.class, IOException.class).intercept(this.readReplace);
        }
        if (name.startsWith(CODEGEN_PACKAGE) || (classLoader instanceof MultipleParentClassLoader)) {
            builder = builder.ignoreAlso(ElementMatchers.isPackagePrivate().or(ElementMatchers.returns(ElementMatchers.isPackagePrivate())).or(ElementMatchers.hasParameters(ElementMatchers.whereAny(ElementMatchers.hasType(ElementMatchers.isPackagePrivate())))));
        }
        return builder.make().load(classLoader, this.loader.resolveStrategy(features.mockedType, classLoader, localMock)).getLoaded();
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public void mockClassStatic(Class<?> type) {
        throw new MockitoException("The subclass byte code generator cannot create static mocks");
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public void mockClassConstruction(Class<?> type) {
        throw new MockitoException("The subclass byte code generator cannot create construction mocks");
    }

    private <T> Collection<Class<? super T>> getAllTypes(Class<T> type) {
        Collection<Class<? super T>> supertypes = new LinkedList<>();
        supertypes.add(type);
        Class<T> cls = type;
        while (true) {
            Class<T> cls2 = cls;
            if (cls2 != null) {
                supertypes.add(cls2);
                cls = cls2.getSuperclass();
            } else {
                return supertypes;
            }
        }
    }

    private static ElementMatcher<MethodDescription> isGroovyMethod() {
        return ElementMatchers.isDeclaredBy(ElementMatchers.named("groovy.lang.GroovyObjectSupport"));
    }

    private boolean isComingFromJDK(Class<?> type) {
        return (type.getPackage() != null && "Java Runtime Environment".equalsIgnoreCase(type.getPackage().getImplementationTitle())) || type.getName().startsWith("java.") || type.getName().startsWith("javax.");
    }

    private static void assertVisibility(Class<?> type) {
        if (!Modifier.isPublic(type.getModifiers())) {
            throw new MockitoException(StringUtil.join("Cannot create mock for " + type, "", "The type is not public and its mock class is loaded by a different class loader.", "This can have multiple reasons:", " - You are mocking a class with additional interfaces of another class loader", " - Mockito is loaded by a different class loader than the mocked type (e.g. with OSGi)", " - The thread's context class loader is different than the mock's class loader"));
        }
    }
}
