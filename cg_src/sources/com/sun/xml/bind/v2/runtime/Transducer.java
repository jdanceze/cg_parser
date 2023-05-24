package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/Transducer.class */
public interface Transducer<ValueT> {
    boolean useNamespace();

    void declareNamespace(ValueT valuet, XMLSerializer xMLSerializer) throws AccessorException;

    @NotNull
    CharSequence print(@NotNull ValueT valuet) throws AccessorException;

    ValueT parse(CharSequence charSequence) throws AccessorException, SAXException;

    void writeText(XMLSerializer xMLSerializer, ValueT valuet, String str) throws IOException, SAXException, XMLStreamException, AccessorException;

    void writeLeafElement(XMLSerializer xMLSerializer, Name name, @NotNull ValueT valuet, String str) throws IOException, SAXException, XMLStreamException, AccessorException;

    QName getTypeName(@NotNull ValueT valuet);
}
