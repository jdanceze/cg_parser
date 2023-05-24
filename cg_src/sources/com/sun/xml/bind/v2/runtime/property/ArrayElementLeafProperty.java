package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/ArrayElementLeafProperty.class */
final class ArrayElementLeafProperty<BeanT, ListT, ItemT> extends ArrayElementProperty<BeanT, ListT, ItemT> {
    private final Transducer<ItemT> xducer;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayElementLeafProperty.class.desiredAssertionStatus();
    }

    public ArrayElementLeafProperty(JAXBContextImpl p, RuntimeElementPropertyInfo prop) {
        super(p, prop);
        if (!$assertionsDisabled && prop.getTypes().size() != 1) {
            throw new AssertionError();
        }
        this.xducer = ((RuntimeTypeRef) prop.getTypes().get(0)).getTransducer();
        if (!$assertionsDisabled && this.xducer == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.ArrayElementProperty
    public void serializeItem(JaxBeanInfo bi, ItemT item, XMLSerializer w) throws SAXException, AccessorException, IOException, XMLStreamException {
        this.xducer.declareNamespace(item, w);
        w.endNamespaceDecls(item);
        w.endAttributes();
        this.xducer.writeText(w, item, this.fieldName);
    }
}
