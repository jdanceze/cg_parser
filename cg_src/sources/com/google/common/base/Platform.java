package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.ServiceConfigurationError;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Platform.class */
public final class Platform {
    private static final Logger logger = Logger.getLogger(Platform.class.getName());
    private static final PatternCompiler patternCompiler = loadPatternCompiler();

    private Platform() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long systemNanoTime() {
        return System.nanoTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
        return matcher.precomputedInternal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
        WeakReference<? extends Enum<?>> ref = Enums.getEnumConstants(enumClass).get(value);
        return ref == null ? Optional.absent() : Optional.of(enumClass.cast(ref.get()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatCompact4Digits(double value) {
        return String.format(Locale.ROOT, "%.4g", Double.valueOf(value));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean stringIsNullOrEmpty(@NullableDecl String string) {
        return string == null || string.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String nullToEmpty(@NullableDecl String string) {
        return string == null ? "" : string;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String emptyToNull(@NullableDecl String string) {
        if (stringIsNullOrEmpty(string)) {
            return null;
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CommonPattern compilePattern(String pattern) {
        Preconditions.checkNotNull(pattern);
        return patternCompiler.compile(pattern);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean patternCompilerIsPcreLike() {
        return patternCompiler.isPcreLike();
    }

    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }

    private static void logPatternCompilerError(ServiceConfigurationError e) {
        logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", (Throwable) e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Platform$JdkPatternCompiler.class */
    public static final class JdkPatternCompiler implements PatternCompiler {
        private JdkPatternCompiler() {
        }

        @Override // com.google.common.base.PatternCompiler
        public CommonPattern compile(String pattern) {
            return new JdkPattern(Pattern.compile(pattern));
        }

        @Override // com.google.common.base.PatternCompiler
        public boolean isPcreLike() {
            return true;
        }
    }
}
