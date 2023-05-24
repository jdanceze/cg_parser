package org.mockito.internal;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.listeners.VerificationStartedNotifier;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.DefaultLenientStubber;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/MockitoCore.class */
public class MockitoCore {
    public boolean isTypeMockable(Class<?> typeToMock) {
        return MockUtil.typeMockabilityOf(typeToMock).mockable();
    }

    public <T> T mock(Class<T> typeToMock, MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException("Unexpected implementation of '" + settings.getClass().getCanonicalName() + "'\nAt the moment, you cannot provide your own implementations of that class.");
        }
        MockSettingsImpl impl = (MockSettingsImpl) MockSettingsImpl.class.cast(settings);
        MockCreationSettings<T> creationSettings = impl.build(typeToMock);
        T mock = (T) MockUtil.createMock(creationSettings);
        ThreadSafeMockingProgress.mockingProgress().mockingStarted(mock, creationSettings);
        return mock;
    }

    public <T> MockedStatic<T> mockStatic(Class<T> classToMock, MockSettings settings) {
        if (!MockSettingsImpl.class.isInstance(settings)) {
            throw new IllegalArgumentException("Unexpected implementation of '" + settings.getClass().getCanonicalName() + "'\nAt the moment, you cannot provide your own implementations of that class.");
        }
        MockSettingsImpl impl = (MockSettingsImpl) MockSettingsImpl.class.cast(settings);
        MockCreationSettings<T> creationSettings = impl.buildStatic(classToMock);
        MockMaker.StaticMockControl<T> control = MockUtil.createStaticMock(classToMock, creationSettings);
        control.enable();
        ThreadSafeMockingProgress.mockingProgress().mockingStarted((Class<?>) classToMock, (MockCreationSettings) creationSettings);
        return new MockedStaticImpl(control);
    }

    public <T> MockedConstruction<T> mockConstruction(Class<T> typeToMock, Function<MockedConstruction.Context, ? extends MockSettings> settingsFactory, MockedConstruction.MockInitializer<T> mockInitializer) {
        Function<MockedConstruction.Context, MockCreationSettings<T>> creationSettings = context -> {
            MockSettings value = (MockSettings) settingsFactory.apply(context);
            if (!MockSettingsImpl.class.isInstance(value)) {
                throw new IllegalArgumentException("Unexpected implementation of '" + value.getClass().getCanonicalName() + "'\nAt the moment, you cannot provide your own implementations of that class.");
            }
            MockSettingsImpl impl = (MockSettingsImpl) MockSettingsImpl.class.cast(value);
            return impl.build(typeToMock);
        };
        MockMaker.ConstructionMockControl<T> control = MockUtil.createConstructionMock(typeToMock, creationSettings, mockInitializer);
        control.enable();
        return new MockedConstructionImpl(control);
    }

    public <T> OngoingStubbing<T> when(T methodCall) {
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.stubbingStarted();
        OngoingStubbing<T> stubbing = (OngoingStubbing<T>) mockingProgress.pullOngoingStubbing();
        if (stubbing == null) {
            mockingProgress.reset();
            throw Reporter.missingMethodInvocation();
        }
        return stubbing;
    }

    public <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            throw Reporter.nullPassedToVerify();
        }
        MockingDetails mockingDetails = mockingDetails(mock);
        if (!mockingDetails.isMock()) {
            throw Reporter.notAMockPassedToVerify(mock.getClass());
        }
        assertNotStubOnlyMock(mock);
        MockHandler handler = mockingDetails.getMockHandler();
        T mock2 = (T) VerificationStartedNotifier.notifyVerificationStarted(handler.getMockSettings().getVerificationStartedListeners(), mockingDetails);
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        VerificationMode actualMode = mockingProgress.maybeVerifyLazily(mode);
        mockingProgress.verificationStarted(new MockAwareVerificationMode(mock2, actualMode, mockingProgress.verificationListeners()));
        return mock2;
    }

    public <T> void reset(T... mocks) {
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        for (T m : mocks) {
            MockUtil.resetMock(m);
        }
    }

    public <T> void clearInvocations(T... mocks) {
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        for (T m : mocks) {
            MockUtil.getInvocationContainer(m).clearInvocations();
        }
    }

    public void verifyNoMoreInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        ThreadSafeMockingProgress.mockingProgress().validateState();
        for (Object mock : mocks) {
            if (mock == null) {
                throw Reporter.nullPassedToVerifyNoMoreInteractions();
            }
            try {
                InvocationContainerImpl invocations = MockUtil.getInvocationContainer(mock);
                assertNotStubOnlyMock(mock);
                VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                VerificationModeFactory.noMoreInteractions().verify(data);
            } catch (NotAMockException e) {
                throw Reporter.notAMockPassedToVerifyNoMoreInteractions();
            }
            throw Reporter.notAMockPassedToVerifyNoMoreInteractions();
        }
    }

    public void verifyNoInteractions(Object... mocks) {
        assertMocksNotEmpty(mocks);
        ThreadSafeMockingProgress.mockingProgress().validateState();
        for (Object mock : mocks) {
            if (mock == null) {
                throw Reporter.nullPassedToVerifyNoMoreInteractions();
            }
            try {
                InvocationContainerImpl invocations = MockUtil.getInvocationContainer(mock);
                assertNotStubOnlyMock(mock);
                VerificationDataImpl data = new VerificationDataImpl(invocations, null);
                VerificationModeFactory.noInteractions().verify(data);
            } catch (NotAMockException e) {
                throw Reporter.notAMockPassedToVerifyNoMoreInteractions();
            }
            throw Reporter.notAMockPassedToVerifyNoMoreInteractions();
        }
    }

    public void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
        ThreadSafeMockingProgress.mockingProgress().validateState();
        VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrderContext, VerifiableInvocationsFinder.find(mocks), null);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
    }

    private void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            throw Reporter.mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }

    private void assertNotStubOnlyMock(Object mock) {
        if (MockUtil.getMockHandler(mock).getMockSettings().isStubOnly()) {
            throw Reporter.stubPassedToVerify(mock);
        }
    }

    public InOrder inOrder(Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            throw Reporter.mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (Object mock : mocks) {
            if (mock == null) {
                throw Reporter.nullPassedWhenCreatingInOrder();
            }
            if (!MockUtil.isMock(mock)) {
                throw Reporter.notAMockPassedWhenCreatingInOrder();
            }
            assertNotStubOnlyMock(mock);
        }
        return new InOrderImpl(Arrays.asList(mocks));
    }

    public Stubber stubber() {
        return stubber(null);
    }

    public Stubber stubber(Strictness strictness) {
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        return new StubberImpl(strictness);
    }

    public void validateMockitoUsage() {
        ThreadSafeMockingProgress.mockingProgress().validateState();
    }

    public Invocation getLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = (OngoingStubbingImpl) ThreadSafeMockingProgress.mockingProgress().pullOngoingStubbing();
        List<Invocation> allInvocations = ongoingStubbing.getRegisteredInvocations();
        return allInvocations.get(allInvocations.size() - 1);
    }

    public Object[] ignoreStubs(Object... mocks) {
        for (Object m : mocks) {
            InvocationContainerImpl container = MockUtil.getInvocationContainer(m);
            List<Invocation> ins = container.getInvocations();
            for (Invocation in : ins) {
                if (in.stubInfo() != null) {
                    in.ignoreForVerification();
                }
            }
        }
        return mocks;
    }

    public MockingDetails mockingDetails(Object toInspect) {
        return new DefaultMockingDetails(toInspect);
    }

    public LenientStubber lenient() {
        return new DefaultLenientStubber();
    }
}
