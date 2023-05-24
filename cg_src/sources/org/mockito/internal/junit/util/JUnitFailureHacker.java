package org.mockito.internal.junit.util;

import java.lang.reflect.Field;
import org.junit.runner.notification.Failure;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.plugins.MemberAccessor;
@Deprecated
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/util/JUnitFailureHacker.class */
public class JUnitFailureHacker {
    public void appendWarnings(Failure failure, String warnings) {
        if (isEmpty(warnings)) {
            return;
        }
        Throwable throwable = (Throwable) getInternalState(failure, "fThrownException");
        String newMessage = "contains both: actual test failure *and* Mockito warnings.\n" + warnings + "\n *** The actual failure is because of: ***\n";
        ExceptionIncludingMockitoWarnings e = new ExceptionIncludingMockitoWarnings(newMessage, throwable);
        e.setStackTrace(throwable.getStackTrace());
        setInternalState(failure, "fThrownException", e);
    }

    private boolean isEmpty(String warnings) {
        return warnings == null || "".equals(warnings);
    }

    private static Object getInternalState(Object target, String field) {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        Class<?> c = target.getClass();
        try {
            Field f = getFieldFromHierarchy(c, field);
            return accessor.get(f, target);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get internal state on a private field. Please report to mockito mailing list.", e);
        }
    }

    private static void setInternalState(Object target, String field, Object value) {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        Class<?> c = target.getClass();
        try {
            Field f = getFieldFromHierarchy(c, field);
            accessor.set(f, target, value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set internal state on a private field. Please report to mockito mailing list.", e);
        }
    }

    private static Field getFieldFromHierarchy(Class<?> clazz, String field) {
        Field f;
        Field field2 = getField(clazz, field);
        while (true) {
            f = field2;
            if (f != null || clazz == Object.class) {
                break;
            }
            clazz = clazz.getSuperclass();
            field2 = getField(clazz, field);
        }
        if (f == null) {
            throw new RuntimeException("You want me to get this field: '" + field + "' on this class: '" + clazz.getSimpleName() + "' but this field is not declared within the hierarchy of this class!");
        }
        return f;
    }

    private static Field getField(Class<?> clazz, String field) {
        try {
            return clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
