package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.v2.model.runtime.RuntimeArrayInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ArrayBeanInfoImpl.class */
public final class ArrayBeanInfoImpl extends JaxBeanInfo {
    private final Class itemType;
    private final JaxBeanInfo itemBeanInfo;
    private Loader loader;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.sun.xml.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo] */
    public ArrayBeanInfoImpl(JAXBContextImpl owner, RuntimeArrayInfo rai) {
        super(owner, (RuntimeTypeInfo) rai, rai.getType(), rai.getTypeName(), false, true, false);
        this.itemType = this.jaxbType.getComponentType();
        this.itemBeanInfo = owner.getOrCreate((RuntimeTypeInfo) rai.getItemType());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void link(JAXBContextImpl grammar) {
        getLoader(grammar, false);
        super.link(grammar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ArrayBeanInfoImpl$ArrayLoader.class */
    public final class ArrayLoader extends Loader implements Receiver {
        private final Loader itemLoader;

        public ArrayLoader(JAXBContextImpl owner) {
            super(false);
            this.itemLoader = ArrayBeanInfoImpl.this.itemBeanInfo.getLoader(owner, true);
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
        public void startElement(UnmarshallingContext.State state, TagName ea) {
            state.setTarget(new ArrayList());
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
        public void leaveElement(UnmarshallingContext.State state, TagName ea) {
            state.setTarget(ArrayBeanInfoImpl.this.toArray((List) state.getTarget()));
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
        public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            if (ea.matches("", "item")) {
                state.setLoader(this.itemLoader);
                state.setReceiver(this);
                return;
            }
            super.childElement(state, ea);
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
        public Collection<QName> getExpectedChildElements() {
            return Collections.singleton(new QName("", "item"));
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Receiver
        public void receive(UnmarshallingContext.State state, Object o) {
            ((List) state.getTarget()).add(o);
        }
    }

    protected Object toArray(List list) {
        int len = list.size();
        Object array = Array.newInstance(this.itemType, len);
        for (int i = 0; i < len; i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++) {
            Object item = Array.get(array, i);
            target.startElement("", "item", null, null);
            if (item == null) {
                target.writeXsiNilTrue();
            } else {
                target.childAsXsiType(item, "arrayItem", this.itemBeanInfo, false);
            }
            target.endElement();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final String getElementNamespaceURI(Object array) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final String getElementLocalName(Object array) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final Object createInstance(UnmarshallingContext context) {
        return new ArrayList();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final boolean reset(Object array, UnmarshallingContext context) {
        return false;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final String getId(Object array, XMLSerializer target) {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final void serializeAttributes(Object array, XMLSerializer target) {
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final void serializeRoot(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(array.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final void serializeURIs(Object array, XMLSerializer target) {
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final Transducer getTransducer() {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (this.loader == null) {
            this.loader = new ArrayLoader(context);
        }
        return this.loader;
    }
}
