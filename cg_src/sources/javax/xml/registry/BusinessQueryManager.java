package javax.xml.registry;

import java.util.Collection;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.Key;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/BusinessQueryManager.class */
public interface BusinessQueryManager extends QueryManager {
    BulkResponse findAssociations(Collection collection, String str, String str2, Collection collection2) throws JAXRException;

    BulkResponse findCallerAssociations(Collection collection, Boolean bool, Boolean bool2, Collection collection2) throws JAXRException;

    BulkResponse findOrganizations(Collection collection, Collection collection2, Collection collection3, Collection collection4, Collection collection5, Collection collection6) throws JAXRException;

    BulkResponse findServices(Key key, Collection collection, Collection collection2, Collection collection3, Collection collection4) throws JAXRException;

    BulkResponse findServiceBindings(Key key, Collection collection, Collection collection2, Collection collection3) throws JAXRException;

    BulkResponse findClassificationSchemes(Collection collection, Collection collection2, Collection collection3, Collection collection4) throws JAXRException;

    ClassificationScheme findClassificationSchemeByName(Collection collection, String str) throws JAXRException;

    BulkResponse findConcepts(Collection collection, Collection collection2, Collection collection3, Collection collection4, Collection collection5) throws JAXRException;

    Concept findConceptByPath(String str) throws JAXRException;

    BulkResponse findRegistryPackages(Collection collection, Collection collection2, Collection collection3, Collection collection4) throws JAXRException;
}
