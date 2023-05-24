package com.sun.xml.bind.v2.util;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/util/TypeCast.class */
public class TypeCast {
    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> Map<K, V> checkedCast(Map<?, ?> m, Class<K> keyType, Class<V> valueType) {
        if (m == 0) {
            return null;
        }
        for (Map.Entry e : m.entrySet()) {
            if (!keyType.isInstance(e.getKey())) {
                throw new ClassCastException(e.getKey().getClass().toString());
            }
            if (!valueType.isInstance(e.getValue())) {
                throw new ClassCastException(e.getValue().getClass().toString());
            }
        }
        return m;
    }
}
