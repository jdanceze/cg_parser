package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/ValueExp.class */
public interface ValueExp extends Serializable {
    ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException;

    void setMBeanServer(MBeanServer mBeanServer);
}
