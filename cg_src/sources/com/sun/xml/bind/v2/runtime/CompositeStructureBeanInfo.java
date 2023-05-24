package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.api.CompositeStructure;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/CompositeStructureBeanInfo.class */
public class CompositeStructureBeanInfo extends JaxBeanInfo<CompositeStructure> {
    public CompositeStructureBeanInfo(JAXBContextImpl context) {
        super(context, null, CompositeStructure.class, false, true, false);
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(CompositeStructure o) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(CompositeStructure o) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public CompositeStructure createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException, SAXException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public boolean reset(CompositeStructure o, UnmarshallingContext context) throws SAXException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getId(CompositeStructure o, XMLSerializer target) throws SAXException {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(CompositeStructure o, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(o.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(CompositeStructure o, XMLSerializer target) throws SAXException {
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(CompositeStructure o, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(CompositeStructure o, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        int len = o.bridges.length;
        for (int i = 0; i < len; i++) {
            Object value = o.values[i];
            InternalBridge bi = (InternalBridge) o.bridges[i];
            bi.marshal((InternalBridge) value, target);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public Transducer<CompositeStructure> getTransducer() {
        return null;
    }
}
