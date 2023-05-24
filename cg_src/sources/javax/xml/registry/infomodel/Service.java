package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Service.class */
public interface Service extends RegistryEntry {
    Organization getProvidingOrganization() throws JAXRException;

    void setProvidingOrganization(Organization organization) throws JAXRException;

    void addServiceBinding(ServiceBinding serviceBinding) throws JAXRException;

    void addServiceBindings(Collection collection) throws JAXRException;

    void removeServiceBinding(ServiceBinding serviceBinding) throws JAXRException;

    void removeServiceBindings(Collection collection) throws JAXRException;

    Collection getServiceBindings() throws JAXRException;
}
