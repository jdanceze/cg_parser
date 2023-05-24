package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/SpecificationLink.class */
public interface SpecificationLink extends RegistryObject {
    RegistryObject getSpecificationObject() throws JAXRException;

    void setSpecificationObject(RegistryObject registryObject) throws JAXRException;

    InternationalString getUsageDescription() throws JAXRException;

    void setUsageDescription(InternationalString internationalString) throws JAXRException;

    Collection getUsageParameters() throws JAXRException;

    void setUsageParameters(Collection collection) throws JAXRException;

    ServiceBinding getServiceBinding() throws JAXRException;
}
