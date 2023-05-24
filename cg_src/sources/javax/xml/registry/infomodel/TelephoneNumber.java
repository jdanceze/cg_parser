package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/TelephoneNumber.class */
public interface TelephoneNumber {
    String getCountryCode() throws JAXRException;

    String getAreaCode() throws JAXRException;

    String getNumber() throws JAXRException;

    String getExtension() throws JAXRException;

    String getUrl() throws JAXRException;

    String getType() throws JAXRException;

    void setCountryCode(String str) throws JAXRException;

    void setAreaCode(String str) throws JAXRException;

    void setNumber(String str) throws JAXRException;

    void setExtension(String str) throws JAXRException;

    void setUrl(String str) throws JAXRException;

    void setType(String str) throws JAXRException;
}
