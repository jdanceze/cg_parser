package org.mockito.internal.util;

import java.util.function.Function;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerFactory;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockResolver;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/MockUtil.class */
public class MockUtil {
    private static final MockMaker mockMaker = Plugins.getMockMaker();

    private MockUtil() {
    }

    public static MockMaker.TypeMockability typeMockabilityOf(Class<?> type) {
        return mockMaker.isTypeMockable(type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T createMock(MockCreationSettings<T> settings) {
        Object createMock;
        MockHandler mockHandler = MockHandlerFactory.createMockHandler(settings);
        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            createMock = mockMaker.createSpy(settings, mockHandler, spiedInstance).orElseGet(() -> {
                Object createMock2 = mockMaker.createMock(settings, mockHandler);
                new LenientCopyTool().copyToMock(spiedInstance, createMock2);
                return createMock2;
            });
        } else {
            createMock = mockMaker.createMock(settings, mockHandler);
        }
        return (T) createMock;
    }

    public static void resetMock(Object mock) {
        MockHandler oldHandler = getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        MockHandler newHandler = MockHandlerFactory.createMockHandler(settings);
        mockMaker.resetMock(resolve(mock), newHandler, settings);
    }

    public static MockHandler<?> getMockHandler(Object mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }
        Object mock2 = resolve(mock);
        MockHandler handler = mockMaker.getHandler(mock2);
        if (handler != null) {
            return handler;
        }
        throw new NotAMockException("Argument should be a mock, but is: " + mock2.getClass());
    }

    public static InvocationContainerImpl getInvocationContainer(Object mock) {
        return (InvocationContainerImpl) getMockHandler(mock).getInvocationContainer();
    }

    public static boolean isSpy(Object mock) {
        return isMock(mock) && getMockSettings(mock).getDefaultAnswer() == Mockito.CALLS_REAL_METHODS;
    }

    public static boolean isMock(Object mock) {
        if (mock == null) {
            return false;
        }
        return mockMaker.getHandler(resolve(mock)) != null;
    }

    private static Object resolve(Object mock) {
        if (mock instanceof Class) {
            return mock;
        }
        for (MockResolver mockResolver : Plugins.getMockResolvers()) {
            mock = mockResolver.resolve(mock);
        }
        return mock;
    }

    public static MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public static void maybeRedefineMockName(Object mock, String newName) {
        MockName mockName = getMockName(mock);
        MockCreationSettings mockSettings = getMockHandler(mock).getMockSettings();
        if (mockName.isDefault() && (mockSettings instanceof CreationSettings)) {
            ((CreationSettings) mockSettings).setMockName(new MockNameImpl(newName));
        }
    }

    public static MockCreationSettings getMockSettings(Object mock) {
        return getMockHandler(mock).getMockSettings();
    }

    public static <T> MockMaker.StaticMockControl<T> createStaticMock(Class<T> type, MockCreationSettings<T> settings) {
        MockHandler<T> handler = MockHandlerFactory.createMockHandler(settings);
        return mockMaker.createStaticMock(type, settings, handler);
    }

    public static <T> MockMaker.ConstructionMockControl<T> createConstructionMock(Class<T> type, Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory, MockedConstruction.MockInitializer<T> mockInitializer) {
        Function<MockedConstruction.Context, MockHandler<T>> handlerFactory = context -> {
            return MockHandlerFactory.createMockHandler((MockCreationSettings) settingsFactory.apply(context));
        };
        return mockMaker.createConstructionMock(type, settingsFactory, handlerFactory, mockInitializer);
    }
}
