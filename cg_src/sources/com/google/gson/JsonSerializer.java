package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/JsonSerializer.class */
public interface JsonSerializer<T> {
    JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext);
}
