package javax.resource.spi;

import java.util.Timer;
import javax.resource.spi.work.WorkManager;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/BootstrapContext.class */
public interface BootstrapContext {
    WorkManager getWorkManager();

    XATerminator getXATerminator();

    Timer createTimer() throws UnavailableException;
}
