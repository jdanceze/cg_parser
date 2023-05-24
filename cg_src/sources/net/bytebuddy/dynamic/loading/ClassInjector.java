package net.bytebuddy.dynamic.loading;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.net.URL;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.MemberRemoval;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.RandomString;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector.class */
public interface ClassInjector {
    public static final Permission SUPPRESS_ACCESS_CHECKS = new ReflectPermission("suppressAccessChecks");
    public static final boolean ALLOW_EXISTING_TYPES = false;

    boolean isAlive();

    Map<TypeDescription, Class<?>> inject(Map<? extends TypeDescription, byte[]> map);

    Map<String, Class<?>> injectRaw(Map<? extends String, byte[]> map);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$AbstractBase.class */
    public static abstract class AbstractBase implements ClassInjector {
        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public Map<TypeDescription, Class<?>> inject(Map<? extends TypeDescription, byte[]> types) {
            Map<String, byte[]> binaryRepresentations = new LinkedHashMap<>();
            for (Map.Entry<? extends TypeDescription, byte[]> entry : types.entrySet()) {
                binaryRepresentations.put(entry.getKey().getName(), entry.getValue());
            }
            Map<String, Class<?>> loadedTypes = injectRaw(binaryRepresentations);
            Map<TypeDescription, Class<?>> result = new LinkedHashMap<>();
            for (TypeDescription typeDescription : types.keySet()) {
                result.put(typeDescription, loadedTypes.get(typeDescription.getName()));
            }
            return result;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection.class */
    public static class UsingReflection extends AbstractBase {
        private static final Dispatcher.Initializable DISPATCHER = (Dispatcher.Initializable) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final ClassLoader classLoader;
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
        private final ProtectionDomain protectionDomain;
        private final PackageDefinitionStrategy packageDefinitionStrategy;
        private final boolean forbidExisting;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass() && this.forbidExisting == ((UsingReflection) obj).forbidExisting && this.classLoader.equals(((UsingReflection) obj).classLoader)) {
                ProtectionDomain protectionDomain = this.protectionDomain;
                ProtectionDomain protectionDomain2 = ((UsingReflection) obj).protectionDomain;
                if (protectionDomain2 != null) {
                    if (protectionDomain == null || !protectionDomain.equals(protectionDomain2)) {
                        return false;
                    }
                } else if (protectionDomain != null) {
                    return false;
                }
                return this.packageDefinitionStrategy.equals(((UsingReflection) obj).packageDefinitionStrategy);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = ((17 * 31) + this.classLoader.hashCode()) * 31;
            ProtectionDomain protectionDomain = this.protectionDomain;
            if (protectionDomain != null) {
                hashCode += protectionDomain.hashCode();
            }
            return (((hashCode * 31) + this.packageDefinitionStrategy.hashCode()) * 31) + (this.forbidExisting ? 1 : 0);
        }

        public UsingReflection(ClassLoader classLoader) {
            this(classLoader, ClassLoadingStrategy.NO_PROTECTION_DOMAIN);
        }

        public UsingReflection(ClassLoader classLoader, ProtectionDomain protectionDomain) {
            this(classLoader, protectionDomain, PackageDefinitionStrategy.Trivial.INSTANCE, false);
        }

        public UsingReflection(ClassLoader classLoader, ProtectionDomain protectionDomain, PackageDefinitionStrategy packageDefinitionStrategy, boolean forbidExisting) {
            if (classLoader == null) {
                throw new IllegalArgumentException("Cannot inject classes into the bootstrap class loader");
            }
            this.classLoader = classLoader;
            this.protectionDomain = protectionDomain;
            this.packageDefinitionStrategy = packageDefinitionStrategy;
            this.forbidExisting = forbidExisting;
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public boolean isAlive() {
            return isAvailable();
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public Map<String, Class<?>> injectRaw(Map<? extends String, byte[]> types) {
            Dispatcher dispatcher = DISPATCHER.initialize();
            Map<String, Class<?>> result = new HashMap<>();
            for (Map.Entry<? extends String, byte[]> entry : types.entrySet()) {
                synchronized (dispatcher.getClassLoadingLock(this.classLoader, entry.getKey())) {
                    Class<?> type = dispatcher.findClass(this.classLoader, entry.getKey());
                    if (type == null) {
                        int packageIndex = entry.getKey().lastIndexOf(46);
                        if (packageIndex != -1) {
                            String packageName = entry.getKey().substring(0, packageIndex);
                            PackageDefinitionStrategy.Definition definition = this.packageDefinitionStrategy.define(this.classLoader, packageName, entry.getKey());
                            if (definition.isDefined()) {
                                Package definedPackage = dispatcher.getPackage(this.classLoader, packageName);
                                if (definedPackage == null) {
                                    dispatcher.definePackage(this.classLoader, packageName, definition.getSpecificationTitle(), definition.getSpecificationVersion(), definition.getSpecificationVendor(), definition.getImplementationTitle(), definition.getImplementationVersion(), definition.getImplementationVendor(), definition.getSealBase());
                                } else if (!definition.isCompatibleTo(definedPackage)) {
                                    throw new SecurityException("Sealing violation for package " + packageName);
                                }
                            }
                        }
                        type = dispatcher.defineClass(this.classLoader, entry.getKey(), entry.getValue(), this.protectionDomain);
                    } else if (this.forbidExisting) {
                        throw new IllegalStateException("Cannot inject already loaded type: " + type);
                    }
                    result.put(entry.getKey(), type);
                }
            }
            return result;
        }

        public static boolean isAvailable() {
            return DISPATCHER.isAvailable();
        }

        public static ClassInjector ofSystemClassLoader() {
            return new UsingReflection(ClassLoader.getSystemClassLoader());
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher.class */
        protected interface Dispatcher {
            public static final Class<?> UNDEFINED = null;

            Object getClassLoadingLock(ClassLoader classLoader, String str);

            Class<?> findClass(ClassLoader classLoader, String str);

            Class<?> defineClass(ClassLoader classLoader, String str, byte[] bArr, ProtectionDomain protectionDomain);

            Package getPackage(ClassLoader classLoader, String str);

            Package definePackage(ClassLoader classLoader, String str, String str2, String str3, String str4, String str5, String str6, String str7, URL url);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$Initializable.class */
            public interface Initializable {
                boolean isAvailable();

                Dispatcher initialize();

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$Initializable$Unavailable.class */
                public static class Unavailable implements Dispatcher, Initializable {
                    private final String message;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.message.equals(((Unavailable) obj).message);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.message.hashCode();
                    }

                    protected Unavailable(String message) {
                        this.message = message;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                    public boolean isAvailable() {
                        return false;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                    public Dispatcher initialize() {
                        return this;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                        return classLoader;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Class<?> findClass(ClassLoader classLoader, String name) {
                        try {
                            return classLoader.loadClass(name);
                        } catch (ClassNotFoundException e) {
                            return UNDEFINED;
                        }
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                        throw new UnsupportedOperationException("Cannot define class using reflection: " + this.message);
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Package getPackage(ClassLoader classLoader, String name) {
                        throw new UnsupportedOperationException("Cannot get package using reflection: " + this.message);
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Package definePackage(ClassLoader classLoader, String name, String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, URL sealBase) {
                        throw new UnsupportedOperationException("Cannot define package using injection: " + this.message);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Initializable> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Initializable run() {
                    try {
                        if (JavaModule.isSupported()) {
                            if (UsingUnsafe.isAvailable()) {
                                return UsingUnsafeInjection.make();
                            }
                            return UsingUnsafeOverride.make();
                        }
                        return Direct.make();
                    } catch (InvocationTargetException exception) {
                        return new Initializable.Unavailable(exception.getCause().getMessage());
                    } catch (Exception exception2) {
                        return new Initializable.Unavailable(exception2.getMessage());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$Direct.class */
            public static abstract class Direct implements Dispatcher, Initializable {
                protected final Method findLoadedClass;
                protected final Method defineClass;
                protected final Method getPackage;
                protected final Method definePackage;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.findLoadedClass.equals(((Direct) obj).findLoadedClass) && this.defineClass.equals(((Direct) obj).defineClass) && this.getPackage.equals(((Direct) obj).getPackage) && this.definePackage.equals(((Direct) obj).definePackage);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.findLoadedClass.hashCode()) * 31) + this.defineClass.hashCode()) * 31) + this.getPackage.hashCode()) * 31) + this.definePackage.hashCode();
                }

                protected Direct(Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage) {
                    this.findLoadedClass = findLoadedClass;
                    this.defineClass = defineClass;
                    this.getPackage = getPackage;
                    this.definePackage = definePackage;
                }

                @SuppressFBWarnings(value = {"DP_DO_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit caller responsibility")
                protected static Initializable make() throws Exception {
                    Method getPackage;
                    if (JavaModule.isSupported()) {
                        try {
                            getPackage = ClassLoader.class.getMethod("getDefinedPackage", String.class);
                        } catch (NoSuchMethodException e) {
                            getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                            getPackage.setAccessible(true);
                        }
                    } else {
                        getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                        getPackage.setAccessible(true);
                    }
                    Method findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
                    findLoadedClass.setAccessible(true);
                    Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                    defineClass.setAccessible(true);
                    Method definePackage = ClassLoader.class.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class);
                    definePackage.setAccessible(true);
                    try {
                        Method getClassLoadingLock = ClassLoader.class.getDeclaredMethod("getClassLoadingLock", String.class);
                        getClassLoadingLock.setAccessible(true);
                        return new ForJava7CapableVm(findLoadedClass, defineClass, getPackage, definePackage, getClassLoadingLock);
                    } catch (NoSuchMethodException e2) {
                        return new ForLegacyVm(findLoadedClass, defineClass, getPackage, definePackage);
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                public boolean isAvailable() {
                    return true;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                public Dispatcher initialize() {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(ClassInjector.SUPPRESS_ACCESS_CHECKS);
                        } catch (Exception exception) {
                            return new Unavailable(exception.getMessage());
                        }
                    }
                    return this;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> findClass(ClassLoader classLoader, String name) {
                    try {
                        return (Class) this.findLoadedClass.invoke(classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#findClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#findClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                    try {
                        return (Class) this.defineClass.invoke(classLoader, name, binaryRepresentation, 0, Integer.valueOf(binaryRepresentation.length), protectionDomain);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#defineClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#defineClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package getPackage(ClassLoader classLoader, String name) {
                    try {
                        return (Package) this.getPackage.invoke(classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#getPackage", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#getPackage", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package definePackage(ClassLoader classLoader, String name, String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, URL sealBase) {
                    try {
                        return (Package) this.definePackage.invoke(classLoader, name, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#definePackage", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#definePackage", exception2.getCause());
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$Direct$ForJava7CapableVm.class */
                public static class ForJava7CapableVm extends Direct {
                    private final Method getClassLoadingLock;

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Direct
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.getClassLoadingLock.equals(((ForJava7CapableVm) obj).getClassLoadingLock);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Direct
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.getClassLoadingLock.hashCode();
                    }

                    protected ForJava7CapableVm(Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage, Method getClassLoadingLock) {
                        super(findLoadedClass, defineClass, getPackage, definePackage);
                        this.getClassLoadingLock = getClassLoadingLock;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                        try {
                            return this.getClassLoadingLock.invoke(classLoader, name);
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Could not access java.lang.ClassLoader#getClassLoadingLock", exception);
                        } catch (InvocationTargetException exception2) {
                            throw new IllegalStateException("Error invoking java.lang.ClassLoader#getClassLoadingLock", exception2.getCause());
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$Direct$ForLegacyVm.class */
                public static class ForLegacyVm extends Direct {
                    protected ForLegacyVm(Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage) {
                        super(findLoadedClass, defineClass, getPackage, definePackage);
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                        return classLoader;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$UsingUnsafeInjection.class */
            public static class UsingUnsafeInjection implements Dispatcher, Initializable {
                private final Object accessor;
                private final Method findLoadedClass;
                private final Method defineClass;
                private final Method getPackage;
                private final Method definePackage;
                private final Method getClassLoadingLock;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.accessor.equals(((UsingUnsafeInjection) obj).accessor) && this.findLoadedClass.equals(((UsingUnsafeInjection) obj).findLoadedClass) && this.defineClass.equals(((UsingUnsafeInjection) obj).defineClass) && this.getPackage.equals(((UsingUnsafeInjection) obj).getPackage) && this.definePackage.equals(((UsingUnsafeInjection) obj).definePackage) && this.getClassLoadingLock.equals(((UsingUnsafeInjection) obj).getClassLoadingLock);
                }

                public int hashCode() {
                    return (((((((((((17 * 31) + this.accessor.hashCode()) * 31) + this.findLoadedClass.hashCode()) * 31) + this.defineClass.hashCode()) * 31) + this.getPackage.hashCode()) * 31) + this.definePackage.hashCode()) * 31) + this.getClassLoadingLock.hashCode();
                }

                protected UsingUnsafeInjection(Object accessor, Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage, Method getClassLoadingLock) {
                    this.accessor = accessor;
                    this.findLoadedClass = findLoadedClass;
                    this.defineClass = defineClass;
                    this.getPackage = getPackage;
                    this.definePackage = definePackage;
                    this.getClassLoadingLock = getClassLoadingLock;
                }

                @SuppressFBWarnings(value = {"DP_DO_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit caller responsibility")
                protected static Initializable make() throws Exception {
                    Method getPackage;
                    if (Boolean.getBoolean(UsingUnsafe.SAFE_PROPERTY)) {
                        return new Initializable.Unavailable("Use of Unsafe was disabled by system property");
                    }
                    Class<?> unsafe = Class.forName("sun.misc.Unsafe");
                    Field theUnsafe = unsafe.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    Object unsafeInstance = theUnsafe.get(null);
                    if (JavaModule.isSupported()) {
                        try {
                            getPackage = ClassLoader.class.getDeclaredMethod("getDefinedPackage", String.class);
                        } catch (NoSuchMethodException e) {
                            getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                        }
                    } else {
                        getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                    }
                    getPackage.setAccessible(true);
                    DynamicType.Builder<?> builder = new ByteBuddy().with(TypeValidation.DISABLED).subclass(Object.class, (ConstructorStrategy) ConstructorStrategy.Default.NO_CONSTRUCTORS).name(ClassLoader.class.getName() + "$ByteBuddyAccessor$" + RandomString.make()).defineMethod("findLoadedClass", Class.class, Visibility.PUBLIC).withParameters(ClassLoader.class, String.class).intercept(MethodCall.invoke(ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class)).onArgument(0).withArgument(1)).defineMethod("defineClass", Class.class, Visibility.PUBLIC).withParameters(ClassLoader.class, String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class).intercept(MethodCall.invoke(ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class)).onArgument(0).withArgument(1, 2, 3, 4, 5)).defineMethod("getPackage", Package.class, Visibility.PUBLIC).withParameters(ClassLoader.class, String.class).intercept(MethodCall.invoke(getPackage).onArgument(0).withArgument(1)).defineMethod("definePackage", Package.class, Visibility.PUBLIC).withParameters(ClassLoader.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class).intercept(MethodCall.invoke(ClassLoader.class.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class)).onArgument(0).withArgument(1, 2, 3, 4, 5, 6, 7, 8));
                    try {
                        builder = builder.defineMethod("getClassLoadingLock", Object.class, Visibility.PUBLIC).withParameters(ClassLoader.class, String.class).intercept(MethodCall.invoke(ClassLoader.class.getDeclaredMethod("getClassLoadingLock", String.class)).onArgument(0).withArgument(1));
                    } catch (NoSuchMethodException e2) {
                        builder = builder.defineMethod("getClassLoadingLock", Object.class, Visibility.PUBLIC).withParameters(ClassLoader.class, String.class).intercept(FixedValue.argument(0));
                    }
                    Class<?> type = builder.make().load(ClassLoadingStrategy.BOOTSTRAP_LOADER, new ClassLoadingStrategy.ForUnsafeInjection()).getLoaded();
                    return new UsingUnsafeInjection(unsafe.getMethod("allocateInstance", Class.class).invoke(unsafeInstance, type), type.getMethod("findLoadedClass", ClassLoader.class, String.class), type.getMethod("defineClass", ClassLoader.class, String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class), type.getMethod("getPackage", ClassLoader.class, String.class), type.getMethod("definePackage", ClassLoader.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class), type.getMethod("getClassLoadingLock", ClassLoader.class, String.class));
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                public boolean isAvailable() {
                    return true;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                public Dispatcher initialize() {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(ClassInjector.SUPPRESS_ACCESS_CHECKS);
                        } catch (Exception exception) {
                            return new Unavailable(exception.getMessage());
                        }
                    }
                    return this;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                    try {
                        return this.getClassLoadingLock.invoke(this.accessor, classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access (accessor)::getClassLoadingLock", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking (accessor)::getClassLoadingLock", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> findClass(ClassLoader classLoader, String name) {
                    try {
                        return (Class) this.findLoadedClass.invoke(this.accessor, classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access (accessor)::findLoadedClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking (accessor)::findLoadedClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                    try {
                        return (Class) this.defineClass.invoke(this.accessor, classLoader, name, binaryRepresentation, 0, Integer.valueOf(binaryRepresentation.length), protectionDomain);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access (accessor)::defineClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking (accessor)::defineClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package getPackage(ClassLoader classLoader, String name) {
                    try {
                        return (Package) this.getPackage.invoke(this.accessor, classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access (accessor)::getPackage", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking (accessor)::getPackage", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package definePackage(ClassLoader classLoader, String name, String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, URL sealBase) {
                    try {
                        return (Package) this.definePackage.invoke(this.accessor, classLoader, name, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access (accessor)::definePackage", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking (accessor)::definePackage", exception2.getCause());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$UsingUnsafeOverride.class */
            public static abstract class UsingUnsafeOverride implements Dispatcher, Initializable {
                protected final Method findLoadedClass;
                protected final Method defineClass;
                protected final Method getPackage;
                protected final Method definePackage;

                protected UsingUnsafeOverride(Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage) {
                    this.findLoadedClass = findLoadedClass;
                    this.defineClass = defineClass;
                    this.getPackage = getPackage;
                    this.definePackage = definePackage;
                }

                @SuppressFBWarnings(value = {"DP_DO_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit caller responsibility")
                protected static Initializable make() throws Exception {
                    Field override;
                    Method getPackage;
                    if (Boolean.getBoolean(UsingUnsafe.SAFE_PROPERTY)) {
                        return new Initializable.Unavailable("Use of Unsafe was disabled by system property");
                    }
                    Class<?> unsafeType = Class.forName("sun.misc.Unsafe");
                    Field theUnsafe = unsafeType.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    Object unsafe = theUnsafe.get(null);
                    try {
                        override = AccessibleObject.class.getDeclaredField("override");
                    } catch (NoSuchFieldException e) {
                        override = new ByteBuddy().redefine(AccessibleObject.class).name("net.bytebuddy.mirror." + AccessibleObject.class.getSimpleName()).noNestMate().visit(new MemberRemoval().stripInvokables(ElementMatchers.any())).make().load(AccessibleObject.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded().getDeclaredField("override");
                    }
                    long offset = ((Long) unsafeType.getMethod("objectFieldOffset", Field.class).invoke(unsafe, override)).longValue();
                    Method putBoolean = unsafeType.getMethod("putBoolean", Object.class, Long.TYPE, Boolean.TYPE);
                    if (JavaModule.isSupported()) {
                        try {
                            getPackage = ClassLoader.class.getMethod("getDefinedPackage", String.class);
                        } catch (NoSuchMethodException e2) {
                            getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                            putBoolean.invoke(unsafe, getPackage, Long.valueOf(offset), true);
                        }
                    } else {
                        getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                        putBoolean.invoke(unsafe, getPackage, Long.valueOf(offset), true);
                    }
                    Method findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
                    Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                    Method definePackage = ClassLoader.class.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class);
                    putBoolean.invoke(unsafe, defineClass, Long.valueOf(offset), true);
                    putBoolean.invoke(unsafe, findLoadedClass, Long.valueOf(offset), true);
                    putBoolean.invoke(unsafe, definePackage, Long.valueOf(offset), true);
                    try {
                        Method getClassLoadingLock = ClassLoader.class.getDeclaredMethod("getClassLoadingLock", String.class);
                        putBoolean.invoke(unsafe, getClassLoadingLock, Long.valueOf(offset), true);
                        return new ForJava7CapableVm(findLoadedClass, defineClass, getPackage, definePackage, getClassLoadingLock);
                    } catch (NoSuchMethodException e3) {
                        return new ForLegacyVm(findLoadedClass, defineClass, getPackage, definePackage);
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                public boolean isAvailable() {
                    return true;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher.Initializable
                public Dispatcher initialize() {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(ClassInjector.SUPPRESS_ACCESS_CHECKS);
                        } catch (Exception exception) {
                            return new Unavailable(exception.getMessage());
                        }
                    }
                    return this;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> findClass(ClassLoader classLoader, String name) {
                    try {
                        return (Class) this.findLoadedClass.invoke(classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#findClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#findClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                    try {
                        return (Class) this.defineClass.invoke(classLoader, name, binaryRepresentation, 0, Integer.valueOf(binaryRepresentation.length), protectionDomain);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#defineClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#defineClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package getPackage(ClassLoader classLoader, String name) {
                    try {
                        return (Package) this.getPackage.invoke(classLoader, name);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#getPackage", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#getPackage", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package definePackage(ClassLoader classLoader, String name, String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, URL sealBase) {
                    try {
                        return (Package) this.definePackage.invoke(classLoader, name, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access java.lang.ClassLoader#definePackage", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.ClassLoader#definePackage", exception2.getCause());
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$UsingUnsafeOverride$ForJava7CapableVm.class */
                public static class ForJava7CapableVm extends UsingUnsafeOverride {
                    private final Method getClassLoadingLock;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.getClassLoadingLock.equals(((ForJava7CapableVm) obj).getClassLoadingLock);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.getClassLoadingLock.hashCode();
                    }

                    protected ForJava7CapableVm(Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage, Method getClassLoadingLock) {
                        super(findLoadedClass, defineClass, getPackage, definePackage);
                        this.getClassLoadingLock = getClassLoadingLock;
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                        try {
                            return this.getClassLoadingLock.invoke(classLoader, name);
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Could not access java.lang.ClassLoader#getClassLoadingLock", exception);
                        } catch (InvocationTargetException exception2) {
                            throw new IllegalStateException("Error invoking java.lang.ClassLoader#getClassLoadingLock", exception2.getCause());
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$UsingUnsafeOverride$ForLegacyVm.class */
                public static class ForLegacyVm extends UsingUnsafeOverride {
                    protected ForLegacyVm(Method findLoadedClass, Method defineClass, Method getPackage, Method definePackage) {
                        super(findLoadedClass, defineClass, getPackage, definePackage);
                    }

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                    public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                        return classLoader;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingReflection$Dispatcher$Unavailable.class */
            public static class Unavailable implements Dispatcher {
                private final String message;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.message.equals(((Unavailable) obj).message);
                }

                public int hashCode() {
                    return (17 * 31) + this.message.hashCode();
                }

                protected Unavailable(String message) {
                    this.message = message;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Object getClassLoadingLock(ClassLoader classLoader, String name) {
                    return classLoader;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> findClass(ClassLoader classLoader, String name) {
                    try {
                        return classLoader.loadClass(name);
                    } catch (ClassNotFoundException e) {
                        return UNDEFINED;
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                    throw new UnsupportedOperationException("Cannot define class using reflection: " + this.message);
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package getPackage(ClassLoader classLoader, String name) {
                    throw new UnsupportedOperationException("Cannot get package using reflection: " + this.message);
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection.Dispatcher
                public Package definePackage(ClassLoader classLoader, String name, String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, URL sealBase) {
                    throw new UnsupportedOperationException("Cannot define package using injection: " + this.message);
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingLookup.class */
    public static class UsingLookup extends AbstractBase {
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.Creator.INSTANCE);
        private static final int PACKAGE_LOOKUP = 8;
        private final Object lookup;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.lookup.equals(((UsingLookup) obj).lookup);
        }

        public int hashCode() {
            return (17 * 31) + this.lookup.hashCode();
        }

        protected UsingLookup(Object lookup) {
            this.lookup = lookup;
        }

        public static UsingLookup of(Object lookup) {
            if (!DISPATCHER.isAlive()) {
                throw new IllegalStateException("The current VM does not support class definition via method handle lookups");
            }
            if (!JavaType.METHOD_HANDLES_LOOKUP.isInstance(lookup)) {
                throw new IllegalArgumentException("Not a method handle lookup: " + lookup);
            }
            if ((DISPATCHER.lookupModes(lookup) & 8) == 0) {
                throw new IllegalArgumentException("Lookup does not imply package-access: " + lookup);
            }
            return new UsingLookup(lookup);
        }

        public Class<?> lookupType() {
            return DISPATCHER.lookupType(this.lookup);
        }

        public UsingLookup in(Class<?> type) {
            return new UsingLookup(DISPATCHER.resolve(this.lookup, type));
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public boolean isAlive() {
            return isAvailable();
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public Map<String, Class<?>> injectRaw(Map<? extends String, byte[]> types) {
            String expectedPackage = TypeDescription.ForLoadedType.of(lookupType()).getPackage().getName();
            Map<String, Class<?>> result = new HashMap<>();
            for (Map.Entry<? extends String, byte[]> entry : types.entrySet()) {
                int index = entry.getKey().lastIndexOf(46);
                if (!expectedPackage.equals(index == -1 ? "" : entry.getKey().substring(0, index))) {
                    throw new IllegalArgumentException(entry.getKey() + " must be defined in the same package as " + this.lookup);
                }
                result.put(entry.getKey(), DISPATCHER.defineClass(this.lookup, entry.getValue()));
            }
            return result;
        }

        public static boolean isAvailable() {
            return DISPATCHER.isAlive();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingLookup$Dispatcher.class */
        public interface Dispatcher {
            boolean isAlive();

            Class<?> lookupType(Object obj);

            int lookupModes(Object obj);

            Object resolve(Object obj, Class<?> cls);

            Class<?> defineClass(Object obj, byte[] bArr);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingLookup$Dispatcher$Creator.class */
            public enum Creator implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Dispatcher run() {
                    try {
                        Class<?> lookup = JavaType.METHOD_HANDLES_LOOKUP.load();
                        return new ForJava9CapableVm(JavaType.METHOD_HANDLES.load().getMethod("privateLookupIn", Class.class, lookup), lookup.getMethod("lookupClass", new Class[0]), lookup.getMethod("lookupModes", new Class[0]), lookup.getMethod("defineClass", byte[].class));
                    } catch (Exception e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingLookup$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public boolean isAlive() {
                    return false;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public Class<?> lookupType(Object lookup) {
                    throw new IllegalStateException("Cannot dispatch method for java.lang.invoke.MethodHandles$Lookup");
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public int lookupModes(Object lookup) {
                    throw new IllegalStateException("Cannot dispatch method for java.lang.invoke.MethodHandles$Lookup");
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public Object resolve(Object lookup, Class<?> type) {
                    throw new IllegalStateException("Cannot dispatch method for java.lang.invoke.MethodHandles");
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public Class<?> defineClass(Object lookup, byte[] binaryRepresentation) {
                    throw new IllegalStateException("Cannot dispatch method for java.lang.invoke.MethodHandles$Lookup");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingLookup$Dispatcher$ForJava9CapableVm.class */
            public static class ForJava9CapableVm implements Dispatcher {
                private static final Object[] NO_ARGUMENTS = new Object[0];
                private final Method privateLookupIn;
                private final Method lookupClass;
                private final Method lookupModes;
                private final Method defineClass;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.privateLookupIn.equals(((ForJava9CapableVm) obj).privateLookupIn) && this.lookupClass.equals(((ForJava9CapableVm) obj).lookupClass) && this.lookupModes.equals(((ForJava9CapableVm) obj).lookupModes) && this.defineClass.equals(((ForJava9CapableVm) obj).defineClass);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.privateLookupIn.hashCode()) * 31) + this.lookupClass.hashCode()) * 31) + this.lookupModes.hashCode()) * 31) + this.defineClass.hashCode();
                }

                protected ForJava9CapableVm(Method privateLookupIn, Method lookupClass, Method lookupModes, Method defineClass) {
                    this.privateLookupIn = privateLookupIn;
                    this.lookupClass = lookupClass;
                    this.lookupModes = lookupModes;
                    this.defineClass = defineClass;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public boolean isAlive() {
                    return true;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public Class<?> lookupType(Object lookup) {
                    try {
                        return (Class) this.lookupClass.invoke(lookup, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandles$Lookup#lookupClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandles$Lookup#lookupClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public int lookupModes(Object lookup) {
                    try {
                        return ((Integer) this.lookupModes.invoke(lookup, NO_ARGUMENTS)).intValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandles$Lookup#lookupModes", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandles$Lookup#lookupModes", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public Object resolve(Object lookup, Class<?> type) {
                    try {
                        return this.privateLookupIn.invoke(null, type, lookup);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandles#privateLookupIn", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandles#privateLookupIn", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingLookup.Dispatcher
                public Class<?> defineClass(Object lookup, byte[] binaryRepresentation) {
                    try {
                        return (Class) this.defineClass.invoke(lookup, binaryRepresentation);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandles$Lookup#defineClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandles$Lookup#defineClass", exception2.getCause());
                    }
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe.class */
    public static class UsingUnsafe extends AbstractBase {
        public static final String SAFE_PROPERTY = "net.bytebuddy.safe";
        private static final Dispatcher.Initializable DISPATCHER = (Dispatcher.Initializable) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private static final Object BOOTSTRAP_LOADER_LOCK = new Object();
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
        private final ClassLoader classLoader;
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
        private final ProtectionDomain protectionDomain;
        private final Dispatcher.Initializable dispatcher;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ClassLoader classLoader = this.classLoader;
                ClassLoader classLoader2 = ((UsingUnsafe) obj).classLoader;
                if (classLoader2 != null) {
                    if (classLoader == null || !classLoader.equals(classLoader2)) {
                        return false;
                    }
                } else if (classLoader != null) {
                    return false;
                }
                ProtectionDomain protectionDomain = this.protectionDomain;
                ProtectionDomain protectionDomain2 = ((UsingUnsafe) obj).protectionDomain;
                if (protectionDomain2 != null) {
                    if (protectionDomain == null || !protectionDomain.equals(protectionDomain2)) {
                        return false;
                    }
                } else if (protectionDomain != null) {
                    return false;
                }
                return this.dispatcher.equals(((UsingUnsafe) obj).dispatcher);
            }
            return false;
        }

        public int hashCode() {
            int i = 17 * 31;
            ClassLoader classLoader = this.classLoader;
            if (classLoader != null) {
                i += classLoader.hashCode();
            }
            int i2 = i * 31;
            ProtectionDomain protectionDomain = this.protectionDomain;
            if (protectionDomain != null) {
                i2 += protectionDomain.hashCode();
            }
            return (i2 * 31) + this.dispatcher.hashCode();
        }

        public UsingUnsafe(ClassLoader classLoader) {
            this(classLoader, ClassLoadingStrategy.NO_PROTECTION_DOMAIN);
        }

        public UsingUnsafe(ClassLoader classLoader, ProtectionDomain protectionDomain) {
            this(classLoader, protectionDomain, DISPATCHER);
        }

        protected UsingUnsafe(ClassLoader classLoader, ProtectionDomain protectionDomain, Dispatcher.Initializable dispatcher) {
            this.classLoader = classLoader;
            this.protectionDomain = protectionDomain;
            this.dispatcher = dispatcher;
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public boolean isAlive() {
            return this.dispatcher.isAvailable();
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public Map<String, Class<?>> injectRaw(Map<? extends String, byte[]> types) {
            Dispatcher dispatcher = this.dispatcher.initialize();
            Map<String, Class<?>> result = new HashMap<>();
            Object obj = this.classLoader == null ? BOOTSTRAP_LOADER_LOCK : this.classLoader;
            Object obj2 = obj;
            synchronized (obj) {
                for (Map.Entry<? extends String, byte[]> entry : types.entrySet()) {
                    try {
                        result.put(entry.getKey(), Class.forName(entry.getKey(), false, this.classLoader));
                    } catch (ClassNotFoundException e) {
                        result.put(entry.getKey(), dispatcher.defineClass(this.classLoader, entry.getKey(), entry.getValue(), this.protectionDomain));
                    }
                }
                return result;
            }
        }

        public static boolean isAvailable() {
            return DISPATCHER.isAvailable();
        }

        public static ClassInjector ofSystemLoader() {
            return new UsingUnsafe(ClassLoader.getSystemClassLoader());
        }

        public static ClassInjector ofPlatformLoader() {
            return new UsingUnsafe(ClassLoader.getSystemClassLoader().getParent());
        }

        public static ClassInjector ofBootLoader() {
            return new UsingUnsafe(ClassLoadingStrategy.BOOTSTRAP_LOADER);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Dispatcher.class */
        protected interface Dispatcher {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Dispatcher$Initializable.class */
            public interface Initializable {
                boolean isAvailable();

                Dispatcher initialize();
            }

            Class<?> defineClass(ClassLoader classLoader, String str, byte[] bArr, ProtectionDomain protectionDomain);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Initializable> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Initializable run() {
                    Field override;
                    if (Boolean.getBoolean(UsingUnsafe.SAFE_PROPERTY)) {
                        return new Unavailable("Use of Unsafe was disabled by system property");
                    }
                    try {
                        Class<?> unsafeType = Class.forName("sun.misc.Unsafe");
                        Field theUnsafe = unsafeType.getDeclaredField("theUnsafe");
                        theUnsafe.setAccessible(true);
                        Object unsafe = theUnsafe.get(null);
                        try {
                            Method defineClass = unsafeType.getMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class);
                            defineClass.setAccessible(true);
                            return new Enabled(unsafe, defineClass);
                        } catch (Exception exception) {
                            try {
                                try {
                                    override = AccessibleObject.class.getDeclaredField("override");
                                } catch (NoSuchFieldException e) {
                                    override = new ByteBuddy().redefine(AccessibleObject.class).name("net.bytebuddy.mirror." + AccessibleObject.class.getSimpleName()).noNestMate().visit(new MemberRemoval().stripInvokables(ElementMatchers.any())).make().load(AccessibleObject.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded().getDeclaredField("override");
                                }
                                long offset = ((Long) unsafeType.getMethod("objectFieldOffset", Field.class).invoke(unsafe, override)).longValue();
                                Method putBoolean = unsafeType.getMethod("putBoolean", Object.class, Long.TYPE, Boolean.TYPE);
                                Class<?> internalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
                                Field theUnsafeInternal = internalUnsafe.getDeclaredField("theUnsafe");
                                putBoolean.invoke(unsafe, theUnsafeInternal, Long.valueOf(offset), true);
                                Method defineClassInternal = internalUnsafe.getMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class);
                                putBoolean.invoke(unsafe, defineClassInternal, Long.valueOf(offset), true);
                                return new Enabled(theUnsafeInternal.get(null), defineClassInternal);
                            } catch (Exception e2) {
                                throw exception;
                            }
                        }
                    } catch (Exception exception2) {
                        return new Unavailable(exception2.getMessage());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Dispatcher$Enabled.class */
            public static class Enabled implements Dispatcher, Initializable {
                private final Object unsafe;
                private final Method defineClass;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.unsafe.equals(((Enabled) obj).unsafe) && this.defineClass.equals(((Enabled) obj).defineClass);
                }

                public int hashCode() {
                    return (((17 * 31) + this.unsafe.hashCode()) * 31) + this.defineClass.hashCode();
                }

                protected Enabled(Object unsafe, Method defineClass) {
                    this.unsafe = unsafe;
                    this.defineClass = defineClass;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Dispatcher.Initializable
                public boolean isAvailable() {
                    return true;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Dispatcher.Initializable
                public Dispatcher initialize() {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(ClassInjector.SUPPRESS_ACCESS_CHECKS);
                        } catch (Exception exception) {
                            return new Unavailable(exception.getMessage());
                        }
                    }
                    return this;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Dispatcher
                public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                    try {
                        return (Class) this.defineClass.invoke(this.unsafe, name, binaryRepresentation, 0, Integer.valueOf(binaryRepresentation.length), classLoader, protectionDomain);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Unsafe::defineClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking Unsafe::defineClass", exception2.getCause());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Dispatcher$Unavailable.class */
            public static class Unavailable implements Dispatcher, Initializable {
                private final String message;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.message.equals(((Unavailable) obj).message);
                }

                public int hashCode() {
                    return (17 * 31) + this.message.hashCode();
                }

                protected Unavailable(String message) {
                    this.message = message;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Dispatcher.Initializable
                public boolean isAvailable() {
                    return false;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Dispatcher.Initializable
                public Dispatcher initialize() {
                    throw new UnsupportedOperationException("Could not access Unsafe class: " + this.message);
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Dispatcher
                public Class<?> defineClass(ClassLoader classLoader, String name, byte[] binaryRepresentation, ProtectionDomain protectionDomain) {
                    throw new UnsupportedOperationException("Could not access Unsafe class: " + this.message);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Factory.class */
        public static class Factory {
            private final Dispatcher.Initializable dispatcher;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.dispatcher.equals(((Factory) obj).dispatcher);
            }

            public int hashCode() {
                return (17 * 31) + this.dispatcher.hashCode();
            }

            public Factory() {
                this(AccessResolver.Default.INSTANCE);
            }

            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception is captured to trigger lazy error upon use.")
            public Factory(AccessResolver accessResolver) {
                Dispatcher.Initializable dispatcher;
                if (UsingUnsafe.DISPATCHER.isAvailable()) {
                    dispatcher = UsingUnsafe.DISPATCHER;
                } else {
                    try {
                        Class<?> unsafeType = Class.forName("jdk.internal.misc.Unsafe");
                        Field theUnsafe = unsafeType.getDeclaredField("theUnsafe");
                        accessResolver.apply(theUnsafe);
                        Object unsafe = theUnsafe.get(null);
                        Method defineClass = unsafeType.getMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class);
                        accessResolver.apply(defineClass);
                        dispatcher = new Dispatcher.Enabled(unsafe, defineClass);
                    } catch (Exception exception) {
                        dispatcher = new Dispatcher.Unavailable(exception.getMessage());
                    }
                }
                this.dispatcher = dispatcher;
            }

            protected Factory(Dispatcher.Initializable dispatcher) {
                this.dispatcher = dispatcher;
            }

            public static Factory resolve(Instrumentation instrumentation) {
                return resolve(instrumentation, false);
            }

            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception intends to trigger disabled injection strategy.")
            public static Factory resolve(Instrumentation instrumentation, boolean local) {
                if (UsingUnsafe.isAvailable() || !JavaModule.isSupported()) {
                    return new Factory();
                }
                try {
                    Class<?> type = Class.forName("jdk.internal.misc.Unsafe");
                    PackageDescription packageDescription = new PackageDescription.ForLoadedPackage(type.getPackage());
                    JavaModule source = JavaModule.ofType(type);
                    JavaModule target = JavaModule.ofType(UsingUnsafe.class);
                    if (source.isOpened(packageDescription, target)) {
                        return new Factory();
                    }
                    if (local) {
                        JavaModule module = JavaModule.ofType(AccessResolver.Default.class);
                        source.modify(instrumentation, Collections.singleton(module), Collections.emptyMap(), Collections.singletonMap(packageDescription.getName(), Collections.singleton(module)), Collections.emptySet(), Collections.emptyMap());
                        return new Factory();
                    }
                    Class<? extends AccessResolver> resolver = new ByteBuddy().subclass(AccessResolver.class).method(ElementMatchers.named("apply")).intercept(MethodCall.invoke(AccessibleObject.class.getMethod("setAccessible", Boolean.TYPE)).onArgument(0).with(true)).make().load(AccessResolver.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER.with(AccessResolver.class.getProtectionDomain())).getLoaded();
                    JavaModule module2 = JavaModule.ofType(resolver);
                    source.modify(instrumentation, Collections.singleton(module2), Collections.emptyMap(), Collections.singletonMap(packageDescription.getName(), Collections.singleton(module2)), Collections.emptySet(), Collections.emptyMap());
                    return new Factory((AccessResolver) resolver.getConstructor(new Class[0]).newInstance(new Object[0]));
                } catch (Exception exception) {
                    return new Factory(new Dispatcher.Unavailable(exception.getMessage()));
                }
            }

            public boolean isAvailable() {
                return this.dispatcher.isAvailable();
            }

            public ClassInjector make(ClassLoader classLoader) {
                return make(classLoader, ClassLoadingStrategy.NO_PROTECTION_DOMAIN);
            }

            public ClassInjector make(ClassLoader classLoader, ProtectionDomain protectionDomain) {
                return new UsingUnsafe(classLoader, protectionDomain, this.dispatcher);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Factory$AccessResolver.class */
            public interface AccessResolver {
                void apply(AccessibleObject accessibleObject);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingUnsafe$Factory$AccessResolver$Default.class */
                public enum Default implements AccessResolver {
                    INSTANCE;

                    @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingUnsafe.Factory.AccessResolver
                    public void apply(AccessibleObject accessibleObject) {
                        accessibleObject.setAccessible(true);
                    }
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingInstrumentation.class */
    public static class UsingInstrumentation extends AbstractBase {
        private static final String JAR = "jar";
        private static final String CLASS_FILE_EXTENSION = ".class";
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final Instrumentation instrumentation;
        private final Target target;
        private final File folder;
        private final RandomString randomString;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.target.equals(((UsingInstrumentation) obj).target) && this.instrumentation.equals(((UsingInstrumentation) obj).instrumentation) && this.folder.equals(((UsingInstrumentation) obj).folder) && this.randomString.equals(((UsingInstrumentation) obj).randomString);
        }

        public int hashCode() {
            return (((((((17 * 31) + this.instrumentation.hashCode()) * 31) + this.target.hashCode()) * 31) + this.folder.hashCode()) * 31) + this.randomString.hashCode();
        }

        protected UsingInstrumentation(File folder, Target target, Instrumentation instrumentation, RandomString randomString) {
            this.folder = folder;
            this.target = target;
            this.instrumentation = instrumentation;
            this.randomString = randomString;
        }

        public static ClassInjector of(File folder, Target target, Instrumentation instrumentation) {
            return new UsingInstrumentation(folder, target, instrumentation, new RandomString());
        }

        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public boolean isAlive() {
            return isAvailable();
        }

        /* JADX WARN: Finally extract failed */
        @Override // net.bytebuddy.dynamic.loading.ClassInjector
        public Map<String, Class<?>> injectRaw(Map<? extends String, byte[]> types) {
            File file = new File(this.folder, JAR + this.randomString.nextString() + "." + JAR);
            try {
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Cannot create file " + file);
                }
                JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(file));
                try {
                    for (Map.Entry<? extends String, byte[]> entry : types.entrySet()) {
                        jarOutputStream.putNextEntry(new JarEntry(entry.getKey().replace('.', '/') + ".class"));
                        jarOutputStream.write(entry.getValue());
                    }
                    jarOutputStream.close();
                    JarFile jarFile = new JarFile(file, false);
                    try {
                        this.target.inject(this.instrumentation, jarFile);
                        jarFile.close();
                        Map<String, Class<?>> result = new HashMap<>();
                        for (String name : types.keySet()) {
                            result.put(name, Class.forName(name, false, this.target.getClassLoader()));
                        }
                        if (!file.delete()) {
                            file.deleteOnExit();
                        }
                        return result;
                    } catch (Throwable th) {
                        jarFile.close();
                        throw th;
                    }
                } catch (Throwable th2) {
                    jarOutputStream.close();
                    throw th2;
                }
            } catch (IOException exception) {
                throw new IllegalStateException("Cannot write jar file to disk", exception);
            } catch (ClassNotFoundException exception2) {
                throw new IllegalStateException("Cannot load injected class", exception2);
            }
        }

        public static boolean isAvailable() {
            return DISPATCHER.isAlive();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingInstrumentation$Dispatcher.class */
        public interface Dispatcher {
            boolean isAlive();

            void appendToBootstrapClassLoaderSearch(Instrumentation instrumentation, JarFile jarFile);

            void appendToSystemClassLoaderSearch(Instrumentation instrumentation, JarFile jarFile);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingInstrumentation$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    try {
                        Class<?> instrumentation = Class.forName("java.lang.instrument.Instrumentation");
                        return new ForJava6CapableVm(instrumentation.getMethod("appendToBootstrapClassLoaderSearch", JarFile.class), instrumentation.getMethod("appendToSystemClassLoaderSearch", JarFile.class));
                    } catch (ClassNotFoundException e) {
                        return ForLegacyVm.INSTANCE;
                    } catch (NoSuchMethodException e2) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingInstrumentation$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Dispatcher
                public boolean isAlive() {
                    return false;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Dispatcher
                public void appendToBootstrapClassLoaderSearch(Instrumentation instrumentation, JarFile jarFile) {
                    throw new UnsupportedOperationException("The current JVM does not support appending to the bootstrap loader");
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Dispatcher
                public void appendToSystemClassLoaderSearch(Instrumentation instrumentation, JarFile jarFile) {
                    throw new UnsupportedOperationException("The current JVM does not support appending to the system class loader");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingInstrumentation$Dispatcher$ForJava6CapableVm.class */
            public static class ForJava6CapableVm implements Dispatcher {
                private final Method appendToBootstrapClassLoaderSearch;
                private final Method appendToSystemClassLoaderSearch;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.appendToBootstrapClassLoaderSearch.equals(((ForJava6CapableVm) obj).appendToBootstrapClassLoaderSearch) && this.appendToSystemClassLoaderSearch.equals(((ForJava6CapableVm) obj).appendToSystemClassLoaderSearch);
                }

                public int hashCode() {
                    return (((17 * 31) + this.appendToBootstrapClassLoaderSearch.hashCode()) * 31) + this.appendToSystemClassLoaderSearch.hashCode();
                }

                protected ForJava6CapableVm(Method appendToBootstrapClassLoaderSearch, Method appendToSystemClassLoaderSearch) {
                    this.appendToBootstrapClassLoaderSearch = appendToBootstrapClassLoaderSearch;
                    this.appendToSystemClassLoaderSearch = appendToSystemClassLoaderSearch;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Dispatcher
                public boolean isAlive() {
                    return true;
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Dispatcher
                public void appendToBootstrapClassLoaderSearch(Instrumentation instrumentation, JarFile jarFile) {
                    try {
                        this.appendToBootstrapClassLoaderSearch.invoke(instrumentation, jarFile);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#appendToBootstrapClassLoaderSearch", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#appendToBootstrapClassLoaderSearch", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Dispatcher
                public void appendToSystemClassLoaderSearch(Instrumentation instrumentation, JarFile jarFile) {
                    try {
                        this.appendToSystemClassLoaderSearch.invoke(instrumentation, jarFile);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#appendToSystemClassLoaderSearch", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#appendToSystemClassLoaderSearch", exception2.getCause());
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/ClassInjector$UsingInstrumentation$Target.class */
        public enum Target {
            BOOTSTRAP(null) { // from class: net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Target.1
                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Target
                protected void inject(Instrumentation instrumentation, JarFile jarFile) {
                    UsingInstrumentation.DISPATCHER.appendToBootstrapClassLoaderSearch(instrumentation, jarFile);
                }
            },
            SYSTEM(ClassLoader.getSystemClassLoader()) { // from class: net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Target.2
                @Override // net.bytebuddy.dynamic.loading.ClassInjector.UsingInstrumentation.Target
                protected void inject(Instrumentation instrumentation, JarFile jarFile) {
                    UsingInstrumentation.DISPATCHER.appendToSystemClassLoaderSearch(instrumentation, jarFile);
                }
            };
            
            private final ClassLoader classLoader;

            protected abstract void inject(Instrumentation instrumentation, JarFile jarFile);

            Target(ClassLoader classLoader) {
                this.classLoader = classLoader;
            }

            protected ClassLoader getClassLoader() {
                return this.classLoader;
            }
        }
    }
}
