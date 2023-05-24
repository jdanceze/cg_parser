package javax.xml.bind;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/JAXBIntrospector.class */
public abstract class JAXBIntrospector {
    public abstract boolean isElement(Object obj);

    public abstract QName getElementName(Object obj);

    public static Object getValue(Object jaxbElement) {
        if (jaxbElement instanceof JAXBElement) {
            return ((JAXBElement) jaxbElement).getValue();
        }
        return jaxbElement;
    }
}
