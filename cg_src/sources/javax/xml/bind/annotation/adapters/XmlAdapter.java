package javax.xml.bind.annotation.adapters;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/adapters/XmlAdapter.class */
public abstract class XmlAdapter<ValueType, BoundType> {
    public abstract BoundType unmarshal(ValueType valuetype) throws Exception;

    public abstract ValueType marshal(BoundType boundtype) throws Exception;
}
