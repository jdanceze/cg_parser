package net.bytebuddy.utility;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationSource;
import net.bytebuddy.description.type.PackageDescription;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule.class */
public class JavaModule implements NamedElement.WithOptionalName, AnnotationSource {
    public static final JavaModule UNSUPPORTED = null;
    private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
    private final AnnotatedElement module;

    protected JavaModule(AnnotatedElement module) {
        this.module = module;
    }

    public static JavaModule ofType(Class<?> type) {
        return DISPATCHER.moduleOf(type);
    }

    public static JavaModule of(Object module) {
        if (!JavaType.MODULE.isInstance(module)) {
            throw new IllegalArgumentException("Not a Java module: " + module);
        }
        return new JavaModule((AnnotatedElement) module);
    }

    public static boolean isSupported() {
        return DISPATCHER.isAlive();
    }

    @Override // net.bytebuddy.description.NamedElement.WithOptionalName
    public boolean isNamed() {
        return DISPATCHER.isNamed(this.module);
    }

    @Override // net.bytebuddy.description.NamedElement
    public String getActualName() {
        return DISPATCHER.getName(this.module);
    }

    public InputStream getResourceAsStream(String name) {
        return DISPATCHER.getResourceAsStream(this.module, name);
    }

    public ClassLoader getClassLoader() {
        return DISPATCHER.getClassLoader(this.module);
    }

    public Object unwrap() {
        return this.module;
    }

    public boolean canRead(JavaModule module) {
        return DISPATCHER.canRead(this.module, module.unwrap());
    }

    public boolean isExported(PackageDescription packageDescription, JavaModule module) {
        return packageDescription == null || DISPATCHER.isExported(this.module, module.unwrap(), packageDescription.getName());
    }

    public boolean isOpened(PackageDescription packageDescription, JavaModule module) {
        return packageDescription == null || DISPATCHER.isOpened(this.module, module.unwrap(), packageDescription.getName());
    }

    @Override // net.bytebuddy.description.annotation.AnnotationSource
    public AnnotationList getDeclaredAnnotations() {
        return new AnnotationList.ForLoadedAnnotations(this.module.getDeclaredAnnotations());
    }

    public void modify(Instrumentation instrumentation, Set<JavaModule> reads, Map<String, Set<JavaModule>> exports, Map<String, Set<JavaModule>> opens, Set<Class<?>> uses, Map<Class<?>, List<Class<?>>> provides) {
        Set<Object> unwrappedReads = new HashSet<>();
        for (JavaModule read : reads) {
            unwrappedReads.add(read.unwrap());
        }
        Map<String, Set<Object>> unwrappedExports = new HashMap<>();
        for (Map.Entry<String, Set<JavaModule>> entry : exports.entrySet()) {
            Set<Object> modules = new HashSet<>();
            for (JavaModule module : entry.getValue()) {
                modules.add(module.unwrap());
            }
            unwrappedExports.put(entry.getKey(), modules);
        }
        Map<String, Set<Object>> unwrappedOpens = new HashMap<>();
        for (Map.Entry<String, Set<JavaModule>> entry2 : opens.entrySet()) {
            Set<Object> modules2 = new HashSet<>();
            for (JavaModule module2 : entry2.getValue()) {
                modules2.add(module2.unwrap());
            }
            unwrappedOpens.put(entry2.getKey(), modules2);
        }
        DISPATCHER.modify(instrumentation, this.module, unwrappedReads, unwrappedExports, unwrappedOpens, uses, provides);
    }

    public int hashCode() {
        return this.module.hashCode();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaModule)) {
            return false;
        }
        JavaModule javaModule = (JavaModule) other;
        return this.module.equals(javaModule.module);
    }

    public String toString() {
        return this.module.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule$Dispatcher.class */
    public interface Dispatcher {
        boolean isAlive();

        JavaModule moduleOf(Class<?> cls);

        boolean isNamed(Object obj);

        String getName(Object obj);

        InputStream getResourceAsStream(Object obj, String str);

        ClassLoader getClassLoader(Object obj);

        boolean isExported(Object obj, Object obj2, String str);

        boolean isOpened(Object obj, Object obj2, String str);

        boolean canRead(Object obj, Object obj2);

        void modify(Instrumentation instrumentation, Object obj, Set<Object> set, Map<String, Set<Object>> map, Map<String, Set<Object>> map2, Set<Class<?>> set2, Map<Class<?>, List<Class<?>>> map3);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule$Dispatcher$CreationAction.class */
        public enum CreationAction implements PrivilegedAction<Dispatcher> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
            public Dispatcher run() {
                try {
                    Class<?> module = Class.forName("java.lang.Module", false, null);
                    try {
                        Class<?> instrumentation = Class.forName("java.lang.instrument.Instrumentation");
                        return new Enabled.WithInstrumentationSupport(Class.class.getMethod("getModule", new Class[0]), module.getMethod("getClassLoader", new Class[0]), module.getMethod("isNamed", new Class[0]), module.getMethod("getName", new Class[0]), module.getMethod("getResourceAsStream", String.class), module.getMethod("isExported", String.class, module), module.getMethod("isOpen", String.class, module), module.getMethod("canRead", module), instrumentation.getMethod("isModifiableModule", module), instrumentation.getMethod("redefineModule", module, Set.class, Map.class, Map.class, Set.class, Map.class));
                    } catch (ClassNotFoundException e) {
                        return new Enabled.WithoutInstrumentationSupport(Class.class.getMethod("getModule", new Class[0]), module.getMethod("getClassLoader", new Class[0]), module.getMethod("isNamed", new Class[0]), module.getMethod("getName", new Class[0]), module.getMethod("getResourceAsStream", String.class), module.getMethod("isExported", String.class, module), module.getMethod("isOpen", String.class, module), module.getMethod("canRead", module));
                    }
                } catch (ClassNotFoundException e2) {
                    return Disabled.INSTANCE;
                } catch (NoSuchMethodException e3) {
                    return Disabled.INSTANCE;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule$Dispatcher$Enabled.class */
        public static abstract class Enabled implements Dispatcher {
            private static final Object[] NO_ARGUMENTS = new Object[0];
            private final Method getModule;
            private final Method getClassLoader;
            private final Method isNamed;
            private final Method getName;
            private final Method getResourceAsStream;
            private final Method isExported;
            private final Method isOpened;
            private final Method canRead;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.getModule.equals(((Enabled) obj).getModule) && this.getClassLoader.equals(((Enabled) obj).getClassLoader) && this.isNamed.equals(((Enabled) obj).isNamed) && this.getName.equals(((Enabled) obj).getName) && this.getResourceAsStream.equals(((Enabled) obj).getResourceAsStream) && this.isExported.equals(((Enabled) obj).isExported) && this.isOpened.equals(((Enabled) obj).isOpened) && this.canRead.equals(((Enabled) obj).canRead);
            }

            public int hashCode() {
                return (((((((((((((((17 * 31) + this.getModule.hashCode()) * 31) + this.getClassLoader.hashCode()) * 31) + this.isNamed.hashCode()) * 31) + this.getName.hashCode()) * 31) + this.getResourceAsStream.hashCode()) * 31) + this.isExported.hashCode()) * 31) + this.isOpened.hashCode()) * 31) + this.canRead.hashCode();
            }

            protected Enabled(Method getModule, Method getClassLoader, Method isNamed, Method getName, Method getResourceAsStream, Method isExported, Method isOpened, Method canRead) {
                this.getModule = getModule;
                this.getClassLoader = getClassLoader;
                this.isNamed = isNamed;
                this.getName = getName;
                this.getResourceAsStream = getResourceAsStream;
                this.isExported = isExported;
                this.isOpened = isOpened;
                this.canRead = canRead;
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isAlive() {
                return true;
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public JavaModule moduleOf(Class<?> type) {
                try {
                    return new JavaModule((AnnotatedElement) this.getModule.invoke(type, NO_ARGUMENTS));
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.getModule, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.getModule, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public InputStream getResourceAsStream(Object module, String name) {
                try {
                    return (InputStream) this.getResourceAsStream.invoke(module, name);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.getResourceAsStream, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.getResourceAsStream, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public ClassLoader getClassLoader(Object module) {
                try {
                    return (ClassLoader) this.getClassLoader.invoke(module, NO_ARGUMENTS);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.getClassLoader, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.getClassLoader, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isNamed(Object module) {
                try {
                    return ((Boolean) this.isNamed.invoke(module, NO_ARGUMENTS)).booleanValue();
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.isNamed, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.isNamed, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public String getName(Object module) {
                try {
                    return (String) this.getName.invoke(module, NO_ARGUMENTS);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.getName, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.getName, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isExported(Object source, Object target, String aPackage) {
                try {
                    return ((Boolean) this.isExported.invoke(source, aPackage, target)).booleanValue();
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.isExported, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.isExported, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isOpened(Object source, Object target, String aPackage) {
                try {
                    return ((Boolean) this.isOpened.invoke(source, aPackage, target)).booleanValue();
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.isOpened, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.isOpened, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean canRead(Object source, Object target) {
                try {
                    return ((Boolean) this.canRead.invoke(source, target)).booleanValue();
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access " + this.canRead, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke " + this.canRead, exception2.getCause());
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule$Dispatcher$Enabled$WithoutInstrumentationSupport.class */
            public static class WithoutInstrumentationSupport extends Enabled {
                protected WithoutInstrumentationSupport(Method getModule, Method getClassLoader, Method isNamed, Method getName, Method getResourceAsStream, Method isExported, Method isOpened, Method canRead) {
                    super(getModule, getClassLoader, isNamed, getName, getResourceAsStream, isExported, isOpened, canRead);
                }

                @Override // net.bytebuddy.utility.JavaModule.Dispatcher
                public void modify(Instrumentation instrumentation, Object source, Set<Object> reads, Map<String, Set<Object>> exports, Map<String, Set<Object>> opens, Set<Class<?>> uses, Map<Class<?>, List<Class<?>>> provides) {
                    throw new IllegalStateException("Did not expect use of instrumentation");
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule$Dispatcher$Enabled$WithInstrumentationSupport.class */
            public static class WithInstrumentationSupport extends Enabled {
                private final Method isModifiableModule;
                private final Method redefineModule;

                protected WithInstrumentationSupport(Method getModule, Method getClassLoader, Method isNamed, Method getName, Method getResourceAsStream, Method isExported, Method isOpened, Method canRead, Method isModifiableModule, Method redefineModule) {
                    super(getModule, getClassLoader, isNamed, getName, getResourceAsStream, isExported, isOpened, canRead);
                    this.isModifiableModule = isModifiableModule;
                    this.redefineModule = redefineModule;
                }

                @Override // net.bytebuddy.utility.JavaModule.Dispatcher
                public void modify(Instrumentation instrumentation, Object source, Set<Object> reads, Map<String, Set<Object>> exports, Map<String, Set<Object>> opens, Set<Class<?>> uses, Map<Class<?>, List<Class<?>>> provides) {
                    try {
                        if (!((Boolean) this.isModifiableModule.invoke(instrumentation, source)).booleanValue()) {
                            throw new IllegalStateException(source + " is not modifiable");
                        }
                        try {
                            this.redefineModule.invoke(instrumentation, source, reads, exports, opens, uses, provides);
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Cannot access " + this.redefineModule, exception);
                        } catch (InvocationTargetException exception2) {
                            throw new IllegalStateException("Cannot invoke " + this.redefineModule, exception2.getCause());
                        }
                    } catch (IllegalAccessException exception3) {
                        throw new IllegalStateException("Cannot access " + this.redefineModule, exception3);
                    } catch (InvocationTargetException exception4) {
                        throw new IllegalStateException("Cannot invoke " + this.redefineModule, exception4.getCause());
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaModule$Dispatcher$Disabled.class */
        public enum Disabled implements Dispatcher {
            INSTANCE;

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isAlive() {
                return false;
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public JavaModule moduleOf(Class<?> type) {
                return JavaModule.UNSUPPORTED;
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public ClassLoader getClassLoader(Object module) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isNamed(Object module) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public String getName(Object module) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public InputStream getResourceAsStream(Object module, String name) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isExported(Object source, Object target, String aPackage) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean isOpened(Object source, Object target, String aPackage) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public boolean canRead(Object source, Object target) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }

            @Override // net.bytebuddy.utility.JavaModule.Dispatcher
            public void modify(Instrumentation instrumentation, Object module, Set<Object> reads, Map<String, Set<Object>> exports, Map<String, Set<Object>> opens, Set<Class<?>> uses, Map<Class<?>, List<Class<?>>> provides) {
                throw new UnsupportedOperationException("Current VM does not support modules");
            }
        }
    }
}
