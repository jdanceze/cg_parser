package org.powermock.api.mockito.internal.exceptions;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/exceptions/StackTraceCleanerProvider.class */
public class StackTraceCleanerProvider implements org.mockito.plugins.StackTraceCleanerProvider {
    @Override // org.mockito.plugins.StackTraceCleanerProvider
    public StackTraceCleaner getStackTraceCleaner(final StackTraceCleaner defaultCleaner) {
        return new StackTraceCleaner() { // from class: org.powermock.api.mockito.internal.exceptions.StackTraceCleanerProvider.1
            @Override // org.mockito.exceptions.stacktrace.StackTraceCleaner
            public boolean isIn(StackTraceElement candidate) {
                return (!defaultCleaner.isIn(candidate) || candidate.getClassName().startsWith("org.powermock.") || candidate.getClassName().startsWith("sun.reflect.") || candidate.getClassName().startsWith("java.lang.reflect.") || candidate.getClassName().startsWith("jdk.internal.reflect.") || candidate.getLineNumber() == -1) ? false : true;
            }
        };
    }
}
