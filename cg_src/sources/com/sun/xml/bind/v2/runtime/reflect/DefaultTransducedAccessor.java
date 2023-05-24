package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/DefaultTransducedAccessor.class */
public abstract class DefaultTransducedAccessor<T> extends TransducedAccessor<T> {
    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public abstract String print(T t) throws AccessorException, SAXException;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException, SAXException {
        return print((DefaultTransducedAccessor<T>) obj);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException {
        w.leafElement(tagName, print((DefaultTransducedAccessor<T>) o), fieldName);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void writeText(XMLSerializer w, T o, String fieldName) throws AccessorException, SAXException, IOException, XMLStreamException {
        w.text(print((DefaultTransducedAccessor<T>) o), fieldName);
    }
}
