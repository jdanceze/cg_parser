package com.google.protobuf;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/JavaType.class */
public enum JavaType {
    VOID(Void.class, Void.class, null),
    INT(Integer.TYPE, Integer.class, 0),
    LONG(Long.TYPE, Long.class, 0L),
    FLOAT(Float.TYPE, Float.class, Float.valueOf(0.0f)),
    DOUBLE(Double.TYPE, Double.class, Double.valueOf((double) Const.default_value_double)),
    BOOLEAN(Boolean.TYPE, Boolean.class, false),
    STRING(String.class, String.class, ""),
    BYTE_STRING(ByteString.class, ByteString.class, ByteString.EMPTY),
    ENUM(Integer.TYPE, Integer.class, null),
    MESSAGE(Object.class, Object.class, null);
    
    private final Class<?> type;
    private final Class<?> boxedType;
    private final Object defaultDefault;

    JavaType(Class cls, Class cls2, Object defaultDefault) {
        this.type = cls;
        this.boxedType = cls2;
        this.defaultDefault = defaultDefault;
    }

    public Object getDefaultDefault() {
        return this.defaultDefault;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Class<?> getBoxedType() {
        return this.boxedType;
    }

    public boolean isValidType(Class<?> t) {
        return this.type.isAssignableFrom(t);
    }
}
