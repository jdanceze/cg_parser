package com.google.gson;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/JsonNull.class */
public final class JsonNull extends JsonElement {
    public static final JsonNull INSTANCE = new JsonNull();

    @Override // com.google.gson.JsonElement
    public JsonNull deepCopy() {
        return INSTANCE;
    }

    public int hashCode() {
        return JsonNull.class.hashCode();
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof JsonNull);
    }
}
