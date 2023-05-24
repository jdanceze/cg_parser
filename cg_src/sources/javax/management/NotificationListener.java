package javax.management;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationListener.class */
public interface NotificationListener extends EventListener {
    void handleNotification(Notification notification, Object obj);
}
