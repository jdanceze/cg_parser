package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.StringUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/exceptions/verification/ArgumentsAreDifferent.class */
public class ArgumentsAreDifferent extends MockitoAssertionError {
    private static final long serialVersionUID = 1;

    public ArgumentsAreDifferent(String message) {
        super(message);
    }

    public ArgumentsAreDifferent(String message, String wanted, String actual) {
        this(message);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return StringUtil.removeFirstLine(super.toString());
    }
}
