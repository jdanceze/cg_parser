package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationEmitter.class */
public interface NotificationEmitter extends NotificationBroadcaster {
    void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException;
}
