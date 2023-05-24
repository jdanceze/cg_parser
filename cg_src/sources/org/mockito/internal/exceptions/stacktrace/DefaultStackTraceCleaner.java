package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/stacktrace/DefaultStackTraceCleaner.class */
public class DefaultStackTraceCleaner implements StackTraceCleaner {
    @Override // org.mockito.exceptions.stacktrace.StackTraceCleaner
    public boolean isIn(StackTraceElement e) {
        if (isFromMockitoRunner(e.getClassName()) || isFromMockitoRule(e.getClassName())) {
            return true;
        }
        if (isMockDispatcher(e.getClassName()) || isFromMockito(e.getClassName())) {
            return false;
        }
        return true;
    }

    private static boolean isMockDispatcher(String className) {
        return className.contains("$$EnhancerByMockitoWithCGLIB$$") || className.contains("$MockitoMock$");
    }

    private static boolean isFromMockito(String className) {
        return className.startsWith("org.mockito.");
    }

    private static boolean isFromMockitoRule(String className) {
        return className.startsWith("org.mockito.internal.junit.JUnitRule");
    }

    private static boolean isFromMockitoRunner(String className) {
        return className.startsWith("org.mockito.internal.runners.") || className.startsWith("org.mockito.runners.") || className.startsWith("org.mockito.junit.");
    }
}
