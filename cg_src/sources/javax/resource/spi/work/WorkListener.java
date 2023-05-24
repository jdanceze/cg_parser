package javax.resource.spi.work;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/work/WorkListener.class */
public interface WorkListener extends EventListener {
    void workAccepted(WorkEvent workEvent);

    void workRejected(WorkEvent workEvent);

    void workStarted(WorkEvent workEvent);

    void workCompleted(WorkEvent workEvent);
}
