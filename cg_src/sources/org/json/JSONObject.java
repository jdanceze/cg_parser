package org.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import javax.resource.spi.work.WorkException;
import soot.JavaBasicTypes;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/JSONObject.class */
public class JSONObject {
    private Map map;
    public static final Object NULL = new Null(null);
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$Character;
    static Class class$java$lang$String;
    static Class class$java$lang$Boolean;

    /* renamed from: org.json.JSONObject$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/JSONObject$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/JSONObject$Null.class */
    private static final class Null {
        private Null() {
        }

        Null(AnonymousClass1 x0) {
            this();
        }

        protected final Object clone() {
            return this;
        }

        public boolean equals(Object object) {
            return object == null || object == this;
        }

        public String toString() {
            return Jimple.NULL;
        }
    }

    public JSONObject() {
        this.map = new HashMap();
    }

    public JSONObject(JSONObject jo, String[] names) throws JSONException {
        this();
        for (int i = 0; i < names.length; i++) {
            putOpt(names[i], jo.opt(names[i]));
        }
    }

    public JSONObject(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        while (true) {
            switch (x.nextClean()) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    String key = x.nextValue().toString();
                    char c = x.nextClean();
                    if (c == '=') {
                        if (x.next() != '>') {
                            x.back();
                        }
                    } else if (c != ':') {
                        throw x.syntaxError("Expected a ':' after a key");
                    }
                    put(key, x.nextValue());
                    switch (x.nextClean()) {
                        case ',':
                        case ';':
                            if (x.nextClean() == '}') {
                                return;
                            }
                            x.back();
                        case '}':
                            return;
                        default:
                            throw x.syntaxError("Expected a ',' or '}'");
                    }
            }
        }
    }

    public JSONObject(Map map) {
        this.map = map == null ? new HashMap() : map;
    }

    public JSONObject(Map map, boolean includeSuperClass) {
        this.map = new HashMap();
        if (map != null) {
            for (Map.Entry e : map.entrySet()) {
                this.map.put(e.getKey(), new JSONObject(e.getValue(), includeSuperClass));
            }
        }
    }

    public JSONObject(Object bean) {
        this();
        populateInternalMap(bean, false);
    }

    public JSONObject(Object bean, boolean includeSuperClass) {
        this();
        populateInternalMap(bean, includeSuperClass);
    }

    private void populateInternalMap(Object bean, boolean includeSuperClass) {
        Class klass = bean.getClass();
        if (klass.getClassLoader() == null) {
            includeSuperClass = false;
        }
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (Method method : methods) {
            try {
                String name = method.getName();
                String key = "";
                if (name.startsWith("get")) {
                    key = name.substring(3);
                } else if (name.startsWith("is")) {
                    key = name.substring(2);
                }
                if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                    if (key.length() == 1) {
                        key = key.toLowerCase();
                    } else if (!Character.isUpperCase(key.charAt(1))) {
                        key = new StringBuffer().append(key.substring(0, 1).toLowerCase()).append(key.substring(1)).toString();
                    }
                    Object result = method.invoke(bean, null);
                    if (result == null) {
                        this.map.put(key, NULL);
                    } else if (result.getClass().isArray()) {
                        this.map.put(key, new JSONArray(result, includeSuperClass));
                    } else if (result instanceof Collection) {
                        this.map.put(key, new JSONArray((Collection) result, includeSuperClass));
                    } else if (result instanceof Map) {
                        this.map.put(key, new JSONObject((Map) result, includeSuperClass));
                    } else if (isStandardProperty(result.getClass())) {
                        this.map.put(key, result);
                    } else if (result.getClass().getPackage().getName().startsWith("java") || result.getClass().getClassLoader() == null) {
                        this.map.put(key, result.toString());
                    } else {
                        this.map.put(key, new JSONObject(result, includeSuperClass));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isStandardProperty(Class clazz) {
        Class<?> cls;
        Class<?> cls2;
        Class<?> cls3;
        Class<?> cls4;
        Class<?> cls5;
        Class<?> cls6;
        Class<?> cls7;
        Class<?> cls8;
        Class<?> cls9;
        if (!clazz.isPrimitive()) {
            if (class$java$lang$Byte == null) {
                cls = class$(JavaBasicTypes.JAVA_LANG_BYTE);
                class$java$lang$Byte = cls;
            } else {
                cls = class$java$lang$Byte;
            }
            if (!clazz.isAssignableFrom(cls)) {
                if (class$java$lang$Short == null) {
                    cls2 = class$(JavaBasicTypes.JAVA_LANG_SHORT);
                    class$java$lang$Short = cls2;
                } else {
                    cls2 = class$java$lang$Short;
                }
                if (!clazz.isAssignableFrom(cls2)) {
                    if (class$java$lang$Integer == null) {
                        cls3 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls3;
                    } else {
                        cls3 = class$java$lang$Integer;
                    }
                    if (!clazz.isAssignableFrom(cls3)) {
                        if (class$java$lang$Long == null) {
                            cls4 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                            class$java$lang$Long = cls4;
                        } else {
                            cls4 = class$java$lang$Long;
                        }
                        if (!clazz.isAssignableFrom(cls4)) {
                            if (class$java$lang$Float == null) {
                                cls5 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                                class$java$lang$Float = cls5;
                            } else {
                                cls5 = class$java$lang$Float;
                            }
                            if (!clazz.isAssignableFrom(cls5)) {
                                if (class$java$lang$Double == null) {
                                    cls6 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
                                    class$java$lang$Double = cls6;
                                } else {
                                    cls6 = class$java$lang$Double;
                                }
                                if (!clazz.isAssignableFrom(cls6)) {
                                    if (class$java$lang$Character == null) {
                                        cls7 = class$(JavaBasicTypes.JAVA_LANG_CHARACTER);
                                        class$java$lang$Character = cls7;
                                    } else {
                                        cls7 = class$java$lang$Character;
                                    }
                                    if (!clazz.isAssignableFrom(cls7)) {
                                        if (class$java$lang$String == null) {
                                            cls8 = class$("java.lang.String");
                                            class$java$lang$String = cls8;
                                        } else {
                                            cls8 = class$java$lang$String;
                                        }
                                        if (!clazz.isAssignableFrom(cls8)) {
                                            if (class$java$lang$Boolean == null) {
                                                cls9 = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
                                                class$java$lang$Boolean = cls9;
                                            } else {
                                                cls9 = class$java$lang$Boolean;
                                            }
                                            if (!clazz.isAssignableFrom(cls9)) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public JSONObject(Object object, String[] names) {
        this();
        Class c = object.getClass();
        for (String name : names) {
            try {
                Field field = c.getField(name);
                Object value = field.get(object);
                put(name, value);
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONObject accumulate(String key, Object value) throws JSONException {
        testValidity(value);
        Object o = opt(key);
        if (o == null) {
            put(key, value instanceof JSONArray ? new JSONArray().put(value) : value);
        } else if (o instanceof JSONArray) {
            ((JSONArray) o).put(value);
        } else {
            put(key, new JSONArray().put(o).put(value));
        }
        return this;
    }

    public JSONObject append(String key, Object value) throws JSONException {
        testValidity(value);
        Object o = opt(key);
        if (o == null) {
            put(key, new JSONArray().put(value));
        } else if (o instanceof JSONArray) {
            put(key, ((JSONArray) o).put(value));
        } else {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(key).append("] is not a JSONArray.").toString());
        }
        return this;
    }

    public static String doubleToString(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return Jimple.NULL;
        }
        String s = Double.toString(d);
        if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while (s.endsWith(WorkException.UNDEFINED)) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    public Object get(String key) throws JSONException {
        Object o = opt(key);
        if (o == null) {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] not found.").toString());
        }
        return o;
    }

    public boolean getBoolean(String key) throws JSONException {
        Object o = get(key);
        if (o.equals(Boolean.FALSE)) {
            return false;
        }
        if ((o instanceof String) && ((String) o).equalsIgnoreCase("false")) {
            return false;
        }
        if (o.equals(Boolean.TRUE)) {
            return true;
        }
        if ((o instanceof String) && ((String) o).equalsIgnoreCase("true")) {
            return true;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a Boolean.").toString());
    }

    public double getDouble(String key) throws JSONException {
        Object o = get(key);
        try {
            return o instanceof Number ? ((Number) o).doubleValue() : Double.valueOf((String) o).doubleValue();
        } catch (Exception e) {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a number.").toString());
        }
    }

    public int getInt(String key) throws JSONException {
        Object o = get(key);
        return o instanceof Number ? ((Number) o).intValue() : (int) getDouble(key);
    }

    public JSONArray getJSONArray(String key) throws JSONException {
        Object o = get(key);
        if (o instanceof JSONArray) {
            return (JSONArray) o;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a JSONArray.").toString());
    }

    public JSONObject getJSONObject(String key) throws JSONException {
        Object o = get(key);
        if (o instanceof JSONObject) {
            return (JSONObject) o;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a JSONObject.").toString());
    }

    public long getLong(String key) throws JSONException {
        Object o = get(key);
        return o instanceof Number ? ((Number) o).longValue() : (long) getDouble(key);
    }

    public static String[] getNames(JSONObject jo) {
        int length = jo.length();
        if (length == 0) {
            return null;
        }
        Iterator i = jo.keys();
        String[] names = new String[length];
        int j = 0;
        while (i.hasNext()) {
            names[j] = (String) i.next();
            j++;
        }
        return names;
    }

    public static String[] getNames(Object object) {
        if (object == null) {
            return null;
        }
        Class klass = object.getClass();
        Field[] fields = klass.getFields();
        int length = fields.length;
        if (length == 0) {
            return null;
        }
        String[] names = new String[length];
        for (int i = 0; i < length; i++) {
            names[i] = fields[i].getName();
        }
        return names;
    }

    public String getString(String key) throws JSONException {
        return get(key).toString();
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public boolean isNull(String key) {
        return NULL.equals(opt(key));
    }

    public Iterator keys() {
        return this.map.keySet().iterator();
    }

    public int length() {
        return this.map.size();
    }

    public JSONArray names() {
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            ja.put(keys.next());
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String numberToString(Number n) throws JSONException {
        if (n == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(n);
        String s = n.toString();
        if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while (s.endsWith(WorkException.UNDEFINED)) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    public Object opt(String key) {
        if (key == null) {
            return null;
        }
        return this.map.get(key);
    }

    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        try {
            return getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONObject put(String key, Collection value) throws JSONException {
        put(key, new JSONArray(value));
        return this;
    }

    public double optDouble(String key) {
        return optDouble(key, Double.NaN);
    }

    public double optDouble(String key, double defaultValue) {
        try {
            Object o = opt(key);
            return o instanceof Number ? ((Number) o).doubleValue() : new Double((String) o).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(String key) {
        return optInt(key, 0);
    }

    public int optInt(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(String key) {
        Object o = opt(key);
        if (o instanceof JSONArray) {
            return (JSONArray) o;
        }
        return null;
    }

    public JSONObject optJSONObject(String key) {
        Object o = opt(key);
        if (o instanceof JSONObject) {
            return (JSONObject) o;
        }
        return null;
    }

    public long optLong(String key) {
        return optLong(key, 0L);
    }

    public long optLong(String key, long defaultValue) {
        try {
            return getLong(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(String key) {
        return optString(key, "");
    }

    public String optString(String key, String defaultValue) {
        Object o = opt(key);
        return o != null ? o.toString() : defaultValue;
    }

    public JSONObject put(String key, boolean value) throws JSONException {
        put(key, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject put(String key, double value) throws JSONException {
        put(key, new Double(value));
        return this;
    }

    public JSONObject put(String key, int value) throws JSONException {
        put(key, new Integer(value));
        return this;
    }

    public JSONObject put(String key, long value) throws JSONException {
        put(key, new Long(value));
        return this;
    }

    public JSONObject put(String key, Map value) throws JSONException {
        put(key, new JSONObject(value));
        return this;
    }

    public JSONObject put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.map.put(key, value);
        } else {
            remove(key);
        }
        return this;
    }

    public JSONObject putOpt(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            put(key, value);
        }
        return this;
    }

    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }
        char c = 0;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        sb.append('\"');
        for (int i = 0; i < len; i++) {
            char b = c;
            c = string.charAt(i);
            switch (c) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    if (b == '<') {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                default:
                    if (c < ' ' || ((c >= 128 && c < 160) || (c >= 8192 && c < 8448))) {
                        String t = new StringBuffer().append("000").append(Integer.toHexString(c)).toString();
                        sb.append(new StringBuffer().append("\\u").append(t.substring(t.length() - 4)).toString());
                        break;
                    } else {
                        sb.append(c);
                        break;
                    }
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    public Object remove(String key) {
        return this.map.remove(key);
    }

    public Iterator sortedKeys() {
        return new TreeSet(this.map.keySet()).iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void testValidity(Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            } else if (o instanceof Float) {
                if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
        }
    }

    public JSONArray toJSONArray(JSONArray names) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        for (int i = 0; i < names.length(); i++) {
            ja.put(opt(names.getString(i)));
        }
        return ja;
    }

    public String toString() {
        try {
            Iterator keys = keys();
            StringBuffer sb = new StringBuffer("{");
            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                Object o = keys.next();
                sb.append(quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(this.map.get(o)));
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        return toString(indentFactor, 0);
    }

    String toString(int indentFactor, int indent) throws JSONException {
        int n = length();
        if (n == 0) {
            return "{}";
        }
        Iterator keys = sortedKeys();
        StringBuffer sb = new StringBuffer("{");
        int newindent = indent + indentFactor;
        if (n == 1) {
            Object o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(": ");
            sb.append(valueToString(this.map.get(o), indentFactor, indent));
        } else {
            while (keys.hasNext()) {
                Object o2 = keys.next();
                if (sb.length() > 1) {
                    sb.append(",\n");
                } else {
                    sb.append('\n');
                }
                for (int j = 0; j < newindent; j++) {
                    sb.append(' ');
                }
                sb.append(quote(o2.toString()));
                sb.append(": ");
                sb.append(valueToString(this.map.get(o2), indentFactor, newindent));
            }
            if (sb.length() > 1) {
                sb.append('\n');
                for (int j2 = 0; j2 < indent; j2++) {
                    sb.append(' ');
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String valueToString(Object value) throws JSONException {
        if (value == null || value.equals(null)) {
            return Jimple.NULL;
        }
        if (value instanceof JSONString) {
            try {
                Object o = ((JSONString) value).toJSONString();
                if (o instanceof String) {
                    return (String) o;
                }
                throw new JSONException(new StringBuffer().append("Bad value from toJSONString: ").append(o).toString());
            } catch (Exception e) {
                throw new JSONException(e);
            }
        } else if (value instanceof Number) {
            return numberToString((Number) value);
        } else {
            if ((value instanceof Boolean) || (value instanceof JSONObject) || (value instanceof JSONArray)) {
                return value.toString();
            }
            if (value instanceof Map) {
                return new JSONObject((Map) value).toString();
            }
            if (value instanceof Collection) {
                return new JSONArray((Collection) value).toString();
            }
            if (value.getClass().isArray()) {
                return new JSONArray(value).toString();
            }
            return quote(value.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String valueToString(Object value, int indentFactor, int indent) throws JSONException {
        if (value == null || value.equals(null)) {
            return Jimple.NULL;
        }
        try {
            if (value instanceof JSONString) {
                Object o = ((JSONString) value).toJSONString();
                if (o instanceof String) {
                    return (String) o;
                }
            }
        } catch (Exception e) {
        }
        if (value instanceof Number) {
            return numberToString((Number) value);
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof JSONObject) {
            return ((JSONObject) value).toString(indentFactor, indent);
        }
        if (value instanceof JSONArray) {
            return ((JSONArray) value).toString(indentFactor, indent);
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value).toString(indentFactor, indent);
        }
        if (value instanceof Collection) {
            return new JSONArray((Collection) value).toString(indentFactor, indent);
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString(indentFactor, indent);
        }
        return quote(value.toString());
    }

    public Writer write(Writer writer) throws JSONException {
        try {
            boolean b = false;
            Iterator keys = keys();
            writer.write(123);
            while (keys.hasNext()) {
                if (b) {
                    writer.write(44);
                }
                Object k = keys.next();
                writer.write(quote(k.toString()));
                writer.write(58);
                Object v = this.map.get(k);
                if (v instanceof JSONObject) {
                    ((JSONObject) v).write(writer);
                } else if (v instanceof JSONArray) {
                    ((JSONArray) v).write(writer);
                } else {
                    writer.write(valueToString(v));
                }
                b = true;
            }
            writer.write(125);
            return writer;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
}
