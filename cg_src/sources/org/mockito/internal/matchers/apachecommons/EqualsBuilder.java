package org.mockito.internal.matchers.apachecommons;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/apachecommons/EqualsBuilder.class */
class EqualsBuilder {
    private boolean isEquals = true;

    public static boolean reflectionEquals(Object lhs, Object rhs) {
        return reflectionEquals(lhs, rhs, false, null, null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, String[] excludeFields) {
        return reflectionEquals(lhs, rhs, false, null, excludeFields);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
        return reflectionEquals(lhs, rhs, testTransients, null, null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass) {
        return reflectionEquals(lhs, rhs, testTransients, reflectUpToClass, null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String[] excludeFields) {
        Class<?> testClass;
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        Class<?> lhsClass = lhs.getClass();
        Class<?> rhsClass = rhs.getClass();
        if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
                testClass = rhsClass;
            }
        } else if (rhsClass.isInstance(lhs)) {
            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
                testClass = lhsClass;
            }
        } else {
            return false;
        }
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        if (reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields)) {
            return false;
        }
        while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
            testClass = testClass.getSuperclass();
            if (reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields)) {
                return false;
            }
        }
        return equalsBuilder.isEquals();
    }

    private static boolean reflectionAppend(Object lhs, Object rhs, Class<?> clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
        List<String> emptyList;
        Field[] fields = clazz.getDeclaredFields();
        if (excludeFields != null) {
            emptyList = Arrays.asList(excludeFields);
        } else {
            emptyList = Collections.emptyList();
        }
        List<String> excludedFieldList = emptyList;
        MemberAccessor accessor = Plugins.getMemberAccessor();
        for (int i = 0; i < fields.length && builder.isEquals; i++) {
            Field f = fields[i];
            if (!excludedFieldList.contains(f.getName()) && f.getName().indexOf(36) == -1 && ((useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers()))) {
                try {
                    builder.append(accessor.get(f, lhs), accessor.get(f, rhs));
                } catch (IllegalAccessException | RuntimeException e) {
                    return true;
                }
            }
        }
        return false;
    }

    public EqualsBuilder appendSuper(boolean superEquals) {
        this.isEquals &= superEquals;
        return this;
    }

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        }
        Class<?> lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            if ((lhs instanceof BigDecimal) && (rhs instanceof BigDecimal)) {
                this.isEquals = ((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0;
            } else {
                this.isEquals = lhs.equals(rhs);
            }
        } else if (lhs.getClass() != rhs.getClass()) {
            setEquals(false);
        } else if (lhs instanceof long[]) {
            append((long[]) lhs, (long[]) rhs);
        } else if (lhs instanceof int[]) {
            append((int[]) lhs, (int[]) rhs);
        } else if (lhs instanceof short[]) {
            append((short[]) lhs, (short[]) rhs);
        } else if (lhs instanceof char[]) {
            append((char[]) lhs, (char[]) rhs);
        } else if (lhs instanceof byte[]) {
            append((byte[]) lhs, (byte[]) rhs);
        } else if (lhs instanceof double[]) {
            append((double[]) lhs, (double[]) rhs);
        } else if (lhs instanceof float[]) {
            append((float[]) lhs, (float[]) rhs);
        } else if (lhs instanceof boolean[]) {
            append((boolean[]) lhs, (boolean[]) rhs);
        } else {
            append((Object[]) lhs, (Object[]) rhs);
        }
        return this;
    }

    public EqualsBuilder append(long lhs, long rhs) {
        this.isEquals &= lhs == rhs;
        return this;
    }

    public EqualsBuilder append(int lhs, int rhs) {
        this.isEquals &= lhs == rhs;
        return this;
    }

    public EqualsBuilder append(short lhs, short rhs) {
        this.isEquals &= lhs == rhs;
        return this;
    }

    public EqualsBuilder append(char lhs, char rhs) {
        this.isEquals &= lhs == rhs;
        return this;
    }

    public EqualsBuilder append(byte lhs, byte rhs) {
        this.isEquals &= lhs == rhs;
        return this;
    }

    public EqualsBuilder append(double lhs, double rhs) {
        if (!this.isEquals) {
            return this;
        }
        return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }

    public EqualsBuilder append(float lhs, float rhs) {
        if (!this.isEquals) {
            return this;
        }
        return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }

    public EqualsBuilder append(boolean lhs, boolean rhs) {
        this.isEquals &= lhs == rhs;
        return this;
    }

    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(long[] lhs, long[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(int[] lhs, int[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(short[] lhs, short[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(char[] lhs, char[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(double[] lhs, double[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(float[] lhs, float[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        if (!this.isEquals) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            setEquals(false);
            return this;
        } else if (lhs.length != rhs.length) {
            setEquals(false);
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.isEquals; i++) {
                append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    protected void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }
}
