package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Organization.class */
public interface Organization extends RegistryObject {
    PostalAddress getPostalAddress() throws JAXRException;

    void setPostalAddress(PostalAddress postalAddress) throws JAXRException;

    User getPrimaryContact() throws JAXRException;

    void setPrimaryContact(User user) throws JAXRException;

    void addUser(User user) throws JAXRException;

    void addUsers(Collection collection) throws JAXRException;

    void removeUser(User user) throws JAXRException;

    void removeUsers(Collection collection) throws JAXRException;

    Collection getUsers() throws JAXRException;

    Collection getTelephoneNumbers(String str) throws JAXRException;

    void setTelephoneNumbers(Collection collection) throws JAXRException;

    void addService(Service service) throws JAXRException;

    void addServices(Collection collection) throws JAXRException;

    void removeService(Service service) throws JAXRException;

    void removeServices(Collection collection) throws JAXRException;

    Collection getServices() throws JAXRException;

    void addChildOrganization(Organization organization) throws JAXRException;

    void addChildOrganizations(Collection collection) throws JAXRException;

    void removeChildOrganization(Organization organization) throws JAXRException;

    void removeChildOrganizations(Collection collection) throws JAXRException;

    int getChildOrganizationCount() throws JAXRException;

    Collection getChildOrganizations() throws JAXRException;

    Collection getDescendantOrganizations() throws JAXRException;

    Organization getParentOrganization() throws JAXRException;

    Organization getRootOrganization() throws JAXRException;
}
