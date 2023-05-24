package javax.xml.registry.samples;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.DeclarativeQueryManager;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/samples/SimpleClient.class */
public class SimpleClient {
    Set credentials = null;

    private void doit() throws JAXRException, NamingException, MalformedURLException {
        ConnectionFactory connectionFactory = (ConnectionFactory) new InitialContext().lookup("JAXRConnectionFactory");
        Properties properties = new Properties();
        properties.put("javax.xml.registry.factoryClass", "com.sun.xml.registry.ConnectionFactory");
        properties.put("javax.xml.registry.registryURL", "http://java.sun.com/ebXMLRegistry");
        connectionFactory.setProperties(properties);
        Connection createConnection = connectionFactory.createConnection();
        createConnection.setCredentials(this.credentials);
        RegistryService registryService = createConnection.getRegistryService();
        registryService.getBusinessLifeCycleManager().saveObjects(new ArrayList());
        BusinessQueryManager businessQueryManager = registryService.getBusinessQueryManager();
        ArrayList arrayList = new ArrayList();
        arrayList.add("Acme");
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(FindQualifier.EXACT_NAME_MATCH);
        businessQueryManager.findOrganizations(arrayList2, arrayList, null, null, null, null).getCollection();
        DeclarativeQueryManager declarativeQueryManager = registryService.getDeclarativeQueryManager();
        declarativeQueryManager.executeQuery(declarativeQueryManager.createQuery(0, "SELECT FROM RegistryObject WHERE objectType = 'BusinessListing' AND name LIKE '%Acme%'"));
    }

    public static void main(String[] strArr) {
        try {
            new SimpleClient().doit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
