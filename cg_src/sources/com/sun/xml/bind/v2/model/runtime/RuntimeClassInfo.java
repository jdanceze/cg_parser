package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeClassInfo.class */
public interface RuntimeClassInfo extends ClassInfo<Type, Class>, RuntimeNonElement {
    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    ClassInfo<Type, Class> getBaseClass();

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    List<? extends PropertyInfo<Type, Class>> getProperties();

    @Override // com.sun.xml.bind.v2.model.core.ClassInfo
    PropertyInfo<Type, Class> getProperty(String str);

    Method getFactoryMethod();

    <BeanT> Accessor<BeanT, Map<QName, String>> getAttributeWildcard();

    <BeanT> Accessor<BeanT, Locator> getLocatorField();
}
