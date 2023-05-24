package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.management.Notification;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationNotification.class */
public class RelationNotification extends Notification {
    private static final long oldSerialVersionUID = -2126464566505527147L;
    private static final long newSerialVersionUID = -6871117877523310399L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    public static final String RELATION_BASIC_CREATION = "jmx.relation.creation.basic";
    public static final String RELATION_MBEAN_CREATION = "jmx.relation.creation.mbean";
    public static final String RELATION_BASIC_UPDATE = "jmx.relation.update.basic";
    public static final String RELATION_MBEAN_UPDATE = "jmx.relation.update.mbean";
    public static final String RELATION_BASIC_REMOVAL = "jmx.relation.removal.basic";
    public static final String RELATION_MBEAN_REMOVAL = "jmx.relation.removal.mbean";
    private String relationId;
    private String relationTypeName;
    private ObjectName relationObjName;
    private List unregisterMBeanList;
    private String roleName;
    private List oldRoleValue;
    private List newRoleValue;
    static Class class$java$util$ArrayList;
    static Class class$java$lang$String;
    static Class class$javax$management$ObjectName;
    static Class class$java$util$List;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Class cls10;
        Class cls11;
        Class cls12;
        Class cls13;
        Class cls14;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[7];
        if (class$java$util$ArrayList == null) {
            cls = class$("java.util.ArrayList");
            class$java$util$ArrayList = cls;
        } else {
            cls = class$java$util$ArrayList;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("myNewRoleValue", cls);
        if (class$java$util$ArrayList == null) {
            cls2 = class$("java.util.ArrayList");
            class$java$util$ArrayList = cls2;
        } else {
            cls2 = class$java$util$ArrayList;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("myOldRoleValue", cls2);
        if (class$java$lang$String == null) {
            cls3 = class$("java.lang.String");
            class$java$lang$String = cls3;
        } else {
            cls3 = class$java$lang$String;
        }
        objectStreamFieldArr[2] = new ObjectStreamField("myRelId", cls3);
        if (class$javax$management$ObjectName == null) {
            cls4 = class$("javax.management.ObjectName");
            class$javax$management$ObjectName = cls4;
        } else {
            cls4 = class$javax$management$ObjectName;
        }
        objectStreamFieldArr[3] = new ObjectStreamField("myRelObjName", cls4);
        if (class$java$lang$String == null) {
            cls5 = class$("java.lang.String");
            class$java$lang$String = cls5;
        } else {
            cls5 = class$java$lang$String;
        }
        objectStreamFieldArr[4] = new ObjectStreamField("myRelTypeName", cls5);
        if (class$java$lang$String == null) {
            cls6 = class$("java.lang.String");
            class$java$lang$String = cls6;
        } else {
            cls6 = class$java$lang$String;
        }
        objectStreamFieldArr[5] = new ObjectStreamField("myRoleName", cls6);
        if (class$java$util$ArrayList == null) {
            cls7 = class$("java.util.ArrayList");
            class$java$util$ArrayList = cls7;
        } else {
            cls7 = class$java$util$ArrayList;
        }
        objectStreamFieldArr[6] = new ObjectStreamField("myUnregMBeanList", cls7);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[7];
        if (class$java$util$List == null) {
            cls8 = class$("java.util.List");
            class$java$util$List = cls8;
        } else {
            cls8 = class$java$util$List;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("newRoleValue", cls8);
        if (class$java$util$List == null) {
            cls9 = class$("java.util.List");
            class$java$util$List = cls9;
        } else {
            cls9 = class$java$util$List;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("oldRoleValue", cls9);
        if (class$java$lang$String == null) {
            cls10 = class$("java.lang.String");
            class$java$lang$String = cls10;
        } else {
            cls10 = class$java$lang$String;
        }
        objectStreamFieldArr2[2] = new ObjectStreamField("relationId", cls10);
        if (class$javax$management$ObjectName == null) {
            cls11 = class$("javax.management.ObjectName");
            class$javax$management$ObjectName = cls11;
        } else {
            cls11 = class$javax$management$ObjectName;
        }
        objectStreamFieldArr2[3] = new ObjectStreamField("relationObjName", cls11);
        if (class$java$lang$String == null) {
            cls12 = class$("java.lang.String");
            class$java$lang$String = cls12;
        } else {
            cls12 = class$java$lang$String;
        }
        objectStreamFieldArr2[4] = new ObjectStreamField("relationTypeName", cls12);
        if (class$java$lang$String == null) {
            cls13 = class$("java.lang.String");
            class$java$lang$String = cls13;
        } else {
            cls13 = class$java$lang$String;
        }
        objectStreamFieldArr2[5] = new ObjectStreamField("roleName", cls13);
        if (class$java$util$List == null) {
            cls14 = class$("java.util.List");
            class$java$util$List = cls14;
        } else {
            cls14 = class$java$util$List;
        }
        objectStreamFieldArr2[6] = new ObjectStreamField("unregisterMBeanList", cls14);
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

    public RelationNotification(String str, Object obj, long j, long j2, String str2, String str3, String str4, ObjectName objectName, List list) throws IllegalArgumentException {
        super(str, obj, j, j2, str2);
        this.relationId = null;
        this.relationTypeName = null;
        this.relationObjName = null;
        this.unregisterMBeanList = null;
        this.roleName = null;
        this.oldRoleValue = null;
        this.newRoleValue = null;
        initMembers(1, str, obj, j, j2, str2, str3, str4, objectName, list, null, null, null);
    }

    public RelationNotification(String str, Object obj, long j, long j2, String str2, String str3, String str4, ObjectName objectName, String str5, List list, List list2) throws IllegalArgumentException {
        super(str, obj, j, j2, str2);
        this.relationId = null;
        this.relationTypeName = null;
        this.relationObjName = null;
        this.unregisterMBeanList = null;
        this.roleName = null;
        this.oldRoleValue = null;
        this.newRoleValue = null;
        initMembers(2, str, obj, j, j2, str2, str3, str4, objectName, null, str5, list, list2);
    }

    public String getRelationId() {
        return this.relationId;
    }

    public String getRelationTypeName() {
        return this.relationTypeName;
    }

    public ObjectName getObjectName() {
        return this.relationObjName;
    }

    public List getMBeansToUnregister() {
        List list;
        if (this.unregisterMBeanList != null) {
            list = (List) ((ArrayList) this.unregisterMBeanList).clone();
        } else {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }

    public String getRoleName() {
        String str = null;
        if (this.roleName != null) {
            str = this.roleName;
        }
        return str;
    }

    public List getOldRoleValue() {
        List list;
        if (this.oldRoleValue != null) {
            list = (List) ((ArrayList) this.oldRoleValue).clone();
        } else {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }

    public List getNewRoleValue() {
        List list;
        if (this.newRoleValue != null) {
            list = (List) ((ArrayList) this.newRoleValue).clone();
        } else {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }

    private void initMembers(int i, String str, Object obj, long j, long j2, String str2, String str3, String str4, ObjectName objectName, List list, String str5, List list2, List list3) throws IllegalArgumentException {
        boolean z = false;
        if (str == null || obj == null || !(obj instanceof RelationService) || str3 == null || str4 == null) {
            z = true;
        }
        if (i == 1) {
            if (!str.equals(RELATION_BASIC_CREATION) && !str.equals(RELATION_MBEAN_CREATION) && !str.equals(RELATION_BASIC_REMOVAL) && !str.equals(RELATION_MBEAN_REMOVAL)) {
                z = true;
            }
        } else if (i == 2 && ((!str.equals(RELATION_BASIC_UPDATE) && !str.equals(RELATION_MBEAN_UPDATE)) || str5 == null || list3 == null || list2 == null)) {
            z = true;
        }
        if (z) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.relationId = str3;
        this.relationTypeName = str4;
        this.relationObjName = objectName;
        if (list != null) {
            this.unregisterMBeanList = new ArrayList(list);
        }
        if (str5 != null) {
            this.roleName = str5;
        }
        if (list3 != null) {
            this.oldRoleValue = new ArrayList(list3);
        }
        if (list2 != null) {
            this.newRoleValue = new ArrayList(list2);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.newRoleValue = (List) readFields.get("myNewRoleValue", (Object) null);
            if (readFields.defaulted("myNewRoleValue")) {
                throw new NullPointerException("newRoleValue");
            }
            this.oldRoleValue = (List) readFields.get("myOldRoleValue", (Object) null);
            if (readFields.defaulted("myOldRoleValue")) {
                throw new NullPointerException("oldRoleValue");
            }
            this.relationId = (String) readFields.get("myRelId", (Object) null);
            if (readFields.defaulted("myRelId")) {
                throw new NullPointerException("relationId");
            }
            this.relationObjName = (ObjectName) readFields.get("myRelObjName", (Object) null);
            if (readFields.defaulted("myRelObjName")) {
                throw new NullPointerException("relationObjName");
            }
            this.relationTypeName = (String) readFields.get("myRelTypeName", (Object) null);
            if (readFields.defaulted("myRelTypeName")) {
                throw new NullPointerException("relationTypeName");
            }
            this.roleName = (String) readFields.get("myRoleName", (Object) null);
            if (readFields.defaulted("myRoleName")) {
                throw new NullPointerException("roleName");
            }
            this.unregisterMBeanList = (List) readFields.get("myUnregMBeanList", (Object) null);
            if (readFields.defaulted("myUnregMBeanList")) {
                throw new NullPointerException("unregisterMBeanList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("myNewRoleValue", this.newRoleValue);
            putFields.put("myOldRoleValue", this.oldRoleValue);
            putFields.put("myRelId", this.relationId);
            putFields.put("myRelObjName", this.relationObjName);
            putFields.put("myRelTypeName", this.relationTypeName);
            putFields.put("myRoleName", this.roleName);
            putFields.put("myUnregMBeanList", this.unregisterMBeanList);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
