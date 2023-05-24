package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationFilter.class */
public interface NotificationFilter extends Serializable {
    boolean isNotificationEnabled(Notification notification);
}
