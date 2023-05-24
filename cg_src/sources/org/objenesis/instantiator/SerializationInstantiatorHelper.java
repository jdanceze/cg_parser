package org.objenesis.instantiator;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/SerializationInstantiatorHelper.class */
public class SerializationInstantiatorHelper {
    public static <T> Class<? super T> getNonSerializableSuperClass(Class<T> type) {
        Class<T> cls = type;
        while (Serializable.class.isAssignableFrom(cls)) {
            cls = cls.getSuperclass();
            if (cls == null) {
                throw new Error("Bad class hierarchy: No non-serializable parents");
            }
        }
        return (Class<? super T>) cls;
    }
}
