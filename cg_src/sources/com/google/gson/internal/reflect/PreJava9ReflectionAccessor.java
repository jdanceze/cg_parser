package com.google.gson.internal.reflect;

import java.lang.reflect.AccessibleObject;
/* loaded from: gencallgraphv3.jar:gson-2.8.9.jar:com/google/gson/internal/reflect/PreJava9ReflectionAccessor.class */
final class PreJava9ReflectionAccessor extends ReflectionAccessor {
    @Override // com.google.gson.internal.reflect.ReflectionAccessor
    public void makeAccessible(AccessibleObject ao) {
        ao.setAccessible(true);
    }
}
