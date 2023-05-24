package javax.management;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerDelegate.class */
public class MBeanServerDelegate implements MBeanServerDelegateMBean, NotificationEmitter {
    private String mbeanServerId;
    private NotificationBroadcasterSupport broadcaster;
    private static Long oldStamp = new Long(0);
    private long sequenceNumber = 1;
    private static final MBeanNotificationInfo[] notifsInfo;

    static {
        String[] strArr = {MBeanServerNotification.UNREGISTRATION_NOTIFICATION, MBeanServerNotification.REGISTRATION_NOTIFICATION};
        notifsInfo = new MBeanNotificationInfo[1];
        notifsInfo[0] = new MBeanNotificationInfo(strArr, "MBeanServerNotification", "Notifications sent by the MBeanServerDelegate MBean");
    }

    public MBeanServerDelegate() {
        Long l;
        this.broadcaster = null;
        String str = "";
        synchronized (oldStamp) {
            l = new Long(new Date().getTime());
            l = oldStamp.longValue() >= l.longValue() ? new Long(oldStamp.longValue() + 1) : l;
            oldStamp = l;
        }
        try {
            str = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        this.mbeanServerId = new String(new StringBuffer().append(str).append("_").append(l.toString()).toString());
        this.broadcaster = new NotificationBroadcasterSupport();
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getMBeanServerId() {
        return this.mbeanServerId;
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getSpecificationName() {
        return "Java Management Extensions";
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getSpecificationVersion() {
        return "1.2 Maintenance Release";
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getSpecificationVendor() {
        return "Sun Microsystems";
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getImplementationName() {
        return "JMX";
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getImplementationVersion() {
        return "1.2_r08";
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getImplementationVendor() {
        return "Sun Microsystems";
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        int length = notifsInfo.length;
        MBeanNotificationInfo[] mBeanNotificationInfoArr = new MBeanNotificationInfo[length];
        System.arraycopy(notifsInfo, 0, mBeanNotificationInfoArr, 0, length);
        return mBeanNotificationInfoArr;
    }

    @Override // javax.management.NotificationBroadcaster
    public synchronized void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException {
        this.broadcaster.addNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.NotificationEmitter
    public synchronized void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        this.broadcaster.removeNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.NotificationBroadcaster
    public synchronized void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        this.broadcaster.removeNotificationListener(notificationListener);
    }

    public void sendNotification(Notification notification) {
        if (notification.getSequenceNumber() < 1) {
            synchronized (this) {
                long j = this.sequenceNumber;
                this.sequenceNumber = j + 1;
                notification.setSequenceNumber(j);
            }
        }
        this.broadcaster.sendNotification(notification);
    }
}
