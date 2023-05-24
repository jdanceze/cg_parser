package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/JsonDeserializer.class */
public interface JsonDeserializer<T> {
    T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
