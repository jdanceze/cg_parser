package org.mockito.internal.creation.bytebuddy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.matcher.ElementMatchers;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.StringUtil;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ModuleHandler.class */
public abstract class ModuleHandler {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isOpened(Class<?> cls, Class<?> cls2);

    abstract boolean canRead(Class<?> cls, Class<?> cls2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isExported(Class<?> cls);

    abstract boolean isExported(Class<?> cls, Class<?> cls2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Class<?> injectionBase(ClassLoader classLoader, String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void adjustModuleGraph(Class<?> cls, Class<?> cls2, boolean z, boolean z2);

    ModuleHandler() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ModuleHandler make(ByteBuddy byteBuddy, SubclassLoader loader, Random random) {
        try {
            return new ModuleSystemFound(byteBuddy, loader, random);
        } catch (Exception e) {
            return new NoModuleSystemFound();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ModuleHandler$ModuleSystemFound.class */
    public static class ModuleSystemFound extends ModuleHandler {
        private final ByteBuddy byteBuddy;
        private final SubclassLoader loader;
        private final Random random;
        private final int injectonBaseSuffix;
        private final Method getModule;
        private final Method isOpen;
        private final Method isExported;
        private final Method isExportedUnqualified;
        private final Method canRead;
        private final Method addExports;
        private final Method addReads;
        private final Method addOpens;
        private final Method forName;

        private ModuleSystemFound(ByteBuddy byteBuddy, SubclassLoader loader, Random random) throws Exception {
            this.byteBuddy = byteBuddy;
            this.loader = loader;
            this.random = random;
            this.injectonBaseSuffix = Math.abs(random.nextInt());
            Class<?> moduleType = Class.forName("java.lang.Module");
            this.getModule = Class.class.getMethod("getModule", new Class[0]);
            this.isOpen = moduleType.getMethod("isOpen", String.class, moduleType);
            this.isExported = moduleType.getMethod("isExported", String.class, moduleType);
            this.isExportedUnqualified = moduleType.getMethod("isExported", String.class);
            this.canRead = moduleType.getMethod("canRead", moduleType);
            this.addExports = moduleType.getMethod("addExports", String.class, moduleType);
            this.addReads = moduleType.getMethod("addReads", moduleType);
            this.addOpens = moduleType.getMethod("addOpens", String.class, moduleType);
            this.forName = Class.class.getMethod("forName", String.class);
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean isOpened(Class<?> source, Class<?> target) {
            if (source.getPackage() == null) {
                return true;
            }
            return ((Boolean) invoke(this.isOpen, invoke(this.getModule, source, new Object[0]), source.getPackage().getName(), invoke(this.getModule, target, new Object[0]))).booleanValue();
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean canRead(Class<?> source, Class<?> target) {
            return ((Boolean) invoke(this.canRead, invoke(this.getModule, source, new Object[0]), invoke(this.getModule, target, new Object[0]))).booleanValue();
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean isExported(Class<?> source) {
            if (source.getPackage() == null) {
                return true;
            }
            return ((Boolean) invoke(this.isExportedUnqualified, invoke(this.getModule, source, new Object[0]), source.getPackage().getName())).booleanValue();
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean isExported(Class<?> source, Class<?> target) {
            if (source.getPackage() == null) {
                return true;
            }
            return ((Boolean) invoke(this.isExported, invoke(this.getModule, source, new Object[0]), source.getPackage().getName(), invoke(this.getModule, target, new Object[0]))).booleanValue();
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        Class<?> injectionBase(ClassLoader classLoader, String typeName) {
            Class<?> type;
            String packageName = typeName.substring(0, typeName.lastIndexOf(46));
            if (classLoader == InjectionBase.class.getClassLoader() && InjectionBase.class.getPackage().getName().equals(packageName)) {
                return InjectionBase.class;
            }
            synchronized (this) {
                int suffix = this.injectonBaseSuffix;
                while (true) {
                    int i = suffix;
                    suffix++;
                    String name = packageName + "." + InjectionBase.class.getSimpleName() + "$" + i;
                    try {
                        type = Class.forName(name, false, classLoader);
                        if (type.getClassLoader() == classLoader) {
                        }
                    } catch (ClassNotFoundException e) {
                        return this.byteBuddy.subclass(Object.class, (ConstructorStrategy) ConstructorStrategy.Default.NO_CONSTRUCTORS).name(name).make().load(classLoader, this.loader.resolveStrategy(InjectionBase.class, classLoader, false)).getLoaded();
                    }
                }
            }
            return type;
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
            boolean targetVisible;
            MethodCall targetLookup;
            Implementation.Composable implementation;
            boolean needsExport = export && !isExported(source, target);
            boolean needsRead = read && !canRead(source, target);
            if (!needsExport && !needsRead) {
                return;
            }
            ClassLoader classLoader = source.getClassLoader();
            if (classLoader == null) {
                throw new MockitoException(StringUtil.join("Cannot adjust module graph for modules in the bootstrap loader", "", source + " is declared by the bootstrap loader and cannot be adjusted", "Requires package export to " + target + ": " + needsExport, "Requires adjusted reading of " + target + ": " + needsRead));
            }
            boolean z = classLoader == target.getClassLoader();
            while (true) {
                targetVisible = z;
                if (targetVisible || classLoader == null) {
                    break;
                }
                classLoader = classLoader.getParent();
                z = classLoader == target.getClassLoader();
            }
            if (targetVisible) {
                targetLookup = MethodCall.invoke(this.getModule).onMethodCall(MethodCall.invoke(this.forName).with(target.getName()));
                implementation = StubMethod.INSTANCE;
            } else {
                try {
                    Class<?> intermediate = this.byteBuddy.subclass(Object.class, (ConstructorStrategy) ConstructorStrategy.Default.NO_CONSTRUCTORS).name(String.format("%s$%d", "org.mockito.codegen.MockitoTypeCarrier", Integer.valueOf(Math.abs(this.random.nextInt())))).defineField("mockitoType", Class.class, Visibility.PUBLIC, Ownership.STATIC).make().load(source.getClassLoader(), this.loader.resolveStrategy(source, source.getClassLoader(), false)).getLoaded();
                    Field field = intermediate.getField("mockitoType");
                    field.set(null, target);
                    targetLookup = MethodCall.invoke(this.getModule).onField(field);
                    implementation = MethodCall.invoke(this.getModule).onMethodCall(MethodCall.invoke(this.forName).with(intermediate.getName()));
                } catch (Exception e) {
                    throw new MockitoException(StringUtil.join("Could not create a carrier for making the Mockito type visible to " + source, "", "This is required to adjust the module graph to enable mock creation"), e);
                }
            }
            MethodCall sourceLookup = MethodCall.invoke(this.getModule).onMethodCall(MethodCall.invoke(this.forName).with(source.getName()));
            if (needsExport) {
                implementation = implementation.andThen(MethodCall.invoke(this.addExports).onMethodCall(sourceLookup).with(target.getPackage().getName()).withMethodCall(targetLookup));
            }
            if (needsRead) {
                implementation = implementation.andThen((Implementation.Composable) MethodCall.invoke(this.addReads).onMethodCall(sourceLookup).withMethodCall(targetLookup));
            }
            try {
                Class.forName(this.byteBuddy.subclass(Object.class).name(String.format("%s$%s$%d", source.getName(), "MockitoModuleProbe", Integer.valueOf(Math.abs(this.random.nextInt())))).invokable(ElementMatchers.isTypeInitializer()).intercept(implementation).make().load(source.getClassLoader(), this.loader.resolveStrategy(source, source.getClassLoader(), false)).getLoaded().getName(), true, source.getClassLoader());
            } catch (Exception e2) {
                throw new MockitoException(StringUtil.join("Could not force module adjustment of the module of " + source, "", "This is required to adjust the module graph to enable mock creation"), e2);
            }
        }

        private static Object invoke(Method method, Object target, Object... args) {
            try {
                return method.invoke(target, args);
            } catch (Exception e) {
                throw new MockitoException(StringUtil.join("Could not invoke " + method + " using reflection", "", "Mockito attempted to interact with the Java module system but an unexpected method behavior was encountered"), e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ModuleHandler$NoModuleSystemFound.class */
    public static class NoModuleSystemFound extends ModuleHandler {
        private NoModuleSystemFound() {
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean isOpened(Class<?> source, Class<?> target) {
            return true;
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean canRead(Class<?> source, Class<?> target) {
            return true;
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean isExported(Class<?> source) {
            return true;
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        boolean isExported(Class<?> source, Class<?> target) {
            return true;
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        Class<?> injectionBase(ClassLoader classLoader, String tyoeName) {
            return InjectionBase.class;
        }

        @Override // org.mockito.internal.creation.bytebuddy.ModuleHandler
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
        }
    }
}
