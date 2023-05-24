package com.sun.xml.bind.v2.runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/RuntimeUtil.class */
public class RuntimeUtil {
    public static final Map<Class, Class> boxToPrimitive;
    public static final Map<Class, Class> primitiveToBox;

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/RuntimeUtil$ToStringAdapter.class */
    public static final class ToStringAdapter extends XmlAdapter<String, Object> {
        @Override // javax.xml.bind.annotation.adapters.XmlAdapter
        public Object unmarshal(String s) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.xml.bind.annotation.adapters.XmlAdapter
        public String marshal(Object o) {
            if (o == null) {
                return null;
            }
            return o.toString();
        }
    }

    static {
        Map<Class, Class> b = new HashMap<>();
        b.put(Byte.TYPE, Byte.class);
        b.put(Short.TYPE, Short.class);
        b.put(Integer.TYPE, Integer.class);
        b.put(Long.TYPE, Long.class);
        b.put(Character.TYPE, Character.class);
        b.put(Boolean.TYPE, Boolean.class);
        b.put(Float.TYPE, Float.class);
        b.put(Double.TYPE, Double.class);
        b.put(Void.TYPE, Void.class);
        primitiveToBox = Collections.unmodifiableMap(b);
        Map<Class, Class> p = new HashMap<>();
        for (Map.Entry<Class, Class> e : b.entrySet()) {
            p.put(e.getValue(), e.getKey());
        }
        boxToPrimitive = Collections.unmodifiableMap(p);
    }

    private static String getTypeName(Object o) {
        return o.getClass().getName();
    }
}
