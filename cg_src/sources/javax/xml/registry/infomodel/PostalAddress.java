package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/PostalAddress.class */
public interface PostalAddress extends ExtensibleObject {
    String getStreet() throws JAXRException;

    void setStreet(String str) throws JAXRException;

    String getStreetNumber() throws JAXRException;

    void setStreetNumber(String str) throws JAXRException;

    String getCity() throws JAXRException;

    void setCity(String str) throws JAXRException;

    String getStateOrProvince() throws JAXRException;

    void setStateOrProvince(String str) throws JAXRException;

    String getPostalCode() throws JAXRException;

    void setPostalCode(String str) throws JAXRException;

    String getCountry() throws JAXRException;

    void setCountry(String str) throws JAXRException;

    String getType() throws JAXRException;

    void setType(String str) throws JAXRException;

    void setPostalScheme(ClassificationScheme classificationScheme) throws JAXRException;

    ClassificationScheme getPostalScheme() throws JAXRException;
}
