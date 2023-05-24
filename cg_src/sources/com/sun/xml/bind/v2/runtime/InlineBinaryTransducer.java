package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/InlineBinaryTransducer.class */
public class InlineBinaryTransducer<V> extends FilterTransducer<V> {
    public InlineBinaryTransducer(Transducer<V> core) {
        super(core);
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    @NotNull
    public CharSequence print(@NotNull V o) throws AccessorException {
        XMLSerializer w = XMLSerializer.getInstance();
        boolean old = w.setInlineBinaryFlag(true);
        try {
            CharSequence print = this.core.print(o);
            w.setInlineBinaryFlag(old);
            return print;
        } catch (Throwable th) {
            w.setInlineBinaryFlag(old);
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        boolean old = w.setInlineBinaryFlag(true);
        try {
            this.core.writeText(w, o, fieldName);
            w.setInlineBinaryFlag(old);
        } catch (Throwable th) {
            w.setInlineBinaryFlag(old);
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        boolean old = w.setInlineBinaryFlag(true);
        try {
            this.core.writeLeafElement(w, tagName, o, fieldName);
            w.setInlineBinaryFlag(old);
        } catch (Throwable th) {
            w.setInlineBinaryFlag(old);
            throw th;
        }
    }
}
