package org.mockito.internal.progress;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.Localized;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.listeners.AutoCleanableListener;
import org.mockito.invocation.Location;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.VerificationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/progress/MockingProgressImpl.class */
public class MockingProgressImpl implements MockingProgress {
    private OngoingStubbing<?> ongoingStubbing;
    private Localized<VerificationMode> verificationMode;
    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();
    private Location stubbingInProgress = null;
    private final Set<MockitoListener> listeners = new LinkedHashSet();
    private VerificationStrategy verificationStrategy = getDefaultVerificationStrategy();

    public static VerificationStrategy getDefaultVerificationStrategy() {
        return new VerificationStrategy() { // from class: org.mockito.internal.progress.MockingProgressImpl.1
            @Override // org.mockito.verification.VerificationStrategy
            public VerificationMode maybeVerifyLazily(VerificationMode mode) {
                return mode;
            }
        };
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void reportOngoingStubbing(OngoingStubbing ongoingStubbing) {
        this.ongoingStubbing = ongoingStubbing;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public OngoingStubbing<?> pullOngoingStubbing() {
        OngoingStubbing<?> temp = this.ongoingStubbing;
        this.ongoingStubbing = null;
        return temp;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public Set<VerificationListener> verificationListeners() {
        LinkedHashSet<VerificationListener> verificationListeners = new LinkedHashSet<>();
        for (MockitoListener listener : this.listeners) {
            if (listener instanceof VerificationListener) {
                verificationListeners.add((VerificationListener) listener);
            }
        }
        return verificationListeners;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void verificationStarted(VerificationMode verify) {
        validateState();
        resetOngoingStubbing();
        this.verificationMode = new Localized<>(verify);
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void resetOngoingStubbing() {
        this.ongoingStubbing = null;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public VerificationMode pullVerificationMode() {
        if (this.verificationMode == null) {
            return null;
        }
        VerificationMode temp = this.verificationMode.getObject();
        this.verificationMode = null;
        return temp;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void stubbingStarted() {
        validateState();
        this.stubbingInProgress = new LocationImpl();
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void validateState() {
        validateMostStuff();
        if (this.stubbingInProgress != null) {
            Location temp = this.stubbingInProgress;
            this.stubbingInProgress = null;
            throw Reporter.unfinishedStubbing(temp);
        }
    }

    private void validateMostStuff() {
        GlobalConfiguration.validate();
        if (this.verificationMode != null) {
            Location location = this.verificationMode.getLocation();
            this.verificationMode = null;
            throw Reporter.unfinishedVerificationException(location);
        }
        getArgumentMatcherStorage().validateState();
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void stubbingCompleted() {
        this.stubbingInProgress = null;
    }

    public String toString() {
        return "ongoingStubbing: " + this.ongoingStubbing + ", verificationMode: " + this.verificationMode + ", stubbingInProgress: " + this.stubbingInProgress;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void reset() {
        this.stubbingInProgress = null;
        this.verificationMode = null;
        getArgumentMatcherStorage().reset();
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public ArgumentMatcherStorage getArgumentMatcherStorage() {
        return this.argumentMatcherStorage;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void mockingStarted(Object mock, MockCreationSettings settings) {
        for (MockitoListener listener : this.listeners) {
            if (listener instanceof MockCreationListener) {
                ((MockCreationListener) listener).onMockCreated(mock, settings);
            }
        }
        validateMostStuff();
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void mockingStarted(Class<?> mock, MockCreationSettings settings) {
        for (MockitoListener listener : this.listeners) {
            if (listener instanceof MockCreationListener) {
                ((MockCreationListener) listener).onStaticMockCreated(mock, settings);
            }
        }
        validateMostStuff();
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void addListener(MockitoListener listener) {
        addListener(listener, this.listeners);
    }

    static void addListener(MockitoListener listener, Set<MockitoListener> listeners) {
        List<MockitoListener> delete = new LinkedList<>();
        for (MockitoListener existing : listeners) {
            if (existing.getClass().equals(listener.getClass())) {
                if ((existing instanceof AutoCleanableListener) && ((AutoCleanableListener) existing).isListenerDirty()) {
                    delete.add(existing);
                } else {
                    Reporter.redundantMockitoListener(listener.getClass().getSimpleName());
                }
            }
        }
        for (MockitoListener toDelete : delete) {
            listeners.remove(toDelete);
        }
        listeners.add(listener);
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void removeListener(MockitoListener listener) {
        this.listeners.remove(listener);
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void setVerificationStrategy(VerificationStrategy strategy) {
        this.verificationStrategy = strategy;
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public VerificationMode maybeVerifyLazily(VerificationMode mode) {
        return this.verificationStrategy.maybeVerifyLazily(mode);
    }

    @Override // org.mockito.internal.progress.MockingProgress
    public void clearListeners() {
        this.listeners.clear();
    }
}
