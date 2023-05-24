package org.powermock.api.mockito.internal.mockcreation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.powermock.api.mockito.invocation.MockitoMethodInvocationControl;
import org.powermock.core.ClassReplicaCreator;
import org.powermock.core.DefaultFieldValueGenerator;
import org.powermock.core.MockRepository;
import org.powermock.core.classloader.MockClassLoader;
import org.powermock.reflect.Whitebox;
import org.powermock.utils.Asserts;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/DefaultMockCreator.class */
public class DefaultMockCreator extends AbstractMockCreator {
    private static final DefaultMockCreator MOCK_CREATOR = new DefaultMockCreator();

    public static <T> T mock(Class<T> type, boolean isStatic, boolean isSpy, Object delegator, MockSettings mockSettings, Method... methods) {
        return (T) MOCK_CREATOR.createMock(type, isStatic, isSpy, delegator, mockSettings, methods);
    }

    @Override // org.powermock.api.mockito.internal.mockcreation.MockCreator
    public <T> T createMock(Class<T> type, boolean isStatic, boolean isSpy, Object delegatorCandidate, MockSettings mockSettings, Method... methods) {
        Asserts.assertNotNull(type, "The class to mock cannot be null");
        validateType(type, isStatic, isSpy);
        registerAfterMethodRunner();
        return (T) doCreateMock(type, isStatic, isSpy, delegatorCandidate, mockSettings, methods);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T doCreateMock(Class<T> type, boolean isStatic, boolean isSpy, Object delegatorCandidate, MockSettings mockSettings, Method[] methods) {
        Class<T> typeToMock = getMockType(type);
        Object delegator = (isSpy && delegatorCandidate == null) ? new Object() : delegatorCandidate;
        MockData<T> mockData = createMethodInvocationControl(typeToMock, methods, delegator, mockSettings);
        T mock = mockData.getMock();
        if (isFinalJavaSystemClass(type) && !isStatic) {
            mock = Whitebox.newInstance(type);
            DefaultFieldValueGenerator.fillWithDefaultValues(mock);
        }
        putMethodInvocationControlToRepository(type, isStatic, mockData, mock);
        return mock;
    }

    private void registerAfterMethodRunner() {
        MockRepository.addAfterMethodRunner(new Runnable() { // from class: org.powermock.api.mockito.internal.mockcreation.DefaultMockCreator.1
            @Override // java.lang.Runnable
            public void run() {
                Mockito.reset(new Object[0]);
            }
        });
    }

    private <T> void putMethodInvocationControlToRepository(Class<T> type, boolean isStatic, MockData<T> mockData, T mock) {
        if (isStatic) {
            MockRepository.putStaticMethodInvocationControl(type, mockData.getMethodInvocationControl());
        } else {
            MockRepository.putInstanceMethodInvocationControl(mock, mockData.getMethodInvocationControl());
        }
    }

    private <T> Class<T> getMockType(Class<T> type) {
        Class<T> typeToMock;
        if (isFinalJavaSystemClass(type)) {
            typeToMock = new ClassReplicaCreator().createClassReplica(type);
        } else {
            typeToMock = type;
        }
        return typeToMock;
    }

    private static <T> boolean isFinalJavaSystemClass(Class<T> type) {
        return type.getName().startsWith("java.") && Modifier.isFinal(type.getModifiers());
    }

    private <T> MockData<T> createMethodInvocationControl(Class<T> type, Method[] methods, Object delegator, MockSettings mockSettings) {
        Object mock = Mockito.mock(type, mockSettings != null ? mockSettings : Mockito.withSettings());
        cacheMockClass(mock.getClass());
        return new MockData<>(new MockitoMethodInvocationControl(delegator, mock, methods), mock);
    }

    private void cacheMockClass(Class<?> mockClass) {
        ClassLoader classLoader = mockClass.getClassLoader();
        if (classLoader instanceof MockClassLoader) {
            MockClassLoader mcl = (MockClassLoader) classLoader;
            mcl.cache(mockClass);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/DefaultMockCreator$MockData.class */
    public static class MockData<T> {
        private final MockitoMethodInvocationControl methodInvocationControl;
        private final T mock;

        private MockData(MockitoMethodInvocationControl methodInvocationControl, T mock) {
            this.methodInvocationControl = methodInvocationControl;
            this.mock = mock;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MockitoMethodInvocationControl getMethodInvocationControl() {
            return this.methodInvocationControl;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public T getMock() {
            return this.mock;
        }
    }
}
