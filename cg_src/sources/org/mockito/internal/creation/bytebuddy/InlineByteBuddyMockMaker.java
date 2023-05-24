package org.mockito.internal.creation.bytebuddy;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.mockito.Incubating;
import org.mockito.MockedConstruction;
import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.Platform;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InlineMockMaker;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;
@Incubating
@SuppressSignatureCheck
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineByteBuddyMockMaker.class */
public class InlineByteBuddyMockMaker implements ClassCreatingMockMaker, InlineMockMaker, Instantiator {
    private static final Instrumentation INSTRUMENTATION;
    private static final Throwable INITIALIZATION_ERROR;
    private final BytecodeGenerator bytecodeGenerator;
    private final WeakConcurrentMap<Object, MockMethodInterceptor> mocks = new WeakConcurrentMap.WithInlinedExpunction();
    private final DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics = new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.INLINE);
    private final DetachedThreadLocal<Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>>> mockedConstruction = new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.INLINE);
    private final ThreadLocal<Boolean> mockitoConstruction = ThreadLocal.withInitial(() -> {
        return false;
    });
    private final ThreadLocal<Object> currentSpied = new ThreadLocal<>();

    /* JADX WARN: Finally extract failed */
    static {
        Instrumentation instrumentation;
        Throwable initializationError = null;
        try {
            try {
                instrumentation = ByteBuddyAgent.install();
            } catch (Throwable throwable) {
                instrumentation = null;
                initializationError = throwable;
            }
            if (!instrumentation.isRetransformClassesSupported()) {
                throw new IllegalStateException(StringUtil.join("Byte Buddy requires retransformation for creating inline mocks. This feature is unavailable on the current VM.", "", "You cannot use this mock maker on this VM"));
            }
            File boot = File.createTempFile("mockitoboot", ".jar");
            boot.deleteOnExit();
            JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(boot));
            try {
                InputStream inputStream = InlineByteBuddyMockMaker.class.getClassLoader().getResourceAsStream("org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.raw");
                if (inputStream == null) {
                    throw new IllegalStateException(StringUtil.join("The MockMethodDispatcher class file is not locatable: org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.raw", "", "The class loader responsible for looking up the resource: " + InlineByteBuddyMockMaker.class.getClassLoader()));
                }
                outputStream.putNextEntry(new JarEntry("org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.class"));
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int length = inputStream.read(buffer);
                        if (length == -1) {
                            break;
                        }
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.closeEntry();
                    outputStream.close();
                    JarFile jarfile = new JarFile(boot);
                    Throwable th = null;
                    try {
                        instrumentation.appendToBootstrapClassLoaderSearch(jarfile);
                        if (0 != 0) {
                            try {
                                jarfile.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            jarfile.close();
                        }
                        try {
                            Class.forName("org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher", false, null);
                            INSTRUMENTATION = instrumentation;
                            INITIALIZATION_ERROR = initializationError;
                        } catch (ClassNotFoundException cnfe) {
                            throw new IllegalStateException(StringUtil.join("Mockito failed to inject the MockMethodDispatcher class into the bootstrap class loader", "", "It seems like your current VM does not support the instrumentation API correctly."), cnfe);
                        }
                    } finally {
                    }
                } catch (Throwable th3) {
                    inputStream.close();
                    throw th3;
                }
            } catch (Throwable th4) {
                outputStream.close();
                throw th4;
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(StringUtil.join("Mockito could not self-attach a Java agent to the current VM. This feature is required for inline mocking.", "This error occured due to an I/O error during the creation of this agent: " + ioe, "", "Potentially, the current VM does not support the instrumentation API correctly"), ioe);
        }
    }

    public InlineByteBuddyMockMaker() {
        String detail;
        if (INITIALIZATION_ERROR != null) {
            if (System.getProperty("java.specification.vendor", "").toLowerCase().contains("android")) {
                detail = "It appears as if you are trying to run this mock maker on Android which does not support the instrumentation API.";
            } else {
                try {
                    if (Class.forName("javax.tools.ToolProvider").getMethod("getSystemJavaCompiler", new Class[0]).invoke(null, new Object[0]) == null) {
                        detail = "It appears as if you are running on a JRE. Either install a JDK or add JNA to the class path.";
                    } else {
                        detail = "It appears as if your JDK does not supply a working agent attachment mechanism.";
                    }
                } catch (Throwable th) {
                    detail = "It appears as if you are running an incomplete JVM installation that might not support all tooling APIs";
                }
            }
            throw new MockitoInitializationException(StringUtil.join("Could not initialize inline Byte Buddy mock maker.", "", detail, Platform.describe()), INITIALIZATION_ERROR);
        }
        ThreadLocal<Class<?>> currentConstruction = new ThreadLocal<>();
        ThreadLocal<Boolean> isSuspended = ThreadLocal.withInitial(() -> {
            return false;
        });
        Predicate<Class<?>> isMockConstruction = type -> {
            if (((Boolean) isSuspended.get()).booleanValue()) {
                return false;
            }
            if (this.mockitoConstruction.get().booleanValue() || isSuspended.get() != null) {
                return true;
            }
            Map<Class<?>, ?> interceptors = this.mockedConstruction.get();
            if (interceptors != null && interceptors.containsKey(currentConstruction)) {
                isSuspended.set(currentConstruction);
                return true;
            }
            return false;
        };
        ConstructionCallback onConstruction = type2, object, arguments, parameterTypeNames -> {
            BiConsumer<Object, MockedConstruction.Context> interceptor;
            if (this.mockitoConstruction.get().booleanValue()) {
                Object spy = this.currentSpied.get();
                if (spy == null) {
                    return null;
                }
                if (currentConstruction.isInstance(spy)) {
                    return spy;
                }
                isSuspended.set(true);
                try {
                    throw new MockitoException("Unexpected spy for " + currentConstruction.getName() + " on instance of " + object.getClass().getName(), object instanceof Throwable ? (Throwable) object : null);
                } finally {
                }
            } else if (isSuspended.get() != currentConstruction) {
                return null;
            } else {
                isSuspended.remove();
                isSuspended.set(true);
                try {
                    Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors = this.mockedConstruction.get();
                    if (interceptors != null && (interceptor = interceptors.get(currentConstruction)) != null) {
                        interceptor.accept(object, new InlineConstructionMockContext(arguments, object.getClass(), parameterTypeNames));
                    }
                    isSuspended.set(false);
                    return null;
                } finally {
                }
            }
        };
        this.bytecodeGenerator = new TypeCachingBytecodeGenerator(new InlineBytecodeGenerator(INSTRUMENTATION, this.mocks, this.mockedStatics, isMockConstruction, onConstruction), true);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return (T) doCreateMock(settings, handler, false);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> Optional<T> createSpy(MockCreationSettings<T> settings, MockHandler handler, T object) {
        if (object == null) {
            throw new MockitoConfigurationException("Spy instance must not be null");
        }
        this.currentSpied.set(object);
        try {
            Optional<T> ofNullable = Optional.ofNullable(doCreateMock(settings, handler, true));
            this.currentSpied.remove();
            return ofNullable;
        } catch (Throwable th) {
            this.currentSpied.remove();
            throw th;
        }
    }

    private <T> T doCreateMock(MockCreationSettings<T> settings, MockHandler handler, boolean nullOnNonInlineConstruction) {
        Object newInstance;
        Class<T> createMockType = createMockType(settings);
        try {
            if (settings.isUsingConstructor()) {
                newInstance = new ConstructorInstantiator(settings.getOuterClassInstance() != null, settings.getConstructorArgs()).newInstance(createMockType);
            } else {
                try {
                    newInstance = newInstance(createMockType);
                } catch (InstantiationException e) {
                    if (nullOnNonInlineConstruction) {
                        return null;
                    }
                    Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
                    newInstance = instantiator.newInstance(createMockType);
                }
            }
            MockMethodInterceptor mockMethodInterceptor = new MockMethodInterceptor(handler, settings);
            this.mocks.put(newInstance, mockMethodInterceptor);
            if (newInstance instanceof MockAccess) {
                ((MockAccess) newInstance).setMockitoInterceptor(mockMethodInterceptor);
            }
            return (T) newInstance;
        } catch (InstantiationException e2) {
            throw new MockitoException("Unable to create mock instance of type '" + createMockType.getSimpleName() + "'", e2);
        }
    }

    @Override // org.mockito.internal.creation.bytebuddy.ClassCreatingMockMaker
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return this.bytecodeGenerator.mockClass(MockFeatures.withMockFeatures(settings.getTypeToMock(), settings.getExtraInterfaces(), settings.getSerializableMode(), settings.isStripAnnotations()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private <T> RuntimeException prettifyFailure(MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        String str;
        if (mockFeatures.getTypeToMock().isArray()) {
            throw new MockitoException(StringUtil.join("Arrays cannot be mocked: " + mockFeatures.getTypeToMock() + ".", ""), generationFailed);
        }
        if (Modifier.isFinal(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(StringUtil.join("Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".", "Can not mock final classes with the following settings :", " - explicit serialization (e.g. withSettings().serializable())", " - extra interfaces (e.g. withSettings().extraInterfaces(...))", "", "You are seeing this disclaimer because Mockito is configured to create inlined mocks.", "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.", "", "Underlying exception : " + generationFailed), generationFailed);
        }
        if (Modifier.isPrivate(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(StringUtil.join("Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".", "Most likely it is a private class that is not visible by Mockito", "", "You are seeing this disclaimer because Mockito is configured to create inlined mocks.", "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.", ""), generationFailed);
        }
        Object[] objArr = new Object[11];
        objArr[0] = "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".";
        objArr[1] = "";
        objArr[2] = "If you're not sure why you're getting this error, please report to the mailing list.";
        objArr[3] = "";
        if (Platform.isJava8BelowUpdate45()) {
            str = "Java 8 early builds have bugs that were addressed in Java 1.8.0_45, please update your JDK!\n";
        } else {
            str = "";
        }
        objArr[4] = Platform.warnForVM("IBM J9 VM", "Early IBM virtual machine are known to have issues with Mockito, please upgrade to an up-to-date version.\n", "Hotspot", str);
        objArr[5] = Platform.describe();
        objArr[6] = "";
        objArr[7] = "You are seeing this disclaimer because Mockito is configured to create inlined mocks.";
        objArr[8] = "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.";
        objArr[9] = "";
        objArr[10] = "Underlying exception : " + generationFailed;
        throw new MockitoException(StringUtil.join(objArr), generationFailed);
    }

    @Override // org.mockito.plugins.MockMaker
    public MockHandler getHandler(Object mock) {
        MockMethodInterceptor interceptor;
        if (mock instanceof Class) {
            Map<Class<?>, MockMethodInterceptor> interceptors = this.mockedStatics.get();
            interceptor = interceptors != null ? interceptors.get(mock) : null;
        } else {
            interceptor = this.mocks.get(mock);
        }
        if (interceptor == null) {
            return null;
        }
        return interceptor.handler;
    }

    @Override // org.mockito.plugins.MockMaker
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        MockMethodInterceptor mockMethodInterceptor = new MockMethodInterceptor(newHandler, settings);
        if (mock instanceof Class) {
            Map<Class<?>, MockMethodInterceptor> interceptors = this.mockedStatics.get();
            if (interceptors == null || !interceptors.containsKey(mock)) {
                throw new MockitoException("Cannot reset " + mock + " which is not currently registered as a static mock");
            }
            interceptors.put((Class) mock, mockMethodInterceptor);
        } else if (!this.mocks.containsKey(mock)) {
            throw new MockitoException("Cannot reset " + mock + " which is not currently registered as a mock");
        } else {
            this.mocks.put(mock, mockMethodInterceptor);
            if (mock instanceof MockAccess) {
                ((MockAccess) mock).setMockitoInterceptor(mockMethodInterceptor);
            }
        }
    }

    @Override // org.mockito.plugins.InlineMockMaker
    public void clearMock(Object mock) {
        if (mock instanceof Class) {
            for (Map<Class<?>, ?> entry : this.mockedStatics.getBackingMap().target.values()) {
                entry.remove(mock);
            }
            return;
        }
        this.mocks.remove((WeakConcurrentMap<Object, MockMethodInterceptor>) mock);
    }

    @Override // org.mockito.plugins.InlineMockMaker
    public void clearAllMocks() {
        this.mockedStatics.getBackingMap().clear();
        this.mocks.clear();
    }

    @Override // org.mockito.plugins.MockMaker
    public MockMaker.TypeMockability isTypeMockable(final Class<?> type) {
        return new MockMaker.TypeMockability() { // from class: org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker.1
            @Override // org.mockito.plugins.MockMaker.TypeMockability
            public boolean mockable() {
                return InlineByteBuddyMockMaker.INSTRUMENTATION.isModifiableClass(type) && !InlineBytecodeGenerator.EXCLUDES.contains(type);
            }

            @Override // org.mockito.plugins.MockMaker.TypeMockability
            public String nonMockableReason() {
                if (mockable()) {
                    return "";
                }
                if (type.isPrimitive()) {
                    return "primitive type";
                }
                if (InlineBytecodeGenerator.EXCLUDES.contains(type)) {
                    return "Cannot mock wrapper types, String.class or Class.class";
                }
                return "VM does not support modification of given type";
            }
        };
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> MockMaker.StaticMockControl<T> createStaticMock(Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        if (type == ConcurrentHashMap.class) {
            throw new MockitoException("It is not possible to mock static methods of ConcurrentHashMap to avoid infinitive loops within Mockito's implementation of static mock handling");
        }
        if (type == Thread.class || type == System.class || type == Arrays.class || ClassLoader.class.isAssignableFrom(type)) {
            throw new MockitoException("It is not possible to mock static methods of " + type.getName() + " to avoid interfering with class loading what leads to infinite loops");
        }
        this.bytecodeGenerator.mockClassStatic(type);
        Map<Class<?>, MockMethodInterceptor> interceptors = this.mockedStatics.get();
        if (interceptors == null) {
            interceptors = new WeakHashMap<>();
            this.mockedStatics.set(interceptors);
        }
        return new InlineStaticMockControl(type, interceptors, settings, handler);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> MockMaker.ConstructionMockControl<T> createConstructionMock(Class<T> type, Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory, Function<MockedConstruction.Context, MockHandler<T>> handlerFactory, MockedConstruction.MockInitializer<T> mockInitializer) {
        if (type == Object.class) {
            throw new MockitoException("It is not possible to mock construction of the Object class to avoid inference with default object constructor chains");
        }
        if (type.isPrimitive() || Modifier.isAbstract(type.getModifiers())) {
            throw new MockitoException("It is not possible to construct primitive types or abstract types: " + type.getName());
        }
        this.bytecodeGenerator.mockClassConstruction(type);
        Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors = this.mockedConstruction.get();
        if (interceptors == null) {
            interceptors = new WeakHashMap<>();
            this.mockedConstruction.set(interceptors);
        }
        return new InlineConstructionMockControl(type, settingsFactory, handlerFactory, mockInitializer, interceptors);
    }

    @Override // org.mockito.creation.instance.Instantiator
    public <T> T newInstance(Class<T> cls) throws InstantiationException {
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new InstantiationException(cls.getName() + " does not define a constructor");
        }
        Constructor<?> selected = constructors[0];
        int length = constructors.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Constructor<?> constructor = constructors[i];
            if (!Modifier.isPublic(constructor.getModifiers())) {
                i++;
            } else {
                selected = constructor;
                break;
            }
        }
        Class<?>[] types = selected.getParameterTypes();
        Object[] arguments = new Object[types.length];
        int index = 0;
        for (Class<?> type : types) {
            int i2 = index;
            index++;
            arguments[i2] = makeStandardArgument(type);
        }
        MemberAccessor accessor = Plugins.getMemberAccessor();
        try {
            return (T) accessor.newInstance(selected, callback -> {
                this.mockitoConstruction.set(true);
                try {
                    return callback.newInstance();
                } finally {
                    this.mockitoConstruction.set(Boolean.valueOf(false));
                }
            }, arguments);
        } catch (Exception e) {
            throw new InstantiationException("Could not instantiate " + cls.getName(), e);
        }
    }

    private Object makeStandardArgument(Class<?> type) {
        if (type == Boolean.TYPE) {
            return false;
        }
        if (type == Byte.TYPE) {
            return (byte) 0;
        }
        if (type == Short.TYPE) {
            return (short) 0;
        }
        if (type == Character.TYPE) {
            return (char) 0;
        }
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Long.TYPE) {
            return 0L;
        }
        if (type == Float.TYPE) {
            return Float.valueOf(0.0f);
        }
        if (type == Double.TYPE) {
            return Double.valueOf((double) Const.default_value_double);
        }
        return null;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineByteBuddyMockMaker$InlineStaticMockControl.class */
    private static class InlineStaticMockControl<T> implements MockMaker.StaticMockControl<T> {
        private final Class<T> type;
        private final Map<Class<?>, MockMethodInterceptor> interceptors;
        private final MockCreationSettings<T> settings;
        private final MockHandler handler;

        private InlineStaticMockControl(Class<T> type, Map<Class<?>, MockMethodInterceptor> interceptors, MockCreationSettings<T> settings, MockHandler handler) {
            this.type = type;
            this.interceptors = interceptors;
            this.settings = settings;
            this.handler = handler;
        }

        @Override // org.mockito.plugins.MockMaker.StaticMockControl
        public Class<T> getType() {
            return this.type;
        }

        @Override // org.mockito.plugins.MockMaker.StaticMockControl
        public void enable() {
            if (this.interceptors.putIfAbsent(this.type, new MockMethodInterceptor(this.handler, this.settings)) != null) {
                throw new MockitoException(StringUtil.join("For " + this.type.getName() + ", static mocking is already registered in the current thread", "", "To create a new mock, the existing static mock registration must be deregistered"));
            }
        }

        @Override // org.mockito.plugins.MockMaker.StaticMockControl
        public void disable() {
            if (this.interceptors.remove(this.type) == null) {
                throw new MockitoException(StringUtil.join("Could not deregister " + this.type.getName() + " as a static mock since it is not currently registered", "", "To register a static mock, use Mockito.mockStatic(" + this.type.getSimpleName() + ".class)"));
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineByteBuddyMockMaker$InlineConstructionMockControl.class */
    private class InlineConstructionMockControl<T> implements MockMaker.ConstructionMockControl<T> {
        private final Class<T> type;
        private final Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory;
        private final Function<MockedConstruction.Context, MockHandler<T>> handlerFactory;
        private final MockedConstruction.MockInitializer<T> mockInitializer;
        private final Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors;
        private final List<Object> all;
        private int count;

        private InlineConstructionMockControl(Class<T> type, Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory, Function<MockedConstruction.Context, MockHandler<T>> handlerFactory, MockedConstruction.MockInitializer<T> mockInitializer, Map<Class<?>, BiConsumer<Object, MockedConstruction.Context>> interceptors) {
            this.all = new ArrayList();
            this.type = type;
            this.settingsFactory = settingsFactory;
            this.handlerFactory = handlerFactory;
            this.mockInitializer = mockInitializer;
            this.interceptors = interceptors;
        }

        @Override // org.mockito.plugins.MockMaker.ConstructionMockControl
        public Class<T> getType() {
            return this.type;
        }

        @Override // org.mockito.plugins.MockMaker.ConstructionMockControl
        public void enable() {
            if (this.interceptors.putIfAbsent(this.type, object, context -> {
                int i = this.count + 1;
                this.count = i;
                ((InlineConstructionMockContext) context).count = i;
                MockMethodInterceptor interceptor = new MockMethodInterceptor(this.handlerFactory.apply(context), this.settingsFactory.apply(context));
                InlineByteBuddyMockMaker.this.mocks.put(object, interceptor);
                try {
                    this.mockInitializer.prepare(object, context);
                    this.all.add(object);
                } catch (Throwable t) {
                    InlineByteBuddyMockMaker.this.mocks.remove((WeakConcurrentMap) object);
                    throw new MockitoException("Could not initialize mocked construction", t);
                }
            }) != null) {
                throw new MockitoException(StringUtil.join("For " + this.type.getName() + ", static mocking is already registered in the current thread", "", "To create a new mock, the existing static mock registration must be deregistered"));
            }
        }

        @Override // org.mockito.plugins.MockMaker.ConstructionMockControl
        public void disable() {
            if (this.interceptors.remove(this.type) == null) {
                throw new MockitoException(StringUtil.join("Could not deregister " + this.type.getName() + " as a static mock since it is not currently registered", "", "To register a static mock, use Mockito.mockStatic(" + this.type.getSimpleName() + ".class)"));
            }
            this.all.clear();
        }

        @Override // org.mockito.plugins.MockMaker.ConstructionMockControl
        public List<T> getMocks() {
            return (List<T>) this.all;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineByteBuddyMockMaker$InlineConstructionMockContext.class */
    private static class InlineConstructionMockContext implements MockedConstruction.Context {
        private static final Map<String, Class<?>> PRIMITIVES = new HashMap();
        private int count;
        private final Object[] arguments;
        private final Class<?> type;
        private final String[] parameterTypeNames;

        static {
            PRIMITIVES.put(Boolean.TYPE.getName(), Boolean.TYPE);
            PRIMITIVES.put(Byte.TYPE.getName(), Byte.TYPE);
            PRIMITIVES.put(Short.TYPE.getName(), Short.TYPE);
            PRIMITIVES.put(Character.TYPE.getName(), Character.TYPE);
            PRIMITIVES.put(Integer.TYPE.getName(), Integer.TYPE);
            PRIMITIVES.put(Long.TYPE.getName(), Long.TYPE);
            PRIMITIVES.put(Float.TYPE.getName(), Float.TYPE);
            PRIMITIVES.put(Double.TYPE.getName(), Double.TYPE);
        }

        private InlineConstructionMockContext(Object[] arguments, Class<?> type, String[] parameterTypeNames) {
            this.arguments = arguments;
            this.type = type;
            this.parameterTypeNames = parameterTypeNames;
        }

        @Override // org.mockito.MockedConstruction.Context
        public int getCount() {
            if (this.count == 0) {
                throw new MockitoConfigurationException("mocked construction context is not initialized");
            }
            return this.count;
        }

        @Override // org.mockito.MockedConstruction.Context
        public Constructor<?> constructor() {
            String[] strArr;
            Class<?>[] parameterTypes = new Class[this.parameterTypeNames.length];
            int index = 0;
            for (String parameterTypeName : this.parameterTypeNames) {
                if (PRIMITIVES.containsKey(parameterTypeName)) {
                    int i = index;
                    index++;
                    parameterTypes[i] = PRIMITIVES.get(parameterTypeName);
                } else {
                    try {
                        int i2 = index;
                        index++;
                        parameterTypes[i2] = Class.forName(parameterTypeName, false, this.type.getClassLoader());
                    } catch (ClassNotFoundException e) {
                        throw new MockitoException("Could not find parameter of type " + parameterTypeName, e);
                    }
                }
            }
            try {
                return this.type.getDeclaredConstructor(parameterTypes);
            } catch (NoSuchMethodException e2) {
                throw new MockitoException(StringUtil.join("Could not resolve constructor of type", "", this.type.getName(), "", "with arguments of types", Arrays.toString(parameterTypes)), e2);
            }
        }

        @Override // org.mockito.MockedConstruction.Context
        public List<?> arguments() {
            return Collections.unmodifiableList(Arrays.asList(this.arguments));
        }
    }
}
