package net.bytebuddy.dynamic.loading;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassInjector;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy.class */
public class ClassReloadingStrategy implements ClassLoadingStrategy<ClassLoader> {
    private static final String INSTALLER_TYPE = "net.bytebuddy.agent.Installer";
    private static final String INSTRUMENTATION_GETTER = "getInstrumentation";
    private static final Object STATIC_MEMBER = null;
    protected static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
    private final Instrumentation instrumentation;
    private final Strategy strategy;
    private final BootstrapInjection bootstrapInjection;
    private final Map<String, Class<?>> preregisteredTypes;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.strategy.equals(((ClassReloadingStrategy) obj).strategy) && this.instrumentation.equals(((ClassReloadingStrategy) obj).instrumentation) && this.bootstrapInjection.equals(((ClassReloadingStrategy) obj).bootstrapInjection) && this.preregisteredTypes.equals(((ClassReloadingStrategy) obj).preregisteredTypes);
    }

    public int hashCode() {
        return (((((((17 * 31) + this.instrumentation.hashCode()) * 31) + this.strategy.hashCode()) * 31) + this.bootstrapInjection.hashCode()) * 31) + this.preregisteredTypes.hashCode();
    }

    public ClassReloadingStrategy(Instrumentation instrumentation, Strategy strategy) {
        this(instrumentation, strategy, BootstrapInjection.Disabled.INSTANCE, Collections.emptyMap());
    }

    protected ClassReloadingStrategy(Instrumentation instrumentation, Strategy strategy, BootstrapInjection bootstrapInjection, Map<String, Class<?>> preregisteredTypes) {
        this.instrumentation = instrumentation;
        this.strategy = strategy.validate(instrumentation);
        this.bootstrapInjection = bootstrapInjection;
        this.preregisteredTypes = preregisteredTypes;
    }

    public static ClassReloadingStrategy of(Instrumentation instrumentation) {
        if (DISPATCHER.isRetransformClassesSupported(instrumentation)) {
            return new ClassReloadingStrategy(instrumentation, Strategy.RETRANSFORMATION);
        }
        if (instrumentation.isRedefineClassesSupported()) {
            return new ClassReloadingStrategy(instrumentation, Strategy.REDEFINITION);
        }
        throw new IllegalArgumentException("Instrumentation does not support reloading of classes: " + instrumentation);
    }

    public static ClassReloadingStrategy fromInstalledAgent() {
        try {
            return of((Instrumentation) ClassLoader.getSystemClassLoader().loadClass(INSTALLER_TYPE).getMethod(INSTRUMENTATION_GETTER, new Class[0]).invoke(STATIC_MEMBER, new Object[0]));
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception2) {
            throw new IllegalStateException("The Byte Buddy agent is not installed or not accessible", exception2);
        }
    }

    public static ClassReloadingStrategy fromInstalledAgent(Strategy strategy) {
        try {
            return new ClassReloadingStrategy((Instrumentation) ClassLoader.getSystemClassLoader().loadClass(INSTALLER_TYPE).getMethod(INSTRUMENTATION_GETTER, new Class[0]).invoke(STATIC_MEMBER, new Object[0]), strategy);
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception2) {
            throw new IllegalStateException("The Byte Buddy agent is not installed or not accessible", exception2);
        }
    }

    @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
    public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
        Class<?>[] initiatedClasses;
        Map<String, Class<?>> availableTypes = new HashMap<>(this.preregisteredTypes);
        for (Class<?> type : this.instrumentation.getInitiatedClasses(classLoader)) {
            availableTypes.put(TypeDescription.ForLoadedType.getName(type), type);
        }
        Map<Class<?>, ClassDefinition> classDefinitions = new ConcurrentHashMap<>();
        Map<TypeDescription, Class<?>> loadedClasses = new HashMap<>();
        Map<TypeDescription, byte[]> unloadedClasses = new LinkedHashMap<>();
        for (Map.Entry<TypeDescription, byte[]> entry : types.entrySet()) {
            Class<?> type2 = availableTypes.get(entry.getKey().getName());
            if (type2 != null) {
                classDefinitions.put(type2, new ClassDefinition(type2, entry.getValue()));
                loadedClasses.put(entry.getKey(), type2);
            } else {
                unloadedClasses.put(entry.getKey(), entry.getValue());
            }
        }
        try {
            this.strategy.apply(this.instrumentation, classDefinitions);
            if (!unloadedClasses.isEmpty()) {
                loadedClasses.putAll((classLoader == null ? this.bootstrapInjection.make(this.instrumentation) : new ClassInjector.UsingReflection(classLoader)).inject(unloadedClasses));
            }
            return loadedClasses;
        } catch (UnmodifiableClassException exception) {
            throw new IllegalStateException("Cannot redefine specified class", exception);
        } catch (ClassNotFoundException exception2) {
            throw new IllegalArgumentException("Could not locate classes for redefinition", exception2);
        }
    }

    public ClassReloadingStrategy reset(Class<?>... type) throws IOException {
        return type.length == 0 ? this : reset(ClassFileLocator.ForClassLoader.of(type[0].getClassLoader()), type);
    }

    public ClassReloadingStrategy reset(ClassFileLocator classFileLocator, Class<?>... type) throws IOException {
        if (type.length > 0) {
            try {
                this.strategy.reset(this.instrumentation, classFileLocator, Arrays.asList(type));
            } catch (ClassNotFoundException exception) {
                throw new IllegalArgumentException("Cannot locate types " + Arrays.toString(type), exception);
            } catch (UnmodifiableClassException exception2) {
                throw new IllegalStateException("Cannot reset types " + Arrays.toString(type), exception2);
            }
        }
        return this;
    }

    public ClassReloadingStrategy enableBootstrapInjection(File folder) {
        return new ClassReloadingStrategy(this.instrumentation, this.strategy, new BootstrapInjection.Enabled(folder), this.preregisteredTypes);
    }

    public ClassReloadingStrategy preregistered(Class<?>... type) {
        Map<String, Class<?>> preregisteredTypes = new HashMap<>(this.preregisteredTypes);
        for (Class<?> aType : type) {
            preregisteredTypes.put(TypeDescription.ForLoadedType.getName(aType), aType);
        }
        return new ClassReloadingStrategy(this.instrumentation, this.strategy, this.bootstrapInjection, preregisteredTypes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Dispatcher.class */
    public interface Dispatcher {
        boolean isModifiableClass(Instrumentation instrumentation, Class<?> cls);

        boolean isRetransformClassesSupported(Instrumentation instrumentation);

        void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean z);

        void retransformClasses(Instrumentation instrumentation, Class<?>[] clsArr) throws UnmodifiableClassException;

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Dispatcher$CreationAction.class */
        public enum CreationAction implements PrivilegedAction<Dispatcher> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Dispatcher run() {
                try {
                    Class<?> instrumentation = Class.forName("java.lang.instrument.Instrumentation");
                    return new ForJava6CapableVm(instrumentation.getMethod("isModifiableClass", Class.class), instrumentation.getMethod("isRetransformClassesSupported", new Class[0]), instrumentation.getMethod("addTransformer", ClassFileTransformer.class, Boolean.TYPE), instrumentation.getMethod("retransformClasses", Class[].class));
                } catch (ClassNotFoundException e) {
                    return ForLegacyVm.INSTANCE;
                } catch (NoSuchMethodException e2) {
                    return ForLegacyVm.INSTANCE;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Dispatcher$ForLegacyVm.class */
        public enum ForLegacyVm implements Dispatcher {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public boolean isModifiableClass(Instrumentation instrumentation, Class<?> type) {
                return (type.isArray() || type.isPrimitive()) ? false : true;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public boolean isRetransformClassesSupported(Instrumentation instrumentation) {
                return false;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean canRetransform) {
                if (canRetransform) {
                    throw new UnsupportedOperationException("Cannot apply retransformation on legacy VM");
                }
                instrumentation.addTransformer(classFileTransformer);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public void retransformClasses(Instrumentation instrumentation, Class<?>[] type) {
                throw new IllegalStateException();
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Dispatcher$ForJava6CapableVm.class */
        public static class ForJava6CapableVm implements Dispatcher {
            private final Method isModifiableClass;
            private final Method isRetransformClassesSupported;
            private final Method addTransformer;
            private final Method retransformClasses;

            protected ForJava6CapableVm(Method isModifiableClass, Method isRetransformClassesSupported, Method addTransformer, Method retransformClasses) {
                this.isModifiableClass = isModifiableClass;
                this.isRetransformClassesSupported = isRetransformClassesSupported;
                this.addTransformer = addTransformer;
                this.retransformClasses = retransformClasses;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public boolean isModifiableClass(Instrumentation instrumentation, Class<?> type) {
                try {
                    return ((Boolean) this.isModifiableClass.invoke(instrumentation, type)).booleanValue();
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#isModifiableClass", exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#isModifiableClass", exception2.getCause());
                }
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public boolean isRetransformClassesSupported(Instrumentation instrumentation) {
                try {
                    return ((Boolean) this.isRetransformClassesSupported.invoke(instrumentation, new Object[0])).booleanValue();
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#isModifiableClass", exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#isModifiableClass", exception2.getCause());
                }
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean canRetransform) {
                try {
                    this.addTransformer.invoke(instrumentation, classFileTransformer, Boolean.valueOf(canRetransform));
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#addTransformer", exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#addTransformer", exception2.getCause());
                }
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Dispatcher
            public void retransformClasses(Instrumentation instrumentation, Class<?>[] type) throws UnmodifiableClassException {
                try {
                    this.retransformClasses.invoke(instrumentation, type);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#retransformClasses", exception);
                } catch (InvocationTargetException exception2) {
                    UnmodifiableClassException cause = exception2.getCause();
                    if (cause instanceof UnmodifiableClassException) {
                        throw cause;
                    }
                    throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#retransformClasses", cause);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Strategy.class */
    public enum Strategy {
        REDEFINITION(true) { // from class: net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy.1
            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy
            protected void apply(Instrumentation instrumentation, Map<Class<?>, ClassDefinition> classDefinitions) throws UnmodifiableClassException, ClassNotFoundException {
                instrumentation.redefineClasses((ClassDefinition[]) classDefinitions.values().toArray(new ClassDefinition[0]));
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy
            protected Strategy validate(Instrumentation instrumentation) {
                if (!instrumentation.isRedefineClassesSupported()) {
                    throw new IllegalArgumentException("Does not support redefinition: " + instrumentation);
                }
                return this;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy
            public void reset(Instrumentation instrumentation, ClassFileLocator classFileLocator, List<Class<?>> types) throws IOException, UnmodifiableClassException, ClassNotFoundException {
                Map<Class<?>, ClassDefinition> classDefinitions = new HashMap<>(types.size());
                for (Class<?> type : types) {
                    classDefinitions.put(type, new ClassDefinition(type, classFileLocator.locate(TypeDescription.ForLoadedType.getName(type)).resolve()));
                }
                apply(instrumentation, classDefinitions);
            }
        },
        RETRANSFORMATION(false) { // from class: net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy.2
            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy
            protected void apply(Instrumentation instrumentation, Map<Class<?>, ClassDefinition> classDefinitions) throws UnmodifiableClassException {
                ClassRedefinitionTransformer classRedefinitionTransformer = new ClassRedefinitionTransformer(classDefinitions);
                synchronized (this) {
                    ClassReloadingStrategy.DISPATCHER.addTransformer(instrumentation, classRedefinitionTransformer, true);
                    ClassReloadingStrategy.DISPATCHER.retransformClasses(instrumentation, (Class[]) classDefinitions.keySet().toArray(new Class[0]));
                    instrumentation.removeTransformer(classRedefinitionTransformer);
                }
                classRedefinitionTransformer.assertTransformation();
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy
            protected Strategy validate(Instrumentation instrumentation) {
                if (!ClassReloadingStrategy.DISPATCHER.isRetransformClassesSupported(instrumentation)) {
                    throw new IllegalArgumentException("Does not support retransformation: " + instrumentation);
                }
                return this;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy
            public void reset(Instrumentation instrumentation, ClassFileLocator classFileLocator, List<Class<?>> types) throws UnmodifiableClassException, ClassNotFoundException {
                for (Class<?> type : types) {
                    if (!ClassReloadingStrategy.DISPATCHER.isModifiableClass(instrumentation, type)) {
                        throw new IllegalArgumentException("Cannot modify type: " + type);
                    }
                }
                ClassReloadingStrategy.DISPATCHER.addTransformer(instrumentation, ClassResettingTransformer.INSTANCE, true);
                try {
                    ClassReloadingStrategy.DISPATCHER.retransformClasses(instrumentation, (Class[]) types.toArray(new Class[0]));
                    instrumentation.removeTransformer(ClassResettingTransformer.INSTANCE);
                } catch (Throwable th) {
                    instrumentation.removeTransformer(ClassResettingTransformer.INSTANCE);
                    throw th;
                }
            }
        };
        
        private static final byte[] NO_REDEFINITION = null;
        private static final boolean REDEFINE_CLASSES = true;
        private final boolean redefinition;

        protected abstract void apply(Instrumentation instrumentation, Map<Class<?>, ClassDefinition> map) throws UnmodifiableClassException, ClassNotFoundException;

        protected abstract Strategy validate(Instrumentation instrumentation);

        public abstract void reset(Instrumentation instrumentation, ClassFileLocator classFileLocator, List<Class<?>> list) throws IOException, UnmodifiableClassException, ClassNotFoundException;

        Strategy(boolean redefinition) {
            this.redefinition = redefinition;
        }

        public boolean isRedefinition() {
            return this.redefinition;
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Strategy$ClassRedefinitionTransformer.class */
        protected static class ClassRedefinitionTransformer implements ClassFileTransformer {
            private final Map<Class<?>, ClassDefinition> redefinedClasses;

            protected ClassRedefinitionTransformer(Map<Class<?>, ClassDefinition> redefinedClasses) {
                this.redefinedClasses = redefinedClasses;
            }

            @SuppressFBWarnings(value = {"EI_EXPOSE_REP"}, justification = "Value is always null")
            public byte[] transform(ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                ClassDefinition redefinedClass;
                if (internalTypeName != null && (redefinedClass = this.redefinedClasses.remove(classBeingRedefined)) != null) {
                    return redefinedClass.getDefinitionClassFile();
                }
                return Strategy.NO_REDEFINITION;
            }

            public void assertTransformation() {
                if (!this.redefinedClasses.isEmpty()) {
                    throw new IllegalStateException("Could not transform: " + this.redefinedClasses.keySet());
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$Strategy$ClassResettingTransformer.class */
        protected enum ClassResettingTransformer implements ClassFileTransformer {
            INSTANCE;

            public byte[] transform(ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                return Strategy.NO_REDEFINITION;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$BootstrapInjection.class */
    protected interface BootstrapInjection {
        ClassInjector make(Instrumentation instrumentation);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$BootstrapInjection$Disabled.class */
        public enum Disabled implements BootstrapInjection {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.BootstrapInjection
            public ClassInjector make(Instrumentation instrumentation) {
                throw new IllegalStateException("Bootstrap injection is not enabled");
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassReloadingStrategy$BootstrapInjection$Enabled.class */
        public static class Enabled implements BootstrapInjection {
            private final File folder;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.folder.equals(((Enabled) obj).folder);
            }

            public int hashCode() {
                return (17 * 31) + this.folder.hashCode();
            }

            protected Enabled(File folder) {
                this.folder = folder;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassReloadingStrategy.BootstrapInjection
            public ClassInjector make(Instrumentation instrumentation) {
                return ClassInjector.UsingInstrumentation.of(this.folder, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation);
            }
        }
    }
}
