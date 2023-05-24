package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerNotification.class */
public class MBeanServerNotification extends Notification {
    private static final long serialVersionUID = 2876477500475969677L;
    public static final String REGISTRATION_NOTIFICATION = "JMX.mbean.registered";
    public static final String UNREGISTRATION_NOTIFICATION = "JMX.mbean.unregistered";
    private final ObjectName objectName;

    public MBeanServerNotification(String str, Object obj, long j, ObjectName objectName) {
        super(str, obj, j);
        this.objectName = objectName;
    }

    public ObjectName getMBeanName() {
        return this.objectName;
    }
}
