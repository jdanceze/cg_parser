package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.StringUtil;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/SpyAnnotationEngine.class */
public class SpyAnnotationEngine implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {
    @Override // org.mockito.plugins.AnnotationEngine
    public AutoCloseable process(Class<?> context, Object testInstance) {
        Field[] fields = context.getDeclaredFields();
        MemberAccessor accessor = Plugins.getMemberAccessor();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
                assertNoIncompatibleAnnotations(Spy.class, field, Mock.class, Captor.class);
                try {
                    Object instance = accessor.get(field, testInstance);
                    if (MockUtil.isMock(instance)) {
                        Mockito.reset(instance);
                    } else if (instance != null) {
                        accessor.set(field, testInstance, spyInstance(field, instance));
                    } else {
                        accessor.set(field, testInstance, spyNewInstance(testInstance, field));
                    }
                } catch (Exception e) {
                    throw new MockitoException("Unable to initialize @Spy annotated field '" + field.getName() + "'.\n" + e.getMessage(), e);
                }
            }
        }
        return new AnnotationEngine.NoAction();
    }

    private static Object spyInstance(Field field, Object instance) {
        return Mockito.mock(instance.getClass(), Mockito.withSettings().spiedInstance(instance).defaultAnswer(Mockito.CALLS_REAL_METHODS).name(field.getName()));
    }

    private static Object spyNewInstance(Object testInstance, Field field) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        MockSettings settings = Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS).name(field.getName());
        Class<?> type = field.getType();
        if (type.isInterface()) {
            return Mockito.mock(type, settings.useConstructor(new Object[0]));
        }
        int modifiers = type.getModifiers();
        if (typeIsPrivateAbstractInnerClass(type, modifiers)) {
            throw new MockitoException(StringUtil.join("@Spy annotation can't initialize private abstract inner classes.", "  inner class: '" + type.getSimpleName() + "'", "  outer class: '" + type.getEnclosingClass().getSimpleName() + "'", "", "You should augment the visibility of this inner class"));
        }
        if (typeIsNonStaticInnerClass(type, modifiers)) {
            Class<?> enclosing = type.getEnclosingClass();
            if (!enclosing.isInstance(testInstance)) {
                throw new MockitoException(StringUtil.join("@Spy annotation can only initialize inner classes declared in the test.", "  inner class: '" + type.getSimpleName() + "'", "  outer class: '" + enclosing.getSimpleName() + "'", ""));
            }
            return Mockito.mock(type, settings.useConstructor(new Object[0]).outerInstance(testInstance));
        }
        Constructor<?> constructor = noArgConstructorOf(type);
        if (Modifier.isPrivate(constructor.getModifiers())) {
            MemberAccessor accessor = Plugins.getMemberAccessor();
            return Mockito.mock(type, settings.spiedInstance(accessor.newInstance(constructor, new Object[0])));
        }
        return Mockito.mock(type, settings.useConstructor(new Object[0]));
    }

    private static Constructor<?> noArgConstructorOf(Class<?> type) {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor(new Class[0]);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new MockitoException("Please ensure that the type '" + type.getSimpleName() + "' has a no-arg constructor.");
        }
    }

    private static boolean typeIsNonStaticInnerClass(Class<?> type, int modifiers) {
        return (Modifier.isStatic(modifiers) || type.getEnclosingClass() == null) ? false : true;
    }

    private static boolean typeIsPrivateAbstractInnerClass(Class<?> type, int modifiers) {
        return Modifier.isPrivate(modifiers) && Modifier.isAbstract(modifiers) && type.getEnclosingClass() != null;
    }

    private static void assertNoIncompatibleAnnotations(Class<? extends Annotation> annotation, Field field, Class<? extends Annotation>... undesiredAnnotations) {
        for (Class<? extends Annotation> u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                throw Reporter.unsupportedCombinationOfAnnotations(annotation.getSimpleName(), u.getSimpleName());
            }
        }
    }
}
