package org.powermock.api.mockito.mockmaker;

import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.powermock.api.mockito.invocation.MockitoMethodInvocationControl;
import org.powermock.configuration.GlobalConfiguration;
import org.powermock.core.MockRepository;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/mockmaker/PowerMockMaker.class */
public class PowerMockMaker implements MockMaker {
    private final MockMaker mockMaker = new MockMakerLoader().load(GlobalConfiguration.mockitoConfiguration());

    @Override // org.mockito.plugins.MockMaker
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return (T) this.mockMaker.createMock(settings, handler);
    }

    @Override // org.mockito.plugins.MockMaker
    public MockHandler getHandler(Object mock) {
        if (mock instanceof Class) {
            return staticMockHandler((Class) mock);
        }
        return instanceMockHandler(mock);
    }

    private MockHandler instanceMockHandler(Object mock) {
        return this.mockMaker.getHandler(getRealMock(mock));
    }

    private Object getRealMock(Object mock) {
        Object realMock;
        MockitoMethodInvocationControl invocationControl = (MockitoMethodInvocationControl) MockRepository.getInstanceMethodInvocationControl(mock);
        if (invocationControl == null) {
            realMock = mock;
        } else {
            realMock = invocationControl.getMockHandlerAdaptor().getMock();
        }
        return realMock;
    }

    private MockHandler staticMockHandler(Class mock) {
        return new StaticMockHandler(createStaticMockSettings(mock));
    }

    @Override // org.mockito.plugins.MockMaker
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        this.mockMaker.resetMock(mock, newHandler, settings);
    }

    @Override // org.mockito.plugins.MockMaker
    public MockMaker.TypeMockability isTypeMockable(Class<?> type) {
        return this.mockMaker.isTypeMockable(type);
    }

    MockMaker getMockMaker() {
        return this.mockMaker;
    }

    private MockCreationSettings<Class> createStaticMockSettings(Class mock) {
        return Mockito.withSettings().name(mock.getName()).build(mock);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/mockmaker/PowerMockMaker$StaticMockHandler.class */
    public static class StaticMockHandler implements MockHandler<Class> {
        private final MockCreationSettings<Class> mockSettings;

        private StaticMockHandler(MockCreationSettings<Class> mockSettings) {
            this.mockSettings = mockSettings;
        }

        @Override // org.mockito.invocation.MockHandler
        public MockCreationSettings<Class> getMockSettings() {
            return this.mockSettings;
        }

        @Override // org.mockito.invocation.MockHandler
        public InvocationContainer getInvocationContainer() {
            return null;
        }

        @Override // org.mockito.invocation.MockHandler
        public Object handle(Invocation invocation) throws Throwable {
            return null;
        }
    }
}
