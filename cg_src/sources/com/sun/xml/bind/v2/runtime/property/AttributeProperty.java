package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.util.QNameMap;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/AttributeProperty.class */
public final class AttributeProperty<BeanT> extends PropertyImpl<BeanT> implements Comparable<AttributeProperty> {
    public final Name attName;
    public final TransducedAccessor<BeanT> xacc;
    private final Accessor acc;

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ String getFieldName() {
        return super.getFieldName();
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ void setHiddenByOverride(boolean z) {
        super.setHiddenByOverride(z);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ boolean isHiddenByOverride() {
        return super.isHiddenByOverride();
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ void wrapUp() {
        super.wrapUp();
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ Accessor getElementPropertyAccessor(String str, String str2) {
        return super.getElementPropertyAccessor(str, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ void serializeBody(Object obj, XMLSerializer xMLSerializer, Object obj2) throws SAXException, AccessorException, IOException, XMLStreamException {
        super.serializeBody(obj, xMLSerializer, obj2);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ RuntimePropertyInfo getInfo() {
        return super.getInfo();
    }

    public AttributeProperty(JAXBContextImpl context, RuntimeAttributePropertyInfo prop) {
        super(context, prop);
        this.attName = context.nameBuilder.createAttributeName(prop.getXmlName());
        this.xacc = TransducedAccessor.get(context, prop);
        this.acc = prop.getAccessor();
    }

    public void serializeAttributes(BeanT o, XMLSerializer w) throws SAXException, AccessorException, IOException, XMLStreamException {
        CharSequence value = this.xacc.print(o);
        if (value != null) {
            w.attribute(this.attName, value.toString());
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public void serializeURIs(BeanT o, XMLSerializer w) throws AccessorException, SAXException {
        this.xacc.declareNamespace(o, w);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public boolean hasSerializeURIAction() {
        return this.xacc.useNamespace();
    }

    @Override // com.sun.xml.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chainElem, QNameMap<ChildLoader> handlers) {
        throw new IllegalStateException();
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.ATTRIBUTE;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void reset(BeanT o) throws AccessorException {
        this.acc.set(o, null);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public String getIdValue(BeanT bean) throws AccessorException, SAXException {
        return this.xacc.print(bean).toString();
    }

    @Override // java.lang.Comparable
    public int compareTo(AttributeProperty that) {
        return this.attName.compareTo(that.attName);
    }
}
