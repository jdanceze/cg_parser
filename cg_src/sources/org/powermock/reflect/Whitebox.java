package org.powermock.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.powermock.reflect.matching.FieldMatchingStrategy;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/Whitebox.class */
public class Whitebox {
    public static Field getField(Class<?> type, String fieldName) {
        return WhiteboxImpl.getField(type, fieldName);
    }

    public static Field[] getFields(Class<?> clazz, String... fieldNames) {
        return WhiteboxImpl.getFields(clazz, fieldNames);
    }

    public static Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        return WhiteboxImpl.getMethod(type, methodName, parameterTypes);
    }

    public static Method getMethod(Class<?> type, Class<?>... parameterTypes) {
        return WhiteboxImpl.getMethod(type, parameterTypes);
    }

    public static <T> T newInstance(Class<T> classToInstantiate) {
        return (T) WhiteboxImpl.newInstance(classToInstantiate);
    }

    public static <T> Constructor<T> getConstructor(Class<?> type, Class<?>... parameterTypes) {
        return (Constructor<T>) WhiteboxImpl.getConstructor(type, parameterTypes);
    }

    public static void setInternalState(Object object, String fieldName, Object value) {
        WhiteboxImpl.setInternalState(object, fieldName, value);
    }

    public static void setInternalState(Object object, String fieldName, Object[] value) {
        WhiteboxImpl.setInternalState(object, fieldName, value);
    }

    public static void setInternalState(Object object, Object value, Object... additionalValues) {
        WhiteboxImpl.setInternalState(object, value, additionalValues);
    }

    public static void setInternalState(Object object, Object value, Class<?> where) {
        WhiteboxImpl.setInternalState(object, value, where);
    }

    public static void setInternalState(Object object, String fieldName, Object value, Class<?> where) {
        WhiteboxImpl.setInternalState(object, fieldName, value, where);
    }

    public static void setInternalState(Object object, Class<?> fieldType, Object value) {
        WhiteboxImpl.setInternalState(object, fieldType, value);
    }

    public static void setInternalState(Object object, Class<?> fieldType, Object value, Class<?> where) {
        WhiteboxImpl.setInternalState(object, fieldType, value, where);
    }

    public static <T> T getInternalState(Object object, String fieldName) {
        return (T) WhiteboxImpl.getInternalState(object, fieldName);
    }

    public static <T> T getInternalState(Object object, String fieldName, Class<?> where) {
        return (T) WhiteboxImpl.getInternalState(object, fieldName, where);
    }

    @Deprecated
    public static <T> T getInternalState(Object object, String fieldName, Class<?> where, Class<T> type) {
        return (T) getInternalState(object, fieldName, where);
    }

    public static <T> T getInternalState(Object object, Class<T> fieldType) {
        return (T) WhiteboxImpl.getInternalState(object, fieldType);
    }

    public static <T> T getInternalState(Object object, Class<T> fieldType, Class<?> where) {
        return (T) WhiteboxImpl.getInternalState(object, fieldType, where);
    }

    public static synchronized <T> T invokeMethod(Object instance, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(instance, arguments);
    }

    public static synchronized <T> T invokeMethod(Class<?> klass, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(klass, arguments);
    }

    public static synchronized <T> T invokeMethod(Object instance, String methodToExecute, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(instance, methodToExecute, arguments);
    }

    public static synchronized <T> T invokeMethod(Object instance, String methodToExecute, Class<?>[] argumentTypes, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(instance, methodToExecute, argumentTypes, arguments);
    }

    public static synchronized <T> T invokeMethod(Object instance, String methodToExecute, Class<?> definedIn, Class<?>[] argumentTypes, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(instance, methodToExecute, definedIn, argumentTypes, arguments);
    }

    public static synchronized <T> T invokeMethod(Object instance, Class<?> declaringClass, String methodToExecute, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(instance, declaringClass, methodToExecute, arguments);
    }

    public static synchronized <T> T invokeMethod(Object object, Class<?> declaringClass, String methodToExecute, Class<?>[] parameterTypes, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(object, declaringClass, methodToExecute, parameterTypes, arguments);
    }

    public static synchronized <T> T invokeMethod(Class<?> clazz, String methodToExecute, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeMethod(clazz, methodToExecute, arguments);
    }

    public static <T> T invokeConstructor(Class<T> classThatContainsTheConstructorToTest, Class<?>[] parameterTypes, Object[] arguments) throws Exception {
        return (T) WhiteboxImpl.invokeConstructor(classThatContainsTheConstructorToTest, parameterTypes, arguments);
    }

    public static <T> T invokeConstructor(Class<T> classThatContainsTheConstructorToTest, Object... arguments) throws Exception {
        return (T) WhiteboxImpl.invokeConstructor(classThatContainsTheConstructorToTest, arguments);
    }

    public static Constructor<?> getFirstParentConstructor(Class<?> klass) {
        return WhiteboxImpl.getFirstParentConstructor(klass);
    }

    public static Method[] getMethods(Class<?> clazz, String... methodNames) {
        return WhiteboxImpl.getMethods(clazz, methodNames);
    }

    public static Class<?> getType(Object object) {
        return WhiteboxImpl.getType(object);
    }

    public static Class<?> getUnproxyType(Object object) {
        return WhiteboxImpl.getUnproxyType(object);
    }

    public static Set<Field> getFieldsAnnotatedWith(Object object, Class<? extends Annotation> annotation, Class<? extends Annotation>... additionalAnnotations) {
        return WhiteboxImpl.getFieldsAnnotatedWith(object, annotation, additionalAnnotations);
    }

    public static Set<Field> getFieldsAnnotatedWith(Object object, Class<? extends Annotation>[] annotationTypes) {
        return WhiteboxImpl.getFieldsAnnotatedWith(object, annotationTypes);
    }

    public static Set<Field> getAllInstanceFields(Object object) {
        return WhiteboxImpl.getAllInstanceFields(object);
    }

    public static Set<Field> getAllStaticFields(Class<?> type) {
        return WhiteboxImpl.getAllStaticFields(type);
    }

    public static Set<Field> getFieldsOfType(Object object, Class<?> type) {
        return WhiteboxImpl.getFieldsOfType(object, type);
    }

    public static Class<Object> getInnerClassType(Class<?> declaringClass, String name) throws ClassNotFoundException {
        return WhiteboxImpl.getInnerClassType(declaringClass, name);
    }

    public static Class<Object> getLocalClassType(Class<?> declaringClass, int occurrence, String name) throws ClassNotFoundException {
        return WhiteboxImpl.getLocalClassType(declaringClass, occurrence, name);
    }

    public static Class<Object> getAnonymousInnerClassType(Class<?> declaringClass, int occurrence) throws ClassNotFoundException {
        return WhiteboxImpl.getAnonymousInnerClassType(declaringClass, occurrence);
    }

    public static void setInternalStateFromContext(Object instance, Object context, Object... additionalContexts) {
        WhiteboxImpl.setInternalStateFromContext(instance, context, additionalContexts);
    }

    public static void setInternalStateFromContext(Object classOrInstance, Class<?> context, Class<?>... additionalContexts) {
        WhiteboxImpl.setInternalStateFromContext(classOrInstance, context, additionalContexts);
    }

    public static void setInternalStateFromContext(Object instance, Object context, FieldMatchingStrategy strategy) {
        WhiteboxImpl.setInternalStateFromContext(instance, context, strategy);
    }

    public static void setInternalStateFromContext(Object instance, Class<?> context, FieldMatchingStrategy strategy) {
        WhiteboxImpl.setInternalStateFromContext(instance, context, strategy);
    }
}
