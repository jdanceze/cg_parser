package com.sun.xml.bind.v2.model.core;

import com.sun.xml.bind.v2.model.nav.Navigator;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/TypeInfoSet.class */
public interface TypeInfoSet<T, C, F, M> {
    Navigator<T, C, F, M> getNavigator();

    NonElement<T, C> getTypeInfo(T t);

    NonElement<T, C> getAnyTypeInfo();

    NonElement<T, C> getClassInfo(C c);

    Map<? extends T, ? extends ArrayInfo<T, C>> arrays();

    Map<C, ? extends ClassInfo<T, C>> beans();

    Map<T, ? extends BuiltinLeafInfo<T, C>> builtins();

    Map<C, ? extends EnumLeafInfo<T, C>> enums();

    ElementInfo<T, C> getElementInfo(C c, QName qName);

    NonElement<T, C> getTypeInfo(Ref<T, C> ref);

    Map<QName, ? extends ElementInfo<T, C>> getElementMappings(C c);

    Iterable<? extends ElementInfo<T, C>> getAllElements();

    Map<String, String> getXmlNs(String str);

    Map<String, String> getSchemaLocations();

    XmlNsForm getElementFormDefault(String str);

    XmlNsForm getAttributeFormDefault(String str);

    void dump(Result result) throws JAXBException;
}
