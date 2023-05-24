package net.bytebuddy.dynamic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.StreamDrainer;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator.class */
public interface ClassFileLocator extends Closeable {
    public static final String CLASS_FILE_EXTENSION = ".class";

    Resolution locate(String str) throws IOException;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$Resolution.class */
    public interface Resolution {
        boolean isResolved();

        byte[] resolve();

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$Resolution$Illegal.class */
        public static class Illegal implements Resolution {
            private final String typeName;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typeName.equals(((Illegal) obj).typeName);
            }

            public int hashCode() {
                return (17 * 31) + this.typeName.hashCode();
            }

            public Illegal(String typeName) {
                this.typeName = typeName;
            }

            @Override // net.bytebuddy.dynamic.ClassFileLocator.Resolution
            public boolean isResolved() {
                return false;
            }

            @Override // net.bytebuddy.dynamic.ClassFileLocator.Resolution
            public byte[] resolve() {
                throw new IllegalStateException("Could not locate class file for " + this.typeName);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$Resolution$Explicit.class */
        public static class Explicit implements Resolution {
            private final byte[] binaryRepresentation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && Arrays.equals(this.binaryRepresentation, ((Explicit) obj).binaryRepresentation);
            }

            public int hashCode() {
                return (17 * 31) + Arrays.hashCode(this.binaryRepresentation);
            }

            @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "The array is not to be modified by contract")
            public Explicit(byte[] binaryRepresentation) {
                this.binaryRepresentation = binaryRepresentation;
            }

            @Override // net.bytebuddy.dynamic.ClassFileLocator.Resolution
            public boolean isResolved() {
                return true;
            }

            @Override // net.bytebuddy.dynamic.ClassFileLocator.Resolution
            @SuppressFBWarnings(value = {"EI_EXPOSE_REP"}, justification = "The array is not to be modified by contract")
            public byte[] resolve() {
                return this.binaryRepresentation;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$NoOp.class */
    public enum NoOp implements ClassFileLocator {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) {
            return new Resolution.Illegal(name);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$Simple.class */
    public static class Simple implements ClassFileLocator {
        private final Map<String, byte[]> classFiles;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classFiles.equals(((Simple) obj).classFiles);
        }

        public int hashCode() {
            return (17 * 31) + this.classFiles.hashCode();
        }

        public Simple(Map<String, byte[]> classFiles) {
            this.classFiles = classFiles;
        }

        public static ClassFileLocator of(String typeName, byte[] binaryRepresentation) {
            return new Simple(Collections.singletonMap(typeName, binaryRepresentation));
        }

        public static ClassFileLocator of(DynamicType dynamicType) {
            return of(dynamicType.getAllTypes());
        }

        public static ClassFileLocator of(Map<TypeDescription, byte[]> binaryRepresentations) {
            Map<String, byte[]> classFiles = new HashMap<>();
            for (Map.Entry<TypeDescription, byte[]> entry : binaryRepresentations.entrySet()) {
                classFiles.put(entry.getKey().getName(), entry.getValue());
            }
            return new Simple(classFiles);
        }

        public static ClassFileLocator ofResources(Map<String, byte[]> binaryRepresentations) {
            Map<String, byte[]> classFiles = new HashMap<>();
            for (Map.Entry<String, byte[]> entry : binaryRepresentations.entrySet()) {
                if (entry.getKey().endsWith(".class")) {
                    classFiles.put(entry.getKey().substring(0, entry.getKey().length() - ".class".length()).replace('/', '.'), entry.getValue());
                }
            }
            return new Simple(classFiles);
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) {
            byte[] binaryRepresentation = this.classFiles.get(name);
            return binaryRepresentation == null ? new Resolution.Illegal(name) : new Resolution.Explicit(binaryRepresentation);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForClassLoader.class */
    public static class ForClassLoader implements ClassFileLocator {
        private static final ClassLoader BOOT_LOADER_PROXY = (ClassLoader) AccessController.doPrivileged(BootLoaderProxyCreationAction.INSTANCE);
        private final ClassLoader classLoader;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classLoader.equals(((ForClassLoader) obj).classLoader);
        }

        public int hashCode() {
            return (17 * 31) + this.classLoader.hashCode();
        }

        protected ForClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public static ClassFileLocator ofSystemLoader() {
            return new ForClassLoader(ClassLoader.getSystemClassLoader());
        }

        public static ClassFileLocator ofPlatformLoader() {
            return of(ClassLoader.getSystemClassLoader().getParent());
        }

        public static ClassFileLocator ofBootLoader() {
            return new ForClassLoader(BOOT_LOADER_PROXY);
        }

        public static ClassFileLocator of(ClassLoader classLoader) {
            return new ForClassLoader(classLoader == null ? BOOT_LOADER_PROXY : classLoader);
        }

        public static byte[] read(Class<?> type) {
            try {
                ClassLoader classLoader = type.getClassLoader();
                return locate(classLoader == null ? BOOT_LOADER_PROXY : classLoader, TypeDescription.ForLoadedType.getName(type)).resolve();
            } catch (IOException exception) {
                throw new IllegalStateException("Cannot read class file for " + type, exception);
            }
        }

        public static Map<Class<?>, byte[]> read(Class<?>... type) {
            return read(Arrays.asList(type));
        }

        public static Map<Class<?>, byte[]> read(Collection<? extends Class<?>> types) {
            Map<Class<?>, byte[]> result = new HashMap<>();
            for (Class<?> type : types) {
                result.put(type, read(type));
            }
            return result;
        }

        public static Map<String, byte[]> readToNames(Class<?>... type) {
            return readToNames(Arrays.asList(type));
        }

        public static Map<String, byte[]> readToNames(Collection<? extends Class<?>> types) {
            Map<String, byte[]> result = new HashMap<>();
            for (Class<?> type : types) {
                result.put(type.getName(), read(type));
            }
            return result;
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            return locate(this.classLoader, name);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        protected static Resolution locate(ClassLoader classLoader, String name) throws IOException {
            InputStream inputStream = classLoader.getResourceAsStream(name.replace('.', '/') + ".class");
            if (inputStream != null) {
                try {
                    Resolution.Explicit explicit = new Resolution.Explicit(StreamDrainer.DEFAULT.drain(inputStream));
                    inputStream.close();
                    return explicit;
                } catch (Throwable th) {
                    inputStream.close();
                    throw th;
                }
            }
            return new Resolution.Illegal(name);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForClassLoader$BootLoaderProxyCreationAction.class */
        protected enum BootLoaderProxyCreationAction implements PrivilegedAction<ClassLoader> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return new URLClassLoader(new URL[0], ClassLoadingStrategy.BOOTSTRAP_LOADER);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForClassLoader$WeaklyReferenced.class */
        public static class WeaklyReferenced extends WeakReference<ClassLoader> implements ClassFileLocator {
            private final int hashCode;

            protected WeaklyReferenced(ClassLoader classLoader) {
                super(classLoader);
                this.hashCode = System.identityHashCode(classLoader);
            }

            public static ClassFileLocator of(ClassLoader classLoader) {
                return (classLoader == null || classLoader == ClassLoader.getSystemClassLoader() || classLoader == ClassLoader.getSystemClassLoader().getParent()) ? ForClassLoader.of(classLoader) : new WeaklyReferenced(classLoader);
            }

            @Override // net.bytebuddy.dynamic.ClassFileLocator
            public Resolution locate(String name) throws IOException {
                ClassLoader classLoader = (ClassLoader) get();
                return classLoader == null ? new Resolution.Illegal(name) : ForClassLoader.locate(classLoader, name);
            }

            @Override // java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }

            public int hashCode() {
                return this.hashCode;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (other == null || getClass() != other.getClass()) {
                    return false;
                }
                WeaklyReferenced weaklyReferenced = (WeaklyReferenced) other;
                ClassLoader classLoader = (ClassLoader) weaklyReferenced.get();
                return classLoader != null && get() == classLoader;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForModule.class */
    public static class ForModule implements ClassFileLocator {
        private static final Object[] NO_ARGUMENTS = new Object[0];
        private final JavaModule module;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.module.equals(((ForModule) obj).module);
        }

        public int hashCode() {
            return (17 * 31) + this.module.hashCode();
        }

        protected ForModule(JavaModule module) {
            this.module = module;
        }

        @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should always be wrapped for clarity")
        public static ClassFileLocator ofBootLayer() {
            try {
                Map<String, ClassFileLocator> bootModules = new HashMap<>();
                Class<?> layerType = Class.forName("java.lang.ModuleLayer");
                Method getPackages = JavaType.MODULE.load().getMethod("getPackages", new Class[0]);
                for (Object rawModule : (Set) layerType.getMethod("modules", new Class[0]).invoke(layerType.getMethod("boot", new Class[0]).invoke(null, new Object[0]), new Object[0])) {
                    ClassFileLocator classFileLocator = of(JavaModule.of(rawModule));
                    for (Object packageName : (Set) getPackages.invoke(rawModule, NO_ARGUMENTS)) {
                        bootModules.put((String) packageName, classFileLocator);
                    }
                }
                return new PackageDiscriminating(bootModules);
            } catch (Exception exception) {
                throw new IllegalStateException("Cannot process boot layer", exception);
            }
        }

        public static ClassFileLocator of(JavaModule module) {
            return module.isNamed() ? new ForModule(module) : ForClassLoader.of(module.getClassLoader());
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            return locate(this.module, name);
        }

        protected static Resolution locate(JavaModule module, String typeName) throws IOException {
            InputStream inputStream = module.getResourceAsStream(typeName.replace('.', '/') + ".class");
            if (inputStream != null) {
                try {
                    Resolution.Explicit explicit = new Resolution.Explicit(StreamDrainer.DEFAULT.drain(inputStream));
                    inputStream.close();
                    return explicit;
                } catch (Throwable th) {
                    inputStream.close();
                    throw th;
                }
            }
            return new Resolution.Illegal(typeName);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForModule$WeaklyReferenced.class */
        public static class WeaklyReferenced extends WeakReference<Object> implements ClassFileLocator {
            private final int hashCode;

            protected WeaklyReferenced(Object module) {
                super(module);
                this.hashCode = System.identityHashCode(module);
            }

            public static ClassFileLocator of(JavaModule module) {
                if (module.isNamed()) {
                    return (module.getClassLoader() == null || module.getClassLoader() == ClassLoader.getSystemClassLoader() || module.getClassLoader() == ClassLoader.getSystemClassLoader().getParent()) ? new ForModule(module) : new WeaklyReferenced(module.unwrap());
                }
                return ForClassLoader.WeaklyReferenced.of(module.getClassLoader());
            }

            @Override // net.bytebuddy.dynamic.ClassFileLocator
            public Resolution locate(String name) throws IOException {
                Object module = get();
                return module == null ? new Resolution.Illegal(name) : ForModule.locate(JavaModule.of(module), name);
            }

            @Override // java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }

            public int hashCode() {
                return this.hashCode;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (other == null || getClass() != other.getClass()) {
                    return false;
                }
                WeaklyReferenced weaklyReferenced = (WeaklyReferenced) other;
                Object module = weaklyReferenced.get();
                return module != null && get() == module;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForJarFile.class */
    public static class ForJarFile implements ClassFileLocator {
        private static final List<String> RUNTIME_LOCATIONS = Arrays.asList("lib/rt.jar", "../lib/rt.jar", "../Classes/classes.jar");
        private final JarFile jarFile;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.jarFile.equals(((ForJarFile) obj).jarFile);
        }

        public int hashCode() {
            return (17 * 31) + this.jarFile.hashCode();
        }

        public ForJarFile(JarFile jarFile) {
            this.jarFile = jarFile;
        }

        public static ClassFileLocator of(File file) throws IOException {
            return new ForJarFile(new JarFile(file));
        }

        public static ClassFileLocator ofClassPath() throws IOException {
            return ofClassPath(System.getProperty("java.class.path"));
        }

        public static ClassFileLocator ofClassPath(String classPath) throws IOException {
            String[] split;
            List<ClassFileLocator> classFileLocators = new ArrayList<>();
            for (String element : Pattern.compile(System.getProperty("path.separator"), 16).split(classPath)) {
                File file = new File(element);
                if (file.isDirectory()) {
                    classFileLocators.add(new ForFolder(file));
                } else if (file.isFile()) {
                    classFileLocators.add(of(file));
                }
            }
            return new Compound(classFileLocators);
        }

        public static ClassFileLocator ofRuntimeJar() throws IOException {
            String javaHome = System.getProperty("java.home").replace('\\', '/');
            File runtimeJar = null;
            Iterator<String> it = RUNTIME_LOCATIONS.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String location = it.next();
                File candidate = new File(javaHome, location);
                if (candidate.isFile()) {
                    runtimeJar = candidate;
                    break;
                }
            }
            if (runtimeJar == null) {
                throw new IllegalStateException("Runtime jar does not exist in " + javaHome + " for any of " + RUNTIME_LOCATIONS);
            }
            return of(runtimeJar);
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            ZipEntry zipEntry = this.jarFile.getEntry(name.replace('.', '/') + ".class");
            if (zipEntry == null) {
                return new Resolution.Illegal(name);
            }
            InputStream inputStream = this.jarFile.getInputStream(zipEntry);
            try {
                Resolution.Explicit explicit = new Resolution.Explicit(StreamDrainer.DEFAULT.drain(inputStream));
                inputStream.close();
                return explicit;
            } catch (Throwable th) {
                inputStream.close();
                throw th;
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.jarFile.close();
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForModuleFile.class */
    public static class ForModuleFile implements ClassFileLocator {
        private static final String JMOD_FILE_EXTENSION = ".jmod";
        private static final List<String> BOOT_LOCATIONS = Arrays.asList("jmods", "../jmods", "modules");
        private final ZipFile zipFile;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.zipFile.equals(((ForModuleFile) obj).zipFile);
        }

        public int hashCode() {
            return (17 * 31) + this.zipFile.hashCode();
        }

        public ForModuleFile(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        public static ClassFileLocator ofBootPath() throws IOException {
            String javaHome = System.getProperty("java.home").replace('\\', '/');
            File bootPath = null;
            Iterator<String> it = BOOT_LOCATIONS.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String location = it.next();
                File candidate = new File(javaHome, location);
                if (candidate.isDirectory()) {
                    bootPath = candidate;
                    break;
                }
            }
            if (bootPath == null) {
                throw new IllegalStateException("Boot modules do not exist in " + javaHome + " for any of " + BOOT_LOCATIONS);
            }
            return ofBootPath(bootPath);
        }

        public static ClassFileLocator ofBootPath(File bootPath) throws IOException {
            File[] module = bootPath.listFiles();
            if (module == null) {
                return NoOp.INSTANCE;
            }
            List<ClassFileLocator> classFileLocators = new ArrayList<>(module.length);
            for (File aModule : module) {
                if (aModule.isFile()) {
                    classFileLocators.add(of(aModule));
                } else if (aModule.isDirectory()) {
                    classFileLocators.add(new ForFolder(aModule));
                }
            }
            return new Compound(classFileLocators);
        }

        public static ClassFileLocator ofModulePath() throws IOException {
            String modulePath = System.getProperty("jdk.module.path");
            return modulePath == null ? NoOp.INSTANCE : ofModulePath(modulePath);
        }

        public static ClassFileLocator ofModulePath(String modulePath) throws IOException {
            return ofModulePath(modulePath, System.getProperty("user.dir"));
        }

        public static ClassFileLocator ofModulePath(String modulePath, String baseFolder) throws IOException {
            String[] split;
            ClassFileLocator of;
            ClassFileLocator of2;
            List<ClassFileLocator> classFileLocators = new ArrayList<>();
            for (String element : Pattern.compile(System.getProperty("path.separator"), 16).split(modulePath)) {
                File file = new File(baseFolder, element);
                if (file.isDirectory()) {
                    File[] module = file.listFiles();
                    if (module != null) {
                        for (File aModule : module) {
                            if (aModule.isDirectory()) {
                                classFileLocators.add(new ForFolder(aModule));
                            } else if (aModule.isFile()) {
                                if (aModule.getName().endsWith(JMOD_FILE_EXTENSION)) {
                                    of2 = of(aModule);
                                } else {
                                    of2 = ForJarFile.of(aModule);
                                }
                                classFileLocators.add(of2);
                            }
                        }
                    }
                } else if (file.isFile()) {
                    if (file.getName().endsWith(JMOD_FILE_EXTENSION)) {
                        of = of(file);
                    } else {
                        of = ForJarFile.of(file);
                    }
                    classFileLocators.add(of);
                }
            }
            return new Compound(classFileLocators);
        }

        public static ClassFileLocator of(File file) throws IOException {
            return new ForModuleFile(new ZipFile(file));
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            ZipEntry zipEntry = this.zipFile.getEntry("classes/" + name.replace('.', '/') + ".class");
            if (zipEntry == null) {
                return new Resolution.Illegal(name);
            }
            InputStream inputStream = this.zipFile.getInputStream(zipEntry);
            try {
                Resolution.Explicit explicit = new Resolution.Explicit(StreamDrainer.DEFAULT.drain(inputStream));
                inputStream.close();
                return explicit;
            } catch (Throwable th) {
                inputStream.close();
                throw th;
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.zipFile.close();
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForFolder.class */
    public static class ForFolder implements ClassFileLocator {
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

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            File file = new File(this.folder, name.replace('.', File.separatorChar) + ".class");
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                try {
                    Resolution.Explicit explicit = new Resolution.Explicit(StreamDrainer.DEFAULT.drain(inputStream));
                    inputStream.close();
                    return explicit;
                } catch (Throwable th) {
                    inputStream.close();
                    throw th;
                }
            }
            return new Resolution.Illegal(name);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForUrl.class */
    public static class ForUrl implements ClassFileLocator {
        private final ClassLoader classLoader;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classLoader.equals(((ForUrl) obj).classLoader);
        }

        public int hashCode() {
            return (17 * 31) + this.classLoader.hashCode();
        }

        public ForUrl(URL... url) {
            this.classLoader = (ClassLoader) AccessController.doPrivileged(new ClassLoaderCreationAction(url));
        }

        public ForUrl(Collection<? extends URL> urls) {
            this((URL[]) urls.toArray(new URL[0]));
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            return ForClassLoader.locate(this.classLoader, name);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.classLoader instanceof Closeable) {
                ((Closeable) this.classLoader).close();
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$ForUrl$ClassLoaderCreationAction.class */
        protected static class ClassLoaderCreationAction implements PrivilegedAction<ClassLoader> {
            private final URL[] url;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && Arrays.equals(this.url, ((ClassLoaderCreationAction) obj).url);
            }

            public int hashCode() {
                return (17 * 31) + Arrays.hashCode(this.url);
            }

            protected ClassLoaderCreationAction(URL[] url) {
                this.url = url;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return new URLClassLoader(this.url, ClassLoadingStrategy.BOOTSTRAP_LOADER);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased.class */
    public static class AgentBased implements ClassFileLocator {
        private static final String INSTALLER_TYPE = "net.bytebuddy.agent.Installer";
        private static final String INSTRUMENTATION_GETTER = "getInstrumentation";
        private static final Object STATIC_MEMBER = null;
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final Instrumentation instrumentation;
        private final ClassLoadingDelegate classLoadingDelegate;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.instrumentation.equals(((AgentBased) obj).instrumentation) && this.classLoadingDelegate.equals(((AgentBased) obj).classLoadingDelegate);
        }

        public int hashCode() {
            return (((17 * 31) + this.instrumentation.hashCode()) * 31) + this.classLoadingDelegate.hashCode();
        }

        public AgentBased(Instrumentation instrumentation, ClassLoader classLoader) {
            this(instrumentation, ClassLoadingDelegate.Default.of(classLoader));
        }

        public AgentBased(Instrumentation instrumentation, ClassLoadingDelegate classLoadingDelegate) {
            if (!DISPATCHER.isRetransformClassesSupported(instrumentation)) {
                throw new IllegalArgumentException(instrumentation + " does not support retransformation");
            }
            this.instrumentation = instrumentation;
            this.classLoadingDelegate = classLoadingDelegate;
        }

        public static ClassFileLocator fromInstalledAgent(ClassLoader classLoader) {
            try {
                return new AgentBased((Instrumentation) ClassLoader.getSystemClassLoader().loadClass(INSTALLER_TYPE).getMethod(INSTRUMENTATION_GETTER, new Class[0]).invoke(STATIC_MEMBER, new Object[0]), classLoader);
            } catch (RuntimeException exception) {
                throw exception;
            } catch (Exception exception2) {
                throw new IllegalStateException("The Byte Buddy agent is not installed or not accessible", exception2);
            }
        }

        public static ClassFileLocator of(Instrumentation instrumentation, Class<?> type) {
            return new AgentBased(instrumentation, ClassLoadingDelegate.Explicit.of(type));
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) {
            try {
                ExtractionClassFileTransformer classFileTransformer = new ExtractionClassFileTransformer(this.classLoadingDelegate.getClassLoader(), name);
                DISPATCHER.addTransformer(this.instrumentation, classFileTransformer, true);
                try {
                    DISPATCHER.retransformClasses(this.instrumentation, new Class[]{this.classLoadingDelegate.locate(name)});
                    byte[] binaryRepresentation = classFileTransformer.getBinaryRepresentation();
                    return binaryRepresentation == null ? new Resolution.Illegal(name) : new Resolution.Explicit(binaryRepresentation);
                } finally {
                    this.instrumentation.removeTransformer(classFileTransformer);
                }
            } catch (RuntimeException exception) {
                throw exception;
            } catch (Exception e) {
                return new Resolution.Illegal(name);
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$Dispatcher.class */
        protected interface Dispatcher {
            boolean isRetransformClassesSupported(Instrumentation instrumentation);

            void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean z);

            void retransformClasses(Instrumentation instrumentation, Class<?>[] clsArr) throws UnmodifiableClassException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    try {
                        Class<?> instrumentation = Class.forName("java.lang.instrument.Instrumentation");
                        return new ForJava6CapableVm(instrumentation.getMethod("isRetransformClassesSupported", new Class[0]), instrumentation.getMethod("addTransformer", ClassFileTransformer.class, Boolean.TYPE), instrumentation.getMethod("retransformClasses", Class[].class));
                    } catch (ClassNotFoundException e) {
                        return ForLegacyVm.INSTANCE;
                    } catch (NoSuchMethodException e2) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.Dispatcher
                public boolean isRetransformClassesSupported(Instrumentation instrumentation) {
                    return false;
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.Dispatcher
                public void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean canRetransform) {
                    throw new IllegalStateException("The current VM does not support class retransformation");
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.Dispatcher
                public void retransformClasses(Instrumentation instrumentation, Class<?>[] type) {
                    throw new IllegalStateException("The current VM does not support class retransformation");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$Dispatcher$ForJava6CapableVm.class */
            public static class ForJava6CapableVm implements Dispatcher {
                private final Method isRetransformClassesSupported;
                private final Method addTransformer;
                private final Method retransformClasses;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.isRetransformClassesSupported.equals(((ForJava6CapableVm) obj).isRetransformClassesSupported) && this.addTransformer.equals(((ForJava6CapableVm) obj).addTransformer) && this.retransformClasses.equals(((ForJava6CapableVm) obj).retransformClasses);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.isRetransformClassesSupported.hashCode()) * 31) + this.addTransformer.hashCode()) * 31) + this.retransformClasses.hashCode();
                }

                protected ForJava6CapableVm(Method isRetransformClassesSupported, Method addTransformer, Method retransformClasses) {
                    this.isRetransformClassesSupported = isRetransformClassesSupported;
                    this.addTransformer = addTransformer;
                    this.retransformClasses = retransformClasses;
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.Dispatcher
                public boolean isRetransformClassesSupported(Instrumentation instrumentation) {
                    try {
                        return ((Boolean) this.isRetransformClassesSupported.invoke(instrumentation, new Object[0])).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#isRetransformClassesSupported", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#isRetransformClassesSupported", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.Dispatcher
                public void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean canRetransform) {
                    try {
                        this.addTransformer.invoke(instrumentation, classFileTransformer, Boolean.valueOf(canRetransform));
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#addTransformer", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#addTransformer", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.Dispatcher
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

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate.class */
        public interface ClassLoadingDelegate {
            Class<?> locate(String str) throws ClassNotFoundException;

            ClassLoader getClassLoader();

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$Default.class */
            public static class Default implements ClassLoadingDelegate {
                protected final ClassLoader classLoader;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.classLoader.equals(((Default) obj).classLoader);
                }

                public int hashCode() {
                    return (17 * 31) + this.classLoader.hashCode();
                }

                protected Default(ClassLoader classLoader) {
                    this.classLoader = classLoader;
                }

                public static ClassLoadingDelegate of(ClassLoader classLoader) {
                    if (ForDelegatingClassLoader.isDelegating(classLoader)) {
                        return new ForDelegatingClassLoader(classLoader);
                    }
                    return new Default(classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader);
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate
                public Class<?> locate(String name) throws ClassNotFoundException {
                    return this.classLoader.loadClass(name);
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate
                public ClassLoader getClassLoader() {
                    return this.classLoader;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader.class */
            public static class ForDelegatingClassLoader extends Default {
                private static final String DELEGATING_CLASS_LOADER_NAME = "sun.reflect.DelegatingClassLoader";
                private static final int ONLY = 0;
                private static final Dispatcher.Initializable DISPATCHER = (Dispatcher.Initializable) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);

                protected ForDelegatingClassLoader(ClassLoader classLoader) {
                    super(classLoader);
                }

                protected static boolean isDelegating(ClassLoader classLoader) {
                    return classLoader != null && classLoader.getClass().getName().equals(DELEGATING_CLASS_LOADER_NAME);
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate.Default, net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate
                public Class<?> locate(String name) throws ClassNotFoundException {
                    try {
                        Vector<Class<?>> classes = DISPATCHER.initialize().extract(this.classLoader);
                        if (classes.size() != 1) {
                            return super.locate(name);
                        }
                        Class<?> type = classes.get(0);
                        return TypeDescription.ForLoadedType.getName(type).equals(name) ? type : super.locate(name);
                    } catch (RuntimeException e) {
                        return super.locate(name);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader$Dispatcher.class */
                protected interface Dispatcher {

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader$Dispatcher$Initializable.class */
                    public interface Initializable {
                        Dispatcher initialize();
                    }

                    Vector<Class<?>> extract(ClassLoader classLoader);

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader$Dispatcher$CreationAction.class */
                    public enum CreationAction implements PrivilegedAction<Initializable> {
                        INSTANCE;

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Initializable run() {
                            try {
                                return new Resolved(ClassLoader.class.getDeclaredField("classes"));
                            } catch (Exception exception) {
                                return new Unresolved(exception.getMessage());
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader$Dispatcher$Resolved.class */
                    public static class Resolved implements Dispatcher, Initializable, PrivilegedAction<Dispatcher> {
                        private final Field field;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.field.equals(((Resolved) obj).field);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.field.hashCode();
                        }

                        public Resolved(Field field) {
                            this.field = field;
                        }

                        @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate.ForDelegatingClassLoader.Dispatcher.Initializable
                        public Dispatcher initialize() {
                            return (Dispatcher) AccessController.doPrivileged(this);
                        }

                        @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate.ForDelegatingClassLoader.Dispatcher
                        public Vector<Class<?>> extract(ClassLoader classLoader) {
                            try {
                                return (Vector) this.field.get(classLoader);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access field", exception);
                            }
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Dispatcher run() {
                            this.field.setAccessible(true);
                            return this;
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader$Dispatcher$Unresolved.class */
                    public static class Unresolved implements Initializable {
                        private final String message;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.message.equals(((Unresolved) obj).message);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.message.hashCode();
                        }

                        public Unresolved(String message) {
                            this.message = message;
                        }

                        @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate.ForDelegatingClassLoader.Dispatcher.Initializable
                        public Dispatcher initialize() {
                            throw new UnsupportedOperationException("Could not locate classes vector: " + this.message);
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ClassLoadingDelegate$Explicit.class */
            public static class Explicit implements ClassLoadingDelegate {
                private final ClassLoadingDelegate fallbackDelegate;
                private final Map<String, Class<?>> types;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fallbackDelegate.equals(((Explicit) obj).fallbackDelegate) && this.types.equals(((Explicit) obj).types);
                }

                public int hashCode() {
                    return (((17 * 31) + this.fallbackDelegate.hashCode()) * 31) + this.types.hashCode();
                }

                public Explicit(ClassLoader classLoader, Collection<? extends Class<?>> types) {
                    this(Default.of(classLoader), types);
                }

                public Explicit(ClassLoadingDelegate fallbackDelegate, Collection<? extends Class<?>> types) {
                    this.fallbackDelegate = fallbackDelegate;
                    this.types = new HashMap();
                    for (Class<?> type : types) {
                        this.types.put(TypeDescription.ForLoadedType.getName(type), type);
                    }
                }

                public static ClassLoadingDelegate of(Class<?> type) {
                    return new Explicit(type.getClassLoader(), Collections.singleton(type));
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate
                public Class<?> locate(String name) throws ClassNotFoundException {
                    Class<?> type = this.types.get(name);
                    return type == null ? this.fallbackDelegate.locate(name) : type;
                }

                @Override // net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate
                public ClassLoader getClassLoader() {
                    return this.fallbackDelegate.getClassLoader();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$AgentBased$ExtractionClassFileTransformer.class */
        protected static class ExtractionClassFileTransformer implements ClassFileTransformer {
            private static final byte[] DO_NOT_TRANSFORM = null;
            private final ClassLoader classLoader;
            private final String typeName;
            @SuppressFBWarnings(value = {"VO_VOLATILE_REFERENCE_TO_ARRAY"}, justification = "The array is not to be modified by contract")
            private volatile byte[] binaryRepresentation;

            protected ExtractionClassFileTransformer(ClassLoader classLoader, String typeName) {
                this.classLoader = classLoader;
                this.typeName = typeName;
            }

            @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "The array is not to be modified by contract")
            public byte[] transform(ClassLoader classLoader, String internalName, Class<?> redefinedType, ProtectionDomain protectionDomain, byte[] binaryRepresentation) {
                if (internalName != null && ElementMatchers.isChildOf(this.classLoader).matches(classLoader) && this.typeName.equals(internalName.replace('/', '.'))) {
                    this.binaryRepresentation = (byte[]) binaryRepresentation.clone();
                }
                return DO_NOT_TRANSFORM;
            }

            @SuppressFBWarnings(value = {"EI_EXPOSE_REP"}, justification = "The array is not to be modified by contract")
            protected byte[] getBinaryRepresentation() {
                return this.binaryRepresentation;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$PackageDiscriminating.class */
    public static class PackageDiscriminating implements ClassFileLocator {
        private final Map<String, ClassFileLocator> classFileLocators;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classFileLocators.equals(((PackageDiscriminating) obj).classFileLocators);
        }

        public int hashCode() {
            return (17 * 31) + this.classFileLocators.hashCode();
        }

        public PackageDiscriminating(Map<String, ClassFileLocator> classFileLocators) {
            this.classFileLocators = classFileLocators;
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            int packageIndex = name.lastIndexOf(46);
            ClassFileLocator classFileLocator = this.classFileLocators.get(packageIndex == -1 ? "" : name.substring(0, packageIndex));
            return classFileLocator == null ? new Resolution.Illegal(name) : classFileLocator.locate(name);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            for (ClassFileLocator classFileLocator : this.classFileLocators.values()) {
                classFileLocator.close();
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/ClassFileLocator$Compound.class */
    public static class Compound implements ClassFileLocator, Closeable {
        private final List<ClassFileLocator> classFileLocators;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.classFileLocators.equals(((Compound) obj).classFileLocators);
        }

        public int hashCode() {
            return (17 * 31) + this.classFileLocators.hashCode();
        }

        public Compound(ClassFileLocator... classFileLocator) {
            this(Arrays.asList(classFileLocator));
        }

        public Compound(List<? extends ClassFileLocator> classFileLocators) {
            this.classFileLocators = new ArrayList();
            for (ClassFileLocator classFileLocator : classFileLocators) {
                if (classFileLocator instanceof Compound) {
                    this.classFileLocators.addAll(((Compound) classFileLocator).classFileLocators);
                } else if (!(classFileLocator instanceof NoOp)) {
                    this.classFileLocators.add(classFileLocator);
                }
            }
        }

        @Override // net.bytebuddy.dynamic.ClassFileLocator
        public Resolution locate(String name) throws IOException {
            for (ClassFileLocator classFileLocator : this.classFileLocators) {
                Resolution resolution = classFileLocator.locate(name);
                if (resolution.isResolved()) {
                    return resolution;
                }
            }
            return new Resolution.Illegal(name);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            for (ClassFileLocator classFileLocator : this.classFileLocators) {
                classFileLocator.close();
            }
        }
    }
}
