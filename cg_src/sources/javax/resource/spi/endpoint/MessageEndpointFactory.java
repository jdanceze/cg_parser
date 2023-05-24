package javax.resource.spi.endpoint;

import java.lang.reflect.Method;
import javax.resource.spi.UnavailableException;
import javax.transaction.xa.XAResource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/endpoint/MessageEndpointFactory.class */
public interface MessageEndpointFactory {
    MessageEndpoint createEndpoint(XAResource xAResource) throws UnavailableException;

    boolean isDeliveryTransacted(Method method) throws NoSuchMethodException;
}
