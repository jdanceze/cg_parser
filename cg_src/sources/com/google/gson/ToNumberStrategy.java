package com.google.gson;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/ToNumberStrategy.class */
public interface ToNumberStrategy {
    Number readNumber(JsonReader jsonReader) throws IOException;
}
