package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/Property.class */
public interface Property<BeanT> extends StructureLoaderBuilder {
    void reset(BeanT beant) throws AccessorException;

    void serializeBody(BeanT beant, XMLSerializer xMLSerializer, Object obj) throws SAXException, AccessorException, IOException, XMLStreamException;

    void serializeURIs(BeanT beant, XMLSerializer xMLSerializer) throws SAXException, AccessorException;

    boolean hasSerializeURIAction();

    String getIdValue(BeanT beant) throws AccessorException, SAXException;

    PropertyKind getKind();

    Accessor getElementPropertyAccessor(String str, String str2);

    void wrapUp();

    RuntimePropertyInfo getInfo();

    boolean isHiddenByOverride();

    void setHiddenByOverride(boolean z);

    String getFieldName();
}
