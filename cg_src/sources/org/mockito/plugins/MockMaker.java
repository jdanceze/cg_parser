package org.mockito.plugins;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.mockito.Incubating;
import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.StringUtil;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MockMaker.class */
public interface MockMaker {

    @Incubating
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MockMaker$ConstructionMockControl.class */
    public interface ConstructionMockControl<T> {
        Class<T> getType();

        void enable();

        void disable();

        List<T> getMocks();
    }

    @Incubating
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MockMaker$StaticMockControl.class */
    public interface StaticMockControl<T> {
        Class<T> getType();

        void enable();

        void disable();
    }

    @Incubating
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MockMaker$TypeMockability.class */
    public interface TypeMockability {
        boolean mockable();

        String nonMockableReason();
    }

    <T> T createMock(MockCreationSettings<T> mockCreationSettings, MockHandler mockHandler);

    MockHandler getHandler(Object obj);

    void resetMock(Object obj, MockHandler mockHandler, MockCreationSettings mockCreationSettings);

    @Incubating
    TypeMockability isTypeMockable(Class<?> cls);

    default <T> Optional<T> createSpy(MockCreationSettings<T> settings, MockHandler handler, T instance) {
        return Optional.empty();
    }

    @Incubating
    default <T> StaticMockControl<T> createStaticMock(Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        throw new MockitoException(StringUtil.join("The used MockMaker " + getClass().getSimpleName() + " does not support the creation of static mocks", "", "Mockito's inline mock maker supports static mocks based on the Instrumentation API.", "You can simply enable this mock mode, by placing the 'mockito-inline' artifact where you are currently using 'mockito-core'.", "Note that Mockito's inline mock maker is not supported on Android."));
    }

    @Incubating
    default <T> ConstructionMockControl<T> createConstructionMock(Class<T> type, Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory, Function<MockedConstruction.Context, MockHandler<T>> handlerFactory, MockedConstruction.MockInitializer<T> mockInitializer) {
        throw new MockitoException(StringUtil.join("The used MockMaker " + getClass().getSimpleName() + " does not support the creation of construction mocks", "", "Mockito's inline mock maker supports construction mocks based on the Instrumentation API.", "You can simply enable this mock mode, by placing the 'mockito-inline' artifact where you are currently using 'mockito-core'.", "Note that Mockito's inline mock maker is not supported on Android."));
    }
}
