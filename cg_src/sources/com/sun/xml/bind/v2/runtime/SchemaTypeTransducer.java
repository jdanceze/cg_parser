package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/SchemaTypeTransducer.class */
public class SchemaTypeTransducer<V> extends FilterTransducer<V> {
    private final QName schemaType;

    public SchemaTypeTransducer(Transducer<V> core, QName schemaType) {
        super(core);
        this.schemaType = schemaType;
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public CharSequence print(V o) throws AccessorException {
        XMLSerializer w = XMLSerializer.getInstance();
        QName old = w.setSchemaType(this.schemaType);
        try {
            CharSequence print = this.core.print(o);
            w.setSchemaType(old);
            return print;
        } catch (Throwable th) {
            w.setSchemaType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        QName old = w.setSchemaType(this.schemaType);
        try {
            this.core.writeText(w, o, fieldName);
            w.setSchemaType(old);
        } catch (Throwable th) {
            w.setSchemaType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.FilterTransducer, com.sun.xml.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        QName old = w.setSchemaType(this.schemaType);
        try {
            this.core.writeLeafElement(w, tagName, o, fieldName);
            w.setSchemaType(old);
        } catch (Throwable th) {
            w.setSchemaType(old);
            throw th;
        }
    }
}
