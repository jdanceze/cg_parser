package javax.xml.registry;

import java.util.Collection;
import javax.xml.registry.infomodel.RegistryObject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/QueryManager.class */
public interface QueryManager {
    RegistryObject getRegistryObject(String str, String str2) throws JAXRException;

    RegistryObject getRegistryObject(String str) throws JAXRException;

    BulkResponse getRegistryObjects(Collection collection) throws JAXRException;

    BulkResponse getRegistryObjects(Collection collection, String str) throws JAXRException;

    BulkResponse getRegistryObjects() throws JAXRException;

    BulkResponse getRegistryObjects(String str) throws JAXRException;

    RegistryService getRegistryService() throws JAXRException;
}
