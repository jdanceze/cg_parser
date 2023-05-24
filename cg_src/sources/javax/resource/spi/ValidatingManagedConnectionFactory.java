package javax.resource.spi;

import java.util.Set;
import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ValidatingManagedConnectionFactory.class */
public interface ValidatingManagedConnectionFactory {
    Set getInvalidConnections(Set set) throws ResourceException;
}
