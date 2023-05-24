package net.bytebuddy.build;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin.class */
public interface Plugin extends ElementMatcher<TypeDescription>, Closeable {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$WithPreprocessor.class */
    public interface WithPreprocessor extends Plugin {
        void onPreprocess(TypeDescription typeDescription, ClassFileLocator classFileLocator);
    }

    DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory.class */
    public interface Factory {
        Plugin make();

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$Simple.class */
        public static class Simple implements Factory {
            private final Plugin plugin;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.plugin.equals(((Simple) obj).plugin);
            }

            public int hashCode() {
                return (17 * 31) + this.plugin.hashCode();
            }

            public Simple(Plugin plugin) {
                this.plugin = plugin;
            }

            @Override // net.bytebuddy.build.Plugin.Factory
            public Plugin make() {
                return this.plugin;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection.class */
        public static class UsingReflection implements Factory {
            private final Class<? extends Plugin> type;
            private final List<ArgumentResolver> argumentResolvers;

            @Target({ElementType.CONSTRUCTOR})
            @Documented
            @Retention(RetentionPolicy.RUNTIME)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$Priority.class */
            public @interface Priority {
                public static final int DEFAULT = 0;

                int value();
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.type.equals(((UsingReflection) obj).type) && this.argumentResolvers.equals(((UsingReflection) obj).argumentResolvers);
            }

            public int hashCode() {
                return (((17 * 31) + this.type.hashCode()) * 31) + this.argumentResolvers.hashCode();
            }

            public UsingReflection(Class<? extends Plugin> type) {
                this(type, Collections.emptyList());
            }

            protected UsingReflection(Class<? extends Plugin> type, List<ArgumentResolver> argumentResolvers) {
                this.type = type;
                this.argumentResolvers = argumentResolvers;
            }

            public UsingReflection with(ArgumentResolver... argumentResolver) {
                return with(Arrays.asList(argumentResolver));
            }

            public UsingReflection with(List<? extends ArgumentResolver> argumentResolvers) {
                return new UsingReflection(this.type, CompoundList.of((List) argumentResolvers, (List) this.argumentResolvers));
            }

            @Override // net.bytebuddy.build.Plugin.Factory
            public Plugin make() {
                Constructor<?>[] constructors;
                Class<?>[] parameterTypes;
                Instantiator instantiator = new Instantiator.Unresolved(this.type);
                loop0: for (Constructor<?> constructor : this.type.getConstructors()) {
                    if (!constructor.isSynthetic()) {
                        List<Object> arguments = new ArrayList<>(constructor.getParameterTypes().length);
                        int index = 0;
                        for (Class<?> type : constructor.getParameterTypes()) {
                            boolean resolved = false;
                            Iterator<ArgumentResolver> it = this.argumentResolvers.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                ArgumentResolver argumentResolver = it.next();
                                ArgumentResolver.Resolution resolution = argumentResolver.resolve(index, type);
                                if (resolution.isResolved()) {
                                    arguments.add(resolution.getArgument());
                                    resolved = true;
                                    break;
                                }
                            }
                            if (!resolved) {
                                break loop0;
                            }
                            index++;
                        }
                        instantiator = instantiator.replaceBy(new Instantiator.Resolved(constructor, arguments));
                    }
                }
                return instantiator.instantiate();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$Instantiator.class */
            protected interface Instantiator {
                Instantiator replaceBy(Resolved resolved);

                Plugin instantiate();

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$Instantiator$Unresolved.class */
                public static class Unresolved implements Instantiator {
                    private final Class<? extends Plugin> type;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.type.equals(((Unresolved) obj).type);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.type.hashCode();
                    }

                    protected Unresolved(Class<? extends Plugin> type) {
                        this.type = type;
                    }

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.Instantiator
                    public Instantiator replaceBy(Resolved instantiator) {
                        return instantiator;
                    }

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.Instantiator
                    public Plugin instantiate() {
                        throw new IllegalStateException("No constructor available for " + this.type);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$Instantiator$Resolved.class */
                public static class Resolved implements Instantiator {
                    private final Constructor<? extends Plugin> constructor;
                    private final List<?> arguments;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.constructor.equals(((Resolved) obj).constructor) && this.arguments.equals(((Resolved) obj).arguments);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.constructor.hashCode()) * 31) + this.arguments.hashCode();
                    }

                    protected Resolved(Constructor<? extends Plugin> constructor, List<?> arguments) {
                        this.constructor = constructor;
                        this.arguments = arguments;
                    }

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.Instantiator
                    public Instantiator replaceBy(Resolved instantiator) {
                        Priority left = (Priority) this.constructor.getAnnotation(Priority.class);
                        Priority right = (Priority) instantiator.constructor.getAnnotation(Priority.class);
                        int leftPriority = left == null ? 0 : left.value();
                        int rightPriority = right == null ? 0 : right.value();
                        if (leftPriority > rightPriority) {
                            return this;
                        }
                        if (leftPriority < rightPriority) {
                            return instantiator;
                        }
                        throw new IllegalStateException("Ambiguous constructors " + this.constructor + " and " + instantiator.constructor);
                    }

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.Instantiator
                    public Plugin instantiate() {
                        try {
                            return this.constructor.newInstance(this.arguments.toArray(new Object[0]));
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Failed to access " + this.constructor, exception);
                        } catch (InstantiationException exception2) {
                            throw new IllegalStateException("Failed to instantiate plugin via " + this.constructor, exception2);
                        } catch (InvocationTargetException exception3) {
                            throw new IllegalStateException("Error during construction of" + this.constructor, exception3.getCause());
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver.class */
            public interface ArgumentResolver {
                Resolution resolve(int i, Class<?> cls);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$Resolution.class */
                public interface Resolution {
                    boolean isResolved();

                    Object getArgument();

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$Resolution$Unresolved.class */
                    public enum Unresolved implements Resolution {
                        INSTANCE;

                        @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver.Resolution
                        public boolean isResolved() {
                            return false;
                        }

                        @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver.Resolution
                        public Object getArgument() {
                            throw new IllegalStateException("Cannot get the argument for an unresolved parameter");
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$Resolution$Resolved.class */
                    public static class Resolved implements Resolution {
                        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
                        private final Object argument;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            if (obj != null && getClass() == obj.getClass()) {
                                Object obj2 = this.argument;
                                Object obj3 = ((Resolved) obj).argument;
                                return obj3 != null ? obj2 != null && obj2.equals(obj3) : obj2 == null;
                            }
                            return false;
                        }

                        public int hashCode() {
                            int i = 17 * 31;
                            Object obj = this.argument;
                            return obj != null ? i + obj.hashCode() : i;
                        }

                        public Resolved(Object argument) {
                            this.argument = argument;
                        }

                        @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver.Resolution
                        public boolean isResolved() {
                            return true;
                        }

                        @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver.Resolution
                        public Object getArgument() {
                            return this.argument;
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$NoOp.class */
                public enum NoOp implements ArgumentResolver {
                    INSTANCE;

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver
                    public Resolution resolve(int index, Class<?> type) {
                        return Resolution.Unresolved.INSTANCE;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$ForType.class */
                public static class ForType<T> implements ArgumentResolver {
                    private final Class<? extends T> type;
                    private final T value;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.type.equals(((ForType) obj).type) && this.value.equals(((ForType) obj).value);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.type.hashCode()) * 31) + this.value.hashCode();
                    }

                    protected ForType(Class<? extends T> type, T value) {
                        this.type = type;
                        this.value = value;
                    }

                    public static <S> ArgumentResolver of(Class<? extends S> type, S value) {
                        return new ForType(type, value);
                    }

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver
                    public Resolution resolve(int index, Class<?> type) {
                        return type == this.type ? new Resolution.Resolved(this.value) : Resolution.Unresolved.INSTANCE;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$ForIndex.class */
                public static class ForIndex implements ArgumentResolver {
                    private static final Map<Class<?>, Class<?>> WRAPPER_TYPES = new HashMap();
                    private final int index;
                    private final Object value;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((ForIndex) obj).index && this.value.equals(((ForIndex) obj).value);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.index) * 31) + this.value.hashCode();
                    }

                    static {
                        WRAPPER_TYPES.put(Boolean.TYPE, Boolean.class);
                        WRAPPER_TYPES.put(Byte.TYPE, Byte.class);
                        WRAPPER_TYPES.put(Short.TYPE, Short.class);
                        WRAPPER_TYPES.put(Character.TYPE, Character.class);
                        WRAPPER_TYPES.put(Integer.TYPE, Integer.class);
                        WRAPPER_TYPES.put(Long.TYPE, Long.class);
                        WRAPPER_TYPES.put(Float.TYPE, Float.class);
                        WRAPPER_TYPES.put(Double.TYPE, Double.class);
                    }

                    public ForIndex(int index, Object value) {
                        this.index = index;
                        this.value = value;
                    }

                    @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver
                    public Resolution resolve(int index, Class<?> type) {
                        if (this.index != index) {
                            return Resolution.Unresolved.INSTANCE;
                        }
                        return type.isPrimitive() ? WRAPPER_TYPES.get(type).isInstance(this.value) ? new Resolution.Resolved(this.value) : Resolution.Unresolved.INSTANCE : (this.value == null || type.isInstance(this.value)) ? new Resolution.Resolved(this.value) : Resolution.Unresolved.INSTANCE;
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Factory$UsingReflection$ArgumentResolver$ForIndex$WithDynamicType.class */
                    public static class WithDynamicType implements ArgumentResolver {
                        private final int index;
                        private final String value;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.index == ((WithDynamicType) obj).index && this.value.equals(((WithDynamicType) obj).value);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.index) * 31) + this.value.hashCode();
                        }

                        public WithDynamicType(int index, String value) {
                            this.index = index;
                            this.value = value;
                        }

                        @Override // net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver
                        public Resolution resolve(int index, Class<?> type) {
                            if (this.index != index) {
                                return Resolution.Unresolved.INSTANCE;
                            }
                            if (type == Character.TYPE || type == Character.class) {
                                return this.value.length() == 1 ? new Resolution.Resolved(Character.valueOf(this.value.charAt(0))) : Resolution.Unresolved.INSTANCE;
                            } else if (type == String.class) {
                                return new Resolution.Resolved(this.value);
                            } else {
                                if (type.isPrimitive()) {
                                    type = (Class) ForIndex.WRAPPER_TYPES.get(type);
                                }
                                try {
                                    Method valueOf = type.getMethod("valueOf", String.class);
                                    return (Modifier.isStatic(valueOf.getModifiers()) && type.isAssignableFrom(valueOf.getReturnType())) ? new Resolution.Resolved(valueOf.invoke(null, this.value)) : Resolution.Unresolved.INSTANCE;
                                } catch (IllegalAccessException exception) {
                                    throw new IllegalStateException(exception);
                                } catch (NoSuchMethodException e) {
                                    return Resolution.Unresolved.INSTANCE;
                                } catch (InvocationTargetException exception2) {
                                    throw new IllegalStateException(exception2.getCause());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine.class */
    public interface Engine {
        public static final String CLASS_FILE_EXTENSION = ".class";

        Engine with(ByteBuddy byteBuddy);

        Engine with(TypeStrategy typeStrategy);

        Engine with(PoolStrategy poolStrategy);

        Engine with(ClassFileLocator classFileLocator);

        Engine with(Listener listener);

        Engine withoutErrorHandlers();

        Engine withErrorHandlers(ErrorHandler... errorHandlerArr);

        Engine withErrorHandlers(List<? extends ErrorHandler> list);

        Engine withParallelTransformation(int i);

        Engine with(Dispatcher.Factory factory);

        Engine ignore(ElementMatcher<? super TypeDescription> elementMatcher);

        Summary apply(File file, File file2, Factory... factoryArr) throws IOException;

        Summary apply(File file, File file2, List<? extends Factory> list) throws IOException;

        Summary apply(Source source, Target target, Factory... factoryArr) throws IOException;

        Summary apply(Source source, Target target, List<? extends Factory> list) throws IOException;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$TypeStrategy.class */
        public interface TypeStrategy {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$TypeStrategy$Default.class */
            public enum Default implements TypeStrategy {
                REDEFINE { // from class: net.bytebuddy.build.Plugin.Engine.TypeStrategy.Default.1
                    @Override // net.bytebuddy.build.Plugin.Engine.TypeStrategy
                    public DynamicType.Builder<?> builder(ByteBuddy byteBuddy, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
                        return byteBuddy.redefine(typeDescription, classFileLocator);
                    }
                },
                REBASE { // from class: net.bytebuddy.build.Plugin.Engine.TypeStrategy.Default.2
                    @Override // net.bytebuddy.build.Plugin.Engine.TypeStrategy
                    public DynamicType.Builder<?> builder(ByteBuddy byteBuddy, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
                        return byteBuddy.rebase(typeDescription, classFileLocator);
                    }
                },
                DECORATE { // from class: net.bytebuddy.build.Plugin.Engine.TypeStrategy.Default.3
                    @Override // net.bytebuddy.build.Plugin.Engine.TypeStrategy
                    public DynamicType.Builder<?> builder(ByteBuddy byteBuddy, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
                        return byteBuddy.decorate(typeDescription, classFileLocator);
                    }
                }
            }

            DynamicType.Builder<?> builder(ByteBuddy byteBuddy, TypeDescription typeDescription, ClassFileLocator classFileLocator);

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$TypeStrategy$ForEntryPoint.class */
            public static class ForEntryPoint implements TypeStrategy {
                private final EntryPoint entryPoint;
                private final MethodNameTransformer methodNameTransformer;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.entryPoint.equals(((ForEntryPoint) obj).entryPoint) && this.methodNameTransformer.equals(((ForEntryPoint) obj).methodNameTransformer);
                }

                public int hashCode() {
                    return (((17 * 31) + this.entryPoint.hashCode()) * 31) + this.methodNameTransformer.hashCode();
                }

                public ForEntryPoint(EntryPoint entryPoint, MethodNameTransformer methodNameTransformer) {
                    this.entryPoint = entryPoint;
                    this.methodNameTransformer = methodNameTransformer;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.TypeStrategy
                public DynamicType.Builder<?> builder(ByteBuddy byteBuddy, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
                    return this.entryPoint.transform(typeDescription, byteBuddy, classFileLocator, this.methodNameTransformer);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$PoolStrategy.class */
        public interface PoolStrategy {
            TypePool typePool(ClassFileLocator classFileLocator);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$PoolStrategy$Default.class */
            public enum Default implements PoolStrategy {
                FAST(TypePool.Default.ReaderMode.FAST),
                EXTENDED(TypePool.Default.ReaderMode.EXTENDED);
                
                private final TypePool.Default.ReaderMode readerMode;

                Default(TypePool.Default.ReaderMode readerMode) {
                    this.readerMode = readerMode;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.PoolStrategy
                public TypePool typePool(ClassFileLocator classFileLocator) {
                    return new TypePool.Default.WithLazyResolution(new TypePool.CacheProvider.Simple(), classFileLocator, this.readerMode, TypePool.ClassLoading.ofPlatformLoader());
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$PoolStrategy$Eager.class */
            public enum Eager implements PoolStrategy {
                FAST(TypePool.Default.ReaderMode.FAST),
                EXTENDED(TypePool.Default.ReaderMode.EXTENDED);
                
                private final TypePool.Default.ReaderMode readerMode;

                Eager(TypePool.Default.ReaderMode readerMode) {
                    this.readerMode = readerMode;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.PoolStrategy
                public TypePool typePool(ClassFileLocator classFileLocator) {
                    return new TypePool.Default(new TypePool.CacheProvider.Simple(), classFileLocator, this.readerMode, TypePool.ClassLoading.ofPlatformLoader());
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$ErrorHandler.class */
        public interface ErrorHandler {
            void onError(TypeDescription typeDescription, Plugin plugin, Throwable th);

            void onError(TypeDescription typeDescription, List<Throwable> list);

            void onError(Map<TypeDescription, List<Throwable>> map);

            void onError(Plugin plugin, Throwable th);

            void onLiveInitializer(TypeDescription typeDescription, TypeDescription typeDescription2);

            void onUnresolved(String str);

            void onManifest(Manifest manifest);

            void onResource(String str);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$ErrorHandler$Failing.class */
            public enum Failing implements ErrorHandler {
                FAIL_FAST { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Failing.1
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                        throw new IllegalStateException("Failed to transform " + typeDescription + " using " + plugin, throwable);
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                        throw new UnsupportedOperationException("onError");
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                        throw new UnsupportedOperationException("onError");
                    }
                },
                FAIL_AFTER_TYPE { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Failing.2
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                        throw new IllegalStateException("Failed to transform " + typeDescription + ": " + throwables);
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                        throw new UnsupportedOperationException("onError");
                    }
                },
                FAIL_LAST { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Failing.3
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                        throw new IllegalStateException("Failed to transform at least one type: " + throwables);
                    }
                };

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    throw new IllegalStateException("Failed to close plugin " + plugin, throwable);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$ErrorHandler$Enforcing.class */
            public enum Enforcing implements ErrorHandler {
                ALL_TYPES_RESOLVED { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing.1
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onUnresolved(String typeName) {
                        throw new IllegalStateException("Failed to resolve type description for " + typeName);
                    }
                },
                NO_LIVE_INITIALIZERS { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing.2
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onLiveInitializer(TypeDescription typeDescription, TypeDescription initializedType) {
                        throw new IllegalStateException("Failed to instrument " + typeDescription + " due to live initializer for " + initializedType);
                    }
                },
                CLASS_FILES_ONLY { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing.3
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onResource(String name) {
                        throw new IllegalStateException("Discovered a resource when only class files were allowed: " + name);
                    }
                },
                MANIFEST_REQUIRED { // from class: net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing.4
                    @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler.Enforcing, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                    public void onManifest(Manifest manifest) {
                        if (manifest == null) {
                            throw new IllegalStateException("Required a manifest but no manifest was found");
                        }
                    }
                };

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$ErrorHandler$Compound.class */
            public static class Compound implements ErrorHandler {
                private final List<ErrorHandler> errorHandlers;

                public Compound(ErrorHandler... errorHandler) {
                    this(Arrays.asList(errorHandler));
                }

                public Compound(List<? extends ErrorHandler> errorHandlers) {
                    this.errorHandlers = new ArrayList();
                    for (ErrorHandler errorHandler : errorHandlers) {
                        if (errorHandler instanceof Compound) {
                            this.errorHandlers.addAll(((Compound) errorHandler).errorHandlers);
                        } else if (!(errorHandler instanceof Listener.NoOp)) {
                            this.errorHandlers.add(errorHandler);
                        }
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onError(typeDescription, plugin, throwable);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onError(typeDescription, throwables);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onError(throwables);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onError(plugin, throwable);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onLiveInitializer(typeDescription, definingType);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onUnresolved(typeName);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onManifest(manifest);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                    for (ErrorHandler errorHandler : this.errorHandlers) {
                        errorHandler.onResource(name);
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener.class */
        public interface Listener extends ErrorHandler {
            void onDiscovery(String str);

            void onTransformation(TypeDescription typeDescription, Plugin plugin);

            void onTransformation(TypeDescription typeDescription, List<Plugin> list);

            void onIgnored(TypeDescription typeDescription, Plugin plugin);

            void onIgnored(TypeDescription typeDescription, List<Plugin> list);

            void onComplete(TypeDescription typeDescription);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$NoOp.class */
            public enum NoOp implements Listener {
                INSTANCE;

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onDiscovery(String typeName) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, Plugin plugin) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, List<Plugin> plugins) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, Plugin plugin) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, List<Plugin> plugins) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onComplete(TypeDescription typeDescription) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$Adapter.class */
            public static abstract class Adapter implements Listener {
                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onDiscovery(String typeName) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, Plugin plugin) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, List<Plugin> plugins) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, Plugin plugin) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, List<Plugin> plugins) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onComplete(TypeDescription typeDescription) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$StreamWriting.class */
            public static class StreamWriting extends Adapter {
                protected static final String PREFIX = "[Byte Buddy]";
                private final PrintStream printStream;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.printStream.equals(((StreamWriting) obj).printStream);
                }

                public int hashCode() {
                    return (17 * 31) + this.printStream.hashCode();
                }

                public StreamWriting(PrintStream printStream) {
                    this.printStream = printStream;
                }

                public static StreamWriting toSystemOut() {
                    return new StreamWriting(System.out);
                }

                public static StreamWriting toSystemError() {
                    return new StreamWriting(System.err);
                }

                public Listener withTransformationsOnly() {
                    return new WithTransformationsOnly(this);
                }

                public Listener withErrorsOnly() {
                    return new WithErrorsOnly(this);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.Listener
                public void onDiscovery(String typeName) {
                    this.printStream.printf("[Byte Buddy] DISCOVERY %s", typeName);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, Plugin plugin) {
                    this.printStream.printf("[Byte Buddy] TRANSFORM %s for %s", typeDescription, plugin);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, Plugin plugin) {
                    this.printStream.printf("[Byte Buddy] IGNORE %s for %s", typeDescription, plugin);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    synchronized (this.printStream) {
                        this.printStream.printf("[Byte Buddy] ERROR %s for %s", typeDescription, plugin);
                        throwable.printStackTrace(this.printStream);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    synchronized (this.printStream) {
                        this.printStream.printf("[Byte Buddy] ERROR %s", plugin);
                        throwable.printStackTrace(this.printStream);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                    this.printStream.printf("[Byte Buddy] UNRESOLVED %s", typeName);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                    this.printStream.printf("[Byte Buddy] LIVE %s on %s", typeDescription, definingType);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.Listener
                public void onComplete(TypeDescription typeDescription) {
                    this.printStream.printf("[Byte Buddy] COMPLETE %s", typeDescription);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                    PrintStream printStream = this.printStream;
                    Object[] objArr = new Object[1];
                    objArr[0] = Boolean.valueOf(manifest != null);
                    printStream.printf("[Byte Buddy] MANIFEST %b", objArr);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                    this.printStream.printf("[Byte Buddy] RESOURCE %s", name);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$WithTransformationsOnly.class */
            public static class WithTransformationsOnly extends Adapter {
                private final Listener delegate;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.delegate.equals(((WithTransformationsOnly) obj).delegate);
                }

                public int hashCode() {
                    return (17 * 31) + this.delegate.hashCode();
                }

                public WithTransformationsOnly(Listener delegate) {
                    this.delegate = delegate;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, Plugin plugin) {
                    this.delegate.onTransformation(typeDescription, plugin);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, List<Plugin> plugins) {
                    this.delegate.onTransformation(typeDescription, plugins);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    this.delegate.onError(typeDescription, plugin, throwable);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                    this.delegate.onError(typeDescription, throwables);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                    this.delegate.onError(throwables);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    this.delegate.onError(plugin, throwable);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$WithErrorsOnly.class */
            public static class WithErrorsOnly extends Adapter {
                private final Listener delegate;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.delegate.equals(((WithErrorsOnly) obj).delegate);
                }

                public int hashCode() {
                    return (17 * 31) + this.delegate.hashCode();
                }

                public WithErrorsOnly(Listener delegate) {
                    this.delegate = delegate;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    this.delegate.onError(typeDescription, plugin, throwable);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                    this.delegate.onError(typeDescription, throwables);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                    this.delegate.onError(throwables);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    this.delegate.onError(plugin, throwable);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$ForErrorHandler.class */
            public static class ForErrorHandler extends Adapter {
                private final ErrorHandler errorHandler;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.errorHandler.equals(((ForErrorHandler) obj).errorHandler);
                }

                public int hashCode() {
                    return (17 * 31) + this.errorHandler.hashCode();
                }

                public ForErrorHandler(ErrorHandler errorHandler) {
                    this.errorHandler = errorHandler;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    this.errorHandler.onError(typeDescription, plugin, throwable);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                    this.errorHandler.onError(typeDescription, throwables);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                    this.errorHandler.onError(throwables);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    this.errorHandler.onError(plugin, throwable);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                    this.errorHandler.onLiveInitializer(typeDescription, definingType);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                    this.errorHandler.onUnresolved(typeName);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                    this.errorHandler.onManifest(manifest);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener.Adapter, net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                    this.errorHandler.onResource(name);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Listener$Compound.class */
            public static class Compound implements Listener {
                private final List<Listener> listeners;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.listeners.equals(((Compound) obj).listeners);
                }

                public int hashCode() {
                    return (17 * 31) + this.listeners.hashCode();
                }

                public Compound(Listener... listener) {
                    this(Arrays.asList(listener));
                }

                public Compound(List<? extends Listener> listeners) {
                    this.listeners = new ArrayList();
                    for (Listener listener : listeners) {
                        if (listener instanceof Compound) {
                            this.listeners.addAll(((Compound) listener).listeners);
                        } else if (!(listener instanceof NoOp)) {
                            this.listeners.add(listener);
                        }
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onDiscovery(String typeName) {
                    for (Listener listener : this.listeners) {
                        listener.onDiscovery(typeName);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, Plugin plugin) {
                    for (Listener listener : this.listeners) {
                        listener.onTransformation(typeDescription, plugin);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onTransformation(TypeDescription typeDescription, List<Plugin> plugins) {
                    for (Listener listener : this.listeners) {
                        listener.onTransformation(typeDescription, plugins);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, Plugin plugin) {
                    for (Listener listener : this.listeners) {
                        listener.onIgnored(typeDescription, plugin);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onIgnored(TypeDescription typeDescription, List<Plugin> plugins) {
                    for (Listener listener : this.listeners) {
                        listener.onIgnored(typeDescription, plugins);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, Plugin plugin, Throwable throwable) {
                    for (Listener listener : this.listeners) {
                        listener.onError(typeDescription, plugin, throwable);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(TypeDescription typeDescription, List<Throwable> throwables) {
                    for (Listener listener : this.listeners) {
                        listener.onError(typeDescription, throwables);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Map<TypeDescription, List<Throwable>> throwables) {
                    for (Listener listener : this.listeners) {
                        listener.onError(throwables);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onError(Plugin plugin, Throwable throwable) {
                    for (Listener listener : this.listeners) {
                        listener.onError(plugin, throwable);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onLiveInitializer(TypeDescription typeDescription, TypeDescription definingType) {
                    for (Listener listener : this.listeners) {
                        listener.onLiveInitializer(typeDescription, definingType);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Listener
                public void onComplete(TypeDescription typeDescription) {
                    for (Listener listener : this.listeners) {
                        listener.onComplete(typeDescription);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onUnresolved(String typeName) {
                    for (Listener listener : this.listeners) {
                        listener.onUnresolved(typeName);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onManifest(Manifest manifest) {
                    for (Listener listener : this.listeners) {
                        listener.onManifest(manifest);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.ErrorHandler
                public void onResource(String name) {
                    for (Listener listener : this.listeners) {
                        listener.onResource(name);
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source.class */
        public interface Source {
            Origin read() throws IOException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Origin.class */
            public interface Origin extends Iterable<Element>, Closeable {
                public static final Manifest NO_MANIFEST = null;

                Manifest getManifest() throws IOException;

                ClassFileLocator getClassFileLocator();

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Origin$ForJarFile.class */
                public static class ForJarFile implements Origin {
                    private final JarFile file;

                    public ForJarFile(JarFile file) {
                        this.file = file;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                    public Manifest getManifest() throws IOException {
                        return this.file.getManifest();
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                    public ClassFileLocator getClassFileLocator() {
                        return new ClassFileLocator.ForJarFile(this.file);
                    }

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public void close() throws IOException {
                        this.file.close();
                    }

                    @Override // java.lang.Iterable
                    public Iterator<Element> iterator() {
                        return new JarFileIterator(this.file.entries());
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Origin$ForJarFile$JarFileIterator.class */
                    protected class JarFileIterator implements Iterator<Element> {
                        private final Enumeration<JarEntry> enumeration;

                        protected JarFileIterator(Enumeration<JarEntry> enumeration) {
                            this.enumeration = enumeration;
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.enumeration.hasMoreElements();
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Iterator
                        public Element next() {
                            return new Element.ForJarEntry(ForJarFile.this.file, this.enumeration.nextElement());
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Element.class */
            public interface Element {
                String getName();

                InputStream getInputStream() throws IOException;

                <T> T resolveAs(Class<T> cls);

                @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Not mutating the byte array is part of the class contract.")
                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Element$ForByteArray.class */
                public static class ForByteArray implements Element {
                    private final String name;
                    private final byte[] binaryRepresentation;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.name.equals(((ForByteArray) obj).name) && Arrays.equals(this.binaryRepresentation, ((ForByteArray) obj).binaryRepresentation);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.name.hashCode()) * 31) + Arrays.hashCode(this.binaryRepresentation);
                    }

                    public ForByteArray(String name, byte[] binaryRepresentation) {
                        this.name = name;
                        this.binaryRepresentation = binaryRepresentation;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public String getName() {
                        return this.name;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public InputStream getInputStream() {
                        return new ByteArrayInputStream(this.binaryRepresentation);
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public <T> T resolveAs(Class<T> type) {
                        return null;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Element$ForFile.class */
                public static class ForFile implements Element {
                    private final File root;
                    private final File file;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.root.equals(((ForFile) obj).root) && this.file.equals(((ForFile) obj).file);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.root.hashCode()) * 31) + this.file.hashCode();
                    }

                    public ForFile(File root, File file) {
                        this.root = root;
                        this.file = file;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public String getName() {
                        return this.root.getAbsoluteFile().toURI().relativize(this.file.getAbsoluteFile().toURI()).getPath();
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public InputStream getInputStream() throws IOException {
                        return new FileInputStream(this.file);
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public <T> T resolveAs(Class<T> type) {
                        if (File.class.isAssignableFrom(type)) {
                            return (T) this.file;
                        }
                        return null;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Element$ForJarEntry.class */
                public static class ForJarEntry implements Element {
                    private final JarFile file;
                    private final JarEntry entry;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.file.equals(((ForJarEntry) obj).file) && this.entry.equals(((ForJarEntry) obj).entry);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.file.hashCode()) * 31) + this.entry.hashCode();
                    }

                    public ForJarEntry(JarFile file, JarEntry entry) {
                        this.file = file;
                        this.entry = entry;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public String getName() {
                        return this.entry.getName();
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public InputStream getInputStream() throws IOException {
                        return this.file.getInputStream(this.entry);
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Source.Element
                    public <T> T resolveAs(Class<T> type) {
                        if (JarEntry.class.isAssignableFrom(type)) {
                            return (T) this.entry;
                        }
                        return null;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$Empty.class */
            public enum Empty implements Source, Origin {
                INSTANCE;

                @Override // net.bytebuddy.build.Plugin.Engine.Source
                public Origin read() {
                    return this;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                public ClassFileLocator getClassFileLocator() {
                    return ClassFileLocator.NoOp.INSTANCE;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                public Manifest getManifest() {
                    return NO_MANIFEST;
                }

                @Override // java.lang.Iterable
                public Iterator<Element> iterator() {
                    return Collections.emptySet().iterator();
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$InMemory.class */
            public static class InMemory implements Source, Origin {
                private final Map<String, byte[]> storage;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.storage.equals(((InMemory) obj).storage);
                }

                public int hashCode() {
                    return (17 * 31) + this.storage.hashCode();
                }

                public InMemory(Map<String, byte[]> storage) {
                    this.storage = storage;
                }

                public static Source ofTypes(Class<?>... type) {
                    return ofTypes(Arrays.asList(type));
                }

                public static Source ofTypes(Collection<? extends Class<?>> types) {
                    Map<TypeDescription, byte[]> binaryRepresentations = new HashMap<>();
                    for (Class<?> type : types) {
                        binaryRepresentations.put(TypeDescription.ForLoadedType.of(type), ClassFileLocator.ForClassLoader.read(type));
                    }
                    return ofTypes(binaryRepresentations);
                }

                public static Source ofTypes(Map<TypeDescription, byte[]> binaryRepresentations) {
                    Map<String, byte[]> storage = new HashMap<>();
                    for (Map.Entry<TypeDescription, byte[]> entry : binaryRepresentations.entrySet()) {
                        storage.put(entry.getKey().getInternalName() + ".class", entry.getValue());
                    }
                    return new InMemory(storage);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source
                public Origin read() {
                    return this;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                public ClassFileLocator getClassFileLocator() {
                    return ClassFileLocator.Simple.ofResources(this.storage);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                public Manifest getManifest() throws IOException {
                    byte[] binaryRepresentation = this.storage.get("META-INF/MANIFEST.MF");
                    if (binaryRepresentation == null) {
                        return NO_MANIFEST;
                    }
                    return new Manifest(new ByteArrayInputStream(binaryRepresentation));
                }

                @Override // java.lang.Iterable
                public Iterator<Element> iterator() {
                    return new MapEntryIterator(this.storage.entrySet().iterator());
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$InMemory$MapEntryIterator.class */
                protected static class MapEntryIterator implements Iterator<Element> {
                    private final Iterator<Map.Entry<String, byte[]>> iterator;

                    protected MapEntryIterator(Iterator<Map.Entry<String, byte[]>> iterator) {
                        this.iterator = iterator;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iterator.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public Element next() {
                        Map.Entry<String, byte[]> entry = this.iterator.next();
                        return new Element.ForByteArray(entry.getKey(), entry.getValue());
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("remove");
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$ForFolder.class */
            public static class ForFolder implements Source, Origin {
                private final File folder;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.folder.equals(((ForFolder) obj).folder);
                }

                public int hashCode() {
                    return (17 * 31) + this.folder.hashCode();
                }

                public ForFolder(File folder) {
                    this.folder = folder;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source
                public Origin read() {
                    return this;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                public ClassFileLocator getClassFileLocator() {
                    return new ClassFileLocator.ForFolder(this.folder);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source.Origin
                public Manifest getManifest() throws IOException {
                    File file = new File(this.folder, "META-INF/MANIFEST.MF");
                    if (file.exists()) {
                        InputStream inputStream = new FileInputStream(file);
                        try {
                            Manifest manifest = new Manifest(inputStream);
                            inputStream.close();
                            return manifest;
                        } catch (Throwable th) {
                            inputStream.close();
                            throw th;
                        }
                    }
                    return NO_MANIFEST;
                }

                @Override // java.lang.Iterable
                public Iterator<Element> iterator() {
                    return new FolderIterator(this.folder);
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$ForFolder$FolderIterator.class */
                protected class FolderIterator implements Iterator<Element> {
                    private final LinkedList<File> files;

                    protected FolderIterator(File folder) {
                        this.files = new LinkedList<>(Collections.singleton(folder));
                        while (true) {
                            File candidate = this.files.removeFirst();
                            File[] file = candidate.listFiles();
                            if (file != null) {
                                this.files.addAll(0, Arrays.asList(file));
                            }
                            if (this.files.isEmpty()) {
                                return;
                            }
                            if (!this.files.peek().isDirectory() && !this.files.peek().equals(new File(folder, "META-INF/MANIFEST.MF"))) {
                                return;
                            }
                        }
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return !this.files.isEmpty();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    @SuppressFBWarnings(value = {"IT_NO_SUCH_ELEMENT"}, justification = "Exception is thrown by invoking removeFirst on an empty list.")
                    public Element next() {
                        boolean isEmpty;
                        boolean isDirectory;
                        boolean equals;
                        try {
                            while (true) {
                                if (!isEmpty) {
                                    if (!isDirectory) {
                                        if (!equals) {
                                            break;
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }
                            return new Element.ForFile(ForFolder.this.folder, this.files.removeFirst());
                        } finally {
                            while (!this.files.isEmpty() && (this.files.peek().isDirectory() || this.files.peek().equals(new File(ForFolder.this.folder, "META-INF/MANIFEST.MF")))) {
                                File folder = this.files.removeFirst();
                                File[] file = folder.listFiles();
                                if (file != null) {
                                    this.files.addAll(0, Arrays.asList(file));
                                }
                            }
                        }
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("remove");
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Source$ForJarFile.class */
            public static class ForJarFile implements Source {
                private final File file;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.file.equals(((ForJarFile) obj).file);
                }

                public int hashCode() {
                    return (17 * 31) + this.file.hashCode();
                }

                public ForJarFile(File file) {
                    this.file = file;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Source
                public Origin read() throws IOException {
                    return new Origin.ForJarFile(new JarFile(this.file));
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target.class */
        public interface Target {
            Sink write(Manifest manifest) throws IOException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$Sink.class */
            public interface Sink extends Closeable {
                void store(Map<TypeDescription, byte[]> map) throws IOException;

                void retain(Source.Element element) throws IOException;

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$Sink$ForJarOutputStream.class */
                public static class ForJarOutputStream implements Sink {
                    private final JarOutputStream outputStream;

                    public ForJarOutputStream(JarOutputStream outputStream) {
                        this.outputStream = outputStream;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                    public void store(Map<TypeDescription, byte[]> binaryRepresentations) throws IOException {
                        for (Map.Entry<TypeDescription, byte[]> entry : binaryRepresentations.entrySet()) {
                            this.outputStream.putNextEntry(new JarEntry(entry.getKey().getInternalName() + ".class"));
                            this.outputStream.write(entry.getValue());
                            this.outputStream.closeEntry();
                        }
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                    public void retain(Source.Element element) throws IOException {
                        JarEntry entry = (JarEntry) element.resolveAs(JarEntry.class);
                        this.outputStream.putNextEntry(entry == null ? new JarEntry(element.getName()) : entry);
                        InputStream inputStream = element.getInputStream();
                        try {
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int length = inputStream.read(buffer);
                                if (length != -1) {
                                    this.outputStream.write(buffer, 0, length);
                                } else {
                                    this.outputStream.closeEntry();
                                    return;
                                }
                            }
                        } finally {
                            inputStream.close();
                        }
                    }

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public void close() throws IOException {
                        this.outputStream.close();
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$Discarding.class */
            public enum Discarding implements Target, Sink {
                INSTANCE;

                @Override // net.bytebuddy.build.Plugin.Engine.Target
                public Sink write(Manifest manifest) {
                    return this;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                public void store(Map<TypeDescription, byte[]> binaryRepresentations) {
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                public void retain(Source.Element element) {
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$InMemory.class */
            public static class InMemory implements Target, Sink {
                private final Map<String, byte[]> storage;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.storage.equals(((InMemory) obj).storage);
                }

                public int hashCode() {
                    return (17 * 31) + this.storage.hashCode();
                }

                public InMemory() {
                    this(new HashMap());
                }

                public InMemory(Map<String, byte[]> storage) {
                    this.storage = storage;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target
                public Sink write(Manifest manifest) throws IOException {
                    if (manifest != null) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try {
                            manifest.write(outputStream);
                            this.storage.put("META-INF/MANIFEST.MF", outputStream.toByteArray());
                        } finally {
                            outputStream.close();
                        }
                    }
                    return this;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                public void store(Map<TypeDescription, byte[]> binaryRepresentations) {
                    for (Map.Entry<TypeDescription, byte[]> entry : binaryRepresentations.entrySet()) {
                        this.storage.put(entry.getKey().getInternalName() + ".class", entry.getValue());
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                public void retain(Source.Element element) throws IOException {
                    String name = element.getName();
                    if (!name.endsWith("/")) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try {
                            InputStream inputStream = element.getInputStream();
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int length = inputStream.read(buffer);
                                if (length != -1) {
                                    outputStream.write(buffer, 0, length);
                                } else {
                                    inputStream.close();
                                    this.storage.put(element.getName(), outputStream.toByteArray());
                                    return;
                                }
                            }
                        } finally {
                            outputStream.close();
                        }
                    }
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                public Map<String, byte[]> getStorage() {
                    return this.storage;
                }

                public Map<String, byte[]> toTypeMap() {
                    Map<String, byte[]> binaryRepresentations = new HashMap<>();
                    for (Map.Entry<String, byte[]> entry : this.storage.entrySet()) {
                        if (entry.getKey().endsWith(".class")) {
                            binaryRepresentations.put(entry.getKey().substring(0, entry.getKey().length() - ".class".length()).replace('/', '.'), entry.getValue());
                        }
                    }
                    return binaryRepresentations;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$ForFolder.class */
            public static class ForFolder implements Target, Sink {
                protected static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
                private final File folder;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.folder.equals(((ForFolder) obj).folder);
                }

                public int hashCode() {
                    return (17 * 31) + this.folder.hashCode();
                }

                public ForFolder(File folder) {
                    this.folder = folder;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target
                public Sink write(Manifest manifest) throws IOException {
                    if (manifest != null) {
                        File target = new File(this.folder, "META-INF/MANIFEST.MF");
                        if (!target.getParentFile().isDirectory() && !target.getParentFile().mkdirs()) {
                            throw new IOException("Could not create directory: " + target.getParent());
                        }
                        OutputStream outputStream = new FileOutputStream(target);
                        try {
                            manifest.write(outputStream);
                            outputStream.close();
                        } catch (Throwable th) {
                            outputStream.close();
                            throw th;
                        }
                    }
                    return this;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                public void store(Map<TypeDescription, byte[]> binaryRepresentations) throws IOException {
                    for (Map.Entry<TypeDescription, byte[]> entry : binaryRepresentations.entrySet()) {
                        File target = new File(this.folder, entry.getKey().getInternalName() + ".class");
                        if (!target.getParentFile().isDirectory() && !target.getParentFile().mkdirs()) {
                            throw new IOException("Could not create directory: " + target.getParent());
                        }
                        OutputStream outputStream = new FileOutputStream(target);
                        try {
                            outputStream.write(entry.getValue());
                            outputStream.close();
                        } catch (Throwable th) {
                            outputStream.close();
                            throw th;
                        }
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target.Sink
                public void retain(Source.Element element) throws IOException {
                    String name = element.getName();
                    if (!name.endsWith("/")) {
                        File target = new File(this.folder, name);
                        File resolved = (File) element.resolveAs(File.class);
                        if (!target.getCanonicalPath().startsWith(this.folder.getCanonicalPath())) {
                            throw new IllegalArgumentException(target + " is not a subdirectory of " + this.folder);
                        }
                        if (!target.getParentFile().isDirectory() && !target.getParentFile().mkdirs()) {
                            throw new IOException("Could not create directory: " + target.getParent());
                        }
                        if (DISPATCHER.isAlive() && resolved != null && !resolved.equals(target)) {
                            DISPATCHER.copy(resolved, target);
                        } else if (!target.equals(resolved)) {
                            InputStream inputStream = element.getInputStream();
                            try {
                                OutputStream outputStream = new FileOutputStream(target);
                                byte[] buffer = new byte[1024];
                                while (true) {
                                    int length = inputStream.read(buffer);
                                    if (length != -1) {
                                        outputStream.write(buffer, 0, length);
                                    } else {
                                        outputStream.close();
                                        return;
                                    }
                                }
                            } finally {
                                inputStream.close();
                            }
                        }
                    }
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$ForFolder$Dispatcher.class */
                protected interface Dispatcher {
                    boolean isAlive();

                    void copy(File file, File file2) throws IOException;

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$ForFolder$Dispatcher$CreationAction.class */
                    public enum CreationAction implements PrivilegedAction<Dispatcher> {
                        INSTANCE;

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Dispatcher run() {
                            try {
                                Class<?> path = Class.forName("java.nio.file.Path");
                                Object[] arguments = (Object[]) Array.newInstance(Class.forName("java.nio.file.CopyOption"), 1);
                                arguments[0] = Enum.valueOf(Class.forName("java.nio.file.StandardCopyOption"), "REPLACE_EXISTING");
                                return new ForJava7CapableVm(File.class.getMethod("toPath", new Class[0]), Class.forName("java.nio.file.Files").getMethod("copy", path, path, arguments.getClass()), arguments);
                            } catch (Throwable th) {
                                return ForLegacyVm.INSTANCE;
                            }
                        }
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$ForFolder$Dispatcher$ForLegacyVm.class */
                    public enum ForLegacyVm implements Dispatcher {
                        INSTANCE;

                        @Override // net.bytebuddy.build.Plugin.Engine.Target.ForFolder.Dispatcher
                        public boolean isAlive() {
                            return false;
                        }

                        @Override // net.bytebuddy.build.Plugin.Engine.Target.ForFolder.Dispatcher
                        public void copy(File source, File target) {
                            throw new UnsupportedOperationException("Cannot use NIO2 copy on current VM");
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$ForFolder$Dispatcher$ForJava7CapableVm.class */
                    public static class ForJava7CapableVm implements Dispatcher {
                        private final Method toPath;
                        private final Method copy;
                        private final Object[] copyOptions;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.toPath.equals(((ForJava7CapableVm) obj).toPath) && this.copy.equals(((ForJava7CapableVm) obj).copy) && Arrays.equals(this.copyOptions, ((ForJava7CapableVm) obj).copyOptions);
                        }

                        public int hashCode() {
                            return (((((17 * 31) + this.toPath.hashCode()) * 31) + this.copy.hashCode()) * 31) + Arrays.hashCode(this.copyOptions);
                        }

                        protected ForJava7CapableVm(Method toPath, Method copy, Object[] copyOptions) {
                            this.toPath = toPath;
                            this.copy = copy;
                            this.copyOptions = copyOptions;
                        }

                        @Override // net.bytebuddy.build.Plugin.Engine.Target.ForFolder.Dispatcher
                        public boolean isAlive() {
                            return true;
                        }

                        @Override // net.bytebuddy.build.Plugin.Engine.Target.ForFolder.Dispatcher
                        public void copy(File source, File target) throws IOException {
                            try {
                                this.copy.invoke(null, this.toPath.invoke(source, new Object[0]), this.toPath.invoke(target, new Object[0]), this.copyOptions);
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
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Target$ForJarFile.class */
            public static class ForJarFile implements Target {
                private final File file;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.file.equals(((ForJarFile) obj).file);
                }

                public int hashCode() {
                    return (17 * 31) + this.file.hashCode();
                }

                public ForJarFile(File file) {
                    this.file = file;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Target
                public Sink write(Manifest manifest) throws IOException {
                    return manifest == null ? new Sink.ForJarOutputStream(new JarOutputStream(new FileOutputStream(this.file))) : new Sink.ForJarOutputStream(new JarOutputStream(new FileOutputStream(this.file), manifest));
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher.class */
        public interface Dispatcher extends Closeable {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$Factory.class */
            public interface Factory {
                Dispatcher make(Target.Sink sink, List<TypeDescription> list, Map<TypeDescription, List<Throwable>> map, List<String> list2);
            }

            void accept(Callable<? extends Callable<? extends Materializable>> callable, boolean z) throws IOException;

            void complete() throws IOException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$Materializable.class */
            public interface Materializable {
                void materialize(Target.Sink sink, List<TypeDescription> list, Map<TypeDescription, List<Throwable>> map, List<String> list2) throws IOException;

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$Materializable$ForTransformedElement.class */
                public static class ForTransformedElement implements Materializable {
                    private final DynamicType dynamicType;

                    protected ForTransformedElement(DynamicType dynamicType) {
                        this.dynamicType = dynamicType;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Materializable
                    public void materialize(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) throws IOException {
                        sink.store(this.dynamicType.getAllTypes());
                        transformed.add(this.dynamicType.getTypeDescription());
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$Materializable$ForRetainedElement.class */
                public static class ForRetainedElement implements Materializable {
                    private final Source.Element element;

                    protected ForRetainedElement(Source.Element element) {
                        this.element = element;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Materializable
                    public void materialize(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) throws IOException {
                        sink.retain(this.element);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$Materializable$ForFailedElement.class */
                public static class ForFailedElement implements Materializable {
                    private final Source.Element element;
                    private final TypeDescription typeDescription;
                    private final List<Throwable> errored;

                    protected ForFailedElement(Source.Element element, TypeDescription typeDescription, List<Throwable> errored) {
                        this.element = element;
                        this.typeDescription = typeDescription;
                        this.errored = errored;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Materializable
                    public void materialize(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) throws IOException {
                        sink.retain(this.element);
                        failed.put(this.typeDescription, this.errored);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$Materializable$ForUnresolvedElement.class */
                public static class ForUnresolvedElement implements Materializable {
                    private final Source.Element element;
                    private final String typeName;

                    protected ForUnresolvedElement(Source.Element element, String typeName) {
                        this.element = element;
                        this.typeName = typeName;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Materializable
                    public void materialize(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) throws IOException {
                        sink.retain(this.element);
                        unresolved.add(this.typeName);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForSerialTransformation.class */
            public static class ForSerialTransformation implements Dispatcher {
                private final Target.Sink sink;
                private final List<TypeDescription> transformed;
                private final Map<TypeDescription, List<Throwable>> failed;
                private final List<String> unresolved;
                private final List<Callable<? extends Materializable>> preprocessings = new ArrayList();

                protected ForSerialTransformation(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                    this.sink = sink;
                    this.transformed = transformed;
                    this.failed = failed;
                    this.unresolved = unresolved;
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher
                public void accept(Callable<? extends Callable<? extends Materializable>> work, boolean eager) throws IOException {
                    try {
                        Callable<? extends Materializable> preprocessed = work.call();
                        if (eager) {
                            preprocessed.call().materialize(this.sink, this.transformed, this.failed, this.unresolved);
                        } else {
                            this.preprocessings.add(preprocessed);
                        }
                    } catch (Exception exception) {
                        if (exception instanceof IOException) {
                            throw ((IOException) exception);
                        }
                        if (exception instanceof RuntimeException) {
                            throw ((RuntimeException) exception);
                        }
                        throw new IllegalStateException(exception);
                    }
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher
                public void complete() throws IOException {
                    for (Callable<? extends Materializable> preprocessing : this.preprocessings) {
                        if (Thread.interrupted()) {
                            Thread.currentThread().interrupt();
                            throw new IllegalStateException("Interrupted during plugin engine completion");
                        }
                        try {
                            preprocessing.call().materialize(this.sink, this.transformed, this.failed, this.unresolved);
                        } catch (Exception exception) {
                            if (exception instanceof IOException) {
                                throw ((IOException) exception);
                            }
                            if (exception instanceof RuntimeException) {
                                throw ((RuntimeException) exception);
                            }
                            throw new IllegalStateException(exception);
                        }
                    }
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForSerialTransformation$Factory.class */
                public enum Factory implements Factory {
                    INSTANCE;

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Factory
                    public Dispatcher make(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                        return new ForSerialTransformation(sink, transformed, failed, unresolved);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForParallelTransformation.class */
            public static class ForParallelTransformation implements Dispatcher {
                private final Target.Sink sink;
                private final List<TypeDescription> transformed;
                private final Map<TypeDescription, List<Throwable>> failed;
                private final List<String> unresolved;
                private final CompletionService<Callable<Materializable>> preprocessings;
                private final CompletionService<Materializable> materializers;
                private int deferred;
                private final Set<Future<?>> futures = new HashSet();

                protected ForParallelTransformation(Executor executor, Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                    this.sink = sink;
                    this.transformed = transformed;
                    this.failed = failed;
                    this.unresolved = unresolved;
                    this.preprocessings = new ExecutorCompletionService(executor);
                    this.materializers = new ExecutorCompletionService(executor);
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher
                public void accept(Callable<? extends Callable<? extends Materializable>> work, boolean eager) {
                    if (eager) {
                        this.futures.add(this.materializers.submit(new EagerWork(work)));
                        return;
                    }
                    this.deferred++;
                    this.futures.add(this.preprocessings.submit(work));
                }

                @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher
                public void complete() throws IOException {
                    try {
                        List<Callable<Materializable>> preprocessings = new ArrayList<>(this.deferred);
                        while (true) {
                            int i = this.deferred;
                            this.deferred = i - 1;
                            if (i <= 0) {
                                break;
                            }
                            Future<Callable<Materializable>> future = this.preprocessings.take();
                            this.futures.remove(future);
                            preprocessings.add(future.get());
                        }
                        for (Callable<Materializable> preprocessing : preprocessings) {
                            this.futures.add(this.materializers.submit(preprocessing));
                        }
                        while (!this.futures.isEmpty()) {
                            Future<Materializable> future2 = this.materializers.take();
                            this.futures.remove(future2);
                            future2.get().materialize(this.sink, this.transformed, this.failed, this.unresolved);
                        }
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(exception);
                    } catch (ExecutionException exception2) {
                        Throwable cause = exception2.getCause();
                        if (cause instanceof IOException) {
                            throw ((IOException) cause);
                        }
                        if (cause instanceof RuntimeException) {
                            throw ((RuntimeException) cause);
                        }
                        if (cause instanceof Error) {
                            throw ((Error) cause);
                        }
                        throw new IllegalStateException(cause);
                    }
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                    for (Future<?> future : this.futures) {
                        future.cancel(true);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForParallelTransformation$WithThrowawayExecutorService.class */
                public static class WithThrowawayExecutorService extends ForParallelTransformation {
                    private final ExecutorService executorService;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.executorService.equals(((WithThrowawayExecutorService) obj).executorService);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.executorService.hashCode();
                    }

                    protected WithThrowawayExecutorService(ExecutorService executorService, Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                        super(executorService, sink, transformed, failed, unresolved);
                        this.executorService = executorService;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.ForParallelTransformation, java.io.Closeable, java.lang.AutoCloseable
                    public void close() {
                        try {
                            super.close();
                        } finally {
                            this.executorService.shutdown();
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForParallelTransformation$WithThrowawayExecutorService$Factory.class */
                    public static class Factory implements Factory {
                        private final int threads;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.threads == ((Factory) obj).threads;
                        }

                        public int hashCode() {
                            return (17 * 31) + this.threads;
                        }

                        public Factory(int threads) {
                            this.threads = threads;
                        }

                        @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Factory
                        public Dispatcher make(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                            return new WithThrowawayExecutorService(Executors.newFixedThreadPool(this.threads), sink, transformed, failed, unresolved);
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForParallelTransformation$Factory.class */
                public static class Factory implements Factory {
                    private final Executor executor;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.executor.equals(((Factory) obj).executor);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.executor.hashCode();
                    }

                    public Factory(Executor executor) {
                        this.executor = executor;
                    }

                    @Override // net.bytebuddy.build.Plugin.Engine.Dispatcher.Factory
                    public Dispatcher make(Target.Sink sink, List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                        return new ForParallelTransformation(this.executor, sink, transformed, failed, unresolved);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Dispatcher$ForParallelTransformation$EagerWork.class */
                protected static class EagerWork implements Callable<Materializable> {
                    private final Callable<? extends Callable<? extends Materializable>> work;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.work.equals(((EagerWork) obj).work);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.work.hashCode();
                    }

                    protected EagerWork(Callable<? extends Callable<? extends Materializable>> work) {
                        this.work = work;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Materializable call() throws Exception {
                        return this.work.call().call();
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Summary.class */
        public static class Summary {
            private final List<TypeDescription> transformed;
            private final Map<TypeDescription, List<Throwable>> failed;
            private final List<String> unresolved;

            public Summary(List<TypeDescription> transformed, Map<TypeDescription, List<Throwable>> failed, List<String> unresolved) {
                this.transformed = transformed;
                this.failed = failed;
                this.unresolved = unresolved;
            }

            public List<TypeDescription> getTransformed() {
                return this.transformed;
            }

            public Map<TypeDescription, List<Throwable>> getFailed() {
                return this.failed;
            }

            public List<String> getUnresolved() {
                return this.unresolved;
            }

            public int hashCode() {
                int result = this.transformed.hashCode();
                return (31 * ((31 * result) + this.failed.hashCode())) + this.unresolved.hashCode();
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (other == null || getClass() != other.getClass()) {
                    return false;
                }
                Summary summary = (Summary) other;
                return this.transformed.equals(summary.transformed) && this.failed.equals(summary.failed) && this.unresolved.equals(summary.unresolved);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$AbstractBase.class */
        public static abstract class AbstractBase implements Engine {
            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine withErrorHandlers(ErrorHandler... errorHandler) {
                return withErrorHandlers(Arrays.asList(errorHandler));
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine withParallelTransformation(int threads) {
                if (threads < 1) {
                    throw new IllegalArgumentException("Number of threads must be positive: " + threads);
                }
                return with(new Dispatcher.ForParallelTransformation.WithThrowawayExecutorService.Factory(threads));
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Summary apply(File source, File target, Factory... factory) throws IOException {
                return apply(source, target, Arrays.asList(factory));
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Summary apply(File source, File target, List<? extends Factory> factories) throws IOException {
                return apply(source.isDirectory() ? new Source.ForFolder(source) : new Source.ForJarFile(source), target.isDirectory() ? new Target.ForFolder(target) : new Target.ForJarFile(target), factories);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Summary apply(Source source, Target target, Factory... factory) throws IOException {
                return apply(source, target, Arrays.asList(factory));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Default.class */
        public static class Default extends AbstractBase {
            private final ByteBuddy byteBuddy;
            private final TypeStrategy typeStrategy;
            private final PoolStrategy poolStrategy;
            private final ClassFileLocator classFileLocator;
            private final Listener listener;
            private final ErrorHandler errorHandler;
            private final Dispatcher.Factory dispatcherFactory;
            private final ElementMatcher.Junction<? super TypeDescription> ignoredTypeMatcher;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.byteBuddy.equals(((Default) obj).byteBuddy) && this.typeStrategy.equals(((Default) obj).typeStrategy) && this.poolStrategy.equals(((Default) obj).poolStrategy) && this.classFileLocator.equals(((Default) obj).classFileLocator) && this.listener.equals(((Default) obj).listener) && this.errorHandler.equals(((Default) obj).errorHandler) && this.dispatcherFactory.equals(((Default) obj).dispatcherFactory) && this.ignoredTypeMatcher.equals(((Default) obj).ignoredTypeMatcher);
            }

            public int hashCode() {
                return (((((((((((((((17 * 31) + this.byteBuddy.hashCode()) * 31) + this.typeStrategy.hashCode()) * 31) + this.poolStrategy.hashCode()) * 31) + this.classFileLocator.hashCode()) * 31) + this.listener.hashCode()) * 31) + this.errorHandler.hashCode()) * 31) + this.dispatcherFactory.hashCode()) * 31) + this.ignoredTypeMatcher.hashCode();
            }

            public Default() {
                this(new ByteBuddy());
            }

            public Default(ByteBuddy byteBuddy) {
                this(byteBuddy, TypeStrategy.Default.REBASE);
            }

            protected Default(ByteBuddy byteBuddy, TypeStrategy typeStrategy) {
                this(byteBuddy, typeStrategy, PoolStrategy.Default.FAST, ClassFileLocator.NoOp.INSTANCE, Listener.NoOp.INSTANCE, new ErrorHandler.Compound(ErrorHandler.Failing.FAIL_FAST, ErrorHandler.Enforcing.ALL_TYPES_RESOLVED, ErrorHandler.Enforcing.NO_LIVE_INITIALIZERS), Dispatcher.ForSerialTransformation.Factory.INSTANCE, ElementMatchers.none());
            }

            protected Default(ByteBuddy byteBuddy, TypeStrategy typeStrategy, PoolStrategy poolStrategy, ClassFileLocator classFileLocator, Listener listener, ErrorHandler errorHandler, Dispatcher.Factory dispatcherFactory, ElementMatcher.Junction<? super TypeDescription> ignoredTypeMatcher) {
                this.byteBuddy = byteBuddy;
                this.typeStrategy = typeStrategy;
                this.poolStrategy = poolStrategy;
                this.classFileLocator = classFileLocator;
                this.listener = listener;
                this.errorHandler = errorHandler;
                this.dispatcherFactory = dispatcherFactory;
                this.ignoredTypeMatcher = ignoredTypeMatcher;
            }

            public static Engine of(EntryPoint entryPoint, ClassFileVersion classFileVersion, MethodNameTransformer methodNameTransformer) {
                return new Default(entryPoint.byteBuddy(classFileVersion), new TypeStrategy.ForEntryPoint(entryPoint, methodNameTransformer));
            }

            public static void main(String... argument) throws ClassNotFoundException, IOException {
                if (argument.length < 2) {
                    throw new IllegalArgumentException("Expected arguments: <source> <target> [<plugin>, ...]");
                }
                List<Factory> factories = new ArrayList<>(argument.length - 2);
                for (String plugin : Arrays.asList(argument).subList(2, argument.length)) {
                    factories.add(new Factory.UsingReflection(Class.forName(plugin)));
                }
                new Default().apply(new File(argument[0]), new File(argument[1]), factories);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine with(ByteBuddy byteBuddy) {
                return new Default(byteBuddy, this.typeStrategy, this.poolStrategy, this.classFileLocator, this.listener, this.errorHandler, this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine with(TypeStrategy typeStrategy) {
                return new Default(this.byteBuddy, typeStrategy, this.poolStrategy, this.classFileLocator, this.listener, this.errorHandler, this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine with(PoolStrategy poolStrategy) {
                return new Default(this.byteBuddy, this.typeStrategy, poolStrategy, this.classFileLocator, this.listener, this.errorHandler, this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine with(ClassFileLocator classFileLocator) {
                return new Default(this.byteBuddy, this.typeStrategy, this.poolStrategy, new ClassFileLocator.Compound(this.classFileLocator, classFileLocator), this.listener, this.errorHandler, this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine with(Listener listener) {
                return new Default(this.byteBuddy, this.typeStrategy, this.poolStrategy, this.classFileLocator, new Listener.Compound(this.listener, listener), this.errorHandler, this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine withoutErrorHandlers() {
                return new Default(this.byteBuddy, this.typeStrategy, this.poolStrategy, this.classFileLocator, this.listener, Listener.NoOp.INSTANCE, this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine withErrorHandlers(List<? extends ErrorHandler> errorHandlers) {
                return new Default(this.byteBuddy, this.typeStrategy, this.poolStrategy, this.classFileLocator, this.listener, new ErrorHandler.Compound(errorHandlers), this.dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine with(Dispatcher.Factory dispatcherFactory) {
                return new Default(this.byteBuddy, this.typeStrategy, this.poolStrategy, this.classFileLocator, this.listener, this.errorHandler, dispatcherFactory, this.ignoredTypeMatcher);
            }

            @Override // net.bytebuddy.build.Plugin.Engine
            public Engine ignore(ElementMatcher<? super TypeDescription> matcher) {
                return new Default(this.byteBuddy, this.typeStrategy, this.poolStrategy, this.classFileLocator, this.listener, this.errorHandler, this.dispatcherFactory, this.ignoredTypeMatcher.or(matcher));
            }

            /* JADX WARN: Finally extract failed */
            @Override // net.bytebuddy.build.Plugin.Engine
            public Summary apply(Source source, Target target, List<? extends Factory> factories) throws IOException {
                Listener listener = new Listener.Compound(this.listener, new Listener.ForErrorHandler(this.errorHandler));
                List<TypeDescription> transformed = new ArrayList<>();
                Map<TypeDescription, List<Throwable>> failed = new LinkedHashMap<>();
                List<String> unresolved = new ArrayList<>();
                Throwable rethrown = null;
                List<Plugin> plugins = new ArrayList<>(factories.size());
                List<WithPreprocessor> preprocessors = new ArrayList<>();
                try {
                    for (Factory factory : factories) {
                        Plugin plugin = factory.make();
                        plugins.add(plugin);
                        if (plugin instanceof WithPreprocessor) {
                            preprocessors.add((WithPreprocessor) plugin);
                        }
                    }
                    Source.Origin origin = source.read();
                    ClassFileLocator classFileLocator = new ClassFileLocator.Compound(origin.getClassFileLocator(), this.classFileLocator);
                    TypePool typePool = this.poolStrategy.typePool(classFileLocator);
                    Manifest manifest = origin.getManifest();
                    listener.onManifest(manifest);
                    Target.Sink sink = target.write(manifest);
                    try {
                        Dispatcher dispatcher = this.dispatcherFactory.make(sink, transformed, failed, unresolved);
                        try {
                            for (Source.Element element : origin) {
                                if (Thread.interrupted()) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException("Thread interrupted during plugin engine application");
                                }
                                String name = element.getName();
                                while (name.startsWith("/")) {
                                    name = name.substring(1);
                                }
                                if (name.endsWith(".class")) {
                                    dispatcher.accept(new Preprocessor(element, name.substring(0, name.length() - ".class".length()).replace('/', '.'), classFileLocator, typePool, listener, plugins, preprocessors), preprocessors.isEmpty());
                                } else if (!name.equals("META-INF/MANIFEST.MF")) {
                                    listener.onResource(name);
                                    sink.retain(element);
                                }
                            }
                            dispatcher.complete();
                            dispatcher.close();
                            if (!failed.isEmpty()) {
                                listener.onError(failed);
                            }
                            sink.close();
                            origin.close();
                            if (rethrown == null) {
                                return new Summary(transformed, failed, unresolved);
                            }
                            if (rethrown instanceof IOException) {
                                throw ((IOException) rethrown);
                            }
                            if (rethrown instanceof RuntimeException) {
                                throw ((RuntimeException) rethrown);
                            }
                            throw new IllegalStateException(rethrown);
                        } catch (Throwable th) {
                            dispatcher.close();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        sink.close();
                        throw th2;
                    }
                } finally {
                    for (Plugin plugin2 : plugins) {
                        try {
                            plugin2.close();
                        } catch (Throwable throwable) {
                            try {
                                listener.onError(plugin2, throwable);
                            } catch (Throwable chained) {
                                rethrown = rethrown == null ? chained : rethrown;
                            }
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Default$Preprocessor.class */
            public class Preprocessor implements Callable<Callable<? extends Dispatcher.Materializable>> {
                private final Source.Element element;
                private final String typeName;
                private final ClassFileLocator classFileLocator;
                private final TypePool typePool;
                private final Listener listener;
                private final List<Plugin> plugins;
                private final List<WithPreprocessor> preprocessors;

                private Preprocessor(Source.Element element, String typeName, ClassFileLocator classFileLocator, TypePool typePool, Listener listener, List<Plugin> plugins, List<WithPreprocessor> preprocessors) {
                    this.element = element;
                    this.typeName = typeName;
                    this.classFileLocator = classFileLocator;
                    this.typePool = typePool;
                    this.listener = listener;
                    this.plugins = plugins;
                    this.preprocessors = preprocessors;
                }

                @Override // java.util.concurrent.Callable
                /* renamed from: call */
                public Callable<? extends Dispatcher.Materializable> call2() throws Exception {
                    this.listener.onDiscovery(this.typeName);
                    TypePool.Resolution resolution = this.typePool.describe(this.typeName);
                    if (resolution.isResolved()) {
                        TypeDescription typeDescription = resolution.resolve();
                        try {
                            if (!Default.this.ignoredTypeMatcher.matches(typeDescription)) {
                                for (WithPreprocessor preprocessor : this.preprocessors) {
                                    preprocessor.onPreprocess(typeDescription, this.classFileLocator);
                                }
                                return new Resolved(typeDescription);
                            }
                            return new Ignored(typeDescription);
                        } catch (Throwable throwable) {
                            this.listener.onComplete(typeDescription);
                            if (throwable instanceof Exception) {
                                throw ((Exception) throwable);
                            }
                            if (throwable instanceof Error) {
                                throw ((Error) throwable);
                            }
                            throw new IllegalStateException(throwable);
                        }
                    }
                    return new Unresolved();
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Default$Preprocessor$Resolved.class */
                public class Resolved implements Callable<Dispatcher.Materializable> {
                    private final TypeDescription typeDescription;

                    private Resolved(TypeDescription typeDescription) {
                        this.typeDescription = typeDescription;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Dispatcher.Materializable call() {
                        List<Plugin> applied = new ArrayList<>();
                        List<Plugin> ignored = new ArrayList<>();
                        List<Throwable> errored = new ArrayList<>();
                        try {
                            DynamicType.Builder<?> builder = Default.this.typeStrategy.builder(Default.this.byteBuddy, this.typeDescription, Preprocessor.this.classFileLocator);
                            for (Plugin plugin : Preprocessor.this.plugins) {
                                if (plugin.matches(this.typeDescription)) {
                                    builder = plugin.apply(builder, this.typeDescription, Preprocessor.this.classFileLocator);
                                    Preprocessor.this.listener.onTransformation(this.typeDescription, plugin);
                                    applied.add(plugin);
                                } else {
                                    Preprocessor.this.listener.onIgnored(this.typeDescription, plugin);
                                    ignored.add(plugin);
                                }
                            }
                            if (!errored.isEmpty()) {
                                Preprocessor.this.listener.onError(this.typeDescription, errored);
                                Dispatcher.Materializable.ForFailedElement forFailedElement = new Dispatcher.Materializable.ForFailedElement(Preprocessor.this.element, this.typeDescription, errored);
                                Preprocessor.this.listener.onComplete(this.typeDescription);
                                return forFailedElement;
                            } else if (applied.isEmpty()) {
                                Preprocessor.this.listener.onIgnored(this.typeDescription, ignored);
                                Dispatcher.Materializable.ForRetainedElement forRetainedElement = new Dispatcher.Materializable.ForRetainedElement(Preprocessor.this.element);
                                Preprocessor.this.listener.onComplete(this.typeDescription);
                                return forRetainedElement;
                            } else {
                                DynamicType dynamicType = builder.make(TypeResolutionStrategy.Disabled.INSTANCE, Preprocessor.this.typePool);
                                Preprocessor.this.listener.onTransformation(this.typeDescription, applied);
                                for (Map.Entry<TypeDescription, LoadedTypeInitializer> entry : dynamicType.getLoadedTypeInitializers().entrySet()) {
                                    if (entry.getValue().isAlive()) {
                                        Preprocessor.this.listener.onLiveInitializer(this.typeDescription, entry.getKey());
                                    }
                                }
                                Dispatcher.Materializable.ForTransformedElement forTransformedElement = new Dispatcher.Materializable.ForTransformedElement(dynamicType);
                                Preprocessor.this.listener.onComplete(this.typeDescription);
                                return forTransformedElement;
                            }
                        } catch (Throwable th) {
                            Preprocessor.this.listener.onComplete(this.typeDescription);
                            throw th;
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Default$Preprocessor$Ignored.class */
                public class Ignored implements Callable<Dispatcher.Materializable> {
                    private final TypeDescription typeDescription;

                    private Ignored(TypeDescription typeDescription) {
                        this.typeDescription = typeDescription;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Dispatcher.Materializable call() {
                        try {
                            Preprocessor.this.listener.onIgnored(this.typeDescription, Preprocessor.this.plugins);
                            return new Dispatcher.Materializable.ForRetainedElement(Preprocessor.this.element);
                        } finally {
                            Preprocessor.this.listener.onComplete(this.typeDescription);
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$Engine$Default$Preprocessor$Unresolved.class */
                public class Unresolved implements Callable<Dispatcher.Materializable> {
                    private Unresolved() {
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Dispatcher.Materializable call() {
                        Preprocessor.this.listener.onUnresolved(Preprocessor.this.typeName);
                        return new Dispatcher.Materializable.ForUnresolvedElement(Preprocessor.this.element, Preprocessor.this.typeName);
                    }
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$NoOp.class */
    public static class NoOp implements Plugin, Factory {
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass();
        }

        public int hashCode() {
            return 17;
        }

        @Override // net.bytebuddy.build.Plugin.Factory
        public Plugin make() {
            return this;
        }

        @Override // net.bytebuddy.matcher.ElementMatcher
        public boolean matches(TypeDescription target) {
            return false;
        }

        @Override // net.bytebuddy.build.Plugin
        public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
            throw new IllegalStateException("Cannot apply non-operational plugin");
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/Plugin$ForElementMatcher.class */
    public static abstract class ForElementMatcher implements Plugin {
        private final ElementMatcher<? super TypeDescription> matcher;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.matcher.equals(((ForElementMatcher) obj).matcher);
        }

        public int hashCode() {
            return (17 * 31) + this.matcher.hashCode();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public ForElementMatcher(ElementMatcher<? super TypeDescription> matcher) {
            this.matcher = matcher;
        }

        @Override // net.bytebuddy.matcher.ElementMatcher
        public boolean matches(TypeDescription target) {
            return this.matcher.matches(target);
        }
    }
}
