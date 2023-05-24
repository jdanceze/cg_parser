package javax.xml.rpc.encoding;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/encoding/TypeMapping.class */
public interface TypeMapping {
    String[] getSupportedEncodings();

    void setSupportedEncodings(String[] strArr);

    boolean isRegistered(Class cls, QName qName);

    void register(Class cls, QName qName, SerializerFactory serializerFactory, DeserializerFactory deserializerFactory);

    SerializerFactory getSerializer(Class cls, QName qName);

    DeserializerFactory getDeserializer(Class cls, QName qName);

    void removeSerializer(Class cls, QName qName);

    void removeDeserializer(Class cls, QName qName);
}
