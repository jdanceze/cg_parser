package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/PersonName.class */
public interface PersonName {
    String getLastName() throws JAXRException;

    void setLastName(String str) throws JAXRException;

    String getFirstName() throws JAXRException;

    void setFirstName(String str) throws JAXRException;

    String getMiddleName() throws JAXRException;

    void setMiddleName(String str) throws JAXRException;

    String getFullName() throws JAXRException;

    void setFullName(String str) throws JAXRException;
}
