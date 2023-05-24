package org.mockito.internal.handler;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.listeners.StubbingLookupNotifier;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.DefaultAnswerValidator;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/handler/MockHandlerImpl.class */
public class MockHandlerImpl<T> implements MockHandler<T> {
    private static final long serialVersionUID = -2917871070982574165L;
    InvocationContainerImpl invocationContainer;
    MatchersBinder matchersBinder;
    private final MockCreationSettings<T> mockSettings;

    public MockHandlerImpl(MockCreationSettings<T> mockSettings) {
        this.matchersBinder = new MatchersBinder();
        this.mockSettings = mockSettings;
        this.matchersBinder = new MatchersBinder();
        this.invocationContainer = new InvocationContainerImpl(mockSettings);
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.mockito.invocation.MockHandler
    public Object handle(Invocation invocation) throws Throwable {
        if (this.invocationContainer.hasAnswersForStubbing()) {
            this.invocationContainer.setMethodForStubbing(this.matchersBinder.bindMatchers(ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage(), invocation));
            return null;
        }
        VerificationMode verificationMode = ThreadSafeMockingProgress.mockingProgress().pullVerificationMode();
        InvocationMatcher invocationMatcher = this.matchersBinder.bindMatchers(ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage(), invocation);
        ThreadSafeMockingProgress.mockingProgress().validateState();
        if (verificationMode != null) {
            if (((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {
                VerificationDataImpl data = new VerificationDataImpl(this.invocationContainer, invocationMatcher);
                verificationMode.verify(data);
                return null;
            }
            ThreadSafeMockingProgress.mockingProgress().verificationStarted(verificationMode);
        }
        this.invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);
        OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<>(this.invocationContainer);
        ThreadSafeMockingProgress.mockingProgress().reportOngoingStubbing(ongoingStubbing);
        StubbedInvocationMatcher stubbing = this.invocationContainer.findAnswerFor(invocation);
        StubbingLookupNotifier.notifyStubbedAnswerLookup(invocation, stubbing, this.invocationContainer.getStubbingsAscending(), (CreationSettings) this.mockSettings);
        if (stubbing != null) {
            stubbing.captureArgumentsFrom(invocation);
            try {
                Object answer = stubbing.answer(invocation);
                ThreadSafeMockingProgress.mockingProgress().reportOngoingStubbing(ongoingStubbing);
                return answer;
            } catch (Throwable th) {
                ThreadSafeMockingProgress.mockingProgress().reportOngoingStubbing(ongoingStubbing);
                throw th;
            }
        }
        Object ret = this.mockSettings.getDefaultAnswer().answer(invocation);
        DefaultAnswerValidator.validateReturnValueFor(invocation, ret);
        this.invocationContainer.resetInvocationForPotentialStubbing(invocationMatcher);
        return ret;
    }

    @Override // org.mockito.invocation.MockHandler
    public MockCreationSettings<T> getMockSettings() {
        return this.mockSettings;
    }

    @Override // org.mockito.invocation.MockHandler
    public InvocationContainer getInvocationContainer() {
        return this.invocationContainer;
    }
}
