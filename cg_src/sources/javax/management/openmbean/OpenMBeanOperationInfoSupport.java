package javax.management.openmbean;

import java.io.Serializable;
import java.util.Arrays;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanOperationInfoSupport.class */
public class OpenMBeanOperationInfoSupport extends MBeanOperationInfo implements OpenMBeanOperationInfo, Serializable {
    static final long serialVersionUID = 4996859732565369366L;
    private OpenType returnOpenType;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanOperationInfoSupport(String str, String str2, OpenMBeanParameterInfo[] openMBeanParameterInfoArr, OpenType openType, int i) {
        super(str, str2, openMBeanParameterInfoArr == null ? null : arrayCopyCast(openMBeanParameterInfoArr), openType == null ? null : openType.getClassName(), i);
        this.myHashCode = null;
        this.myToString = null;
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument name cannot be null or empty.");
        }
        if (str2 == null || str2.trim().equals("")) {
            throw new IllegalArgumentException("Argument description cannot be null or empty.");
        }
        if (openType == null) {
            throw new IllegalArgumentException("Argument returnOpenType cannot be null.");
        }
        if (i != 1 && i != 2 && i != 0) {
            throw new IllegalArgumentException("Argument impact can be only one of ACTION, ACTION_INFO or INFO.");
        }
        this.returnOpenType = openType;
    }

    private static MBeanParameterInfo[] arrayCopyCast(OpenMBeanParameterInfo[] openMBeanParameterInfoArr) throws ArrayStoreException {
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[openMBeanParameterInfoArr.length];
        System.arraycopy(openMBeanParameterInfoArr, 0, mBeanParameterInfoArr, 0, openMBeanParameterInfoArr.length);
        return mBeanParameterInfoArr;
    }

    @Override // javax.management.openmbean.OpenMBeanOperationInfo
    public OpenType getReturnOpenType() {
        return this.returnOpenType;
    }

    @Override // javax.management.MBeanOperationInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanOperationInfo openMBeanOperationInfo = (OpenMBeanOperationInfo) obj;
            if (!getName().equals(openMBeanOperationInfo.getName()) || !Arrays.equals(getSignature(), openMBeanOperationInfo.getSignature()) || !getReturnOpenType().equals(openMBeanOperationInfo.getReturnOpenType()) || getImpact() != openMBeanOperationInfo.getImpact()) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.MBeanOperationInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = new Integer(0 + getName().hashCode() + Arrays.asList(getSignature()).hashCode() + getReturnOpenType().hashCode() + getImpact());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenMBeanOperationInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = new StringBuffer().append(getClass().getName()).append("(name=").append(getName()).append(",signature=").append(Arrays.asList(getSignature()).toString()).append(",return=").append(getReturnOpenType().toString()).append(",impact=").append(getImpact()).append(")").toString();
        }
        return this.myToString;
    }
}
