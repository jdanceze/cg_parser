package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/RegistryObject.class */
public interface RegistryObject extends ExtensibleObject {
    Key getKey() throws JAXRException;

    InternationalString getDescription() throws JAXRException;

    void setDescription(InternationalString internationalString) throws JAXRException;

    InternationalString getName() throws JAXRException;

    void setName(InternationalString internationalString) throws JAXRException;

    void setKey(Key key) throws JAXRException;

    String toXML() throws JAXRException;

    void addClassification(Classification classification) throws JAXRException;

    void addClassifications(Collection collection) throws JAXRException;

    void removeClassification(Classification classification) throws JAXRException;

    void removeClassifications(Collection collection) throws JAXRException;

    void setClassifications(Collection collection) throws JAXRException;

    Collection getClassifications() throws JAXRException;

    Collection getAuditTrail() throws JAXRException;

    void addAssociation(Association association) throws JAXRException;

    void addAssociations(Collection collection) throws JAXRException;

    void removeAssociation(Association association) throws JAXRException;

    void removeAssociations(Collection collection) throws JAXRException;

    void setAssociations(Collection collection) throws JAXRException;

    Collection getAssociations() throws JAXRException;

    Collection getAssociatedObjects() throws JAXRException;

    void addExternalIdentifier(ExternalIdentifier externalIdentifier) throws JAXRException;

    void addExternalIdentifiers(Collection collection) throws JAXRException;

    void removeExternalIdentifier(ExternalIdentifier externalIdentifier) throws JAXRException;

    void removeExternalIdentifiers(Collection collection) throws JAXRException;

    void setExternalIdentifiers(Collection collection) throws JAXRException;

    Collection getExternalIdentifiers() throws JAXRException;

    void addExternalLink(ExternalLink externalLink) throws JAXRException;

    void addExternalLinks(Collection collection) throws JAXRException;

    void removeExternalLink(ExternalLink externalLink) throws JAXRException;

    void removeExternalLinks(Collection collection) throws JAXRException;

    void setExternalLinks(Collection collection) throws JAXRException;

    Collection getExternalLinks() throws JAXRException;

    Concept getObjectType() throws JAXRException;

    Organization getSubmittingOrganization() throws JAXRException;

    Collection getRegistryPackages() throws JAXRException;

    LifeCycleManager getLifeCycleManager() throws JAXRException;
}
