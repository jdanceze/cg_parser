package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.NotCompliantMBeanException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RoleInfo.class */
public class RoleInfo implements Serializable {
    private static final long oldSerialVersionUID = 7227256952085334351L;
    private static final long newSerialVersionUID = 2504952983494636987L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    public static int ROLE_CARDINALITY_INFINITY;
    private boolean isReadable;
    private boolean isWritable;
    private int minDegree;
    private int maxDegree;
    static Class class$java$lang$String;
    private String name = null;
    private String description = null;
    private String referencedMBeanClassName = null;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[7];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("myName", cls);
        objectStreamFieldArr[1] = new ObjectStreamField("myIsReadableFlg", Boolean.TYPE);
        objectStreamFieldArr[2] = new ObjectStreamField("myIsWritableFlg", Boolean.TYPE);
        if (class$java$lang$String == null) {
            cls2 = class$("java.lang.String");
            class$java$lang$String = cls2;
        } else {
            cls2 = class$java$lang$String;
        }
        objectStreamFieldArr[3] = new ObjectStreamField("myDescription", cls2);
        objectStreamFieldArr[4] = new ObjectStreamField("myMinDegree", Integer.TYPE);
        objectStreamFieldArr[5] = new ObjectStreamField("myMaxDegree", Integer.TYPE);
        if (class$java$lang$String == null) {
            cls3 = class$("java.lang.String");
            class$java$lang$String = cls3;
        } else {
            cls3 = class$java$lang$String;
        }
        objectStreamFieldArr[6] = new ObjectStreamField("myRefMBeanClassName", cls3);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[7];
        if (class$java$lang$String == null) {
            cls4 = class$("java.lang.String");
            class$java$lang$String = cls4;
        } else {
            cls4 = class$java$lang$String;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("name", cls4);
        objectStreamFieldArr2[1] = new ObjectStreamField("isReadable", Boolean.TYPE);
        objectStreamFieldArr2[2] = new ObjectStreamField("isWritable", Boolean.TYPE);
        if (class$java$lang$String == null) {
            cls5 = class$("java.lang.String");
            class$java$lang$String = cls5;
        } else {
            cls5 = class$java$lang$String;
        }
        objectStreamFieldArr2[3] = new ObjectStreamField("description", cls5);
        objectStreamFieldArr2[4] = new ObjectStreamField("minDegree", Integer.TYPE);
        objectStreamFieldArr2[5] = new ObjectStreamField("maxDegree", Integer.TYPE);
        if (class$java$lang$String == null) {
            cls6 = class$("java.lang.String");
            class$java$lang$String = cls6;
        } else {
            cls6 = class$java$lang$String;
        }
        objectStreamFieldArr2[6] = new ObjectStreamField("referencedMBeanClassName", cls6);
        newSerialPersistentFields = objectStreamFieldArr2;
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged((PrivilegedAction<Object>) new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
        ROLE_CARDINALITY_INFINITY = -1;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public RoleInfo(String str, String str2, boolean z, boolean z2, int i, int i2, String str3) throws IllegalArgumentException, InvalidRoleInfoException, ClassNotFoundException, NotCompliantMBeanException {
        init(str, str2, z, z2, i, i2, str3);
    }

    public RoleInfo(String str, String str2, boolean z, boolean z2) throws IllegalArgumentException, ClassNotFoundException, NotCompliantMBeanException {
        try {
            init(str, str2, z, z2, 1, 1, null);
        } catch (InvalidRoleInfoException e) {
        }
    }

    public RoleInfo(String str, String str2) throws IllegalArgumentException, ClassNotFoundException, NotCompliantMBeanException {
        try {
            init(str, str2, true, true, 1, 1, null);
        } catch (InvalidRoleInfoException e) {
        }
    }

    public RoleInfo(RoleInfo roleInfo) throws IllegalArgumentException {
        if (roleInfo == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        try {
            init(roleInfo.getName(), roleInfo.getRefMBeanClassName(), roleInfo.isReadable(), roleInfo.isWritable(), roleInfo.getMinDegree(), roleInfo.getMaxDegree(), roleInfo.getDescription());
        } catch (InvalidRoleInfoException e) {
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean isReadable() {
        return this.isReadable;
    }

    public boolean isWritable() {
        return this.isWritable;
    }

    public String getDescription() {
        return this.description;
    }

    public int getMinDegree() {
        return this.minDegree;
    }

    public int getMaxDegree() {
        return this.maxDegree;
    }

    public String getRefMBeanClassName() {
        return this.referencedMBeanClassName;
    }

    public boolean checkMinDegree(int i) {
        if (i >= ROLE_CARDINALITY_INFINITY) {
            if (this.minDegree == ROLE_CARDINALITY_INFINITY || i >= this.minDegree) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean checkMaxDegree(int i) {
        if (i >= ROLE_CARDINALITY_INFINITY) {
            if (this.maxDegree != ROLE_CARDINALITY_INFINITY) {
                if (i != ROLE_CARDINALITY_INFINITY && i <= this.maxDegree) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new StringBuffer().append("role info name: ").append(this.name).toString());
        stringBuffer.append(new StringBuffer().append("; isReadable: ").append(this.isReadable).toString());
        stringBuffer.append(new StringBuffer().append("; isWritable: ").append(this.isWritable).toString());
        stringBuffer.append(new StringBuffer().append("; description: ").append(this.description).toString());
        stringBuffer.append(new StringBuffer().append("; minimum degree: ").append(this.minDegree).toString());
        stringBuffer.append(new StringBuffer().append("; maximum degree: ").append(this.maxDegree).toString());
        stringBuffer.append(new StringBuffer().append("; MBean class: ").append(this.referencedMBeanClassName).toString());
        return stringBuffer.toString();
    }

    private void init(String str, String str2, boolean z, boolean z2, int i, int i2, String str3) throws IllegalArgumentException, InvalidRoleInfoException {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.name = str;
        this.isReadable = z;
        this.isWritable = z2;
        if (str3 != null) {
            this.description = str3;
        }
        boolean z3 = false;
        StringBuffer stringBuffer = new StringBuffer();
        if (i2 != ROLE_CARDINALITY_INFINITY && (i == ROLE_CARDINALITY_INFINITY || i > i2)) {
            stringBuffer.append("Minimum degree ");
            stringBuffer.append(i);
            stringBuffer.append(" is greater than maximum degree ");
            stringBuffer.append(i2);
            z3 = true;
        } else if (i < ROLE_CARDINALITY_INFINITY || i2 < ROLE_CARDINALITY_INFINITY) {
            stringBuffer.append("Minimum or maximum degree has an illegal value, must be [0, ROLE_CARDINALITY_INFINITY].");
            z3 = true;
        }
        if (z3) {
            throw new InvalidRoleInfoException(stringBuffer.toString());
        }
        this.minDegree = i;
        this.maxDegree = i2;
        this.referencedMBeanClassName = str2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.name = (String) readFields.get("myName", (Object) null);
            if (readFields.defaulted("myName")) {
                throw new NullPointerException("myName");
            }
            this.isReadable = readFields.get("myIsReadableFlg", false);
            if (readFields.defaulted("myIsReadableFlg")) {
                throw new NullPointerException("myIsReadableFlg");
            }
            this.isWritable = readFields.get("myIsWritableFlg", false);
            if (readFields.defaulted("myIsWritableFlg")) {
                throw new NullPointerException("myIsWritableFlg");
            }
            this.description = (String) readFields.get("myDescription", (Object) null);
            if (readFields.defaulted("myDescription")) {
                throw new NullPointerException("myDescription");
            }
            this.minDegree = readFields.get("myMinDegree", 0);
            if (readFields.defaulted("myMinDegree")) {
                throw new NullPointerException("myMinDegree");
            }
            this.maxDegree = readFields.get("myMaxDegree", 0);
            if (readFields.defaulted("myMaxDegree")) {
                throw new NullPointerException("myMaxDegree");
            }
            this.referencedMBeanClassName = (String) readFields.get("myRefMBeanClassName", (Object) null);
            if (readFields.defaulted("myRefMBeanClassName")) {
                throw new NullPointerException("myRefMBeanClassName");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("myName", this.name);
            putFields.put("myIsReadableFlg", this.isReadable);
            putFields.put("myIsWritableFlg", this.isWritable);
            putFields.put("myDescription", this.description);
            putFields.put("myMinDegree", this.minDegree);
            putFields.put("myMaxDegree", this.maxDegree);
            putFields.put("myRefMBeanClassName", this.referencedMBeanClassName);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
