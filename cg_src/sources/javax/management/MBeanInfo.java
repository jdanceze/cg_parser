package javax.management;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanInfo.class */
public class MBeanInfo implements Cloneable, Serializable {
    static final long serialVersionUID = -6451021435135161911L;
    private final String description;
    private final String className;
    private final MBeanAttributeInfo[] attributes;
    private final MBeanOperationInfo[] operations;
    private final MBeanConstructorInfo[] constructors;
    private final MBeanNotificationInfo[] notifications;
    private transient int hashCode;
    private final transient boolean immutable;
    private static final Map immutability = new WeakHashMap();
    static Class class$javax$management$MBeanInfo;

    public MBeanInfo(String str, String str2, MBeanAttributeInfo[] mBeanAttributeInfoArr, MBeanConstructorInfo[] mBeanConstructorInfoArr, MBeanOperationInfo[] mBeanOperationInfoArr, MBeanNotificationInfo[] mBeanNotificationInfoArr) throws IllegalArgumentException {
        Class cls;
        mustBeValidMBeanTypeName(str);
        this.className = str;
        this.description = str2;
        this.attributes = (mBeanAttributeInfoArr == null || mBeanAttributeInfoArr.length == 0) ? MBeanAttributeInfo.NO_ATTRIBUTES : mBeanAttributeInfoArr;
        this.operations = (mBeanOperationInfoArr == null || mBeanOperationInfoArr.length == 0) ? MBeanOperationInfo.NO_OPERATIONS : mBeanOperationInfoArr;
        this.constructors = (mBeanConstructorInfoArr == null || mBeanConstructorInfoArr.length == 0) ? MBeanConstructorInfo.NO_CONSTRUCTORS : mBeanConstructorInfoArr;
        this.notifications = (mBeanNotificationInfoArr == null || mBeanNotificationInfoArr.length == 0) ? MBeanNotificationInfo.NO_NOTIFICATIONS : mBeanNotificationInfoArr;
        Class<?> cls2 = getClass();
        if (class$javax$management$MBeanInfo == null) {
            cls = class$("javax.management.MBeanInfo");
            class$javax$management$MBeanInfo = cls;
        } else {
            cls = class$javax$management$MBeanInfo;
        }
        this.immutable = isImmutableClass(cls2, cls);
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

    public String getClassName() {
        return this.className;
    }

    public String getDescription() {
        return this.description;
    }

    public MBeanAttributeInfo[] getAttributes() {
        if (this.attributes.length == 0) {
            return this.attributes;
        }
        return (MBeanAttributeInfo[]) this.attributes.clone();
    }

    private MBeanAttributeInfo[] fastGetAttributes() {
        if (this.immutable) {
            return this.attributes;
        }
        return getAttributes();
    }

    public MBeanOperationInfo[] getOperations() {
        if (this.operations.length == 0) {
            return this.operations;
        }
        return (MBeanOperationInfo[]) this.operations.clone();
    }

    private MBeanOperationInfo[] fastGetOperations() {
        if (this.immutable) {
            return this.operations;
        }
        return getOperations();
    }

    public MBeanConstructorInfo[] getConstructors() {
        if (this.constructors.length == 0) {
            return this.constructors;
        }
        return (MBeanConstructorInfo[]) this.constructors.clone();
    }

    private MBeanConstructorInfo[] fastGetConstructors() {
        if (this.immutable) {
            return this.constructors;
        }
        return getConstructors();
    }

    public MBeanNotificationInfo[] getNotifications() {
        if (this.notifications.length == 0) {
            return this.notifications;
        }
        return (MBeanNotificationInfo[]) this.notifications.clone();
    }

    private MBeanNotificationInfo[] fastGetNotifications() {
        if (this.immutable) {
            return this.notifications;
        }
        return getNotifications();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanInfo)) {
            return false;
        }
        MBeanInfo mBeanInfo = (MBeanInfo) obj;
        return mBeanInfo.getClassName().equals(getClassName()) && mBeanInfo.getDescription().equals(getDescription()) && Arrays.equals(mBeanInfo.fastGetAttributes(), fastGetAttributes()) && Arrays.equals(mBeanInfo.fastGetOperations(), fastGetOperations()) && Arrays.equals(mBeanInfo.fastGetConstructors(), fastGetConstructors()) && Arrays.equals(mBeanInfo.fastGetNotifications(), fastGetNotifications());
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        this.hashCode = (((getClassName().hashCode() ^ arrayHashCode(fastGetAttributes())) ^ arrayHashCode(fastGetOperations())) ^ arrayHashCode(fastGetConstructors())) ^ arrayHashCode(fastGetNotifications());
        return this.hashCode;
    }

    private static int arrayHashCode(Object[] objArr) {
        int i = 0;
        for (Object obj : objArr) {
            i ^= obj.hashCode();
        }
        return i;
    }

    static boolean isValidJavaIdentifier(String str) {
        int length;
        if (str == null || (length = str.length()) == 0 || !Character.isJavaIdentifierStart(str.charAt(0))) {
            return false;
        }
        for (int i = 1; i < length; i++) {
            if (!Character.isJavaIdentifierPart(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static boolean isValidJavaTypeName(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        int i = 0;
        while (i < length && str.charAt(i) == '[') {
            i++;
        }
        if (i == length) {
            return false;
        }
        if (i == 0) {
            return isValidJavaTypeIdentifier(str);
        }
        switch (str.charAt(i)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return i == length - 1;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                return false;
            case 'L':
                if (i < length - 1 && str.charAt(length - 1) == ';') {
                    return isValidJavaTypeIdentifier(str.substring(i, length - 1));
                }
                return false;
        }
    }

    static boolean isValidJavaTypeIdentifier(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        boolean z = true;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (z) {
                if (!Character.isJavaIdentifierStart(charAt)) {
                    return false;
                }
                z = false;
            } else if (charAt == '.') {
                z = true;
            } else if (!Character.isJavaIdentifierPart(charAt)) {
                return false;
            }
        }
        return !z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void mustBeValidMBeanTypeName(String str) throws IllegalArgumentException {
        if (!isValidJavaTypeIdentifier(str)) {
            throw new IllegalArgumentException(new StringBuffer().append("Not a valid Java MBean type name: ").append(str).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void mustBeValidJavaTypeName(String str) throws IllegalArgumentException {
        if (!isValidJavaTypeName(str)) {
            throw new IllegalArgumentException(new StringBuffer().append("Not a valid Java type name: ").append(str).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void mustBeValidJavaIdentifier(String str) throws IllegalArgumentException {
        if (!isValidJavaIdentifier(str)) {
            throw new IllegalArgumentException(new StringBuffer().append("Not a valid Java identifier: ").append(str).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isImmutableClass(Class cls, Class cls2) {
        boolean booleanValue;
        if (cls == cls2) {
            return true;
        }
        synchronized (immutability) {
            Boolean bool = (Boolean) immutability.get(cls);
            if (bool == null) {
                try {
                    bool = (Boolean) AccessController.doPrivileged(new ImmutabilityAction(cls, cls2));
                } catch (Exception e) {
                    bool = Boolean.FALSE;
                }
                immutability.put(cls, bool);
            }
            booleanValue = bool.booleanValue();
        }
        return booleanValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanInfo$ImmutabilityAction.class */
    public static class ImmutabilityAction implements PrivilegedAction {
        private final Class subclass;
        private final Class immutableClass;

        ImmutabilityAction(Class cls, Class cls2) {
            this.subclass = cls;
            this.immutableClass = cls2;
        }

        @Override // java.security.PrivilegedAction
        public Object run() {
            Method[] methods;
            for (Method method : this.immutableClass.getMethods()) {
                String name = method.getName();
                if (name.startsWith("get") || name.startsWith("is")) {
                    try {
                        if (!this.subclass.getMethod(name, method.getParameterTypes()).equals(method)) {
                            return Boolean.FALSE;
                        }
                    } catch (NoSuchMethodException e) {
                        return Boolean.FALSE;
                    }
                }
            }
            return Boolean.TRUE;
        }
    }
}
