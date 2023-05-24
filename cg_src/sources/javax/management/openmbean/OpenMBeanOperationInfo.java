package javax.management.openmbean;

import javax.management.MBeanParameterInfo;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanOperationInfo.class */
public interface OpenMBeanOperationInfo {
    String getDescription();

    String getName();

    MBeanParameterInfo[] getSignature();

    int getImpact();

    String getReturnType();

    OpenType getReturnOpenType();

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
