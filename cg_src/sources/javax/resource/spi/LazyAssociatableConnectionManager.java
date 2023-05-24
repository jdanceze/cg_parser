package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/LazyAssociatableConnectionManager.class */
public interface LazyAssociatableConnectionManager {
    void associateConnection(Object obj, ManagedConnectionFactory managedConnectionFactory, ConnectionRequestInfo connectionRequestInfo) throws ResourceException;
}
