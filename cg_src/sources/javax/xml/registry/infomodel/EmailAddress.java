package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/EmailAddress.class */
public interface EmailAddress {
    String getAddress() throws JAXRException;

    void setAddress(String str) throws JAXRException;

    String getType() throws JAXRException;

    void setType(String str) throws JAXRException;
}
