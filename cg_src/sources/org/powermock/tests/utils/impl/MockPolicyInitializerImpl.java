package org.powermock.tests.utils.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import org.powermock.core.MockRepository;
import org.powermock.core.classloader.MockClassLoader;
import org.powermock.core.classloader.MockClassLoaderConfiguration;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.spi.PowerMockPolicy;
import org.powermock.mockpolicies.MockPolicyClassLoadingSettings;
import org.powermock.mockpolicies.MockPolicyInterceptionSettings;
import org.powermock.mockpolicies.impl.MockPolicyClassLoadingSettingsImpl;
import org.powermock.mockpolicies.impl.MockPolicyInterceptionSettingsImpl;
import org.powermock.reflect.Whitebox;
import org.powermock.tests.utils.MockPolicyInitializer;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/MockPolicyInitializerImpl.class */
public class MockPolicyInitializerImpl implements MockPolicyInitializer {
    private final PowerMockPolicy[] mockPolicies;
    private final Class<? extends PowerMockPolicy>[] mockPolicyTypes;
    private final Class<?> testClass;

    public MockPolicyInitializerImpl(Class<? extends PowerMockPolicy>[] mockPolicies) {
        this(mockPolicies, false);
    }

    public MockPolicyInitializerImpl(Class<?> testClass) {
        this(getMockPolicies(testClass), testClass, false);
    }

    private MockPolicyInitializerImpl(Class<? extends PowerMockPolicy>[] mockPolicies, boolean internal) {
        this(mockPolicies, null, internal);
    }

    private MockPolicyInitializerImpl(Class<? extends PowerMockPolicy>[] mockPolicies, Class<?> testClass, boolean internal) {
        this.testClass = testClass;
        if (internal) {
            this.mockPolicyTypes = null;
        } else {
            this.mockPolicyTypes = mockPolicies;
        }
        if (mockPolicies == null) {
            this.mockPolicies = new PowerMockPolicy[0];
            return;
        }
        this.mockPolicies = new PowerMockPolicy[mockPolicies.length];
        for (int i = 0; i < mockPolicies.length; i++) {
            this.mockPolicies[i] = (PowerMockPolicy) Whitebox.newInstance(mockPolicies[i]);
        }
    }

    private static Class<? extends PowerMockPolicy>[] getMockPolicies(Class<?> testClass) {
        Class<? extends PowerMockPolicy>[] powerMockPolicies = new Class[0];
        if (testClass.isAnnotationPresent(MockPolicy.class)) {
            MockPolicy annotation = (MockPolicy) testClass.getAnnotation(MockPolicy.class);
            powerMockPolicies = annotation.value();
        }
        return powerMockPolicies;
    }

    @Override // org.powermock.tests.utils.MockPolicyInitializer
    public boolean isPrepared(String fullyQualifiedClassName) {
        MockPolicyClassLoadingSettings settings = getClassLoadingSettings();
        boolean foundInSuppressStaticInitializer = Arrays.binarySearch(settings.getStaticInitializersToSuppress(), fullyQualifiedClassName) < 0;
        boolean foundClassesLoadedByMockClassloader = Arrays.binarySearch(settings.getFullyQualifiedNamesOfClassesToLoadByMockClassloader(), fullyQualifiedClassName) < 0;
        return foundInSuppressStaticInitializer || foundClassesLoadedByMockClassloader;
    }

    @Override // org.powermock.tests.utils.MockPolicyInitializer
    public boolean needsInitialization() {
        MockPolicyClassLoadingSettings settings = getClassLoadingSettings();
        return settings.getStaticInitializersToSuppress().length > 0 || settings.getFullyQualifiedNamesOfClassesToLoadByMockClassloader().length > 0;
    }

    @Override // org.powermock.tests.utils.MockPolicyInitializer
    public void initialize(ClassLoader classLoader) {
        if (classLoader instanceof MockClassLoader) {
            initialize((MockClassLoader) classLoader);
        }
    }

    private void initialize(MockClassLoader classLoader) {
        if (this.mockPolicies.length > 0) {
            updateClassLoaderConfiguration(classLoader.getConfiguration());
            invokeInitializeInterceptionSettingsFromClassLoader(classLoader);
        }
    }

    private void updateClassLoaderConfiguration(MockClassLoaderConfiguration configuration) {
        String[] staticInitializersToSuppress;
        MockPolicyClassLoadingSettings classLoadingSettings = getClassLoadingSettings();
        String[] fullyQualifiedNamesOfClassesToLoadByMockClassloader = classLoadingSettings.getFullyQualifiedNamesOfClassesToLoadByMockClassloader();
        configuration.addClassesToModify(fullyQualifiedNamesOfClassesToLoadByMockClassloader);
        if (this.testClass == null) {
            throw new IllegalStateException("Internal error: testClass should never be null when calling initialize on a mock policy");
        }
        configuration.addClassesToModify(this.testClass.getName());
        Class<?>[] classes = this.testClass.getDeclaredClasses();
        for (Class<?> clazz : classes) {
            configuration.addClassesToModify(clazz.getName());
        }
        Class<?>[] declaredClasses = this.testClass.getClasses();
        for (Class<?> clazz2 : declaredClasses) {
            configuration.addClassesToModify(clazz2.getName());
        }
        for (String string : classLoadingSettings.getStaticInitializersToSuppress()) {
            configuration.addClassesToModify(string);
            MockRepository.addSuppressStaticInitializer(string);
        }
    }

    @Override // org.powermock.tests.utils.MockPolicyInitializer
    public void refreshPolicies(ClassLoader classLoader) {
        if (classLoader instanceof MockClassLoader) {
            invokeInitializeInterceptionSettingsFromClassLoader((MockClassLoader) classLoader);
        }
    }

    private void invokeInitializeInterceptionSettingsFromClassLoader(MockClassLoader classLoader) {
        try {
            int sizeOfPolicies = this.mockPolicyTypes.length;
            Object mockPolicies = Array.newInstance(Class.class, sizeOfPolicies);
            for (int i = 0; i < sizeOfPolicies; i++) {
                Class<?> policyLoadedByClassLoader = Class.forName(this.mockPolicyTypes[i].getName(), false, classLoader);
                Array.set(mockPolicies, i, policyLoadedByClassLoader);
            }
            Class<?> thisTypeLoadedByMockClassLoader = Class.forName(getClass().getName(), false, classLoader);
            Object mockPolicyHandler = Whitebox.invokeConstructor(thisTypeLoadedByMockClassLoader, mockPolicies, true);
            Whitebox.invokeMethod(mockPolicyHandler, "initializeInterceptionSettings", new Object[0]);
        } catch (RuntimeException e) {
            throw e;
        } catch (InvocationTargetException e2) {
            Throwable targetException = e2.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            throw new RuntimeException(e2);
        } catch (Exception e3) {
            throw new IllegalStateException("PowerMock internal error: Failed to load class.", e3);
        }
    }

    private void initializeInterceptionSettings() {
        Method[] methodsToSuppress;
        Field[] fieldsToSuppress;
        String[] fieldTypesToSuppress;
        MockPolicyInterceptionSettings interceptionSettings = getInterceptionSettings();
        for (Method method : interceptionSettings.getMethodsToSuppress()) {
            MockRepository.addMethodToSuppress(method);
        }
        for (Map.Entry<Method, InvocationHandler> entry : interceptionSettings.getProxiedMethods().entrySet()) {
            MockRepository.putMethodProxy(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Method, Object> entry2 : interceptionSettings.getStubbedMethods().entrySet()) {
            Method method2 = entry2.getKey();
            Object className = entry2.getValue();
            MockRepository.putMethodToStub(method2, className);
        }
        for (Field field : interceptionSettings.getFieldsToSuppress()) {
            MockRepository.addFieldToSuppress(field);
        }
        for (String type : interceptionSettings.getFieldTypesToSuppress()) {
            MockRepository.addFieldTypeToSuppress(type);
        }
    }

    private MockPolicyInterceptionSettings getInterceptionSettings() {
        PowerMockPolicy[] powerMockPolicyArr;
        MockPolicyInterceptionSettings settings = new MockPolicyInterceptionSettingsImpl();
        for (PowerMockPolicy mockPolicy : this.mockPolicies) {
            mockPolicy.applyInterceptionPolicy(settings);
        }
        return settings;
    }

    private MockPolicyClassLoadingSettings getClassLoadingSettings() {
        PowerMockPolicy[] powerMockPolicyArr;
        MockPolicyClassLoadingSettings settings = new MockPolicyClassLoadingSettingsImpl();
        for (PowerMockPolicy mockPolicy : this.mockPolicies) {
            mockPolicy.applyClassLoadingPolicy(settings);
        }
        return settings;
    }
}
