package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanParameterInfo.class */
public class MBeanParameterInfo extends MBeanFeatureInfo implements Serializable, Cloneable {
    static final long serialVersionUID = 7432616882776782338L;
    static final MBeanParameterInfo[] NO_PARAMS = new MBeanParameterInfo[0];
    private final String type;

    public MBeanParameterInfo(String str, String str2, String str3) throws IllegalArgumentException {
        super(str, str3);
        MBeanInfo.mustBeValidJavaIdentifier(str);
        MBeanInfo.mustBeValidJavaTypeName(str2);
        this.type = str2;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getType() {
        return this.type;
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanParameterInfo)) {
            return false;
        }
        MBeanParameterInfo mBeanParameterInfo = (MBeanParameterInfo) obj;
        return mBeanParameterInfo.getName().equals(getName()) && mBeanParameterInfo.getType().equals(getType()) && mBeanParameterInfo.getDescription().equals(getDescription());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return getName().hashCode() ^ getType().hashCode();
    }
}
