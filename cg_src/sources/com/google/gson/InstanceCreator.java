package com.google.gson;

import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/InstanceCreator.class */
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
