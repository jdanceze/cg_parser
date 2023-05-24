package javax.xml.bind;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/JAXBContextFactory.class */
public interface JAXBContextFactory {
    JAXBContext createContext(Class<?>[] clsArr, Map<String, ?> map) throws JAXBException;

    JAXBContext createContext(String str, ClassLoader classLoader, Map<String, ?> map) throws JAXBException;
}
