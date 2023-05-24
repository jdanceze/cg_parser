package javax.management.openmbean;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanAttributeInfo.class */
public interface OpenMBeanAttributeInfo extends OpenMBeanParameterInfo {
    boolean isReadable();

    boolean isWritable();

    boolean isIs();

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    boolean equals(Object obj);

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    int hashCode();

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    String toString();
}
