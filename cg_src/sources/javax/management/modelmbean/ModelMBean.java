package javax.management.modelmbean;

import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.PersistentMBean;
import javax.management.RuntimeOperationsException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/ModelMBean.class */
public interface ModelMBean extends DynamicMBean, PersistentMBean, ModelMBeanNotificationBroadcaster {
    void setModelMBeanInfo(ModelMBeanInfo modelMBeanInfo) throws MBeanException, RuntimeOperationsException;

    void setManagedResource(Object obj, String str) throws MBeanException, RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException;
}
