package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/ProxyLoader.class */
public abstract class ProxyLoader extends Loader {
    protected abstract Loader selectLoader(UnmarshallingContext.State state, TagName tagName) throws SAXException;

    public ProxyLoader() {
        super(false);
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        Loader loader = selectLoader(state, ea);
        state.setLoader(loader);
        loader.startElement(state, ea);
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public final void leaveElement(UnmarshallingContext.State state, TagName ea) {
        throw new IllegalStateException();
    }
}
