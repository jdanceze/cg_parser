package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanAttributeInfo.class */
public class MBeanAttributeInfo extends MBeanFeatureInfo implements Serializable, Cloneable {
    private static final long serialVersionUID;
    static final MBeanAttributeInfo[] NO_ATTRIBUTES;
    private final String attributeType;
    private final boolean isWrite;
    private final boolean isRead;
    private final boolean is;
    static Class class$java$lang$Boolean;

    static {
        long j = 8644704819898565848L;
        try {
            if ("1.0".equals((String) AccessController.doPrivileged((PrivilegedAction<Object>) new GetPropertyAction("jmx.serial.form")))) {
                j = 7043855487133450673L;
            }
        } catch (Exception e) {
        }
        serialVersionUID = j;
        NO_ATTRIBUTES = new MBeanAttributeInfo[0];
    }

    public MBeanAttributeInfo(String str, String str2, String str3, boolean z, boolean z2, boolean z3) throws IllegalArgumentException {
        super(str, str3);
        MBeanInfo.mustBeValidJavaIdentifier(str);
        MBeanInfo.mustBeValidJavaTypeName(str2);
        this.attributeType = str2;
        this.isRead = z;
        this.isWrite = z2;
        if (z3 && !z) {
            throw new IllegalArgumentException("Cannot have an \"is\" getter for a non-readable attribute.");
        }
        if (z3 && !str2.equals(JavaBasicTypes.JAVA_LANG_BOOLEAN) && !str2.equals("boolean")) {
            throw new IllegalArgumentException("Cannot have an \"is\" getter for a non-boolean attribute.");
        }
        this.is = z3;
    }

    public MBeanAttributeInfo(String str, String str2, Method method, Method method2) throws IntrospectionException {
        this(str, attributeType(method, method2), str2, method != null, method2 != null, isIs(method));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getType() {
        return this.attributeType;
    }

    public boolean isReadable() {
        return this.isRead;
    }

    public boolean isWritable() {
        return this.isWrite;
    }

    public boolean isIs() {
        return this.is;
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanAttributeInfo)) {
            return false;
        }
        MBeanAttributeInfo mBeanAttributeInfo = (MBeanAttributeInfo) obj;
        return mBeanAttributeInfo.getName().equals(getName()) && mBeanAttributeInfo.getType().equals(getType()) && mBeanAttributeInfo.getDescription().equals(getDescription()) && mBeanAttributeInfo.isReadable() == isReadable() && mBeanAttributeInfo.isWritable() == isWritable() && mBeanAttributeInfo.isIs() == isIs();
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return getName().hashCode() ^ getType().hashCode();
    }

    private static boolean isIs(Method method) {
        Class cls;
        if (method != null && method.getName().startsWith("is")) {
            if (!method.getReturnType().equals(Boolean.TYPE)) {
                Class<?> returnType = method.getReturnType();
                if (class$java$lang$Boolean == null) {
                    cls = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
                    class$java$lang$Boolean = cls;
                } else {
                    cls = class$java$lang$Boolean;
                }
                if (returnType.equals(cls)) {
                }
            }
            return true;
        }
        return false;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static String attributeType(Method method, Method method2) throws IntrospectionException {
        Class<?> cls = null;
        if (method != null) {
            if (method.getParameterTypes().length != 0) {
                throw new IntrospectionException("bad getter arg count");
            }
            cls = method.getReturnType();
            if (cls == Void.TYPE) {
                throw new IntrospectionException(new StringBuffer().append("getter ").append(method.getName()).append(" returns void").toString());
            }
        }
        if (method2 != null) {
            Class<?>[] parameterTypes = method2.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new IntrospectionException("bad setter arg count");
            }
            if (cls == null) {
                cls = parameterTypes[0];
            } else if (cls != parameterTypes[0]) {
                throw new IntrospectionException("type mismatch between getter and setter");
            }
        }
        if (cls == null) {
            throw new IntrospectionException("getter and setter cannot both be null");
        }
        return cls.getName();
    }
}
