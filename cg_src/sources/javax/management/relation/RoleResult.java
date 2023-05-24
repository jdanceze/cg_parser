package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RoleResult.class */
public class RoleResult implements Serializable {
    private static final long oldSerialVersionUID = 3786616013762091099L;
    private static final long newSerialVersionUID = -6304063118040985512L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private RoleList roleList = null;
    private RoleUnresolvedList unresolvedRoleList = null;
    static Class class$javax$management$relation$RoleList;
    static Class class$javax$management$relation$RoleUnresolvedList;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[2];
        if (class$javax$management$relation$RoleList == null) {
            cls = class$("javax.management.relation.RoleList");
            class$javax$management$relation$RoleList = cls;
        } else {
            cls = class$javax$management$relation$RoleList;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("myRoleList", cls);
        if (class$javax$management$relation$RoleUnresolvedList == null) {
            cls2 = class$("javax.management.relation.RoleUnresolvedList");
            class$javax$management$relation$RoleUnresolvedList = cls2;
        } else {
            cls2 = class$javax$management$relation$RoleUnresolvedList;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("myRoleUnresList", cls2);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[2];
        if (class$javax$management$relation$RoleList == null) {
            cls3 = class$("javax.management.relation.RoleList");
            class$javax$management$relation$RoleList = cls3;
        } else {
            cls3 = class$javax$management$relation$RoleList;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("roleList", cls3);
        if (class$javax$management$relation$RoleUnresolvedList == null) {
            cls4 = class$("javax.management.relation.RoleUnresolvedList");
            class$javax$management$relation$RoleUnresolvedList = cls4;
        } else {
            cls4 = class$javax$management$relation$RoleUnresolvedList;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("unresolvedRoleList", cls4);
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

    public RoleResult(RoleList roleList, RoleUnresolvedList roleUnresolvedList) {
        setRoles(roleList);
        setRolesUnresolved(roleUnresolvedList);
    }

    public RoleList getRoles() {
        return this.roleList;
    }

    public RoleUnresolvedList getRolesUnresolved() {
        return this.unresolvedRoleList;
    }

    public void setRoles(RoleList roleList) {
        if (roleList != null) {
            this.roleList = new RoleList();
            Iterator it = roleList.iterator();
            while (it.hasNext()) {
                this.roleList.add((Role) ((Role) it.next()).clone());
            }
            return;
        }
        this.roleList = null;
    }

    public void setRolesUnresolved(RoleUnresolvedList roleUnresolvedList) {
        if (roleUnresolvedList != null) {
            this.unresolvedRoleList = new RoleUnresolvedList();
            Iterator it = roleUnresolvedList.iterator();
            while (it.hasNext()) {
                this.unresolvedRoleList.add((RoleUnresolved) ((RoleUnresolved) it.next()).clone());
            }
            return;
        }
        this.unresolvedRoleList = null;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.roleList = (RoleList) readFields.get("myRoleList", (Object) null);
            if (readFields.defaulted("myRoleList")) {
                throw new NullPointerException("myRoleList");
            }
            this.unresolvedRoleList = (RoleUnresolvedList) readFields.get("myRoleUnresList", (Object) null);
            if (readFields.defaulted("myRoleUnresList")) {
                throw new NullPointerException("myRoleUnresList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("myRoleList", this.roleList);
            putFields.put("myRoleUnresList", this.unresolvedRoleList);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
