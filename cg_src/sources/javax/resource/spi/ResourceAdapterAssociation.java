package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ResourceAdapterAssociation.class */
public interface ResourceAdapterAssociation {
    ResourceAdapter getResourceAdapter();

    void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException;
}
