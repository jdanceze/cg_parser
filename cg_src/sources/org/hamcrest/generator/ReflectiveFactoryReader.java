package org.hamcrest.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/ReflectiveFactoryReader.class */
public class ReflectiveFactoryReader implements Iterable<FactoryMethod> {
    private final Class<?> cls;
    private final ClassLoader classLoader;

    public ReflectiveFactoryReader(Class<?> cls) {
        this.cls = cls;
        this.classLoader = cls.getClassLoader();
    }

    @Override // java.lang.Iterable
    public Iterator<FactoryMethod> iterator() {
        return new Iterator<FactoryMethod>() { // from class: org.hamcrest.generator.ReflectiveFactoryReader.1
            private int currentMethod = -1;
            private Method[] allMethods;

            {
                this.allMethods = ReflectiveFactoryReader.this.cls.getMethods();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                do {
                    this.currentMethod++;
                    if (this.currentMethod >= this.allMethods.length) {
                        return false;
                    }
                } while (!ReflectiveFactoryReader.this.isFactoryMethod(this.allMethods[this.currentMethod]));
                return true;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public FactoryMethod next() {
                if (!outsideArrayBounds()) {
                    return ReflectiveFactoryReader.buildFactoryMethod(this.allMethods[this.currentMethod]);
                }
                throw new IllegalStateException("next() called without hasNext() check.");
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private boolean outsideArrayBounds() {
                return this.currentMethod < 0 || this.allMethods.length <= this.currentMethod;
            }
        };
    }

    protected boolean isFactoryMethod(Method javaMethod) {
        return Modifier.isStatic(javaMethod.getModifiers()) && Modifier.isPublic(javaMethod.getModifiers()) && hasFactoryAnnotation(javaMethod) && !Void.TYPE.equals(javaMethod.getReturnType());
    }

    private boolean hasFactoryAnnotation(Method javaMethod) {
        try {
            Class<?> factoryClass = this.classLoader.loadClass("org.hamcrest.Factory");
            if (Annotation.class.isAssignableFrom(factoryClass)) {
                return javaMethod.getAnnotation(factoryClass) != null;
            }
            throw new RuntimeException("Not an annotation class: " + factoryClass.getCanonicalName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load hamcrest core", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FactoryMethod buildFactoryMethod(Method javaMethod) {
        FactoryMethod result = new FactoryMethod(classToString(javaMethod.getDeclaringClass()), javaMethod.getName(), classToString(javaMethod.getReturnType()));
        TypeVariable<Method>[] arr$ = javaMethod.getTypeParameters();
        for (TypeVariable<Method> typeVariable : arr$) {
            boolean hasBound = false;
            StringBuilder s = new StringBuilder(typeVariable.getName());
            Type[] arr$2 = typeVariable.getBounds();
            for (Type bound : arr$2) {
                if (bound != Object.class) {
                    if (hasBound) {
                        s.append(" & ");
                    } else {
                        s.append(" extends ");
                        hasBound = true;
                    }
                    s.append(typeToString(bound));
                }
            }
            result.addGenericTypeParameter(s.toString());
        }
        Type returnType = javaMethod.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type generifiedType = parameterizedType.getActualTypeArguments()[0];
            result.setGenerifiedType(typeToString(generifiedType));
        }
        int paramNumber = 0;
        Type[] arr$3 = javaMethod.getGenericParameterTypes();
        for (Type paramType : arr$3) {
            String type = typeToString(paramType);
            if (javaMethod.isVarArgs() && paramNumber == javaMethod.getParameterTypes().length - 1) {
                type = type.replaceFirst("\\[\\]$", "...");
            }
            paramNumber++;
            result.addParameter(type, "param" + paramNumber);
        }
        Class<?>[] arr$4 = javaMethod.getExceptionTypes();
        for (Class<?> exception : arr$4) {
            result.addException(typeToString(exception));
        }
        return result;
    }

    private static String typeToString(Type type) {
        return type instanceof Class ? classToString((Class) type) : type.toString();
    }

    private static String classToString(Class<?> cls) {
        String name = cls.isArray() ? cls.getComponentType().getName() + "[]" : cls.getName();
        return name.replace('$', '.');
    }
}
