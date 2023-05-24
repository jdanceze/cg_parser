package javax.resource.spi;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ResourceAdapter.class */
public interface ResourceAdapter {
    void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException;

    void stop();

    void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws ResourceException;

    void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec);

    XAResource[] getXAResources(ActivationSpec[] activationSpecArr) throws ResourceException;
}
