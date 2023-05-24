package javax.xml.registry;

import java.util.Collection;
import java.util.Locale;
import javax.activation.DataHandler;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.ExternalIdentifier;
import javax.xml.registry.infomodel.ExternalLink;
import javax.xml.registry.infomodel.ExtrinsicObject;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.LocalizedString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.RegistryPackage;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;
import javax.xml.registry.infomodel.SpecificationLink;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/LifeCycleManager.class */
public interface LifeCycleManager {
    public static final String ASSOCIATION = "Association";
    public static final String AUDITABLE_EVENT = "AuditableEvent";
    public static final String CLASSIFICATION = "Classification";
    public static final String CLASSIFICATION_SCHEME = "ClassificationScheme";
    public static final String CONCEPT = "Concept";
    public static final String EMAIL_ADDRESS = "EmailAddress";
    public static final String EXTERNAL_IDENTIFIER = "ExternalIdentifier";
    public static final String EXTERNAL_LINK = "ExternalLink";
    public static final String EXTRINSIC_OBJECT = "ExtrinsicObject";
    public static final String INTERNATIONAL_STRING = "InternationalString";
    public static final String KEY = "Key";
    public static final String LOCALIZED_STRING = "LocalizedString";
    public static final String ORGANIZATION = "Organization";
    public static final String PERSON_NAME = "PersonName";
    public static final String POSTAL_ADDRESS = "PostalAddress";
    public static final String REGISTRY_ENTRY = "RegistryEntry";
    public static final String REGISTRY_PACKAGE = "RegistryPackage";
    public static final String SERVICE = "Service";
    public static final String SERVICE_BINDING = "ServiceBinding";
    public static final String SLOT = "Slot";
    public static final String SPECIFICATION_LINK = "SpecificationLink";
    public static final String TELEPHONE_NUMBER = "TelephoneNumber";
    public static final String USER = "User";
    public static final String VERSIONABLE = "Versionable";

    Object createObject(String str) throws JAXRException, InvalidRequestException, UnsupportedCapabilityException;

    Association createAssociation(RegistryObject registryObject, Concept concept) throws JAXRException;

    Classification createClassification(ClassificationScheme classificationScheme, String str, String str2) throws JAXRException;

    Classification createClassification(ClassificationScheme classificationScheme, InternationalString internationalString, String str) throws JAXRException;

    Classification createClassification(Concept concept) throws JAXRException, InvalidRequestException;

    ClassificationScheme createClassificationScheme(String str, String str2) throws JAXRException, InvalidRequestException;

    ClassificationScheme createClassificationScheme(InternationalString internationalString, InternationalString internationalString2) throws JAXRException, InvalidRequestException;

    ClassificationScheme createClassificationScheme(Concept concept) throws JAXRException, InvalidRequestException;

    Concept createConcept(RegistryObject registryObject, String str, String str2) throws JAXRException;

    Concept createConcept(RegistryObject registryObject, InternationalString internationalString, String str) throws JAXRException;

    EmailAddress createEmailAddress(String str) throws JAXRException;

    EmailAddress createEmailAddress(String str, String str2) throws JAXRException;

    ExternalIdentifier createExternalIdentifier(ClassificationScheme classificationScheme, String str, String str2) throws JAXRException;

    ExternalIdentifier createExternalIdentifier(ClassificationScheme classificationScheme, InternationalString internationalString, String str) throws JAXRException;

    ExternalLink createExternalLink(String str, String str2) throws JAXRException;

    ExternalLink createExternalLink(String str, InternationalString internationalString) throws JAXRException;

    ExtrinsicObject createExtrinsicObject(DataHandler dataHandler) throws JAXRException;

    InternationalString createInternationalString() throws JAXRException;

    InternationalString createInternationalString(String str) throws JAXRException;

    InternationalString createInternationalString(Locale locale, String str) throws JAXRException;

    Key createKey(String str) throws JAXRException;

    LocalizedString createLocalizedString(Locale locale, String str) throws JAXRException;

    LocalizedString createLocalizedString(Locale locale, String str, String str2) throws JAXRException;

    Organization createOrganization(String str) throws JAXRException;

    Organization createOrganization(InternationalString internationalString) throws JAXRException;

    PersonName createPersonName(String str, String str2, String str3) throws JAXRException;

    PersonName createPersonName(String str) throws JAXRException;

    PostalAddress createPostalAddress(String str, String str2, String str3, String str4, String str5, String str6, String str7) throws JAXRException;

    RegistryPackage createRegistryPackage(String str) throws JAXRException;

    RegistryPackage createRegistryPackage(InternationalString internationalString) throws JAXRException;

    Service createService(String str) throws JAXRException;

    Service createService(InternationalString internationalString) throws JAXRException;

    ServiceBinding createServiceBinding() throws JAXRException;

    Slot createSlot(String str, String str2, String str3) throws JAXRException;

    Slot createSlot(String str, Collection collection, String str2) throws JAXRException;

    SpecificationLink createSpecificationLink() throws JAXRException;

    TelephoneNumber createTelephoneNumber() throws JAXRException;

    User createUser() throws JAXRException;

    BulkResponse saveObjects(Collection collection) throws JAXRException;

    BulkResponse deprecateObjects(Collection collection) throws JAXRException;

    BulkResponse unDeprecateObjects(Collection collection) throws JAXRException;

    BulkResponse deleteObjects(Collection collection) throws JAXRException;

    BulkResponse deleteObjects(Collection collection, String str) throws JAXRException;

    RegistryService getRegistryService() throws JAXRException;
}
