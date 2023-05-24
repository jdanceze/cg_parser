package org.mockito.internal.progress;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/progress/SequenceNumber.class */
public class SequenceNumber {
    private static int sequenceNumber = 1;

    public static synchronized int next() {
        int i = sequenceNumber;
        sequenceNumber = i + 1;
        return i;
    }
}
