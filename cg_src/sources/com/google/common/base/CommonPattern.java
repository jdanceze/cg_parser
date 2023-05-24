package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/CommonPattern.class */
public abstract class CommonPattern {
    public abstract CommonMatcher matcher(CharSequence charSequence);

    public abstract String pattern();

    public abstract int flags();

    public abstract String toString();

    public static CommonPattern compile(String pattern) {
        return Platform.compilePattern(pattern);
    }

    public static boolean isPcreLike() {
        return Platform.patternCompilerIsPcreLike();
    }
}
