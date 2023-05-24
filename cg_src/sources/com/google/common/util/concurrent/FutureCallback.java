package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/util/concurrent/FutureCallback.class */
public interface FutureCallback<V> {
    void onSuccess(@NullableDecl V v);

    void onFailure(Throwable th);
}
