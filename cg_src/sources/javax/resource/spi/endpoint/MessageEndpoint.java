package javax.resource.spi.endpoint;

import java.lang.reflect.Method;
import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/endpoint/MessageEndpoint.class */
public interface MessageEndpoint {
    void beforeDelivery(Method method) throws NoSuchMethodException, ResourceException;

    void afterDelivery() throws ResourceException;

    void release();
}
