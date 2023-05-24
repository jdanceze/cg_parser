package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/DissociatableManagedConnection.class */
public interface DissociatableManagedConnection {
    void dissociateConnections() throws ResourceException;
}
