package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/Connection.class */
public interface Connection {
    Interaction createInteraction() throws ResourceException;

    LocalTransaction getLocalTransaction() throws ResourceException;

    ConnectionMetaData getMetaData() throws ResourceException;

    ResultSetInfo getResultSetInfo() throws ResourceException;

    void close() throws ResourceException;
}
