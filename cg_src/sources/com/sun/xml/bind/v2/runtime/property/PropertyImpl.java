package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/PropertyImpl.class */
public abstract class PropertyImpl<BeanT> implements Property<BeanT> {
    protected final String fieldName;
    private RuntimePropertyInfo propertyInfo;
    private boolean hiddenByOverride = false;

    public PropertyImpl(JAXBContextImpl context, RuntimePropertyInfo prop) {
        this.propertyInfo = null;
        this.fieldName = prop.getName();
        if (context.retainPropertyInfo) {
            this.propertyInfo = prop;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public RuntimePropertyInfo getInfo() {
        return this.propertyInfo;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void serializeURIs(BeanT o, XMLSerializer w) throws SAXException, AccessorException {
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public boolean hasSerializeURIAction() {
        return false;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void wrapUp() {
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public boolean isHiddenByOverride() {
        return this.hiddenByOverride;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void setHiddenByOverride(boolean hidden) {
        this.hiddenByOverride = hidden;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public String getFieldName() {
        return this.fieldName;
    }
}
