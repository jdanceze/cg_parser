package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.plugins.StackTraceCleanerProvider;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/stacktrace/DefaultStackTraceCleanerProvider.class */
public class DefaultStackTraceCleanerProvider implements StackTraceCleanerProvider {
    @Override // org.mockito.plugins.StackTraceCleanerProvider
    public StackTraceCleaner getStackTraceCleaner(StackTraceCleaner defaultCleaner) {
        return defaultCleaner;
    }
}
