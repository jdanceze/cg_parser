package javax.management.j2ee;

import java.rmi.RemoteException;
import java.util.Set;
import javax.ejb.EJBObject;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/Management.class */
public interface Management extends EJBObject {
    Set queryNames(ObjectName objectName, QueryExp queryExp) throws RemoteException;

    boolean isRegistered(ObjectName objectName) throws RemoteException;

    Integer getMBeanCount() throws RemoteException;

    MBeanInfo getMBeanInfo(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException, RemoteException;

    Object getAttribute(ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, RemoteException;

    AttributeList getAttributes(ObjectName objectName, String[] strArr) throws InstanceNotFoundException, ReflectionException, RemoteException;

    void setAttribute(ObjectName objectName, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, RemoteException;

    AttributeList setAttributes(ObjectName objectName, AttributeList attributeList) throws InstanceNotFoundException, ReflectionException, RemoteException;

    Object invoke(ObjectName objectName, String str, Object[] objArr, String[] strArr) throws InstanceNotFoundException, MBeanException, ReflectionException, RemoteException;

    String getDefaultDomain() throws RemoteException;

    ListenerRegistration getListenerRegistry() throws RemoteException;
}
