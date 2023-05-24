package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/rtlib/tamiflex/ReflectiveCalls.class */
public class ReflectiveCalls {
    private static final Set<String> classForName = new HashSet();
    private static final Set<String> classNewInstance = new HashSet();
    private static final Set<String> constructorNewInstance = new HashSet();
    private static final Set<String> methodInvoke = new HashSet();
    private static final Set<String> fieldSet = new HashSet();
    private static final Set<String> fieldGet = new HashSet();

    public static void knownClassForName(int contextId, String className) {
        if (!classForName.contains(String.valueOf(contextId) + className)) {
            UnexpectedReflectiveCall.classForName(className);
        }
    }

    public static void knownClassNewInstance(int contextId, Class<?> c) {
        if (!classNewInstance.contains(String.valueOf(contextId) + c.getName())) {
            UnexpectedReflectiveCall.classNewInstance(c);
        }
    }

    public static void knownConstructorNewInstance(int contextId, Constructor<?> c) {
        if (!constructorNewInstance.contains(String.valueOf(contextId) + SootSig.sootSignature(c))) {
            UnexpectedReflectiveCall.constructorNewInstance(c);
        }
    }

    public static void knownMethodInvoke(int contextId, Object o, Method m) {
        if (!methodInvoke.contains(String.valueOf(contextId) + SootSig.sootSignature(o, m))) {
            UnexpectedReflectiveCall.methodInvoke(o, m);
        }
    }

    public static void knownFieldSet(int contextId, Object o, Field f) {
        if (!fieldSet.contains(String.valueOf(contextId) + SootSig.sootSignature(f))) {
            UnexpectedReflectiveCall.fieldSet(o, f);
        }
    }

    public static void knownFieldGet(int contextId, Object o, Field f) {
        if (!fieldGet.contains(String.valueOf(contextId) + SootSig.sootSignature(f))) {
            UnexpectedReflectiveCall.fieldGet(o, f);
        }
    }
}
