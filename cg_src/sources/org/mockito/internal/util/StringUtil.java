package org.mockito.internal.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/StringUtil.class */
public class StringUtil {
    private static final Pattern CAPS = Pattern.compile("([A-Z\\d][^A-Z\\d]*)");

    private StringUtil() {
    }

    public static String removeFirstLine(String text) {
        return text.replaceFirst(".*?\n", "");
    }

    public static String join(Object... linesToBreak) {
        return join("\n", Arrays.asList(linesToBreak));
    }

    public static String join(String start, Collection<?> lines) {
        return join(start, "", lines);
    }

    public static String join(String start, String linePrefix, Collection<?> lines) {
        if (lines.isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder(start);
        for (Object line : lines) {
            out.append(linePrefix).append(line).append("\n");
        }
        return out.substring(0, out.length() - 1);
    }

    public static String decamelizeMatcherName(String className) {
        if (className.length() == 0) {
            return "<custom argument matcher>";
        }
        String decamelized = decamelizeClassName(className);
        if (decamelized.length() == 0) {
            return "<" + className + ">";
        }
        return "<" + decamelized + ">";
    }

    private static String decamelizeClassName(String className) {
        Matcher match = CAPS.matcher(className);
        StringBuilder deCameled = new StringBuilder();
        while (match.find()) {
            if (deCameled.length() == 0) {
                deCameled.append(match.group());
            } else {
                deCameled.append(Instruction.argsep);
                deCameled.append(match.group().toLowerCase());
            }
        }
        return deCameled.toString();
    }
}
