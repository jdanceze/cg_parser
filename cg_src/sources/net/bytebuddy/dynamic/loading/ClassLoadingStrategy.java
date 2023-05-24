package net.bytebuddy.dynamic.loading;

import java.io.File;
import java.lang.ClassLoader;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.Callable;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy.class */
public interface ClassLoadingStrategy<T extends ClassLoader> {
    public static final ClassLoader BOOTSTRAP_LOADER = null;
    public static final ProtectionDomain NO_PROTECTION_DOMAIN = null;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$Configurable.class */
    public interface Configurable<S extends ClassLoader> extends ClassLoadingStrategy<S> {
        Configurable<S> with(ProtectionDomain protectionDomain);

        Configurable<S> with(PackageDefinitionStrategy packageDefinitionStrategy);

        Configurable<S> allowExistingTypes();

        Configurable<S> opened();
    }

    Map<TypeDescription, Class<?>> load(T t, Map<TypeDescription, byte[]> map);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$Default.class */
    public enum Default implements Configurable<ClassLoader> {
        WRAPPER(new WrappingDispatcher(ByteArrayClassLoader.PersistenceHandler.LATENT, false)),
        WRAPPER_PERSISTENT(new WrappingDispatcher(ByteArrayClassLoader.PersistenceHandler.MANIFEST, false)),
        CHILD_FIRST(new WrappingDispatcher(ByteArrayClassLoader.PersistenceHandler.LATENT, true)),
        CHILD_FIRST_PERSISTENT(new WrappingDispatcher(ByteArrayClassLoader.PersistenceHandler.MANIFEST, true)),
        INJECTION(new InjectionDispatcher());
        
        private static final boolean DEFAULT_FORBID_EXISTING = true;
        private final Configurable<ClassLoader> dispatcher;

        Default(Configurable configurable) {
            this.dispatcher = configurable;
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
        public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
            return this.dispatcher.load(classLoader, types);
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
        public Configurable<ClassLoader> with(ProtectionDomain protectionDomain) {
            return this.dispatcher.with(protectionDomain);
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
        public Configurable<ClassLoader> with(PackageDefinitionStrategy packageDefinitionStrategy) {
            return this.dispatcher.with(packageDefinitionStrategy);
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
        public Configurable<ClassLoader> allowExistingTypes() {
            return this.dispatcher.allowExistingTypes();
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
        public Configurable<ClassLoader> opened() {
            return this.dispatcher.opened();
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$Default$InjectionDispatcher.class */
        protected static class InjectionDispatcher implements Configurable<ClassLoader> {
            @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
            private final ProtectionDomain protectionDomain;
            private final PackageDefinitionStrategy packageDefinitionStrategy;
            private final boolean forbidExisting;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj != null && getClass() == obj.getClass() && this.forbidExisting == ((InjectionDispatcher) obj).forbidExisting) {
                    ProtectionDomain protectionDomain = this.protectionDomain;
                    ProtectionDomain protectionDomain2 = ((InjectionDispatcher) obj).protectionDomain;
                    if (protectionDomain2 != null) {
                        if (protectionDomain == null || !protectionDomain.equals(protectionDomain2)) {
                            return false;
                        }
                    } else if (protectionDomain != null) {
                        return false;
                    }
                    return this.packageDefinitionStrategy.equals(((InjectionDispatcher) obj).packageDefinitionStrategy);
                }
                return false;
            }

            public int hashCode() {
                int i = 17 * 31;
                ProtectionDomain protectionDomain = this.protectionDomain;
                if (protectionDomain != null) {
                    i += protectionDomain.hashCode();
                }
                return (((i * 31) + this.packageDefinitionStrategy.hashCode()) * 31) + (this.forbidExisting ? 1 : 0);
            }

            protected InjectionDispatcher() {
                this(NO_PROTECTION_DOMAIN, PackageDefinitionStrategy.NoOp.INSTANCE, true);
            }

            private InjectionDispatcher(ProtectionDomain protectionDomain, PackageDefinitionStrategy packageDefinitionStrategy, boolean forbidExisting) {
                this.protectionDomain = protectionDomain;
                this.packageDefinitionStrategy = packageDefinitionStrategy;
                this.forbidExisting = forbidExisting;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
            public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
                return new ClassInjector.UsingReflection(classLoader, this.protectionDomain, this.packageDefinitionStrategy, this.forbidExisting).inject(types);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> with(ProtectionDomain protectionDomain) {
                return new InjectionDispatcher(protectionDomain, this.packageDefinitionStrategy, this.forbidExisting);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> with(PackageDefinitionStrategy packageDefinitionStrategy) {
                return new InjectionDispatcher(this.protectionDomain, packageDefinitionStrategy, this.forbidExisting);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> allowExistingTypes() {
                return new InjectionDispatcher(this.protectionDomain, this.packageDefinitionStrategy, false);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> opened() {
                return this;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$Default$WrappingDispatcher.class */
        protected static class WrappingDispatcher implements Configurable<ClassLoader> {
            private static final boolean CHILD_FIRST = true;
            private static final boolean PARENT_FIRST = false;
            @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
            private final ProtectionDomain protectionDomain;
            private final ByteArrayClassLoader.PersistenceHandler persistenceHandler;
            private final PackageDefinitionStrategy packageDefinitionStrategy;
            private final boolean childFirst;
            private final boolean forbidExisting;
            private final boolean sealed;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj != null && getClass() == obj.getClass() && this.childFirst == ((WrappingDispatcher) obj).childFirst && this.forbidExisting == ((WrappingDispatcher) obj).forbidExisting && this.sealed == ((WrappingDispatcher) obj).sealed && this.persistenceHandler.equals(((WrappingDispatcher) obj).persistenceHandler)) {
                    ProtectionDomain protectionDomain = this.protectionDomain;
                    ProtectionDomain protectionDomain2 = ((WrappingDispatcher) obj).protectionDomain;
                    if (protectionDomain2 != null) {
                        if (protectionDomain == null || !protectionDomain.equals(protectionDomain2)) {
                            return false;
                        }
                    } else if (protectionDomain != null) {
                        return false;
                    }
                    return this.packageDefinitionStrategy.equals(((WrappingDispatcher) obj).packageDefinitionStrategy);
                }
                return false;
            }

            public int hashCode() {
                int i = 17 * 31;
                ProtectionDomain protectionDomain = this.protectionDomain;
                if (protectionDomain != null) {
                    i += protectionDomain.hashCode();
                }
                return (((((((((i * 31) + this.persistenceHandler.hashCode()) * 31) + this.packageDefinitionStrategy.hashCode()) * 31) + (this.childFirst ? 1 : 0)) * 31) + (this.forbidExisting ? 1 : 0)) * 31) + (this.sealed ? 1 : 0);
            }

            protected WrappingDispatcher(ByteArrayClassLoader.PersistenceHandler persistenceHandler, boolean childFirst) {
                this(NO_PROTECTION_DOMAIN, PackageDefinitionStrategy.Trivial.INSTANCE, persistenceHandler, childFirst, true, true);
            }

            private WrappingDispatcher(ProtectionDomain protectionDomain, PackageDefinitionStrategy packageDefinitionStrategy, ByteArrayClassLoader.PersistenceHandler persistenceHandler, boolean childFirst, boolean forbidExisting, boolean sealed) {
                this.protectionDomain = protectionDomain;
                this.packageDefinitionStrategy = packageDefinitionStrategy;
                this.persistenceHandler = persistenceHandler;
                this.childFirst = childFirst;
                this.forbidExisting = forbidExisting;
                this.sealed = sealed;
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
            public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
                if (this.childFirst) {
                    return ByteArrayClassLoader.ChildFirst.load(classLoader, types, this.protectionDomain, this.persistenceHandler, this.packageDefinitionStrategy, this.forbidExisting, this.sealed);
                }
                return ByteArrayClassLoader.load(classLoader, types, this.protectionDomain, this.persistenceHandler, this.packageDefinitionStrategy, this.forbidExisting, this.sealed);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> with(ProtectionDomain protectionDomain) {
                return new WrappingDispatcher(protectionDomain, this.packageDefinitionStrategy, this.persistenceHandler, this.childFirst, this.forbidExisting, this.sealed);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> with(PackageDefinitionStrategy packageDefinitionStrategy) {
                return new WrappingDispatcher(this.protectionDomain, packageDefinitionStrategy, this.persistenceHandler, this.childFirst, this.forbidExisting, this.sealed);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> allowExistingTypes() {
                return new WrappingDispatcher(this.protectionDomain, this.packageDefinitionStrategy, this.persistenceHandler, this.childFirst, false, this.sealed);
            }

            @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Configurable
            public Configurable<ClassLoader> opened() {
                return new WrappingDispatcher(this.protectionDomain, this.packageDefinitionStrategy, this.persistenceHandler, this.childFirst, this.forbidExisting, false);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$UsingLookup.class */
    public static class UsingLookup implements ClassLoadingStrategy<ClassLoader> {
        private final ClassInjector classInjector;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classInjector.equals(((UsingLookup) obj).classInjector);
        }

        public int hashCode() {
            return (17 * 31) + this.classInjector.hashCode();
        }

        protected UsingLookup(ClassInjector classInjector) {
            this.classInjector = classInjector;
        }

        public static ClassLoadingStrategy<ClassLoader> of(Object lookup) {
            return new UsingLookup(ClassInjector.UsingLookup.of(lookup));
        }

        public static ClassLoadingStrategy<ClassLoader> withFallback(Callable<?> lookup) {
            if (ClassInjector.UsingLookup.isAvailable()) {
                try {
                    return of(lookup.call());
                } catch (Exception exception) {
                    throw new IllegalStateException(exception);
                }
            } else if (ClassInjector.UsingUnsafe.isAvailable()) {
                return new ForUnsafeInjection();
            } else {
                throw new IllegalStateException("Neither lookup or unsafe class injection is available");
            }
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
        public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
            return this.classInjector.inject(types);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$ForBootstrapInjection.class */
    public static class ForBootstrapInjection implements ClassLoadingStrategy<ClassLoader> {
        private final Instrumentation instrumentation;
        private final File folder;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.instrumentation.equals(((ForBootstrapInjection) obj).instrumentation) && this.folder.equals(((ForBootstrapInjection) obj).folder);
        }

        public int hashCode() {
            return (((17 * 31) + this.instrumentation.hashCode()) * 31) + this.folder.hashCode();
        }

        public ForBootstrapInjection(Instrumentation instrumentation, File folder) {
            this.instrumentation = instrumentation;
            this.folder = folder;
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
        public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
            ClassInjector classInjector = classLoader == null ? ClassInjector.UsingInstrumentation.of(this.folder, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, this.instrumentation) : new ClassInjector.UsingReflection(classLoader);
            return classInjector.inject(types);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassLoadingStrategy$ForUnsafeInjection.class */
    public static class ForUnsafeInjection implements ClassLoadingStrategy<ClassLoader> {
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
        private final ProtectionDomain protectionDomain;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ProtectionDomain protectionDomain = this.protectionDomain;
                ProtectionDomain protectionDomain2 = ((ForUnsafeInjection) obj).protectionDomain;
                return protectionDomain2 != null ? protectionDomain != null && protectionDomain.equals(protectionDomain2) : protectionDomain == null;
            }
            return false;
        }

        public int hashCode() {
            int i = 17 * 31;
            ProtectionDomain protectionDomain = this.protectionDomain;
            return protectionDomain != null ? i + protectionDomain.hashCode() : i;
        }

        public ForUnsafeInjection() {
            this(NO_PROTECTION_DOMAIN);
        }

        public ForUnsafeInjection(ProtectionDomain protectionDomain) {
            this.protectionDomain = protectionDomain;
        }

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
        public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
            return new ClassInjector.UsingUnsafe(classLoader, this.protectionDomain).inject(types);
        }
    }
}
