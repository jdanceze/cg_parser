package com.google.gson;

import com.google.gson.reflect.TypeToken;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/TypeAdapterFactory.class */
public interface TypeAdapterFactory {
    <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken);
}
