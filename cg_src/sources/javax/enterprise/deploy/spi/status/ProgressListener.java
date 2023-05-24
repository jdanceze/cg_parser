package javax.enterprise.deploy.spi.status;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/status/ProgressListener.class */
public interface ProgressListener extends EventListener {
    void handleProgressEvent(ProgressEvent progressEvent);
}
