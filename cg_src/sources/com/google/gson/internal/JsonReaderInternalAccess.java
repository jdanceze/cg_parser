package com.google.gson.internal;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/internal/JsonReaderInternalAccess.class */
public abstract class JsonReaderInternalAccess {
    public static JsonReaderInternalAccess INSTANCE;

    public abstract void promoteNameToValue(JsonReader jsonReader) throws IOException;
}
