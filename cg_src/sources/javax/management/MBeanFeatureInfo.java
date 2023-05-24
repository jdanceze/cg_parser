package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanFeatureInfo.class */
public class MBeanFeatureInfo implements Serializable {
    static final long serialVersionUID = 3952882688968447265L;
    protected String name;
    protected String description;

    public MBeanFeatureInfo(String str, String str2) throws IllegalArgumentException {
        this.name = str;
        this.description = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanFeatureInfo)) {
            return false;
        }
        MBeanFeatureInfo mBeanFeatureInfo = (MBeanFeatureInfo) obj;
        return mBeanFeatureInfo.getName().equals(getName()) && mBeanFeatureInfo.getDescription().equals(getDescription());
    }

    public int hashCode() {
        return getName().hashCode() ^ getDescription().hashCode();
    }
}
