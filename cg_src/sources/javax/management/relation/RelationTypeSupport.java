package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationTypeSupport.class */
public class RelationTypeSupport implements RelationType {
    private static final long oldSerialVersionUID = -8179019472410837190L;
    private static final long newSerialVersionUID = 4611072955724144607L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String typeName;
    private Map roleName2InfoMap;
    private boolean isInRelationService;
    private static String localClassName;
    static Class class$java$lang$String;
    static Class class$java$util$HashMap;
    static Class class$java$util$Map;

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
        objectStreamFieldArr[0] = new ObjectStreamField("myTypeName", cls);
        if (class$java$util$HashMap == null) {
            cls2 = class$("java.util.HashMap");
            class$java$util$HashMap = cls2;
        } else {
            cls2 = class$java$util$HashMap;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("myRoleName2InfoMap", cls2);
        objectStreamFieldArr[2] = new ObjectStreamField("myIsInRelServFlg", Boolean.TYPE);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[3];
        if (class$java$lang$String == null) {
            cls3 = class$("java.lang.String");
            class$java$lang$String = cls3;
        } else {
            cls3 = class$java$lang$String;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("typeName", cls3);
        if (class$java$util$Map == null) {
            cls4 = class$("java.util.Map");
            class$java$util$Map = cls4;
        } else {
            cls4 = class$java$util$Map;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("roleName2InfoMap", cls4);
        objectStreamFieldArr2[2] = new ObjectStreamField("isInRelationService", Boolean.TYPE);
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
        localClassName = "RelationTypeSupport";
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public RelationTypeSupport(String str, RoleInfo[] roleInfoArr) throws IllegalArgumentException, InvalidRelationTypeException {
        this.typeName = null;
        this.roleName2InfoMap = new HashMap();
        this.isInRelationService = false;
        if (str == null || roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("Constructor: entering", str);
        }
        initMembers(str, roleInfoArr);
        if (isTraceOn()) {
            trace("Constructor: exiting", null);
        }
    }

    protected RelationTypeSupport(String str) {
        this.typeName = null;
        this.roleName2InfoMap = new HashMap();
        this.isInRelationService = false;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("Protected constructor: entering", str);
        }
        this.typeName = str;
        if (isTraceOn()) {
            trace("Protected constructor: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationType
    public String getRelationTypeName() {
        return this.typeName;
    }

    @Override // javax.management.relation.RelationType
    public List getRoleInfos() {
        return new ArrayList(this.roleName2InfoMap.values());
    }

    @Override // javax.management.relation.RelationType
    public RoleInfo getRoleInfo(String str) throws IllegalArgumentException, RoleInfoNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoleInfo: entering", str);
        }
        RoleInfo roleInfo = (RoleInfo) this.roleName2InfoMap.get(str);
        if (roleInfo == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("No role info for role ");
            stringBuffer.append(str);
            throw new RoleInfoNotFoundException(stringBuffer.toString());
        }
        if (isTraceOn()) {
            trace("getRoleInfo: exiting", null);
        }
        return roleInfo;
    }

    protected void addRoleInfo(RoleInfo roleInfo) throws IllegalArgumentException, InvalidRelationTypeException {
        if (roleInfo == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("addRoleInfo: entering", roleInfo.toString());
        }
        if (this.isInRelationService) {
            throw new RuntimeException("Relation type cannot be updated as it is declared in the Relation Service.");
        }
        String name = roleInfo.getName();
        if (this.roleName2InfoMap.containsKey(name)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Two role infos provided for role ");
            stringBuffer.append(name);
            throw new InvalidRelationTypeException(stringBuffer.toString());
        }
        this.roleName2InfoMap.put(name, new RoleInfo(roleInfo));
        if (isDebugOn()) {
            debug("addRoleInfo: exiting", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRelationServiceFlag(boolean z) {
        this.isInRelationService = z;
    }

    private void initMembers(String str, RoleInfo[] roleInfoArr) throws IllegalArgumentException, InvalidRelationTypeException {
        if (str == null || roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("initMembers: entering", str);
        }
        this.typeName = str;
        checkRoleInfos(roleInfoArr);
        for (RoleInfo roleInfo : roleInfoArr) {
            this.roleName2InfoMap.put(new String(roleInfo.getName()), new RoleInfo(roleInfo));
        }
        if (isDebugOn()) {
            debug("initMembers: exiting", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkRoleInfos(RoleInfo[] roleInfoArr) throws IllegalArgumentException, InvalidRelationTypeException {
        if (roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (roleInfoArr.length == 0) {
            throw new InvalidRelationTypeException("No role info provided.");
        }
        ArrayList arrayList = new ArrayList();
        for (RoleInfo roleInfo : roleInfoArr) {
            if (roleInfo == null) {
                throw new InvalidRelationTypeException("Null role info provided.");
            }
            String name = roleInfo.getName();
            if (arrayList.contains(name)) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Two role infos provided for role ");
                stringBuffer.append(name);
                throw new InvalidRelationTypeException(stringBuffer.toString());
            }
            arrayList.add(name);
        }
    }

    private boolean isTraceOn() {
        return Trace.isSelected(1, 64);
    }

    private void trace(String str, String str2) {
        Trace.send(1, 64, localClassName, str, str2);
        Trace.send(1, 64, "", "", "\n");
    }

    private boolean isDebugOn() {
        return Trace.isSelected(2, 64);
    }

    private void debug(String str, String str2) {
        Trace.send(2, 64, localClassName, str, str2);
        Trace.send(2, 64, "", "", "\n");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.typeName = (String) readFields.get("myTypeName", (Object) null);
            if (readFields.defaulted("myTypeName")) {
                throw new NullPointerException("myTypeName");
            }
            this.roleName2InfoMap = (Map) readFields.get("myRoleName2InfoMap", (Object) null);
            if (readFields.defaulted("myRoleName2InfoMap")) {
                throw new NullPointerException("myRoleName2InfoMap");
            }
            this.isInRelationService = readFields.get("myIsInRelServFlg", false);
            if (readFields.defaulted("myIsInRelServFlg")) {
                throw new NullPointerException("myIsInRelServFlg");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("myTypeName", this.typeName);
            putFields.put("myRoleName2InfoMap", (HashMap) this.roleName2InfoMap);
            putFields.put("myIsInRelServFlg", this.isInRelationService);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
