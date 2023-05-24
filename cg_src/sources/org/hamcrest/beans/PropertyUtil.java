package org.hamcrest.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/beans/PropertyUtil.class */
public class PropertyUtil {
    public static final Object[] NO_ARGUMENTS = new Object[0];

    public static PropertyDescriptor getPropertyDescriptor(String propertyName, Object fromObj) throws IllegalArgumentException {
        PropertyDescriptor[] arr$ = propertyDescriptorsFor(fromObj, null);
        for (PropertyDescriptor property : arr$) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    public static PropertyDescriptor[] propertyDescriptorsFor(Object fromObj, Class<Object> stopClass) throws IllegalArgumentException {
        try {
            return Introspector.getBeanInfo(fromObj.getClass(), stopClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Could not get property descriptors for " + fromObj.getClass(), e);
        }
    }
}
