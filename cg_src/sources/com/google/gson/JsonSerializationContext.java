package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/JsonSerializationContext.class */
public interface JsonSerializationContext {
    JsonElement serialize(Object obj);

    JsonElement serialize(Object obj, Type type);
}
