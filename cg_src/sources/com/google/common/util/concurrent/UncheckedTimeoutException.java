package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/UncheckedTimeoutException.class */
public class UncheckedTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public UncheckedTimeoutException() {
    }

    public UncheckedTimeoutException(@NullableDecl String message) {
        super(message);
    }

    public UncheckedTimeoutException(@NullableDecl Throwable cause) {
        super(cause);
    }

    public UncheckedTimeoutException(@NullableDecl String message, @NullableDecl Throwable cause) {
        super(message, cause);
    }
}
