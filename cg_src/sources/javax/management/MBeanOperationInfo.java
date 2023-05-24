package javax.management;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanOperationInfo.class */
public class MBeanOperationInfo extends MBeanFeatureInfo implements Serializable, Cloneable {
    static final long serialVersionUID = -6178860474881375330L;
    static final MBeanOperationInfo[] NO_OPERATIONS = new MBeanOperationInfo[0];
    public static final int INFO = 0;
    public static final int ACTION = 1;
    public static final int ACTION_INFO = 2;
    public static final int UNKNOWN = 3;
    private final String type;
    private final MBeanParameterInfo[] signature;
    private final int impact;
    private final transient boolean immutable;
    static Class class$javax$management$MBeanOperationInfo;

    public MBeanOperationInfo(String str, Method method) throws IllegalArgumentException {
        this(method.getName(), str, methodSignature(method), method.getReturnType().getName(), 3);
    }

    public MBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i) throws IllegalArgumentException {
        super(str, str2);
        MBeanParameterInfo[] mBeanParameterInfoArr2;
        Class cls;
        MBeanInfo.mustBeValidJavaIdentifier(str);
        MBeanInfo.mustBeValidJavaTypeName(str3);
        if (mBeanParameterInfoArr == null || mBeanParameterInfoArr.length == 0) {
            mBeanParameterInfoArr2 = MBeanParameterInfo.NO_PARAMS;
        } else {
            mBeanParameterInfoArr2 = (MBeanParameterInfo[]) mBeanParameterInfoArr.clone();
        }
        this.signature = mBeanParameterInfoArr2;
        this.type = str3;
        this.impact = i;
        Class<?> cls2 = getClass();
        if (class$javax$management$MBeanOperationInfo == null) {
            cls = class$("javax.management.MBeanOperationInfo");
            class$javax$management$MBeanOperationInfo = cls;
        } else {
            cls = class$javax$management$MBeanOperationInfo;
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

    public String getReturnType() {
        return this.type;
    }

    public MBeanParameterInfo[] getSignature() {
        if (this.signature.length == 0) {
            return this.signature;
        }
        return (MBeanParameterInfo[]) this.signature.clone();
    }

    private MBeanParameterInfo[] fastGetSignature() {
        if (this.immutable) {
            return this.signature;
        }
        return getSignature();
    }

    public int getImpact() {
        return this.impact;
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanOperationInfo)) {
            return false;
        }
        MBeanOperationInfo mBeanOperationInfo = (MBeanOperationInfo) obj;
        return mBeanOperationInfo.getName().equals(getName()) && mBeanOperationInfo.getReturnType().equals(getReturnType()) && mBeanOperationInfo.getDescription().equals(getDescription()) && mBeanOperationInfo.getImpact() == getImpact() && Arrays.equals(mBeanOperationInfo.fastGetSignature(), fastGetSignature());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return getName().hashCode() ^ getReturnType().hashCode();
    }

    private static MBeanParameterInfo[] methodSignature(Method method) throws IllegalArgumentException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            mBeanParameterInfoArr[i] = new MBeanParameterInfo(new StringBuffer().append("p").append(i + 1).toString(), parameterTypes[i].getName(), "");
        }
        return mBeanParameterInfoArr;
    }
}
