package org.powermock.api.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.powermock.core.MockRepository;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.internal.WhiteboxImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/SuppressCode.class */
public class SuppressCode {
    public static synchronized void suppressConstructor(Constructor<?>... constructors) {
        if (constructors == null) {
            throw new IllegalArgumentException("constructors cannot be null.");
        }
        for (Constructor<?> constructor : constructors) {
            MockRepository.addConstructorToSuppress(constructor);
            Class<?> declaringClass = constructor.getDeclaringClass();
            if (declaringClass != null) {
                suppressConstructor(declaringClass.getSuperclass());
            }
        }
    }

    public static synchronized void suppressSpecificConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        MockRepository.addConstructorToSuppress(Whitebox.getConstructor(clazz, parameterTypes));
    }

    public static synchronized void suppressConstructor(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            Class<?> cls = clazz;
            while (true) {
                Class<?> tempClass = cls;
                if (tempClass != Object.class) {
                    suppressConstructor(tempClass, false);
                    cls = tempClass.getSuperclass();
                }
            }
        }
    }

    public static synchronized void suppressConstructor(Class<?> clazz, boolean excludePrivateConstructors) {
        Constructor<?>[] ctors;
        Constructor<?>[] constructorArr;
        if (excludePrivateConstructors) {
            ctors = clazz.getConstructors();
        } else {
            ctors = clazz.getDeclaredConstructors();
        }
        for (Constructor<?> ctor : ctors) {
            MockRepository.addConstructorToSuppress(ctor);
        }
    }

    public static synchronized void suppressField(Field... fields) {
        for (Field field : fields) {
            MockRepository.addFieldToSuppress(field);
        }
    }

    public static synchronized void suppressField(Class<?>[] classes) {
        if (classes == null || classes.length == 0) {
            throw new IllegalArgumentException("You must supply at least one class.");
        }
        for (Class<?> clazz : classes) {
            suppressField(clazz.getDeclaredFields());
        }
    }

    public static synchronized void suppressField(Class<?> clazz, String... fieldNames) {
        Field[] fields;
        if (fieldNames != null && fieldNames.length != 0) {
            for (Field field : Whitebox.getFields(clazz, fieldNames)) {
                MockRepository.addFieldToSuppress(field);
            }
            return;
        }
        suppressField(new Class[]{clazz});
    }

    public static synchronized void suppressMethod(Method... methods) {
        for (Method method : methods) {
            MockRepository.addMethodToSuppress(method);
        }
    }

    public static synchronized void suppressMethod(Class<?> cls, Class<?>... additionalClasses) {
        suppressMethod(cls, false);
        for (Class<?> clazz : additionalClasses) {
            suppressMethod(clazz, false);
        }
    }

    public static synchronized void suppressMethod(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            suppressMethod(clazz, false);
        }
    }

    public static synchronized void suppressMethod(Class<?> clazz, String methodName, String... additionalMethodNames) {
        Method[] methods;
        Method[] methods2;
        for (Method method : Whitebox.getMethods(clazz, methodName)) {
            MockRepository.addMethodToSuppress(method);
        }
        if (additionalMethodNames != null && additionalMethodNames.length > 0) {
            for (Method method2 : Whitebox.getMethods(clazz, additionalMethodNames)) {
                MockRepository.addMethodToSuppress(method2);
            }
        }
    }

    public static synchronized void suppressMethod(Class<?> clazz, String[] methodNames) {
        Method[] methods;
        for (Method method : Whitebox.getMethods(clazz, methodNames)) {
            MockRepository.addMethodToSuppress(method);
        }
    }

    public static synchronized void suppressMethod(Class<?> clazz, boolean excludePrivateMethods) {
        Method[] methods;
        Method[] methodArr;
        if (excludePrivateMethods) {
            methods = clazz.getMethods();
        } else {
            methods = clazz.getDeclaredMethods();
        }
        for (Method method : methods) {
            MockRepository.addMethodToSuppress(method);
        }
    }

    public static synchronized void suppressMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        Method method;
        if (parameterTypes.length > 0) {
            method = Whitebox.getMethod(clazz, methodName, parameterTypes);
        } else {
            method = WhiteboxImpl.findMethodOrThrowException(clazz, methodName, parameterTypes);
        }
        MockRepository.addMethodToSuppress(method);
    }
}
