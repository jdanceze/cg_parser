package com.sun.xml.bind.v2.runtime;

import com.sun.istack.FinalArrayList;
import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ValueListBeanInfoImpl.class */
public final class ValueListBeanInfoImpl extends JaxBeanInfo {
    private final Class itemType;
    private final Transducer xducer;
    private final Loader loader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ValueListBeanInfoImpl.class.desiredAssertionStatus();
    }

    public ValueListBeanInfoImpl(JAXBContextImpl owner, Class arrayType) throws JAXBException {
        super(owner, null, arrayType, false, true, false);
        this.loader = new Loader(true) { // from class: com.sun.xml.bind.v2.runtime.ValueListBeanInfoImpl.1
            @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
            public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
                List<Object> r = new FinalArrayList<>();
                int idx = 0;
                int len = text.length();
                while (true) {
                    int p = idx;
                    while (p < len && !WhiteSpaceProcessor.isWhiteSpace(text.charAt(p))) {
                        p++;
                    }
                    CharSequence token = text.subSequence(idx, p);
                    if (!token.equals("")) {
                        try {
                            r.add(ValueListBeanInfoImpl.this.xducer.parse(token));
                        } catch (AccessorException e) {
                            handleGenericException(e, true);
                        }
                    }
                    if (p == len) {
                        break;
                    }
                    while (p < len && WhiteSpaceProcessor.isWhiteSpace(text.charAt(p))) {
                        p++;
                    }
                    if (p == len) {
                        break;
                    }
                    idx = p;
                }
                state.setTarget(ValueListBeanInfoImpl.this.toArray(r));
            }
        };
        this.itemType = this.jaxbType.getComponentType();
        this.xducer = owner.getBeanInfo((Class) arrayType.getComponentType(), true).getTransducer();
        if (!$assertionsDisabled && this.xducer == null) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object toArray(List list) {
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
            try {
                this.xducer.writeText(target, item, "arrayItem");
            } catch (AccessorException e) {
                target.reportError("arrayItem", e);
            }
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final void serializeURIs(Object array, XMLSerializer target) throws SAXException {
        if (this.xducer.useNamespace()) {
            int len = Array.getLength(array);
            for (int i = 0; i < len; i++) {
                Object item = Array.get(array, i);
                try {
                    this.xducer.declareNamespace(item, target);
                } catch (AccessorException e) {
                    target.reportError("arrayItem", e);
                }
            }
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
        throw new UnsupportedOperationException();
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
    public final void serializeRoot(Object array, XMLSerializer target) throws SAXException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(array.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final Transducer getTransducer() {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        return this.loader;
    }
}
