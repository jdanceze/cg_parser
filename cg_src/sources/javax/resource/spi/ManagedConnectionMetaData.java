package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ManagedConnectionMetaData.class */
public interface ManagedConnectionMetaData {
    String getEISProductName() throws ResourceException;

    String getEISProductVersion() throws ResourceException;

    int getMaxConnections() throws ResourceException;

    String getUserName() throws ResourceException;
}
