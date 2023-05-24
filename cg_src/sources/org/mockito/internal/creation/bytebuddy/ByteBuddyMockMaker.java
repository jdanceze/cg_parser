package org.mockito.internal.creation.bytebuddy;

import java.util.Optional;
import java.util.function.Function;
import org.mockito.Incubating;
import org.mockito.MockedConstruction;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ByteBuddyMockMaker.class */
public class ByteBuddyMockMaker implements ClassCreatingMockMaker {
    private ClassCreatingMockMaker defaultByteBuddyMockMaker = new SubclassByteBuddyMockMaker();

    @Override // org.mockito.plugins.MockMaker
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return (T) this.defaultByteBuddyMockMaker.createMock(settings, handler);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> Optional<T> createSpy(MockCreationSettings<T> settings, MockHandler handler, T object) {
        return this.defaultByteBuddyMockMaker.createSpy(settings, handler, object);
    }

    @Override // org.mockito.internal.creation.bytebuddy.ClassCreatingMockMaker
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> creationSettings) {
        return this.defaultByteBuddyMockMaker.createMockType(creationSettings);
    }

    @Override // org.mockito.plugins.MockMaker
    public MockHandler getHandler(Object mock) {
        return this.defaultByteBuddyMockMaker.getHandler(mock);
    }

    @Override // org.mockito.plugins.MockMaker
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        this.defaultByteBuddyMockMaker.resetMock(mock, newHandler, settings);
    }

    @Override // org.mockito.plugins.MockMaker
    @Incubating
    public MockMaker.TypeMockability isTypeMockable(Class<?> type) {
        return this.defaultByteBuddyMockMaker.isTypeMockable(type);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> MockMaker.StaticMockControl<T> createStaticMock(Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        return this.defaultByteBuddyMockMaker.createStaticMock(type, settings, handler);
    }

    @Override // org.mockito.plugins.MockMaker
    public <T> MockMaker.ConstructionMockControl<T> createConstructionMock(Class<T> type, Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory, Function<MockedConstruction.Context, MockHandler<T>> handlerFactory, MockedConstruction.MockInitializer<T> mockInitializer) {
        return this.defaultByteBuddyMockMaker.createConstructionMock(type, settingsFactory, handlerFactory, mockInitializer);
    }
}
