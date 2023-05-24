package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/JsonDeserializationContext.class */
public interface JsonDeserializationContext {
    <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException;
}
