package org.mockito.internal.exceptions.stacktrace;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.internal.configuration.plugins.Plugins;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/stacktrace/StackTraceFilter.class */
public class StackTraceFilter implements Serializable {
    static final long serialVersionUID = -5499819791513105700L;
    private static final StackTraceCleaner CLEANER = Plugins.getStackTraceCleanerProvider().getStackTraceCleaner(new DefaultStackTraceCleaner());
    private static Object JAVA_LANG_ACCESS;
    private static Method GET_STACK_TRACE_ELEMENT;

    static {
        try {
            JAVA_LANG_ACCESS = Class.forName("sun.misc.SharedSecrets").getMethod("getJavaLangAccess", new Class[0]).invoke(null, new Object[0]);
            GET_STACK_TRACE_ELEMENT = Class.forName("sun.misc.JavaLangAccess").getMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
        } catch (Exception e) {
        }
    }

    public StackTraceElement[] filter(StackTraceElement[] target, boolean keepTop) {
        List<StackTraceElement> filtered = new ArrayList<>();
        for (StackTraceElement element : target) {
            if (CLEANER.isIn(element)) {
                filtered.add(element);
            }
        }
        StackTraceElement[] result = new StackTraceElement[filtered.size()];
        return (StackTraceElement[]) filtered.toArray(result);
    }

    public StackTraceElement filterFirst(Throwable target, boolean isInline) {
        StackTraceElement[] stackTrace;
        boolean shouldSkip = isInline;
        if (GET_STACK_TRACE_ELEMENT != null) {
            int i = 0;
            while (true) {
                try {
                    StackTraceElement stackTraceElement = (StackTraceElement) GET_STACK_TRACE_ELEMENT.invoke(JAVA_LANG_ACCESS, target, Integer.valueOf(i));
                    if (CLEANER.isIn(stackTraceElement)) {
                        if (shouldSkip) {
                            shouldSkip = false;
                        } else {
                            return stackTraceElement;
                        }
                    }
                    i++;
                } catch (Exception e) {
                }
            }
        }
        for (StackTraceElement stackTraceElement2 : target.getStackTrace()) {
            if (CLEANER.isIn(stackTraceElement2)) {
                if (shouldSkip) {
                    shouldSkip = false;
                } else {
                    return stackTraceElement2;
                }
            }
        }
        return null;
    }

    public String findSourceFile(StackTraceElement[] target, String defaultValue) {
        for (StackTraceElement e : target) {
            if (CLEANER.isIn(e)) {
                return e.getFileName();
            }
        }
        return defaultValue;
    }
}
