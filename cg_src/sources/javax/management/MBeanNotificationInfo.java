package javax.management;

import java.io.Serializable;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanNotificationInfo.class */
public class MBeanNotificationInfo extends MBeanFeatureInfo implements Cloneable, Serializable {
    static final long serialVersionUID = -3888371564530107064L;
    private static final String[] NO_TYPES = new String[0];
    static final MBeanNotificationInfo[] NO_NOTIFICATIONS = new MBeanNotificationInfo[0];
    private final String[] types;
    private final transient boolean immutable;
    static Class class$javax$management$MBeanNotificationInfo;

    public MBeanNotificationInfo(String[] strArr, String str, String str2) throws IllegalArgumentException {
        super(str, str2);
        Class cls;
        MBeanInfo.mustBeValidJavaTypeName(str);
        this.types = strArr == null ? NO_TYPES : strArr;
        Class<?> cls2 = getClass();
        if (class$javax$management$MBeanNotificationInfo == null) {
            cls = class$("javax.management.MBeanNotificationInfo");
            class$javax$management$MBeanNotificationInfo = cls;
        } else {
            cls = class$javax$management$MBeanNotificationInfo;
        }
        this.immutable = MBeanInfo.isImmutableClass(cls2, cls);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String[] getNotifTypes() {
        if (this.types.length == 0) {
            return NO_TYPES;
        }
        return (String[]) this.types.clone();
    }

    private String[] fastGetNotifTypes() {
        if (this.immutable) {
            return this.types;
        }
        return getNotifTypes();
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanNotificationInfo)) {
            return false;
        }
        MBeanNotificationInfo mBeanNotificationInfo = (MBeanNotificationInfo) obj;
        return mBeanNotificationInfo.getName().equals(getName()) && mBeanNotificationInfo.getDescription().equals(getDescription()) && Arrays.equals(mBeanNotificationInfo.fastGetNotifTypes(), fastGetNotifTypes());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        int hashCode = getName().hashCode();
        for (int i = 0; i < this.types.length; i++) {
            hashCode ^= this.types[i].hashCode();
        }
        return hashCode;
    }
}
