package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/PersistentMBean.class */
public interface PersistentMBean {
    void load() throws MBeanException, RuntimeOperationsException, InstanceNotFoundException;

    void store() throws MBeanException, RuntimeOperationsException, InstanceNotFoundException;
}
