package javax.resource.spi;

import java.io.Serializable;
import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ConnectionManager.class */
public interface ConnectionManager extends Serializable {
    Object allocateConnection(ManagedConnectionFactory managedConnectionFactory, ConnectionRequestInfo connectionRequestInfo) throws ResourceException;
}
