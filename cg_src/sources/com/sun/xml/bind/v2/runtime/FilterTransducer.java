package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/FilterTransducer.class */
public abstract class FilterTransducer<T> implements Transducer<T> {
    protected final Transducer<T> core;

    /* JADX INFO: Access modifiers changed from: protected */
    public FilterTransducer(Transducer<T> core) {
        this.core = core;
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public boolean useNamespace() {
        return this.core.useNamespace();
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public void declareNamespace(T o, XMLSerializer w) throws AccessorException {
        this.core.declareNamespace(o, w);
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    @NotNull
    public CharSequence print(@NotNull T o) throws AccessorException {
        return this.core.print(o);
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public T parse(CharSequence lexical) throws AccessorException, SAXException {
        return this.core.parse(lexical);
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        this.core.writeText(w, o, fieldName);
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
        this.core.writeLeafElement(w, tagName, o, fieldName);
    }

    @Override // com.sun.xml.bind.v2.runtime.Transducer
    public QName getTypeName(T instance) {
        return null;
    }
}
