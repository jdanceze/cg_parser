package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/QueryExp.class */
public interface QueryExp extends Serializable {
    boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException;

    void setMBeanServer(MBeanServer mBeanServer);
}
