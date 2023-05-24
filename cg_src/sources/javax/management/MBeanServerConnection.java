package javax.management;

import java.io.IOException;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerConnection.class */
public interface MBeanServerConnection {
    ObjectInstance createMBean(String str, ObjectName objectName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException;

    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException;

    ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException;

    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException;

    void unregisterMBean(ObjectName objectName) throws InstanceNotFoundException, MBeanRegistrationException, IOException;

    ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException, IOException;

    Set queryMBeans(ObjectName objectName, QueryExp queryExp) throws IOException;

    Set queryNames(ObjectName objectName, QueryExp queryExp) throws IOException;

    boolean isRegistered(ObjectName objectName) throws IOException;

    Integer getMBeanCount() throws IOException;

    Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException;

    AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException, IOException;

    void setAttribute(ObjectName objectName, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException;

    AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException, IOException;

    Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException;

    String getDefaultDomain() throws IOException;

    String[] getDomains() throws IOException;

    void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException, IOException;

    void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException, IOException;

    void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws InstanceNotFoundException, ListenerNotFoundException, IOException;

    void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException, ListenerNotFoundException, IOException;

    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws InstanceNotFoundException, ListenerNotFoundException, IOException;

    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException, ListenerNotFoundException, IOException;

    MBeanInfo getMBeanInfo(ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException;

    boolean isInstanceOf(ObjectName objectName, String str) throws InstanceNotFoundException, IOException;
}
