package org.mockito.internal.verification;

import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/VerificationModeFactory.class */
public class VerificationModeFactory {
    public static VerificationMode atLeastOnce() {
        return atLeast(1);
    }

    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return new AtLeast(minNumberOfInvocations);
    }

    public static VerificationMode only() {
        return new Only();
    }

    public static Times times(int wantedNumberOfInvocations) {
        return new Times(wantedNumberOfInvocations);
    }

    public static Calls calls(int wantedNumberOfInvocations) {
        return new Calls(wantedNumberOfInvocations);
    }

    public static NoMoreInteractions noMoreInteractions() {
        return new NoMoreInteractions();
    }

    public static NoInteractions noInteractions() {
        return new NoInteractions();
    }

    public static VerificationMode atMostOnce() {
        return atMost(1);
    }

    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return new AtMost(maxNumberOfInvocations);
    }

    public static VerificationMode description(VerificationMode mode, String description) {
        return new Description(mode, description);
    }
}
