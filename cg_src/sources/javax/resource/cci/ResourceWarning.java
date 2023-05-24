package javax.resource.cci;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/ResourceWarning.class */
public class ResourceWarning extends ResourceException {
    public ResourceWarning() {
    }

    public ResourceWarning(String message) {
        super(message);
    }

    public ResourceWarning(Throwable cause) {
        super(cause);
    }

    public ResourceWarning(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceWarning(String message, String errorCode) {
        super(message, errorCode);
    }

    public ResourceWarning getLinkedWarning() {
        try {
            return (ResourceWarning) getLinkedException();
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void setLinkedWarning(ResourceWarning warning) {
        setLinkedException(warning);
    }
}
