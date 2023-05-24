package javax.management.openmbean;

import javax.management.MBeanParameterInfo;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanConstructorInfo.class */
public interface OpenMBeanConstructorInfo {
    String getDescription();

    String getName();

    MBeanParameterInfo[] getSignature();

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
