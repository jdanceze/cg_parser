package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/DeclarativeQueryManager.class */
public interface DeclarativeQueryManager extends QueryManager {
    Query createQuery(int i, String str) throws InvalidRequestException, JAXRException;

    BulkResponse executeQuery(Query query) throws JAXRException;
}
