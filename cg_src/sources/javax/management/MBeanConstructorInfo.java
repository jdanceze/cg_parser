package javax.management;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanConstructorInfo.class */
public class MBeanConstructorInfo extends MBeanFeatureInfo implements Serializable, Cloneable {
    static final long serialVersionUID = 4433990064191844427L;
    static final MBeanConstructorInfo[] NO_CONSTRUCTORS = new MBeanConstructorInfo[0];
    private final transient boolean immutable;
    private final MBeanParameterInfo[] signature;
    static Class class$javax$management$MBeanConstructorInfo;

    public MBeanConstructorInfo(String str, Constructor constructor) {
        this(constructor.getName(), str, constructorSignature(constructor));
    }

    public MBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr) throws IllegalArgumentException {
        super(str, str2);
        MBeanParameterInfo[] mBeanParameterInfoArr2;
        Class cls;
        MBeanInfo.mustBeValidMBeanTypeName(str);
        if (mBeanParameterInfoArr == null || mBeanParameterInfoArr.length == 0) {
            mBeanParameterInfoArr2 = MBeanParameterInfo.NO_PARAMS;
        } else {
            mBeanParameterInfoArr2 = (MBeanParameterInfo[]) mBeanParameterInfoArr.clone();
        }
        this.signature = mBeanParameterInfoArr2;
        Class<?> cls2 = getClass();
        if (class$javax$management$MBeanConstructorInfo == null) {
            cls = class$("javax.management.MBeanConstructorInfo");
            class$javax$management$MBeanConstructorInfo = cls;
        } else {
            cls = class$javax$management$MBeanConstructorInfo;
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

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanConstructorInfo)) {
            return false;
        }
        MBeanConstructorInfo mBeanConstructorInfo = (MBeanConstructorInfo) obj;
        return mBeanConstructorInfo.getName().equals(getName()) && mBeanConstructorInfo.getDescription().equals(getDescription()) && Arrays.equals(mBeanConstructorInfo.fastGetSignature(), fastGetSignature());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        int hashCode = getName().hashCode();
        for (MBeanParameterInfo mBeanParameterInfo : fastGetSignature()) {
            hashCode ^= mBeanParameterInfo.hashCode();
        }
        return hashCode;
    }

    private static MBeanParameterInfo[] constructorSignature(Constructor constructor) throws IllegalArgumentException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            mBeanParameterInfoArr[i] = new MBeanParameterInfo(new StringBuffer().append("p").append(i + 1).toString(), parameterTypes[i].getName(), "");
        }
        return mBeanParameterInfoArr;
    }
}
