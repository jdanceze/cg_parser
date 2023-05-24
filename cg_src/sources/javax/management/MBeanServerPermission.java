package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.StringTokenizer;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerPermission.class */
public class MBeanServerPermission extends BasicPermission {
    private static final long serialVersionUID = -5661980843569388590L;
    private static final int CREATE = 0;
    private static final int FIND = 1;
    private static final int NEW = 2;
    private static final int RELEASE = 3;
    private static final int N_NAMES = 4;
    private static final int CREATE_MASK = 1;
    private static final int FIND_MASK = 2;
    private static final int NEW_MASK = 4;
    private static final int RELEASE_MASK = 8;
    private static final int ALL_MASK = 15;
    transient int mask;
    private static final String[] names = {"createMBeanServer", "findMBeanServer", "newMBeanServer", "releaseMBeanServer"};
    private static final String[] canonicalNames = new String[16];

    public MBeanServerPermission(String str) {
        this(str, null);
    }

    public MBeanServerPermission(String str, String str2) {
        super(getCanonicalName(parseMask(str)), str2);
        this.mask = parseMask(str);
        if (str2 != null && str2.length() > 0) {
            throw new IllegalArgumentException(new StringBuffer().append("MBeanServerPermission actions must be null: ").append(str2).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MBeanServerPermission(int i) {
        super(getCanonicalName(i));
        this.mask = impliedMask(i);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.mask = parseMask(getName());
    }

    static int simplifyMask(int i) {
        if ((i & 1) != 0) {
            i &= -5;
        }
        return i;
    }

    static int impliedMask(int i) {
        if ((i & 1) != 0) {
            i |= 4;
        }
        return i;
    }

    static String getCanonicalName(int i) {
        if (i == 15) {
            return "*";
        }
        int simplifyMask = simplifyMask(i);
        synchronized (canonicalNames) {
            if (canonicalNames[simplifyMask] == null) {
                canonicalNames[simplifyMask] = makeCanonicalName(simplifyMask);
            }
        }
        return canonicalNames[simplifyMask];
    }

    private static String makeCanonicalName(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < 4; i2++) {
            if ((i & (1 << i2)) != 0) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(',');
                }
                stringBuffer.append(names[i2]);
            }
        }
        return stringBuffer.toString().intern();
    }

    private static int parseMask(String str) {
        if (str == null) {
            throw new NullPointerException("MBeanServerPermission: target name can't be null");
        }
        String trim = str.trim();
        if (trim.equals("*")) {
            return 15;
        }
        if (trim.indexOf(44) < 0) {
            return impliedMask(1 << nameIndex(trim.trim()));
        }
        int i = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(trim, ",");
        while (stringTokenizer.hasMoreTokens()) {
            i |= 1 << nameIndex(stringTokenizer.nextToken().trim());
        }
        return impliedMask(i);
    }

    private static int nameIndex(String str) throws IllegalArgumentException {
        for (int i = 0; i < 4; i++) {
            if (names[i].equals(str)) {
                return i;
            }
        }
        throw new IllegalArgumentException(new StringBuffer().append("Invalid MBeanServerPermission name: \"").append(str).append("\"").toString());
    }

    public int hashCode() {
        return this.mask;
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof MBeanServerPermission)) {
            return false;
        }
        MBeanServerPermission mBeanServerPermission = (MBeanServerPermission) permission;
        return (this.mask & mBeanServerPermission.mask) == mBeanServerPermission.mask;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof MBeanServerPermission) && this.mask == ((MBeanServerPermission) obj).mask;
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new MBeanServerPermissionCollection();
    }
}
