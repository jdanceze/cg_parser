package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/io/LineProcessor.class */
public interface LineProcessor<T> {
    @CanIgnoreReturnValue
    boolean processLine(String str) throws IOException;

    T getResult();
}
