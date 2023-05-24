package javax.management;

import java.io.ObjectInputStream;
import java.util.Set;
import javax.management.loading.ClassLoaderRepository;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServer.class */
public interface MBeanServer extends MBeanServerConnection {
    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Object[] objArr, String[] strArr) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException;

    ObjectInstance registerMBean(Object obj, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException;

    @Override // javax.management.MBeanServerConnection
    void unregisterMBean(ObjectName objectName) throws InstanceNotFoundException, MBeanRegistrationException;

    @Override // javax.management.MBeanServerConnection
    ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    Set queryMBeans(ObjectName objectName, QueryExp queryExp);

    @Override // javax.management.MBeanServerConnection
    Set queryNames(ObjectName objectName, QueryExp queryExp);

    @Override // javax.management.MBeanServerConnection
    boolean isRegistered(ObjectName objectName);

    @Override // javax.management.MBeanServerConnection
    Integer getMBeanCount();

    @Override // javax.management.MBeanServerConnection
    Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    void setAttribute(ObjectName objectName, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws InstanceNotFoundException, MBeanException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    String getDefaultDomain();

    @Override // javax.management.MBeanServerConnection
    String[] getDomains();

    @Override // javax.management.MBeanServerConnection
    void addNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void addNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, ObjectName objectName2) throws InstanceNotFoundException, ListenerNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, ObjectName objectName2, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException, ListenerNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener) throws InstanceNotFoundException, ListenerNotFoundException;

    @Override // javax.management.MBeanServerConnection
    void removeNotificationListener(ObjectName objectName, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws InstanceNotFoundException, ListenerNotFoundException;

    @Override // javax.management.MBeanServerConnection
    MBeanInfo getMBeanInfo(ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException;

    @Override // javax.management.MBeanServerConnection
    boolean isInstanceOf(ObjectName objectName, String str) throws InstanceNotFoundException;

    Object instantiate(String str) throws ReflectionException, MBeanException;

    Object instantiate(String str, ObjectName objectName) throws ReflectionException, MBeanException, InstanceNotFoundException;

    Object instantiate(String str, Object[] objArr, String[] strArr) throws ReflectionException, MBeanException;

    Object instantiate(String str, ObjectName objectName, Object[] objArr, String[] strArr) throws ReflectionException, MBeanException, InstanceNotFoundException;

    ObjectInputStream deserialize(ObjectName objectName, byte[] bArr) throws InstanceNotFoundException, OperationsException;

    ObjectInputStream deserialize(String str, byte[] bArr) throws OperationsException, ReflectionException;

    ObjectInputStream deserialize(String str, ObjectName objectName, byte[] bArr) throws InstanceNotFoundException, OperationsException, ReflectionException;

    ClassLoader getClassLoaderFor(ObjectName objectName) throws InstanceNotFoundException;

    ClassLoader getClassLoader(ObjectName objectName) throws InstanceNotFoundException;

    ClassLoaderRepository getClassLoaderRepository();
}
