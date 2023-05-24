package javax.xml.registry.samples;

import java.util.ArrayList;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.JAXRException;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;
import javax.xml.registry.infomodel.SpecificationLink;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.User;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/samples/SaveOrganizationTest.class */
public class SaveOrganizationTest {
    BusinessQueryManager bqm;
    BusinessLifeCycleManager lcm;

    public static void main(String[] strArr) {
        new SaveOrganizationTest().saveOrganizationTest();
    }

    public SaveOrganizationTest() {
        this.bqm = null;
        this.lcm = null;
        try {
            ConnectionFactory connectionFactory = (ConnectionFactory) new InitialContext().lookup("javax.xml.registry.ConnectionFactory");
            Properties properties = new Properties();
            properties.put("javax.xml.registry.factoryClass", "com.sun.xml.registry.ConnectionFactory");
            properties.put("javax.xml.registry.queryManagerURL", "http://java.sun.com/uddi/inquiry");
            properties.put("javax.xml.registry.lifeCycleManagerURL", "http://java.sun.com/uddi/publish");
            connectionFactory.setProperties(properties);
            Connection createConnection = connectionFactory.createConnection();
            createConnection.setCredentials(null);
            RegistryService registryService = createConnection.getRegistryService();
            this.lcm = registryService.getBusinessLifeCycleManager();
            this.bqm = registryService.getBusinessQueryManager();
        } catch (JAXRException e) {
            e.printStackTrace();
        } catch (NamingException e2) {
            e2.printStackTrace();
        }
    }

    public void saveOrganizationTest() {
        try {
            BusinessLifeCycleManager businessLifeCycleManager = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager2 = this.lcm;
            Organization organization = (Organization) businessLifeCycleManager.createObject(LifeCycleManager.ORGANIZATION);
            organization.setName(this.lcm.createInternationalString("Acme Systems"));
            organization.setDescription(this.lcm.createInternationalString("A high tech gadget manufacturer"));
            BusinessLifeCycleManager businessLifeCycleManager3 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager4 = this.lcm;
            PersonName personName = (PersonName) businessLifeCycleManager3.createObject(LifeCycleManager.PERSON_NAME);
            personName.setFullName("Waqar Sadiq");
            BusinessLifeCycleManager businessLifeCycleManager5 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager6 = this.lcm;
            User user = (User) businessLifeCycleManager5.createObject(LifeCycleManager.USER);
            user.setPersonName(personName);
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.lcm.createEmailAddress("waqar.sadiq@eds.com"));
            user.setEmailAddresses(arrayList);
            BusinessLifeCycleManager businessLifeCycleManager7 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager8 = this.lcm;
            TelephoneNumber telephoneNumber = (TelephoneNumber) businessLifeCycleManager7.createObject(LifeCycleManager.TELEPHONE_NUMBER);
            telephoneNumber.setNumber("972-797-8408");
            telephoneNumber.setType(null);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(telephoneNumber);
            user.setTelephoneNumbers(arrayList2);
            BusinessLifeCycleManager businessLifeCycleManager9 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager10 = this.lcm;
            Slot slot = (Slot) businessLifeCycleManager9.createObject(LifeCycleManager.SLOT);
            slot.setName(Slot.SORT_CODE_SLOT);
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(new String("546789"));
            slot.setValues(arrayList3);
            slot.setSlotType(null);
            new String("5400 Legacy Drive");
            new String("Plano");
            new String("TX");
            new String("75024");
            PostalAddress createPostalAddress = this.lcm.createPostalAddress("5400", "Legacy Drive", "Plano", "TX", "USA", "75024", null);
            createPostalAddress.addSlot(slot);
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(createPostalAddress);
            user.setPostalAddresses(arrayList4);
            organization.setPrimaryContact(user);
            organization.addExternalIdentifier(this.lcm.createExternalIdentifier(this.bqm.findClassificationSchemeByName(null, "%D-U-N-S"), "Sun Microsystems", "123456789"));
            organization.addClassification(this.lcm.createClassification(this.bqm.findClassificationSchemeByName(null, "%naics"), "NAICS product code", "12345"));
            BusinessLifeCycleManager businessLifeCycleManager11 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager12 = this.lcm;
            Service service = (Service) businessLifeCycleManager11.createObject(LifeCycleManager.SERVICE);
            service.setName(this.lcm.createInternationalString("purchasing service"));
            organization.addService(service);
            BusinessLifeCycleManager businessLifeCycleManager13 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager14 = this.lcm;
            ServiceBinding serviceBinding = (ServiceBinding) businessLifeCycleManager13.createObject(LifeCycleManager.SERVICE_BINDING);
            serviceBinding.setName(this.lcm.createInternationalString("purchasing service SOAP binding"));
            serviceBinding.setAccessURI("http://www.sun.com/purchasingService");
            service.addServiceBinding(serviceBinding);
            BusinessLifeCycleManager businessLifeCycleManager15 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager16 = this.lcm;
            SpecificationLink specificationLink = (SpecificationLink) businessLifeCycleManager15.createObject(LifeCycleManager.SPECIFICATION_LINK);
            specificationLink.setName(this.lcm.createInternationalString("purchasing service SOAP binding SpecificationLink"));
            specificationLink.setUsageDescription(this.lcm.createInternationalString("Use the WSDL file with a tool like JAXRPC to call this service"));
            serviceBinding.addSpecificationLink(specificationLink);
            BusinessLifeCycleManager businessLifeCycleManager17 = this.lcm;
            BusinessLifeCycleManager businessLifeCycleManager18 = this.lcm;
            Concept concept = (Concept) businessLifeCycleManager17.createObject(LifeCycleManager.CONCEPT);
            concept.setDescription(this.lcm.createInternationalString("purchasing service SOAP binding WSDL file's proxy"));
            specificationLink.setSpecificationObject(concept);
            concept.addExternalLink(this.lcm.createExternalLink("http://www.sun.com/purchasing/purchasingSOAPBinding.wsdl", "ExternalLink to WSDL file for SOAP binding for purchasing service"));
            ArrayList arrayList5 = new ArrayList();
            arrayList5.add(organization);
            this.lcm.saveOrganizations(arrayList5);
        } catch (JAXRException e) {
            e.printStackTrace();
        }
    }
}
