package com.sun.xml.txw2;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/TypedXmlWriter.class */
public interface TypedXmlWriter {
    void commit();

    void commit(boolean z);

    void block();

    Document getDocument();

    void _attribute(String str, Object obj);

    void _attribute(String str, String str2, Object obj);

    void _attribute(QName qName, Object obj);

    void _namespace(String str);

    void _namespace(String str, String str2);

    void _namespace(String str, boolean z);

    void _pcdata(Object obj);

    void _cdata(Object obj);

    void _comment(Object obj) throws UnsupportedOperationException;

    <T extends TypedXmlWriter> T _element(String str, Class<T> cls);

    <T extends TypedXmlWriter> T _element(String str, String str2, Class<T> cls);

    <T extends TypedXmlWriter> T _element(QName qName, Class<T> cls);

    <T extends TypedXmlWriter> T _element(Class<T> cls);

    <T extends TypedXmlWriter> T _cast(Class<T> cls);
}
