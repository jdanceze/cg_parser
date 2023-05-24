package javax.resource.spi;

import java.io.PrintWriter;
import javax.resource.ResourceException;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ManagedConnection.class */
public interface ManagedConnection {
    Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException;

    void destroy() throws ResourceException;

    void cleanup() throws ResourceException;

    void associateConnection(Object obj) throws ResourceException;

    void addConnectionEventListener(ConnectionEventListener connectionEventListener);

    void removeConnectionEventListener(ConnectionEventListener connectionEventListener);

    XAResource getXAResource() throws ResourceException;

    LocalTransaction getLocalTransaction() throws ResourceException;

    ManagedConnectionMetaData getMetaData() throws ResourceException;

    void setLogWriter(PrintWriter printWriter) throws ResourceException;

    PrintWriter getLogWriter() throws ResourceException;
}
