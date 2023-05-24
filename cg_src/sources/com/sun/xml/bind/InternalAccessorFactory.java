package com.sun.xml.bind;

import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import javax.xml.bind.JAXBException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/InternalAccessorFactory.class */
public interface InternalAccessorFactory extends AccessorFactory {
    Accessor createFieldAccessor(Class cls, Field field, boolean z, boolean z2) throws JAXBException;
}
