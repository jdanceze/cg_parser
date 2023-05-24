package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/BeanPropertySetter.class */
public class BeanPropertySetter {
    private static final String SET_PREFIX = "set";
    private final Object target;
    private final boolean reportNoSetterFound;
    private final Field field;

    public BeanPropertySetter(Object target, Field propertyField, boolean reportNoSetterFound) {
        this.field = propertyField;
        this.target = target;
        this.reportNoSetterFound = reportNoSetterFound;
    }

    public BeanPropertySetter(Object target, Field propertyField) {
        this(target, propertyField, false);
    }

    public boolean set(Object value) {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        Method writeMethod = null;
        try {
            writeMethod = this.target.getClass().getMethod(setterName(this.field.getName()), this.field.getType());
            accessor.invoke(writeMethod, this.target, value);
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Access not authorized on field '" + this.field + "' of object '" + this.target + "' with value: '" + value + "'", e);
        } catch (NoSuchMethodException e2) {
            reportNoSetterFound();
            reportNoSetterFound();
            return false;
        } catch (InvocationTargetException e3) {
            throw new RuntimeException("Setter '" + writeMethod + "' of '" + this.target + "' with value '" + value + "' threw exception : '" + e3.getTargetException() + "'", e3);
        }
    }

    private String setterName(String fieldName) {
        return SET_PREFIX + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
    }

    private void reportNoSetterFound() {
        if (this.reportNoSetterFound) {
            throw new RuntimeException("Problems setting value on object: [" + this.target + "] for property : [" + this.field.getName() + "], setter not found");
        }
    }
}
