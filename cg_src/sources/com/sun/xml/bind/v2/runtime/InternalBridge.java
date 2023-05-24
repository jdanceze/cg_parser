package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.api.Bridge;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/InternalBridge.class */
public abstract class InternalBridge<T> extends Bridge<T> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void marshal(T t, XMLSerializer xMLSerializer) throws IOException, SAXException, XMLStreamException;

    /* JADX INFO: Access modifiers changed from: protected */
    public InternalBridge(JAXBContextImpl context) {
        super(context);
    }

    @Override // com.sun.xml.bind.api.Bridge
    public JAXBContextImpl getContext() {
        return this.context;
    }
}
