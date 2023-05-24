package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/DefaultValueLoaderDecorator.class */
public final class DefaultValueLoaderDecorator extends Loader {
    private final Loader l;
    private final String defaultValue;

    public DefaultValueLoaderDecorator(Loader l, String defaultValue) {
        this.l = l;
        this.defaultValue = defaultValue;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        if (state.getElementDefaultValue() == null) {
            state.setElementDefaultValue(this.defaultValue);
        }
        state.setLoader(this.l);
        this.l.startElement(state, ea);
    }
}
