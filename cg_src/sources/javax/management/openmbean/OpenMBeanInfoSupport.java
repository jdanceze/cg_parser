package javax.management.openmbean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanInfoSupport.class */
public class OpenMBeanInfoSupport extends MBeanInfo implements OpenMBeanInfo, Serializable {
    static final long serialVersionUID = 4349395935420511492L;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanInfoSupport(String str, String str2, OpenMBeanAttributeInfo[] openMBeanAttributeInfoArr, OpenMBeanConstructorInfo[] openMBeanConstructorInfoArr, OpenMBeanOperationInfo[] openMBeanOperationInfoArr, MBeanNotificationInfo[] mBeanNotificationInfoArr) {
        super(str, str2, openMBeanAttributeInfoArr == null ? null : attributesArrayCopyCast(openMBeanAttributeInfoArr), openMBeanConstructorInfoArr == null ? null : constructorsArrayCopyCast(openMBeanConstructorInfoArr), openMBeanOperationInfoArr == null ? null : operationsArrayCopyCast(openMBeanOperationInfoArr), mBeanNotificationInfoArr == null ? null : notificationsArrayCopy(mBeanNotificationInfoArr));
        this.myHashCode = null;
        this.myToString = null;
    }

    private static MBeanAttributeInfo[] attributesArrayCopyCast(OpenMBeanAttributeInfo[] openMBeanAttributeInfoArr) throws ArrayStoreException {
        MBeanAttributeInfo[] mBeanAttributeInfoArr = new MBeanAttributeInfo[openMBeanAttributeInfoArr.length];
        System.arraycopy(openMBeanAttributeInfoArr, 0, mBeanAttributeInfoArr, 0, openMBeanAttributeInfoArr.length);
        return mBeanAttributeInfoArr;
    }

    private static MBeanConstructorInfo[] constructorsArrayCopyCast(OpenMBeanConstructorInfo[] openMBeanConstructorInfoArr) throws ArrayStoreException {
        MBeanConstructorInfo[] mBeanConstructorInfoArr = new MBeanConstructorInfo[openMBeanConstructorInfoArr.length];
        System.arraycopy(openMBeanConstructorInfoArr, 0, mBeanConstructorInfoArr, 0, openMBeanConstructorInfoArr.length);
        return mBeanConstructorInfoArr;
    }

    private static MBeanOperationInfo[] operationsArrayCopyCast(OpenMBeanOperationInfo[] openMBeanOperationInfoArr) throws ArrayStoreException {
        MBeanOperationInfo[] mBeanOperationInfoArr = new MBeanOperationInfo[openMBeanOperationInfoArr.length];
        System.arraycopy(openMBeanOperationInfoArr, 0, mBeanOperationInfoArr, 0, openMBeanOperationInfoArr.length);
        return mBeanOperationInfoArr;
    }

    private static MBeanNotificationInfo[] notificationsArrayCopy(MBeanNotificationInfo[] mBeanNotificationInfoArr) {
        MBeanNotificationInfo[] mBeanNotificationInfoArr2 = new MBeanNotificationInfo[mBeanNotificationInfoArr.length];
        System.arraycopy(mBeanNotificationInfoArr, 0, mBeanNotificationInfoArr2, 0, mBeanNotificationInfoArr.length);
        return mBeanNotificationInfoArr2;
    }

    @Override // javax.management.MBeanInfo, javax.management.openmbean.OpenMBeanInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanInfo openMBeanInfo = (OpenMBeanInfo) obj;
            if (!getClassName().equals(openMBeanInfo.getClassName()) || !new HashSet(Arrays.asList(getAttributes())).equals(new HashSet(Arrays.asList(openMBeanInfo.getAttributes()))) || !new HashSet(Arrays.asList(getConstructors())).equals(new HashSet(Arrays.asList(openMBeanInfo.getConstructors()))) || !new HashSet(Arrays.asList(getOperations())).equals(new HashSet(Arrays.asList(openMBeanInfo.getOperations()))) || !new HashSet(Arrays.asList(getNotifications())).equals(new HashSet(Arrays.asList(openMBeanInfo.getNotifications())))) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.MBeanInfo, javax.management.openmbean.OpenMBeanInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = new Integer(0 + getClassName().hashCode() + new HashSet(Arrays.asList(getAttributes())).hashCode() + new HashSet(Arrays.asList(getConstructors())).hashCode() + new HashSet(Arrays.asList(getOperations())).hashCode() + new HashSet(Arrays.asList(getNotifications())).hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenMBeanInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = new StringBuffer().append(getClass().getName()).append("(mbean_class_name=").append(getClassName()).append(",attributes=").append(Arrays.asList(getAttributes()).toString()).append(",constructors=").append(Arrays.asList(getConstructors()).toString()).append(",operations=").append(Arrays.asList(getOperations()).toString()).append(",notifications=").append(Arrays.asList(getNotifications()).toString()).append(")").toString();
        }
        return this.myToString;
    }
}
