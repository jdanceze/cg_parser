package org.mockito.plugins;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/StackTraceCleanerProvider.class */
public interface StackTraceCleanerProvider {
    StackTraceCleaner getStackTraceCleaner(StackTraceCleaner stackTraceCleaner);
}
