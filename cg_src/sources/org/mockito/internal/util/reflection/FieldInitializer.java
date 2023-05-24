package org.mockito.internal.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldInitializer.class */
public class FieldInitializer {
    private final Object fieldOwner;
    private final Field field;
    private final ConstructorInstantiator instantiator;

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldInitializer$ConstructorArgumentResolver.class */
    public interface ConstructorArgumentResolver {
        Object[] resolveTypeInstances(Class<?>... clsArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldInitializer$ConstructorInstantiator.class */
    public interface ConstructorInstantiator {
        FieldInitializationReport instantiate();
    }

    public FieldInitializer(Object fieldOwner, Field field) {
        this(fieldOwner, field, new NoArgConstructorInstantiator(fieldOwner, field));
    }

    public FieldInitializer(Object fieldOwner, Field field, ConstructorArgumentResolver argResolver) {
        this(fieldOwner, field, new ParameterizedConstructorInstantiator(fieldOwner, field, argResolver));
    }

    private FieldInitializer(Object fieldOwner, Field field, ConstructorInstantiator instantiator) {
        if (new FieldReader(fieldOwner, field).isNull()) {
            checkNotLocal(field);
            checkNotInner(field);
            checkNotInterface(field);
            checkNotEnum(field);
            checkNotAbstract(field);
        }
        this.fieldOwner = fieldOwner;
        this.field = field;
        this.instantiator = instantiator;
    }

    public FieldInitializationReport initialize() {
        try {
            return acquireFieldInstance();
        } catch (IllegalAccessException e) {
            throw new MockitoException("Problems initializing field '" + this.field.getName() + "' of type '" + this.field.getType().getSimpleName() + "'", e);
        }
    }

    private void checkNotLocal(Field field) {
        if (field.getType().isLocalClass()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is a local class.");
        }
    }

    private void checkNotInner(Field field) {
        Class<?> type = field.getType();
        if (type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) {
            throw new MockitoException("the type '" + type.getSimpleName() + "' is an inner non static class.");
        }
    }

    private void checkNotInterface(Field field) {
        if (field.getType().isInterface()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an interface.");
        }
    }

    private void checkNotAbstract(Field field) {
        if (Modifier.isAbstract(field.getType().getModifiers())) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an abstract class.");
        }
    }

    private void checkNotEnum(Field field) {
        if (field.getType().isEnum()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an enum.");
        }
    }

    private FieldInitializationReport acquireFieldInstance() throws IllegalAccessException {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        Object fieldInstance = accessor.get(this.field, this.fieldOwner);
        if (fieldInstance != null) {
            return new FieldInitializationReport(fieldInstance, false, false);
        }
        return this.instantiator.instantiate();
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldInitializer$NoArgConstructorInstantiator.class */
    static class NoArgConstructorInstantiator implements ConstructorInstantiator {
        private final Object testClass;
        private final Field field;

        NoArgConstructorInstantiator(Object testClass, Field field) {
            this.testClass = testClass;
            this.field = field;
        }

        @Override // org.mockito.internal.util.reflection.FieldInitializer.ConstructorInstantiator
        public FieldInitializationReport instantiate() {
            MemberAccessor invoker = Plugins.getMemberAccessor();
            try {
                Constructor<?> constructor = this.field.getType().getDeclaredConstructor(new Class[0]);
                Object[] noArg = new Object[0];
                Object newFieldInstance = invoker.newInstance(constructor, noArg);
                invoker.set(this.field, this.testClass, newFieldInstance);
                return new FieldInitializationReport(invoker.get(this.field, this.testClass), true, false);
            } catch (IllegalAccessException e) {
                throw new MockitoException("IllegalAccessException (see the stack trace for cause): " + e.toString(), e);
            } catch (InstantiationException e2) {
                throw new MockitoException("InstantiationException (see the stack trace for cause): " + e2.toString(), e2);
            } catch (NoSuchMethodException e3) {
                throw new MockitoException("the type '" + this.field.getType().getSimpleName() + "' has no default constructor", e3);
            } catch (InvocationTargetException e4) {
                throw new MockitoException("the default constructor of type '" + this.field.getType().getSimpleName() + "' has raised an exception (see the stack trace for cause): " + e4.getTargetException().toString(), e4);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldInitializer$ParameterizedConstructorInstantiator.class */
    static class ParameterizedConstructorInstantiator implements ConstructorInstantiator {
        private final Object testClass;
        private final Field field;
        private final ConstructorArgumentResolver argResolver;
        private final Comparator<Constructor<?>> byParameterNumber = new Comparator<Constructor<?>>() { // from class: org.mockito.internal.util.reflection.FieldInitializer.ParameterizedConstructorInstantiator.1
            @Override // java.util.Comparator
            public int compare(Constructor<?> constructorA, Constructor<?> constructorB) {
                int argLengths = constructorB.getParameterTypes().length - constructorA.getParameterTypes().length;
                if (argLengths == 0) {
                    int constructorAMockableParamsSize = countMockableParams(constructorA);
                    int constructorBMockableParamsSize = countMockableParams(constructorB);
                    return constructorBMockableParamsSize - constructorAMockableParamsSize;
                }
                return argLengths;
            }

            private int countMockableParams(Constructor<?> constructor) {
                Class<?>[] parameterTypes;
                int constructorMockableParamsSize = 0;
                for (Class<?> aClass : constructor.getParameterTypes()) {
                    if (MockUtil.typeMockabilityOf(aClass).mockable()) {
                        constructorMockableParamsSize++;
                    }
                }
                return constructorMockableParamsSize;
            }
        };

        ParameterizedConstructorInstantiator(Object testClass, Field field, ConstructorArgumentResolver argumentResolver) {
            this.testClass = testClass;
            this.field = field;
            this.argResolver = argumentResolver;
        }

        @Override // org.mockito.internal.util.reflection.FieldInitializer.ConstructorInstantiator
        public FieldInitializationReport instantiate() {
            MemberAccessor accessor = Plugins.getMemberAccessor();
            Constructor<?> constructor = biggestConstructor(this.field.getType());
            Object[] args = this.argResolver.resolveTypeInstances(constructor.getParameterTypes());
            try {
                Object newFieldInstance = accessor.newInstance(constructor, args);
                accessor.set(this.field, this.testClass, newFieldInstance);
                return new FieldInitializationReport(accessor.get(this.field, this.testClass), false, true);
            } catch (IllegalAccessException e) {
                throw new MockitoException("IllegalAccessException (see the stack trace for cause): " + e.toString(), e);
            } catch (IllegalArgumentException e2) {
                throw new MockitoException("internal error : argResolver provided incorrect types for constructor " + constructor + " of type " + this.field.getType().getSimpleName(), e2);
            } catch (InstantiationException e3) {
                throw new MockitoException("InstantiationException (see the stack trace for cause): " + e3.toString(), e3);
            } catch (InvocationTargetException e4) {
                throw new MockitoException("the constructor of type '" + this.field.getType().getSimpleName() + "' has raised an exception (see the stack trace for cause): " + e4.getTargetException().toString(), e4);
            }
        }

        private void checkParameterized(Constructor<?> constructor, Field field) {
            if (constructor.getParameterTypes().length == 0) {
                throw new MockitoException("the field " + field.getName() + " of type " + field.getType() + " has no parameterized constructor");
            }
        }

        private Constructor<?> biggestConstructor(Class<?> clazz) {
            List<? extends Constructor<?>> constructors = Arrays.asList(clazz.getDeclaredConstructors());
            Collections.sort(constructors, this.byParameterNumber);
            Constructor<?> constructor = (Constructor) constructors.get(0);
            checkParameterized(constructor, this.field);
            return constructor;
        }
    }
}
