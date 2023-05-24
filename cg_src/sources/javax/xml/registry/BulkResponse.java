package javax.xml.registry;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/BulkResponse.class */
public interface BulkResponse extends JAXRResponse {
    Collection getCollection() throws JAXRException;

    Collection getExceptions() throws JAXRException;

    boolean isPartialResponse() throws JAXRException;
}
