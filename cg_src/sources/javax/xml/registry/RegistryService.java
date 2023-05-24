package javax.xml.registry;

import javax.xml.registry.infomodel.ClassificationScheme;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/RegistryService.class */
public interface RegistryService {
    CapabilityProfile getCapabilityProfile() throws JAXRException;

    BusinessLifeCycleManager getBusinessLifeCycleManager() throws JAXRException;

    BusinessQueryManager getBusinessQueryManager() throws JAXRException;

    DeclarativeQueryManager getDeclarativeQueryManager() throws JAXRException, UnsupportedCapabilityException;

    BulkResponse getBulkResponse(String str) throws InvalidRequestException, JAXRException;

    ClassificationScheme getDefaultPostalScheme() throws JAXRException;

    String makeRegistrySpecificRequest(String str) throws JAXRException;
}
