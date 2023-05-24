package org.powermock.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/ClassReplicaCreator.class */
public class ClassReplicaCreator {
    private static final String POWERMOCK_INSTANCE_DELEGATOR_FIELD_NAME = "powerMockInstanceDelegatorField";
    private static AtomicInteger counter = new AtomicInteger(0);

    public <T> Class<T> createClassReplica(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }
        ClassPool classpool = ClassPool.getDefault();
        String originalClassName = clazz.getName();
        CtClass newClass = classpool.makeClass(generateReplicaClassName(clazz));
        try {
            CtClass originalClassAsCtClass = classpool.get(originalClassName);
            CtMethod[] declaredMethods = originalClassAsCtClass.getDeclaredMethods();
            for (CtMethod ctMethod : declaredMethods) {
                String code = getReplicaMethodDelegationCode(clazz, ctMethod, null);
                CtNewMethod.make(ctMethod.getReturnType(), ctMethod.getName(), ctMethod.getParameterTypes(), ctMethod.getExceptionTypes(), code, newClass);
            }
            return (Class<T>) newClass.toClass(getClass().getClassLoader(), getClass().getProtectionDomain());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Class<T> createInstanceReplica(T delegator) {
        if (delegator == null) {
            throw new IllegalArgumentException("delegator cannot be null");
        }
        Class<?> cls = delegator.getClass();
        ClassPool classpool = ClassPool.getDefault();
        String originalClassName = cls.getName();
        CtClass newClass = classpool.makeClass(generateReplicaClassName(cls));
        try {
            CtClass originalClassAsCtClass = classpool.get(originalClassName);
            copyFields(originalClassAsCtClass, newClass);
            addDelegatorField(delegator, newClass);
            CtMethod[] declaredMethods = originalClassAsCtClass.getDeclaredMethods();
            for (CtMethod ctMethod : declaredMethods) {
                getReplicaMethodDelegationCode(delegator.getClass(), ctMethod, POWERMOCK_INSTANCE_DELEGATOR_FIELD_NAME);
                CtMethod make2 = CtNewMethod.copy(ctMethod, newClass, null);
                newClass.addMethod(make2);
            }
            CtConstructor[] declaredConstructors = originalClassAsCtClass.getDeclaredConstructors();
            for (CtConstructor ctConstructor : declaredConstructors) {
                CtConstructor copy = CtNewConstructor.copy(ctConstructor, newClass, null);
                newClass.addConstructor(copy);
            }
            return (Class<T>) newClass.toClass(getClass().getClassLoader(), getClass().getProtectionDomain());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void addDelegatorField(T delegator, CtClass replicaClass) throws CannotCompileException {
        CtField f = CtField.make(String.format("private %s %s = null;", delegator.getClass().getName(), POWERMOCK_INSTANCE_DELEGATOR_FIELD_NAME), replicaClass);
        replicaClass.addField(f);
    }

    private <T> String generateReplicaClassName(Class<T> clazz) {
        return "replica." + clazz.getName() + "$$PowerMock" + counter.getAndIncrement();
    }

    private void copyFields(CtClass originalClassAsCtClass, CtClass newClass) throws CannotCompileException, NotFoundException {
        CtField[] declaredFields = originalClassAsCtClass.getDeclaredFields();
        CtField[] undeclaredFields = originalClassAsCtClass.getFields();
        Set<CtField> allFields = new HashSet<>();
        Collections.addAll(allFields, declaredFields);
        Collections.addAll(allFields, undeclaredFields);
        for (CtField ctField : allFields) {
            CtField f = new CtField(ctField.getType(), ctField.getName(), newClass);
            newClass.addField(f);
        }
    }

    private String getReplicaMethodDelegationCode(Class<?> clazz, CtMethod ctMethod, String classOrInstanceToDelegateTo) throws NotFoundException {
        StringBuilder builder = new StringBuilder();
        builder.append("{java.lang.reflect.Method originalMethod = ");
        builder.append(clazz.getName());
        builder.append(".class.getDeclaredMethod(\"");
        builder.append(ctMethod.getName());
        builder.append("\", ");
        String parametersAsString = getParametersAsString(getParameterTypes(ctMethod));
        if ("".equals(parametersAsString)) {
            builder.append(Jimple.NULL);
        } else {
            builder.append(parametersAsString);
        }
        builder.append(");\n");
        builder.append("originalMethod.setAccessible(true);\n");
        CtClass returnType = ctMethod.getReturnType();
        boolean isVoid = returnType.equals(CtClass.voidType);
        if (!isVoid) {
            builder.append("return (");
            builder.append(returnType.getName());
            builder.append(") ");
        }
        builder.append("originalMethod.invoke(");
        if (Modifier.isStatic(ctMethod.getModifiers()) || classOrInstanceToDelegateTo == null) {
            builder.append(clazz.getName());
            builder.append(".class");
        } else {
            builder.append(classOrInstanceToDelegateTo);
        }
        builder.append(", $args);}");
        return builder.toString();
    }

    private String[] getParameterTypes(CtMethod ctMethod) throws NotFoundException {
        CtClass[] parameterTypesAsCtClass = ctMethod.getParameterTypes();
        String[] parameterTypes = new String[parameterTypesAsCtClass.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = parameterTypesAsCtClass[i].getName() + ".class";
        }
        return parameterTypes;
    }

    private static String getParametersAsString(String[] types) {
        StringBuilder parametersAsString = new StringBuilder();
        if (types != null && types.length == 0) {
            parametersAsString.append("new Class[0]");
        } else {
            parametersAsString.append("new Class[] {");
            if (types != null) {
                for (int i = 0; i < types.length; i++) {
                    parametersAsString.append(types[i]);
                    if (i != types.length - 1) {
                        parametersAsString.append(", ");
                    }
                }
            }
            parametersAsString.append("}");
        }
        return parametersAsString.toString();
    }
}
