package org.mockito.internal.creation.bytebuddy;

import android.provider.ContactsContract;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Platform;
import org.mockito.internal.util.StringUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/SubclassInjectionLoader.class */
class SubclassInjectionLoader implements SubclassLoader {
    private static final String ERROR_MESSAGE = StringUtil.join("The current JVM does not support any class injection mechanism.", "", "Currently, Mockito supports injection via either by method handle lookups or using sun.misc.Unsafe", "Neither seems to be available on your current JVM.");
    private final SubclassLoader loader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SubclassInjectionLoader() {
        if (!Boolean.getBoolean("org.mockito.internal.noUnsafeInjection") && ClassInjector.UsingReflection.isAvailable()) {
            this.loader = new WithReflection();
        } else if (ClassInjector.UsingLookup.isAvailable()) {
            this.loader = tryLookup();
        } else {
            throw new MockitoException(StringUtil.join(ERROR_MESSAGE, "", Platform.describe()));
        }
    }

    private static SubclassLoader tryLookup() {
        try {
            Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
            Object lookup = methodHandles.getMethod(ContactsContract.ContactsColumns.LOOKUP_KEY, new Class[0]).invoke(null, new Object[0]);
            Method privateLookupIn = methodHandles.getMethod("privateLookupIn", Class.class, Class.forName("java.lang.invoke.MethodHandles$Lookup"));
            Object codegenLookup = privateLookupIn.invoke(null, InjectionBase.class, lookup);
            return new WithLookup(lookup, codegenLookup, privateLookupIn);
        } catch (Exception exception) {
            throw new MockitoException(StringUtil.join(ERROR_MESSAGE, "", Platform.describe()), exception);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/SubclassInjectionLoader$WithReflection.class */
    private static class WithReflection implements SubclassLoader {
        private WithReflection() {
        }

        @Override // org.mockito.internal.creation.bytebuddy.SubclassLoader
        public boolean isDisrespectingOpenness() {
            return true;
        }

        @Override // org.mockito.internal.creation.bytebuddy.SubclassLoader
        public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, ClassLoader classLoader, boolean localMock) {
            ProtectionDomain protectionDomain;
            ClassLoadingStrategy.Default r0 = ClassLoadingStrategy.Default.INJECTION;
            if (localMock) {
                protectionDomain = mockedType.getProtectionDomain();
            } else {
                protectionDomain = InjectionBase.class.getProtectionDomain();
            }
            return r0.with(protectionDomain);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/SubclassInjectionLoader$WithLookup.class */
    public static class WithLookup implements SubclassLoader {
        private final Object lookup;
        private final Object codegenLookup;
        private final Method privateLookupIn;

        WithLookup(Object lookup, Object codegenLookup, Method privateLookupIn) {
            this.lookup = lookup;
            this.codegenLookup = codegenLookup;
            this.privateLookupIn = privateLookupIn;
        }

        @Override // org.mockito.internal.creation.bytebuddy.SubclassLoader
        public boolean isDisrespectingOpenness() {
            return false;
        }

        @Override // org.mockito.internal.creation.bytebuddy.SubclassLoader
        public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, ClassLoader classLoader, boolean localMock) {
            if (localMock) {
                try {
                    try {
                        Object privateLookup = this.privateLookupIn.invoke(null, mockedType, this.lookup);
                        return ClassLoadingStrategy.UsingLookup.of(privateLookup);
                    } catch (InvocationTargetException exception) {
                        if (exception.getCause() instanceof IllegalAccessException) {
                            return ClassLoadingStrategy.Default.WRAPPER.with(mockedType.getProtectionDomain());
                        }
                        throw exception.getCause();
                    }
                } catch (Throwable exception2) {
                    throw new MockitoException(StringUtil.join("The Java module system prevents Mockito from defining a mock class in the same package as " + mockedType, "", "To overcome this, you must open and export the mocked type to Mockito.", "Remember that you can also do so programmatically if the mocked class is defined by the same module as your test code", exception2));
                }
            } else if (classLoader == InjectionBase.class.getClassLoader()) {
                return ClassLoadingStrategy.UsingLookup.of(this.codegenLookup);
            } else {
                return ClassLoadingStrategy.Default.WRAPPER.with(mockedType.getProtectionDomain());
            }
        }
    }

    @Override // org.mockito.internal.creation.bytebuddy.SubclassLoader
    public boolean isDisrespectingOpenness() {
        return this.loader.isDisrespectingOpenness();
    }

    @Override // org.mockito.internal.creation.bytebuddy.SubclassLoader
    public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, ClassLoader classLoader, boolean localMock) {
        return this.loader.resolveStrategy(mockedType, classLoader, localMock);
    }
}
