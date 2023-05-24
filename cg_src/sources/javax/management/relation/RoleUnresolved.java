package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RoleUnresolved.class */
public class RoleUnresolved implements Serializable {
    private static final long oldSerialVersionUID = -9026457686611660144L;
    private static final long newSerialVersionUID = -48350262537070138L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String roleName = null;
    private List roleValue = null;
    private int problemType;
    static Class class$java$lang$String;
    static Class class$java$util$ArrayList;
    static Class class$java$util$List;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[3];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("myRoleName", cls);
        if (class$java$util$ArrayList == null) {
            cls2 = class$("java.util.ArrayList");
            class$java$util$ArrayList = cls2;
        } else {
            cls2 = class$java$util$ArrayList;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("myRoleValue", cls2);
        objectStreamFieldArr[2] = new ObjectStreamField("myPbType", Integer.TYPE);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[3];
        if (class$java$lang$String == null) {
            cls3 = class$("java.lang.String");
            class$java$lang$String = cls3;
        } else {
            cls3 = class$java$lang$String;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("roleName", cls3);
        if (class$java$util$List == null) {
            cls4 = class$("java.util.List");
            class$java$util$List = cls4;
        } else {
            cls4 = class$java$util$List;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("roleValue", cls4);
        objectStreamFieldArr2[2] = new ObjectStreamField("problemType", Integer.TYPE);
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
            return;
        }
        serialPersistentFields = newSerialPersistentFields;
        serialVersionUID = newSerialVersionUID;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public RoleUnresolved(String str, List list, int i) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        setRoleName(str);
        setRoleValue(list);
        setProblemType(i);
    }

    public String getRoleName() {
        return this.roleName;
    }

    public List getRoleValue() {
        return this.roleValue;
    }

    public int getProblemType() {
        return this.problemType;
    }

    public void setRoleName(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.roleName = str;
    }

    public void setRoleValue(List list) {
        if (list != null) {
            this.roleValue = new ArrayList(list);
        } else {
            this.roleValue = null;
        }
    }

    public void setProblemType(int i) throws IllegalArgumentException {
        if (!RoleStatus.isRoleStatus(i)) {
            throw new IllegalArgumentException("Incorrect problem type.");
        }
        this.problemType = i;
    }

    public Object clone() {
        try {
            return new RoleUnresolved(this.roleName, this.roleValue, this.problemType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new StringBuffer().append("role name: ").append(this.roleName).toString());
        if (this.roleValue != null) {
            stringBuffer.append("; value: ");
            Iterator it = this.roleValue.iterator();
            while (it.hasNext()) {
                stringBuffer.append(((ObjectName) it.next()).toString());
                if (it.hasNext()) {
                    stringBuffer.append(", ");
                }
            }
        }
        stringBuffer.append(new StringBuffer().append("; problem type: ").append(this.problemType).toString());
        return stringBuffer.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.roleName = (String) readFields.get("myRoleName", (Object) null);
            if (readFields.defaulted("myRoleName")) {
                throw new NullPointerException("myRoleName");
            }
            this.roleValue = (List) readFields.get("myRoleValue", (Object) null);
            if (readFields.defaulted("myRoleValue")) {
                throw new NullPointerException("myRoleValue");
            }
            this.problemType = readFields.get("myPbType", 0);
            if (readFields.defaulted("myPbType")) {
                throw new NullPointerException("myPbType");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("myRoleName", this.roleName);
            putFields.put("myRoleValue", (ArrayList) this.roleValue);
            putFields.put("myPbType", this.problemType);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
