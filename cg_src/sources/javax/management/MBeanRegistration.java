package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanRegistration.class */
public interface MBeanRegistration {
    ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception;

    void postRegister(Boolean bool);

    void preDeregister() throws Exception;

    void postDeregister();
}
