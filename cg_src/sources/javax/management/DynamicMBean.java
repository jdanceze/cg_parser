package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/DynamicMBean.class */
public interface DynamicMBean {
    Object getAttribute(String str) throws AttributeNotFoundException, MBeanException, ReflectionException;

    void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException;

    AttributeList getAttributes(String[] strArr);

    AttributeList setAttributes(AttributeList attributeList);

    Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException;

    MBeanInfo getMBeanInfo();
}
