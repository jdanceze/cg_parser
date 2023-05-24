package org.mockito;

import java.util.function.Function;
import org.mockito.MockedConstruction;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.debugging.MockitoDebuggerImpl;
import org.mockito.internal.framework.DefaultMockitoFramework;
import org.mockito.internal.session.DefaultMockitoSessionBuilder;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.session.MockitoSessionBuilder;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.After;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationAfterDelay;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationWithTimeout;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/Mockito.class */
public class Mockito extends ArgumentMatchers {
    static final MockitoCore MOCKITO_CORE = new MockitoCore();
    public static final Answer<Object> RETURNS_DEFAULTS = Answers.RETURNS_DEFAULTS;
    public static final Answer<Object> RETURNS_SMART_NULLS = Answers.RETURNS_SMART_NULLS;
    public static final Answer<Object> RETURNS_MOCKS = Answers.RETURNS_MOCKS;
    public static final Answer<Object> RETURNS_DEEP_STUBS = Answers.RETURNS_DEEP_STUBS;
    public static final Answer<Object> CALLS_REAL_METHODS = Answers.CALLS_REAL_METHODS;
    public static final Answer<Object> RETURNS_SELF = Answers.RETURNS_SELF;

    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock) {
        return (T) mock(classToMock, withSettings());
    }

    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock, String name) {
        return (T) mock(classToMock, withSettings().name(name).defaultAnswer(RETURNS_DEFAULTS));
    }

    @CheckReturnValue
    public static MockingDetails mockingDetails(Object toInspect) {
        return MOCKITO_CORE.mockingDetails(toInspect);
    }

    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock, Answer defaultAnswer) {
        return (T) mock(classToMock, withSettings().defaultAnswer(defaultAnswer));
    }

    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
        return (T) MOCKITO_CORE.mock(classToMock, mockSettings);
    }

    @CheckReturnValue
    public static <T> T spy(T object) {
        return (T) MOCKITO_CORE.mock(object.getClass(), withSettings().spiedInstance(object).defaultAnswer(CALLS_REAL_METHODS));
    }

    @CheckReturnValue
    @Incubating
    public static <T> T spy(Class<T> classToSpy) {
        return (T) MOCKITO_CORE.mock(classToSpy, withSettings().useConstructor(new Object[0]).defaultAnswer(CALLS_REAL_METHODS));
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock) {
        return mockStatic(classToMock, withSettings());
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock, Answer defaultAnswer) {
        return mockStatic(classToMock, withSettings().defaultAnswer(defaultAnswer));
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock, String name) {
        return mockStatic(classToMock, withSettings().name(name));
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock, MockSettings mockSettings) {
        return MOCKITO_CORE.mockStatic(classToMock, mockSettings);
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstructionWithAnswer(Class<T> classToMock, Answer defaultAnswer, Answer... additionalAnswers) {
        return mockConstruction(classToMock, context -> {
            if (context.getCount() == 1 || additionalAnswers.length == 0) {
                return withSettings().defaultAnswer(defaultAnswer);
            }
            if (context.getCount() >= additionalAnswers.length) {
                return withSettings().defaultAnswer(additionalAnswers[additionalAnswers.length - 1]);
            }
            return withSettings().defaultAnswer(additionalAnswers[context.getCount() - 2]);
        }, mock, context2 -> {
        });
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstruction(Class<T> classToMock) {
        return mockConstruction(classToMock, index -> {
            return withSettings();
        }, mock, context -> {
        });
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstruction(Class<T> classToMock, MockedConstruction.MockInitializer<T> mockInitializer) {
        return mockConstruction(classToMock, withSettings(), mockInitializer);
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstruction(Class<T> classToMock, MockSettings mockSettings) {
        return mockConstruction(classToMock, context -> {
            return mockSettings;
        });
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstruction(Class<T> classToMock, Function<MockedConstruction.Context, MockSettings> mockSettingsFactory) {
        return mockConstruction(classToMock, mockSettingsFactory, mock, context -> {
        });
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstruction(Class<T> classToMock, MockSettings mockSettings, MockedConstruction.MockInitializer<T> mockInitializer) {
        return mockConstruction(classToMock, index -> {
            return mockSettings;
        }, mockInitializer);
    }

    @CheckReturnValue
    @Incubating
    public static <T> MockedConstruction<T> mockConstruction(Class<T> classToMock, Function<MockedConstruction.Context, MockSettings> mockSettingsFactory, MockedConstruction.MockInitializer<T> mockInitializer) {
        return MOCKITO_CORE.mockConstruction(classToMock, mockSettingsFactory, mockInitializer);
    }

    @CheckReturnValue
    public static <T> OngoingStubbing<T> when(T methodCall) {
        return MOCKITO_CORE.when(methodCall);
    }

    @CheckReturnValue
    public static <T> T verify(T mock) {
        return (T) MOCKITO_CORE.verify(mock, times(1));
    }

    @CheckReturnValue
    public static <T> T verify(T mock, VerificationMode mode) {
        return (T) MOCKITO_CORE.verify(mock, mode);
    }

    public static <T> void reset(T... mocks) {
        MOCKITO_CORE.reset(mocks);
    }

    public static <T> void clearInvocations(T... mocks) {
        MOCKITO_CORE.clearInvocations(mocks);
    }

    public static void verifyNoMoreInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoMoreInteractions(mocks);
    }

    @Deprecated
    public static void verifyZeroInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoMoreInteractions(mocks);
    }

    public static void verifyNoInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoInteractions(mocks);
    }

    @CheckReturnValue
    public static Stubber doThrow(Throwable... toBeThrown) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown);
    }

    @CheckReturnValue
    public static Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown);
    }

    @CheckReturnValue
    public static Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... toBeThrownNext) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown, toBeThrownNext);
    }

    @CheckReturnValue
    public static Stubber doCallRealMethod() {
        return MOCKITO_CORE.stubber().doCallRealMethod();
    }

    @CheckReturnValue
    public static Stubber doAnswer(Answer answer) {
        return MOCKITO_CORE.stubber().doAnswer(answer);
    }

    @CheckReturnValue
    public static Stubber doNothing() {
        return MOCKITO_CORE.stubber().doNothing();
    }

    @CheckReturnValue
    public static Stubber doReturn(Object toBeReturned) {
        return MOCKITO_CORE.stubber().doReturn(toBeReturned);
    }

    @CheckReturnValue
    public static Stubber doReturn(Object toBeReturned, Object... toBeReturnedNext) {
        return MOCKITO_CORE.stubber().doReturn(toBeReturned, toBeReturnedNext);
    }

    @CheckReturnValue
    public static InOrder inOrder(Object... mocks) {
        return MOCKITO_CORE.inOrder(mocks);
    }

    public static Object[] ignoreStubs(Object... mocks) {
        return MOCKITO_CORE.ignoreStubs(mocks);
    }

    @CheckReturnValue
    public static VerificationMode times(int wantedNumberOfInvocations) {
        return VerificationModeFactory.times(wantedNumberOfInvocations);
    }

    @CheckReturnValue
    public static VerificationMode never() {
        return times(0);
    }

    @CheckReturnValue
    public static VerificationMode atLeastOnce() {
        return VerificationModeFactory.atLeastOnce();
    }

    @CheckReturnValue
    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return VerificationModeFactory.atLeast(minNumberOfInvocations);
    }

    @CheckReturnValue
    public static VerificationMode atMostOnce() {
        return VerificationModeFactory.atMostOnce();
    }

    @CheckReturnValue
    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return VerificationModeFactory.atMost(maxNumberOfInvocations);
    }

    @CheckReturnValue
    public static VerificationMode calls(int wantedNumberOfInvocations) {
        return VerificationModeFactory.calls(wantedNumberOfInvocations);
    }

    @CheckReturnValue
    public static VerificationMode only() {
        return VerificationModeFactory.only();
    }

    @CheckReturnValue
    public static VerificationWithTimeout timeout(long millis) {
        return new Timeout(millis, VerificationModeFactory.times(1));
    }

    @CheckReturnValue
    public static VerificationAfterDelay after(long millis) {
        return new After(millis, VerificationModeFactory.times(1));
    }

    public static void validateMockitoUsage() {
        MOCKITO_CORE.validateMockitoUsage();
    }

    @CheckReturnValue
    public static MockSettings withSettings() {
        return new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
    }

    @CheckReturnValue
    public static VerificationMode description(String description) {
        return times(1).description(description);
    }

    @CheckReturnValue
    @Deprecated
    static MockitoDebugger debug() {
        return new MockitoDebuggerImpl();
    }

    @CheckReturnValue
    @Incubating
    public static MockitoFramework framework() {
        return new DefaultMockitoFramework();
    }

    @CheckReturnValue
    @Incubating
    public static MockitoSessionBuilder mockitoSession() {
        return new DefaultMockitoSessionBuilder();
    }

    @Incubating
    public static LenientStubber lenient() {
        return MOCKITO_CORE.lenient();
    }
}
