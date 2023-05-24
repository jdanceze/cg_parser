package org.powermock.api.mockito.internal.invocation;

import java.util.regex.Matcher;
import org.powermock.core.spi.support.InvocationSubstitute;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/invocation/InvocationControlAssertionError.class */
public class InvocationControlAssertionError {
    private static final String AT = "at";
    private static final String ERROR_LOCATION_MARKER = "->";
    private static final String COLON_NEWLINE = ":\n";
    private static final String NEWLINE_POINT = "\n.";
    private static final String HERE_TEXT = "here:\n";
    private static final String UNDESIRED_INVOCATION_TEXT = " Undesired invocation:";
    private static final String POWER_MOCKITO_CLASS_NAME = "org.powermock.api.mockito.PowerMockito";

    public static void updateErrorMessageForVerifyNoMoreInteractions(AssertionError errorToUpdate) {
        int invocationStackTraceIndex;
        String verifyNoMoreInteractionsInvocation = null;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; i--) {
            StackTraceElement stackTraceElement = stackTrace[i];
            if (stackTraceElement.getClassName().equals(POWER_MOCKITO_CLASS_NAME) && stackTraceElement.getMethodName().equals("verifyNoMoreInteractions")) {
                if (stackTrace[i + 1].getClassName().equals(POWER_MOCKITO_CLASS_NAME) && stackTrace[i + 1].getMethodName().equals("verifyZeroInteractions")) {
                    invocationStackTraceIndex = i + 2;
                } else {
                    invocationStackTraceIndex = i + 1;
                }
                verifyNoMoreInteractionsInvocation = stackTrace[invocationStackTraceIndex].toString();
            }
        }
        if (verifyNoMoreInteractionsInvocation == null) {
            return;
        }
        String message = errorToUpdate.getMessage();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        int indexOfFirstAt = message.indexOf(AT);
        int startOfVerifyNoMoreInteractionsInvocation = indexOfFirstAt + AT.length() + 1;
        int endOfVerifyNoMoreInteractionsInvocation = message.indexOf(10, indexOfFirstAt + AT.length());
        builder.replace(startOfVerifyNoMoreInteractionsInvocation, endOfVerifyNoMoreInteractionsInvocation, verifyNoMoreInteractionsInvocation);
        builder.delete(builder.indexOf("\n", endOfVerifyNoMoreInteractionsInvocation + 1), builder.lastIndexOf("\n"));
        Whitebox.setInternalState(errorToUpdate, "detailMessage", builder.toString());
    }

    public static void updateErrorMessageForMethodInvocation(AssertionError errorToUpdate) {
        Whitebox.setInternalState(errorToUpdate, "detailMessage", "\n" + changeMessageContent(errorToUpdate.getMessage()));
    }

    public static void throwAssertionErrorForNewSubstitutionFailure(AssertionError oldError, Class<?> type) {
        String newSubsitutionClassName = InvocationSubstitute.class.getSimpleName();
        String newSubsitutionClassNameInMockito = newSubsitutionClassName.substring(0, 1).toLowerCase() + newSubsitutionClassName.substring(1);
        String message = oldError.getMessage();
        String newSubsitutionMethodName = InvocationSubstitute.class.getDeclaredMethods()[0].getName();
        throw new AssertionError(changeMessageContent(message.replaceAll(newSubsitutionClassNameInMockito + "." + newSubsitutionMethodName, Matcher.quoteReplacement(type.getName())).replaceAll("method", "constructor")));
    }

    private static String changeMessageContent(String message) {
        StringBuilder builder = removeFailureLocations(message);
        removeText(builder, UNDESIRED_INVOCATION_TEXT);
        removeAndReplaceText(builder, HERE_TEXT, ' ');
        removeAndReplaceText(builder, COLON_NEWLINE, ' ');
        return builder.toString().trim();
    }

    private static StringBuilder removeFailureLocations(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        int indexOf = builder.indexOf(ERROR_LOCATION_MARKER);
        while (true) {
            int indexOfBeginLocation = indexOf;
            if (indexOfBeginLocation > 0) {
                int indexOfLocationEnd = builder.indexOf("\n", indexOfBeginLocation);
                builder.delete(indexOfBeginLocation, indexOfLocationEnd < 0 ? builder.length() : indexOfLocationEnd + 1);
                indexOf = builder.indexOf(ERROR_LOCATION_MARKER);
            } else {
                return builder;
            }
        }
    }

    private static void removeAndReplaceText(StringBuilder builder, String text, char appender) {
        int currentTextIndex = builder.indexOf(text);
        boolean isSingleConcat = true;
        while (currentTextIndex > 0) {
            int previousTextIndex = currentTextIndex;
            builder.delete(currentTextIndex, currentTextIndex + text.length());
            currentTextIndex = builder.indexOf(text);
            int length = builder.length();
            if (isLastFinding(currentTextIndex) && !isSingleConcat) {
                int start = builder.charAt(length - 1) == '\n' ? length - 1 : length;
                builder.replace(start, length, ".");
            } else {
                int end = previousTextIndex < length ? previousTextIndex + 1 : length;
                builder.replace(previousTextIndex, end, String.valueOf(builder.charAt(previousTextIndex)).toLowerCase());
                builder.insert(previousTextIndex, String.valueOf(appender));
                currentTextIndex++;
                isSingleConcat = false;
            }
        }
    }

    private static boolean isLastFinding(int index) {
        return index < 0;
    }

    private static void removeText(StringBuilder builder, String text) {
        int indexOf = builder.indexOf(text);
        while (true) {
            int textIndex = indexOf;
            if (textIndex > 0) {
                builder.delete(textIndex, textIndex + text.length());
                indexOf = builder.indexOf(text);
            } else {
                return;
            }
        }
    }
}
