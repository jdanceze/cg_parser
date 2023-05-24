package org.powermock.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Set;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.internal.TypeUtils;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/DefaultFieldValueGenerator.class */
public class DefaultFieldValueGenerator {
    public static <T> T fillWithDefaultValues(T object) {
        if (object == null) {
            throw new IllegalArgumentException("object to fill cannot be null");
        }
        Set<Field> allInstanceFields = Whitebox.getAllInstanceFields(object);
        for (Field field : allInstanceFields) {
            Class<?> fieldType = field.getType();
            Object defaultValue = TypeUtils.getDefaultValue(fieldType);
            if (defaultValue == null && fieldType != object.getClass() && !field.isSynthetic()) {
                defaultValue = instantiateFieldType(field);
                if (defaultValue != null) {
                    fillWithDefaultValues(defaultValue);
                }
            }
            try {
                field.set(object, defaultValue);
            } catch (Exception e) {
                throw new RuntimeException("Internal error: Failed to set field.", e);
            }
        }
        return object;
    }

    private static Object instantiateFieldType(Field field) {
        Object defaultValue;
        Class<?> fieldType = field.getType();
        int modifiers = fieldType.getModifiers();
        if (fieldType.isAssignableFrom(ClassLoader.class) || isClass(fieldType)) {
            defaultValue = null;
        } else if (Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers) && !fieldType.isArray()) {
            Class<?> createConcreteSubClass = new ConcreteClassGenerator().createConcreteSubClass(fieldType);
            defaultValue = createConcreteSubClass == null ? null : Whitebox.newInstance(createConcreteSubClass);
        } else {
            defaultValue = Whitebox.newInstance(substituteKnownProblemTypes(fieldType));
        }
        return defaultValue;
    }

    private static boolean isClass(Class<?> fieldType) {
        return fieldType == Class.class;
    }

    private static Class<?> substituteKnownProblemTypes(Class<?> fieldType) {
        if (fieldType == InetAddress.class) {
            return Inet4Address.class;
        }
        return fieldType;
    }
}
