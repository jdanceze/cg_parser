package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MessageLiteToString.class */
public final class MessageLiteToString {
    private static final String LIST_SUFFIX = "List";
    private static final String BUILDER_LIST_SUFFIX = "OrBuilderList";
    private static final String MAP_SUFFIX = "Map";
    private static final String BYTES_SUFFIX = "Bytes";
    private static final char[] INDENT_BUFFER = new char[80];

    static {
        Arrays.fill(INDENT_BUFFER, ' ');
    }

    private MessageLiteToString() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toString(MessageLite messageLite, String commentString) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("# ").append(commentString);
        reflectivePrintWithIndent(messageLite, buffer, 0);
        return buffer.toString();
    }

    private static void reflectivePrintWithIndent(MessageLite messageLite, StringBuilder buffer, int indent) {
        java.lang.reflect.Method[] declaredMethods;
        boolean booleanValue;
        java.lang.reflect.Method mapMethod;
        java.lang.reflect.Method listMethod;
        Set<String> setters = new HashSet<>();
        Map<String, java.lang.reflect.Method> hazzers = new HashMap<>();
        Map<String, java.lang.reflect.Method> getters = new TreeMap<>();
        for (java.lang.reflect.Method method : messageLite.getClass().getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers()) && method.getName().length() >= 3) {
                if (method.getName().startsWith("set")) {
                    setters.add(method.getName());
                } else if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
                    if (method.getName().startsWith("has")) {
                        hazzers.put(method.getName(), method);
                    } else if (method.getName().startsWith("get")) {
                        getters.put(method.getName(), method);
                    }
                }
            }
        }
        for (Map.Entry<String, java.lang.reflect.Method> getter : getters.entrySet()) {
            String suffix = getter.getKey().substring(3);
            if (suffix.endsWith(LIST_SUFFIX) && !suffix.endsWith(BUILDER_LIST_SUFFIX) && !suffix.equals(LIST_SUFFIX) && (listMethod = getter.getValue()) != null && listMethod.getReturnType().equals(List.class)) {
                printField(buffer, indent, suffix.substring(0, suffix.length() - LIST_SUFFIX.length()), GeneratedMessageLite.invokeOrDie(listMethod, messageLite, new Object[0]));
            } else if (suffix.endsWith(MAP_SUFFIX) && !suffix.equals(MAP_SUFFIX) && (mapMethod = getter.getValue()) != null && mapMethod.getReturnType().equals(Map.class) && !mapMethod.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(mapMethod.getModifiers())) {
                printField(buffer, indent, suffix.substring(0, suffix.length() - MAP_SUFFIX.length()), GeneratedMessageLite.invokeOrDie(mapMethod, messageLite, new Object[0]));
            } else if (setters.contains("set" + suffix) && (!suffix.endsWith(BYTES_SUFFIX) || !getters.containsKey("get" + suffix.substring(0, suffix.length() - BYTES_SUFFIX.length())))) {
                java.lang.reflect.Method getMethod = getter.getValue();
                java.lang.reflect.Method hasMethod = hazzers.get("has" + suffix);
                if (getMethod != null) {
                    Object value = GeneratedMessageLite.invokeOrDie(getMethod, messageLite, new Object[0]);
                    if (hasMethod == null) {
                        booleanValue = !isDefaultValue(value);
                    } else {
                        booleanValue = ((Boolean) GeneratedMessageLite.invokeOrDie(hasMethod, messageLite, new Object[0])).booleanValue();
                    }
                    boolean hasValue = booleanValue;
                    if (hasValue) {
                        printField(buffer, indent, suffix, value);
                    }
                }
            }
        }
        if (messageLite instanceof GeneratedMessageLite.ExtendableMessage) {
            Iterator<Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object>> iter = ((GeneratedMessageLite.ExtendableMessage) messageLite).extensions.iterator();
            while (iter.hasNext()) {
                Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object> entry = iter.next();
                printField(buffer, indent, "[" + entry.getKey().getNumber() + "]", entry.getValue());
            }
        }
        if (((GeneratedMessageLite) messageLite).unknownFields != null) {
            ((GeneratedMessageLite) messageLite).unknownFields.printWithIndent(buffer, indent);
        }
    }

    private static boolean isDefaultValue(Object o) {
        if (o instanceof Boolean) {
            return !((Boolean) o).booleanValue();
        } else if (o instanceof Integer) {
            return ((Integer) o).intValue() == 0;
        } else if (o instanceof Float) {
            return Float.floatToRawIntBits(((Float) o).floatValue()) == 0;
        } else if (o instanceof Double) {
            return Double.doubleToRawLongBits(((Double) o).doubleValue()) == 0;
        } else if (o instanceof String) {
            return o.equals("");
        } else {
            if (o instanceof ByteString) {
                return o.equals(ByteString.EMPTY);
            }
            return o instanceof MessageLite ? o == ((MessageLite) o).getDefaultInstanceForType() : (o instanceof java.lang.Enum) && ((java.lang.Enum) o).ordinal() == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void printField(StringBuilder buffer, int indent, String name, Object object) {
        if (object instanceof List) {
            List<?> list = (List) object;
            Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                printField(buffer, indent, name, it.next());
            }
        } else if (object instanceof Map) {
            Map<?, ?> map = (Map) object;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                printField(buffer, indent, name, entry);
            }
        } else {
            buffer.append('\n');
            indent(indent, buffer);
            buffer.append(pascalCaseToSnakeCase(name));
            if (object instanceof String) {
                buffer.append(": \"").append(TextFormatEscaper.escapeText((String) object)).append('\"');
            } else if (object instanceof ByteString) {
                buffer.append(": \"").append(TextFormatEscaper.escapeBytes((ByteString) object)).append('\"');
            } else if (object instanceof GeneratedMessageLite) {
                buffer.append(" {");
                reflectivePrintWithIndent((GeneratedMessageLite) object, buffer, indent + 2);
                buffer.append("\n");
                indent(indent, buffer);
                buffer.append("}");
            } else if (object instanceof Map.Entry) {
                buffer.append(" {");
                Map.Entry<?, ?> entry2 = (Map.Entry) object;
                printField(buffer, indent + 2, "key", entry2.getKey());
                printField(buffer, indent + 2, "value", entry2.getValue());
                buffer.append("\n");
                indent(indent, buffer);
                buffer.append("}");
            } else {
                buffer.append(": ").append(object);
            }
        }
    }

    private static void indent(int indent, StringBuilder buffer) {
        while (indent > 0) {
            int partialIndent = indent;
            if (partialIndent > INDENT_BUFFER.length) {
                partialIndent = INDENT_BUFFER.length;
            }
            buffer.append(INDENT_BUFFER, 0, partialIndent);
            indent -= partialIndent;
        }
    }

    private static String pascalCaseToSnakeCase(String pascalCase) {
        if (pascalCase.isEmpty()) {
            return pascalCase;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(Character.toLowerCase(pascalCase.charAt(0)));
        for (int i = 1; i < pascalCase.length(); i++) {
            char ch = pascalCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append("_");
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString();
    }
}
