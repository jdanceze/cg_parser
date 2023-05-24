package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.api.AccessorException;
import java.io.IOException;
import javax.activation.MimeType;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/MimeTypedTransducer.class */
public final class MimeTypedTransducer<V> extends FilterTransducer<V> {
    private final MimeType expectedMimeType;

    public MimeTypedTransducer(Transducer<V> core, MimeType expectedMimeType) {
        super(core);
        this.expectedMimeType = expectedMimeType;
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public CharSequence print(V o) throws AccessorException {
        XMLSerializer w = XMLSerializer.getInstance();
        MimeType old = w.setExpectedMimeType(this.expectedMimeType);
        try {
            CharSequence print = this.core.print(o);
            w.setExpectedMimeType(old);
            return print;
        } catch (Throwable th) {
            w.setExpectedMimeType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        MimeType old = w.setExpectedMimeType(this.expectedMimeType);
        try {
            this.core.writeText(w, o, fieldName);
            w.setExpectedMimeType(old);
        } catch (Throwable th) {
            w.setExpectedMimeType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        MimeType old = w.setExpectedMimeType(this.expectedMimeType);
        try {
            this.core.writeLeafElement(w, tagName, o, fieldName);
            w.setExpectedMimeType(old);
        } catch (Throwable th) {
            w.setExpectedMimeType(old);
            throw th;
        }
    }
}
