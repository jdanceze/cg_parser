package org.junit.internal;

import java.util.ArrayList;
import java.util.List;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/ArrayComparisonFailure.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/ArrayComparisonFailure.class */
public class ArrayComparisonFailure extends AssertionError {
    private static final long serialVersionUID = 1;
    private final List<Integer> fIndices = new ArrayList();
    private final String fMessage;
    private final AssertionError fCause;

    public ArrayComparisonFailure(String message, AssertionError cause, int index) {
        this.fMessage = message;
        this.fCause = cause;
        initCause(this.fCause);
        addDimension(index);
    }

    public void addDimension(int index) {
        this.fIndices.add(0, Integer.valueOf(index));
    }

    @Override // java.lang.Throwable
    public synchronized Throwable getCause() {
        return super.getCause() == null ? this.fCause : super.getCause();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (this.fMessage != null) {
            sb.append(this.fMessage);
        }
        sb.append("arrays first differed at element ");
        for (Integer num : this.fIndices) {
            int each = num.intValue();
            sb.append("[");
            sb.append(each);
            sb.append("]");
        }
        sb.append("; ");
        sb.append(getCause().getMessage());
        return sb.toString();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getMessage();
    }
}
