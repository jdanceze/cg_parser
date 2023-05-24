package javax.resource.spi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;
import javax.resource.ResourceException;
import javax.security.auth.Subject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ManagedConnectionFactory.class */
public interface ManagedConnectionFactory extends Serializable {
    Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException;

    Object createConnectionFactory() throws ResourceException;

    ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException;

    ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException;

    void setLogWriter(PrintWriter printWriter) throws ResourceException;

    PrintWriter getLogWriter() throws ResourceException;

    int hashCode();

    boolean equals(Object obj);
}
