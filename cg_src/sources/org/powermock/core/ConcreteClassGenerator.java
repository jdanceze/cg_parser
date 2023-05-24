package org.powermock.core;

import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import org.powermock.reflect.internal.TypeUtils;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/ConcreteClassGenerator.class */
public class ConcreteClassGenerator {
    private static AtomicInteger counter = new AtomicInteger(0);

    public Class<?> createConcreteSubClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("clazz must be abstract");
        }
        ClassPool classpool = ClassPool.getDefault();
        String originalClassName = clazz.getName();
        CtClass newClass = classpool.makeClass(generateClassName(clazz));
        try {
            newClass.setSuperclass(classpool.get(clazz.getName()));
            try {
                CtClass originalClassAsCtClass = classpool.get(originalClassName);
                CtMethod[] declaredMethods = originalClassAsCtClass.getDeclaredMethods();
                for (CtMethod ctMethod : declaredMethods) {
                    if (javassist.Modifier.isAbstract(ctMethod.getModifiers())) {
                        String code = getReturnCode(ctMethod.getReturnType());
                        CtNewMethod.make(ctMethod.getReturnType(), ctMethod.getName(), ctMethod.getParameterTypes(), ctMethod.getExceptionTypes(), code, newClass);
                    }
                }
                if (!hasInheritableConstructor(originalClassAsCtClass)) {
                    return null;
                }
                return newClass.toClass(getClass().getClassLoader(), getClass().getProtectionDomain());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    private boolean hasInheritableConstructor(CtClass cls) throws NotFoundException {
        CtConstructor[] constructors = cls.getDeclaredConstructors();
        if (constructors.length == 0) {
            return true;
        }
        for (CtConstructor ctConstructor : constructors) {
            int modifiers = ctConstructor.getModifiers();
            if (!javassist.Modifier.isPackage(modifiers) && !javassist.Modifier.isPrivate(modifiers)) {
                return true;
            }
        }
        return false;
    }

    private String getReturnCode(CtClass returnType) {
        if (returnType.equals(CtClass.voidType)) {
            return "{}";
        }
        return "{return " + TypeUtils.getDefaultValueAsString(returnType.getName()) + ";}";
    }

    private <T> String generateClassName(Class<T> clazz) {
        return "subclass." + clazz.getName() + "$$PowerMock" + counter.getAndIncrement();
    }
}
