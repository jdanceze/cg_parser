package javax.xml.registry.infomodel;

import java.net.URL;
import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/User.class */
public interface User extends RegistryObject {
    Organization getOrganization() throws JAXRException;

    PersonName getPersonName() throws JAXRException;

    void setPersonName(PersonName personName) throws JAXRException;

    Collection getPostalAddresses() throws JAXRException;

    void setPostalAddresses(Collection collection) throws JAXRException;

    URL getUrl() throws JAXRException;

    void setUrl(URL url) throws JAXRException;

    Collection getTelephoneNumbers(String str) throws JAXRException;

    void setTelephoneNumbers(Collection collection) throws JAXRException;

    Collection getEmailAddresses() throws JAXRException;

    void setEmailAddresses(Collection collection) throws JAXRException;

    String getType() throws JAXRException;

    void setType(String str) throws JAXRException;
}
