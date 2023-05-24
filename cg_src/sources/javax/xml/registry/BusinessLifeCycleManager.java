package javax.xml.registry;

import java.util.Collection;
import javax.xml.registry.infomodel.Association;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/BusinessLifeCycleManager.class */
public interface BusinessLifeCycleManager extends LifeCycleManager {
    BulkResponse saveOrganizations(Collection collection) throws JAXRException;

    BulkResponse saveServices(Collection collection) throws JAXRException;

    BulkResponse saveServiceBindings(Collection collection) throws JAXRException;

    BulkResponse saveConcepts(Collection collection) throws JAXRException;

    BulkResponse saveClassificationSchemes(Collection collection) throws JAXRException;

    BulkResponse saveAssociations(Collection collection, boolean z) throws JAXRException;

    BulkResponse deleteOrganizations(Collection collection) throws JAXRException;

    BulkResponse deleteServices(Collection collection) throws JAXRException;

    BulkResponse deleteServiceBindings(Collection collection) throws JAXRException;

    BulkResponse deleteConcepts(Collection collection) throws JAXRException;

    BulkResponse deleteClassificationSchemes(Collection collection) throws JAXRException;

    BulkResponse deleteAssociations(Collection collection) throws JAXRException;

    void confirmAssociation(Association association) throws JAXRException, InvalidRequestException;

    void unConfirmAssociation(Association association) throws JAXRException, InvalidRequestException;
}
