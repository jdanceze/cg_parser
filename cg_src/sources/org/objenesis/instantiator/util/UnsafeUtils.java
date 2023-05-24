package org.objenesis.instantiator.util;

import java.lang.reflect.Field;
import org.objenesis.ObjenesisException;
import sun.misc.Unsafe;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/util/UnsafeUtils.class */
public final class UnsafeUtils {
    private static final Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            try {
                unsafe = (Unsafe) f.get(null);
            } catch (IllegalAccessException e) {
                throw new ObjenesisException(e);
            }
        } catch (NoSuchFieldException e2) {
            throw new ObjenesisException(e2);
        }
    }

    private UnsafeUtils() {
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
