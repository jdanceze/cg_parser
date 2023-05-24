package com.sun.xml.bind;

import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.xml.bind.JAXBException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/AccessorFactory.class */
public interface AccessorFactory {
    Accessor createFieldAccessor(Class cls, Field field, boolean z) throws JAXBException;

    Accessor createPropertyAccessor(Class cls, Method method, Method method2) throws JAXBException;
}
