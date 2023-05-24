package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/UnnecessaryStubbingsReporter.class */
public class UnnecessaryStubbingsReporter implements MockCreationListener {
    private List<Object> mocks = new LinkedList();

    public void validateUnusedStubs(Class<?> testClass, RunNotifier notifier) {
        Collection<Invocation> unused = new UnusedStubbingsFinder().getUnusedStubbingsByLocation(this.mocks);
        if (unused.isEmpty()) {
            return;
        }
        Description unnecessaryStubbings = Description.createTestDescription(testClass, "unnecessary Mockito stubbings");
        notifier.fireTestFailure(new Failure(unnecessaryStubbings, Reporter.formatUnncessaryStubbingException(testClass, unused)));
    }

    @Override // org.mockito.listeners.MockCreationListener
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.add(mock);
    }
}
