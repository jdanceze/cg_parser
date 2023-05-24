package org.mockito.internal.progress;

import java.util.Set;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.VerificationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/progress/MockingProgress.class */
public interface MockingProgress {
    void reportOngoingStubbing(OngoingStubbing<?> ongoingStubbing);

    OngoingStubbing<?> pullOngoingStubbing();

    Set<VerificationListener> verificationListeners();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();

    void validateState();

    void reset();

    void resetOngoingStubbing();

    ArgumentMatcherStorage getArgumentMatcherStorage();

    void mockingStarted(Object obj, MockCreationSettings mockCreationSettings);

    void mockingStarted(Class<?> cls, MockCreationSettings mockCreationSettings);

    void addListener(MockitoListener mockitoListener);

    void removeListener(MockitoListener mockitoListener);

    void setVerificationStrategy(VerificationStrategy verificationStrategy);

    VerificationMode maybeVerifyLazily(VerificationMode verificationMode);

    void clearListeners();
}
