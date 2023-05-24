package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/LazyEnlistableConnectionManager.class */
public interface LazyEnlistableConnectionManager {
    void lazyEnlist(ManagedConnection managedConnection) throws ResourceException;
}
