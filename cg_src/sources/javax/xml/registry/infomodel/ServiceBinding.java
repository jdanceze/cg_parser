package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/ServiceBinding.class */
public interface ServiceBinding extends RegistryObject, URIValidator {
    String getAccessURI() throws JAXRException;

    void setAccessURI(String str) throws JAXRException;

    ServiceBinding getTargetBinding() throws JAXRException;

    void setTargetBinding(ServiceBinding serviceBinding) throws JAXRException;

    Service getService() throws JAXRException;

    void addSpecificationLink(SpecificationLink specificationLink) throws JAXRException;

    void addSpecificationLinks(Collection collection) throws JAXRException;

    void removeSpecificationLink(SpecificationLink specificationLink) throws JAXRException;

    void removeSpecificationLinks(Collection collection) throws JAXRException;

    Collection getSpecificationLinks() throws JAXRException;
}
