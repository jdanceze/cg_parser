package javax.management.openmbean;

import java.io.Serializable;
import java.util.Arrays;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanParameterInfo;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenMBeanConstructorInfoSupport.class */
public class OpenMBeanConstructorInfoSupport extends MBeanConstructorInfo implements OpenMBeanConstructorInfo, Serializable {
    static final long serialVersionUID = -4400441579007477003L;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanConstructorInfoSupport(String str, String str2, OpenMBeanParameterInfo[] openMBeanParameterInfoArr) {
        super(str, str2, openMBeanParameterInfoArr == null ? null : arrayCopyCast(openMBeanParameterInfoArr));
        this.myHashCode = null;
        this.myToString = null;
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument name cannot be null or empty.");
        }
        if (str2 == null || str2.trim().equals("")) {
            throw new IllegalArgumentException("Argument description cannot be null or empty.");
        }
    }

    private static MBeanParameterInfo[] arrayCopyCast(OpenMBeanParameterInfo[] openMBeanParameterInfoArr) throws ArrayStoreException {
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[openMBeanParameterInfoArr.length];
        System.arraycopy(openMBeanParameterInfoArr, 0, mBeanParameterInfoArr, 0, openMBeanParameterInfoArr.length);
        return mBeanParameterInfoArr;
    }

    @Override // javax.management.MBeanConstructorInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanConstructorInfo openMBeanConstructorInfo = (OpenMBeanConstructorInfo) obj;
            if (!getName().equals(openMBeanConstructorInfo.getName()) || !Arrays.equals(getSignature(), openMBeanConstructorInfo.getSignature())) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.MBeanConstructorInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = new Integer(0 + getName().hashCode() + Arrays.asList(getSignature()).hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenMBeanConstructorInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = new StringBuffer().append(getClass().getName()).append("(name=").append(getName()).append(",signature=").append(Arrays.asList(getSignature()).toString()).append(")").toString();
        }
        return this.myToString;
    }
}
