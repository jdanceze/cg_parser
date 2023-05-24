package org.mockito;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.internal.matchers.Matches;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.Primitives;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/ArgumentMatchers.class */
public class ArgumentMatchers {
    public static <T> T any() {
        return (T) anyObject();
    }

    @Deprecated
    public static <T> T anyObject() {
        reportMatcher(Any.ANY);
        return null;
    }

    public static <T> T any(Class<T> type) {
        reportMatcher(new InstanceOf.VarArgAware(type, "<any " + type.getCanonicalName() + ">"));
        return (T) Primitives.defaultValue(type);
    }

    public static <T> T isA(Class<T> type) {
        reportMatcher(new InstanceOf(type));
        return (T) Primitives.defaultValue(type);
    }

    @Deprecated
    public static <T> T anyVararg() {
        any();
        return null;
    }

    public static boolean anyBoolean() {
        reportMatcher(new InstanceOf(Boolean.class, "<any boolean>"));
        return false;
    }

    public static byte anyByte() {
        reportMatcher(new InstanceOf(Byte.class, "<any byte>"));
        return (byte) 0;
    }

    public static char anyChar() {
        reportMatcher(new InstanceOf(Character.class, "<any char>"));
        return (char) 0;
    }

    public static int anyInt() {
        reportMatcher(new InstanceOf(Integer.class, "<any integer>"));
        return 0;
    }

    public static long anyLong() {
        reportMatcher(new InstanceOf(Long.class, "<any long>"));
        return 0L;
    }

    public static float anyFloat() {
        reportMatcher(new InstanceOf(Float.class, "<any float>"));
        return 0.0f;
    }

    public static double anyDouble() {
        reportMatcher(new InstanceOf(Double.class, "<any double>"));
        return Const.default_value_double;
    }

    public static short anyShort() {
        reportMatcher(new InstanceOf(Short.class, "<any short>"));
        return (short) 0;
    }

    public static String anyString() {
        reportMatcher(new InstanceOf(String.class, "<any string>"));
        return "";
    }

    public static <T> List<T> anyList() {
        reportMatcher(new InstanceOf(List.class, "<any List>"));
        return new ArrayList(0);
    }

    @Deprecated
    public static <T> List<T> anyListOf(Class<T> clazz) {
        return anyList();
    }

    public static <T> Set<T> anySet() {
        reportMatcher(new InstanceOf(Set.class, "<any set>"));
        return new HashSet(0);
    }

    @Deprecated
    public static <T> Set<T> anySetOf(Class<T> clazz) {
        return anySet();
    }

    public static <K, V> Map<K, V> anyMap() {
        reportMatcher(new InstanceOf(Map.class, "<any map>"));
        return new HashMap(0);
    }

    @Deprecated
    public static <K, V> Map<K, V> anyMapOf(Class<K> keyClazz, Class<V> valueClazz) {
        return anyMap();
    }

    public static <T> Collection<T> anyCollection() {
        reportMatcher(new InstanceOf(Collection.class, "<any collection>"));
        return new ArrayList(0);
    }

    @Deprecated
    public static <T> Collection<T> anyCollectionOf(Class<T> clazz) {
        return anyCollection();
    }

    public static <T> Iterable<T> anyIterable() {
        reportMatcher(new InstanceOf(Iterable.class, "<any iterable>"));
        return new ArrayList(0);
    }

    @Deprecated
    public static <T> Iterable<T> anyIterableOf(Class<T> clazz) {
        return anyIterable();
    }

    public static boolean eq(boolean value) {
        reportMatcher(new Equals(Boolean.valueOf(value)));
        return false;
    }

    public static byte eq(byte value) {
        reportMatcher(new Equals(Byte.valueOf(value)));
        return (byte) 0;
    }

    public static char eq(char value) {
        reportMatcher(new Equals(Character.valueOf(value)));
        return (char) 0;
    }

    public static double eq(double value) {
        reportMatcher(new Equals(Double.valueOf(value)));
        return Const.default_value_double;
    }

    public static float eq(float value) {
        reportMatcher(new Equals(Float.valueOf(value)));
        return 0.0f;
    }

    public static int eq(int value) {
        reportMatcher(new Equals(Integer.valueOf(value)));
        return 0;
    }

    public static long eq(long value) {
        reportMatcher(new Equals(Long.valueOf(value)));
        return 0L;
    }

    public static short eq(short value) {
        reportMatcher(new Equals(Short.valueOf(value)));
        return (short) 0;
    }

    public static <T> T eq(T value) {
        reportMatcher(new Equals(value));
        if (value == null) {
            return null;
        }
        return (T) Primitives.defaultValue(value.getClass());
    }

    public static <T> T refEq(T value, String... excludeFields) {
        reportMatcher(new ReflectionEquals(value, excludeFields));
        return null;
    }

    public static <T> T same(T value) {
        reportMatcher(new Same(value));
        if (value == null) {
            return null;
        }
        return (T) Primitives.defaultValue(value.getClass());
    }

    public static <T> T isNull() {
        reportMatcher(Null.NULL);
        return null;
    }

    @Deprecated
    public static <T> T isNull(Class<T> clazz) {
        return (T) isNull();
    }

    public static <T> T notNull() {
        reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    @Deprecated
    public static <T> T notNull(Class<T> clazz) {
        return (T) notNull();
    }

    public static <T> T isNotNull() {
        return (T) notNull();
    }

    @Deprecated
    public static <T> T isNotNull(Class<T> clazz) {
        return (T) notNull(clazz);
    }

    public static <T> T nullable(Class<T> clazz) {
        AdditionalMatchers.or(isNull(), isA(clazz));
        return (T) Primitives.defaultValue(clazz);
    }

    public static String contains(String substring) {
        reportMatcher(new Contains(substring));
        return "";
    }

    public static String matches(String regex) {
        reportMatcher(new Matches(regex));
        return "";
    }

    public static String matches(Pattern pattern) {
        reportMatcher(new Matches(pattern));
        return "";
    }

    public static String endsWith(String suffix) {
        reportMatcher(new EndsWith(suffix));
        return "";
    }

    public static String startsWith(String prefix) {
        reportMatcher(new StartsWith(prefix));
        return "";
    }

    public static <T> T argThat(ArgumentMatcher<T> matcher) {
        reportMatcher(matcher);
        return null;
    }

    public static char charThat(ArgumentMatcher<Character> matcher) {
        reportMatcher(matcher);
        return (char) 0;
    }

    public static boolean booleanThat(ArgumentMatcher<Boolean> matcher) {
        reportMatcher(matcher);
        return false;
    }

    public static byte byteThat(ArgumentMatcher<Byte> matcher) {
        reportMatcher(matcher);
        return (byte) 0;
    }

    public static short shortThat(ArgumentMatcher<Short> matcher) {
        reportMatcher(matcher);
        return (short) 0;
    }

    public static int intThat(ArgumentMatcher<Integer> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    public static long longThat(ArgumentMatcher<Long> matcher) {
        reportMatcher(matcher);
        return 0L;
    }

    public static float floatThat(ArgumentMatcher<Float> matcher) {
        reportMatcher(matcher);
        return 0.0f;
    }

    public static double doubleThat(ArgumentMatcher<Double> matcher) {
        reportMatcher(matcher);
        return Const.default_value_double;
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        ThreadSafeMockingProgress.mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
