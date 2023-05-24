package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/Query.class */
public interface Query {
    public static final int QUERY_TYPE_SQL = 0;
    public static final int QUERY_TYPE_XQUERY = 1;
    public static final int QUERY_TYPE_EBXML_FILTER_QUERY = 2;

    int getType() throws JAXRException;

    String toString();
}
