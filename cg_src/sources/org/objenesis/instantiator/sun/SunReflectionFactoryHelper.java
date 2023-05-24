package org.objenesis.instantiator.sun;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.objenesis.ObjenesisException;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/sun/SunReflectionFactoryHelper.class */
class SunReflectionFactoryHelper {
    SunReflectionFactoryHelper() {
    }

    public static <T> Constructor<T> newConstructorForSerialization(Class<T> type, Constructor<?> constructor) {
        Class<?> reflectionFactoryClass = getReflectionFactoryClass();
        Object reflectionFactory = createReflectionFactory(reflectionFactoryClass);
        Method newConstructorForSerializationMethod = getNewConstructorForSerializationMethod(reflectionFactoryClass);
        try {
            return (Constructor) newConstructorForSerializationMethod.invoke(reflectionFactory, type, constructor);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ObjenesisException(e);
        }
    }

    private static Class<?> getReflectionFactoryClass() {
        try {
            return Class.forName("sun.reflect.ReflectionFactory");
        } catch (ClassNotFoundException e) {
            throw new ObjenesisException(e);
        }
    }

    private static Object createReflectionFactory(Class<?> reflectionFactoryClass) {
        try {
            Method method = reflectionFactoryClass.getDeclaredMethod(TypeProxy.SilentConstruction.Appender.GET_REFLECTION_FACTORY_METHOD_NAME, new Class[0]);
            return method.invoke(null, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            throw new ObjenesisException(e);
        }
    }

    private static Method getNewConstructorForSerializationMethod(Class<?> reflectionFactoryClass) {
        try {
            return reflectionFactoryClass.getDeclaredMethod(TypeProxy.SilentConstruction.Appender.NEW_CONSTRUCTOR_FOR_SERIALIZATION_METHOD_NAME, Class.class, Constructor.class);
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(e);
        }
    }
}
