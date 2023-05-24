package com.sun.xml.bind;

import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/AccessorFactoryImpl.class */
public class AccessorFactoryImpl implements InternalAccessorFactory {
    private static AccessorFactoryImpl instance = new AccessorFactoryImpl();

    private AccessorFactoryImpl() {
    }

    public static AccessorFactoryImpl getInstance() {
        return instance;
    }

    @Override // com.sun.xml.bind.AccessorFactory
    public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly) {
        if (readOnly) {
            return new Accessor.ReadOnlyFieldReflection(field);
        }
        return new Accessor.FieldReflection(field);
    }

    @Override // com.sun.xml.bind.InternalAccessorFactory
    public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly, boolean supressWarning) {
        if (readOnly) {
            return new Accessor.ReadOnlyFieldReflection(field, supressWarning);
        }
        return new Accessor.FieldReflection(field, supressWarning);
    }

    @Override // com.sun.xml.bind.AccessorFactory
    public Accessor createPropertyAccessor(Class bean, Method getter, Method setter) {
        if (getter == null) {
            return new Accessor.SetterOnlyReflection(setter);
        }
        if (setter == null) {
            return new Accessor.GetterOnlyReflection(getter);
        }
        return new Accessor.GetterSetterReflection(getter, setter);
    }
}
