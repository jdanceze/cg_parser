package javax.xml.bind.annotation.adapters;

import javax.xml.bind.DatatypeConverter;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/adapters/HexBinaryAdapter.class */
public final class HexBinaryAdapter extends XmlAdapter<String, byte[]> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public byte[] unmarshal(String s) {
        if (s == null) {
            return null;
        }
        return DatatypeConverter.parseHexBinary(s);
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return DatatypeConverter.printHexBinary(bytes);
    }
}
