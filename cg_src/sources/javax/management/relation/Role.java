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
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/Role.class */
public class Role implements Serializable {
    private static final long oldSerialVersionUID = -1959486389343113026L;
    private static final long newSerialVersionUID = -279985518429862552L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String name = null;
    private List objectNameList = new ArrayList();
    static Class class$java$lang$String;
    static Class class$java$util$ArrayList;
    static Class class$java$util$List;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[2];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("myName", cls);
        if (class$java$util$ArrayList == null) {
            cls2 = class$("java.util.ArrayList");
            class$java$util$ArrayList = cls2;
        } else {
            cls2 = class$java$util$ArrayList;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("myObjNameList", cls2);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[2];
        if (class$java$lang$String == null) {
            cls3 = class$("java.lang.String");
            class$java$lang$String = cls3;
        } else {
            cls3 = class$java$lang$String;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("name", cls3);
        if (class$java$util$List == null) {
            cls4 = class$("java.util.List");
            class$java$util$List = cls4;
        } else {
            cls4 = class$java$util$List;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("objectNameList", cls4);
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

    public Role(String str, List list) throws IllegalArgumentException {
        if (str == null || list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        setRoleName(str);
        setRoleValue(list);
    }

    public String getRoleName() {
        return this.name;
    }

    public List getRoleValue() {
        return this.objectNameList;
    }

    public void setRoleName(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.name = str;
    }

    public void setRoleValue(List list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.objectNameList = new ArrayList(list);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new StringBuffer().append("role name: ").append(this.name).append("; role value: ").toString());
        Iterator it = this.objectNameList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(((ObjectName) it.next()).toString());
            if (it.hasNext()) {
                stringBuffer.append(", ");
            }
        }
        return stringBuffer.toString();
    }

    public Object clone() {
        try {
            return new Role(this.name, this.objectNameList);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String roleValueToString(List list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            stringBuffer.append(((ObjectName) it.next()).toString());
            if (it.hasNext()) {
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.name = (String) readFields.get("myName", (Object) null);
            if (readFields.defaulted("myName")) {
                throw new NullPointerException("myName");
            }
            this.objectNameList = (List) readFields.get("myObjNameList", (Object) null);
            if (readFields.defaulted("myObjNameList")) {
                throw new NullPointerException("myObjNameList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("myName", this.name);
            putFields.put("myObjNameList", (ArrayList) this.objectNameList);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
