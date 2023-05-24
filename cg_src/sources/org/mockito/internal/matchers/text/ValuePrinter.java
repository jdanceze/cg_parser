package org.mockito.internal.matchers.text;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/text/ValuePrinter.class */
public class ValuePrinter {
    private ValuePrinter() {
    }

    public static String print(final Object value) {
        if (value == null) {
            return Jimple.NULL;
        }
        if (value instanceof String) {
            return '\"' + value.toString() + '\"';
        }
        if (value instanceof Character) {
            return printChar(((Character) value).charValue());
        }
        if (value instanceof Long) {
            return value + "L";
        }
        if (value instanceof Double) {
            return value + "d";
        }
        if (value instanceof Float) {
            return value + "f";
        }
        if (value instanceof Short) {
            return "(short) " + value;
        }
        if (value instanceof Byte) {
            return String.format("(byte) 0x%02X", (Byte) value);
        }
        if (value instanceof Map) {
            return printMap((Map) value);
        }
        if (value.getClass().isArray()) {
            return printValues("[", ", ", "]", new Iterator<Object>() { // from class: org.mockito.internal.matchers.text.ValuePrinter.1
                private int currentIndex = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.currentIndex < Array.getLength(value);
                }

                @Override // java.util.Iterator
                public Object next() {
                    Object obj = value;
                    int i = this.currentIndex;
                    this.currentIndex = i + 1;
                    return Array.get(obj, i);
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException("cannot remove items from an array");
                }
            });
        }
        if (value instanceof FormattedText) {
            return ((FormattedText) value).getText();
        }
        return descriptionOf(value);
    }

    private static String printMap(Map<?, ?> map) {
        StringBuilder result = new StringBuilder();
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            result.append(print(entry.getKey())).append(" = ").append(print(entry.getValue()));
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        return "{" + result.toString() + "}";
    }

    public static String printValues(String start, String separator, String end, Iterator<?> values) {
        if (start == null) {
            start = "(";
        }
        if (separator == null) {
            separator = ",";
        }
        if (end == null) {
            end = ")";
        }
        StringBuilder sb = new StringBuilder(start);
        while (values.hasNext()) {
            sb.append(print(values.next()));
            if (values.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.append(end).toString();
    }

    private static String printChar(char value) {
        StringBuilder sb = new StringBuilder();
        sb.append('\'');
        switch (value) {
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\"':
                sb.append("\\\"");
                break;
            default:
                sb.append(value);
                break;
        }
        sb.append('\'');
        return sb.toString();
    }

    private static String descriptionOf(Object value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
        }
    }
}
