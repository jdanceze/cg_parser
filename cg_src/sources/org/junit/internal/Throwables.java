package org.junit.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/Throwables.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/Throwables.class */
public final class Throwables {
    private static final Method getSuppressed = initGetSuppressed();
    private static final String[] TEST_FRAMEWORK_METHOD_NAME_PREFIXES = {"org.junit.runner.", "org.junit.runners.", "org.junit.experimental.runners.", "org.junit.internal.", "junit.extensions", "junit.framework", "junit.runner", "junit.textui"};
    private static final String[] TEST_FRAMEWORK_TEST_METHOD_NAME_PREFIXES = {"org.junit.internal.StackTracesTest"};
    private static final String[] REFLECTION_METHOD_NAME_PREFIXES = {"sun.reflect.", "java.lang.reflect.", "jdk.internal.reflect.", "org.junit.rules.RunRules.<init>(", "org.junit.rules.RunRules.applyAll(", "org.junit.runners.RuleContainer.apply(", "junit.framework.TestCase.runBare("};

    private Throwables() {
    }

    public static Exception rethrowAsException(Throwable e) throws Exception {
        rethrow(e);
        return null;
    }

    private static <T extends Throwable> void rethrow(Throwable e) throws Throwable {
        throw e;
    }

    public static String getStacktrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        exception.printStackTrace(writer);
        return stringWriter.toString();
    }

    public static String getTrimmedStackTrace(Throwable exception) {
        List<String> trimmedStackTraceLines = getTrimmedStackTraceLines(exception);
        if (trimmedStackTraceLines.isEmpty()) {
            return getFullStackTrace(exception);
        }
        StringBuilder result = new StringBuilder(exception.toString());
        appendStackTraceLines(trimmedStackTraceLines, result);
        appendStackTraceLines(getCauseStackTraceLines(exception), result);
        return result.toString();
    }

    private static List<String> getTrimmedStackTraceLines(Throwable exception) {
        List<StackTraceElement> stackTraceElements = Arrays.asList(exception.getStackTrace());
        int linesToInclude = stackTraceElements.size();
        State state = State.PROCESSING_OTHER_CODE;
        for (StackTraceElement stackTraceElement : asReversedList(stackTraceElements)) {
            state = state.processStackTraceElement(stackTraceElement);
            if (state == State.DONE) {
                List<String> trimmedLines = new ArrayList<>(linesToInclude + 2);
                trimmedLines.add("");
                for (StackTraceElement each : stackTraceElements.subList(0, linesToInclude)) {
                    trimmedLines.add("\tat " + each);
                }
                if (exception.getCause() != null) {
                    trimmedLines.add("\t... " + (stackTraceElements.size() - trimmedLines.size()) + " trimmed");
                }
                return trimmedLines;
            }
            linesToInclude--;
        }
        return Collections.emptyList();
    }

    private static Method initGetSuppressed() {
        try {
            return Throwable.class.getMethod("getSuppressed", new Class[0]);
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean hasSuppressed(Throwable exception) {
        if (getSuppressed == null) {
            return false;
        }
        try {
            Throwable[] suppressed = (Throwable[]) getSuppressed.invoke(exception, new Object[0]);
            return suppressed.length != 0;
        } catch (Throwable th) {
            return false;
        }
    }

    private static List<String> getCauseStackTraceLines(Throwable exception) {
        String line;
        if (exception.getCause() != null || hasSuppressed(exception)) {
            String fullTrace = getFullStackTrace(exception);
            BufferedReader reader = new BufferedReader(new StringReader(fullTrace.substring(exception.toString().length())));
            List<String> causedByLines = new ArrayList<>();
            do {
                try {
                    line = reader.readLine();
                    if (line != null) {
                        if (line.startsWith("Caused by: ")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                }
            } while (!line.trim().startsWith("Suppressed: "));
            causedByLines.add(line);
            while (true) {
                String line2 = reader.readLine();
                if (line2 != null) {
                    causedByLines.add(line2);
                } else {
                    return causedByLines;
                }
            }
        }
        return Collections.emptyList();
    }

    private static String getFullStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        exception.printStackTrace(writer);
        return stringWriter.toString();
    }

    private static void appendStackTraceLines(List<String> stackTraceLines, StringBuilder destBuilder) {
        for (String stackTraceLine : stackTraceLines) {
            destBuilder.append(String.format("%s%n", stackTraceLine));
        }
    }

    private static <T> List<T> asReversedList(final List<T> list) {
        return new AbstractList<T>() { // from class: org.junit.internal.Throwables.1
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // java.util.AbstractList, java.util.List
            public T get(int index) {
                return list.get((list.size() - index) - 1);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return list.size();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/Throwables$State.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/Throwables$State.class */
    public enum State {
        PROCESSING_OTHER_CODE { // from class: org.junit.internal.Throwables.State.1
            @Override // org.junit.internal.Throwables.State
            public State processLine(String methodName) {
                if (Throwables.isTestFrameworkMethod(methodName)) {
                    return PROCESSING_TEST_FRAMEWORK_CODE;
                }
                return this;
            }
        },
        PROCESSING_TEST_FRAMEWORK_CODE { // from class: org.junit.internal.Throwables.State.2
            @Override // org.junit.internal.Throwables.State
            public State processLine(String methodName) {
                if (!Throwables.isReflectionMethod(methodName)) {
                    if (Throwables.isTestFrameworkMethod(methodName)) {
                        return this;
                    }
                    return PROCESSING_OTHER_CODE;
                }
                return PROCESSING_REFLECTION_CODE;
            }
        },
        PROCESSING_REFLECTION_CODE { // from class: org.junit.internal.Throwables.State.3
            @Override // org.junit.internal.Throwables.State
            public State processLine(String methodName) {
                if (!Throwables.isReflectionMethod(methodName)) {
                    if (Throwables.isTestFrameworkMethod(methodName)) {
                        return PROCESSING_TEST_FRAMEWORK_CODE;
                    }
                    return DONE;
                }
                return this;
            }
        },
        DONE { // from class: org.junit.internal.Throwables.State.4
            @Override // org.junit.internal.Throwables.State
            public State processLine(String methodName) {
                return this;
            }
        };

        protected abstract State processLine(String str);

        public final State processStackTraceElement(StackTraceElement element) {
            return processLine(element.getClassName() + "." + element.getMethodName() + "()");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTestFrameworkMethod(String methodName) {
        return isMatchingMethod(methodName, TEST_FRAMEWORK_METHOD_NAME_PREFIXES) && !isMatchingMethod(methodName, TEST_FRAMEWORK_TEST_METHOD_NAME_PREFIXES);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isReflectionMethod(String methodName) {
        return isMatchingMethod(methodName, REFLECTION_METHOD_NAME_PREFIXES);
    }

    private static boolean isMatchingMethod(String methodName, String[] methodNamePrefixes) {
        for (String methodNamePrefix : methodNamePrefixes) {
            if (methodName.startsWith(methodNamePrefix)) {
                return true;
            }
        }
        return false;
    }
}
