package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/PatternCompiler.class */
public interface PatternCompiler {
    CommonPattern compile(String str);

    boolean isPcreLike();
}
