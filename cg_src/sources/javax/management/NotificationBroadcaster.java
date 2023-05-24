package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationBroadcaster.class */
public interface NotificationBroadcaster {
    void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException;

    void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException;

    MBeanNotificationInfo[] getNotificationInfo();
}
