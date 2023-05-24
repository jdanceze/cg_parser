package org.powermock.api.mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.expectation.ConstructorAwareExpectationSetup;
import org.powermock.api.mockito.expectation.ConstructorExpectationSetup;
import org.powermock.api.mockito.expectation.DefaultConstructorExpectationSetup;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.api.mockito.expectation.WithOrWithoutExpectedArguments;
import org.powermock.api.mockito.internal.PowerMockitoCore;
import org.powermock.api.mockito.internal.expectation.DefaultMethodExpectationSetup;
import org.powermock.api.mockito.internal.mockcreation.DefaultMockCreator;
import org.powermock.api.mockito.internal.verification.DefaultPrivateMethodVerification;
import org.powermock.api.mockito.internal.verification.VerifyNoMoreInteractions;
import org.powermock.api.mockito.verification.ConstructorArgumentsVerification;
import org.powermock.api.mockito.verification.PrivateMethodVerification;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/PowerMockito.class */
public class PowerMockito extends MemberModifier {
    private static final PowerMockitoCore POWERMOCKITO_CORE = new PowerMockitoCore();

    public static synchronized void mockStatic(Class<?> type, Class<?>... types) {
        DefaultMockCreator.mock(type, true, false, null, null, null);
        if (types != null && types.length > 0) {
            for (Class<?> aClass : types) {
                DefaultMockCreator.mock(aClass, true, false, null, null, null);
            }
        }
    }

    public static void mockStatic(Class<?> classMock, Answer defaultAnswer) {
        mockStatic(classMock, Mockito.withSettings().defaultAnswer(defaultAnswer));
    }

    public static void mockStatic(Class<?> classToMock, MockSettings mockSettings) {
        DefaultMockCreator.mock(classToMock, true, false, null, mockSettings, null);
    }

    public static synchronized <T> T mock(Class<T> type) {
        return (T) DefaultMockCreator.mock(type, false, false, null, null, null);
    }

    public static <T> T mock(Class<T> classToMock, Answer defaultAnswer) {
        return (T) mock(classToMock, Mockito.withSettings().defaultAnswer(defaultAnswer));
    }

    public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
        return (T) DefaultMockCreator.mock(classToMock, false, false, null, mockSettings, null);
    }

    public static synchronized <T> T spy(T object) {
        return (T) POWERMOCKITO_CORE.spy(object);
    }

    public static synchronized <T> void spy(Class<T> type) {
        MockSettings mockSettings = Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS);
        DefaultMockCreator.mock(type, true, true, type, mockSettings, null);
    }

    public static synchronized <T> void verifyStatic(Class<T> mockedClass) {
        verifyStatic(mockedClass, Mockito.times(1));
    }

    public static synchronized <T> void verifyStatic(Class<T> mockedClass, VerificationMode verificationMode) {
        Mockito.verify(mockedClass, verificationMode);
    }

    public static PrivateMethodVerification verifyPrivate(Object object) {
        return verifyPrivate(object, Mockito.times(1));
    }

    public static PrivateMethodVerification verifyPrivate(Object object, VerificationMode verificationMode) {
        Mockito.verify(object, verificationMode);
        return new DefaultPrivateMethodVerification(object);
    }

    public static PrivateMethodVerification verifyPrivate(Class<?> clazz) throws Exception {
        return verifyPrivate((Object) clazz);
    }

    public static PrivateMethodVerification verifyPrivate(Class<?> clazz, VerificationMode verificationMode) {
        return verifyPrivate((Object) clazz, verificationMode);
    }

    public static synchronized <T> ConstructorArgumentsVerification verifyNew(Class<T> mock) {
        return verifyNew(mock, Mockito.times(1));
    }

    public static <T> ConstructorArgumentsVerification verifyNew(Class<T> mock, VerificationMode mode) {
        return POWERMOCKITO_CORE.verifyNew(mock, mode);
    }

    public static <T> OngoingStubbing<T> when(Object instance, String methodName, Object... arguments) throws Exception {
        return Mockito.when(Whitebox.invokeMethod(instance, methodName, arguments));
    }

    public static <T> WithOrWithoutExpectedArguments<T> when(Object instance, Method method) {
        return new DefaultMethodExpectationSetup(instance, method);
    }

    public static <T> WithOrWithoutExpectedArguments<T> when(Class<?> cls, Method method) {
        return new DefaultMethodExpectationSetup(cls, method);
    }

    public static <T> OngoingStubbing<T> when(Object instance, Object... arguments) throws Exception {
        return Mockito.when(Whitebox.invokeMethod(instance, arguments));
    }

    public static <T> OngoingStubbing<T> when(Class<?> clazz, String methodToExpect, Object... arguments) throws Exception {
        return Mockito.when(Whitebox.invokeMethod(clazz, methodToExpect, arguments));
    }

    public static <T> OngoingStubbing<T> when(Class<?> klass, Object... arguments) throws Exception {
        return Mockito.when(Whitebox.invokeMethod(klass, arguments));
    }

    public static <T> OngoingStubbing<T> when(T methodCall) {
        return Mockito.when(methodCall);
    }

    public static synchronized <T> WithOrWithoutExpectedArguments<T> whenNew(Constructor<T> ctor) {
        return new ConstructorAwareExpectationSetup(ctor);
    }

    public static synchronized <T> ConstructorExpectationSetup<T> whenNew(Class<T> type) {
        return new DefaultConstructorExpectationSetup(type);
    }

    public static synchronized <T> ConstructorExpectationSetup<T> whenNew(String fullyQualifiedName) throws Exception {
        return new DefaultConstructorExpectationSetup(Class.forName(fullyQualifiedName));
    }

    public static void verifyNoMoreInteractions(Object... mocks) {
        VerifyNoMoreInteractions.verifyNoMoreInteractions(mocks);
    }

    public static void verifyZeroInteractions(Object... mocks) {
        VerifyNoMoreInteractions.verifyNoMoreInteractions(mocks);
    }

    public static PowerMockitoStubber doAnswer(Answer<?> answer) {
        return POWERMOCKITO_CORE.doAnswer(answer);
    }

    public static PowerMockitoStubber doThrow(Throwable toBeThrown) {
        return POWERMOCKITO_CORE.doThrow(toBeThrown);
    }

    public static PowerMockitoStubber doCallRealMethod() {
        return POWERMOCKITO_CORE.doCallRealMethod();
    }

    public static PowerMockitoStubber doNothing() {
        return POWERMOCKITO_CORE.doNothing();
    }

    public static PowerMockitoStubber doReturn(Object toBeReturned) {
        return POWERMOCKITO_CORE.doReturn(toBeReturned);
    }

    public static PowerMockitoStubber doReturn(Object toBeReturned, Object... othersToBeReturned) {
        return POWERMOCKITO_CORE.doAnswer(toBeReturned, othersToBeReturned);
    }
}
