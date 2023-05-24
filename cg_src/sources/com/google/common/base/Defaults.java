package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Defaults.class */
public final class Defaults {
    private static final Double DOUBLE_DEFAULT = Double.valueOf((double) Const.default_value_double);
    private static final Float FLOAT_DEFAULT = Float.valueOf(0.0f);

    private Defaults() {
    }

    @NullableDecl
    public static <T> T defaultValue(Class<T> type) {
        Preconditions.checkNotNull(type);
        if (type == Boolean.TYPE) {
            return (T) Boolean.FALSE;
        }
        if (type == Character.TYPE) {
            return (T) (char) 0;
        }
        if (type == Byte.TYPE) {
            return (T) (byte) 0;
        }
        if (type == Short.TYPE) {
            return (T) (short) 0;
        }
        if (type == Integer.TYPE) {
            return (T) 0;
        }
        if (type == Long.TYPE) {
            return (T) 0L;
        }
        if (type == Float.TYPE) {
            return (T) FLOAT_DEFAULT;
        }
        if (type == Double.TYPE) {
            return (T) DOUBLE_DEFAULT;
        }
        return null;
    }
}
