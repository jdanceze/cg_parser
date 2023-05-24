package net.bytebuddy.dynamic.loading;

import android.provider.ContactsContract;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy;
import net.bytebuddy.utility.JavaModule;
import soot.PolymorphicMethodRef;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader.class */
public class ByteArrayClassLoader extends InjectionClassLoader {
    public static final String URL_SCHEMA = "bytebuddy";
    private static final int FROM_BEGINNING = 0;
    private static final Class<?> UNLOADED_TYPE = null;
    private static final URL NO_URL = null;
    private static final PackageLookupStrategy PACKAGE_LOOKUP_STRATEGY = (PackageLookupStrategy) AccessController.doPrivileged(PackageLookupStrategy.CreationAction.INSTANCE);
    protected static final SynchronizationStrategy.Initializable SYNCHRONIZATION_STRATEGY = (SynchronizationStrategy.Initializable) AccessController.doPrivileged(SynchronizationStrategy.CreationAction.INSTANCE);
    protected final ConcurrentMap<String, byte[]> typeDefinitions;
    protected final PersistenceHandler persistenceHandler;
    protected final ProtectionDomain protectionDomain;
    protected final PackageDefinitionStrategy packageDefinitionStrategy;
    protected final ClassFileTransformer classFileTransformer;
    protected final AccessControlContext accessControlContext;

    public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> typeDefinitions) {
        this(parent, true, typeDefinitions);
    }

    public ByteArrayClassLoader(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions) {
        this(parent, sealed, typeDefinitions, PersistenceHandler.LATENT);
    }

    public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> typeDefinitions, PersistenceHandler persistenceHandler) {
        this(parent, true, typeDefinitions, persistenceHandler);
    }

    public ByteArrayClassLoader(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions, PersistenceHandler persistenceHandler) {
        this(parent, sealed, typeDefinitions, ClassLoadingStrategy.NO_PROTECTION_DOMAIN, persistenceHandler, PackageDefinitionStrategy.Trivial.INSTANCE);
    }

    public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy) {
        this(parent, true, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy);
    }

    public ByteArrayClassLoader(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy) {
        this(parent, sealed, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy, NoOpClassFileTransformer.INSTANCE);
    }

    public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy, ClassFileTransformer classFileTransformer) {
        this(parent, true, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy, classFileTransformer);
    }

    public ByteArrayClassLoader(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy, ClassFileTransformer classFileTransformer) {
        super(parent, sealed);
        this.typeDefinitions = new ConcurrentHashMap(typeDefinitions);
        this.protectionDomain = protectionDomain;
        this.persistenceHandler = persistenceHandler;
        this.packageDefinitionStrategy = packageDefinitionStrategy;
        this.classFileTransformer = classFileTransformer;
        this.accessControlContext = AccessController.getContext();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object methodHandle() throws Exception {
        return Class.forName("java.lang.invoke.MethodHandles").getMethod(ContactsContract.ContactsColumns.LOOKUP_KEY, new Class[0]).invoke(null, new Object[0]);
    }

    public static Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
        return load(classLoader, types, ClassLoadingStrategy.NO_PROTECTION_DOMAIN, PersistenceHandler.LATENT, PackageDefinitionStrategy.Trivial.INSTANCE, false, true);
    }

    @SuppressFBWarnings(value = {"DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit user responsibility")
    public static Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy, boolean forbidExisting, boolean sealed) {
        Map<String, byte[]> typesByName = new HashMap<>();
        for (Map.Entry<TypeDescription, byte[]> entry : types.entrySet()) {
            typesByName.put(entry.getKey().getName(), entry.getValue());
        }
        ClassLoader classLoader2 = new ByteArrayClassLoader(classLoader, sealed, typesByName, protectionDomain, persistenceHandler, packageDefinitionStrategy, NoOpClassFileTransformer.INSTANCE);
        Map<TypeDescription, Class<?>> result = new LinkedHashMap<>();
        for (TypeDescription typeDescription : types.keySet()) {
            try {
                Class<?> type = Class.forName(typeDescription.getName(), false, classLoader2);
                if (forbidExisting && type.getClassLoader() != classLoader2) {
                    throw new IllegalStateException("Class already loaded: " + type);
                }
                result.put(typeDescription, type);
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Cannot load class " + typeDescription, exception);
            }
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.bytebuddy.dynamic.loading.InjectionClassLoader
    protected Map<String, Class<?>> doDefineClasses(Map<String, byte[]> typeDefinitions) throws ClassNotFoundException {
        Map<String, byte[]> previous = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : typeDefinitions.entrySet()) {
            previous.put(entry.getKey(), this.typeDefinitions.putIfAbsent(entry.getKey(), entry.getValue()));
        }
        try {
            Map<String, Class<?>> types = new LinkedHashMap<>();
            for (String name : typeDefinitions.keySet()) {
                synchronized (SYNCHRONIZATION_STRATEGY.initialize().getClassLoadingLock(this, name)) {
                    types.put(name, loadClass(name));
                }
            }
            return types;
        } finally {
            for (Map.Entry<String, byte[]> entry2 : previous.entrySet()) {
                if (entry2.getValue() == null) {
                    this.persistenceHandler.release(entry2.getKey(), this.typeDefinitions);
                } else {
                    this.typeDefinitions.put(entry2.getKey(), entry2.getValue());
                }
            }
        }
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] binaryRepresentation = this.persistenceHandler.lookup(name, this.typeDefinitions);
        if (binaryRepresentation == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            byte[] transformed = this.classFileTransformer.transform(this, name, UNLOADED_TYPE, this.protectionDomain, binaryRepresentation);
            if (transformed != null) {
                binaryRepresentation = transformed;
            }
            return (Class) AccessController.doPrivileged(new ClassDefinitionAction(name, binaryRepresentation), this.accessControlContext);
        } catch (IllegalClassFormatException exception) {
            throw new IllegalStateException("The class file for " + name + " is not legal", exception);
        }
    }

    @Override // java.lang.ClassLoader
    protected URL findResource(String name) {
        return this.persistenceHandler.url(name, this.typeDefinitions);
    }

    @Override // java.lang.ClassLoader
    protected Enumeration<URL> findResources(String name) {
        URL url = this.persistenceHandler.url(name, this.typeDefinitions);
        return url == null ? EmptyEnumeration.INSTANCE : new SingletonEnumeration(url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Package doGetPackage(String name) {
        return getPackage(name);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SynchronizationStrategy.class */
    protected interface SynchronizationStrategy {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SynchronizationStrategy$Initializable.class */
        public interface Initializable {
            SynchronizationStrategy initialize();
        }

        Object getClassLoadingLock(ByteArrayClassLoader byteArrayClassLoader, String str);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SynchronizationStrategy$CreationAction.class */
        public enum CreationAction implements PrivilegedAction<Initializable> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
            public Initializable run() {
                try {
                    Class<?> methodType = Class.forName("java.lang.invoke.MethodType");
                    Class<?> methodHandle = Class.forName(PolymorphicMethodRef.METHODHANDLE_SIGNATURE);
                    return new ForJava8CapableVm(Class.forName("java.lang.invoke.MethodHandles$Lookup").getMethod("findVirtual", Class.class, String.class, methodType).invoke(ByteArrayClassLoader.methodHandle(), ClassLoader.class, "getClassLoadingLock", methodType.getMethod("methodType", Class.class, Class[].class).invoke(null, Object.class, new Class[]{String.class})), methodHandle.getMethod("bindTo", Object.class), methodHandle.getMethod("invokeWithArguments", Object[].class));
                } catch (Exception e) {
                    try {
                        return (ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V9) && ByteArrayClassLoader.class.getClassLoader() == null) ? ForLegacyVm.INSTANCE : new ForJava7CapableVm(ClassLoader.class.getDeclaredMethod("getClassLoadingLock", String.class));
                    } catch (Exception e2) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SynchronizationStrategy$ForLegacyVm.class */
        public enum ForLegacyVm implements SynchronizationStrategy, Initializable {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.SynchronizationStrategy
            public Object getClassLoadingLock(ByteArrayClassLoader classLoader, String name) {
                return classLoader;
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.SynchronizationStrategy.Initializable
            public SynchronizationStrategy initialize() {
                return this;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SynchronizationStrategy$ForJava7CapableVm.class */
        public static class ForJava7CapableVm implements SynchronizationStrategy, Initializable {
            private final Method method;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.method.equals(((ForJava7CapableVm) obj).method);
            }

            public int hashCode() {
                return (17 * 31) + this.method.hashCode();
            }

            protected ForJava7CapableVm(Method method) {
                this.method = method;
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.SynchronizationStrategy
            public Object getClassLoadingLock(ByteArrayClassLoader classLoader, String name) {
                try {
                    return this.method.invoke(classLoader, name);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access class loading lock for " + name + " on " + classLoader, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Error when getting " + name + " on " + classLoader, exception2);
                }
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.SynchronizationStrategy.Initializable
            @SuppressFBWarnings(value = {"DP_DO_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicitly user responsibility")
            public SynchronizationStrategy initialize() {
                try {
                    this.method.setAccessible(true);
                    return this;
                } catch (Exception e) {
                    return ForLegacyVm.INSTANCE;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SynchronizationStrategy$ForJava8CapableVm.class */
        public static class ForJava8CapableVm implements SynchronizationStrategy, Initializable {
            private final Object methodHandle;
            private final Method bindTo;
            private final Method invokeWithArguments;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodHandle.equals(((ForJava8CapableVm) obj).methodHandle) && this.bindTo.equals(((ForJava8CapableVm) obj).bindTo) && this.invokeWithArguments.equals(((ForJava8CapableVm) obj).invokeWithArguments);
            }

            public int hashCode() {
                return (((((17 * 31) + this.methodHandle.hashCode()) * 31) + this.bindTo.hashCode()) * 31) + this.invokeWithArguments.hashCode();
            }

            protected ForJava8CapableVm(Object methodHandle, Method bindTo, Method invokeWithArguments) {
                this.methodHandle = methodHandle;
                this.bindTo = bindTo;
                this.invokeWithArguments = invokeWithArguments;
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.SynchronizationStrategy.Initializable
            public SynchronizationStrategy initialize() {
                return this;
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.SynchronizationStrategy
            public Object getClassLoadingLock(ByteArrayClassLoader classLoader, String name) {
                try {
                    return this.invokeWithArguments.invoke(this.bindTo.invoke(this.methodHandle, classLoader), new Object[]{name});
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access class loading lock for " + name + " on " + classLoader, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Error when getting " + name + " on " + classLoader, exception2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$ClassDefinitionAction.class */
    public class ClassDefinitionAction implements PrivilegedAction<Class<?>> {
        private final String name;
        private final byte[] binaryRepresentation;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.name.equals(((ClassDefinitionAction) obj).name) && Arrays.equals(this.binaryRepresentation, ((ClassDefinitionAction) obj).binaryRepresentation) && ByteArrayClassLoader.this.equals(ByteArrayClassLoader.this);
        }

        public int hashCode() {
            return (((((17 * 31) + this.name.hashCode()) * 31) + Arrays.hashCode(this.binaryRepresentation)) * 31) + ByteArrayClassLoader.this.hashCode();
        }

        protected ClassDefinitionAction(String name, byte[] binaryRepresentation) {
            this.name = name;
            this.binaryRepresentation = binaryRepresentation;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Class<?> run() {
            int packageIndex = this.name.lastIndexOf(46);
            if (packageIndex != -1) {
                String packageName = this.name.substring(0, packageIndex);
                PackageDefinitionStrategy.Definition definition = ByteArrayClassLoader.this.packageDefinitionStrategy.define(ByteArrayClassLoader.this, packageName, this.name);
                if (definition.isDefined()) {
                    Package definedPackage = ByteArrayClassLoader.PACKAGE_LOOKUP_STRATEGY.apply(ByteArrayClassLoader.this, packageName);
                    if (definedPackage == null) {
                        ByteArrayClassLoader.this.definePackage(packageName, definition.getSpecificationTitle(), definition.getSpecificationVersion(), definition.getSpecificationVendor(), definition.getImplementationTitle(), definition.getImplementationVersion(), definition.getImplementationVendor(), definition.getSealBase());
                    } else if (!definition.isCompatibleTo(definedPackage)) {
                        throw new SecurityException("Sealing violation for package " + packageName);
                    }
                }
            }
            return ByteArrayClassLoader.this.defineClass(this.name, this.binaryRepresentation, 0, this.binaryRepresentation.length, ByteArrayClassLoader.this.protectionDomain);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PackageLookupStrategy.class */
    public interface PackageLookupStrategy {
        Package apply(ByteArrayClassLoader byteArrayClassLoader, String str);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PackageLookupStrategy$CreationAction.class */
        public enum CreationAction implements PrivilegedAction<PackageLookupStrategy> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
            public PackageLookupStrategy run() {
                if (JavaModule.isSupported()) {
                    try {
                        return new ForJava9CapableVm(ClassLoader.class.getMethod("getDefinedPackage", String.class));
                    } catch (Exception e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
                return ForLegacyVm.INSTANCE;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PackageLookupStrategy$ForLegacyVm.class */
        public enum ForLegacyVm implements PackageLookupStrategy {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PackageLookupStrategy
            public Package apply(ByteArrayClassLoader classLoader, String name) {
                return classLoader.doGetPackage(name);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PackageLookupStrategy$ForJava9CapableVm.class */
        public static class ForJava9CapableVm implements PackageLookupStrategy {
            private final Method getDefinedPackage;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.getDefinedPackage.equals(((ForJava9CapableVm) obj).getDefinedPackage);
            }

            public int hashCode() {
                return (17 * 31) + this.getDefinedPackage.hashCode();
            }

            protected ForJava9CapableVm(Method getDefinedPackage) {
                this.getDefinedPackage = getDefinedPackage;
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PackageLookupStrategy
            public Package apply(ByteArrayClassLoader classLoader, String name) {
                try {
                    return (Package) this.getDefinedPackage.invoke(classLoader, name);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.getDefinedPackage, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.getDefinedPackage, exception2.getCause());
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PersistenceHandler.class */
    public enum PersistenceHandler {
        MANIFEST(true) { // from class: net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler.1
            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler
            protected byte[] lookup(String name, ConcurrentMap<String, byte[]> typeDefinitions) {
                return typeDefinitions.get(name);
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler
            protected URL url(String resourceName, ConcurrentMap<String, byte[]> typeDefinitions) {
                if (!resourceName.endsWith(".class")) {
                    return ByteArrayClassLoader.NO_URL;
                }
                if (resourceName.startsWith("/")) {
                    resourceName = resourceName.substring(1);
                }
                String typeName = resourceName.replace('/', '.').substring(0, resourceName.length() - ".class".length());
                byte[] binaryRepresentation = typeDefinitions.get(typeName);
                if (binaryRepresentation == null) {
                    return ByteArrayClassLoader.NO_URL;
                }
                return (URL) AccessController.doPrivileged(new UrlDefinitionAction(resourceName, binaryRepresentation));
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler
            protected void release(String name, ConcurrentMap<String, byte[]> typeDefinitions) {
            }
        },
        LATENT(false) { // from class: net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler.2
            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler
            protected byte[] lookup(String name, ConcurrentMap<String, byte[]> typeDefinitions) {
                return typeDefinitions.remove(name);
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler
            protected URL url(String resourceName, ConcurrentMap<String, byte[]> typeDefinitions) {
                return ByteArrayClassLoader.NO_URL;
            }

            @Override // net.bytebuddy.dynamic.loading.ByteArrayClassLoader.PersistenceHandler
            protected void release(String name, ConcurrentMap<String, byte[]> typeDefinitions) {
                typeDefinitions.remove(name);
            }
        };
        
        private static final String CLASS_FILE_SUFFIX = ".class";
        private final boolean manifest;

        protected abstract byte[] lookup(String str, ConcurrentMap<String, byte[]> concurrentMap);

        protected abstract URL url(String str, ConcurrentMap<String, byte[]> concurrentMap);

        protected abstract void release(String str, ConcurrentMap<String, byte[]> concurrentMap);

        PersistenceHandler(boolean manifest) {
            this.manifest = manifest;
        }

        public boolean isManifest() {
            return this.manifest;
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PersistenceHandler$UrlDefinitionAction.class */
        protected static class UrlDefinitionAction implements PrivilegedAction<URL> {
            private static final String ENCODING = "UTF-8";
            private static final int NO_PORT = -1;
            private static final String NO_FILE = "";
            private final String typeName;
            private final byte[] binaryRepresentation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typeName.equals(((UrlDefinitionAction) obj).typeName) && Arrays.equals(this.binaryRepresentation, ((UrlDefinitionAction) obj).binaryRepresentation);
            }

            public int hashCode() {
                return (((17 * 31) + this.typeName.hashCode()) * 31) + Arrays.hashCode(this.binaryRepresentation);
            }

            protected UrlDefinitionAction(String typeName, byte[] binaryRepresentation) {
                this.typeName = typeName;
                this.binaryRepresentation = binaryRepresentation;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public URL run() {
                try {
                    return new URL(ByteArrayClassLoader.URL_SCHEMA, URLEncoder.encode(this.typeName.replace('.', '/'), "UTF-8"), -1, "", new ByteArrayUrlStreamHandler(this.binaryRepresentation));
                } catch (UnsupportedEncodingException exception) {
                    throw new IllegalStateException("Could not find encoding: UTF-8", exception);
                } catch (MalformedURLException exception2) {
                    throw new IllegalStateException("Cannot create URL for " + this.typeName, exception2);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PersistenceHandler$UrlDefinitionAction$ByteArrayUrlStreamHandler.class */
            public static class ByteArrayUrlStreamHandler extends URLStreamHandler {
                private final byte[] binaryRepresentation;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && Arrays.equals(this.binaryRepresentation, ((ByteArrayUrlStreamHandler) obj).binaryRepresentation);
                }

                public int hashCode() {
                    return (17 * 31) + Arrays.hashCode(this.binaryRepresentation);
                }

                protected ByteArrayUrlStreamHandler(byte[] binaryRepresentation) {
                    this.binaryRepresentation = binaryRepresentation;
                }

                @Override // java.net.URLStreamHandler
                protected URLConnection openConnection(URL url) {
                    return new ByteArrayUrlConnection(url, new ByteArrayInputStream(this.binaryRepresentation));
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$PersistenceHandler$UrlDefinitionAction$ByteArrayUrlStreamHandler$ByteArrayUrlConnection.class */
                protected static class ByteArrayUrlConnection extends URLConnection {
                    private final InputStream inputStream;

                    protected ByteArrayUrlConnection(URL url, InputStream inputStream) {
                        super(url);
                        this.inputStream = inputStream;
                    }

                    @Override // java.net.URLConnection
                    public void connect() {
                        this.connected = true;
                    }

                    @Override // java.net.URLConnection
                    public InputStream getInputStream() {
                        connect();
                        return this.inputStream;
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$ChildFirst.class */
    public static class ChildFirst extends ByteArrayClassLoader {
        private static final String CLASS_FILE_SUFFIX = ".class";

        public ChildFirst(ClassLoader parent, Map<String, byte[]> typeDefinitions) {
            super(parent, typeDefinitions);
        }

        public ChildFirst(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions) {
            super(parent, sealed, typeDefinitions);
        }

        public ChildFirst(ClassLoader parent, Map<String, byte[]> typeDefinitions, PersistenceHandler persistenceHandler) {
            super(parent, typeDefinitions, persistenceHandler);
        }

        public ChildFirst(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions, PersistenceHandler persistenceHandler) {
            super(parent, sealed, typeDefinitions, persistenceHandler);
        }

        public ChildFirst(ClassLoader parent, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy) {
            super(parent, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy);
        }

        public ChildFirst(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy) {
            super(parent, sealed, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy);
        }

        public ChildFirst(ClassLoader parent, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy, ClassFileTransformer classFileTransformer) {
            super(parent, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy, classFileTransformer);
        }

        public ChildFirst(ClassLoader parent, boolean sealed, Map<String, byte[]> typeDefinitions, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy, ClassFileTransformer classFileTransformer) {
            super(parent, sealed, typeDefinitions, protectionDomain, persistenceHandler, packageDefinitionStrategy, classFileTransformer);
        }

        public static Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
            return load(classLoader, types, ClassLoadingStrategy.NO_PROTECTION_DOMAIN, PersistenceHandler.LATENT, PackageDefinitionStrategy.Trivial.INSTANCE, false, true);
        }

        @SuppressFBWarnings(value = {"DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit user responsibility")
        public static Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types, ProtectionDomain protectionDomain, PersistenceHandler persistenceHandler, PackageDefinitionStrategy packageDefinitionStrategy, boolean forbidExisting, boolean sealed) {
            Map<String, byte[]> typesByName = new HashMap<>();
            for (Map.Entry<TypeDescription, byte[]> entry : types.entrySet()) {
                typesByName.put(entry.getKey().getName(), entry.getValue());
            }
            ClassLoader classLoader2 = new ChildFirst(classLoader, sealed, typesByName, protectionDomain, persistenceHandler, packageDefinitionStrategy, NoOpClassFileTransformer.INSTANCE);
            Map<TypeDescription, Class<?>> result = new LinkedHashMap<>();
            for (TypeDescription typeDescription : types.keySet()) {
                try {
                    Class<?> type = Class.forName(typeDescription.getName(), false, classLoader2);
                    if (forbidExisting && type.getClassLoader() != classLoader2) {
                        throw new IllegalStateException("Class already loaded: " + type);
                    }
                    result.put(typeDescription, type);
                } catch (ClassNotFoundException exception) {
                    throw new IllegalStateException("Cannot load class " + typeDescription, exception);
                }
            }
            return result;
        }

        @Override // java.lang.ClassLoader
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            synchronized (SYNCHRONIZATION_STRATEGY.initialize().getClassLoadingLock(this, name)) {
                Class<?> type = findLoadedClass(name);
                if (type != null) {
                    return type;
                }
                try {
                    Class<?> type2 = findClass(name);
                    if (resolve) {
                        resolveClass(type2);
                    }
                    return type2;
                } catch (ClassNotFoundException e) {
                    return super.loadClass(name, resolve);
                }
            }
        }

        @Override // java.lang.ClassLoader
        public URL getResource(String name) {
            URL url = this.persistenceHandler.url(name, this.typeDefinitions);
            return (url != null || isShadowed(name)) ? url : super.getResource(name);
        }

        @Override // java.lang.ClassLoader
        public Enumeration<URL> getResources(String name) throws IOException {
            URL url = this.persistenceHandler.url(name, this.typeDefinitions);
            if (url == null) {
                return super.getResources(name);
            }
            return new PrependingEnumeration(url, super.getResources(name));
        }

        private boolean isShadowed(String resourceName) {
            if (this.persistenceHandler.isManifest() || !resourceName.endsWith(".class")) {
                return false;
            }
            synchronized (this) {
                String typeName = resourceName.replace('/', '.').substring(0, resourceName.length() - ".class".length());
                if (this.typeDefinitions.containsKey(typeName)) {
                    return true;
                }
                Class<?> loadedClass = findLoadedClass(typeName);
                return loadedClass != null && loadedClass.getClassLoader() == this;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$ChildFirst$PrependingEnumeration.class */
        protected static class PrependingEnumeration implements Enumeration<URL> {
            private URL nextElement;
            private final Enumeration<URL> enumeration;

            protected PrependingEnumeration(URL url, Enumeration<URL> enumeration) {
                this.nextElement = url;
                this.enumeration = enumeration;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.nextElement != null && this.enumeration.hasMoreElements();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public URL nextElement() {
                if (this.nextElement != null && this.enumeration.hasMoreElements()) {
                    try {
                        return this.nextElement;
                    } finally {
                        this.nextElement = this.enumeration.nextElement();
                    }
                }
                throw new NoSuchElementException();
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$EmptyEnumeration.class */
    protected enum EmptyEnumeration implements Enumeration<URL> {
        INSTANCE;

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public URL nextElement() {
            throw new NoSuchElementException();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ByteArrayClassLoader$SingletonEnumeration.class */
    protected static class SingletonEnumeration implements Enumeration<URL> {
        private URL element;

        protected SingletonEnumeration(URL element) {
            this.element = element;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.element != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public URL nextElement() {
            if (this.element == null) {
                throw new NoSuchElementException();
            }
            try {
                return this.element;
            } finally {
                this.element = null;
            }
        }
    }
}
